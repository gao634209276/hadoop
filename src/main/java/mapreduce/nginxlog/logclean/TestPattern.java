package mapreduce.nginxlog.logclean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPattern {
	public static void main(String[] args) {


		Pattern InfoPattern = Pattern.compile("(\\\"[POST|GET].+?\\\") (\\d+) (\\d+).+?(\\\".+?\\\") (\\\".+?\\\")");

		Matcher m = InfoPattern.matcher("\"GET /shoppingMall?ver=1.2.1 HTTP/1.1\" 200 7200 \"http://www.baidu.com.cn\" \"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; BTRS101170; InfoPath.2; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727)\"\n");
		if (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				System.out.println(m.group(i));
			}
		}
	}
}
