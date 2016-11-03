Flume+Hadoop+Hive的离线分析系统基本架构
实际生产环境中的大数据离线分析技术还涉及到很多细节的处理和高可用的架构。
这篇文章的目的只是带大家入个门，让大家对离线分析技术有一个简单的认识，并和大家一起做学习交流。

		离线分析系统的结构图
		http://www.aboutyun.com/data/attachment/forum/201605/31/111751v40gdap7ui8uge8d.jpg
		服务器NGINX(access.log)-->服务器FTP
		-->1.使用Flume从FTP服务器上拉取数据并存储在HDFS上
		-->Hadoop集群:
		-->2.使用MapReduce进行数据清洗
		-->3.根据业务需求,使用Hive创建事实表和维度表,然后用HIVESQL针对具体的业务指标进行数据分析--Mysql for  metadata
		-->4.使用Sqoop讲HIVE表中的数据导入到业务数据库中-->业务数据服务器Mysql for transaction
		-->Web服务器

		使用Shell脚本配合Crontab定时器实现自动化任务调度功能,完成每天拉取数据,清晰数据,分析数据等任务

	整个离线分析的总体架构就是使用Flume从FTP服务器上采集日志文件，并存储在Hadoop HDFS文件系统上，
	再接着用Hadoop的mapreduce清洗日志文件，最后使用HIVE构建数据仓库做离线分析。
	任务的调度使用Shell脚本完成，当然大家也可以尝试一些自动化的任务调度工具，比如说AZKABAN或者OOZIE等。
	分析所使用的点击流日志文件主要来自Nginx的access.log日志文件，
	需要注意的是在这里并不是用Flume直接去生产环境上拉取nginx的日志文件，而是多设置了一层FTP服务器来缓冲所有的日志文件，
	然后再用Flume监听FTP服务器上指定的目录并拉取目录里的日志文件到HDFS服务器上(具体原因下面分析)。
	从生产环境推送日志文件到FTP服务器的操作可以通过Shell脚本配合Crontab定时器来实现。

	http://www.aboutyun.com/data/attachment/forum/201605/31/111810aqzsuwqr2unsrtvn.png
	一般在WEB系统中，用户对站点的页面的访问浏览，点击行为等一系列的数据都会记录在日志中，每一条日志记录就代表着上图中的一个数据点；
	而点击流数据关注的就是所有这些点连起来后的一个完整的网站浏览行为记录，可以认为是一个用户对网站的浏览session。
	比如说用户从哪一个外站进入到当前的网站，用户接下来浏览了当前网站的哪些页面，点击了哪些图片链接按钮等一系列的行为记录，这一个整体的信息就称为是该用户的点击流记录。

	这篇文章中设计的离线分析系统就是收集WEB系统中产生的这些数据日志，并清洗日志内容存储分布式的HDFS文件存储系统上，
	接着使用离线分析工具HIVE去统计所有用户的点击流信息。

	本系统中我们采用Nginx的access.log来做点击流分析的日志文件。access.log日志文件的格式如下：
	样例数据格式:
	124.42.13.230 - - [18/Sep/2013:06:57:50 +0000] "GET /shoppingMall?ver=1.2.1 HTTP/1.1" 200 7200 "http://www.baidu.com.cn" "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; BTRS101170; InfoPath.2; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727)"
	格式分析:
		1、访客ip地址:124.42.13.230
		2、访客用户信息： - -
		3、请求时间：[18/Sep/2013:06:57:50 +0000]
		4、请求方式：GET
		5、请求的url：/shoppingMall?ver=1.10.2
		6、请求所用协议：HTTP/1.1
		7、响应码：200
		8、返回的数据流量：7200
		9、访客的来源url：http://www.baidu.com.cn
		10、访客所用浏览器：Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; BTRS101170; InfoPath.2; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727)

