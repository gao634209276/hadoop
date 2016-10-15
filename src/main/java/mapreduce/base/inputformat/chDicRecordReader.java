package mapreduce.base.inputformat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.RecordReader;

import java.io.IOException;

/**
 * RecordReader的重写
 */
public class chDicRecordReader implements RecordReader<Text, Text> {

	private static final Log LOG = LogFactory.getLog(chDicRecordReader.class.getName());
	private CompressionCodecFactory compressionCodecs = null;
	private long start;
	private long pos;
	private long end;
	private byte[] buffer;
	private String keyName;
	private FSDataInputStream fileIn;

	public chDicRecordReader(Configuration job, FileSplit split) throws IOException {
		start = split.getStart(); //从中可以看出每个文件是作为一个split的
		end = split.getLength() + start;
		final Path path = split.getPath();
		keyName = path.toString();
		LOG.info("filename in hdfs is : " + keyName);
		final FileSystem fs = path.getFileSystem(job);
		fileIn = fs.open(path);
		fileIn.seek(start);
		buffer = new byte[(int) (end - start)];
		this.pos = start;
	}

	public Text createKey() {
		return new Text();
	}

	public Text createValue() {
		return new Text();
	}

	public long getPos() throws IOException {
		return pos;
	}

	public boolean next(Text key, Text value) throws IOException {
		while (pos < end) {
			key.set(keyName);
			value.clear();
			fileIn.readFully(pos, buffer);
			value.set(buffer);
//      LOG.info("---内容: " + value.toString());
			pos += buffer.length;
			LOG.info("end is : " + end + " pos is : " + pos);
			return true;
		}
		return false;
	}

	public void close() throws IOException {
		if (fileIn != null) {
			fileIn.close();
		}
	}

	@Override
	public float getProgress() throws IOException {
		return 0;
	}
}
