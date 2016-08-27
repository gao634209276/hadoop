package mapreduce.app.topk;

import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TopKMapReduceV5 {
	public static final int KEY = 3;

	// Mapper Class
	static class TopKMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
		// map key set
		private Text mapOutputKey = new Text();
		private LongWritable mapOutputValue = new LongWritable();

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			// get value
			String lineValue = value.toString();
			// split
			String[] strs = lineValue.split("\t");
			long tempValue = Long.valueOf(strs[1]);
			// set map output
			mapOutputKey.set(strs[0]);
			mapOutputValue.set(tempValue);
			// context.write(new Text(strs[0]), new LongWritable(tempValue));
			context.write(mapOutputKey, mapOutputValue);
		}

		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
		}

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			super.cleanup(context);
		}
	}

	// Reduce Class
	static class TopKReduce extends Reducer<Text, LongWritable, Text, LongWritable> {
		TreeSet<TopKWritable> topSet = new TreeSet<TopKWritable>(new Comparator<TopKWritable>() {

			@Override
			public int compare(TopKWritable o1, TopKWritable o2) {
				return o1.getCount().compareTo(o2.getCount());
			}
		});

		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
		}

		@Override
		public void reduce(Text key, Iterable<LongWritable> values, Context context)
				throws IOException, InterruptedException {
			long count = 0L;
			// get count
			for (LongWritable value : values) {
				count += value.get();
			}
			// add
			topSet.add(new TopKWritable(key.toString(), count));
			// comparator
			if (topSet.size() > KEY) {
				topSet.remove(topSet.first());
			}
		}

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			for (TopKWritable top : topSet) {
				context.write(new Text(top.getWord()), new LongWritable(top.getCount()));
			}
		}
	}

	// Driver Code
	public int run(String[] args) throws Exception {

		// get conf
		Configuration conf = new Configuration();
		// create job
		Job job = new Job(conf, TopKMapper.class.getSimpleName());
		// set
		job.setJarByClass(TopKMapper.class);
		// 1) input
		Path inputDir = new Path(args[0]);
		FileInputFormat.addInputPath(job, inputDir);
		// 2) map
		job.setMapperClass(TopKMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		// set reduce task
		job.setNumReduceTasks(1);// default reduce tasks is one
		// 3) reduce 
		job.setReducerClass(TopKReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		// 4) output
		Path outputDir = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outputDir);
		// submint job
		boolean isSuccess = job.waitForCompletion(true);
		return isSuccess ? 0 : 1;
	}

	// run mapreduce
	public static void main(String[] args) throws Exception {

		// set args
		args = new String[] {
				// input path
				"hdfs://hadoop-master:9000/opt/topkey/input1",
				// output path
				"hdfs://hadoop-master:9000/opt/topkey/output1" };
		// run job
		int status = new TopKMapReduceV5().run(args);
		// exit
		System.exit(status);

	}

}
