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
package es.bsc.compss.data;

public interface FetchDataListener {

    /**
     * Notification that the value has been fetched properly.
     *
     * @param fetchedDataId Id of the data fetched.
     */
    public void fetchedValue(String fetchedDataId);

    /**
     * Notification of an error while fetching {@code failedDataId}.
     *
     * @param failedDataId Id of the data that could not be fetched.
     * @param cause reason why the value fetching failed.
     */
    public void errorFetchingValue(String failedDataId, Exception cause);

}