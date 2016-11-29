package hdfs;

import java.io.InputStream;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

/**
 * HDFS API URL方式操作
 */
public class HDFSUrlTest {

	// 让java程序识别HDFS的DURL
	static {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}

	// 查看文件内容
	@Test
	public void testRead() throws Exception {
		InputStream in = null;
		// 文件路径,服务器域名+端口号+路径全称
		String fileUrl = "hdfs://hadoop-master.dragon.org:9000/opt/data/test/01.data";
		try {
			// 获取文件输入流--open()
			in = new URL(fileUrl).openStream();
			// 将文件内容打印到控制台--read()
			IOUtils.copyBytes(in, System.out, 4096, false);
		} finally {
			// close()
			IOUtils.closeStream(in);
		}

	}
}