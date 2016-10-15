package mapreduce.base;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 提供path下不同文件的数据
 * 返回数据中的word及对应的文件和统计的个数
 */
public class InverseIndex {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		if (args == null || args.length == 0) {
			args = new String[]{"file/inverseIndex", "target/out"};
		}

		job.setJarByClass(InverseIndex.class);
		job.setMapperClass(IndexMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setCombinerClass(IndexConbiner.class);
		FileInputFormat.setInputPaths(job, new Path(args[0]));

		job.setReducerClass(IndexReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}

	/**
	 * map端通过context获取输入分片,然后得到该文件的路径
	 * 按行切分word并与该分片的path组装为key,value为1
	 */
	private static class IndexMapper extends
			Mapper<LongWritable, Text, Text, Text> {

		private Text k = new Text();
		private Text v = new Text();

		protected void map(LongWritable key, Text value, Context context)
				throws java.io.IOException, InterruptedException {
			String line = value.toString();
			String[] words = line.split(" ");

			// 获取当前map对应的输入分片对象,再获取其路径
			FileSplit inputSplit = (FileSplit) context.getInputSplit();
			String path = inputSplit.getPath().toString();
			for (String word : words) {
				k.set(word + "-->" + path);
				v.set("1");
				// 格式如:word-->path 1
				context.write(k, v);
			}
		}
	}

	/**
	 * map端做combine
	 * <p>
	 * 对相同的word和path的个数进行sum聚合记为count
	 * 对word-->path再次拆分为word和path,输出key为word,value为path-->count
	 */
	private static class IndexConbiner extends Reducer<Text, Text, Text, Text> {

		private Text k = new Text();
		private Text v = new Text();

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			String[] wordAndPath = key.toString().split("-->");
			String word = wordAndPath[0];
			String path = wordAndPath[1];
			int counter = 0;
			// 对相同的word-->path在map端聚合
			for (Text t : values) {
				counter += Integer.parseInt(t.toString());
			}
			// 然后输入格式:word path-->counter
			k.set(word);
			v.set(path + "-->" + counter);
			context.write(k, v);
		}

	}

	/**
	 * reduce对相同word聚合,
	 * value(path-->count)通过字符串append聚合
	 */
	private static class IndexReducer extends Reducer<Text, Text, Text, Text> {
		private Text v = new Text();

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			String result = "";
			for (Text value : values) {
				result += value.toString() + "\t";

			}
			v.set(result);
			context.write(key, v);
		}
	}


}
