/*
 *  Copyright 2002-2019 Barcelona Supercomputing Center (www.bsc.es)
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
package es.bsc.compss.types;

import es.bsc.compss.NIOProfile;
import es.bsc.compss.api.ParameterCollectionMonitor;
import es.bsc.compss.api.ParameterMonitor;
import es.bsc.compss.api.TaskMonitor;
import es.bsc.compss.types.annotations.parameter.DataType;
import es.bsc.compss.worker.COMPSsException;


public class DeadlineMonitor implements TaskMonitor {

    private NIOProfile p;


    public DeadlineMonitor() {
        this.p = null;
    }

    public NIOProfile getProfile() {
        return this.p;
    }

    public void setProfile(NIOProfile profile) {
        this.p = profile;
    }

    @Override
    public void onCreation() {
        // NOTHING TO DO
    }

    @Override
    public void onAccessesProcessed() {
        // NOTHING TO DO
    }

    @Override
    public void onSchedule() {
        // NOTHING TO DO
    }

    @Override
    public void onSubmission() {
        // NOTHING TO DO
    }

    @Override
    public void onDataReception() {
        // NOTHING TO DO
    }

    @Override
    public ParameterMonitor getParameterMonitor(int elementId) {
        return new DeadlineParameterMonitor();
    }

    @Override
    public void onSuccesfulExecution() {
        this.p = p;
    }

    public void onSuccesfulExecution(NIOProfile p) {
        this.p = p;
    }

    @Override
    public void onAbortedExecution() {
        // NOTHING TO DO
    }

    @Override
    public void onErrorExecution() {
        // NOTHING TO DO
    }

    @Override
    public void onFailedExecution() {
        // NOTHING TO DO
    }

    @Override
    public void onCancellation() {
        // NOTHING TO DO
    }

    @Override
    public void onCompletion() {
        // NOTHING TO DO
    }

    @Override
    public void onFailure() {
        // NOTHING TO DO
    }

    @Override
    public void onException(COMPSsException e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onExecutionStart() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onExecutionStartAt(long ts) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onExecutionEnd() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onExecutionEndAt(long ts) {
        // TODO Auto-generated method stub

    }


    private static class DeadlineParameterMonitor implements ParameterMonitor, ParameterCollectionMonitor {

        @Override
        public ParameterMonitor getParameterMonitor(int elementId) {
            return this;
        }

        @Override
        public void onCreation(DataType type, String dataName) {
            // Ignore Notification
        }

    }
}
