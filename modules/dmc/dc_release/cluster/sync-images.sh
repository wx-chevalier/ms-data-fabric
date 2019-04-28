#!/bin/bash
# https://www.thegeekdiary.com/how-to-make-alias-command-work-in-bash-script-or-bashrc-file/
cd "$(dirname "$0")"
shopt -s expand_aliases

CLUSTER_NAME=$1
IMAGES="${@:2}"
TMP_FILE="/tmp/tmp-img.tgz"
WORKER_NODE="nodes/${CLUSTER_NAME}-worker"

if ! [ -f $WORKER_NODE ]; then
  echo "Cluster node file not found: $WORKER_NODE"
  exit 1;
fi

# centos pssh
which pssh && alias parallel-ssh='pssh' > /dev/null
which pscp.pssh && alias parallel-scp='pscp.pssh' > /dev/null

for i in ${IMAGES};
do
  rm -f $TMP_FILE

  echo "Backing up ${i}"
  docker save ${i} | gzip -c > $TMP_FILE

  echo "Copy ${i} to worker nodes"
  parallel-scp -h ${WORKER_NODE} $TMP_FILE $TMP_FILE

  echo "Remove ${i} from worker nodes"
  parallel-ssh -h ${WORKER_NODE} -i "docker rmi -f ${i}"

  echo "Load ${i} on worker nodes"
  parallel-ssh -h ${WORKER_NODE} -i "docker load -i $TMP_FILE"
done
