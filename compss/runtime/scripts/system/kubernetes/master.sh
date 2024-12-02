#!/bin/bash

# Take params
NAMESPACE="${1}"
WORKERS_NUMBER="${2}"
WORKERS_CPU="${3}"
WORKERS_MEMORY="${4}"
IMAGE_NAME="${5}"
ABS_CONTEXT="${6}"
MIN_VMS="${7}"
MAX_VMS="${8}"
CREATION_TIME="${9}"
shift 9

RUNCOMPSS_ARGS="$*"

# Echo the parameters
echo "NAMESPACE=${NAMESPACE}"
echo "WORKERS_NUMBER=${WORKERS_NUMBER}"
echo "WORKERS_CPU=${WORKERS_CPU}"
echo "WORKERS_MEMORY=${WORKERS_MEMORY}"
echo "IMAGE_NAME=${IMAGE_NAME}"
echo "ABS_CONTEXT=${ABS_CONTEXT}"
echo "MIN_VMS=${MIN_VMS}"
echo "MAX_VMS=${MAX_VMS}"
echo "CREATION_TIME=${CREATION_TIME}"
echo "RUNCOMPSS_ARGS=${RUNCOMPSS_ARGS}"

CLOUD="False"
if [ ${MAX_VMS} -gt 0 ]; then
  CLOUD="True"
fi

# Generate resources and project XML files
/opt/COMPSs/Runtime/scripts/system/kubernetes/generate_k8s_resources.sh "$ABS_CONTEXT/resources.xml" $WORKERS_NUMBER $WORKERS_CPU $WORKERS_MEMORY "127.0.0.1" $IMAGE_NAME $CLOUD $CREATION_TIME
/opt/COMPSs/Runtime/scripts/system/kubernetes/generate_k8s_project.sh "$ABS_CONTEXT/project.xml" $WORKERS_NUMBER $IMAGE_NAME $MIN_VMS $MAX_VMS

# Run the COMPSs application
cd $ABS_CONTEXT
/opt/COMPSs/Runtime/scripts/user/runcompss --master_name=master --project="$ABS_CONTEXT/project.xml" --resources="$ABS_CONTEXT/resources.xml" $RUNCOMPSS_ARGS 2>&1

sleep 2

# Scale down COMPSs execution (deletion should be done with Helm/runcompss-k8s)
# Workers are scaled down first to allow runcompss-k8s to copy results from the master
echo "Scaling down workers"
kubectl --namespace $NAMESPACE scale statefulset $DEPLOYMENT_NAME-worker --replicas=0

sleep 30

echo "Scaling down master"
kubectl --namespace $NAMESPACE scale deployment $DEPLOYMENT_NAME-master --replicas=0

exit 0