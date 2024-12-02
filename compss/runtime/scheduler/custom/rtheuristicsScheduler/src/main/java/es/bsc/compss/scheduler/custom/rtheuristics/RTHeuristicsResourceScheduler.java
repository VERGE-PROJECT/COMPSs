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

import es.bsc.compss.NIOProfile;
import es.bsc.compss.api.TaskMonitor;
import es.bsc.compss.components.impl.ResourceScheduler;
import es.bsc.compss.scheduler.exceptions.ActionNotFoundException;
import es.bsc.compss.scheduler.types.AllocatableAction;
import es.bsc.compss.scheduler.types.Score;
import es.bsc.compss.scheduler.types.allocatableactions.StartWorkerAction;
import es.bsc.compss.types.DeadlineMonitor;
import es.bsc.compss.types.Task;
import es.bsc.compss.types.TaskDescription;
import es.bsc.compss.types.allocatableactions.ExecutionAction;
import es.bsc.compss.types.implementations.Implementation;
import es.bsc.compss.types.resources.Worker;
import es.bsc.compss.types.resources.WorkerResourceDescription;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import org.json.JSONObject;


public class RTHeuristicsResourceScheduler<T extends WorkerResourceDescription> extends ResourceScheduler<T> {

    private RTHeuristicsScheduler scheduler;


    /**
     * New resource scheduler instance.
     *
     * @param w Worker
     * @param resJSON Resource JSON file
     * @param implJSON Implementation JSON file
     */
    public RTHeuristicsResourceScheduler(Worker<T> w, JSONObject resJSON, JSONObject implJSON,
        RTHeuristicsScheduler sched) {
        super(w, resJSON, implJSON);
        this.scheduler = sched;
    }

    /*
     * ***************************************************************************************************************
     * SCORES
     * ***************************************************************************************************************
     */

    /**
     * Assigns an initial Schedule for the given action {@code action}.
     *
     * @param action AllocatableAction.
     */
    @Override
    public void scheduleAction(AllocatableAction action) {
        LOGGER.debug("[ResourceScheduler] Schedule action " + action + " on resource " + getName());
        if (action instanceof ExecutionAction) {
            int id = ((ExecutionAction) action).getTask().getId();
            if (!this.scheduler.getInitComputationTime()
                && !this.scheduler.getScheduledTasks().get(id).getWarmUpTask()) {
                long startingTime = System.nanoTime();
                LOGGER.debug("MY STARTING TIME is " + startingTime);
                this.scheduler.setInitComputationTime(true);
            }
        }

        // Assign no resource dependencies.
        // The worker will automatically block the tasks when there are not enough resources available.
    }

    @Override
    public PriorityQueue<AllocatableAction> getBlockedActions() {
        PriorityQueue<AllocatableAction> blockedActions = new PriorityQueue<>(20, new Comparator<AllocatableAction>() {

            @Override
            public int compare(AllocatableAction a1, AllocatableAction a2) {
                Score score1 = generateBlockedScore(a1);
                Score score2 = generateBlockedScore(a2);
                return score1.compareTo(score2);
            }
        });

        blockedActions.addAll(super.getBlockedActions());
        return blockedActions;
    }

    @Override
    public Score generateBlockedScore(AllocatableAction action) {
        LOGGER.debug("[RTResourceScheduler] Generate blocked score for action " + action);

        int taskId = ((ExecutionAction) action).getTask().getId();
        if (!((EnhancedSchedulingInformation) action.getSchedulingInfo()).hasPredecessors()) {
            return new Score(-this.scheduler.getScheduledTasks().get(taskId).getPosOrderSlotUnit(), 0, 0, 0, 0);
        } else {
            return new Score(this.scheduler.getScheduledTasks().get(taskId).getPosOrderSlotUnit(), 0, 0, 0, 0);
        }

    }

