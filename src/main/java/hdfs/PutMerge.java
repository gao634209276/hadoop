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


	/**
	 * 复制上传文件,并将文件合并
	 * 
	 * @param localDir
	 *            本地上传的文件目录
	 * @param hdfsFile
	 *            HDFS上的文件名称,包含路径
	 */
	public static void put(String localDir, String hdfsFile) throws Exception {

		// 1.获取配置信息
		Configuration conf = new Configuration();

		Path localPath = new Path(localDir);

		Path hdfsPath = new Path(hdfsFile);

		try {
			// 获取本地文件系统
			FileSystem localFs = FileSystem.getLocal(conf);
			// 获取HDFS
			FileSystem hdfs = FileSystem.get(conf);
			// 获取本地文件系统中指定目录的所有文件
			FileStatus[] status = localFs.listStatus(localPath);
			// 打开HDFS上文件的输出流
			FSDataOutputStream fsDataOutputStream = hdfs.create(hdfsPath);

			// 循环遍历本地文件
			for (FileStatus fileStatus : status) {
				// 获取文件
				Path path = fileStatus.getPath();
				System.out.println("文件为:" + path.getName());
				// 打开文件输入流
				FSDataInputStream fsDataInputStream = localFs.open(path);
				// 通过缓冲字节数组,进行流的读写操作
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = fsDataInputStream.read(buffer)) > 0) {
					fsDataOutputStream.write(buffer, 0, len);
				}
				fsDataInputStream.close();
			}
			fsDataOutputStream.close();
			System.out.println("传输完成");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
		String l = "/home/kqiao/hadoop/MyHadoopCodes/putmergeFiles";
		String f = "hdfs://ubuntu:9000/user/kqiao/test/PutMergeTest";
		putMergeFunc(l, f);
	}
}