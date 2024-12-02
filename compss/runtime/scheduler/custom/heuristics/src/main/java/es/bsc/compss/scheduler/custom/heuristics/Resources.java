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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Resources {

    // ibw matrix that shows the ibw from a given resource k to another l
    private Map<String, Map<String, Float>> ibw;

    private Map<String, Map<String, Float>> origIbw;

    // maxmimum frame size allowed
    private float mfs;

    // size of header
    private float h;

    // number of resources
    private int m;

    private ArrayList<String> resourceNames;

    private ArrayList<String> originalWorkers;

    private LinkedHashMap<String, Integer> posByName;

    private HashMap<String, Integer> computingUnits;

    // Total slot units for computation
    protected int slotUnits;

    // LOGGER
    protected static final Logger LOGGER = LogManager.getLogger(Loggers.TS_COMP);


    /**
     * Constructor of the Resources class with all the parameters. Initialize all the attributes.
     * 
     * @param ibw Float computed from the delayRtt parameter provided by the NFR Communications.
     * @param compUnits Worker name 2.
     * @param mfs maximum frame size allowed.
     * @param h size of header.
     * @param m number of resources.
     */
    public Resources(Map<String, Map<String, Float>> ibw, Map<String, Integer> compUnits, float mfs, float h, int m) {
        fillMaps(ibw, compUnits);
        this.mfs = mfs;
        this.h = h;
        this.m = m;
        this.resourceNames = new ArrayList<>(this.m);
        this.originalWorkers = new ArrayList<>(this.m);
        this.posByName = new LinkedHashMap<>();
    }

    /**
     * Reads the input file input_ilp.dat.
     */
    private void fillMaps(Map<String, Map<String, Float>> ibw, Map<String, Integer> compUnits) {
        this.ibw = new HashMap<>();
        this.origIbw = new HashMap<>();
        this.computingUnits = new HashMap<>();
        for (String worker1 : ibw.keySet()) {
            this.computingUnits.put(worker1, compUnits.get(worker1));
            this.ibw.put(worker1, new HashMap<>());
            this.origIbw.put(worker1, new HashMap<>());
            for (String worker2 : ibw.get(worker1).keySet()) {
                this.ibw.get(worker1).put(worker2, ibw.get(worker1).get(worker2).floatValue());
                this.origIbw.get(worker1).put(worker2, ibw.get(worker1).get(worker2).floatValue());
            }
            System.out.println();
        }
    }

    /**
     * Copy Constructor of the class Resources. Initialize all the attributes.
     * 
     * @param resources Object resources with the all the attributes to copy.
     */
    public Resources(Resources resources) {
        fillMaps(resources.ibw, resources.computingUnits);
        this.mfs = resources.mfs;
        this.h = resources.h;
        this.m = resources.m;
        this.resourceNames = new ArrayList<>(m);
        this.posByName = new LinkedHashMap<>();
    }

    public void setSlotUnits(int slotUnits) {
        this.slotUnits = slotUnits;
    }

    public int getSlotUnits() {
        return this.slotUnits;
    }

    public Map<String, Map<String, Float>> getIBW() {
        return this.ibw;
    }

    public float getMFS() {
        return this.mfs;
    }

    public void setMFS(float mfs) {
        this.mfs = mfs;
    }

    public float getH() {
        return this.h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public int getM() {
        return this.m;
    }

    public void setM(int m) {
        this.m = m;
    }

    /**
     * Remove a resource to the resource pool to be considered by the scheduler by name.
     * 
     * @param name Name of the resources to remove.
     */
    public void removeResource(String name) {
        // public void removeResource(String name, int pos) {
        /*
         * TODO: need to check what happens with computing units, safe to keep values without removing them from map
         * TODO: since resourceNames does not have the worker name anymore?
         */
        this.m--;
        ibw.remove(name); // erase entry in ibw for name (e.g. erase row in ibw matrix)
        for (String worker : ibw.keySet()) { // erase columns belonging to name
            ibw.get(worker).remove(name);
        }
        int i = 0;
        boolean found = false;
        for (String worker : this.resourceNames) { // erase worker name
            if (worker.equals(name)) {
                found = true;
                break;
            }
            ++i;
        }
        if (found) {
            this.resourceNames.remove(i);
        }
    }

    /*
     * private float findBw() { for (int i = 0; i < this.m - 1; ++i) { for (int j = 0; j < this.m - 1; ++j) { if
     * (this.ibw[i][j] != 0) { return this.ibw[i][j]; } } } return 0.0f; }
     */

    /**
     * Adds a new resource to the resource pool to be considered by the scheduler.
     *
     * @param name Name of the resource to add.
     */
    public void addResource(String name) {
        this.m++;
        for (String worker : ibw.keySet()) {
            ibw.get(worker).put(name, origIbw.get(worker).get(name)); // add float values to columns
        }
        ibw.put(name, new HashMap<>());
        origIbw.get(name).forEach((key, value) -> ibw.get(name).put(key, value)); // add row for worker name
        // TODO: needs further checking with cloud workers

        int i = 0;
        for (String worker : this.originalWorkers) {
            if (worker.equals(name)) {
                this.resourceNames.add(i, worker);
            }
            ++i;
        }
    }

    /**
     * Adds a new resource to the resource pool to be considered by the scheduler.
     *
     * @param name Name of the resource to add.
     */
    public int addResourceCloud(String cloudProvider, String name) {
        this.m++;
        for (String worker : ibw.keySet()) {
            // ibw.get(worker).put(name, origIbw.get(worker).get(cloudProvider).floatValue()); // regular workers
            if (worker.startsWith(cloudProvider)) {
                ibw.get(worker).put(name, origIbw.get(cloudProvider).get(cloudProvider).floatValue()); // add float
                // values to columns
            } else {
                ibw.get(worker).put(name, origIbw.get(worker).get(cloudProvider).floatValue()); // regular workers
            }
        }
        // if key in origIbw is cloudProvider -> then when adding new entry in ibw it will be the workers name
        // (rotterdam-0, f.e.)
        // else if not cloud provider (regular resource) then it will put the worker name itself (key)
        ibw.put(name, new HashMap<>());
        origIbw.get(cloudProvider).forEach((key, value) -> { // TODO: needs further checking when mixing cloud and
            // regular resources
            if (key.equals(cloudProvider)) {
                ibw.get(name).put(name, value);
            } else {
                ibw.get(name).put(key, value);
            }
        });

        // add cloud worker to origIbw and update other map entries to add this new worker name
        // it is needed because otherwise first lambda does not introduce all entries (some workers)
        for (String worker : origIbw.keySet()) {
            if (ibw.get(worker) != null) { // worker != cloudProvider
                origIbw.get(worker).put(name, ibw.get(worker).get(name).floatValue());
            } else { // worker == cloudProvider -> if worker is cloud provider it does not matter as worker already, in
                // cloud -> 0.0f
                origIbw.get(worker).put(name, 0.0f);
            }
        }
        origIbw.put(name, new HashMap<>());
        ibw.get(name).forEach((key, value) -> origIbw.get(name).put(key, value));

        int pos = this.posByName.get(cloudProvider);
        int i = 0;
        for (String worker : this.originalWorkers) {
            // System.out.println(worker);
            if (worker.equals(cloudProvider)) {
                // this.resourceNames.set(i, worker);
                this.resourceNames.add(i, name);
            }
            ++i;
        }
        return pos;
    }

    /**
     * Sets the internal structure that represents the names of the workers to identify them.
     *
     * @param names Array containing the names.
     */
    public void setResourceNames(List<String> names) {
        this.resourceNames = new ArrayList<>(names);
        this.originalWorkers = new ArrayList<>(names);
        for (String name : names) {
            int pos = this.posByName.size();
            this.posByName.put(name, pos);
        }
    }

    /**
     * Update inverse bandwidth structures. If entries in ibw are present, update it as well.
     * 
     * @param worker1 Worker name 1.
     * @param worker2 Worker name 2.
     * @param ibw Float computed from the delayRtt parameter provided by the NFR Communications.
     */
    public void setIbw(String worker1, String worker2, float ibw) {
        this.origIbw.get(worker1).put(worker2, ibw);
        if (this.ibw.containsKey(worker1) && this.ibw.get(worker1).containsKey(worker2)) {
            this.ibw.get(worker1).put(worker2, ibw);
        }
    }

    public ArrayList<String> getResourceNames() {
        return this.resourceNames;
    }

    public String getWorker(int pos) {
        return this.resourceNames.get(pos);
    }

    public int getComputingUnits(String worker) {
        return computingUnits.get(worker);
    }
}
