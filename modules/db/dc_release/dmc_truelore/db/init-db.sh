#!/usr/bin/env bash

docker run --restart always --name mysql-dmc \
       -p 80:3306 \
       -e MYSQL_ROOT_PASSWORD=DABB2A09-528E-4D1C-9067-EC15AA96459F -d \
       -v ${PWD}/init-db:/docker-entrypoint-initdb.d \
       -v /var/lib/mysql-dmc:/var/lib/mysql \
       mysql:5.7.23 \
       --character-set-server=utf8mb4 \
       --collation-server=utf8mb4_unicode_ci \
       --default-authentication-plugin=mysql_native_password
