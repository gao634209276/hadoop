package mapreduce.base;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 对输入文件中数据进行就算学生平均成绩。
 * 输入文件中的每行内容均为一个学生的姓名和他相应的成绩，如果有多门学科，则每门学科为一个文件。
 * 要求在输出中每行有两个间隔的数据，其中，第一个代表学生的姓名，第二个代表其平均成绩。
 * See : file/avg/
 */
public class Avg {

	public static void main(String[] args) throws Exception {

		Configuration configuration = new Configuration();
		Job job = Job.getInstance(configuration, "avg-job");
		job.setJarByClass(Avg.class);


		if (args == null || args.length == 0) {
			args = new String[]{"file/avg/", "target/out"};
		}

		job.setMapperClass(AvgMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setReducerClass(AvgReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileSystem fs = FileSystem.get(configuration);
		Path outputDir = new Path(args[1]);
		if (fs.exists(outputDir)) {
			fs.delete(outputDir, true);
		}
		FileOutputFormat.setOutputPath(job, outputDir);

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

	/**
	 * Map处理的 是一个纯文本文件，文件中存放的数据时每一行表示一个学生的姓名和他相应一科成绩。
	 * <p>
	 * Mapper处理的数据是由InputFormat分解过的数据集，
	 * InputFormat的作用是将数据集切割成小数据集InputSplit，每一个InputSplit将由一个Mapper负责处理。
	 * InputFormat中提供了一个RecordReader的实现，将一个InputSplit解析成<key,value>对提供给了map函数。
	 * InputFormat的默认值是TextInputFormat，它针对文本文件，按行将文本切割成 InputSlit，
	 * 并用 LineRecordReader将InputSplit解析成<key,value>对，key是行在文本中的位置，value是文件中的 一行。
	 * <p>
	 * Map的结果会通过partition分发到Reducer,Reducer做完Reduce操作后，将通过以格式OutputFormat输出
	 */
	private static class AvgMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		/**
		 * map对每行记录过滤并解析为:name score的格式
		 */
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String line = value.toString();

			if (line.trim().length() > 0) {
				String[] arr = line.split("\t");

				if (arr.length == 2) {
					context.write(new Text(arr[0]),
							new IntWritable(Integer.valueOf(arr[1])));
				}
			}
		}
	}

	/**
	 * Mapper最终处理的结果对<key,value>，在Reducer中进行合并
	 * 合并的时候，有相同key的键/值对则送到同一个 Reducer上。
	 * Reduce的结果由Reducer.Context的write方法输出到文件中。
	 */
	private static class AvgReducer extends
			Reducer<Text, IntWritable, Text, DoubleWritable> {

		/**
		 * reduce根据相同的name,对score求平均值
		 */
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
		                      Context context) throws IOException, InterruptedException {

			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			context.write(key, new DoubleWritable(sum / 3.0));
		}
	}
}