    @Override
    public Score generateResourceScore(AllocatableAction action, TaskDescription params, Score actionScore) {
        LOGGER.debug(
            "[RTResourceScheduler] Generate resource score for action " + action + " in resource " + this.getName());

        long actionPriority = actionScore.getPriority();
        long resourceScore;
        int taskId = ((ExecutionAction) action).getTask().getId();
        if (Objects.equals(this.getName(), this.scheduler.getScheduledTasks().get(taskId).getAssignedResource())) {
            resourceScore = 100;
        } else {
            resourceScore = Long.MIN_VALUE;
        }
        long waitingScore = 0;
        long implementationScore = 0;
        long actionGroupPriority = 0;

        return new Score(actionPriority, actionGroupPriority, resourceScore, waitingScore, implementationScore);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Score generateImplementationScore(AllocatableAction action, TaskDescription params, Implementation impl,
        Score resourceScore) {
        LOGGER.debug("[RTResourceScheduler] Generate implementation score for action " + action);

        // if (this.myWorker.canRunNow((T) impl.getRequirements())) { should return null as the score

        if (resourceScore == null || resourceScore.getResourceScore() == Long.MIN_VALUE) {
            return null;
        }
        long actionPriority = resourceScore.getPriority();
        long resourcePriority;
        int taskId = ((ExecutionAction) action).getTask().getId();
        if (Objects.equals(this.getName(), this.scheduler.getScheduledTasks().get(taskId).getAssignedResource())) {
            resourcePriority = 100;
        } else {
            return null;
        }
        long waitingScore = 0;
        long implScore = 0;
        long actionGroupPriority = 0;

        return new Score(actionPriority, actionGroupPriority, resourcePriority, waitingScore, implScore);
    }

    /**
     * TODO: Explain what is testingUnscheduleAction.
     * 
     * @param action TODO.
     */
    public List<AllocatableAction> testingUnscheduleAction(AllocatableAction action) throws ActionNotFoundException {
        super.unscheduleAction(action);
        LOGGER.debug("[RTResourceScheduler] Unschedule action " + action + " on resource " + getName());

        // only checkDeadlines pushes to prometheus, maybe comment only there and print in green/red the deadlines
        if (RTHeuristicsConfiguration.PROMETHEUS_ACTIVE) {
            checkDeadlines(action);
        }

        // remove dependencies between the action finished and its predecessors, and vice versa
        EnhancedSchedulingInformation actionDSI = (EnhancedSchedulingInformation) action.getSchedulingInfo();
        for (AllocatableAction pred : actionDSI.getPredecessors()) {
            if (pred != null) {
                EnhancedSchedulingInformation predDSI = (EnhancedSchedulingInformation) pred.getSchedulingInfo();
                predDSI.removeSuccessor(action);
            }
        }
        actionDSI.clearPredecessors();

        LinkedList<AllocatableAction> resourceFree = new LinkedList<>();
        for (AllocatableAction successor : actionDSI.getSuccessors()) {
            EnhancedSchedulingInformation succDSI = (EnhancedSchedulingInformation) successor.getSchedulingInfo();
            succDSI.removePredecessor(action);
            List<AllocatableAction> predecessors = succDSI.getPredecessors();
            int taskId = ((ExecutionAction) successor).getTask().getId();

            if (!succDSI.hasPredecessors()) {
                // if successor does not have other predecessors, add them to resourceFree to be executed
                if (resourceFree.isEmpty()) {
                    resourceFree = new LinkedList<>();
                }
                resourceFree.add(successor);
                LOGGER.debug("[RTResourceScheduler] Next action to execute is " + taskId);
            } else if (succDSI.hasOpActionPred() && predecessors.size() == 1 && resourceFree.isEmpty()) {
                // if successor only has fake dependency with opAction, and resourceFree is empty, then add the action
                // as free to be executed
                Iterator<AllocatableAction> it = predecessors.iterator();
                EnhancedSchedulingInformation opActionDSI =
                    (EnhancedSchedulingInformation) (it.next()).getSchedulingInfo();
                opActionDSI.removeSuccessor(successor);
                succDSI.clearPredecessors();
                succDSI.setHasOpAction(false);
                resourceFree.add(successor);
                LOGGER.debug("[RTResourceScheduler] Next action to execute is " + taskId + " on resource " + getName());
            }
        }

        actionDSI.clearSuccessors();

        return resourceFree;
    }

    @Override
    public List<AllocatableAction> unscheduleAction(AllocatableAction action) throws ActionNotFoundException {
        super.unscheduleAction(action);
        LOGGER.debug("[RTResourceScheduler] Unschedule action " + action + " on resource " + getName());

        // only checkDeadlines pushes to prometheus, maybe comment only there and print in green/red the deadlines
        if (RTHeuristicsConfiguration.PROMETHEUS_ACTIVE) {
            checkDeadlines(action);
        }

        // remove dependencies between the action finished and its predecessors, and vice versa
        EnhancedSchedulingInformation actionDSI = (EnhancedSchedulingInformation) action.getSchedulingInfo();
        for (AllocatableAction pred : actionDSI.getPredecessors()) {
            if (pred != null) {
                EnhancedSchedulingInformation predDSI = (EnhancedSchedulingInformation) pred.getSchedulingInfo();
                predDSI.removeSuccessor(action);
            }
        }
        actionDSI.clearPredecessors();

        LinkedList<AllocatableAction> resourceFree = new LinkedList<>();
        for (AllocatableAction successor : actionDSI.getSuccessors()) {
            EnhancedSchedulingInformation succDSI = (EnhancedSchedulingInformation) successor.getSchedulingInfo();
            succDSI.removePredecessor(action);
            List<AllocatableAction> predecessors = succDSI.getPredecessors();
            int taskId = ((ExecutionAction) successor).getTask().getId();

            if (!succDSI.hasPredecessors()) {
                // if successor does not have other predecessors, add them to resourceFree to be executed
                if (resourceFree.isEmpty()) {
                    resourceFree = new LinkedList<>();
                }
                resourceFree.add(successor);
                LOGGER.debug("[RTResourceScheduler] Next action to execute is " + taskId);
            } else if (succDSI.hasOpActionPred() && predecessors.size() == 1 && resourceFree.isEmpty()) {
                // if successor only has fake dependency with opAction, and resourceFree is empty, then add the action
                // as free to be executed
                Iterator<AllocatableAction> it = predecessors.iterator();
                EnhancedSchedulingInformation opActionDSI =
                    (EnhancedSchedulingInformation) (it.next()).getSchedulingInfo();
                opActionDSI.removeSuccessor(successor);
                succDSI.clearPredecessors();
                succDSI.setHasOpAction(false);
                resourceFree.add(successor);
                LOGGER.debug("[RTResourceScheduler] Next action to execute is " + taskId + " on resource " + getName());
            }
        }
        actionDSI.clearSuccessors();
        if (action instanceof ExecutionAction) {
            int id = ((ExecutionAction) action).getTask().getId();
            if (id == this.scheduler.getNumTasks()) {
                long endTime = System.nanoTime();
                LOGGER.debug("MY ENDING TIME is " + endTime);
            }
        }

        return resourceFree;
    }

    private void checkDeadlines(AllocatableAction action) {
        if (action instanceof ExecutionAction) {
            Task task = ((ExecutionAction) action).getTask();
            int taskId = task.getId();

            TaskMonitor monitor = task.getTaskMonitor();

            if (!this.scheduler.getScheduledTasks().get((int) taskId).getWarmUpTask()) {
                if (monitor instanceof DeadlineMonitor) {
                    NIOProfile p = ((DeadlineMonitor) monitor).getProfile();

                    long startExecutionTime = p.getStartTime();
                    long endExecutionTime = p.getEndTime();

                    LOGGER.debug("[RTResourceScheduler] Action " + taskId + " started at " + startExecutionTime
                        + ", ended at " + endExecutionTime + " and lasted " + (endExecutionTime - startExecutionTime));

                    float teoricScheduledTime =
                        this.scheduler.getScheduledTasks().get((int) taskId).getTeoricScheduledTime();

                    // teoricScheduledTime includes cMargin
                    if ((endExecutionTime - startExecutionTime) > teoricScheduledTime) {
                        System.out.println("WARNING: Deadline has been missed for task " + taskId + " on resource "
                            + getName() + " (End time: " + (endExecutionTime - startExecutionTime)
                            + "; Expected end time: " + teoricScheduledTime + ")");

                        new Thread(() -> {
                            this.scheduler.incrementTaskCounter();
                        }).start();
                    } else {
                        System.out.println("Deadline has been successful for task " + taskId + " on resource "
                            + getName() + " (End time: " + (endExecutionTime - startExecutionTime)
                            + "; Expected end time: " + teoricScheduledTime + ")");
                    }
                }
            }
        }
    }

    /*
     * ***************************************************************************************************************
     * OTHER
     * ***************************************************************************************************************
     */
    @Override
    public String toString() {
        return "RTResourceScheduler@" + getName();
    }

}
