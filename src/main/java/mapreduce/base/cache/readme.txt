DistributedCache 是一个提供给Map/Reduce框架的工具，
用来缓存文件（text, archives, jars and so on）文件的默认访问协议为(hdfs://).
DistributedCache将拷贝缓存的文件到Slave节点在任何Job在节点上执行之前。
文件在每个Job中只会被拷贝一次，缓存的归档文件会被在Slave节点中解压缩。

符号链接
	每个存储在HDFS中的文件被放到缓存中后都可以通过一个符号链接使用。
	URI hdfs://namenode/test/input/file1#myfile 你可以在程序中直接使用myfile来访问 file1这个文件。myfile是一个符号链接文件。

	缓存在本地的存储目录mapred.local.dir设定,同时可以限制local.cache.size
	实际在DataNode节点中的存储目录：${Hadoop.tmp.dir}//mapred/local/xx

DistributedCache
	DistributedCache 可将具体应用相关的、大尺寸的、只读的文件有效地分布放置。
	DistributedCache 是Map/Reduce框架提供的功能，能够缓存应用程序所需的文件 （包括文本，档案文件，jar文件等）。

	应用程序在JobConf中通过url(hdfs://)指定需要被缓存的文件。
	DistributedCache假定由hdfs://格式url指定的文件已经在 FileSystem上了。
	Map-Redcue框架在作业所有任务执行之前会把必要的文件拷贝到slave节点上。
	它运行高效是因为每个作业的文件只拷贝一次并且为那些没有文档的slave节点缓存文档。

	DistributedCache 根据缓存文档修改的时间戳进行追踪。 在作业执行期间，当前应用程序或者外部程序不能修改缓存文件。
	distributedCache可以分发简单的只读数据或文本文件，也可以分发复杂类型的文件例如归档文件和jar文件。
	归档文件(zip,tar,tgz和tar.gz文件)在slave节点上会被解档（un-archived）。 这些文件可以设置执行权限。

	用户可以通过设置mapred.cache.{files|archives}来分发文件。
	如果要分发多个文件，可以使用逗号分隔文件所在路径。
	也可以利用API来设置该属性：
		DistributedCache.addCacheFile(URI,conf)/ DistributedCache.addCacheArchive(URI,conf)
		DistributedCache.setCacheFiles(URIs,conf)/ DistributedCache.setCacheArchives(URIs,conf)
	其中URI的形式是 hdfs://host:port/absolute-path#link-name 在Streaming程序中，可以通过命令行选项 -cacheFile/-cacheArchive 分发文件。

	用户可以通过 DistributedCache.createSymlink(Configuration)方法让DistributedCache 在当前工作目录下创建到缓存文件的符号链接。
	或者通过设置配置文件属性mapred.create.symlink为yes。 分布式缓存会截取URI的片段作为链接的名字。
	例如，URI是 hdfs://namenode:port/lib.so.1#lib.so， 则在task当前工作目录会有名为lib.so的链接， 它会链接分布式缓存中的lib.so.1。

	DistributedCache可在map/reduce任务中作为 一种基础软件分发机制使用。
	它可以被用于分发jar包和本地库（native libraries）。
	DistributedCache.addArchiveToClassPath(Path, Configuration)和
	DistributedCache.addFileToClassPath(Path, Configuration) API能够被用于 缓存文件和jar包，并把它们加入子jvm的classpath。
	也可以通过设置配置文档里的属性 mapred.job.classpath.{files|archives}达到相同的效果。缓存文件可用于分发和装载本地库。
