/*         
 *  Copyright 2002-2018 Barcelona Supercomputing Center (www.bsc.es)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package es.bsc.compss.scheduler.custom.rtheuristics;

import es.bsc.compss.components.impl.ResourceScheduler;
import es.bsc.compss.components.impl.TaskScheduler;

import es.bsc.compss.scheduler.custom.heuristics.DAG;
import es.bsc.compss.scheduler.custom.heuristics.HeuristicTask;
import es.bsc.compss.scheduler.custom.heuristics.Heuristics;
import es.bsc.compss.scheduler.custom.heuristics.HeuristicsConfiguration;
import es.bsc.compss.scheduler.custom.heuristics.LNS;
import es.bsc.compss.scheduler.custom.heuristics.LNSNL;
import es.bsc.compss.scheduler.custom.heuristics.LPT;
import es.bsc.compss.scheduler.custom.heuristics.Resources;
import es.bsc.compss.scheduler.custom.heuristics.SPT;

import es.bsc.compss.scheduler.exceptions.ActionNotFoundException;
import es.bsc.compss.scheduler.exceptions.BlockedActionException;
import es.bsc.compss.scheduler.exceptions.InvalidSchedulingException;
import es.bsc.compss.scheduler.exceptions.UnassignedActionException;

import es.bsc.compss.scheduler.types.ActionOrchestrator;
import es.bsc.compss.scheduler.types.AllocatableAction;
import es.bsc.compss.scheduler.types.Score;
import es.bsc.compss.types.Task;
import es.bsc.compss.types.TaskDescription;
import es.bsc.compss.types.TaskState;
import es.bsc.compss.types.allocatableactions.ExecutionAction;
import es.bsc.compss.types.allocatableactions.TransferValueAction;
import es.bsc.compss.types.annotations.parameter.Direction;
import es.bsc.compss.types.parameter.Parameter;
import es.bsc.compss.types.parameter.impl.DependencyParameter;
import es.bsc.compss.types.resources.CloudMethodWorker;
import es.bsc.compss.types.resources.MethodWorker;
import es.bsc.compss.types.resources.Worker;
import es.bsc.compss.types.resources.WorkerResourceDescription;
import es.bsc.compss.util.ErrorManager;
import es.bsc.compss.util.ResourceManager;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.json.JSONObject;


/**
 * Main Scheduler class to run the execution with the Heuristics and Prometheus implementation.
 */
public class RTHeuristicsScheduler extends TaskScheduler {

    private long totalTime = 0;

    // List containing the BlockingActions
    private List<List<BlockingAction>> blockingActions;

    // List containing the Allocatable Actions already scheduled by task scheduler received
    private List<List<AllocatableAction>> scheduledActions;

    private static Gauge respUB =
        Gauge.build().name("Rub").help("Response time upper bound (Rub) for each iteration").register();

    private static Map<String, Gauge> workerMetrics = new HashMap<>();

    // indicates the number of tasks that the workflow to be executed will contain
    private int numTasks;

    // push gateway object to access the http exporter
    private PushGateway pg;

    private CollectorRegistry registry;

    private Heuristics<? extends Number> heuristic;

    private List<AllocatableAction> unassignedActions;

    // Gauge counting the number of missed deadlines present in a workflow
    private Gauge deadlines;

    private List<AbstractMap.SimpleEntry<String, String>> cloudWorkers;

    private boolean noWorkers;
    private boolean newWorkers;

    // updated when generatedResourceScheduler executed
    private int activeNumWorkers;

    // used to count if the number of workers that are up and running matches activeNumWorkers
    private int count;

    private long timeSpentAtt;
    private long transferTime;
    private HashMap<Integer, HeuristicTask<? extends Number>> heuristicsTasks;

    private boolean initComputationTime = false;


    /**
     * Constructs a new RTHeuristics Scheduler instance.
     */
    public RTHeuristicsScheduler() {
        super();
        RTHeuristicsConfiguration.load();
        this.unassignedActions = new ArrayList<>();
        this.scheduledActions = new LinkedList<>();
        this.cloudWorkers = new ArrayList<>();
        this.blockingActions = new LinkedList<>();
        this.heuristicsTasks = new HashMap<>();

        noWorkers = true;
        newWorkers = false;
        this.activeNumWorkers = this.count = 0;
        timeSpentAtt = 0;
        transferTime = 0;

        noWorkers = false;

        // Initialize and calculate the teoric scheduling based on heuristics
        initializeHeuristics();
    }

