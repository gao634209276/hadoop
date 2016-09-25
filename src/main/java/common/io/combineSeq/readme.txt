众所周知，Hadoop对处理单个大文件比处理多个小文件更有效率，
另外单个文件也非常占用HDFS的存储空间。所以往往要将其合并起来。

1，getmerge
	hadoop有一个命令行工具getmerge，
	用于将一组HDFS上的文件复制到本地计算机以前进行合并
	参考：http://hadoop.apache.org/common/docs
	使用方法：hadoop fs -getmerge <src> <localdst> [addnl]
	接受一个源目录和一个目标文件作为输入，并且将源目录中所有的文件连接成本地目标文件。
	addnl是可选的，用于指定在每个文件结尾添加一个换行符。

	调用文件系统(FS)Shell命令应使用 bin/hadoop fs <args>的形式。
	所有的的FS shell命令使用URI路径作为参数。URI格式是scheme://authority/path。

2.putmerge
将本地小文件合并上传到HDFS文件系统中。
	See:hdfs.putmerge.java
	
	一种方法可以现在本地写一个脚本，
		先将一个文件合并为一个大文件，然后将整个大文件上传，
		这种方法占用大量的本地磁盘空间；
	另一种方法如下，在复制的过程中上传。参考：《hadoop in action》


3.将小文件打包成SequenceFile的MapReduce任务
	来自：《hadoop权威指南》
	实现将整个文件作为一条记录处理的InputFormat:
	See:WholeFileInputFormat
	实现上面类中使用的定制的RecordReader：
	See:WholeFileRecordReader
	将小文件打包成SequenceFile：
	See:SmallFilesToSequenceFileConverter
	
	










