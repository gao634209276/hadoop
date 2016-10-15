package mapreduce.base;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.Arrays;

/**
 * 数据文件，取第三列价格 取TOPn
 * 输入格式:orderID,userID,cost,productID
 */
public class TopNSorted {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		conf.setInt("topn", 3);//设置前几名,在mapper中设置默认值为5


		if (args == null || args.length == 0) {
			args = new String[]{"file/topN", "target/out"};//设置默认的path
		}
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length < 2) {
			System.err.println("Usage: TopNSorted <in> [<in>...] <out>");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "TopNSorted");
		//根据参数个数,可设置多个输入path
		for (int i = 0; i < otherArgs.length - 1; ++i) {
			FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
		}
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));


		job.setJarByClass(TopNSorted.class);
		job.setMapperClass(DataMapper.class);
		job.setReducerClass(DataReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	private static class DataMapper extends Mapper<LongWritable, Text, Text, Text> {
		int[] topN;
		int length;

		/**
		 * setup中初始化topN数组[0,0,0,0,0,0]
		 * 这里长度多1位置是便于在map中进行sort的时候,保持第一位一直是0或者淘汰掉的最小值
		 */
		@Override
		protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {

			length = context.getConfiguration().getInt("topn", 5);
			topN = new int[length + 1];

			System.out.println("map setup:topN:" + Arrays.toString(topN));
		}

		/**
		 * map方法对每条记录判断data长度为4的时候,
		 * 将该数据中的cost赋值到topN数组第一位,并进行sort排序
		 */
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			System.out.println("Map Methond Invoked!!!");
			String[] data = value.toString().split(",");
			if (4 == data.length) {
				int cost = Integer.valueOf(data[2]);
				topN[0] = cost;
				System.out.println("map: top[0]=" + topN[0]);
				Arrays.sort(topN);

				System.out.println("map topN:" + Arrays.toString(topN));
			}
		}

		/**
		 * 在cleanup中才调用context.write方法
		 */
		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {

			for (int i = 1; i < length + 1; i++) {
				context.write(new Text(String.valueOf(topN[i])), new Text(String.valueOf(topN[i])));
			}
		}
	}

	private static class DataReducer extends Reducer<Text, Text, Text, Text> {
		int[] topN;
		int length;

		@Override
		protected void setup(Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
			length = context.getConfiguration().getInt("topn", 5);
			topN = new int[length + 1];
			System.out.println("Reducer :setup topN:" + Arrays.toString(topN));

		}

		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			System.out.println("Reduce Methond Invoked!!!");

			topN[0] = Integer.valueOf(key.toString());
			System.out.println("reduce: topN[0]=" + topN[0]);
			Arrays.sort(topN);
			System.out.println("reduce topN:" + Arrays.toString(topN));
		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			for (int i = length; i > 0; i--) {
				context.write(new Text(String.valueOf(length - i + 1)), new Text(String.valueOf(topN[i])));
			}
			System.out.println("reduce :cleanup topN:" + Arrays.toString(topN));
		}
	}
}
