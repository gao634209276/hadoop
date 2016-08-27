package common.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.io.compress.Compressor;
import org.apache.hadoop.io.compress.zlib.BuiltInZlibDeflater;

public class Demo2 {
	
	private static int conpressorOutputBufferSize = 1024 * 1024;

	public static void compress() throws ClassNotFoundException,
			IOException {
		File fileIn = new File("/home/hadoop/test/data/008411-99999-2016");
		InputStream in = new FileInputStream(fileIn);
		int datalength = in.available();
		byte[] inbuf = new byte[datalength];
		in.read(inbuf, 0, datalength);
		in.close();
		// 长度受限的输出缓冲区,用于说明finished()方法
		byte[] outbuf = new byte[conpressorOutputBufferSize];

		Compressor compressor = new BuiltInZlibDeflater();// 构造压缩器
		int step = 100;
		int inputPos = 0;
		int putcount = 0;
		int getcount = 0;
		int compressedlen = 0;
		while (inputPos < datalength) {
			// 进行多次setInput()将inbuf分成几个部分,送往compressor中
			int len = (datalength - inputPos >= step) ? step : datalength
					- inputPos;
			compressor.setInput(inbuf, inputPos, len);
			putcount++;
			while (!compressor.needsInput()) {
				compressedlen = compressor.compress(outbuf, 0, 1024);
				if (compressedlen > 0) {
					getcount++;// 能读到数据
				}
			}// end of while(!compressor.needInput())
			inputPos += step;
		}
		compressor.finish();
		// 通过finished()循环判断compressor
		// 是否还有未读取的数据并使用compress获取
		while (!compressor.finished()) {// 压缩器中有数据
			getcount++;
			compressor.compress(outbuf, 0, conpressorOutputBufferSize);
		}
		System.out.println("Copress " + compressor.getBytesRead()// 输出信息
				+ " bytes into " + compressor.getBytesWritten());
		System.out.println("put " + putcount + " times and get " + getcount
				+ " times");
		compressor.end();// 停止
	}
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Demo2.compress();
	}
}
