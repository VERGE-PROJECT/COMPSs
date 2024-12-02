package es.bsc.compss.scheduler.custom.heuristics;

import es.bsc.compss.log.Loggers;
import es.bsc.compss.util.ErrorManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class Heuristics<N extends Number> {

    // Logger
    protected static final Logger LOGGER = LogManager.getLogger(Loggers.TS_COMP);

    // DAG containing c, z and succ
    protected DAG dag;

    // Resources contains ibw, mfs, h, overhead representing the resources used in the system
    protected Resources resources;

    // Computing units
    protected ArrayList<Integer> computingUnits;

    // Array containing scheduled tasks time per slot unit
    protected float[] lastScheduledEndTime;

    // Array containing the max release time (actual release time) of a task
    protected float[] maxReleaseCTime;

    // readyqueue automatically ordered by heuristic score
    protected PriorityQueue<HeuristicTask<N>> readyQueue = new PriorityQueue<>(new ReadyQueueElementComparator());

    protected float rub = 0.0f;

    protected int[] warmUpAllocation;

    // Array with the computed tasks
    protected boolean[] completed;

    private int[] absolutePosComputingUnits;

    // Position of the action in the computing unit, execution order
    // e.g. if a task a,b,c have to be executed in order in the same computing unit it will be assigned
    // a.pos = 0,b.pos = 1, c.pos = 2
    private int[] posTaskSlot;


    /**
     * Constructor of Heuristic class. Initialize all the attributes by reading the input file.
     */
    public Heuristics() {
        HeuristicsConfiguration.load(this);

        // Initialize maxReleaseCTime
        this.maxReleaseCTime = new float[this.dag.getN()];
        this.warmUpAllocation = new int[this.resources.getSlotUnits()];
        this.completed = new boolean[this.dag.getN()];

        this.absolutePosComputingUnits = new int[this.resources.getSlotUnits()];
        this.posTaskSlot = new int[this.resources.getSlotUnits()];
    }

    /**
     * Constructor of Heuristic class. Initialize all the attributes.
     * 
     * @param dag DAG object of the program to execute.
     * @param resources Resources object to plan the execution
     */
    public Heuristics(DAG dag, Resources resources) {
        this.dag = dag;
        this.resources = resources;

        // Initialize maxReleaseCTime
        this.maxReleaseCTime = new float[this.dag.getN()];
        this.warmUpAllocation = new int[this.resources.getSlotUnits()];
    }

    public int[] getPosTaskSlot() {
        return this.posTaskSlot;
    }

    protected float minLastScheduledEndTime() {
        float minScheduledEndTime = Float.MAX_VALUE;

        for (int i = 0; i < this.resources.getSlotUnits(); i++) {
            if (lastScheduledEndTime[i] < minScheduledEndTime) {
                minScheduledEndTime = lastScheduledEndTime[i];
            }
        }
        return minScheduledEndTime;
    }

    public void setLastScheduledEndTime(float[] lastScheduledEndTime) {
        this.lastScheduledEndTime = lastScheduledEndTime;
    }

    public Resources getResources() {
        return this.resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public DAG getDag() {
        return this.dag;
    }

    public void setDag(DAG dag) {
        this.dag = dag;
    }

    public float getRub() {
        return this.rub;
    }

    /**
     * TaskScheduling selects the task to be executed if releaseTime is completed if not select the one with the least
     * releaseTime. It also remove from readyqueue the element selected.
     * 
     * @return task selected as best to execute.
     */
    public HeuristicTask<N> taskScheduling() {
        float lastScheduledEndTime = this.minLastScheduledEndTime();
        float minRT = Float.MAX_VALUE;
        HeuristicTask<N> selectedElement = null;

        LOGGER.debug("Printing readyqueue");

        for (HeuristicTask<N> currentElement : readyQueue) {
            LOGGER.debug(currentElement.getTaskId());
            float currentRT = currentElement.getReleaseTime();
            float currentCTime = this.dag.getC(currentElement.getTaskId(), this.resources.getWorker(0));

            if (lastScheduledEndTime >= currentRT) {
                LOGGER.debug("El lastScheduled es " + lastScheduledEndTime + " y es mayor que el RT " + currentRT);
                if ((currentRT + currentCTime) < minRT) {
                    LOGGER.debug("Ademas es min");
                    minRT = currentRT + currentCTime;
                    selectedElement = currentElement;
                }
            }
        }

        if (selectedElement != null) {
            readyQueue.remove(selectedElement);
            return selectedElement;
        }

        return readyQueue.poll();
    }

    /**
     * This function receives the task and get the corresponding score depending on the heuristic. Override in each
     * heuristic class.
     * 
     * @param task task to get the score.
     * @return score of the given task.
     */
    protected abstract N getHeuristicScore(int task);

    /**
     * Function that returns the first score of HeuristicTask, @Override in each heuristic class.
     * 
     * @param sourceId task to get the score.
     * @return score of the source task.
     */
    protected abstract N getFirstScore(int sourceId);

    /**
     * ReleaseTasks releases the dependencies and add them to the priorityqueue if all predecessors finished.
     * 
     * @param selectedElement Task computed.
     */
    protected void releaseTasks(HeuristicTask<N> selectedElement) {
        boolean[][] succ = this.dag.getSucc();
        int selectedResource = selectedElement.getAssignedResourceNumber();
        int readyTask = selectedElement.getTaskId();
        float releaseCTimeCurr =
            this.dag.getC(readyTask, this.resources.getWorker(selectedResource)) + selectedElement.getReleaseTime();

        // Set the task computed as completed
        this.completed[readyTask] = true;

        int n = this.dag.getN();

        for (int s = 0; s < n; ++s) {
            if (succ[readyTask][s]) {
                boolean ready = true;
                for (int pred = 0; (pred < s) && ready; ++pred) {
                    if (succ[pred][s] && !this.completed[pred]) {
                        ready = false;
                    }
                }

                if (releaseCTimeCurr > this.maxReleaseCTime[s]) {
                    this.maxReleaseCTime[s] = releaseCTimeCurr;
                }

                if (ready) {
                    HeuristicTask<N> rqe = new HeuristicTask<N>(s, this.maxReleaseCTime[s], this.getHeuristicScore(s),
                        this.dag.checkSyncTask(s), this.dag.checkWarmUpTask(s));
                    rqe.addToQueue(readyQueue);
                }
            }
        }
    }

    /**
     * Generates a static plan outlining the execution strategy of the application. This function takes into account the
     * current configuration and state of the application to create a detailed plan for its execution. The plan includes
     * steps and procedures that should be followed to ensure efficient and correct operation of the application.
     */
    public void schedule(HashMap<Integer, HeuristicTask<? extends Number>> scheduledTasks) {
        this.computingUnits = new ArrayList<>(Collections.nCopies(this.dag.getN(), 0));

        // 1st task to the readyQueue (id=0, releaseTime=0.0f, synctask=true, warmUpTask=false)
        readyQueue.offer(new HeuristicTask<N>(0, 0.0f, this.getFirstScore(0), true, false));

        int taskId;

        // Heuristic Task object
        HeuristicTask<N> ht;
        float executionTimeTask;

        while (!readyQueue.isEmpty()) {
            ht = taskScheduling(); // Selects the task to be computed (object)

            // Find the best possible resource for the task selected
            resourceSelection(ht, scheduledTasks);

            taskId = ht.getTaskId();
            computingUnits.set(taskId, ht.getAssignedComputingUnit());

            if (!ht.getSyncTask()) {
                scheduledTasks.put(taskId, ht); // Add already scheduled task to
            }

            // Update rub
            executionTimeTask = this.dag.getC(taskId, ht.getAssignedResource());
            this.rub = Math.max(this.rub, executionTimeTask);

            // update readyqueue and freedependencies for next level tasks
            releaseTasks(ht);
        }
    }


    protected class ReadyQueueElementComparator<N extends Number> implements Comparator<HeuristicTask<N>> {

        @Override
        public int compare(HeuristicTask<N> st1, HeuristicTask<N> st2) {

            int returnValue = -Float.compare(st1.getScore().floatValue(), st2.getScore().floatValue());
            // Larger number (x>y) if it is true return -1

            if (returnValue == 0) {
                returnValue = Integer.compare(st1.getTaskId(), st2.getTaskId());
            }
            return returnValue;
        }
    }


    /**
     * Finds the best resource computing node to attach to candidated task.
     * 
     * @param rqe task to attach.
     */
    protected void resourceSelection(HeuristicTask<N> rqe,
        HashMap<Integer, HeuristicTask<? extends Number>> scheduledTasks) {

        int resource = 0;
        int totalResources = this.resources.getM();
        float[] transfers = new float[totalResources];
        int[] computingUnitSelected = new int[totalResources];
        boolean[][] succ = this.dag.getSucc();
        Map<String, Map<String, Float>> ibw = this.resources.getIBW();
        float[][] z = this.dag.getZ();
        Map<String, float[]> c = this.dag.getC();
        String workerName;

        float totalTime;
        int totalTimePos = 0;
        float teoricScheduledTime = 0;

        if (!rqe.getSyncTask()) {
            // NOT sync task
            if (!rqe.getWarmUpTask()) {
                // Not warm task
                for (int idxRes = 0; idxRes < totalResources; idxRes++) {
                    workerName = this.resources.getWorker(idxRes);
                    float maxTransferPredecessors = 0.0f;

                    int taskId = rqe.getTaskId();

                    for (int pred = 0; pred < taskId; pred++) {
                        if (succ[pred][taskId]) {
                            if (scheduledTasks.get(pred).getAssignedResourceNumber() != idxRes) {
                                // search for the max transferTime if multiple predecesssor
                                float currentTransfer =
                                    (float) ((Math.ceil(((z[pred][taskId] * 8) / this.resources.getMFS() * 8))
                                        * this.resources.getH() * 8 + z[pred][taskId] * 8)
                                        / (ibw.get(scheduledTasks.get(pred).getAssignedResource()).get(workerName)
                                            * 1_000_000));

                                maxTransferPredecessors = Math.max(currentTransfer, maxTransferPredecessors);

                            }
                        }
                    }
                    transfers[idxRes] = maxTransferPredecessors;
                }

                int prevTotalUnits = 0;
                float min = Float.MAX_VALUE; // min totalTime
                for (int idxRes = 0; idxRes < totalResources; idxRes++) {
                    int currentTotalUnits = this.resources.getComputingUnits(this.resources.getWorker(idxRes));
                    for (int compUnit = 0; compUnit < currentTotalUnits; compUnit++) {
                        totalTime = this.lastScheduledEndTime[prevTotalUnits + compUnit] + transfers[idxRes]
                            + c.get(this.resources.getWorker(idxRes))[rqe.getTaskId()];

                        LOGGER.debug("id: " + rqe.getTaskId() + " transferTime " + transfers[idxRes] + " c time "
                            + c.get(this.resources.getWorker(idxRes))[rqe.getTaskId()] + " totalTime " + totalTime
                            + " for the cu" + compUnit + " in the worker" + idxRes + " lastscheduled "
                            + this.lastScheduledEndTime[prevTotalUnits + compUnit]);

                        if (totalTime < min) {
                            min = totalTime;
                            resource = idxRes;
                            totalTimePos = prevTotalUnits + compUnit;
                            computingUnitSelected[idxRes] = compUnit;
                        }
                    }
                    prevTotalUnits += currentTotalUnits;
                }
                teoricScheduledTime = transfers[resource] + c.get(this.resources.getWorker(resource))[rqe.getTaskId()];

                if (this.lastScheduledEndTime[totalTimePos] < rqe.getReleaseTime()) {
                    this.lastScheduledEndTime[totalTimePos] = transfers[resource]
                        + c.get(this.resources.getWorker(resource))[rqe.getTaskId()] + rqe.getReleaseTime();
                } else {
                    this.lastScheduledEndTime[totalTimePos] +=
                        (transfers[resource] + c.get(this.resources.getWorker(resource))[rqe.getTaskId()]);
                }
            } else {
                // Warm Up Task
                boolean assigned = false;
                int prevTotalUnits = 0;
                for (int idxRes = 0; idxRes < totalResources && !assigned; idxRes++) {
                    int currentTotalUnits = this.resources.getComputingUnits(this.resources.getWorker(idxRes));
                    for (int compUnit = 0; compUnit < currentTotalUnits && !assigned; compUnit++) {
                        if (this.warmUpAllocation[prevTotalUnits + compUnit] == 0) {
                            resource = idxRes;
                            computingUnitSelected[resource] = compUnit;
                            totalTimePos = prevTotalUnits + compUnit;
                            this.warmUpAllocation[prevTotalUnits + compUnit] = 1;
                            assigned = true;
                        }
                    }
                    prevTotalUnits += currentTotalUnits;
                }
                teoricScheduledTime = c.get(this.resources.getWorker(resource))[rqe.getTaskId()];
            }

            // Update the position of the task in the computing unit
            rqe.setPosOrderSlotUnit(this.posTaskSlot[totalTimePos]);
            LOGGER.debug("RS LA POS ACTION ES " + rqe.getPosOrderSlotUnit() + " total time pos " + totalTimePos
                + " posTaskSlot " + this.posTaskSlot[totalTimePos]);
            this.posTaskSlot[totalTimePos]++;
        } else {
            // Sync task
            resource = 0;
            computingUnitSelected[resource] = 0;
            totalTimePos = 0;
        }

        /*
         * if (rqe.getTaskId() == 240 || rqe.getTaskId() == 239 || rqe.getTaskId() == 282) { resource = 1;
         * computingUnitSelected[resource] = 0; totalTimePos = 4; }
         */

        // Time of last scheduled task
        LOGGER.debug("SELECCIONO EL RESOURCE : " + resource + " Y SU COMPUNIT " + computingUnitSelected[resource]
            + " PARA LA TASK ID: " + rqe.getTaskId() + " Y CON TOTALTIMEPOS " + totalTimePos + " MY RELEASETIME IS: "
            + rqe.getReleaseTime() + " SOY SYNC NODE " + rqe.getSyncTask());

        // Assign the selections to the HeuristicTask
        rqe.assignResource(this.resources.getWorker(resource), resource); // Resource name assigned
        rqe.setAssignedComputingUnit(computingUnitSelected[resource]); // Core assigned
        rqe.setTeoricScheduledTime(teoricScheduledTime); // c + transfer time

        // int tempPosition = this.absolutePosComputingUnits[totalTimePos];
        rqe.setAbsolutePosition(totalTimePos); // AbsolutePositions in the computing unit
        // this.absolutePosComputingUnits[totalTimePos] = tempPosition + 1;
    }
}