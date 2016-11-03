package mapreduce.base;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 日志分析：分析非结构化文件
 * 需求：根据tomcat日志计算url访问了情况，See :file/tomcat/log.txt
 * 要求：区别统计GET和POST URL访问量
 * 结果为：访问方式、URL、访问量
 */
public class LogMR {

	public static void main(String[] args) throws Exception {
		Configuration configuration = new Configuration();

		Job job = Job.getInstance(configuration, "log_job");
		job.setJarByClass(LogMR.class);

		job.setMapperClass(LogMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setReducerClass(LogReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		Path path = new Path(args[1]);
		FileSystem fs = FileSystem.get(configuration);
		if (fs.exists(path)) {
			fs.delete(path, true);
		}
		FileOutputFormat.setOutputPath(job, path);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	private static class LogMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		private IntWritable val = new IntWritable(1);

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String line = value.toString().trim();
			String tmp = handlerLog(line);
			if (tmp.length() > 0) {
				context.write(new Text(tmp), val);
			}
		}

		// 127.0.0.1 - - [03/Jul/2014:23:36:38 +0800]
		// "GET /course/detail/3.htm HTTP/1.0" 200 38435 0.038
		private String handlerLog(String line) {
			String result = "";
			try {
				if (line.length() > 20) {
					if (line.indexOf("GET") > 0) {
						result = line.substring(line.indexOf("GET"), line.indexOf("HTTP/1.0")).trim();
					} else if (line.indexOf("POST") > 0) {
						result = line.substring(line.indexOf("POST"), line.indexOf("HTTP/1.0")).trim();
					}
				}
			} catch (Exception e) {
				System.out.println(line);
			}

			return result;
		}

		public static void main(String[] args) {
			String line = "127.0.0.1 - - [03/Jul/2014:23:36:38 +0800] \"GET /course/detail/3.htm HTTP/1.0\" 200 38435 0.038";
			System.out.println(new LogMapper().handlerLog(line));
		}
	}

	private static class LogReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			context.write(key, new IntWritable(sum));
		}
	}

}
