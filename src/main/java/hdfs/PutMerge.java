package hdfs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

//参数1为本地目录，参数2为HDFS上的文件
public class PutMerge {

	public static void putMergeFunc(String LocalDir, String fsFile)
			throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf); // fs是HDFS文件系统
		FileSystem local = FileSystem.getLocal(conf); // 本地文件系统

		Path localDir = new Path(LocalDir);
		Path HDFSFile = new Path(fsFile);

		FileStatus[] status = local.listStatus(localDir); // 得到输入目录
		FSDataOutputStream out = fs.create(HDFSFile); // 在HDFS上创建输出文件

		for (FileStatus st : status) {
			Path temp = st.getPath();
			FSDataInputStream in = local.open(temp);
			IOUtils.copyBytes(in, out, 4096, false); // 读取in流中的内容放入out
			in.close(); // 完成后，关闭当前文件输入流
		}
		out.close();
	}

	public static void main(String[] args) throws IOException {
		String l = "/home/kqiao/hadoop/MyHadoopCodes/putmergeFiles";
		String f = "hdfs://ubuntu:9000/user/kqiao/test/PutMergeTest";
		putMergeFunc(l, f);
	}
}