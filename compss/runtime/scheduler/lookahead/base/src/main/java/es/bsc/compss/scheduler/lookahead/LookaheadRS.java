/*
 *  Copyright 2002-2023 Barcelona Supercomputing Center (www.bsc.es)
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
package es.bsc.compss.scheduler.lookahead;

import es.bsc.compss.components.impl.ResourceScheduler;
import es.bsc.compss.scheduler.exceptions.ActionNotFoundException;
import es.bsc.compss.scheduler.types.AllocatableAction;
import es.bsc.compss.scheduler.types.Score;
import es.bsc.compss.types.TaskDescription;
import es.bsc.compss.types.allocatableactions.ExecutionAction;
import es.bsc.compss.types.implementations.Implementation;
import es.bsc.compss.types.resources.Worker;
import es.bsc.compss.types.resources.WorkerResourceDescription;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONObject;


public abstract class LookaheadRS<T extends WorkerResourceDescription> extends ResourceScheduler<T> {

    private int computedTasks = 0;


    /**
     * New Ready Resource Scheduler instance.
     *
     * @param w Associated worker.
     * @param resJSON Worker JSON description.
     * @param implJSON Implementation JSON description.
     */
    public LookaheadRS(Worker<T> w, JSONObject resJSON, JSONObject implJSON) {
        super(w, resJSON, implJSON);
    }

    @Override
    public abstract Score generateBlockedScore(AllocatableAction action);

    @Override
    public abstract Score generateResourceScore(AllocatableAction action, TaskDescription params, Score actionScore);

    @Override
    public final Score generateImplementationScore(AllocatableAction action, TaskDescription params,
        Implementation impl, Score resourceScore) {
        // LOGGER.debug("[FifoRS] Generate implementation score for action " + action);
        if (this.hasBlockedActions()) {
            // Added for scale-down: In readyScheduler, should disable the node for scheduling more tasks?
            return null;
        }
        if (this.myWorker.canRunNow((T) impl.getRequirements())) {
            return getRunnableImplScore(action, params, impl, resourceScore);
        } else {
            // Implementation cannot be run
            return null;
        }
    }

    /**
     * Returns the score for an implementation that the resource could host at this moment.
     * 
     * @param action Action to score the compute
     * @param params data parameters used by the action
     * @param impl implementation to evaluate
     * @param resourceScore score computed by the resource
     * @return Score for the given implementation of the action
     */
    protected abstract Score getRunnableImplScore(AllocatableAction action, TaskDescription params, Implementation impl,
        Score resourceScore);

    @Override
    public abstract String toString();

    @Override
    public void scheduleAction(AllocatableAction action) {
        LOGGER.debug("[ResourceScheduler] Schedule action " + action + " on resource " + getName());
        int magicNumber = 12;
        if (action instanceof ExecutionAction && this.computedTasks == magicNumber) {
            long startingTime = System.nanoTime();
            LOGGER.debug("MY STARTING TIME is " + startingTime);
        }
    }


    private ArrayList<Integer> tasksOrder = new ArrayList<>();
    private ArrayList<String> nameOrder = new ArrayList<>();


    /**
     * Print tasks order and resource for each.
     */
    public void printOrder() {
        StringBuilder tasksOrderBuilder = new StringBuilder();

        for (Integer task : tasksOrder) {
            tasksOrderBuilder.append(task).append(", ");
        }

        LOGGER.debug(
            tasksOrderBuilder.length() > 0 ? tasksOrderBuilder.substring(0, tasksOrderBuilder.length() - 2) : "");

        StringBuilder nameOrderBuilder = new StringBuilder();

        for (String name : nameOrder) {
            nameOrderBuilder.append(name).append(", ");
        }

        LOGGER.debug(nameOrderBuilder.length() > 0 ? nameOrderBuilder.substring(0, nameOrderBuilder.length() - 2) : "");
    }

    @Override
    public List<AllocatableAction> unscheduleAction(AllocatableAction action) throws ActionNotFoundException {
        if (DEBUG) {
            LOGGER.debug("[ResourceScheduler] Unschedule action " + action + " on resource scheduler for " + getName()
                + " No new actions have been released.");

        }
        if (action instanceof ExecutionAction) {
            int id = ((ExecutionAction) action).getTask().getId();
            this.tasksOrder.add(id);
            this.nameOrder.add(getName());

            this.computedTasks++;
            int magicNumber = 336;
            if (this.computedTasks == magicNumber) {
                long endTime = System.nanoTime();
                LOGGER.debug("MY ENDING TIME is " + endTime);
                printOrder();
            }
        }
        action.assignResource(null);
        return new LinkedList<>();
    }

}
