#!/usr/bin/env bash

cd $(dirname $0)
WORKDIR=$(pwd)

echo 'Syncing configuration'
cd config && tar -cf /tmp/dmc-config.tar . && cd .. \
    && ../cluster/sync-files.sh 12345 /tmp/dmc-config.tar /tmp/dmc-config.tar \
    && rm -rf /opt/cluster/config/dmc && mkdir -p /opt/cluster/config/dmc && cd /opt/cluster/config/dmc && tar xf /tmp/dmc-config.tar \
    && cd ${WORKDIR} \
    && ../cluster/cluster-execute.sh 12345 'rm -rf /opt/cluster/config/dmc && mkdir -p /opt/cluster/config/dmc && cd /opt/cluster/config/dmc && tar xf /tmp/dmc-config.tar'

echo 'Startup'
docker stack deploy -c docker-compose.yml dmc
