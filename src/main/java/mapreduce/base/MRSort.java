package mapreduce.base;

import java.io.IOException;

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

/**
 * 数据排序是许多实际任务执行时要完成的第一项工作
 * <p>
 * 1）需求描述
 * 对输入文件中数据进行排序。输入文件中的每行内容均为一个数字，即一个数据。
 * 要求在输出中每行有两个间隔的数字，其中，第一个代表原始数据在原始数据集中的位次，第二个代表原始数据。
 * 这个实例仅仅要求对输入数据进行排序,可以利用这个MR默认的排序，不需要自己再实现具体的排序,
 * <p>
 * 但是在使用之前首先需要了解它的默认排序规则。
 * 它是按照key值进行排序的，如果key为封装int的IntWritable类型，那么MapReduce按照数字大小对key排序，
 * 如果key为封装为String的Text类型，那么MapReduce按照字典顺序对字符串排序。
 * <p>
 * 了解了这个细节，我们就知道应该使用封装int的IntWritable型数据结构了。
 * 需要注意的是这个程序中没有配置Combiner，也就是在MapReduce过程中不使用Combiner。
 * 这主要是因为使用map和reduce就已经能够完成任务了。
 */
public class MRSort {

	public static void main(String[] args) throws Exception {


		if (args == null || args.length == 0) {
			args = new String[]{"file/sort", "target/out"};
		}

		Configuration configuration = new Configuration();
		Job job = Job.getInstance(configuration, "sort-job");
		job.setJarByClass(MRSort.class);

		job.setMapperClass(SortMapper.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setReducerClass(SortReducer.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileSystem fs = FileSystem.get(configuration);
		Path outputDir = new Path(args[1]);
		if (fs.exists(outputDir)) {
			fs.delete(outputDir, true);
		}
		FileOutputFormat.setOutputPath(job, outputDir);

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

	/**
	 * map中将读入的数据转化成 IntWritable型，然后作为key值输出（value任意）
	 */
	private static class SortMapper extends Mapper<LongWritable, Text, IntWritable, Text> {

		private Text val = new Text("");

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			String line = value.toString();

			if (line.trim().length() > 0) {
				context.write(new IntWritable(Integer.valueOf(line.trim())), val);
			}
		}
	}

	/**
	 * reduce拿到<key，value-list>之后，将输入的 key作为value输出，并根据value-list中元素的个数决定输出的次数。
	 * 输出的key（即代码中的num）是一个全局变量，它统计当前key的位次
	 */
	private static class SortReducer extends Reducer<IntWritable, Text, IntWritable, IntWritable> {

		private IntWritable num = new IntWritable(1);

		@Override
		protected void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

			for (Text val : values) {
				context.write(num, key);
				num = new IntWritable(num.get() + 1);
			}
		}
	}
}
