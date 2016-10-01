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

//import com.hadoop.mapreduce.LzoTextInputFormat;

public class WordCountMapReduce extends Configured implements Tool {
	/**
	 * Mapper Class
	 */
	public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			super.cleanup(context);
		}

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}

		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
		}

	}

	/**
	 * Reduce Class
	 */
	public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			super.cleanup(context);
		}

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

		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
		}

	}

	/**
	 * Driver
	 */
	@Override
	public int run(String[] args) throws Exception {

		// step 1:set configuration
		Configuration conf = new Configuration();

		Job job = parseInputAndOutput(this, conf, args);

		// step 3: set job
		// 1: set run jar class
		job.setJarByClass(WordCountMapReduce.class);
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

		args = new String[] { "hdfs://hadoop:9000/opt/wc/input",
				"hdfs://hadoop:9000/opt/wc/output" };
		// run map reduce
		int status = ToolRunner.run(new WordCountMapReduce(), args);
		// step 5 exit
		System.exit(status);
	}

}
