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
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *ʹ���Զ���������ͷ�װk/v
 *��������Ƚ���
 */
public class TopKMapReduceV4 {

	// Mapper Class
	static class TopKMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
		public static final int KEY = 3;
		// map key set
		TreeSet<TopKWritable> topSet = new TreeSet<TopKWritable>(new Comparator<TopKWritable>() {

			@Override
			public int compare(TopKWritable o1, TopKWritable o2) {
				return o1.getCount().compareTo(o2.getCount());
			}
		});

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			// get value
			String lineValue = value.toString();
			// split
			String[] strs = lineValue.split("\t");
			// add topset
			TopKWritable topWritable = new TopKWritable();
			topWritable.set(strs[0], Long.valueOf(strs[1]));
			topSet.add(topWritable);
			if (topSet.size() > KEY) {
				topSet.remove(topSet.first());
			}
		}

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
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
		// set reduce task number is 0,no reduce task
		job.setNumReduceTasks(0);
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
				"hdfs://hadoop-master:9000/opt/topkey/input",
				// output path
				"hdfs://hadoop-master:9000/opt/topkey/output" };
		// run job
		int status = new TopKMapReduceV4().run(args);
		// exit
		System.exit(status);

	}

}
