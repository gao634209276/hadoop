package mapreduce.base;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 通过采集的气象数据分析每年的最高温度
 */
public class MaxTemperature {
	public static void main(String[] args) throws Exception {
		MaxTemperature d = new MaxTemperature();

		if (args == null || args.length == 0) {
			args = new String[]{"file/temperature/", "target/out"};
		}
		System.exit(d.run(args));
	}

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.print("Usage: MaxTemperature<input path> <output path>");
			System.exit(-1);
		}
		Configuration conf = new Configuration();

		Job job = Job.getInstance(conf, "MaxTemperature");
		job.setJarByClass(MaxTemperature.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(NewMaxTemperatureMapper.class);
		job.setReducerClass(NewMaxTemperatureReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		return job.waitForCompletion(true) ? 0 : 1;

	}

	/**
	 * MAP阶段，hadoop Mapreduce框架自动将每行记录赋值给map的value值，（key值为每行记录偏移量）
	 * 我们将value通过字符串截取出第15-19个字符的year，45-50个字符的是温度，以及清洗掉不符合规则的记录。
	 * MAP阶段输出的KEY值为year；输出的value值为温度
	 */
	private static class NewMaxTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		private static final int MISSING = 9999;

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			//每行记录0067011990999991950051507004888888889999999N9+00001+9999999999999999999999
			String line = value.toString();
			System.out.println("key: " + key);

			String year = line.substring(15, 19);
			int airTemperature;
			if (line.charAt(45) == '+') {
				//第15-19个字符表示year，例如1950年、1949年等
				airTemperature = Integer.parseInt(line.substring(46, 50));
			} else {
				//第45-50个字符表示的是温度，例如-00111、+00001
				airTemperature = Integer.parseInt(line.substring(45, 50));
			}
			//第50位只能是0、1、4、5、9等几个数字；
			String quality = line.substring(50, 51);
			System.out.println("quality: " + quality);

			if (airTemperature != MISSING && quality.matches("[01459]")) {
				context.write(new Text(year), new IntWritable(airTemperature));
			}
		}
	}

	/**
	 * Reduce 阶段，上述Map汇总过来的每年的温度值value是一个集合，
	 * 例如一年每天合计有365个温度，因此Reduce 读入的value类型Iterable<IntWritable>；key是年份
	 * 对于value的温度集合遍历，找出最小值的温度。
	 * Reduce 阶段汇总输出的KEY值是为year；输出的value值为最小值的温度（即最低温度）。
	 */
	private static class NewMaxTemperatureReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
		                      Context context)
				throws IOException, InterruptedException {

			int maxValue = Integer.MIN_VALUE;
			for (IntWritable value : values) { //遍历温度集合，比较温度大小
				maxValue = Math.max(maxValue, value.get());
			}
			context.write(key, new IntWritable(maxValue));
		}
	}
}