package mapreduce.base.writable.topK;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 * 设置reduce task个数0
 * 在map中通过treeMap数据结构保存前3最大的value对应的kv对
 */
public class TopKMapReduceV3 {

	private static class TopKMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
		public static final int KEY = 3;
		// map key set
		TreeMap<Long, String> topMap = new TreeMap<Long, String>();

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			// get value
			String lineValue = value.toString();
			// split
			String[] strs = lineValue.split("\t");
			//add topMap
			topMap.put(Long.valueOf(strs[1]), strs[0]);
			if (topMap.size() > KEY) {
				topMap.remove(topMap.firstEntry().getKey());
			}
		}

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			/*
			 * Iterator<Map.Entry<LongWritable, Text>> it =
			 * topMap.entrySet().iterator(); while(it.hasNext()){
			 * Map.Entry<LongWritable, Text> top = it.next(); context.write(
			 * top.getValue(),top.getKey()); }
			 */
			Iterator<Map.Entry<Long, String>> it = topMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Long, String> top = it.next();
				LongWritable topKey = new LongWritable(top.getKey());
				Text topValue = new Text(top.getValue());
				context.write(topValue, topKey);
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

		args = new String[]{"file/topK", "target/out"};
		int status = new TopKMapReduceV3().run(args);
		// exit
		System.exit(status);

	}

}
