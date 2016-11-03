#!/bin/bash

export HADOOP_HOME=/opt/modules/hadoop-2.7.2
export HIVE_HOME=/opt/modules/hive-1.2.1

CURDATE=$(date +%y-%m-%d)
CURDATEHIVE=$(date +%Y-%m-%d)


${HADOOP_HOME}/bin/hdfs dfs -df /flume/events/$CURDATE

if [[ 1 -ne $? ]]
then
${HADOOP_HOME}/bin/hadoop jar /export/data/mydata/clickstream.jar com.guludada.clickstream.logClean
fi

if [[ 1 -ne $? ]]
then
${HADOOP_HOME}/bin/hadoop jar /export/data/mydata/clickstream.jar com.guludada.clickstream.logSession
fi

if [[ 1 -ne $? ]]
then
${HADOOP_HOME}/bin/hadoop jar /export/data/mydata/clickstream.jar com.guludada.clickstream.PageViews
fi

#Load today's data
if [[ 1 -ne $? ]]
then
${HADOOP_HOME}/bin/hdfs dfs -chmod 777 /clickstream/pageviews/$CURDATE/
echo "load data inpath '/clickstream/pageviews/$CURDATE/' into table pageviews partition(inputDate='$CURDATEHIVE');" | ${HIVE_HOME}/bin/beeline -u jdbc:hive2://localhost:10000
fi

#Create fact table and its dimension tables
if [[ 1 -ne $? ]]
then
echo "insert into table ods_pageviews partition(inputDate='$CURDATEHIVE') select pv.session,pv.ip,concat(pv.requestdate,'-',pv.requesttime) as viewtime,pv.visitpage,pv.staytime,pv.step from pageviews as pv where pv.inputDate='$CURDATEHIVE';" | ${HIVE_HOME}/bin/beeline -u jdbc:hive2://localhost:10000
fi

if [[ 1 -ne $? ]]
then
echo "insert into table ods_dim_pageviews_time partition(inputDate='$CURDATEHIVE') select distinct pv.viewtime, substring(pv.viewtime,0,4),substring(pv.viewtime,6,2),substring(pv.viewtime,9,2),substring(pv.viewtime,12,2),substring(pv.viewtime,15,2),substring(pv.viewtime,18,2) from ods_pageviews as pv;" | ${HIVE_HOME}/bin/beeline -u jdbc:hive2://localhost:10000
fi

if [[ 1 -ne $? ]]
then
echo "insert into table ods_dim_pageviews_url partition(inputDate='$CURDATEHIVE') select distinct pv.visitpage,b.host,b.path,b.query from pageviews pv lateral view parse_url_tuple(concat('https://localhost',pv.visitpage),'HOST','PATH','QUERY') b as host,path,query;" | ${HIVE_HOME}/bin/beeline -u jdbc:hive2://localhost:10000
fi
