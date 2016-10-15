package mapreduce.base;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 求最大最小值问题，同时在一个任务中求出来
 */
public class MaxMin {

	public static void main(String[] args) throws Exception {
		Configuration configuration = new Configuration();

		if (args == null || args.length == 0) {
			args = new String[]{"file/maxMin", "target/out"};
		}

		Job job = Job.getInstance(configuration, "max_min_job");

		job.setJarByClass(MaxMin.class);

		job.setMapperClass(MaxMinMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);

		job.setReducerClass(MaxMinReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		Path path = new Path(args[1]);
		FileSystem fs = FileSystem.get(configuration);
		if (fs.exists(path)) {
			fs.delete(path, true);
		}
		FileOutputFormat.setOutputPath(job, path);

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

	/**
	 * map端读取每行并解析为一个Long型值,使用统一的Key,输出为Key LongWritable值
	 */
	public static class MaxMinMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

		private Text keyText = new Text("Key");

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			String line = value.toString();
			if (line.trim().length() > 0) {
				context.write(keyText, new LongWritable(Long.parseLong(line.trim())));
			}
		}

	}

	/**
	 * reduce端对value集合循环求出最大值和最小值
	 */
	public static class MaxMinReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

		@Override
		protected void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {

			long max = Long.MIN_VALUE;
			long min = Long.MAX_VALUE;
			for (LongWritable val : values) {
				if (val.get() > max) {
					max = val.get();
				}
				if (val.get() < min) {
					min = val.get();
				}
			}
			context.write(new Text("Max"), new LongWritable(max));
			context.write(new Text("Min"), new LongWritable(min));
		}
	}
}
