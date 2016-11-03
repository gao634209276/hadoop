package mapreduce.nginxlog.logclean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 使用Mapreduce清洗日志文件
 * 当把日志文件中的数据拉取到HDFS文件系统后，使用Mapreduce程序去进行日志清洗
 * <p>
 * 第一步，先用Mapreduce过滤掉无效的数据
 * 仅仅通过map端通过WebLogParser的parser对每条log解析,
 * 其中将对应的数据信息组装为一个JavaBean类实例对象weblogbean
 * map输出的key为WebLogParser的toString,value为null
 */
public class logClean {


	private static class cleanMap extends Mapper<Object, Text, Text, NullWritable> {

		private NullWritable v = NullWritable.get();
		private Text word = new Text();
		WebLogParser webLogParser = new WebLogParser();

		public void map(Object key, Text value, Context context) {

			// 将一行内容转成string
			String line = value.toString();
			String cleanContent = webLogParser.parser(line);
			if (cleanContent != "") {
				word.set(cleanContent);
				try {
					context.write(word, v);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		// conf.set("fs.defaultFS", "hdfs://hadoop:9000");
		Job job = Job.getInstance(conf);
		job.setJarByClass(logClean.class);

		job.setMapperClass(cleanMap.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		//job.setNumReduceTasks(0);

		String dateStr = new SimpleDateFormat("yy-MM-dd").format(new Date());

		FileInputFormat.setInputPaths(job, new Path("file/log/flume" + dateStr + "/*/*"));
		FileOutputFormat.setOutputPath(job, new Path("file/log/cleandata" + dateStr + "/"));

		boolean res = job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}

}