package common.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Writable;

import java.io.IOException;

/**
 * An RPC Server.
 */
public abstract class Server extends org.apache.hadoop.ipc.Server {
	boolean verbose;

	protected Server(String bindAddress, int port, Class<? extends Writable> paramClass, int handlerCount, Configuration conf) throws IOException {
		super(bindAddress, port, paramClass, handlerCount, conf);
	}

	static String classNameBase(String className) {
		String[] names = className.split("\\.", -1);
		if (names == null || names.length == 0) {
			return className;
		}
		return names[names.length - 1];
	}
}