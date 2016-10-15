package hdfs;

import java.io.IOException;
import java.net.URI;

import junit.framework.TestCase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class TextFileSystem extends TestCase {

	public static String hdfsURL = "hdfs://hadoop:9000";

	public void testHDFSMkdir() throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(hdfsURL), conf);
		Path p1 = new Path("/test1/");
		Path p2 = new Path("/test1/test");

		fs.mkdirs(p1);

		//fs.create返回一个输出流
		FSDataOutputStream out = fs.create(p2);
		out.write("Hello hadoop".getBytes());

		fs.rename(p2, new Path("/test/b.txt"));

		fs.copyFromLocalFile(new Path("/home/hadoop/test/test"), p1);

		//IOUtils.copyBytes(in, out, conf, close);
	}

	public static void testListFile() throws IOException {
		Configuration conf = new Configuration();

		FileSystem fs = FileSystem.get(conf);
		Path list = new Path("/opt/data");

		FileStatus status[] = fs.listStatus(list);

		for (FileStatus fileStatus : status) {
			System.out.println(fileStatus.getPath().toString());
		}

		fs.close();
	}

}
