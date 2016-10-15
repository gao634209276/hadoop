package mapreduce.base.writable.topK;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 设置0个reduce task
 * 仅通过map获取记录中最大value的一行数据(kv对)
 */
public class TopKMapReduceV1 {

	public static void main(String[] args) throws Exception {

		args = new String[]{"file/topK", "target/out"};
		int status = new TopKMapReduceV1().run(args);
		System.exit(status);
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

	/**
	 * 通过一个变量topkValue保存当前最大的value,同时将对应的key保存在mapOutputKey中
	 * 在map方法中,不断读取记录比较,如果有更大的就按照以上方式保存
	 * 在map task结束的时候,输出最终的一条kv
	 */
	private static class TopKMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

		//map out key
		private Text mapOutputKey = new Text();
		//map out value
		private LongWritable mapOutputValue = new LongWritable();
		//store max value, init Long_MIN_VALUE
		long topkValue = Long.MIN_VALUE;

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String lineValue = value.toString();
			String[] strs = lineValue.split("\t");

			long tempValue = Long.valueOf(strs[1]);
			if (topkValue < tempValue) {
				topkValue = tempValue;
				mapOutputKey.set(strs[0]);
			}
		}

		protected void cleanup(Context context) throws IOException, InterruptedException {
			//set map output value
			mapOutputValue.set(topkValue);
			context.write(mapOutputKey, mapOutputValue);
		}
	}
}
