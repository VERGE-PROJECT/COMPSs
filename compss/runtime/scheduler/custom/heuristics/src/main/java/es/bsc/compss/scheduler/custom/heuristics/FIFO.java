package es.bsc.compss.scheduler.custom.heuristics;

import es.bsc.compss.log.Loggers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import javax.lang.model.util.ElementScanner6;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FIFO extends Heuristics<Integer> {

    // Logger
    protected static final Logger LOGGER = LogManager.getLogger(Loggers.TS_COMP);


    public FIFO() {
        // Call the abstract class constructor
        super();
    }

    /**
     * Constructor of None class. Initialize all the attributes.
     * 
     * @param dag DAG object of the program to execute.
     * @param resources Resources object to plan the execution
     */
    public FIFO(DAG dag, Resources resources) {
        // Call the abstract class constructor
        super(dag, resources);
    }

    /**
     * getHeuristicScore set the score of the given task using None heuristic.
     * 
     * @param task id of the given task.
     * @return The total number of succesors of the given task.
     */
    @Override
    protected Integer getHeuristicScore(int task) {
        return 1;
    }

    /**
     * getFirstScore set the score of the source task using LNS heuristic.
     * 
     * @param sourceId id of the source task.
     * @return The total number of succesors of the source task.
     */
    @Override
    protected Integer getFirstScore(int sourceId) {
        return 1;
    }


    protected class ReadyQueueElementComparator<N extends Number> implements Comparator<HeuristicTask<N>> {

        @Override
        public int compare(HeuristicTask<N> st1, HeuristicTask<N> st2) {
            return Integer.compare(st1.getTaskId(), st2.getTaskId());
        }
    }
}