收集用户数据
	网站会通过前端JS代码或服务器端的后台代码收集用户浏览数据并存储在网站服务器中。
	一般运维人员会在离线分析系统和真实生产环境之间部署FTP服务器，
	并将生产环境上的用户数据每天定时发送到FTP服务器上，离线分析系统就会从FTP服务上采集数据而不会影响到生产环境。

	采集数据的方式有多种，
		一种是通过自己编写shell脚本或Java编程采集数据，但是工作量大，不方便维护，
		另一种就是直接使用第三方框架去进行日志的采集，一般第三方框架的健壮性，容错性和易用性都做得很好也易于维护。
	本文采用第三方框架Flume进行日志采集，Flume是一个分布式的高效的日志采集系统，
	它能把分布在不同服务器上的海量日志文件数据统一收集到一个集中的存储资源中，
	Flume是Apache的一个顶级项目，与Hadoop也有很好的兼容性。
	不过需要注意的是Flume并不是一个高可用的框架，这方面的优化得用户自己去维护。

	Flume的agent是运行在JVM上的，所以各个服务器上的JVM环境必不可少。
	每一个Flume agent部署在一台服务器上，Flume会收集web server 产生的日志数据，并封装成一个个的事件发送给Flume Agent的Source，
	Flume Agent Source会消费这些收集来的数据事件并放在Flume Agent Channel，
	Flume Agent Sink会从Channel中收集这些采集过来的数据，要么存储在本地的文件系统中,
		要么作为一个消费资源分发给下一个装在分布式系统中其它服务器上的Flume进行处理。
	Flume提供了点对点的高可用的保障，某个服务器上的Flume Agent Channel中的数据只有确保传输到了另一个服务器上的Flume Agent Channel里或者正确保存到了本地的文件存储系统中，才会被移除。

	本系统中每一个FTP服务器以及Hadoop的name node服务器上都要部署一个Flume Agent；
	FTP的Flume Agent采集Web Server的日志并汇总到name node服务器上的Flume Agent，
	最后由hadoop name node服务器将所有的日志数据下沉到分布式的文件存储系统HDFS上面。

	需要注意的是Flume的Source在本文的系统中选择的是Spooling Directory Source，而没有选择Exec Source，
	因为当Flume服务down掉的时候Spooling Directory Source能记录上一次读取到的位置，
	而Exec Source则没有，需要用户自己去处理，当重启Flume服务器的时候如果处理不好就会有重复数据的问题。
	当然Spooling Directory Source也是有缺点的，会对读取过的文件重命名，所以多架一层FTP服务器也是为了避免Flume“污染”生产环境。
	Spooling Directory Source另外一个比较大的缺点就是无法做到灵活监听某个文件夹底下所有子文件夹里的所有文件里新追加的内容。
	关于这些问题的解决方案也有很多，比如选择其它的日志采集工具，像logstash等。

	FTP服务器上的Flume配置文件如下：SEE ftp.properties

	这里有几个参数需要说明，
	Flume Agent Source可以通过配置deserializer.maxLineLength这个属性来指定每个Event的大小，默认是每个Event是2048个byte。
	Flume Agent Channel的大小默认等于于本地服务器上JVM所获取到的内存的80%，用户可以通过byteCapacityBufferPercentage和byteCapacity两个参数去进行优化。
	需要特别注意的是FTP上放入Flume监听的文件夹中的日志文件不能同名，不然Flume会报错并停止工作，最好的解决方案就是为每份日志文件拼上时间戳。
	在Hadoop服务器上的配置文件如下：SEE hdfs-sink.properties

	round, roundValue,roundUnit三个参数是用来配置每10分钟在hdfs里生成一个文件夹保存从FTP服务器上拉取下来的数据。

Troubleshooting
	使用Flume拉取文件到HDFS中会遇到将文件分散成多个1KB-5KB的小文件的问题
	需要注意的是如果遇到Flume会将拉取过来的文件分成很多份1KB-5KB的小文件存储到HDFS上，
	那么很可能是HDFS Sink的配置不正确，导致系统使用了默认配置。
	spooldir类型的source是将指定目录中的文件的每一行封装成一个event放入到channel中，默认每一行最大读取1024个字符。
	在HDFS Sink端主要是通过rollInterval(默认30秒), rollSize(默认1KB), rollCount(默认10个event)3个属性来决定写进HDFS的分片文件的大小。
		rollInterval表示经过多少秒后就将当前.tmp文件(写入的是从channel中过来的events)下沉到HDFS文件系统中，
		rollSize表示一旦.tmp文件达到一定的size后，就下沉到HDFS文件系统中，
		rollCount表示.tmp文件一旦写入了指定数量的events就下沉到HDFS文件系统中。

使用Flume拉取到HDFS中的文件格式错乱
	这是因为HDFS Sink的配置中，hdfs.writeFormat属性默认为“Writable”会将原先的文件的内容序列化成HDFS的格式，
		应该手动设置成hdfs.writeFormat=“text”;
	并且hdfs.fileType默认是“SequenceFile”类型的，是将所有event拼成一行，
		应该该手动设置成hdfs.fileType=“DataStream”，这样就可以是一行一个event，与原文件格式保持一致

