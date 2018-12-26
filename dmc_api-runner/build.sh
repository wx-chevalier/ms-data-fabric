#!/usr/bin/env bash

# ENV: REGISTRY
#      ex. REGISTRY=localhost:5000

APP_NAME=dmc-api-runner
IMAGE_NAME=dmc-api-runner:0.1
DB_NAME=dmc_runner

echo "Start building project ${APP_NAME} [log to ${PWD}/build.log]"
docker-compose run --rm -v ${PWD}:/home/gradle build \
               gradle \
               --project-cache-dir=/home/gradle/.gradle \
               -PdevDbURL=jdbc:mysql://dmc_dev_mysql:3306/${DB_NAME} \
               -PdevDbUser=root \
               -PdevDbPassword= \
               clean \
               build > build.log

if [ $? -eq 0 ]; then
    docker-compose kill && yes | docker-compose rm
else
    echo "${APP_NAME}: building FAILED"
    exit 1
fi

echo "Start building image ${IMAGE_NAME}"
JAR_FILE=$(ls build/libs/${APP_NAME}-*.jar)
if [ $? -eq 0 ]; then
    echo "Building using ${JAR_FILE} [log to ${PWD}/build.log]"
    docker build --build-arg JAR_FILE="${JAR_FILE}" -t ${IMAGE_NAME} . >> build.log
else
    echo "Built JAR not found"
    exit 1
fi

if [ ${REGISTRY} ]; then
    echo "Push to registry ${REGISTRY}"
    docker tag ${IMAGE_NAME} ${REGISTRY}/${IMAGE_NAME} >> build.log
    docker push ${REGISTRY}/${IMAGE_NAME} >> build.log
fi
