package es.bsc.compss.scheduler.custom.heuristics;

import es.bsc.compss.log.Loggers;
import es.bsc.compss.util.ErrorManager;

import java.util.PriorityQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HeuristicTask<N extends Number> {

    private int taskId;
    private float releaseTime; // Time until task can become ready to compute
    private N score; // numberSuccessors in LNS and LNSNL (integer), resourceTime in LPT and SPT (float)
    private boolean syncTask; // If task is sync node
    private boolean warmUpTask; // If task is warm up task

    // Assigned once calculated (computed heuristics)
    private float teoricScheduledTime; // Scheduling time (teoric) once found the best computing configuration
    private String assignedResource; // Assigned resource (name) for the task selected
    private int assignedComputingUnit; // Assigned computing unit for the task selected
    private int numberAssignedResource; // assigned resource (name) for the task selected
    private int assignedResourceNumber; // assigned resource number (int) for the task selected
    private int absolutePosition; // Absolute position inside the Computing Unit assigned

    // Position of the task allocated in the Computing Unit taking into account previous tasks allocation
    private int posOrderSlotUnit;


    /**
     * Creates the heuristic Task used to find the best possible configurationn for an execution.
     * 
     * @param taskId taskId.
     * @param releaseTime time to be ready to be computed (free dependency time).
     * @param score score of the task given by an heuristic.
     * @param syncTask true if task is a syncTask.
     */
    public HeuristicTask(int taskId, float releaseTime, N score, boolean syncTask, boolean warmUpTask) {
        this.taskId = taskId;
        this.releaseTime = releaseTime;
        this.score = score;
        this.syncTask = syncTask;
        this.warmUpTask = warmUpTask;
    }

    public void setPosOrderSlotUnit(int posTask) {
        this.posOrderSlotUnit = posTask;
    }

    public int getPosOrderSlotUnit() {
        return this.posOrderSlotUnit;
    }

    public int getAssignedResourceNumber() {
        return this.assignedResourceNumber;
    }

    public int getAssignedComputingUnit() {
        return this.assignedComputingUnit;
    }

    public void setAssignedComputingUnit(int computingUnit) {
        this.assignedComputingUnit = computingUnit;
    }

    public String getAssignedResource() {
        return this.assignedResource;
    }

    public int getAbsolutePosition() {
        return this.absolutePosition;
    }

    public void setAbsolutePosition(int absolutePosition) {
        this.absolutePosition = absolutePosition;
    }

    public void assignResource(String assignedResource, int resourceNumber) {
        this.assignedResource = assignedResource;
        this.assignedResourceNumber = resourceNumber;
    }

    public float getTeoricScheduledTime() {
        return this.teoricScheduledTime;
    }

    public void setTeoricScheduledTime(float scheduledTime) {
        this.teoricScheduledTime = scheduledTime;
    }

    public int getTaskId() {
        return this.taskId;
    }

    public boolean getSyncTask() {
        return this.syncTask;
    }

    public void setSyncTask(boolean syncTask) {
        this.syncTask = syncTask;
    }

    public boolean getWarmUpTask() { // true if warmUptask
        return this.warmUpTask;
    }

    public void setWarmUpTask(boolean warmUpTask) {
        this.warmUpTask = warmUpTask;
    }

    public float getReleaseTime() {
        return this.releaseTime;
    }

    public void setReleaseTime(float releaseTime) {
        this.releaseTime = releaseTime;
    }

    public N getScore() {
        return this.score;
    }

    public void setScore(N score) {
        this.score = score;
    }

    public void addToQueue(PriorityQueue<HeuristicTask<N>> queue) {
        queue.offer(this);
    }

}