使用Mapreduce清洗日志文件
	当把日志文件中的数据拉取到HDFS文件系统后，使用Mapreduce程序去进行日志清洗

	第一步，先用Mapreduce过滤掉无效的数据See : logClean.java
	输出数据:
		IP_addr time method request_URL request_protocol respond_code respond_data requst_come_from browser

	第二步，根据访问记录生成相应的Session信息记录，假设Session的过期时间是30分钟
	输出数据:
		time IP_addr session request_URL referal

	第三步，清洗第二步生成的Session信息，生成PageViews信息表
	输出数据:
		session IP_addr time visit_URL stayTime step

	第四步，再次清洗Session日志，并生成Visits信息表
	输出数据:
		SessionID 访问时间 离开时间 第一次访问页面 最后一次访问的页面 访问的页面总数 IP Referal

	以上就是要进行日志清洗的所有MapReduce程序，因为只是一个简单的演示，方法并没有做很好的抽象。



MapReduce Troubleshooting
	指定某个文件夹路径下所有文件作为mapreduce的输入参数的解决方案。
	1.hdfs的文件系统中的路径是支持正则表达式的
	2.使用.setInputDirRecursive(job,true)方法，然后指定文件夹路径

	在分布式环境下如何设置每个用户的SessionID
		可以使用UUID,UUID是分布式环境下唯一的元素识别码，
		它由日期和时间，时钟序列，机器识别码(一般为网卡MAC地址)三部分组成。
		这样就保证了每个用户的SessionID的唯一性。

HIVE建立数据仓库

	使用MapReduce清洗完日志文件后，我们就开始使用Hive去构建对应的数据仓库并使用HiveSql对数据进行分析。
	而在本系统里，我们将使用星型模型来构建数据仓库的ODS(OperationalData Store)层。
	下面的命令我们可以通过启动Hive的hiveserver2服务器并使用beeline客户端进行操作或者直接写脚本去定时调度。

	PageViews数据分析
	PageViews的事实表和维度表结构
	http://www.aboutyun.com/data/attachment/forum/201605/31/113052bpi6i8pio0a8zqoq.jpg

	PageViews事实表:(ssession string,ip string,viewtime string,visitpage string,staytime string,step)
	时间维度表:viewtime (time string,year string,month string,hour string,minutes string,seconds string)
	URL维度表:visitpage (visitpage string,host string,path string,query string)

使用HIVE在数据仓库中创建PageViews的贴源数据表：
create table pageviews(
session string,
ip string,
requestdate string,
requesttime string,
visitpage string,
staytime string,
step string) comment ‘this is the table for pageviews’
partitioned by(inputDate string)
clustered by(session) sorted by(requestdate,requesttime) into 4 buckets
row format delimited fields terminated by ‘ ’;

将HDFS中的数据导入到HIVE的PageViews贴源数据表中
load data inpath ‘/clickstream/pageviews’ overwrite into table pageviews partition(inputDate=‘2016-05-17’);

如果没有标示是在’Local‘本地文件系统中，则会去HDFS中加载数据
根据具体的业务分析逻辑创建ODS层的PageViews事实表，并从PageViews的贴源表中导入数据
这里根据请求的页面URL来分组(clustered)是为了方便统计每个页面的PV

create table ods_pageviews(
session string,
ip string,
viewtime string,
visitpage string,
staytime string,
step string) partitioned by(inputDate string)
clustered by(visitpage) sorted by(viewtime) into 4 buckets
row format delimited fields terminated by ‘ ’;

insert into table ods_pageviews partition(inputDate='2016-05-17')
select pv.session,pv.ip,concat(pv.requestdate,"-",pv.requesttime),pv.visitpage,pv.staytime,pv.step
from pageviews as pv
where pv.inputDate='2016-05-17';

创建PageViews事实表的时间维度表并从当天的事实表里导入数据

create table ods_dim_pageviews_time(
time string,
year string,
month string,
day string,
hour string,
minutes string,
seconds string) partitioned by(inputDate String)
clustered by(year,month,day) sorted by(time) into 4 buckets
row format delimited fields terminated by ' ';

insert overwrite table ods_dim_pageviews_time partition(inputDate='2016-05-17')
select distinct pv.viewtime,
substring(pv.viewtime,0,4),substring(pv.viewtime,6,2),substring(pv.viewtime,9,2),substring(pv.viewtime,12,2),substring(pv.viewtime,15,2),substring(pv.viewtime,18,2)
from ods_pageviews as pv;

创建PageViews事实表的URL维度表并从当天的事实表里导入数据
create table ods_dim_pageviews_url(
visitpage string,
host string,
path string,
query string) partitioned by(inputDate string)
clustered by(visitpage) sorted by(visitpage) into 4 buckets
row format delimited fields terminated by ' ';

