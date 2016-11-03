package mapreduce.base.cache;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.pattern.LogEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 测试hadoop的全局共享文件
 * 使用DistributedCached
 * <p>
 * 大数据技术交流群： 37693216
 *
 * @author qindongliang
 ***/
public class TestDistributed {


	private static Logger logger = LoggerFactory.getLogger(TestDistributed.class);


	private static class FileMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		Path path[] = null;

		/**
		 * Map函数前调用
		 */
		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			logger.info("开始启动setup了哈哈哈哈");
			// System.out.println("运行了.........");
			Configuration conf = context.getConfiguration();
			path = DistributedCache.getLocalCacheFiles(conf);
			System.out.println("获取的路径是：  " + path[0].toString());
			//  FileSystem fs = FileSystem.get(conf);
			FileSystem fsopen = FileSystem.getLocal(conf);
			FSDataInputStream in = fsopen.open(path[0]);
			// System.out.println(in.readLine());
			//        for(Path tmpRefPath : path) {
			//            if(tmpRefPath.toString().indexOf("ref.png") != -1) {
			//                in = reffs.open(tmpRefPath);
			//                break;
			//            }
			//        }

			// FileReader reader=new FileReader("file://"+path[0].toString());
			//      File f=new File("file://"+path[0].toString());
			// FSDataInputStream in=fs.open(new Path(path[0].toString()));
			Scanner scan = new Scanner(in);
			while (scan.hasNext()) {
				System.out.println(Thread.currentThread().getName() + "扫描的内容:  " + scan.next());
			}
			scan.close();
			//
			// System.out.println("size: "+path.length);


		}


		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			// System.out.println("map    aaa");
			//logger.info("Map里的任务");
			System.out.println("map里输出了");
			// logger.info();
			context.write(new Text(""), new IntWritable(0));


		}


		@Override
		protected void cleanup(Context context)
				throws IOException, InterruptedException {


			logger.info("清空任务了。。。。。。");
		}

	}


	private static class FileReduce extends Reducer<Object, Object, Object, Object> {
		@Override
		protected void reduce(Object key, Iterable<Object> values, Context context) throws IOException, InterruptedException {
			super.reduce(key, values, context);
			System.out.println("我是reduce里面的东西");
		}
	}


	public static void main(String[] args) throws Exception {


		JobConf conf = new JobConf(TestDistributed.class);
		//conf.set("mapred.local.dir", "/root/hadoop");
		//Configuration conf=new Configuration();

		// conf.set("mapred.job.tracker","192.168.75.130:9001");
		//读取person中的数据字段
		//conf.setJar("tt.jar");

		//注意这行代码放在最前面，进行初始化，否则会报
		String inputPath = "hdfs://192.168.75.130:9000/root/input";
		String outputPath = "hdfs://192.168.75.130:9000/root/outputsort";

		Job job = new Job(conf, "a");
		DistributedCache.addCacheFile(new URI("hdfs://192.168.75.130:9000/root/input/f1.txt"), job.getConfiguration());
		job.setJarByClass(TestDistributed.class);
		System.out.println("运行模式：  " + conf.get("mapred.job.tracker"));
		/**设置输出表的的信息  第一个参数是job任务，第二个参数是表名，第三个参数字段项**/
		FileSystem fs = FileSystem.get(job.getConfiguration());

		Path pout = new Path(outputPath);
		if (fs.exists(pout)) {
			fs.delete(pout, true);
			System.out.println("存在此路径, 已经删除......");
		}
		/**设置Map类**/
		// job.setOutputKeyClass(Text.class);
		//job.setOutputKeyClass(IntWritable.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setMapperClass(FileMapper.class);
		job.setReducerClass(FileReduce.class);
		FileInputFormat.setInputPaths(job, new Path(inputPath));  //输入路径
		FileOutputFormat.setOutputPath(job, new Path(outputPath));//输出路径

		System.exit(job.waitForCompletion(true) ? 0 : 1);


	}


}