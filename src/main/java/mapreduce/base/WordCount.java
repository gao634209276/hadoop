package mapreduce.base;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {

		Job job = Job.getInstance(new Configuration());
		job.setJarByClass(WordCount.class);
		job.setMapperClass(WCMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		FileInputFormat.setInputPaths(job, new Path(
				"hdfs://hadoop:9000/user/hadoop/mr/wc/input/"));

		job.setReducerClass(WCReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		job.setOutputValueClass(LongWritable.class);
		FileOutputFormat.setOutputPath(job, new Path(
				"hdfs://hadoop:9000/user/hadoop/mr/wc/output"));

		job.waitForCompletion(true);
	}

	public class WCMapper extends
			Mapper<LongWritable, Text, Text, LongWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			// 切分数据
			String[] words = line.split(" ");
			for (String word : words) {
				// 循环输出
				context.write(new Text(word), new LongWritable(1));
			}

		}

	}

	public class WCReducer extends
			Reducer<Text, LongWritable, Text, LongWritable> {

		@Override
		protected void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {
			long counter = 0;
			for (LongWritable value : values) {
				counter += value.get();
			}
			context.write(key, new LongWritable(counter));
		}

	}

}
