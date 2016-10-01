package mapreduce.base.mode.min;

import org.apache.hadoop.conf.Configuration;
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
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;


public class DefaultMapReduce {

	public static void main(String[] args) throws Exception {

		args = new String[] { "hdfs://hadoop-master.dragon.org:9000/opt/test/input",
				"hdfs://hadoop-master.dragon.org:9000/opt/test/output" };
		// step 1: configuration
		Configuration conf = new Configuration();
		// step 2:create job
		Job job = Job.getInstance(conf, DefaultMapReduce.class.getSimpleName());
		// step 3: set job

		// 1: set run jar class
		job.setJarByClass(DefaultMapReduce.class);
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
		job.setPartitionerClass(HashPartitioner.class);
		// 7: set reducer task number
		job.setNumReduceTasks(1);
		// 8: set sort comparator class
		job.setSortComparatorClass(LongWritable.Comparator.class);
		// 9: set group comparator class
		job.setGroupingComparatorClass(LongWritable.Comparator.class);
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

		// step 4 submit job
		boolean isSuccess = job.waitForCompletion(true);

		// step 5 exit
		System.exit(isSuccess ? 0 : 1);
	}
}
