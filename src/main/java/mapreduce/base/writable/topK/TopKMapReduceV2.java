package mapreduce.base.writable.topK;

import java.io.IOException;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 在Map端通过TreeSet保存前n个最大的kv对
 * reduce task设置为0
 */
public class TopKMapReduceV2 {

	// Mapper Class
	private static class TopKMapper extends Mapper<LongWritable, Text, LongWritable, NullWritable> {
		public static final int KEY = 3;
		// map key set
		TreeSet<Long> topSet = new TreeSet<>();

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String lineValue = value.toString();
			String[] strs = lineValue.split("\t");
			long tempValue = Long.valueOf(strs[1]);
			topSet.add(tempValue);
			if (topSet.size() > KEY) {
				topSet.remove(topSet.first());
			}
		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			LongWritable setKey = new LongWritable();
			for (Long top : topSet) {
				setKey.set(top);
				context.write(setKey, NullWritable.get());
			}
		}
	}

	public int run(String[] args) throws Exception {

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, TopKMapper.class.getSimpleName());
		job.setJarByClass(TopKMapper.class);
		Path inputDir = new Path(args[0]);
		FileInputFormat.addInputPath(job, inputDir);

		job.setMapperClass(TopKMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		job.setNumReduceTasks(0);

		Path outputDir = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outputDir);

		boolean isSuccess = job.waitForCompletion(true);
		return isSuccess ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {

		args = new String[]{"file/topK", "target/out"};
		int status = new TopKMapReduceV2().run(args);
		System.exit(status);
	}
}
