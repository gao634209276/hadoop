package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

/**
 * HDFS 工具类
 */
public class HDFSUtils {
	public static FileSystem getFileSystem() {
		// 声明FilSystem
		FileSystem hdfs = null;
		try {
			// 获取文件信息
			Configuration conf = new Configuration();
			// 获取文件系统
			hdfs = FileSystem.get(conf);
		} catch (Exception e) {

		}
		return hdfs;
	}
}
