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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DAG {

    // Logger
    protected static final Logger LOGGER = LogManager.getLogger(Loggers.TS_COMP);

    // float matrix containing the execution times of each task for each resource
    private HashMap<String, float[]> c;

    // float matrix containing the data size transfers between tasks
    private float[][] z;

    // boolean matrix indicating the precedence relationships between tasks
    private boolean[][] succ;

    // Total number of succesors of each task
    private int[] nSucc;

    // integer, total number of tasks to execute
    private int n;

    // Map, original float matrix containing the execution times of each task for each resource (never modified)
    private Map<String, float[]> origC;

    // float, safetyMargin to add
    private float safetyMargin;

    // List, list of all workers in the system to execute de DAG, sorted
    private List<String> workerOrder;

    // Sync task information
    private int totalSyncTasks;
    private boolean[] syncTaskIds;

    // Warm Up task information
    private boolean[] warmUpTaskIds;


    /**
     * Constructor of the DAG. Initialize all the attributes
     * 
     * @param c float matrix containing the execution times of each task for each resource
     * @param z float matrix containing the data size transfers between tasks
     * @param succ boolean matrix indicating the precedence relationships between tasks
     * @param n Total number of tasks
     * @param isCloud Nodes that are cloud (sorted)
     * @param totalSyncTasks number of syncTasks detected in application.
     * @param syncTaskIds SyncTasks, index = taskId.
     * @param warmUpTaskIds warmUpTasks, index = taskId.
     */
    public DAG(Map<String, float[]> c, float[][] z, boolean[][] succ, int n, List<Boolean> isCloud,
        List<String> workersOrder, int[] nSucc, int totalSyncTasks, boolean[] syncTaskIds, boolean[] warmUpTaskIds) {
        this.totalSyncTasks = totalSyncTasks;
        this.syncTaskIds = syncTaskIds;
        this.warmUpTaskIds = warmUpTaskIds;
        this.c = new HashMap<>();
        this.origC = new HashMap<>();
        for (String worker : c.keySet()) {
            this.c.put(worker, Arrays.copyOf(c.get(worker), n));
            this.origC.put(worker, Arrays.copyOf(c.get(worker), n));
        }

        for (int i = 0; i < isCloud.size(); i++) {
            if (isCloud.get(i)) {
                this.c.remove(workersOrder.get(i));
            }
        }

        this.z = Arrays.copyOf(z, z.length);
        this.succ = Arrays.copyOf(succ, succ.length);
        this.n = n;
        this.safetyMargin = 0.0f; // TODO: update it with the correct safetyMargin used
        this.nSucc = Arrays.copyOf(nSucc, nSucc.length);
    }

    public int getTotalSyncTasks() {
        return this.totalSyncTasks;
    }

    public boolean checkSyncTask(int taskId) {
        return this.syncTaskIds[taskId];
    }

    public boolean checkWarmUpTask(int taskId) {
        return this.warmUpTaskIds[taskId];
    }

    public HashMap<String, float[]> getC() {
        return this.c;
    }

    public float getC(int task, String res) {
        return this.c.get(res)[task];
    }

    public float[][] getZ() {
        return this.z;
    }

    public float getZ(int pred, int succ) {
        return this.z[pred][succ];
    }

    public void setZ(float[][] z) {
        this.z = z;
    }

    public int getExecutableTasks() {
        return this.n - this.totalSyncTasks;
    }

    public boolean[][] getSucc() {
        return this.succ;
    }

    public void setSucc(boolean[][] succ) {
        this.succ = succ;
    }

    public int getN() {
        return this.n;
    }

    public int[] getNSucc() {
        return this.nSucc;
    }

    public void setNSucc(int[] nSucc) {
        this.nSucc = nSucc;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void removeResource(String worker) {
        this.c.remove(worker);
    }

    /**
     * Returns the action score of the given action.
     * 
     * @param worker String with the workers to add plus the safety margin
     */
    public void addResource(String worker) {
        float[] tmp = Arrays.copyOf(this.origC.get(worker), this.n);
        for (int i = 0; i < this.n; ++i) {
            tmp[i] += tmp[i] * this.safetyMargin;
        }
        this.c.put(worker, tmp);
    }

    /**
     * Add CloudProvider to the resource elements.
     * 
     * @param cloudProvider String with the cloud provider name
     * @param worker TODO: Explain this
     */
    public void addResourceCloud(String cloudProvider, String worker) {
        float[] tmp = Arrays.copyOf(this.origC.get(cloudProvider), this.n);
        for (int i = 0; i < this.n; ++i) {
            tmp[i] += tmp[i] * this.safetyMargin;
        }
        this.c.put(worker, tmp);
    }

    public float getSafetyMargin() {
        return this.safetyMargin;
    }

    /**
     * Set the safety margin to add to the maximum observed time.
     * 
     * @param safetyMargin Float with the safetyMargin to add to the DAG object.
     */
    public void setSafetyMargin(float safetyMargin) {
        this.safetyMargin = safetyMargin;
        for (String worker : this.c.keySet()) {
            float[] times = this.c.get(worker);
            for (int j = 0; j < this.n; ++j) {
                times[j] = this.origC.get(worker)[j] + (this.origC.get(worker)[j] * this.safetyMargin);
            }
            this.c.put(worker, times);
        }
    }
}
