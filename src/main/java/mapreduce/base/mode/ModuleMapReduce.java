package mapreduce.base.mode;

import java.io.IOException;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
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

/**
 * MapReduce
 * 这里通过使用util中的ToolRunner以及Tool封装的run方法进行提交job
 * <p>
 * main()-->ToolRunner.run-->new Tool().run-->conf/create/submitJob
 */
public class ModuleMapReduce extends Configured implements Tool {
	/**
	 * Mapper Class
	 */
	public static class MpduleMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			super.cleanup(context);
		}

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			super.map(key, value, context);
		}

		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
		}

	}

	/**
	 * Reduce Class
	 */
	public static class ModuleReducer extends Reducer<LongWritable, Text, Text, LongWritable> {

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			super.cleanup(context);
		}

		@Override
		public void reduce(LongWritable key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			super.reduce(key, values, context);
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

		/**
		 * step 1:set configuration
		 */
		Configuration conf = new Configuration();

		/**
		 * 对参数解析,参数包含input,output的path
		 */
		Job job = parseInputAndOutput(this, conf, args);

		/**
		 * step 3: set job
		 */
		// 1: set run jar class
		job.setJarByClass(ModuleMapReduce.class);
		// 2: set input format
		job.setInputFormatClass(TextInputFormat.class);
		// 3: set input path
		FileInputFormat.addInputPath(job, new Path(args[0]));
		// 4: set mapper
		job.setMapperClass(Mapper.class);
		// 5: set map output key/value class
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		// 6: set partitioner class
		// job.setPartitionerClass(HashPartitioner.class);
		// 7: set reducer task number
		job.setNumReduceTasks(1);
		// 8: set sort comparator class
		// job.setSortComparatorClass(LongWritable.Comparator.class);
		// 9: set group comparator class
		// job.setGroupingComparatorClass(LongWritable.Comparator.class);
		// 10: set combiner class
		// job.setCombinerClass(null);
		// 11: set reduce class
		job.setReducerClass(Reducer.class);
		// 12: set output format
		job.setOutputFormatClass(TextOutputFormat.class);
		// 13: job output key/value class
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		// 14:set job output path
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		/**
		 * step 4 : submit job
		 */
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
		Job job = Job.getInstance(conf, tool.getClass().getSimpleName());
		return job;
	}

	public static void main(String[] args) throws Exception {
		// run map reduce
		int status = ToolRunner.run(new ModuleMapReduce(), args);
		// step 5 exit
		System.exit(status);
	}

}
