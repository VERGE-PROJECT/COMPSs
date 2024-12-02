package es.bsc.compss.scheduler.custom.heuristics;

import es.bsc.compss.log.Loggers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SPT extends Heuristics<Float> {

    // Logger
    protected static final Logger LOGGER = LogManager.getLogger(Loggers.TS_COMP);


    public SPT() {
        // Call the abstract class constructor
        super();
    }

    /**
     * Constructor of SPT class. Initialize all the attributes.
     * 
     * @param dag DAG object of the program to execute.
     * @param resources Resources object to plan the execution
     */
    public SPT(DAG dag, Resources resources) {
        // Call the abstract class constructor
        super(dag, resources);
    }

    /**
     * getHeuristicScore set the score of the given task using SPT heuristic.
     * 
     * @param task id of the given task.
     * @return The execution time of the given task for computing node 0.
     */
    @Override
    protected Float getHeuristicScore(int task) {
        return this.dag.getC(task, this.resources.getWorker(0));
    }

    /**
     * getFirstScore set the score of the source task using SPT heuristic.
     * 
     * @param sourceId id of the source task.
     * @return The execution time of the source task for computing node 0.
     */
    @Override
    protected Float getFirstScore(int sourceId) {
        return this.dag.getC(sourceId, this.resources.getWorker(sourceId));
    }


    protected class ReadyQueueElementComparator implements Comparator<HeuristicTask<Float>> {

        // (Overrided function) To order the readyqueue with the shortest process time.
        @Override
        public int compare(HeuristicTask<Float> st1, HeuristicTask<Float> st2) {
            // Order by the shortest process time (opposite from the parent)
            int returnValue = Float.compare(st1.getScore().floatValue(), st2.getScore().floatValue());
            // Larger number successors (x>y) if it is true return -1

            if (returnValue == 0) {
                returnValue = Float.compare(st1.getReleaseTime(), st2.getReleaseTime());
                if (returnValue == 0) {
                    returnValue = Integer.compare(st1.getTaskId(), st2.getTaskId());
                }
            }
            return returnValue;
        }
    }
}