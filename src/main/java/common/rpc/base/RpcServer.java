package common.rpc.base;
import java.io.IOException;
import org.apache.hadoop.HadoopIllegalArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Server;

public class RpcServer implements Bizable {

	public String sysHi(String name) {
		return "Hi~" + name;
	}
	public static void main(String[] args) throws HadoopIllegalArgumentException, IOException {
		Configuration conf = new Configuration();
		Server server = new RPC.Builder(conf).setProtocol(Bizable.class).setInstance(new RpcServer())
				.setBindAddress("hadoop").setPort(9527).build();
		server.start();
	}
}
