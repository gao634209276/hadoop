package org.hadoopinternal.ipc;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

public class IPCQueryServer {
	public static final int IPC_PORT = 32121;

	public static void main(String[] args) {
		try {
			Configuration conf = new Configuration();
			IPCQueryStatusImpl queryService = new IPCQueryStatusImpl();
			//IPCQueryServer ipcserver = new IPCQueryServer();
			System.out.println(conf);
			Server server = new RPC.Builder(conf)
					.setProtocol(IPCQueryStatus.class)
					.setInstance(queryService).setBindAddress("0.0.0.0")
					.setPort(IPC_PORT).setNumHandlers(1).setVerbose(true)
					.build();
			server.start();

			System.out.println("Server ready, press any key to stop");
			System.in.read();

			server.stop();
			System.out.println("Server stopped");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}