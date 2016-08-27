package mapreduce;



import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class MyWordCount {
	
	//GenericOptionsParser
	
	//Mapper
	static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable>{

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		@Override
		protected void map(LongWritable key, Text value,Context context)
				throws IOException, InterruptedException {
			
			
			String lineValue = value.toString();
			StringTokenizer stringTokenizer = new StringTokenizer(lineValue);
			while(stringTokenizer.hasMoreTokens()){
				String wordValue = stringTokenizer.nextToken();
				word.set(wordValue);
				context.write(word, one);
			}
		}
		
	}
	
	/**
	 * KEYIN, 	VALUEIN, 	KEYOUT, 	VALUEOUT
	 *
	 */
	static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
		private IntWritable result = new IntWritable();
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			result.set(sum);
			context.write(key, result);
		}
		
	}
	
	//Client
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		
		/*args = new String[] {"hdfs://hadoop-master.dragon.org:9000/opt/wc/input/",
		"hdfs://hadoop-master.dragon.org:9000/opt/wc/output4"};*/

		Configuration conf = new Configuration();
		
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if(otherArgs.length != 2){
			System.err.println("Usage: wordcount <in> <out>");
			System.exit(2);
		}
		
		Job job = new Job(conf,"wc");
		job.setJarByClass(MyWordCount.class);
		job.setMapperClass(MyMapper.class);
		//job.setCombinerClass(MyReducer.class);
		job.setReducerClass(MyReducer.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		boolean isSuccess = job.waitForCompletion(true);
		System.exit(isSuccess ? 0 : 1);
		
	}
}
