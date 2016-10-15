package mapreduce.base;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * 自定义技术器(敏感词汇),自定义Combiner在map端对key聚合
 * 自定义partition通过key是否为敏感词汇分区,设置2个reduce聚合
 * 并分别在各阶段相应过程中输出信息
 */
public class WordCountShuffle {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "WordCountShuffle");
		job.setJarByClass(WordCountShuffle.class);

		if (args == null || args.length == 0) {
			args = new String[]{"file/wc", "target/out"};
		}
		FileSystem fs = FileSystem.get(conf);
		fs.delete(new Path(args[1]), true); // 删除输出路径

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setInputFormatClass(TextInputFormat.class);
		job.setMapperClass(WordCountShuffleMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);

		job.setCombinerClass(MyCombiner.class);
		job.setPartitionerClass(HelloPartitioner.class);
		job.setNumReduceTasks(2); //自定义partition对map输出分为两个partition,这里设置2个reduce

		job.setReducerClass(WordCountShuffleReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		boolean isSuccess = job.waitForCompletion(true);

		System.exit(isSuccess ? 0 : 1);

	}

	/**
	 * map端，做了Sort 与 Combiner
	 * 通过partition 分发数据到reduce端
	 * map输出文件位于运行map任务yarnChild所在节点的本地磁盘。
	 */
	private static class WordCountShuffleMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();

			/**
			 * 通过context.getCounter自定义一个记录计数器
			 * groupName为Sensitive Words,counterName为hello
			 * 使用String.contains方法判断是否含有hello,如果含有计数器+1
			 */
			Counter helloCounter = context.getCounter("Sensitive Words", "hello");
			if (line.contains("hello")) {
				helloCounter.increment(1L);
			}

			String[] splited = line.split(" ");
			for (String word : splited) {
				System.out.println("Mapper 输出  <" + word + " , " + 1 + ">");
				context.write(new Text(word), new LongWritable(1));
			}
		}
	}

	/**
	 * reduce端，对map端发来的数据进行merge
	 * 在reduce阶段，对已排序输出中每个键都要调用 reduce函数，
	 * 此阶段的输出直接写到输出文件系统，如HDFS等
	 */
	private static class WordCountShuffleReduce extends Reducer<Text, LongWritable, Text, LongWritable> {

		@Override
		protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

			// 显示次数表示Combiner函数被调用了多少, 表示k2有多少个分组
			System.out.println("reduce 输入分组  <" + key + " , ...>");

			long count = 0L;
			for (LongWritable v2 : values) {
				count += v2.get();

				// 显示次数表示输入的键值对数量
				System.out.println("reduce 输入键值对<" + key + " , " + v2.get() + ">");
			}

			context.write(key, new LongWritable(count));
			// 显示次数表示输入的键值对数量
			System.out.println("reduce 输出键值对  <" + key + " , " + count + ">");
		}
	}

	/**
	 * 自定义Combiner
	 */
	private static class MyCombiner extends Reducer<Text, LongWritable, Text, LongWritable> {

		@Override
		protected void reduce(Text key, Iterable<LongWritable> values,
		                      Context context) throws IOException, InterruptedException {

			// 显示次数表示Combiner函数被调用了多少, 表示k2有多少个分组
			System.out.println("Combiner 输入分组  <" + key + " , ...>");

			long count = 0L;
			for (LongWritable v2 : values) {
				count += v2.get();
				// 显示次数表示输入的键值对数量
				System.out.println("Combiner 输入键值对<" + key + " , " + v2.get() + ">");
			}

			context.write(key, new LongWritable(count));
			// 显示次数表示输入的键值对数量
			System.out.println("Combiner 输出键值对  <" + key + " , " + count + ">");
		}
	}

	/**
	 * 自定义Partition类，对热点key进行分Partition来处理，分发到不同的reduce中
	 * 根据自定义的partitions应该分为两个reduce来处理
	 */
	private static class HelloPartitioner extends Partitioner<Text, LongWritable> {

		@Override
		public int getPartition(Text key, LongWritable value, int numReduceTasks) {

			System.out.println("分区操作： " + key);
			return (key.toString().contains("hello")) ? 0 : 1;
		}
	}
}
