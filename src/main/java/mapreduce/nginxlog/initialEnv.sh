#!/bin/bash

export HADOOP_HOME=/opt/modules/hadoop-2.7.2
export FLUME=/opt/modules/flume-1.6.0
#start hdfs
${HADOOP_HOME}/sbin/start-dfs.sh

#start yarn
if [[ 0 == $? ]]
then
${HADOOP_HOME}/sbin/start-yarn.sh
fi

#start flume
#if [[ 0 == $? ]]
#then
#start flume
#$nohup ~/apache-flume-1.6.0-bin/bin/flume-ng agent -n agent -c conf -f ~/apache-flume-1.6.0-bin/conf/flume-conf.properties &
#fi

#start mysql
if [ 0 = $? ]
then
service mysqld start
fi

#start HIVE SERVER
if [ 0 = $? ]
then
$nohup /apps/apache-hive-1.2.1-bin/bin/hiveserver2 &
fi