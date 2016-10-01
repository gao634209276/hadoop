package mapreduce.demo.combine_partition;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

public class WordCountShuffle {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "WordCountShuffle");
		job.setJarByClass(WordCountShuffle.class); // 打包在集群中运行必须设置主类的 class

		if (args == null || args.length == 0) {
			args = new String[2];
			args[0] = "hdfs://hadoop:9000/temp/input/hello";
			args[1] = "hdfs://hadoop:9000/temp/output";
		}
		FileSystem fs = FileSystem.get(conf);
		fs.delete(new Path(args[1]), true); // 删除输出路径

		FileInputFormat.setInputPaths(job, new Path(args[0]));

		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(WordCountShuffleMapper.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);

		job.setCombinerClass(MyCombiner.class);
		job.setPartitionerClass(HelloPartitioner.class);
		job.setNumReduceTasks(2);

		job.setReducerClass(WordCountShuffleReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setOutputFormatClass(TextOutputFormat.class);

		boolean isSuccessed = job.waitForCompletion(true);

		System.exit(isSuccessed ? 0 : 1);

	}

	static class WordCountShuffleMapper extends
			Mapper<LongWritable, Text, Text, LongWritable> {
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();

			// 自定义计数器
			Counter helloCounter = context.getCounter("Sensitive Words ",
					"hello");
			if (line.contains("hello")) {
				// 记录敏感词出现在一行中
				helloCounter.increment(1L);
			}

			String[] splited = line.split(" ");
			for (String word : splited) {
				System.out.println("Mapper 输出  <" + word + " , " + 1 + ">");
				context.write(new Text(word), new LongWritable(1));
			}
		};
	}

	static class WordCountShuffleReduce extends
			Reducer<Text, LongWritable, Text, LongWritable> {
		@Override
		protected void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {
			// 显示次数表示Combiner函数被调用了多少, 表示k2有多少个分组
			System.out.println("reduce 输入分组  <" + key + " , ...>");

			long count = 0l;

			for (LongWritable v2 : values) {
				count += v2.get();

				// 显示次数表示输入的键值对数量
				System.out.println("reduce 输入键值对<" + key + " , " + v2.get()
						+ ">");
			}

			context.write(key, new LongWritable(count));
			// 显示次数表示输入的键值对数量
			System.out.println("reduce 输出键值对  <" + key + " , " + count + ">");
		};

	}

	/**
	 * 自定义Comiber
	 * 
	 * @author Ganymede
	 * 
	 */
	static class MyCombiner extends
			Reducer<Text, LongWritable, Text, LongWritable> {
		@Override
		protected void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {
			// 显示次数表示Combiner函数被调用了多少, 表示k2有多少个分组
			System.out.println("Combiner 输入分组  <" + key + " , ...>");
			long count = 0l;

			for (LongWritable v2 : values) {
				count += v2.get();

				// 显示次数表示输入的键值对数量
				System.out.println("Combiner 输入键值对<" + key + " , " + v2.get()
						+ ">");
			}
			context.write(key, new LongWritable(count));
			// 显示次数表示输入的键值对数量
			System.out.println("Combiner 输出键值对  <" + key + " , " + count + ">");
		};

	}

	/**
	 * 自定义Partitioner，对热点key进行分Partition来处理，分发到不同的reduce中
	 * 
	 * @author Ganymede
	 * 
	 */
	static class HelloPartitioner extends HashPartitioner<Text, LongWritable> {
		@Override
		public int getPartition(Text key, LongWritable value, int numReduceTasks) {
			System.out.println("分区操作： " + key);
			return (key.toString().contains("hello")) ? 0 : 1;
		}

	}
}
