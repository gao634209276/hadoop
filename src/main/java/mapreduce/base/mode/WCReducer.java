package mapreduce.base.mode;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WCReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

	@Override
	protected void reduce(Text key, Iterable<LongWritable> values,
			Context context) throws IOException, InterruptedException {
		long counter = 0;
		for (LongWritable value : values) {
			counter += value.get();
		}
		context.write(key, new LongWritable(counter));
	}

}
