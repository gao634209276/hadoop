package mapreduce.app.topkey;

import java.io.IOException;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * ��ݸ�ʽ: ������� ������� �ղش��� ���Ŵ��� ������� ���� ͳ��ǰʮ�ײ��Ŵ������ĸ�����ƺʹ���
 */
public class TopKeyMapReduce {

	private static final int KEY = 10;

	// 1)Mapper Class
	public static class TopKeyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			super.setup(context);
		}

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String lineValue = value.toString();

			// invalidate
			if (null == lineValue) {
				return;
			}

			// split
			String[] strs = lineValue.split("\t");
			if (null != strs && strs.length == 5) {
				String languageType = strs[0];
				String singName = strs[1];
				String playTimes = strs[3];

				context.write(new Text(languageType + "\t" + singName), new LongWritable(Long.valueOf(playTimes)));
			}

		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			super.cleanup(context);
		}

	}

	// 2) Reduce Class

	public static class TopKeyReduce extends Reducer<Text, LongWritable, TopKeyWritable, NullWritable> {
		// store data
		TreeSet<TopKeyWritable> topSet = new TreeSet<TopKeyWritable>();

		@Override
		protected void setup(Reducer<Text, LongWritable, TopKeyWritable, NullWritable>.Context context)
				throws IOException, InterruptedException {
			super.setup(context);
		}

		@Override
		protected void reduce(Text key, Iterable<LongWritable> values, Context context)
				throws IOException, InterruptedException {
			if (null == key) {
				return;
			}

			String[] splited = key.toString().split("\t");
			if (null == splited || splited.length == 0) {
				return;
			}
			String languageType = splited[0];
			String singName = splited[1];
			Long palyTimes = 0L;
			for (LongWritable value : values) {
				palyTimes += value.get();
			}

			topSet.add(new TopKeyWritable(languageType, singName, palyTimes));
			if (topSet.size() > KEY) {
				topSet.remove(topSet.last());
			}
		}

		@Override
		protected void cleanup(Context context)
				throws IOException, InterruptedException {
			for (TopKeyWritable top : topSet) {
				context.write(top, NullWritable.get());
			}
		}
	}

	// 3) Driver Class
	public int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		// set conf
		Configuration conf = new Configuration();

		// create job
		Job job = new Job(conf, TopKeyMapReduce.class.getSimpleName());

		// set job
		job.setJarByClass(TopKeyMapReduce.class);
		// 1)input
		Path inputPath = new Path(args[0]);
		FileInputFormat.addInputPath(job, inputPath);
		// 2)map
		job.setMapperClass(TopKeyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		// 3)reduce
		job.setReducerClass(TopKeyReduce.class);
		job.setNumReduceTasks(1);
		job.setOutputKeyClass(TopKeyWritable.class);
		job.setOutputValueClass(NullWritable.class);
		// 4)output
		Path outputDir = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outputDir);

		// submit job
		boolean isSuccess = job.waitForCompletion(true);

		return isSuccess ? 0 : 1;

	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		
		args = new String[] {
				// input path
				"hdfs://hadoop-master:9000/opt/topkey/input3",
				// output path
				"hdfs://hadoop-master:9000/opt/topkey/output3" };
		int status = new TopKeyMapReduce().run(args);
		System.exit(status);
	}

}
