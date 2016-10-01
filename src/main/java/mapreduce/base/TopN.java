package mapreduce.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TopN {

	public static void main(String[] args) throws Exception {
		Configuration configuration = new Configuration();
		Job job = new Job(configuration, "topn_job");
		job.setJarByClass(TopN.class);

		job.setMapperClass(TopNMapper.class);
		job.setMapOutputKeyClass(MyIntWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setReducerClass(TopNReducer.class);
		job.setOutputKeyClass(MyIntWritable.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		Path path = new Path(args[1]);
		FileSystem fs = FileSystem.get(configuration);
		if (fs.exists(path)) {
			fs.delete(path, true);
		}
		FileOutputFormat.setOutputPath(job, path);
		job.setNumReduceTasks(1);

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}

	/**
	 * 读取每行以','分割的第二个字段解析为Integer, 使用自己实现的序列化类封装该值输出,value为空
	 */
	public class TopNMapper extends
			Mapper<LongWritable, Text, MyIntWritable, Text> {

		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString().trim();

			if (line.length() > 0) {// 1,9819,100,121

				String[] arr = line.split(",");

				if (arr.length == 4) {
					int payment = Integer.parseInt(arr[2]);
					context.write(new MyIntWritable(payment), new Text(""));
				}
			}
		}

	}

	/**
	 * 利用框架自动排序并 通过定义一个reduce全局变量idx控制输出
	 */
	public class TopNReducer extends
			Reducer<MyIntWritable, Text, Text, MyIntWritable> {

		private int idx = 0;

		@Override
		protected void reduce(MyIntWritable key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {

			idx++;
			if (idx <= 5) {
				context.write(new Text(idx + ""), key);
			}

		}

	}

	/**
	 * 简单实现一个可比较序列化类,对象只包含一个Integer类型值
	 */
	public class MyIntWritable implements WritableComparable<MyIntWritable> {
		private Integer num;

		public MyIntWritable(Integer num) {
			this.num = num;
		}

		public MyIntWritable() {
		}

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeInt(num);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			this.num = in.readInt();
		}

		@Override
		public int compareTo(MyIntWritable o) {
			int minus = this.num - o.num;

			return minus * (-1);
		}

		@Override
		public int hashCode() {
			return this.num.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof MyIntWritable) {
				return false;
			}
			MyIntWritable ok2 = (MyIntWritable) obj;
			return (this.num == ok2.num);
		}

		@Override
		public String toString() {
			return num + "";
		}

	}
}