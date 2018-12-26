FROM openjdk:8-jre-slim

ARG MIRROR="https://repo1.maven.org/maven2/com/facebook/presto"
ARG PRESTO_VERSION="0.208"
ARG PRESTO_BIN="presto-server-${PRESTO_VERSION}.tar.gz"
ARG PRESTO_CLI_BIN="presto-cli-${PRESTO_VERSION}-executable.jar"

USER root

RUN sed -i 's/deb.debian.org/mirrors.ustc.edu.cn/g' /etc/apt/sources.list && \
    sed -i 's|security.debian.org/debian-security|mirrors.ustc.edu.cn/debian-security|g' /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -y --allow-unauthenticated curl wget less && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Need to work with python2
# See: https://github.com/prestodb/presto/issues/4678
ENV PYTHON2_DEBIAN_VERSION 2.7.13-2
RUN apt-get update && apt-get install -y --no-install-recommends \
    python="${PYTHON2_DEBIAN_VERSION}" \
    && rm -rf /var/lib/apt/lists/* \
    && cd /usr/local/bin \
    && rm -rf idle pydoc python python-config

ENV PRESTO_HOME                      /presto
ENV PRESTO_USER                      presto
ENV PRESTO_CONF_DIR                  ${PRESTO_HOME}/etc
ENV PRESTO_LOG_DIR                   /var/log/presto
ENV PRESTO_NODE_DATA_DIR             /presto-data
ENV PRESTO_JVM_MAX_HEAP              16G
ENV PRESTO_QUERY_MAX_MEMORY          20GB
ENV PRESTO_QUERY_MAX_MEMORY_PER_NODE 1GB
ENV PRESTO_DISCOVERY_URI             http://coordinator-1.vnet:8080

ENV PATH $PATH:$PRESTO_HOME/bin

RUN useradd \
    --create-home \
    --home-dir ${PRESTO_HOME} \
    --shell /bin/bash \
    $PRESTO_USER

ADD ${PRESTO_BIN} .
COPY ${PRESTO_CLI_BIN} .

COPY etc/  ${PRESTO_CONF_DIR}/
COPY bin/*  /usr/local/bin/
COPY lib/*  /usr/local/lib/

RUN mkdir -p ${PRESTO_HOME} \
             ${PRESTO_HOME}/data/ \
             ${PRESTO_CONF_DIR}/catalog/ \
             ${PRESTO_LOG_DIR} \
             ${PRESTO_NODE_DATA_DIR} && \
    mv presto-server-${PRESTO_VERSION}/* $PRESTO_HOME && \
    rm -rf presto-server-${PRESTO_VERSION} && \
    mv presto-cli-${PRESTO_VERSION}-executable.jar ${PRESTO_HOME}/bin/presto && \
    cd ${PRESTO_HOME}/bin && \
    chmod +x presto && \
    chown -R ${PRESTO_USER}:${PRESTO_USER} \
          ${PRESTO_HOME} \
          ${PRESTO_LOG_DIR} \
          ${PRESTO_NODE_DATA_DIR}

VOLUME ["${PRESTO_LOG_DIR}", "${PRESTO_NODE_DATA_DIR}"]

USER $PRESTO_USER

# CMD ["launcher", "run"]
ENTRYPOINT ["entrypoint.sh"]
