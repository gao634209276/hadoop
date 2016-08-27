package com.it18zhang.hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MaxTemperatureReducer extends
		Reducer<Text, IntWritable, Text, IntWritable> {
	protected void reduce(Text key, java.lang.Iterable<IntWritable> values,
			Context context) throws java.io.IOException, InterruptedException {
		int maxValue = Integer.MIN_VALUE;
		for (IntWritable value : values) {
			maxValue = Math.max(maxValue, value.get());
		}
		
		context.write(key, new IntWritable(maxValue));
	}

}
