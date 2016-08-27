package mapreduce.nginxlog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 测试输入数据格式： ... 183.60.212.153 - - [19/Feb/2013:10:23:29 +0800]
 * "GET /o2o/media.html?menu=3 HTTP/1.1" 200 16691 "-"
 * "Mozilla/5.0 (compatible; EasouSpider; +http://www.easou.com/search/spider.html)"
 * ... 输出数据格式 ... 日期 独立IP个数 ...
 * 
 * @author liangchuan
 */
public class NginxAccessLogMR {

	public static class Map01 extends Mapper<LongWritable, Text, Text, Text> {
		private Date getDateByValue(String vs) throws ParseException {
			String date = vs.substring(vs.indexOf("["), vs.indexOf("]") + 1);
			SimpleDateFormat format = new SimpleDateFormat(
					"[dd/MMM/yyyy:HH:mm:ss Z]", Locale.US);
			Date d = format.parse(date);
			return d;
		}

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			try {
				String vs = value.toString();
				String[] arr = vs.split("- -");
				// String k = arr[0].trim();// IP
				String v = arr[1].trim();// others
				Date d = getDateByValue(vs);// DATE
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
				Text k = new Text(f.format(d));
				// 以日期分组
				context.write(k, new Text(vs));
			} catch (Exception e) {
				System.out.println("MAPPER ++++++++++++++++++++++++++"
						+ e.getMessage());
			}
		}
	}

	public static class Reduce01 extends Reducer<Text, Text, Text, IntWritable> {

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			Map<String, String> m = new HashMap<String, String>();
			for (Text value : values) {
				String vs = value.toString();
				String[] arr = vs.split("- -");
				String ip = arr[0].trim();// IP
				m.put(ip, "");
			}
			System.out.println("RRRRRRR <><> " + m);
			context.write(key, new IntWritable(m.size()));
		}
	}

	/**
	 * 文件名过滤
	 * 
	 * @author liangchuan
	 * 
	 */
	public static class MyPathFilter implements PathFilter, Configurable {
		Configuration conf = null;
		FileSystem fs = null;

		@Override
		public Configuration getConf() {
			return this.conf;
		}

		@Override
		public void setConf(Configuration conf) {
			this.conf = conf;
		}

		@Override
		public boolean accept(Path path) {
			try {
				fs = FileSystem.get(conf);
				FileStatus fileStatus = fs.getFileStatus(path);
				if (!fileStatus.isDir()) {
					String fileName = path.getName();
					if (!fileName.contains(conf.get("pathfilter.pattern"))) {
						return true;
					}
				}
			} catch (IOException e) {
				System.out.println("MyPathFilter ++++++++++++++++++++++++++");
				e.printStackTrace();
			}
			return false;
		}
	}

	public static void main(String[] args) {

		// JobConf conf = new JobConf(MaxTptr.class);
		Job job = null;
		try {
			job = Job.getInstance(new Configuration());
			//job = new Job();
			job.setJarByClass(NginxAccessLogMR.class);

			FileInputFormat.addInputPath(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));

			job.setMapperClass(Map01.class);
			job.setReducerClass(Reduce01.class);

			/**
			 * map 的输出如果跟 reduce 的输出不一致则必须要做此步配置，否则会按照 reduce 的输出进行默认
			 */
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);

			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);

			// 第三个参数是要过滤的文件名关键字，默认error
			String pfk = args.length > 2 ? args[2] : "error";
			job.getConfiguration().set("pathfilter.pattern", pfk);
			FileInputFormat.setInputPathFilter(job, MyPathFilter.class);

			System.exit(job.waitForCompletion(true) ? 0 : 1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