insert into table ods_dim_pageviews_url partition(inputDate='2016-05-17')
select distinct pv.visitpage,b.host,b.path,b.query
from pageviews pv
lateral view parse_url_tuple(concat('https://localhost',pv.visitpage),'HOST','PATH','QUERY') b as host,path,query;

查询每天PV总数前20的页面
select op.visitpage as path,count(*) as num
from ods_pageviews as op
join ods_dim_pageviews_url as opurl
on (op.visitpage = opurl.visitpage)
join ods_dim_pageviews_time as optime
on (optime.time = op.viewtime)
where optime.year='2013' and optime.month='09' and optime.day='19'
group by op.visitpage sort by num desc limit 20;


Visits数据分析
	页面具体访问记录Visits的事实表和维度表结构
	时间维度表:(time string,year string,month string,day string,hour string,minutes string,seconds string)
	Visits事实表:(session string,entrytime string,leavetime string,entrypage string,leavepage string,viewpageum string,ip string,referal string)
	URL维度表:(visitpage string, host string, path string,query string)

使用HIVE在数据仓库中创建Visits信息的贴源数据表：

create table visitsinfo(
session string,
startdate string,
starttime string,
enddate string,
endtime string,
entrypage string,
leavepage string,
viewpagenum string,
ip string,
referal string) partitioned by(inputDate string)
clustered by(session) sorted by(startdate,starttime) into 4 buckets
row format delimited fields terminated by ' ';

将HDFS中的数据导入到HIVE的Visits信息贴源数据表中
load data inpath '/clickstream/visitsinfo' overwrite into table visitsinfo partition(inputDate='2016-05-18');

根据具体的业务分析逻辑创建ODS层的Visits事实表，并从visitsinfo的贴源表中导入数据
create table ods_visits(
session string,
entrytime string,
leavetime string,
entrypage string,
leavepage string,
viewpagenum string,
ip string,
referal string) partitioned by(inputDate string)
clustered by(session) sorted by(entrytime) into 4 buckets
row format delimited fields terminated by ' ';

insert into table ods_visits partition(inputDate='2016-05-18')
select vi.session,concat(vi.startdate,"-",vi.starttime),concat(vi.enddate,"-",vi.endtime),vi.entrypage,vi.leavepage,vi.viewpagenum,vi.ip,vi.referal
from visitsinfo as vi where vi.inputDate='2016-05-18';

创建Visits事实表的时间维度表并从当天的事实表里导入数据
create table ods_dim_visits_time(
time string,
year string,
month string,
day string,
hour string,
minutes string,
seconds string) partitioned by(inputDate String)
clustered by(year,month,day) sorted by(time) into 4 buckets
row format delimited fields terminated by ' ';

将“访问时间”和“离开时间”两列的值合并后再放入时间维度表中，减少数据的冗余
insert overwrite table ods_dim_visits_time partition(inputDate='2016-05-18')
select distinct ov.timeparam, substring(ov.timeparam,0,4),substring(ov.timeparam,6,2),substring(ov.timeparam,9,2),substring(ov.timeparam,12,2),substring(ov.timeparam,15,2),substring(ov.timeparam,18,2)
from (
select ov1.entrytime as timeparam from ods_visits as ov1
union
select ov2.leavetime as timeparam from ods_visits as ov2) as ov;

创建visits事实表的URL维度表并从当天的事实表里导入数据
create table ods_dim_visits_url(
pageurl string,
host string,
path string,
query string) partitioned by(inputDate string)
clustered by(pageurl) sorted by(pageurl) into 4 buckets
row format delimited fields terminated by ' ';

将每个session的进入页面和离开页面的URL合并后存入到URL维度表中
insert into table ods_dim_visits_url partition(inputDate='2016-05-18')
select distinct ov.pageurl,b.host,b.path,b.query
from (
select ov1.entrypage as pageurl from ods_visits as ov1
union
select ov2.leavepage as pageurl from ods_visits as ov2 ) as ov
lateral view parse_url_tuple(concat('https://localhost',ov.pageurl),'HOST','PATH','QUERY') b as host,path,query;

将每个session从哪个外站进入当前网站的信息存入到URL维度表中
insert into table ods_dim_visits_url partition(inputDate='2016-05-18')
select distinct ov.referal,b.host,b.path,b.query
from ods_visits as ov
lateral view parse_url_tuple(ov.referal,'HOST','PATH','QUERY') b as host,path,query;

