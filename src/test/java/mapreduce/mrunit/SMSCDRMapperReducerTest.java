package mapreduce.mrunit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mapreduce.mrunit.SMSCDRMapper.CDRCounter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * 测试数据说明 CDRID;CDRType;Phone1;Phone2;SMS Status Code
 * 655209;1;796764372490213;804422938115889;6
 * 353415;0;356857119806206;287572231184798;4
 * 835699;1;252280313968413;889717902341635;0
 */
public class SMSCDRMapperReducerTest {
	Configuration conf = new Configuration();
	MapDriver<LongWritable, Text, Text, IntWritable> mapDriver;
	ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
	MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

	@Before
	public void setUp() {
		//测试mapreduce
		SMSCDRMapper mapper = new SMSCDRMapper();
		SMSCDRReducer reducer = new SMSCDRReducer();
		mapDriver = MapDriver.newMapDriver(mapper);
		reduceDriver = ReduceDriver.newReduceDriver(reducer);
		mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
		//测试配置参数
		mapDriver.setConfiguration(conf);
		conf.set("myParameter1", "20");
		conf.set("myParameter2", "23");
	}

	@Test
	public void testMapper() throws IOException {
		mapDriver.withInput(new LongWritable(), new Text(
				"655209;1;796764372490213;804422938115889;6"));
		mapDriver.withOutput(new Text("6"), new IntWritable(1));
		mapDriver.runTest();
	}

	@Test
	public void testReducer() throws IOException {
		List<IntWritable> values = new ArrayList<IntWritable>();
		values.add(new IntWritable(1));
		values.add(new IntWritable(1));
		reduceDriver.withInput(new Text("6"), values);
		reduceDriver.withOutput(new Text("6"), new IntWritable(2));
		reduceDriver.runTest();
	}

	@Test
	public void testMapReduce() throws IOException {
		mapReduceDriver.withInput(new LongWritable(), new Text(
				"655209;1;796764372490213;804422938115889;6"));
		List<IntWritable> values = new ArrayList<IntWritable>();
		values.add(new IntWritable(1));
		values.add(new IntWritable(1));
		mapReduceDriver.withOutput(new Text("6"), new IntWritable(2));
		mapReduceDriver.runTest();
	}

	@Test
	public void testMapperCounter() throws IOException {
		mapDriver.withInput(new LongWritable(), new Text(
				"655209;0;796764372490213;804422938115889;6"));
		//mapDriver.withOutput(new Text("6"), new IntWritable(1));
		mapDriver.runTest();
		assertEquals("Expected 1 counter increment", 1, mapDriver.getCounters()
				.findCounter(CDRCounter.NonSMSCDR).getValue());
	}
}