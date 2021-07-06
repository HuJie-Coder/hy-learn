#!/bin/bash

# hudi on flink cluster
# flink version: 1.12.2
# scala version: 2.12
# hadoop version: 2.10.1
# bash env.sh

docker compose down
docker compose up -d
hosts=(jobmanager taskmanager_1)
#hosts=(jobmanager taskmanager_1 taskmanager_2)
for _host in ${hosts[@]}
do
    echo "${_host}"
    docker cp hadoop ${_host}:/opt
    docker cp lib/*.jar ${_host}:/opt/flink/lib
    docker container exec ${_host} apt update
    docker container exec ${_host} apt install less
    docker container exec ${_host} apt install -y vim
    docker container exec ${_host} sed -i '$a alias ll="ls -l"' /etc/bash.bashrc
    docker container exec ${_host} sed -i '19i export HADOOP_CLASSPATH=\$(/opt/hadoop/bin/hadoop classpath)' /opt/flink/bin/config.sh
done
docker compose stop
docker compose up -d

