package mapreduce.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 *Hadoop value Data Type
 */
public class MyWritable implements Writable {

	private int counter;
	private long timestamp;

	public int getCounter() {
		return counter;
	}

	public void set(int counter, long timestamp) {
		this.counter = counter;
		this.timestamp = timestamp;
	}

	public MyWritable() {
	}

	public MyWritable(int counter, long timestamp) {
		set(counter, timestamp);
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(counter);
		out.writeLong(timestamp);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.counter = in.readInt();
		this.timestamp = in.readLong();
	}

	public static MyWritable read(DataInput in) throws IOException{
		MyWritable myWritable = new MyWritable();
		myWritable.readFields(in);
		return myWritable;
	}
	@Override
	public String toString() {
		return this.counter+"\t"+this.timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + counter;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyWritable other = (MyWritable) obj;
		if (counter != other.counter)
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}
	
}