统计每个页面的跳出人数(事实上真正有价值的统计应该是统计页面的跳出率,但为了简单示范,作者在这里简化成统计跳出人数)

select ov.leavepage as jumpPage, count(*) as jumpNum
from ods_visits as ov
group by ov.leavepage order by jumpNum desc;

业务页面转换率分析(漏斗模型)
	Hive在创建表的时候无法实现某个字段自增长的关键字，得使用自定义函数(user-defined function)UDF来实现相应的功能。
	在查询的时候可以使用row_number()来显示行数，不过必须要在complete mode下才能使用，所以可以使用row_number() 函数配合开窗函数over()，具体示例如下。
	为简单起见，这里我们创建一个临时表，并手动在里面插入要查看的业务页面链接以及该页面的PV总数，通过这几个参数来计算业务页面之间的转换率，也就是所谓的漏斗模型。
	假设我们有“/index” -> “/detail” -> “/createOrder” ->”/confirmOrder” 这一业务页面转化流程

	首先我们要创建业务页面的PV的临时信息表，临时表和里面的数据会在session结束的时候清理掉
create temporary table transactionpageviews(url string,views int) row format delimited fields terminated by ' ';

先统计业务页面的总PV然后按转换步骤顺序插入每个页面的PV信息到transactionpageviews表中
insert into table transactionpageviews
select opurl.path as path,count(*) as num
from ods_pageviews as op
join ods_dim_pageviews_url as opurl
on (op.visitpage = opurl.visitpage)
join ods_dim_pageviews_time as optime
on (optime.time = op.viewtime)
where optime.year='2013' and optime.month='09' and optime.day='19' and opurl.path='/index'
group by opurl.path;


insert into table transactionpageviews
select opurl.path as path,count(*) as num
from ods_pageviews as op
join ods_dim_pageviews_url as opurl
on (op.visitpage = opurl.visitpage)
join ods_dim_pageviews_time as optime
on (optime.time = op.viewtime)
where optime.year='2013' and optime.month='09' and optime.day='19' and opurl.path='/detail'
group by opurl.path;

insert into table transactionpageviews
select opurl.path as path,count(*) as num
from ods_pageviews as op
join ods_dim_pageviews_url as opurl
on (op.visitpage = opurl.visitpage)
join ods_dim_pageviews_time as optime
on (optime.time = op.viewtime)
where optime.year='2013' and optime.month='09' and optime.day='19' and opurl.path='/createOrder'
group by opurl.path;

insert into table transactionpageviews
select opurl.path as path,count(*) as num
from ods_pageviews as op
join ods_dim_pageviews_url as opurl
on (op.visitpage = opurl.visitpage)
join ods_dim_pageviews_time as optime
on (optime.time = op.viewtime)
where optime.year='2013' and optime.month='09' and optime.day='19' and opurl.path='/confirmOrder'
group by opurl.path;


计算业务页面之间的转换率
select row_number() over() as rownum,a.url as url, a.views as pageViews,b.views as lastPageViews,a.views/b.views as transferRation
from (
select row_number() over() as rownum,views,url from transactionpageviews) as a
left join (select row_number() over() as rownum,views,url from transactionpageviews) as b
on (a.rownum = b.rownum-1 );

Shell脚本+Crontab定时器执行任务调度
	执行initialEnv.sh脚本初始化系统环境，为了简单测试，作者只启动了单台服务器，
	下面的脚本是建立在Hadoop的standalone单节点模式，并且Hive也装在Hadoop服务器上

执行dataAnalyseTask.sh脚本，先启动MapReduce程序去清洗当日的日志信息，随后使用Hive去构建当日的ODS数据。
需要注意的是，本脚本是建立在ODS层中事实表和维度表已经创建完毕的基础上去执行，
所以脚本中不会有创建事实表和维度表的HIVE语句（创建语句见上一个章节的内容），并且为了节省篇幅，只列出了PageViews数据分析的脚本部分。

创建crontab文件，指定每天的凌晨01点整执行dataAnalyseTask.sh脚本，
该脚本执行“使用MapReduce清理日志文件”和“使用HiveSql构建分析ODS层数据”两项任务，并将用户自定义的crontab文件加入到定时器中


至此，使用Hadoop进行离线计算的简单架构和示例已经全部阐述完毕，
而关于如何使用Sqoop将Hive中的数据导入Mysql中，因为篇幅有限，这里就不展开了。
作者刚开始接触分布式离线计算，文章中尚有许多不足的地方，欢迎大家提出宝贵意见并做进一步交流。
