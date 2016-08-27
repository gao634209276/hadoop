package mapreduce.demo8;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TopNMapper extends Mapper<LongWritable, Text, MyIntWritable, Text> {

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
