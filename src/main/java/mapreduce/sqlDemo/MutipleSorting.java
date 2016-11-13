package mapreduce.sqlDemo;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MutipleSorting {

	public static class DataMapper extends Mapper<LongWritable, Text, IntMultiplePair, IntWritable> {
		private IntMultiplePair intMultiplePair = new IntMultiplePair();
		private IntWritable intWritable = new IntWritable(0);

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			System.out.println("Map Methond Invoked!!!");

			String data = value.toString();
			// String[] splited = data.split("\t");
			String[] splited = data.split(",");

			intMultiplePair.setFirst(splited[0]);
			intMultiplePair.setSecond(Integer.valueOf(splited[1]));
			intWritable.set(Integer.valueOf(splited[1]));

			context.write(intMultiplePair, intWritable);
		}
	}


	public static class DataReducer extends Reducer<IntMultiplePair, IntWritable, Text, Text> {

		public void reduce(IntMultiplePair key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			System.out.println("Reduce Methond Invoked!!!");

			StringBuffer buffered = new StringBuffer();
			Iterator<IntWritable> iter = values.iterator();
			while (iter.hasNext()) {
				buffered.append(iter.next().get() + ",");
			}

			int length = buffered.toString().length();
			String result = buffered.toString().substring(0, length - 1);
			context.write(new Text(key.getFirst()), new Text(result));
		}
	}

	public static void main(String[] args) throws Exception {


		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length < 2) {
			System.err.println("Usage: MutlpleSorting <in> [<in>...] <out>");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "MutlpleSorting");
		job.setJarByClass(MutipleSorting.class);

		job.setMapperClass(DataMapper.class);
		job.setReducerClass(DataReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapOutputKeyClass(IntMultiplePair.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setPartitionerClass(MyMultipleSortingPartitioner.class);
		job.setSortComparatorClass(IntMultipleSortingComparator.class);
		job.setGroupingComparatorClass(GroupingMultipleComparator.class);

		for (int i = 0; i < otherArgs.length - 1; ++i) {
			FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
		}
		FileOutputFormat.setOutputPath(job,
				new Path(otherArgs[otherArgs.length - 1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}

/**
 * 自定义一个可比较可序列化的数据类型
 * 没有设定具体比较,都相等
 */
class IntMultiplePair implements WritableComparable<IntMultiplePair> {
	private String first;
	private int second;

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public IntMultiplePair() {
	}

	public IntMultiplePair(String first, int second) {

		this.first = first;
		this.second = second;
	}

	@Override
	public void readFields(DataInput input) throws IOException {
		this.first = input.readUTF();
		this.second = input.readInt();
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeUTF(this.first);
		output.writeInt(this.second);
	}

	@Override
	public int compareTo(IntMultiplePair o) {
		return 0;
	}
}

/**
 * 自定义一个排序比较器,制定排序规则
 * 对map端IntMultiplePair类型的key,如果该类型的key相同则以value比较
 */
class IntMultipleSortingComparator extends WritableComparator {
	public IntMultipleSortingComparator() {
		super(IntMultiplePair.class, true);
	}

	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		IntMultiplePair x = (IntMultiplePair) a;
		IntMultiplePair y = (IntMultiplePair) b;

		if (!x.getFirst().equals(y.getFirst())) {
			System.out.println("排序开始了，比较第一个first：  " + x.getFirst() + "    " + y.getFirst() + "    " + x.getFirst().compareTo(y.getFirst()));
			return x.getFirst().compareTo(y.getFirst());
		} else {
			System.out.println("排序开始了，比较第二个second：  " + x.getSecond() + "    " + y.getSecond() + "    " + (x.getSecond() - y.getSecond()));
			return x.getSecond() - y.getSecond();
		}
	}
}

/**
 * 自定义分组比较器
 * 以IntMultiplePair的key分组
 */
class GroupingMultipleComparator extends WritableComparator {

	public GroupingMultipleComparator() {
		super(IntMultiplePair.class, true);
	}

	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		IntMultiplePair x = (IntMultiplePair) a;
		IntMultiplePair y = (IntMultiplePair) b;
		System.out.println("分组开始了 :  " + x.getFirst() + "    " + y.getFirst() + "    " + x.getFirst().compareTo(y.getFirst()));
		return x.getFirst().compareTo(y.getFirst());
	}
}

/**
 * 自定义分区类型,仅在默认的HashPartitioner中加入输出
 */
class MyMultipleSortingPartitioner extends Partitioner<IntMultiplePair, IntWritable> {

	@Override
	public int getPartition(IntMultiplePair arg0, IntWritable arg1, int arg2) {
		System.out.println("getPartition分区的计算过程     ！！！！！！！ " + arg0.getFirst().hashCode() + "    " + Integer.MAX_VALUE + arg2);
		System.out.println("getPartition的值       " + (arg0.getFirst().hashCode() & Integer.MAX_VALUE) % arg2);
		return (arg0.getFirst().hashCode() & Integer.MAX_VALUE) % arg2;
	}
}