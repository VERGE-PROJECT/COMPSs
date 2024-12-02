#!/bin/bash

INPUT_PATH=$1
ILP_PATH=$2
compute_successors=$3
read -ra WORKERS_HOSTNAMES <<< "$4"    # Split WORKER_ORDER into an array

COMPLETE_GRAPH_PATH="$INPUT_PATH/${WORKERS_HOSTNAMES[0]}/debug/*.py_01/monitor/complete_graph.dot"

computeSuccessors() {
    local taskId=$1
    local computedNSucc=0
    local stack=()

    stack+=($taskId)
    visited[$taskId]=true

    while [ ${#stack[@]} -ne 0 ]; do
        local currentTask=${stack[-1]}
        stack=("${stack[@]::${#stack[@]}-1}")

        for ((i=2; i<=$total_tasks; i++)); do
            if [ "${succ[$currentTask,$i]}" = "1" ] && [ "${visited[$i]}" = false ]; then
                visited[$i]=true
                stack+=($i)
                computedNSucc=$((computedNSucc + 1))
            fi
        done
    done

    echo $computedNSucc  # set the global array value for the current task ID
}

### Compute Data Size method and ZID ###
compute_z_and_zid() {
	local pred=$1
	local succes=$2
	local total_size=0
	contains=()


	file_id=`grep -m 1 -R "$pred -> $succes \[" $COMPLETE_GRAPH_PATH | cut -d'"' -f2`;
	while read -r line;
	do
		# echo $line
		if [ -z "$file_id" ];
        	then
                	# file_id not in complete_graph, retrieve it from runtime.log 
			# based on pred value
	                filenames=`echo $line | grep -oP '(?<=MGMT ID = ).*?(?=])' \
        	                  | grep -oh "\w.*${pred}v.*IT\w*"`
        	else
                	# retrieve complete file name starting with $file_id
	                filenames=`echo $line | grep -oP "(?<=\[DATA )[^ ]+" \
        	                  | grep "${file_id}.*IT" | cut -d"@" -f1`
        	fi

		# filenames contain all the d[0-9]v... files that the task $succes receives
		# from $pred (can be more than once), we need to compute the total sum of 
		# param files received
		for param_file in $filenames;
		do
			if [[ $param_file == *".IT"* ]];
			then
				size=`grep "New output value generated $param_file" \
					$INPUT_PATH/${WORKERS_HOSTNAMES[0]}/debug/*.py_01/workers/*/Log/static_worker_*.out | awk '{print $NF}'`

				# checks if the parameter is the return, in that case it's 
				# not accounted
				is_return=`echo $line | grep -oP '(?<=PARAM\[).*?(?=VALUE)' \
					   | grep "$param_file"`
				if [[ "$is_return" != *"return_0"* ]] && [[ ! " ${contains[@]} " =~ " ${param_file} " ]];
				then
					# printf "For task $succes adds the size $size of file"
					# echo "$param_file"
                                        total_size=$((total_size+size))
					contains+=("$param_file")
				fi
			fi
		done
	done < <(grep -R "TASK ID= $succes]" $INPUT_PATH/${WORKERS_HOSTNAMES[0]}/debug/*.py_01/runtime.log)
	z[$((pred+1)),$((succes+1))]=$total_size

	if [[ $file_id == "d"* ]];
	then
		zid[$((pred+1)),$((succes+1))]=$file_id
	fi
}

###           MAIN             ###
num_tasks=`awk '$1 ~ /^[0-9]*\[/' $COMPLETE_GRAPH_PATH | wc -l`
sync_nodes=`awk '$1 ~ /^Synchro[0-9]*\[/' $COMPLETE_GRAPH_PATH | wc -l`
total_tasks=$((num_tasks+sync_nodes))

declare -A succ
declare -A z
declare -A zid
declare -a visited # Used to calculated the nSucc array

# init succ, zid and z matrices
for ((i=1;i<=total_tasks;i++)) do
	# Set the visited array to false 
	visited+=(false)
	for ((j=1;j<=total_tasks;j++)) do
		succ[$i,$j]=0
		z[$i,$j]=0
		zid[$i,$j]="0"
	done
done

echo "Computing Z and Z_id matrices"

# iterate complete_graph file looking for dependencies
while read -r line;
do
	pred=`echo $line | awk '{print $1}'`
	succes=`echo $line | awk '{print $3}' | cut -d";" -f1`
	
	# to fill z matrix, pred and succes can be either Synchro* or [0-9]
	compute_z_and_zid $pred $succes
	
	if [[ $pred == Sync* ]];
	then
		# remove the Synchro part and get the number, if > 0 
		# goes at the end
		pred=`echo $pred | cut -d"o" -f2`
		if [ $pred -ne 0 ];
		then
			pred=$((pred+num_tasks))
		fi
	fi

	if [[ $succes == Sync* ]];
	then
		# remove the Synchro part and get the number, since it 
		# is > 0 it goes at the end
		succes=`echo $succes | cut -d"o" -f2`
		succes=$((succes+num_tasks))
	fi
	
	# Synchro0 -> pos 1, Task 1 -> pos 2, etc
	succ[$((pred+1)),$((succes+1))]=1

done < <(grep -- "->" $COMPLETE_GRAPH_PATH)

echo -e "Z and Z_id matrices done\n"

if [ $compute_successors = true ]
then
	echo "Computing the successors (high computational cost)"

	# Set the nSucc arraylist by computing all the number of succesors of each node
	printf "nSucc = [" >> $ILP_PATH
	
	# Set first node always to total_tasks -1
	printf "$((total_tasks-1)) " >> $ILP_PATH
	for ((i=2;i<total_tasks;i++))
	do
		numberSuccessors=$(computeSuccessors $i)
		numberSuccessors=$(( numberSuccessors + 1 ))
		printf "$numberSuccessors " >> $ILP_PATH
	done
	# Set last node always to 0
	printf "0 " >> $ILP_PATH
	printf "];\n" >> $ILP_PATH
else
	echo "Computing successors false, set everything to -1"
	
	printf "nSucc = [" >> $ILP_PATH
	negative_value=-1
	# Set first node always to total_tasks -1
	printf "%d " ${negative_value}  >> $ILP_PATH
	for ((i=2;i<total_tasks;i++)); do
		printf "%d " ${negative_value} >> $ILP_PATH
	done
	# Set last node
	printf "%d " ${negative_value} >> $ILP_PATH
	printf "];\n" >> $ILP_PATH
fi


echo "Filling successors matrix"

echo "succ = [" >> $ILP_PATH
for ((i=1;i<=total_tasks;i++)) do
	for ((j=1;j<=total_tasks;j++)) do
		if [ $j -eq 1 ];
		then
			printf "[" >> $ILP_PATH
		fi
		printf "${succ[$i,$j]} " >> $ILP_PATH
	done
	printf "]" >> $ILP_PATH
	echo >> $ILP_PATH
done
echo "];" >> $ILP_PATH

echo -e "Successors matrix done\n"

echo "Filling Z matrix" 

# Prepare a sequence of operations for bc
operations=""
for ((i=1; i<=total_tasks; i++)); do
    for ((j=1; j<=total_tasks; j++)); do
        operations+="scale=6; ${z[$i,$j]} / (1024*1024)\n"
    done
done

# Perform all calculations
values=($(echo -e "$operations" | bc -l))

# Build the output
output="z = [\n"
idx=0
for ((i=1; i<=total_tasks; i++)) do
    line="["
    for ((j=1; j<=total_tasks; j++)) do
        line+="${values[$idx]} "
        idx=$((idx + 1))
    done
    line+="]\n"
    output+="$line"
done
output+="];"

# Write to file
echo -e "$output" >> $ILP_PATH

echo -e "Z matrix done\n" 

echo "Filling Z_id matrix" 

# Build the output in memory
output="zid = [\n"

for ((i=1; i<=total_tasks; i++)) do
    line="["
    for ((j=1; j<=total_tasks; j++)) do
        line+="\"${zid[$i,$j]}\" "
    done
    line+="]\n"
    output+="$line"
done

output+="];"

# Write to the file
echo -e "$output" >> $ILP_PATH

echo -e "Z_id matrix done\n" 