package mapreduce.base.inputformat.custom;


import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

/**
 * 系统默认的LineRecordReader是按照每行的偏移量做为map输出时的key值，每行的内容作为map的value值，默认的分隔符是回车和换行。
 * 现在要更改map对应的输入的<key,value>值，key对应的文件的路径（或者是文件名），value对应的是文件的内容（content）。
 * 那么我们需要重写InputFormat和RecordReader，因为RecordReader是在InputFormat中调用的，当然重写RecordReader才是重点！
 *
 * 然后再在main函数中设置InputFormat对应的类，就可以使用这种新的读入格式了。
 * 对于那些需要对整个文档进行处理的工作来说，还是比较有效的。
 */
public class chDicInputFormat extends FileInputFormat<Text, Text> implements JobConfigurable {

	private CompressionCodecFactory compressionCodecs = null;

	public void configure(JobConf conf) {
		compressionCodecs = new CompressionCodecFactory(conf);
	}

	protected boolean isSplitable(FileSystem fs, Path file) {
		//  CompressionCodec codec = compressionCodecs.getCode(file);
		return false;//以文件为单位，每个单位作为一个split，即使单个文件的大小超过了64M，也就是Hadoop一个块得大小，也不进行分片
	}

	@Override
	public RecordReader<Text, Text> getRecordReader(InputSplit genericSplit,
	                                                JobConf job, Reporter reporter) throws IOException {
		reporter.setStatus(genericSplit.toString());
		return new chDicRecordReader(job, (FileSplit) genericSplit);
	}


}
