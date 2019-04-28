#!/bin/bash
# https://www.thegeekdiary.com/how-to-make-alias-command-work-in-bash-script-or-bashrc-file/
cd "$(dirname "$0")"
shopt -s expand_aliases

CLUSTER_NAME=$1
SRC_FILE=$2
DST_FILE=$3
WORKER_NODE="nodes/${CLUSTER_NAME}-worker"

if ! [ -f $WORKER_NODE ]; then
  echo "sync-files.sh <CLUSTER_NAME> <SRC_FILE> <DST_FILE>"
  echo "Cluster node file not found[$CLUSTER_NAME]: $WORKER_NODE"
  exit 1;
fi

# CentOS pssh
which pssh && alias parallel-ssh='pssh' > /dev/null
which pscp.pssh && alias parallel-scp='pscp.pssh' > /dev/null

echo "Copy src=${SRC_FILE} to worker nodes dst=${DST_FILE}"
parallel-scp -h ${WORKER_NODE} $SRC_FILE $DST_FILE