    private void initializeHeuristics() {
        selectHeuristic();

        cloudWorkers.clear();
        this.numTasks = this.heuristic.getDag().getExecutableTasks();

        // this.schedulerAllocation = new int[this.numTasks];

        long startTime = System.nanoTime();

        this.heuristic.schedule(this.heuristicsTasks);

        LOGGER.debug("Scheduling spent time is " + (System.nanoTime() - startTime));

        initializeActionStructures();

        newWorkers = false;
    }

    /**
     * This function creates the structure of the scheduledActions (AllocatableAction) and blockingActions
     * (blockingAction).
     */
    private void initializeActionStructures() {
        int[] posTaskSlot = this.heuristic.getPosTaskSlot();
        int length = posTaskSlot.length;

        for (int i = 0; i < length; i++) {
            this.scheduledActions.add(new LinkedList<>());
            this.blockingActions.add(new LinkedList<>());

            int taskCount = posTaskSlot[i];

            // Reserve space for tasks
            for (int j = 0; j < taskCount; j++) {
                this.blockingActions.get(i).add(new BlockingAction());
                this.scheduledActions.get(i).add(null);
            }
        }

        this.respUB.set(this.heuristic.getRub() / 1000.0f);
    }

    private void selectHeuristic() {
        switch (RTHeuristicsConfiguration.HEURISTICS_SELECTED) {
            case "spt":
                heuristic = new SPT();
                break;
            case "lns":
                heuristic = new LNS();
                break;
            case "lpt":
                heuristic = new LPT();
                break;
            default:
                heuristic = new LNSNL();
        }
    }

    public void setInitComputationTime(boolean value) {
        this.initComputationTime = value;
    }

    public boolean getInitComputationTime() {
        return this.initComputationTime;
    }

    private void setUpPrometheusConnection() throws Exception {
        pg = new PushGateway(RTHeuristicsConfiguration.PROMETHEUS_ENDPOINT);
        this.registry = new CollectorRegistry();
        this.registry.register(this.deadlines);
        String job = "compss";
        pg.pushAdd(this.registry, job);
    }

    /*
     * *********************************************************************************************************
     * *********************************************************************************************************
     * ***************************** UPDATE STRUCTURES OPERATIONS **********************************************
     * *********************************************************************************************************
     * *********************************************************************************************************
     */
    @Override
    public <T extends WorkerResourceDescription> RTHeuristicsResourceScheduler<T>
        generateSchedulerForResource(Worker<T> w, JSONObject resJSON, JSONObject implJSON) {
        LOGGER.debug("[RTHeuristicsScheduler] Generate scheduler for resource " + w.getName());

        if (RTHeuristicsConfiguration.PROMETHEUS_ACTIVE) {
            if (((MethodWorker) w).getName() != null) {
                LOGGER.debug(
                    "[RTHeuristicScheduler] Generate scheduler for cloud resource " + ((MethodWorker) w).getName());
                initializeCounter(((MethodWorker) w).getName());
            } else { // normal workers
                LOGGER.debug("[RTHeuristicScheduler] Generate scheduler for regular resource " + w.getName());
                initializeCounter(w.getName());
            }
        }

        if (w instanceof CloudMethodWorker) {
            LOGGER.debug("[RTHeuristicScheduler] CLOUD METHOD WORKER " + w.getName());
            cloudWorkers
                .add(new AbstractMap.SimpleEntry<>(w.getName(), ((CloudMethodWorker) w).getProvider().getName()));
        }
        newWorkers = true;
        this.activeNumWorkers++;
        return new RTHeuristicsResourceScheduler<>(w, resJSON, implJSON, this);
    }

    /**
     * TODO: Explain what is newWorkersAlreadyUp.
     */
    public void newWorkersAlreadyUp() {
        count++;
        if (this.activeNumWorkers == count) {
            noWorkers = false;
            newWorkers = true;
        }
    }

    @Override
    public Score generateActionScore(AllocatableAction action) {
        LOGGER.debug("[RTHeuristicScheduler] Generate Action Score for " + action);
        return new Score(action.getPriority(), 0, 0, 0, 0);
    }

