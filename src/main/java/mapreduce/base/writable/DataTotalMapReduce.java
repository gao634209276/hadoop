package mapreduce.base.writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 数据格式:phoneNum,upPackNum,downPayLoad,upPayLoad,downPackNum
 * <p>
 * map将数据解析组装为自定义的Writable实例对象,phoneNum为key,该对象为value
 * reduce对相同key的dataWritable对象中各自的属性(int值)进行sum聚合,最后再次组装输出
 */
public class DataTotalMapReduce {

	public static void main(String[] args) throws Exception {

		args = new String[]{"file/wc", "target/out"};
		int status = new DataTotalMapReduce().run(args);
		System.exit(status);
	}


	public int run(String[] args) throws Exception {

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, DataTotalMapReduce.class.getSimpleName());
		job.setJarByClass(DataTotalMapReduce.class);


		// 1) input
		Path inputDir = new Path(args[0]);
		FileInputFormat.addInputPath(job, inputDir);
		// 2) map
		job.setMapperClass(DataTotalMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(DataWritable.class);
		// 3) reduce
		job.setReducerClass(DataTotalReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DataWritable.class);
		// 4) output
		Path outputDir = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outputDir);
		// submint job
		boolean isSuccess = job.waitForCompletion(true);
		return isSuccess ? 0 : 1;
	}


	// Mapper Class
	private static class DataTotalMapper extends Mapper<LongWritable, Text, Text, DataWritable> {

		private Text mapOutputKey = new Text();
		private DataWritable dataWritable = new DataWritable();

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String lineValue = value.toString();
			// split
			String[] strs = lineValue.split("\t");
			// get data
			String phoneNum = strs[1];
			int upPackNum = Integer.valueOf(strs[6]);
			int downPayLoad = Integer.valueOf(strs[7]);
			int upPayLoad = Integer.valueOf(strs[8]);
			int downPackNum = Integer.valueOf(strs[9]);
			// set map output key / Value
			mapOutputKey.set(phoneNum);
			dataWritable.set(upPackNum, upPayLoad, downPackNum, downPayLoad);
			// set map out
			context.write(mapOutputKey, dataWritable);
		}
	}

	private static class DataTotalReduce extends Reducer<Text, DataWritable, Text, DataWritable> {

		private DataWritable dataWritable = new DataWritable();

		@Override
		public void reduce(Text key, Iterable<DataWritable> values, Context context)
				throws IOException, InterruptedException {
			int upPackNum = 0;
			int downPayLoad = 0;
			int upPayLoad = 0;
			int downPackNum = 0;
			// iterator
			for (DataWritable data : values) {
				upPackNum += data.getUpPackNum();
				downPayLoad += data.getDownPayLoad();
				upPayLoad += data.getUpPayLoad();
				downPackNum += data.getDownPackNum();
			}
			// set dataWritable
			dataWritable.set(upPackNum, upPayLoad, downPackNum, downPayLoad);
			//System.out.println(dataWritable.toString());
			// set reduce/job output
			context.write(key, dataWritable);
		}
	}
}

/**
 * 自定义一个包含4个int数据的数据结构,组装为javaBean,并实现可序列化
 */
class DataWritable implements Writable {

	// upload
	private int upPackNum;
	private int upPayLoad;
	// download
	private int downPackNum;
	private int downPayLoad;

	public DataWritable() {
	}

	public void set(int upPackNum, int upPayLoad, int downPackNum, int downPayLoad) {

		this.upPackNum = upPackNum;
		this.upPayLoad = upPayLoad;
		this.downPackNum = downPackNum;
		this.downPayLoad = downPayLoad;

	}

	public int getUpPackNum() {
		return upPackNum;
	}

	public int getUpPayLoad() {
		return upPayLoad;
	}

	public int getDownPackNum() {
		return downPackNum;
	}

	public int getDownPayLoad() {
		return downPayLoad;
	}

	/**
	 * 序列化对象中要输出的属性
	 *
	 * @param out 将对象序列化输出为输出流的对象
	 */
	@Override
	public void write(DataOutput out) throws IOException {

		out.writeInt(upPackNum);
		out.writeInt(upPayLoad);
		out.writeInt(downPackNum);
		out.writeInt(downPayLoad);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.upPackNum = in.readInt();
		this.upPayLoad = in.readInt();
		this.downPackNum = in.readInt();
		this.downPayLoad = in.readInt();
	}


	@Override
	public String toString() {
		return upPackNum + "\t" + upPayLoad + "\t" + downPackNum + "\t" + downPayLoad;
	}

}
