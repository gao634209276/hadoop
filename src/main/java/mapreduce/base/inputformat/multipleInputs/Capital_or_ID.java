package mapreduce.base.inputformat.multipleInputs;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.MultipleInputs;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

public class Capital_or_ID {
	/**
	 * 处理File_1.txt 输入: 行号,行内容<br/>
	 * 输出: key=国家名, value=编号
	 */
	public static class MapA extends MapReduceBase implements Mapper<LongWritable, Text, Text, Capital_or_IDWritable> {

		@Override
		public void map(LongWritable lineN, Text content, OutputCollector<Text, Capital_or_IDWritable> collect, Reporter rp) throws IOException {
			String part[] = content.toString().split("\t");
			if (part.length == 2) {
				Capital_or_IDWritable coi = new Capital_or_IDWritable(part[0], "file_1");
				collect.collect(new Text(part[1]), coi);
			}
			System.out.println("in MapA: content=" + content);
			for (String s : part) {
				System.out.println("part[idx]=" + s);
			}
		}

	}

	/**
	 * 处理File_2.txt 输入: 行号,行内容<br/>
	 * 输出: key=国家名,value=首都名
	 */
	public static class MapB extends MapReduceBase implements Mapper<LongWritable, Text, Text, Capital_or_IDWritable> {

		@Override
		public void map(LongWritable lineN, Text content, OutputCollector<Text, Capital_or_IDWritable> collect, Reporter rp)
				throws IOException {
			String part[] = content.toString().split("\t");
			if (part.length == 2) {
				Capital_or_IDWritable coi = new Capital_or_IDWritable(part[1], "file_2");
				collect.collect(new Text(part[0]), coi);

			}
			System.out.println("in MapB: content=" + content);
			for (String s : part) {
				System.out.println("part[idx]=" + s);
			}
		}

	}

	/**
	 * Reduce.class处理最后结果，将国家名、编号和首都格式化为："ID=%s\tcountry=%s\tcapital=%s"
	 * ID=1 country=China capital=BeiJing
	 */
	public static class Reduce extends MapReduceBase implements Reducer<Text, Capital_or_IDWritable, Text, Text> {

		@Override
		public void reduce(Text countryName, Iterator<Capital_or_IDWritable> values, OutputCollector<Text, Text> collect, Reporter rp) throws IOException {
			String capitalName = null, ID = null;
			while (values.hasNext()) {
				Capital_or_IDWritable coi = values.next();
				if (coi.getTag().equals("file_1")) {
					ID = coi.getValue();
				} else if (coi.getTag().equals("file_2")) {
					capitalName = coi.getValue();
				}
			}

			String result = String.format("ID=%s\tname=%s\tcapital=%s", ID, countryName, capitalName);

			collect.collect(countryName, new Text(result));
		}
	}

	public static void main(String args[]) throws IOException {
		if (args == null || args.length == 0) {
			args = new String[]{"file/inputformat/file_1.txt", "file/inputformat/file_2.txt", "target/out"};
		}
		// args[0] file1 for MapA
		String file_1 = args[0];
		// args[1] file2 for MapB
		String file_2 = args[1];
		// args[2] outPath
		String outPath = args[2];

		JobConf conf = new JobConf(Capital_or_IDWritable.class);
		conf.setJobName("example-MultipleInputs");

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Capital_or_IDWritable.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setReducerClass(Reduce.class);

		conf.setOutputFormat(TextOutputFormat.class);
		FileOutputFormat.setOutputPath(conf, new Path(outPath));

		MultipleInputs.addInputPath(conf, new Path(file_1), TextInputFormat.class, MapA.class);
		MultipleInputs.addInputPath(conf, new Path(file_2), TextInputFormat.class, MapB.class);

		JobClient.runJob(conf);
	}

	/**
	 * 该类为自定义数据类型.
	 * 其目的是为了在多个输入文件多个Mapper的情况下，标记数据.从而Reduce可以辨认value的来源
	 */
	public static class Capital_or_IDWritable implements Writable {
		/**
		 * 相同来源的value应当具有相同的tag
		 */
		private String tag = null;

		private String value = null;

		public Capital_or_IDWritable() {
		}

		public Capital_or_IDWritable(String value, String tag) {
			this.value = value;
			this.tag = tag;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getValue() {
			return value;
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			tag = in.readUTF();
			value = in.readUTF();
		}

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeUTF(tag);
			out.writeUTF(value);
		}
	}

}
