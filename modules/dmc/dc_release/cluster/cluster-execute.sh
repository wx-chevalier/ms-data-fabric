#!/bin/bash
# https://www.thegeekdiary.com/how-to-make-alias-command-work-in-bash-script-or-bashrc-file/
cd "$(dirname "$0")"
shopt -s expand_aliases

CLUSTER_NAME=$1
CMD=$2
WORKER_NODE="nodes/${CLUSTER_NAME}-worker"

if ! [ -f $WORKER_NODE ]; then
  echo "cluster-execute.sh <CLUSTER_NAME> <CMD>"
  echo "Cluster node file not found[${CLUSTER_NAME}]: $WORKER_NODE"
  exit 1;
fi

# CentOS pssh
which pssh && alias parallel-ssh='pssh' > /dev/null
which pscp.pssh && alias parallel-scp='pscp.pssh' > /dev/null

echo "Executing ${CMD} across cluster ${CLUSTER_NAME}"
parallel-ssh -h ${WORKER_NODE} -i "${CMD}"