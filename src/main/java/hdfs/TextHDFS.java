package hdfs;

import java.io.IOException;
import java.net.URI;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

public class TextHDFS extends TestCase{

	public static String hdfsURL = "hdfs://hadoop:9000";
	//create HDFS folder
	@Test
	public void testHDFSMkdir() throws IOException{
		Configuration conf = new Configuration();
		
		FileSystem fs =FileSystem.get(URI.create(hdfsURL), conf);
		Path p1 = new Path("/test1/");
		fs.mkdirs(p1);
		Path p2 = new Path("/test1/test");
		fs.create(p2);
		FSDataOutputStream out =fs.create(p2);
		out.write("Hello hadoop".getBytes());
		fs.rename(p2, new Path("/test/b.txt"));
		fs.copyFromLocalFile(new Path("/home/hadoop/test/test"), p1);
		
		//IOUtils.copyBytes(in, out, conf, close);
		
	}
	
}
