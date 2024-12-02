#!/bin/bash

NUM_WORKERS=$1

# Function to attempt SSH connection (in parallel)
try_ssh() {
    local worker=$1
    local i=0  # Initialize counter (to avoid infinitely waiting, max_time_out)

    while [ $i -ne 10 ]; do
        # Attempt to execute a command via SSH silently
        if ssh -o ConnectTimeout=1 -o StrictHostKeyChecking=no "$worker" 'exit' 2>/dev/null; then
            return 0
        fi
        
        i=$((i+1))
        sleep 1
    done

    return 1
}

# Declare a structure (array) to attempt the connections without any specific order (for better performance)
declare -a workers
for ((i=1; i<=NUM_WORKERS; i++)); do
    workers+=("worker$i")
done

# Use associative array to keep track of which workers have been successfully SSH'd
declare -A ssh_done

# Attempt SSH connections in parallel
for worker_name in "${workers[@]}"; do
    # Call try_ssh in the background and capture the index of successful attempts
    try_ssh "$worker_name" & pid=$!
    PID_LIST+=" $pid"
done

# Wait for all SSH attempts to complete
for pid in $PID_LIST; do
    wait $pid || true
done

# Independently SSH Connection are stablished (max_time_out 10 seconds) keep with the execution
exit 0