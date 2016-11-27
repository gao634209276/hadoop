package common.rpc;

import java.util.Map;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
public class StatsClient {

	public static final String ADDRESS = "127.0.0.1";
	public static final int PORT = 9090;
	public static final int TIMEOUT = 30000;

	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("args length must >= 4,current length is " + args.length);
			System.out.println("<info>****************</info>");
			System.out.println("ADDRESS,beginDate,endDate,kpiCode,...");
			System.out.println("<info>****************</info>");
			return;
		}
		TTransport transport = new TFramedTransport(new TSocket(args[0], PORT, TIMEOUT));
		TProtocol protocol = new TCompactProtocol(transport);
		StatQueryService.Client client = new StatQueryService.Client(protocol);
		String beginDate = args[1]; // "20150308"
		String endDate = args[2]; // "20150312"
		String kpiCode = args[3]; // "login_times"
		String userName = "";
		int areaId = 0;
		String type = "";
		String fashion = "";

		try {
			transport.open();
			Map<String, String> map = client.queryConditionDayKPI(beginDate, endDate, kpiCode, userName, areaId, type,
					fashion);
			System.out.println(map.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			transport.close();
		}
	}

}