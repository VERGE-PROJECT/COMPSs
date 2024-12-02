package es.bsc.compss.scheduler.custom.heuristics;

import es.bsc.compss.log.Loggers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import javax.lang.model.util.ElementScanner6;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LNS extends Heuristics<Integer> {

    // Logger
    protected static final Logger LOGGER = LogManager.getLogger(Loggers.TS_COMP);


    public LNS() {
        // Call the abstract class constructor
        super();
    }

    /**
     * Constructor of LNS class. Initialize all the attributes.
     * 
     * @param dag DAG object of the program to execute.
     * @param resources Resources object to plan the execution
     */
    public LNS(DAG dag, Resources resources) {
        // Call the abstract class constructor
        super(dag, resources);
    }

    /**
     * getHeuristicScore set the score of the given task using LNS heuristic.
     * 
     * @param task id of the given task.
     * @return The total number of succesors of the given task.
     */
    @Override
    protected Integer getHeuristicScore(int task) {
        return this.dag.getNSucc()[task];
    }

    /**
     * getFirstScore set the score of the source task using LNS heuristic.
     * 
     * @param sourceId id of the source task.
     * @return The total number of succesors of the source task.
     */
    @Override
    protected Integer getFirstScore(int sourceId) {
        return this.dag.getN() - 1;
    }
}