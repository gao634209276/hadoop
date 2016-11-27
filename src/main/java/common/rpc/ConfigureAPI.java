package common.rpc;

/**
 * 本工程使用的是Hadoop-2.6.0版本，这里CaculateService接口需要加入注解，来声明版本号
 * Defined rpc info
 */
public class ConfigureAPI {

	public interface VersionID {
		public static final long RPC_VERSION = 7788L;
	}

	public interface ServerAddress {
		public static final int NIO_PORT = 8888;
		public static final String NIO_IP = "127.0.0.1";
	}

}