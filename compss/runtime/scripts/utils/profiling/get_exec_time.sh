#!/bin/bash

INPUT_PATH=$1
ILP_PATH=$2
read -ra WORKERS_HOSTNAMES <<< "$3"    # Split WORKER_ORDER into an array

num_tasks=`awk '$1 ~ /^[0-9]*\[/' $INPUT_PATH/${WORKERS_HOSTNAMES[0]}/debug/*.py_01/monitor/complete_graph.dot | wc -l`
sync_nodes=`awk '$1 ~ /^Synchro[0-9]*\[/' $INPUT_PATH/${WORKERS_HOSTNAMES[0]}/debug/*.py_01/monitor/complete_graph.dot | wc -l`
total_tasks=$((num_tasks+sync_nodes))
num_workers=${#WORKERS_HOSTNAMES[@]}

# C Margin added to C matrix
c_margin=0.1
echo "cMargin = $c_margin;" >> $ILP_PATH
echo -e "CMargin set to $c_margin\n"

echo "Generating C matrix (execution times)" 

# get exec_time
C_TEMP_PATH=$(dirname "$ILP_PATH")/c_temp.txt
echo 'C = [' > $C_TEMP_PATH

for ((i=0; i < total_tasks; i++)); do
    echo -n "[" >> $C_TEMP_PATH
    for ((j=0; j < num_workers; j++)); do
        if [[ $i -eq 0 ]] || [[ $i -gt $num_tasks ]] && [[ $j -ne 0 ]]; then
            echo -n "-1 " >> $C_TEMP_PATH
        else 
            echo -n "0 " >> $C_TEMP_PATH
        fi
    done
    echo "]" >> $C_TEMP_PATH
done
echo "];" >> $C_TEMP_PATH

for (( w=1; w<=num_workers; w++ )); do

    filename=$(find "$INPUT_PATH/${WORKERS_HOSTNAMES[((w-1))]}/debug/"*.py_01/trace/ -type f -name "*.prv" -print -quit)
    aux_filename="$(dirname "$filename")/aux.prv"

    cp -r "$filename" "$aux_filename"

    taskTimes=()
    taskIds=()
    taskTimesZero=()
    prefixes=()
    
    # Process the aux file to fill the taskTimes array
    while IFS= read -r line; do
        if [[ "$line" =~ .*:8000002:[1-9][0-9]*$ ]]; then
            task_id=$(echo "$line" | awk -F':' '{print $NF}')
            task_time=$(echo "$line" | awk -F':' '{print $6}')
            taskIds+=($task_id)
            taskTimes+=($task_time)
            # Extract everything before the last two colon-separated fields
            prefix=$(echo "$line" | awk -F':' '{
                for (i=1; i<=NF-3; i++) 
                    printf "%s%s", $i, (i<NF-3 ? ":" : "")
            }')
            prefixes+=($prefix)
        fi
    done < "$aux_filename"
    
    # Precompile the pattern to avoid doing it in the loop
    pattern=':.*:8000002:0$'
    
    while IFS= read -r line; do
        if [[ "$line" =~ $pattern ]]; then
            task_time_zero=$(echo "$line" | awk -F':' '{print $6}')
            
            # Iterate through prefixes to find a match
            for i in "${!prefixes[@]}"; do
                prefix="${prefixes[$i]}"
                
                if [[ "$line" == "$prefix"* ]]; then
                    task_execution_time=$(echo "scale=3; ($task_time_zero - ${taskTimes[$i]}) / 1000000" | bc)
    
                    if [ "$w" == 1 ]; then
                        awk -v i="${taskIds[$i]}" -v c=$w -v v="[$task_execution_time" 'NR==i+2 {$c=v} 1' $C_TEMP_PATH > temp.txt && mv temp.txt $C_TEMP_PATH
                    else
                        awk -v i="${taskIds[$i]}" -v c=$w -v v="$task_execution_time" 'NR==i+2 {$c=v} 1' $C_TEMP_PATH > temp.txt && mv temp.txt $C_TEMP_PATH
                    fi
                    
                    # Once a match is found, unset matched elements
                    unset 'prefixes[i]'
                    unset 'taskIds[i]'
                    unset 'taskTimes[i]' # Assuming you also want to remove the corresponding taskTime
    
                    break # Break the loop as we found the match and already removed it
                fi
            done
        fi
    done < "$aux_filename"
    
    rm -rf "$aux_filename"
done

cat $C_TEMP_PATH >> $ILP_PATH

rm -rf $C_TEMP_PATH

echo -e "C matrix done\n"