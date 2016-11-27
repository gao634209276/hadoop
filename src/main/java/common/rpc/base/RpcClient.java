package common.rpc.base;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtocolProxy;
import org.apache.hadoop.ipc.RPC;

public class RpcClient {

	public static void main(String[] args) throws IOException {
		ProtocolProxy<Bizable> proxy = RPC.getProtocolProxy(Bizable.class, 1L, new InetSocketAddress("localhost",9527), new Configuration());
		String result =  proxy.getProxy().sysHi("tomcat");
		System.out.println(result);
	}
}
