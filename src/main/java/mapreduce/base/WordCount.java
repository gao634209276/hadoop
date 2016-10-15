package mapreduce.base;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class WordCount extends Configured implements Tool {

	/**
	 * Mapper将kv对映射为一个中间结果的kv对
	 * 一对输入kv对经过map产生的中间结果kv可能为0-n对
	 */
	public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		/**
		 * cleanup方法在一个map task结束时候调用一次
		 */
		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			super.cleanup(context);
		}

		/**
		 * 输入分片的每个kv对调用一次map方法
		 *
		 * @param key     LineRecordReader读取InputSplit解析的行数
		 * @param value   LineRecordReader读取InputSplit读取key行对应的数据
		 * @param context mapper上下文
		 */
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}

		/**
		 * 一个map task启动的时候调用一次
		 */
		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
		}

	}

	/**
	 * Reducer是所有用户定制Reducer类的基础
	 * Reducer将对一组共享一个key的value集合的中间结果进行reduce操作,在reduce之前有三个阶段:
	 * <p>
	 * shuffle:Reducer使用网络HTTP方式copy每个mapper的中间数据结果
	 * sort:由于不同的mapper输出可能含有相同的key,框架将merge排序的reducer输入
	 * shuffle和sort将同时发生,也就是说当output被接收到时他们也已经被merge
	 * <p>
	 * SecondarySort:为了能够获取二次排序的values,改值通过iterator返回,
	 * 程序应该对key扩展一个secondary key,并且定义一个分组比较器
	 * 所有keys使用这个key进行排序,但分组比较器将决定那些kv对分组发给那些reduce
	 * 分组比较器通过Job.setGroupingComparatorClass(Class)设置,
	 * 排序比较器通过Job.setSortComparatorClass(Class)控制
	 * 如使用a和b为key,设置outputKeyComparator为a升序b降序,设置outputValueGroupingComparator为a
	 * <p>
	 * Reduce:在该阶段,每个排过序的inputs将调用reduce方法,
	 * reduce task的输出通过reducer.context.write写入到一个RecordWriter,Reducer的输出将不会再次排序
	 */
	public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			super.cleanup(context);
		}

		/**
		 * @param key     Mapper处理结果的key
		 * @param values  key对应的所有value的一个迭代器
		 * @param context Reducer的上下文
		 */
		@Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);
			context.write(key, result);
		}

		/**
		 * 一个reduce task启动的时候调用一次
		 */
		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
		}

	}

	@Override
	public int run(String[] args) throws Exception {

		// step 1:set configuration
		Configuration conf = new Configuration();

		Job job = parseInputAndOutput(this, conf, args);

		// step 3: set job
		// 1: set run jar class
		job.setJarByClass(WordCount.class);
		// 2: set input format
		//job.setInputFormatClass(LzoTextInputFormat.class);
		job.setInputFormatClass(TextInputFormat.class);
		// 3: set input path
		FileInputFormat.addInputPath(job, new Path(args[0]));
		// 4: set mapper
		job.setMapperClass(WordCountMapper.class);
		// 5: set map output key/value class
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		// 6: set partitioner class
		// job.setPartitionerClass(HashPartitioner.class);
		// 7: set reducer task number
		job.setNumReduceTasks(1);
		// 8: set sort comparator class
		// job.setSortComparatorClass(LongWritable.Comparator.class);
		// 9: set group comparator class
		// job.setGroupingComparatorClass(LongWritable.Comparator.class);
		// 10: set combiner class
		job.setCombinerClass(WordCountReducer.class);
		// 11: set reduce class
		job.setReducerClass(WordCountReducer.class);
		// 12: set output format
		job.setOutputFormatClass(TextOutputFormat.class);
		// 13: job output key/value class
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		// 14:set job output path
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		// step 4 submit job
		boolean isSuccess = job.waitForCompletion(true);
		return isSuccess ? 0 : 1;

	}

	public Job parseInputAndOutput(Tool tool, Configuration conf, String[] args) throws Exception {

		// validate
		if (args.length != 2) {
			System.err.printf("Usage: %s [generic options] <input> <output> \n");
			return null;
		}

		// step 2:create job
		//Job job = new Job(conf, tool.getClass().getSimpleName());
		Job job = Job.getInstance(conf, tool.getClass().getSimpleName());
		return job;
	}

	public static void main(String[] args) throws Exception {

		args = new String[]{"hdfs://hadoop:9000/opt/wc/input",
				"hdfs://hadoop:9000/opt/wc/output"};
		// run map reduce
		int status = ToolRunner.run(new WordCount(), args);
		// step 5 exit
		System.exit(status);
	}

}
