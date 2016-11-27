package common.rpc;

import common.rpc.service.CaculateService;
import common.rpc.service.CaculateServiceImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Server;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


/**
 * 注意，在Hadoop V2版本中，获取RPC下的Server对象不能在使用RPC.getServer()方法了，
 * 该方法已被移除，取而代之的是使用Builder方法来构建新的Server对象。
 * Server Main
 */
public class CaculateServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CaculateServer.class);

	public static final int IPC_PORT = 9090;

	public static void main(String[] args) {
		try {
			Server server = new RPC.Builder(new Configuration()).setProtocol(CaculateService.class)
					.setBindAddress("127.0.0.1").setPort(IPC_PORT).setInstance(new CaculateServiceImpl()).build();
			server.start();
			LOGGER.info("CaculateServer has started");
			System.in.read();
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("CaculateServer server error,message is " + ex.getMessage());
		}
	}

}