    public long getTransferTime() {
        return this.transferTime;
    }

    @Override
    public <T extends WorkerResourceDescription> EnhancedSchedulingInformation generateSchedulingInformation(
        ResourceScheduler<T> enforcedTargetResource, List<? extends Parameter> params, Integer coreId) {
        return new EnhancedSchedulingInformation(enforcedTargetResource);
    }

    /*
     * *********************************************************************************************************
     * *********************************************************************************************************
     * ********************************* SCHEDULING OPERATIONS *************************************************
     * *********************************************************************************************************
     * *********************************************************************************************************
     */

    /**
     * Print tasks order and resource for each.
     */
    /*
     * public void printOrder() { StringBuilder taskOrder = new StringBuilder();
     * 
     * for (HeuristicTask<? extends Number> task: this.heuristic.getScheduledTasks()) {
     * taskOrder.append(task.getTaskId()).append(", "); }
     * 
     * LOGGER.debug(taskOrder.length() > 0 ? taskOrder.substring(0, taskOrder.length() - 2) : "");
     * 
     * StringBuilder resourcePerTask = new StringBuilder();
     * 
     * for (HeuristicTask<? extends Number> task: this.heuristic.getScheduledTasks()) {
     * resourcePerTask.append(task.getAssignedResource()).append(", "); }
     * 
     * LOGGER.debug(resourcePerTask.length() > 0 ? resourcePerTask.substring(0, resourcePerTask.length() - 2) : ""); }
     */

    public HashMap<Integer, HeuristicTask<? extends Number>> getScheduledTasks() {
        return this.heuristicsTasks;
    }

    private void initializeCounter(String id) {
        if (this.deadlines == null) {
            System.out.println("Initializing metrics with name deadlines_missed_" + id);
            this.deadlines =
                Gauge.build().name("deadlines_missed_" + id).help("Total deadlines missed per workflow").register();
            // Gauge.build().name("deadlines_missed_" + id).help("Total deadlines missed per workflow").register();

            try {
                setUpPrometheusConnection();
            } catch (Exception e) {
                LOGGER.warn("[RTHeuristicScheduler] Error while connecting to PushGateway");
                LOGGER.warn(e);
            }
        }
    }

    /**
     * Increments the task counter.
     */
    public void incrementTaskCounter() {
        this.deadlines.inc();
        updateRegistry();
    }

    /**
     * Resets the task counter.
     */
    public void resetTaskCounter() {
        this.deadlines.set(0);
        updateRegistry();
    }

    /**
     * TODO: Explain what is updateRegistry.
     */
    public void updateRegistry() {
        String job = "compss";
        try {
            pg.pushAdd(registry, job);
        } catch (Exception e) {
            // NOTHING TO DO
        }
    }

    public int getActiveNumWorkers() {
        return this.activeNumWorkers;
    }

    @Override
    protected void scheduleAction(AllocatableAction action, Score actionScore) throws BlockedActionException {
        LOGGER.debug("[RTHeuristicScheduler] Scheduling action " + action);
        if (!noWorkers) {
            int id = ((ExecutionAction) action).getTask().getId();
            String name = this.heuristicsTasks.get(id).getAssignedResource();

            addResolveBlockingActions(action);
            // advanceTransfersFinal(action);

            try {
                action.schedule(workers.get(ResourceManager.getWorker(name)), actionScore);
            } catch (UnassignedActionException uae) {
                LOGGER.warn("[RTHeuristicScheduler] Action " + action + " is unassigned");
                this.unassignedActions.add(action);
            }
        } else {
            LOGGER.debug("[RTHeuristicScheduler] Action " + action + " unassigned due to no workers available.");
            this.unassignedActions.add(action);
        }
    }

