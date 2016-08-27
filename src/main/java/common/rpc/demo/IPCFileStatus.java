package common.rpc.demo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class IPCFileStatus implements Writable {
	private String filename;
	private long time;

	public IPCFileStatus() {
	}

	public IPCFileStatus(String filename) {
		this.filename = filename;
		this.time = (new Date()).getTime();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String toString() {
		return "File: " + filename + " Create at " + (new Date(time));
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.filename = Text.readString(in);
		this.time = in.readLong();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, filename);
		out.writeLong(time);
	}
}