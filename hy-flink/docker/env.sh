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
for _host in ${hosts[@]};do
    echo "${_host}"
    docker cp hadoop ${_host}:/opt
    docker cp hive-site.xml ${_host}:/opt/flink/conf
    docker cp lib/flink-sql-connector-hive-2.3.6_2.12-1.12.3.jar ${_host}:/opt/flink/lib/
    docker cp lib/hive-exec-2.3.9.jar ${_host}:/opt/flink/lib/
#    docker cp lib/hudi-flink-bundle_2.12-0.9.0-SNAPSHOT.jar ${_host}:/opt/flink/lib/
    docker container exec ${_host} apt update
    docker container exec ${_host} apt install less
    docker container exec ${_host} apt install -y vim
    docker container exec ${_host} sed -i '$a alias ll="ls -l"' /etc/bash.bashrc
    # flink hadoop combine 1.12
    docker container exec ${_host} sed -i '19i export HADOOP_CLASSPATH=\$(/opt/hadoop/bin/hadoop classpath)' /opt/flink/bin/config.sh
    # flink-sql config
#    docker container exec ${_host} sed -i '74d' /opt/flink/conf/sql-client-defaults.yaml
#    docker container exec ${_host} sed -i '74i catalogs:\n        - name: hive\n          type: hive\n          hive-conf-dir: /opt/flink/conf/' /opt/flink/conf/sql-client-defaults.yaml
#    docker container exec ${_host} sed -i '123s/default_catalog/hive/g' /opt/flink/conf/sql-client-defaults.yaml
#    docker container exec ${_host} sed -i '125s/default_database/default/g' /opt/flink/conf/sql-client-defaults.yaml
done

docker compose stop
docker compose up -d

