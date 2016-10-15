package mapreduce.base.writable.topK;

import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

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
 * map端对数据每行第一个word为key,第二个word为value输出
 *
 * reduce端通过获取map端相同key,对相应values进行sum聚合,
 * 并保存在自定义WritableComparable实现类TopKWritable的对象
 * 并通过TreeSet保存,使用临时比较器,通过该自定义类的count属性比较
 * 保存top前n的最大value值,在cleanup中输出
 */
public class TopKMapReduceV5 {
	public static final int KEY = 3;

	private static class TopKMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String[] strs = value.toString().split("\t");
			context.write(new Text(strs[0]), new LongWritable(Long.valueOf(strs[1])));
		}


	}

	private static class TopKReduce extends Reducer<Text, LongWritable, Text, LongWritable> {

		TreeSet<TopKWritable> topSet = new TreeSet<>(new Comparator<TopKWritable>() {

			@Override
			public int compare(TopKWritable o1, TopKWritable o2) {
				return o1.getCount().compareTo(o2.getCount());
			}
		});


		@Override
		public void reduce(Text key, Iterable<LongWritable> values, Context context)
				throws IOException, InterruptedException {
			long count = 0L;
			for (LongWritable value : values) {
				count += value.get();
			}
			topSet.add(new TopKWritable(key.toString(), count));
			// comparator
			if (topSet.size() > KEY) {
				topSet.remove(topSet.first());
			}
		}

		@Override
		public void cleanup(Context context) throws IOException, InterruptedException {
			for (TopKWritable top : topSet) {
				context.write(new Text(top.getWord()), new LongWritable(top.getCount()));
			}
		}
	}

	// Driver Code
	public int run(String[] args) throws Exception {

		// get conf
		Configuration conf = new Configuration();
		// create job
		Job job = Job.getInstance(conf, TopKMapper.class.getSimpleName());
		// set
		job.setJarByClass(TopKMapper.class);
		// 1) input
		Path inputDir = new Path(args[0]);
		FileInputFormat.addInputPath(job, inputDir);
		// 2) map
		job.setMapperClass(TopKMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		// set reduce task
		job.setNumReduceTasks(1);// default reduce tasks is one
		// 3) reduce 
		job.setReducerClass(TopKReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		// 4) output
		Path outputDir = new Path(args[1]);
		FileOutputFormat.setOutputPath(job, outputDir);
		// submint job
		boolean isSuccess = job.waitForCompletion(true);
		return isSuccess ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {

		args = new String[]{"file/topK", "target/out"};
		int status = new TopKMapReduceV5().run(args);
		System.exit(status);

	}

}
