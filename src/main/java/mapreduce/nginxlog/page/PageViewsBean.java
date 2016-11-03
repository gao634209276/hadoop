package mapreduce.nginxlog.page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PageViewsBean数据结构保存一下数据:
 * session IP_addr time visit_URL stayTime step
 */
public class PageViewsBean {

	private String session;
	private String IP_addr;
	private String time;
	private String visit_URL;
	private String stayTime;
	private String step;

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getIP_addr() {
		return IP_addr;
	}

	public void setIP_addr(String iP_addr) {
		IP_addr = iP_addr;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getVisit_URL() {
		return visit_URL;
	}

	public void setVisit_URL(String visit_URL) {
		this.visit_URL = visit_URL;
	}

	public String getStayTime() {
		return stayTime;
	}

	public void setStayTime(String stayTime) {
		this.stayTime = stayTime;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	/**
	 * 对该对象的属性time格式化yyyy-MM-dd HH:mm:ss,返回此格式的Data
	 */
	public Date getTimeWithDateFormat() {

		SimpleDateFormat sdf_final = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (this.time != null && this.time != "") {
			try {
				return sdf_final.parse(this.time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return session + " " + IP_addr + " " + time + " " + visit_URL + " " + stayTime + " " + step;
	}
}