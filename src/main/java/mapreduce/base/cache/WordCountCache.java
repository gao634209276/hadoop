package mapreduce.base.cache;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class WordCountCache {


	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

		if (otherArgs.length != 2) {
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		}

		DistributedCache.createSymlink(conf);
		String path = "mr/cache/cache";
		Path filePath = new Path(path);

		String uriWithLink = filePath.toUri().toString() + "#cache";
		DistributedCache.addCacheFile(new URI(uriWithLink), conf);

		// Path p = new Path("/tmp/hadoop-0.20.2-capacity-scheduler.jar#hadoop-0.20.2-capacity-scheduler.jar");
		// DistributedCache.addArchiveToClassPath(p, conf);

		Job job = new Job(conf, "WordCountCache");
		job.setJarByClass(WordCountCache.class);

		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}


	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();


		protected void setup(Context context) throws IOException, InterruptedException {

			System.out.println("Now, use the distributed cache and syslink");
			try {
				UseDistributedCacheBySymbolicLink();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

			StringTokenizer itr = new StringTokenizer(value.toString());

			while (itr.hasMoreTokens()) {
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}

	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void UseDistributedCacheBySymbolicLink() throws Exception {

		FileReader reader = new FileReader("hdfs://mr/cache/cache");
		BufferedReader br = new BufferedReader(reader);
		String s = null;
		while ((s = br.readLine()) != null) {
			System.out.println(s);
		}

		br.close();
		reader.close();
	}
}