package mapreduce.nginxlog.logclean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用正则表达式匹配出合法的日志记录
 */
public class WebLogParser {

	public String parser(String weblog_origin) {

		WebLogBean weblogbean = new WebLogBean();

		/**
		 * 获取IP地址如:124.42.13.230
		 * Pattern:任意多个数字.任意多数字.任意多数字.任意多数字
		 */
		Pattern IPPattern = Pattern.compile("\\d+.\\d+.\\d+.\\d+");
		Matcher IPMatcher = IPPattern.matcher(weblog_origin);
		if (IPMatcher.find()) {
			String IPAddr = IPMatcher.group(0);
			weblogbean.setIP_addr(IPAddr);
		} else {
			return "";
		}
		/**
		 *  获取时间信息[18/Sep/2013:06:57:50 +0000]
		 *  Pattern:[任意多字符为一组group]
		 */
		Pattern TimePattern = Pattern.compile("[(.+)]");
		Matcher TimeMatcher = TimePattern.matcher(weblog_origin);
		if (TimeMatcher.find()) {
			String time = TimeMatcher.group(1);
			String[] cleanTime = time.split(" ");
			weblogbean.setTime(cleanTime[0]);
		} else {
			return "";
		}

		/**
		 * 获取其余请求信息
		 * Pattern:
		 * group(1): \"GET /shoppingMall?ver=1.2.1 HTTP/1.1\"
		 * 这里由于"属于特殊符号,程序读取到的log中是用\"表示,而pattern要对程序读取的字符串匹配所以用:\\\",即\"
		 * group(2): 200
		 * group(3): 7200
		 * group(4): \"http://www.baidu.com.cn\"
		 * group(5): \"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; BTRS101170; InfoPath.2; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727)\"
		 */
		Pattern InfoPattern = Pattern.compile("(\\\"[POST|GET].+?\\\") (\\d+) (\\d+).+?(\\\".+?\\\") (\\\".+?\\\")");

		Matcher InfoMatcher = InfoPattern.matcher(weblog_origin);
		if (InfoMatcher.find()) {

			// \"GET /shoppingMall?ver=1.2.1 HTTP/1.1\"
			String requestInfo = InfoMatcher.group(1).replace('\"', ' ').trim();
			String[] requestInfoArry = requestInfo.split(" ");
			weblogbean.setMethod(requestInfoArry[0]);// GET
			weblogbean.setRequest_URL(requestInfoArry[1]);// /shoppingMall?ver=1.2.1
			weblogbean.setRequest_protocol(requestInfoArry[2]);// HTTP/1.1
			// 200
			String status_code = InfoMatcher.group(2);
			weblogbean.setRespond_code(status_code);

			// 7200
			String respond_data = InfoMatcher.group(3);
			weblogbean.setRespond_data(respond_data);

			// \"http://www.baidu.com.cn\"
			String request_come_from = InfoMatcher.group(4).replace('\"', ' ').trim();
			weblogbean.setRequst_come_from(request_come_from);

			// \"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; BTRS101170; InfoPath.2; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727)\"
			String browserInfo = InfoMatcher.group(5).replace('\"', ' ').trim();
			weblogbean.setBrowser(browserInfo);
		} else {
			return "";
		}
		return weblogbean.toString();
	}
}