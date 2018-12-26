#!/usr/bin/env bash

# args:
# $1: 用户接口部署地址
# $2: 接口管理接口部署地址

# ENV: REGISTRY
# ENV: PUBLIC_URL

# ENV: DISABLE_MULTI_STAGE (docker-ce>=17.05)
# ENV: SKIP_BUILD 跳过前端重新编译，只替换环境变量，针对非 MULTI_STATE 的编译

IMAGE_NAME=dmc-fe:0.1

if [ ${1} ]; then
    USER_API=${1}
else
    echo './build.sh <user-api> <apim-api>'
    echo 'user api not specified'
    exit 1
fi

if [ ${2} ]; then
    APIM_API=${2}
else
    echo './build.sh <user-api> <apim-api>'
    echo 'apim api not specified'
    exit 1
fi

if [ "${DISABLE_MULTI_STAGE}" = 'true' ]; then
    docker image inspect dmc-fe:build 2>&1 > /dev/null
    # 不存在或者不跳过编译
    if [ $? -ne 0 -o "${SKIP_BUILD}" != 'true' ]; then
        echo docker build --build-arg PUBLIC_URL=${PUBLIC_URL} \
             -f docker/Dockerfile-build -t dmc-fe:build .
        docker build --build-arg PUBLIC_URL=${PUBLIC_URL} \
               -f docker/Dockerfile-build -t dmc-fe:build .
    fi
    docker create --name dmc-fe-build dmc-fe:build
    rm -rf release
    docker cp dmc-fe-build:/app/build release
    docker rm -f dmc-fe-build

    cat <<EOF > release/replace.sh
for f in \$(find /app -type f \( -name "*.js" -o -name "*.css" \)); do \\
  echo sed -i "s|USER_API_DEPLOY_ADDR|${USER_API}|g" \$f; \\
  sed -i "s|USER_API_DEPLOY_ADDR|${USER_API}|g" \$f; \\
  echo sed -i "s|APIM_API_DEPLOY_ADDR|${APIM_API}|g" \$f; \\
  sed -i "s|APIM_API_DEPLOY_ADDR|${APIM_API}|g" \$f; \\
done
EOF

    docker run --rm -it -v ${PWD}/release:/app busybox:1.29 sh /app/replace.sh
    docker build --no-cache -f docker/Dockerfile-release -t ${IMAGE_NAME} .
    rm -rf release
else
    echo 'Build using multi stage'
    docker build -f docker/Dockerfile -t ${IMAGE_NAME} \
           --build-arg PUBLIC_URL=${PUBLIC_URL} \
           --build-arg USER_API_DEPLOY_ADDR=${USER_API} \
           --build-arg APIM_API_DEPLOY_ADDR=${APIM_API} .
fi

if [ ${REGISTRY} ]; then
    docker tag ${IMAGE_NAME} ${REGISTRY}/${IMAGE_NAME}
    docker push ${REGISTRY}/${IMAGE_NAME}
fi