    /**
     * Add BlockingAction per each of the tasks received and resolve unresolved dependencies.
     * 
     * @param action Action received in the scheduleAction.
     */
    private void addResolveBlockingActions(AllocatableAction action) {
        final int id = ((ExecutionAction) action).getTask().getId();
        final String name = this.heuristicsTasks.get(id).getAssignedResource();
        final int posAction = this.heuristicsTasks.get(id).getPosOrderSlotUnit();
        final int compUnit = this.heuristicsTasks.get(id).getAssignedComputingUnit();
        final int absPos = this.heuristicsTasks.get(id).getAbsolutePosition();

        if (posAction != 0) { // Attach BlockingAction to the action if it is not the first task
            // to be computed in the computing unit

            BlockingAction blockingAction = this.blockingActions.get(absPos).get(posAction);

            ((EnhancedSchedulingInformation) blockingAction.getSchedulingInfo()).addSuccessor(action);
            ((EnhancedSchedulingInformation) action.getSchedulingInfo()).addPredecessor(blockingAction);

            // TODO: Verify if necessary
            ((EnhancedSchedulingInformation) action.getSchedulingInfo()).setHasOpAction(true);
            ((EnhancedSchedulingInformation) action.getSchedulingInfo()).incCountOpAction();
        }

        // Add to the compunit list the AllocatableAction (COMPSs task)
        this.scheduledActions.get(absPos).set(posAction, action);

        // BEGIN: Analizing the successors to resolve the dependencies
        AllocatableAction curr = action;
        int pos = posAction;

        while (pos < this.heuristic.getPosTaskSlot()[absPos] - 1) {
            AllocatableAction succ = this.scheduledActions.get(absPos).get(pos + 1);

            if (succ != null && curr != null) {
                BlockingAction blockingActionSucc = this.blockingActions.get(absPos).get(pos + 1);

                if (blockingActionSucc != null) {
                    // Assigning dependencies from current to successor (updating links)
                    ((EnhancedSchedulingInformation) curr.getSchedulingInfo()).addSuccessor(succ);
                    ((EnhancedSchedulingInformation) succ.getSchedulingInfo()).addPredecessor(curr);

                    ((EnhancedSchedulingInformation) succ.getSchedulingInfo()).decCountOpAction();
                    if (((EnhancedSchedulingInformation) succ.getSchedulingInfo()).getCountOpAction() == 0) {
                        ((EnhancedSchedulingInformation) succ.getSchedulingInfo()).setHasOpAction(false);
                    }

                    // Removing blockingActions attached on the successor AllocatableAction
                    ((EnhancedSchedulingInformation) blockingActionSucc.getSchedulingInfo()).removeSuccessor(succ);
                    ((EnhancedSchedulingInformation) succ.getSchedulingInfo()).removePredecessor(blockingActionSucc);
                    this.blockingActions.get(absPos).set(pos + 1, null);
                }
            }
            curr = succ;
            pos++;
        }
        // END: Analizing the successors to resolve the dependencies

        // BEGIN: Analizing the predecessors to resolve the dependencies
        curr = action; // reset the variable curr
        pos = posAction; // reset the variable pos

        while (pos > 0) {
            AllocatableAction pred = this.scheduledActions.get(absPos).get(pos - 1);

            if (pred != null && curr != null) {
                Task predTask = ((ExecutionAction) pred).getTask();
                BlockingAction blockingActionCurr = this.blockingActions.get(absPos).get(pos);

                if (predTask.getStatus() == TaskState.FINISHED) { // Previous tasks finished, no further check
                    if (blockingActionCurr != null) {
                        // Removing blockingActions attach on the current AllocatableAction
                        ((EnhancedSchedulingInformation) blockingActionCurr.getSchedulingInfo()).removeSuccessor(curr);
                        ((EnhancedSchedulingInformation) curr.getSchedulingInfo())
                            .removePredecessor(blockingActionCurr);
                        this.blockingActions.get(absPos).set(pos, null);
                    }
                    break;
                }

                if (blockingActionCurr != null) {
                    // Assigning dependencies from predecessor to current (updating links)
                    ((EnhancedSchedulingInformation) pred.getSchedulingInfo()).addSuccessor(curr);
                    ((EnhancedSchedulingInformation) curr.getSchedulingInfo()).addPredecessor(pred);

                    ((EnhancedSchedulingInformation) curr.getSchedulingInfo()).decCountOpAction();
                    if (((EnhancedSchedulingInformation) curr.getSchedulingInfo()).getCountOpAction() == 0) {
                        ((EnhancedSchedulingInformation) curr.getSchedulingInfo()).setHasOpAction(false);
                    }

                    // Removing blockingActions attached on the current AllocatableAction
                    ((EnhancedSchedulingInformation) blockingActionCurr.getSchedulingInfo()).removeSuccessor(curr);
                    ((EnhancedSchedulingInformation) curr.getSchedulingInfo()).removePredecessor(blockingActionCurr);
                    this.blockingActions.get(absPos).set(pos, null);
                }
            }
            curr = pred;
            pos--;
        }
        // END: Analizing the predecessors to resolve the dependencies
    }

