package mapreduce.base;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * 按行对记录去重
 * <p>
 * 数据去重的最终目标是让原始数据中出现次数超过一次的数据在输出文件中只出现一次。
 * 我们自然而然会想到将同一个数据的所有记录都交给一台reduce机器，无论这个数据出现多少次，只要在最终结果中输出一次就可以了。
 * 具体就是reduce的输入应该以数据作为key，而对value-list则没有要求。
 * 当reduce接收到一个<key，value-list>时就直接将key复制到输出的key中，并将value设置成空值。
 */
public class Distributed {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		if (args == null || args.length == 0) {
			args = new String[]{"file/distribute", "target/out"};
			;
		}

		/**
		 * 通过conf和arg参数构造一个通用选项解析器
		 * getRemainingArgs对arg进行解析并返回有效arg[]数组
		 */
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: Data Deduplication <in> <out>");
			System.exit(2);
		}
		Job job = Job.getInstance(conf, "Data Deduplication");
		job.setJarByClass(Distributed.class);


		// 设置Map、Combine和Reduce处理类
		job.setMapperClass(Map.class);
		job.setCombinerClass(Reduce.class);
		job.setReducerClass(Reduce.class);

		// 设置输出类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// 设置输入和输出目录

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	// map将输入中的value复制到出数据的key上，并直接输出
	public static class Map extends Mapper<Object, Text, Text, Text> {

		private static Text line = new Text();// 每行数据

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			line = value;
			context.write(line, new Text(""));
		}
	}

	// reduce将输入中的key复制到输出数据的key上，并直接输出
	public static class Reduce extends Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			context.write(key, new Text(""));
		}
	}
}
