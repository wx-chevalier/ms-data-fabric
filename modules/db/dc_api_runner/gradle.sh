#!/usr/bin/env bash
clear
echo "正在编译,请稍候......"
gradle build -x test
echo "正在执行,请稍候......."
java -jar build/libs/apirunner-1.0-SNAPSHOT.jar
