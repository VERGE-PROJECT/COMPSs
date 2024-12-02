#!/bin/bash

INPUT_PATH=$1
ILP_PATH=$2
mode=$3
read -ra WORKERS_HOSTNAMES <<< "$4"    # Split WORKER_ORDER into an array
read -ra COMPUTING_UNITS <<< "$5"   # Split WORKER_ORDER into an array

num_tasks=`awk '$1 ~ /^[0-9]*\[/' $INPUT_PATH/${WORKERS_HOSTNAMES[0]}/debug/*.py_01/monitor/complete_graph.dot | wc -l`
sync_nodes=`awk '$1 ~ /^Synchro[0-9]*\[/' $INPUT_PATH/${WORKERS_HOSTNAMES[0]}/debug/*.py_01/monitor/complete_graph.dot | wc -l`
total_tasks=$((num_tasks+sync_nodes))
num_workers=${#WORKERS_HOSTNAMES[@]}

header_function() {
	total_computing_units=0

	for cu in "${COMPUTING_UNITS[@]}"; do
    # Check if the value is an integer using a regex
		if [[ $cu =~ ^[0-9]+$ ]]; then
			# Add the integer value to total
			total_computing_units=$((total_computing_units + cu))
		fi
	done
	
	echo "m = ${num_workers};" > $ILP_PATH
	echo "n = ${total_tasks};" >> $ILP_PATH
	echo "warmUpTasks = ${total_computing_units};" >> $ILP_PATH
}

bottom_function() {
	# Adding Computing units to the input_ilp file
	printf "computingUnits = [" >> $ILP_PATH

	for cu in "${COMPUTING_UNITS[@]}"; do
    	printf "$cu " >> $ILP_PATH
	done
	echo "];" >> $ILP_PATH

	printf "workersOrder = [" >> $ILP_PATH
	for ((w=0; w<num_workers; w++)); do
		printf "worker-$w " >> $ILP_PATH
	done

	echo "];" >> $ILP_PATH

	# Hardcoded (verify if the element to add is cloud)
	printf "isCloud = [" >> $ILP_PATH
	for ((i=0; i<num_workers; i++)); do
		printf "0 " >> $ILP_PATH
	done
	echo "];" >> $ILP_PATH

}

# MAIN FUNCTION

echo "Capturing additional data"

if [ "$mode" == "header" ]; then
	header_function
else
	bottom_function
fi

echo -e "Additional data done\n"

