package mapreduce.base.writable.demo;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 数据格式:account,in,out(分隔符\t)
 * 对相同的account的in,out进行sum聚合
 * 输出格式:account,in,out,surplus
 *
 * 其中surplus数值是在javaBean对象toString的时候自动计算
 * 而在toString方法中为将account组装,在这里通过reduce函数单独输出account
 */
public class SumStep {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		if (args == null || args.length == 0) {
			args = new String[]{"file/sum-sort/sum", "file/sum-sort/sort"};
		}

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJarByClass(SumStep.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(SumMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(InfoBean.class);

		job.setReducerClass(SumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(InfoBean.class);

		job.waitForCompletion(true);
	}

	/**
	 * map端读取每行,第一个单词作为key,该行的三个单词组装为InfoBean作为value
	 */
	private static class SumMapper extends Mapper<LongWritable, Text, Text, InfoBean> {

		private Text k = new Text();
		private InfoBean v = new InfoBean();

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			String[] fields = line.split(" ");
			String account = fields[0];
			double in = Double.parseDouble(fields[1]);
			double out = Double.parseDouble(fields[2]);
			k.set(account);
			v.set(account, in, out);
			context.write(k, v);
		}
	}

	/**
	 * reduce对每个value集合的infoBean结构中解析属性进行各自sum聚合,
	 */
	private static class SumReducer extends Reducer<Text, InfoBean, Text, InfoBean> {

		private InfoBean v = new InfoBean();

		@Override
		protected void reduce(Text key, Iterable<InfoBean> values,
		                      Context context)
				throws IOException, InterruptedException {

			double in_sum = 0;
			double out_sum = 0;
			for (InfoBean infoBean : values) {
				in_sum += infoBean.getIncome();
				out_sum += infoBean.getExpenses();

			}
			v.set("", in_sum, out_sum);
			context.write(key, v);
		}
	}

}
