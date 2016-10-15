package hdfs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class IOCopy {
	public static void main(String[] args) throws IOException, URISyntaxException {

		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop:9000"), new Configuration());

		InputStream in = fs.open(new Path("/test.java"));
		OutputStream out = new FileOutputStream("/home/hadoop/test/test");	
		IOUtils.copyBytes(in, out, 4096,true);
	}
}
