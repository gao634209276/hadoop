package common.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

public class Demo1 {

	public static void compress(String method) throws ClassNotFoundException,
			IOException {
		File fileIn = new File("/home/hadoop/test/data/008411-99999-2016");
		// 输入流
		InputStream in = new FileInputStream(fileIn);
		Class<?> codecClass = Class.forName(method);
		Configuration conf = new Configuration();
		// 编码/解码器
		CompressionCodec codec = (CompressionCodec) ReflectionUtils
				.newInstance(codecClass, conf);
		File fileOut = new File("/home/hadoop/test/data/out"
				+ codec.getDefaultExtension());
		fileOut.delete();
		// 输出流
		OutputStream out = new FileOutputStream(fileOut);
		// 通过编码/解码器 创建对应的输出流
		CompressionOutputStream cout = codec.createOutputStream(out);
		// 压缩
		IOUtils.copyBytes(in, cout, 4096, false);

		in.close();
		out.close();
	}

	public static void decompress(File file) throws IOException {
		Configuration conf = new Configuration();
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
		CompressionCodec codec = factory.getCodec(new Path(file.getName()));
		// 通过文件扩展名获取相应的编码/解码器
		if (codec == null) {
			System.out.println("Connot find codec for file " + file);
		}
		File fileOut = new File(file.getName() + ".txt");
		// 通过编码/解码器创建对应的输入流
		InputStream in = codec.createInputStream(new FileInputStream(file));
		OutputStream out = new FileOutputStream(fileOut);
		IOUtils.copyBytes(in, out, 4096, false);
	}

	public static void main(String[] args) throws ClassNotFoundException,
			IOException {
		Demo1.compress("org.apache.hadoop.io.compress.DefaultCodec");
		Demo1.compress("org.apache.hadoop.io.compress.GzipCodec");
		Demo1.compress("org.apache.hadoop.io.compress.BZip2Codec");
		// c.compress("org.apache.hadoop.io.compress.SnappyCodec");

		Demo1.decompress(new File("/home/hadoop/test/data/out.bz2"));
		Demo1.decompress(new File("/home/hadoop/test/data/out.deflate"));
		Demo1.decompress(new File("/home/hadoop/test/data/out.gz"));
		// CodeDemo.decompress(new File("/home/hadoop/test/data/out.snappy"));
	}
}
