#!/usr/bin/env bash

# envs:
# - PRESTO_VERSION
# - REGISTRY

cd $(dirname $0)

if [ ! ${PRESTO_VERSION} ]; then
    PRESTO_VERSION=0.208
fi

wget -c https://repo1.maven.org/maven2/com/facebook/presto/presto-server/${PRESTO_VERSION}/presto-server-${PRESTO_VERSION}.tar.gz
if [ ! $? -eq 0 ]; then
    echo "Failed downloading https://repo1.maven.org/maven2/com/facebook/presto/presto-server/${PRESTO_VERSION}/presto-server-${PRESTO_VERSION}.tar.gz"
    exit 1
fi
wget -c https://repo1.maven.org/maven2/com/facebook/presto/presto-cli/${PRESTO_VERSION}/presto-cli-${PRESTO_VERSION}-executable.jar
if [ ! $? -eq 0 ]; then
    echo "Failed downloading https://repo1.maven.org/maven2/com/facebook/presto/presto-cli/${PRESTO_VERSION}/presto-cli-${PRESTO_VERSION}-executable.jar"
    exit 1
fi

IMAGE_NAME="presto:${PRESTO_VERSION}"
docker build -t ${IMAGE_NAME} .

if [ ${REGISTRY} ]; then
    echo "Push to registry ${REGISTRY}"
    docker tag ${IMAGE_NAME} ${REGISTRY}/${IMAGE_NAME}
    docker push ${REGISTRY}/${IMAGE_NAME}
fi
