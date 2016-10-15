package mapreduce.base.writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 数据格式:languageType,singName,playTimes
 * map端将languageType和singName为key组装为key,playTimes为value
 * reduce对三个特征组装为一个自定义WritableComparable的实现类对象,
 * 并将该对象放入TreeSet的数据结构,利用TreeSet自动排序功能,不断的add和remove
 */
public class TopNinReduceWithTreeSet {

	private static final int KEY = 10;

	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {

		args = new String[]{"", ""};
		int status = new TopNinReduceWithTreeSet().run(args);
		System.exit(status);
	}

	public int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, TopNinReduceWithTreeSet.class.getSimpleName());
		job.setJarByClass(TopNinReduceWithTreeSet.class);

		Path inputPath = new Path(args[0]);
		FileInputFormat.addInputPath(job, inputPath);

		job.setMapperClass(TopKeyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);

		job.setReducerClass(TopKeyReduce.class);
		job.setNumReduceTasks(1);
		job.setOutputKeyClass(SingWriteableComparable.class);
		job.setOutputValueClass(NullWritable.class);

		Path outputDir = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outputDir);

		boolean isSuccess = job.waitForCompletion(true);
		return isSuccess ? 0 : 1;
	}


	private static class TopKeyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String lineValue = value.toString();

			// invalidate
			if (null == lineValue) {
				return;
			}
			// split
			String[] strs = lineValue.split("\t");
			if (strs.length == 5) {
				String languageType = strs[0];
				String singName = strs[1];
				String playTimes = strs[2];
				context.write(new Text(languageType + "\t" + singName), new LongWritable(Long.valueOf(playTimes)));
			}
		}
	}

	private static class TopKeyReduce extends Reducer<Text, LongWritable, SingWriteableComparable, NullWritable> {
		// store data
		TreeSet<SingWriteableComparable> topSet = new TreeSet<>();

		@Override
		protected void reduce(Text key, Iterable<LongWritable> values, Context context)
				throws IOException, InterruptedException {
			if (null == key) {
				return;
			}

			String[] splited = key.toString().split("\t");
			if (splited.length == 0) {
				return;
			}
			String languageType = splited[0];
			String singName = splited[1];
			Long palyTimes = 0L;
			for (LongWritable value : values) {
				palyTimes += value.get();
			}

			topSet.add(new SingWriteableComparable(languageType, singName, palyTimes));
			if (topSet.size() > KEY) {
				topSet.remove(topSet.last());
			}
		}

		@Override
		protected void cleanup(Context context)
				throws IOException, InterruptedException {
			for (SingWriteableComparable top : topSet) {
				context.write(top, NullWritable.get());
			}
		}
	}
}

class SingWriteableComparable implements WritableComparable<SingWriteableComparable> {

	String languageType;
	String singName;
	Long playTimes;

	public SingWriteableComparable() {
	}

	public SingWriteableComparable(String languageType, String singName, Long playTimes) {
		this.set(languageType, singName, playTimes);
	}

	public String getLanguageType() {
		return languageType;
	}

	public String getSingName() {
		return singName;
	}

	public Long getPlayTimes() {
		return playTimes;
	}

	public void set(String languageType, String singName, Long playTimes) {
		this.languageType = languageType;
		this.singName = singName;
		this.playTimes = playTimes;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(languageType);
		out.writeUTF(singName);
		out.writeLong(playTimes);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.languageType = in.readUTF();
		this.singName = in.readUTF();
		this.playTimes = in.readLong();
	}

	@Override
	public int compareTo(SingWriteableComparable o) {
		return (int) -(this.getPlayTimes().compareTo(o.getPlayTimes()));
	}

	@Override
	public String toString() {
		return languageType + "\t" + singName + "\t" + playTimes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((languageType == null) ? 0 : languageType.hashCode());
		result = prime * result + ((playTimes == null) ? 0 : playTimes.hashCode());
		result = prime * result + ((singName == null) ? 0 : singName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SingWriteableComparable other = (SingWriteableComparable) obj;
		if (languageType == null) {
			if (other.languageType != null)
				return false;
		} else if (!languageType.equals(other.languageType))
			return false;
		if (playTimes == null) {
			if (other.playTimes != null)
				return false;
		} else if (!playTimes.equals(other.playTimes))
			return false;
		if (singName == null) {
			if (other.singName != null)
				return false;
		} else if (!singName.equals(other.singName))
			return false;
		return true;
	}

}
