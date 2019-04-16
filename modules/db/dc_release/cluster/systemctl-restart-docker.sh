#!/bin/bash
cd "$(dirname "$0")"

CLUSTER_NAME=$1
DOCKER_CONFIG_FILE=$2
DST_DOCKER_CONFIG_FILE=/etc/systemd/system/docker.service.d/override.conf

if [ ${DOCKER_CONFIG_FILE} ]; then
  if [ -f ${DOCKER_CONFIG_FILE} ]; then
    echo "Using docker config: ${DOCKER_CONFIG_FILE}"
  else
    echo "systemctl-restart-docker.sh <CLUSTER_NAME> [DOCKER_CONFIG_FILE]"
    echo "Config file not found: ${DOCKER_CONFIG_FILE}"
    exit 1
  fi
else
  DOCKER_CONFIG_FILE=/etc/systemd/system/docker.service.d/override.conf
fi

echo 'Sync config'
./sync-files.sh ${CLUSTER_NAME} ${DOCKER_CONFIG_FILE} ${DST_DOCKER_CONFIG_FILE}
if [ ${DOCKER_CONFIG_FILE} != ${DST_DOCKER_CONFIG_FILE} ]; then
  cp ${DOCKER_CONFIG_FILE} ${DST_DOCKER_CONFIG_FILE}
fi

echo 'Restart docker'
./cluster-execute.sh ${CLUSTER_NAME} 'systemctl daemon-reload && systemctl restart docker'