package mapreduce.demo8;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TopNReducer extends
		Reducer<MyIntWritable, Text, Text, MyIntWritable> {

	private int idx = 0;

	@Override
	protected void reduce(MyIntWritable key, Iterable<Text> values,
			Context context) throws IOException, InterruptedException {
		idx++;
		if (idx <= 5) {
			context.write(new Text(idx + ""), key);
		}

	}

}
