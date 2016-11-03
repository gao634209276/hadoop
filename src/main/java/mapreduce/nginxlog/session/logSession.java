package mapreduce.nginxlog.session;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import mapreduce.nginxlog.logclean.WebLogParser;
import mapreduce.nginxlog.logclean.logClean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 第二步:根据访问记录生成相应的Session信息记录，假设Session的过期时间是30分钟
 * 输入数据:IP_addr time method request_URL request_protocol respond_code respond_data requst_come_from browser
 * 输出数据:time IP_addr session request_URL referal
 */
public class logSession {

	/**
	 * Mapper端读取每行,输出key为ip,value为整行(WebLogBean的toString)
	 */
	private static class sessionMapper extends Mapper<Object, Text, Text, Text> {

		private Text IPAddr = new Text();
		private Text content = new Text();
		private NullWritable v = NullWritable.get();
		WebLogParser webLogParser = new WebLogParser();

		public void map(Object key, Text value, Context context) {

			String line = value.toString();
			String[] weblogArry = line.split(" ");

			IPAddr.set(weblogArry[0]);
			content.set(line);

			try {
				context.write(IPAddr, content);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reducer端对相同ip的values遍历解析,并组装为一个WebLogSessionBean对象,存入一个ArrayList中
	 * 通过临时一个比较器(按WebLogSessionBean类的time解析为data进行比较)对该list进行排序
	 * 然后遍历list,获取Session的startTime以及endTime(超出30分钟重设startTime以及UUID)
	 * 每次遍历都进行输出:以WebLogSessionBean的实例对象为key,value为null
	 */
	private static class sessionReducer extends Reducer<Text, Text, Text, NullWritable> {

		//private Text IPAddr = new Text();
		private Text content = new Text();
		private NullWritable v = NullWritable.get();
		//WebLogParser webLogParser = new WebLogParser();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SessionParser sessionParser = new SessionParser();

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

			Date sessionStartTime = null;
			String sessionID = UUID.randomUUID().toString();// 根据ip随机生成一个加密的UUID

			// 将IP地址所对应的用户的所有浏览记录按时间排序
			ArrayList<WebLogSessionBean> sessionBeanGroup = new ArrayList<>();
			for (Text browseHistory : values) {
				WebLogSessionBean sessionBean = sessionParser.loadBean(browseHistory.toString());
				sessionBeanGroup.add(sessionBean);
			}
			Collections.sort(sessionBeanGroup, new Comparator<WebLogSessionBean>() {

				public int compare(WebLogSessionBean sessionBean1, WebLogSessionBean sessionBean2) {
					Date date1 = sessionBean1.getTimeWithDateFormat();
					Date date2 = sessionBean2.getTimeWithDateFormat();
					if (date1 == null && date2 == null)
						return 0;
					return date1.compareTo(date2);
				}
			});

			for (WebLogSessionBean sessionBean : sessionBeanGroup) {

				if (sessionStartTime == null) {
					// 当天日志中某用户第一次访问网站的时间
					sessionStartTime = timeTransform(sessionBean.getTime());
					content.set(sessionParser.parser(sessionBean, sessionID));
					try {
						context.write(content, v);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}

				} else {
					Date sessionEndTime = timeTransform(sessionBean.getTime());
					long sessionStayTime = timeDiffer(sessionStartTime, sessionEndTime);

					// 超过30分钟,重设start Time为该次的点击时间,重设一个UUID
					if (sessionStayTime > 30 * 60 * 1000) {
						// 将当前浏览记录的时间设为下一个session的开始时间
						sessionStartTime = timeTransform(sessionBean.getTime());
						sessionID = UUID.randomUUID().toString();
						continue;
					}
					content.set(sessionParser.parser(sessionBean, sessionID));
					try {
						context.write(content, v);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		/**
		 * 将time格式化为yyyy-MM-dd HH:mm:ss
		 */
		private Date timeTransform(String time) {

			Date standard_time = null;
			try {
				standard_time = sdf.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return standard_time;
		}

		/**
		 * 指定两个data,计算时间差
		 */
		private long timeDiffer(Date start_time, Date end_time) {

			long diffTime = 0;
			diffTime = end_time.getTime() - start_time.getTime();

			return diffTime;
		}

	}

	/**
	 * Job设置根据当前job执行时间(天为单位)设置输出路径
	 */
	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();
		//conf.set("fs.defaultFS", "hdfs://ymhHadoop:9000");
		Job job = Job.getInstance(conf);
		job.setJarByClass(logClean.class);

		job.setMapperClass(sessionMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setReducerClass(sessionReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		String dateStr = new SimpleDateFormat("yy-MM-dd").format(new Date());
		FileInputFormat.setInputPaths(job, new Path("file/log/cleandata/" + dateStr + "/*"));
		FileOutputFormat.setOutputPath(job, new Path("file/log/sessiondata/" + dateStr + "/"));

		boolean res = job.waitForCompletion(true);
		System.exit(res ? 0 : 1);
	}
}