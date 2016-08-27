http://blog.csdn.net/kwu_ganymede/article/details/51226365
打包运行：
Hadoop jar hadoop-Project.jar com.ganymede.hadoop.shuffle.WordCountShuffle  /temp/input /temp/output1
4、作业运行分析
1 ) map端，可以看出map端即做了Sort 与 Combiner ，通过partition 分发数据到reduce端
说明： map输出文件位于运行map任务tasktracker的本地磁盘。
2 ) reduce端，对map端发来的数据进行 merge合并
根据自定义的partitions应该分为两个reduce来处理
a)  一个专门处理hello的key，对于大数据量中某些有热点key的mapreduce常用到
b) 另一个处理其他的key，非热点key
说明： 在reduce阶段，对已排序输出中每个键都要调用 reduce函数，此阶段的输出直接写到输出文件系统，如HDFS.


