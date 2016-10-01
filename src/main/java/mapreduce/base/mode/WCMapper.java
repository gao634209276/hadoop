package mapreduce.base.mode;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WCMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		// 切分数据
		String[] words = line.split(" ");
		for (String word : words) {
			// 循环输出
			context.write(new Text(word), new LongWritable(1));
		}
	}

}