    public void updateTotal(long time) {
        this.transferTime += time;
    }

    /**
     * TODO: Explain what is scheduleTransfer.
     * 
     * @param predecessor TODO.
     * @param param TODO.
     * @param resourceName TODO.
     * @param predTaskStatus TODO.
     */
    private void scheduleTransfer(AllocatableAction predecessor, DependencyParameter param, String resourceName,
        TaskState predTaskStatus) {

        EnhancedSchedulingInformation schedInfo = new EnhancedSchedulingInformation(null);
        ActionOrchestrator orchestrator = predecessor.getActionOrchestrator();
        TransferValueAction transfer = new TransferValueAction(schedInfo, orchestrator, param,
            workers.get(ResourceManager.getWorker(resourceName)));

        if (predTaskStatus != TaskState.FINISHED) {
            transfer.addDataPredecessor(predecessor);
        }

        Score transferScore = generateActionScore(transfer);

        try {
            transfer.schedule(transferScore);
            transfer.tryToLaunch();
        } catch (BlockedActionException | UnassignedActionException e) {
            String exceptionType = e instanceof BlockedActionException ? "Blocked" : "Unassigned";
            ErrorManager.warn("[RTHeuristicsScheduler] " + exceptionType + " scheduling for transfer action "
                + predecessor + " " + param);
        } catch (InvalidSchedulingException e) {
            ErrorManager
                .warn("[RTHeuristicsScheduler] Invalid scheduling for transfer action " + predecessor + " " + param);
        }
    }

    /**
     * TODO: Explain what is advanceTransfers.
     * 
     * @param action TODO.
     */
    /*
     * private void advanceTransfersFinal(AllocatableAction action) { ExecutionAction execAction = (ExecutionAction)
     * action; Task actionTask = execAction.getTask(); int id = actionTask.getId(); String actionResource =
     * getResourceForTask(id);
     * 
     * if (!action.hasDataPredecessors()) { return; }
     * 
     * Map<String, Parameter> outParameters = new HashMap<>(); for (Parameter p :
     * actionTask.getTaskDescription().getParameters()) { if (p.getDirection() != Direction.OUT && p instanceof
     * DependencyParameter) { outParameters.put(((DependencyParameter) p).getOriginalName(), p); } }
     * 
     * for (AllocatableAction predecessor : action.getDataPredecessors()) { ExecutionAction execPred = (ExecutionAction)
     * predecessor; Task predTask = execPred.getTask(); int calculatedId = predTask.getId(); String predResource =
     * getResourceForTask(calculatedId);
     * 
     * if (actionResource.equals(predResource)) { continue; }
     * 
     * for (Parameter param : predTask.getTaskDescription().getParameters()) { if (param.getDirection() != Direction.IN
     * || !(param instanceof DependencyParameter)) { continue; } DependencyParameter depParam = (DependencyParameter)
     * param; Parameter matchingParam = outParameters.get(depParam.getOriginalName());
     * 
     * if (matchingParam != null) { scheduleTransfer(predecessor, (DependencyParameter) matchingParam, actionResource,
     * predTask.getStatus()); } } } }
     */

    public float getEndTime(int taskId) {
        return this.heuristicsTasks.get(taskId).getTeoricScheduledTime();
    }

    public int getNumTasks() {
        return this.numTasks;
    }

    @Override
    public <T extends WorkerResourceDescription> void handleDependencyFreeActions(
        List<AllocatableAction> dataFreeActions, List<AllocatableAction> resourceFreeActions,
        List<AllocatableAction> blockedCandidates, ResourceScheduler<T> resource) {

        Set<AllocatableAction> freeTasks = new HashSet<>();
        freeTasks.addAll(resourceFreeActions);
        freeTasks.addAll(dataFreeActions);
        for (AllocatableAction freeAction : freeTasks) {
            EnhancedSchedulingInformation freeDSI = (EnhancedSchedulingInformation) freeAction.getSchedulingInfo();

            if (!freeDSI.hasPredecessors()) {
                tryToLaunch(freeAction);
            }
        }
    }
}
