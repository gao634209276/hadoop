package common.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;


public class StatsServer {

	private static Logger logger = LoggerFactory.getLogger(StatsServer.class);

	private final int PORT = 9090;

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void start() {
		try {
			TNonblockingServerSocket socket = new TNonblockingServerSocket(PORT);
			final StatQueryService.Processor processor = new StatQueryService.Processor(new StatQueryServiceImpl());
			THsHaServer.Args arg = new THsHaServer.Args(socket);
		    /*
             * Binary coded format efficient, intensive data transmission, The
             * use of non blocking mode of transmission, according to the size
             * of the block, similar to the Java of NIO
             */
			arg.protocolFactory(new TCompactProtocol.Factory());
			arg.transportFactory(new TFramedTransport.Factory());
			arg.processorFactory(new TProcessorFactory(processor));
			TServer server = new THsHaServer(arg);
			server.serve();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			logger.info("start thrift server...");
			StatsServer stats = new StatsServer();
			stats.start();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(String.format("run thrift server has error,msg is %s", ex.getMessage()));
		}
	}

}