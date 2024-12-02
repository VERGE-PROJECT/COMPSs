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

package es.bsc.compss.scheduler.custom.heuristics;

import es.bsc.compss.log.Loggers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LNSNL extends Heuristics<Integer> {

    // Logger
    protected static final Logger LOGGER = LogManager.getLogger(Loggers.TS_COMP);


    public LNSNL() {
        // Call the abstract class constructor
        super();
    }

    /**
     * Constructor of LNSNL class. Initialize all the attributes.
     * 
     * @param dag DAG object of the program to execute.
     * @param resources Resources object to plan the execution
     */
    public LNSNL(DAG dag, Resources resources) {
        // Call the abstract class constructor
        super(dag, resources);
    }

    /**
     * getHeuristicScore set the score of the given task using LNSNL heuristic.
     * 
     * @param task id of the given task.
     * @return The total number of succesors of the given task.
     */
    @Override
    protected Integer getHeuristicScore(int task) {
        return computeNextLevelSuccessors(task);
    }

    /**
     * getFirstScore set the score of the source task using LNSNL heuristic.
     * 
     * @param sourceId id of the source task.
     * @return The directed number succesors of the source task.
     */
    @Override
    protected Integer getFirstScore(int sourceId) {
        return computeNextLevelSuccessors(sourceId);
    }

    /**
     * computeNextLevelSuccessors calculate the number of directed successors for a given task.
     * 
     * @param taskId id of the source task.
     * @return The directed number succesors of the source task.
     */
    protected int computeNextLevelSuccessors(int taskId) {
        int nSucc = 0;
        boolean[][] succ = this.dag.getSucc();
        for (int i = 0; i < this.dag.getN(); ++i) {
            if (succ[taskId][i]) {
                ++nSucc;
            }
        }
        return nSucc;
    }
}
