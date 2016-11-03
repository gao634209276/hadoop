package mapreduce.nginxlog.session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SessionParser {

	SimpleDateFormat sdf_origin = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss",
			Locale.ENGLISH);
	SimpleDateFormat sdf_final = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	/**
	 * 根据指定webSessionBean的sessionID,返回该对象的toString
	 */
	public String parser(WebLogSessionBean sessionBean, String sessionID) {

		sessionBean.setSession(sessionID);
		return sessionBean.toString();
	}

	/**
	 * 读取的来自WebLogBean的toString:
	 * IP_addr time method request_URL request_protocol respond_code respond_data requst_come_from browser
	 * 解析字符串组装为WebLogSessionBean类对象返回
	 */
	public WebLogSessionBean loadBean(String sessionContent) {

		WebLogSessionBean weblogSession = new WebLogSessionBean();

		String[] contents = sessionContent.split(" ");
		weblogSession.setTime(timeTransform(contents[1]));//将日志格式化为:yyyy-MM-dd HH:mm:ss
		weblogSession.setIP_addr(contents[0]);
		weblogSession.setRequest_URL(contents[3]);
		weblogSession.setReferal(contents[7]);

		return weblogSession;
	}

	private String timeTransform(String time) {

		Date standard_time = null;
		try {
			standard_time = sdf_origin.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf_final.format(standard_time);
	}
}