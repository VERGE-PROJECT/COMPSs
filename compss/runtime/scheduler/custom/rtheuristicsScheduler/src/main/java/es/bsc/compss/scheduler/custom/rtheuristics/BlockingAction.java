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
package es.bsc.compss.scheduler.custom.rtheuristics;

import es.bsc.compss.components.impl.ResourceScheduler;
import es.bsc.compss.scheduler.exceptions.BlockedActionException;
import es.bsc.compss.scheduler.exceptions.FailedActionException;
import es.bsc.compss.scheduler.exceptions.UnassignedActionException;
import es.bsc.compss.scheduler.types.AllocatableAction;
import es.bsc.compss.scheduler.types.Score;
import es.bsc.compss.types.annotations.parameter.OnFailure;
import es.bsc.compss.types.implementations.Implementation;
import es.bsc.compss.types.resources.Worker;
import es.bsc.compss.types.resources.WorkerResourceDescription;
import es.bsc.compss.worker.COMPSsException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class BlockingAction extends AllocatableAction {

    /**
     * Creates a new OptimizationAction instance.
     */
    public BlockingAction() {
        super(new EnhancedSchedulingInformation(null), null);
        EnhancedSchedulingInformation asi = (EnhancedSchedulingInformation) this.getSchedulingInfo();
        asi.scheduled();
    }

    @Override
    protected void doAction() {
        // Nothing to do
    }

    @Override
    protected void doAbort() {
        // Nothing to do
    }

    @Override
    protected void doCanceled() {
        // Nothing to do
    }

    @Override
    protected void doFailIgnored() {
        // Nothing to do
    }

    @Override
    public OnFailure getOnFailure() {
        return OnFailure.RETRY;
    }

    @Override
    protected void doCompleted() {
        // Nothing to do
    }

    @Override
    protected void doError() throws FailedActionException {
        // Nothing to do
    }

    @Override
    protected void doFailed() {
        // Nothing to do
    }

    @Override
    public Integer getCoreId() {
        return null;
    }

    @Override
    public LinkedList<ResourceScheduler<? extends WorkerResourceDescription>> getCompatibleWorkers() {
        return null;
    }

    @Override
    public Implementation[] getImplementations() {
        return null;
    }

    @Override
    public <W extends WorkerResourceDescription> boolean isCompatible(Worker<W> r) {
        return true;
    }

    @Override
    public <T extends WorkerResourceDescription> LinkedList<Implementation>
        getCompatibleImplementations(ResourceScheduler<T> r) {
        return null;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public long getGroupPriority() {
        return ACTION_OPTIMIZE;
    }

    @Override
    public <T extends WorkerResourceDescription> Score schedulingScore(ResourceScheduler<T> targetWorker,
        Score actionScore) {
        return null;
    }

    @Override
    public void schedule(Score actionScore) throws BlockedActionException, UnassignedActionException {
        // Nothing to do
    }

    @Override
    public void schedule(Collection<ResourceScheduler<? extends WorkerResourceDescription>> candidates,
        Score actionScore) throws UnassignedActionException {
        // Nothing to do
    }

    @Override
    public void schedule(ResourceScheduler<? extends WorkerResourceDescription> targetWorker, Score actionScore)
        throws UnassignedActionException {
        // Nothing to do
    }

    @Override
    public void schedule(ResourceScheduler<? extends WorkerResourceDescription> targetWorker, Implementation impl)
        throws UnassignedActionException {
        // Nothing to do
    }

    @Override
    public boolean isToReserveResources() {
        return true;
    }

    @Override
    public boolean isToReleaseResources() {
        return true;
    }

    @Override
    public boolean isToStopResource() {
        return true;
    }

    @Override
    protected Collection<AllocatableAction> doException(COMPSsException e) {
        List<AllocatableAction> list = new ArrayList<AllocatableAction>();
        return list;
    }

    @Override
    protected void stopAction() throws Exception {
    }

    @Override
    public boolean checkIfCanceled(AllocatableAction aa) {
        return false;
    }

    @Override
    public String toString() {
        return "Scheduling blocking action";
    }

}
