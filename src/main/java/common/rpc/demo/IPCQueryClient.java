package common.rpc.demo;

import java.net.InetSocketAddress;  
import org.apache.hadoop.conf.Configuration;  
import org.apache.hadoop.ipc.RPC;  

  
public class IPCQueryClient {  

	public static void main(String[] args) {  
        try {  
            System.out.println("Interface name: "+IPCQueryStatus.class.getName());  
            System.out.println("Interface name: "+IPCQueryStatus.class.getMethod("getFileStatus", String.class).getName());  
              
            InetSocketAddress addr=new InetSocketAddress("localhost", 32121);  
              
            IPCQueryStatus proxy = RPC.getProtocolProxy(IPCQueryStatus.class, IPCQueryStatus.versionID, addr, new Configuration()).getProxy();
            IPCFileStatus status=proxy.getFileStatus("/home/hadoop/zookeeper.out");  
            System.out.println(status);  
            RPC.stopProxy(proxy);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}  