package hdfs;

import java.io.IOException;

import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

/**
 * 通过FileSystem API操作HDFS
 */
public class HDFSFsTest {

	// 读取--IOUtils.copyBytes(in, out, conf, close);
	@Test
	public void testRead() throws Exception {
		/*
		 * //获取配置 Configuration conf = new Configuration(); //获取文件系统 FileSystem
		 * hdfs = FileSystem.get(conf);
		 */
		FileSystem hdfs = HDFSUtils.getFileSystem();
		// 文件名称
		// Path path = new Path("/opt/data/test/01.data");
		Path path = new Path("/opt/data/dir/touch.data");
		// 打开输入流--open()
		FSDataInputStream inStream = hdfs.open(path);
		// 进行文件读取,到控制台显示--read()

		IOUtils.copyBytes(inStream, System.out, 4096, false);
		// 关闭流--close()
		IOUtils.closeStream(inStream);
	}

	// 查看目录--listStatus(path)
	@Test
	public void testList() throws Exception {
		FileSystem hdfs = HDFSUtils.getFileSystem();
		Path path = new Path("/opt/data/");
		FileStatus[] fileStatus = hdfs.listStatus(path);
		for (FileStatus fs : fileStatus) {
			Path p = fs.getPath();
			String info = fs.isDir() ? "目录" : "文件";
			System.out.println(info + p.toString());
		}
	}

	// 创建目录--mkdirs(path)
	@Test
	public void create() throws Exception {
		FileSystem hdfs = HDFSUtils.getFileSystem();
		// 目录
		Path path = new Path("/opt/data/dir");
		boolean isSuccess = hdfs.mkdirs(path);
		String info = isSuccess ? "成功" : "失败";
		System.out.println("创建目录[" + path + "]" + info);
	}

	// 上传--copyFromLocalFile(srcPath, dstPath);
	@Test
	public void testPut() throws IOException {
		FileSystem hdfs = HDFSUtils.getFileSystem();
		// 本地上传文件(目录+文件名称)
		//Path srcPath = new Path("E:/Workspaces/FileInput/books-UTF-8.xml");
		Path srcPath = new Path("E:/Workspaces/FileInput/jdk-7u80-linux-x64.tar.gz");
		// HDFS文件上传路径(目录or目录+文件名称)
		Path dstPath = new Path("/opt/data/dir/");
		hdfs.copyFromLocalFile(srcPath, dstPath);
	}

	// 创建文件并写入内容--hdfs.create(path);writeUTF(" str");
	@Test
	public void createAndWrite() throws Exception {
		FileSystem hdfs = HDFSUtils.getFileSystem();
		Path path = new Path("/opt/data/dir/touch");
		FSDataOutputStream fsDataOutputStream = hdfs.create(path);
		// 在字符串前空一格空格.不加空格上传后字符出现问题
		fsDataOutputStream.writeUTF(" Hello Hadoop");
		// fsDataOutputStream.write("Hello Hadoop");
		IOUtils.closeStream(fsDataOutputStream);
	}

	// 对HDFS 文件重命名--hdfs.rename(src, dst)
	@Test
	public void testReanme() throws IOException {
		FileSystem hdfs = HDFSUtils.getFileSystem();
		Path srcPath = new Path("/opt/data/dir/touch");
		Path destPath = new Path("/opt/data/dir/renametouch");

		boolean isSuccess = hdfs.rename(srcPath, destPath);
		String info = isSuccess ? "成功" : "失败";
		System.out.println("重命名" + srcPath + info);
	}

	// 删除文件--deleteOnExit(f)
	@Test
	public void testDelete() throws IOException {
		FileSystem hdfs = HDFSUtils.getFileSystem();
		Path srcPath = new Path("/opt/data/dir/renametouch");

		System.out.println(hdfs.deleteOnExit(srcPath));
	}

	// 删除路径--delete(f, recursive)
	@Test
	public void testDeleteDir() throws IOException {
		FileSystem hdfs = HDFSUtils.getFileSystem();
		Path srcPath = new Path("/opt/data/dir");

		System.out.println(hdfs.delete(srcPath, true));
	}

	// 查找某个文件在HFDS集群的位置
	@Test
	public void testLocation() throws IOException {
		FileSystem hdfs = HDFSUtils.getFileSystem();
		Path path = new Path("/opt/data/dir/file");

		FileStatus fileStatus = hdfs.getFileStatus(path);
		BlockLocation[] blockLocation = hdfs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
		for (BlockLocation bl : blockLocation) {
			String[] hosts = bl.getHosts();
			for (String host : hosts) {
				System.out.print(host+" ");
			}
		}
	}
	
	//获取HDFS集群上所有节点名称信息
	@Test
	public void testCluster() throws IOException{
		FileSystem fs = HDFSUtils.getFileSystem();
		DistributedFileSystem dfs = (DistributedFileSystem) fs;
		DatanodeInfo[] daInfos = dfs.getDataNodeStats();
		for (DatanodeInfo daInfo : daInfos) {
			System.out.println(daInfo.getHostName());
		}
	}
}
