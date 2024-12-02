#!/bin/bash

INPUT_PATH=$1
ILP_PATH=$2
read -ra WORKERS_HOSTNAMES <<< "$3"    # Split WORKER_ORDER into an array

DATA_PATH=$INPUT_PATH/data_files
IPERF_MATRIX_PATH=$DATA_PATH/iperf-matrix.dat

num_workers=${#WORKERS_HOSTNAMES[@]}

echo "Filling network data"

mkdir -p $DATA_PATH

sed -i '1!{$!{s/^/[&/; s/$/] /}}' "$IPERF_MATRIX_PATH"

cat $IPERF_MATRIX_PATH >> $ILP_PATH

max=$(grep -oE '[0-9]*\.[0-9]+' "$IPERF_MATRIX_PATH" | sort -g | tail -n 1)

rm -f $IPERF_MATRIX_PATH

# Check if the number starts with a dot and prepend 0 if necessary
if [[ $max == .* ]]; then
    max="0$max"
fi

echo "maxBw = $max;" >> $ILP_PATH
# Take the MTU size to set the MFS, previous hardcoded value MFS = 0.00143051147
mtu_size=1500 # Default value =1500 bytes just to avoid errors

#mtu_size=$(awk '{split(FILENAME,arr,"/"); print arr[5],$1}' /sys/class/net/*/mtu | grep eth0 | awk '{print $NF}' | awk '{print $0/(1024*1024)}') # MTU size of eth0 in MegaBytes
mtu_size=$(awk '{split(FILENAME,arr,"/"); print arr[5],$1}' /sys/class/net/*/mtu | grep eth0 | awk '{print $NF}')
echo "MFS = $mtu_size;" >> $ILP_PATH

transp_prot="tcp" # Hardcoded (depends on the protocol used)

if [ "$transp_prot" == "tcp" ];
then
	#echo "H = 40/(1024*1024)" >> $ILP_PATH
	#echo "H = 0.00003814697;" >> $ILP_PATH ### MB
	echo "H = 66;" >> $ILP_PATH
else
	#echo "H = 28/(1024/1024)" >> $ILP_PATH
	echo "H = 0.00002670288;" >> $ILP_PATH ### MB
fi

net_m=0.05 # Hardcoded (verify the amount) 

# Adding Network margin to the input_ilp file
echo "networkMargin = $net_m;" >> $ILP_PATH

echo -e "Network data done\n"