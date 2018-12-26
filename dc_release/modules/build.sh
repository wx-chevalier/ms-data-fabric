#!/usr/bin/env bash

cd $(dirname $0)
chmod a+rw .

# args:
# $1: to build module, if not set, build all

# envs:
# PUBLIC_URL=dmc
# USER_API_URL=http://127.0.0.1:9002
# APIM_API_URL=http://127.0.0.1:9001
# REGISTRY=localhost:5000

# ex.
# 1. build all
#  PUBLIC_URL=/ USER_API_URL=http://127.0.0.1:9002 APIM_API_URL=http://127.0.0.1:9001 \
#      ./build.sh
# 2. build dmc_apim
#  PUBLIC_URL=/ USER_API_URL=http://127.0.0.1:9002 APIM_API_URL=http://127.0.0.1:9001 \
#      ./build.sh dmc_apim

for i in 'dmc_user' 'dmc_apim' 'dmc_proxy' 'dmc_api-runner'; do
    if [ ${1} -a "${1}" != "${i}" ]; then
        continue;
    fi
    echo "====================Buidling $i===================="
    cd $i
    ./build.sh
    cd ..
done

echo "====================Buidling dmc-fe===================="
if [ ${1} -a "${1}" != "dmc_fe" ]; then
    exit 0;
fi

cd dmc_fe
if [ ! ${PUBLIC_URL} ]; then
    PUBLIC_URL=/
fi
if [ ! ${USER_API_URL} ]; then
    echo "USER_API_URL not specified"
fi
if [ ! ${APIM_API_URL} ]; then
    echo "APIM_API_URL not specified"
fi
PUBLIC_URL=${PUBLIC_URL} ./build.sh $USER_API_URL $APIM_API_URL
cd ..
