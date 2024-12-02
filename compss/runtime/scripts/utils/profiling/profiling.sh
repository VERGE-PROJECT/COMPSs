#!/bin/bash

SCRIPTS_PATH=$(dirname "$0")
EXECUTION_PATH=$1
WORKERS_ORDER="$2"
COMPUTING_UNITS=$3

RESULT_PATH="$EXECUTION_PATH/results"
OUTPUT_PATH=$EXECUTION_PATH/data_files
ILP_PATH="$OUTPUT_PATH/input_ilp.dat"

# Header data
bash $SCRIPTS_PATH/get_additional_data.sh $RESULT_PATH $ILP_PATH "header" "$WORKERS_ORDER" "$COMPUTING_UNITS"

# Launch get successors and z matrices
compute_sucessors=false # Currently set to true (high computational cost)
bash $SCRIPTS_PATH/get_successors_and_z.sh $RESULT_PATH $ILP_PATH $compute_sucessors "$WORKERS_ORDER" 

# Launch get execution times (C matrix)
bash $SCRIPTS_PATH/get_exec_time.sh $RESULT_PATH $ILP_PATH "$WORKERS_ORDER"

# Launch get network data
bash $SCRIPTS_PATH/get_network_data.sh $EXECUTION_PATH $ILP_PATH "$WORKERS_ORDER"

# Bottom data
bash $SCRIPTS_PATH/get_additional_data.sh $RESULT_PATH $ILP_PATH "bottom" "$WORKERS_ORDER" "$COMPUTING_UNITS"

echo "Profiling done"
