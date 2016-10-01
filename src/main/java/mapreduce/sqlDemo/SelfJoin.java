package mapreduce.sqlDemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class SelfJoin {
	public static class DataMapper extends
			Mapper<LongWritable, Text, Text, Text> {

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			System.out.println("Map Methond Invoked!!!");
			// String[] array =new String[2];
			// int i=0;
			String[] array;
			array = value.toString().split("\t");

			StringTokenizer stringTokenizer = new StringTokenizer(
					value.toString());

			/*
			 * while(stringTokenizer.hasMoreElements()){ array[i]
			 * =stringTokenizer.nextToken().trim(); i++; }
			 */

			// System.out.println("key  "+array[1] +
			// "value:  "+"1_"+array[0]+"_"+array[1]);
			// System.out.println("key  "+array[0] +
			// "value:  "+"0_"+array[0]+"_"+array[1]);

			// context.write(new Text(array[1]), new
			// Text("1_"+array[0]+"_"+array[1]));
			// context.write(new Text(array[0]), new
			// Text("0_"+array[0]+"_"+array[1]));

			System.out.println("map key array[1]:  " + array[1].trim()
					+ "     value array[0]:  " + "1_" + array[0].trim());
			System.out.println("map key array[0]:  " + array[0].trim()
					+ "     value array[1]:  " + "0_" + array[1].trim());

			context.write(new Text(array[1].trim()),
					new Text("1_" + array[0].trim())); // left
			context.write(new Text(array[0].trim()),
					new Text("0_" + array[1].trim())); // right

		}

	}

	public static class DataReducer extends Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			System.out.println("Reduce Methond Invoked!!!");

			Iterator<Text> iterator = values.iterator();
			List<String> grandChildList = new ArrayList<String>();
			List<String> grandParentList = new ArrayList<String>();

			while (iterator.hasNext()) {
				String item = iterator.next().toString();
				String[] splited = item.split("_");

				if (splited[0].equals("1")) {
					grandChildList.add(splited[1]);
				} else {
					grandParentList.add(splited[1]);
				}
			}

			if (grandChildList.size() > 0 && grandParentList.size() > 0) {

				for (String grandChild : grandChildList) {
					for (String grandParent : grandParentList) {
						// context.write(new Text(grandChild),new
						// Text(grandParent));
						context.write(new Text(grandChild), new Text(
								grandParent));

					}
				}

			}
		}

	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length < 2) {
			System.err.println("Usage: JoinImproved <in> [<in>...] <out>");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "JoinImproved");
		job.setJarByClass(SelfJoin.class);

		job.setMapperClass(DataMapper.class);
		job.setReducerClass(DataReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		for (int i = 0; i < otherArgs.length - 1; ++i) {
			FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
		}
		FileOutputFormat.setOutputPath(job, new Path(
				otherArgs[otherArgs.length - 1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}