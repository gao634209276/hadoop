package mapreduce.min;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.Text;

/**
 *MinimalMapRedece
 */
public class MinimalMapReduce {

	//no Mapper Class 
	
	//no Reduce Class
	
	//Driver Code
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		args = new String[]{
			"hdfs://hadoop-master.dragon.org:9000/opt/test/input",
			"hdfs://hadoop-master.dragon.org:9000/opt/test/output"};
		//conf 
		Configuration conf = new Configuration();
		//create job
		Job job = new Job(conf,MinimalMapReduce.class.getSimpleName());
		//set job
		job.setJarByClass(MinimalMapReduce.class);
		
		/*job.setMapperClass(Mapper.class);
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setCombinerClass(null); 
		job.setPartitionerClass(HashPartitioner.class);
		job.setSortComparatorClass(LongWritable.Comparator.class);
		job.setGroupingComparatorClass(LongWritable.Comparator.class);
		job.setReducerClass(Reducer.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		
		job.setOutputFormatClass(TextOutputFormat.class);*/
		
		
		//set input/output path
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		//submit job
		boolean isSuccess = job.waitForCompletion(true);
		//exit
		System.exit(isSuccess ? 0 : 1);
	}
}
