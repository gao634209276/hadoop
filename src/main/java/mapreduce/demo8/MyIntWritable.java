package mapreduce.demo8;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class MyIntWritable implements WritableComparable<MyIntWritable> {
	private Integer num;

	public MyIntWritable(Integer num) {
		this.num = num;
	}

	public MyIntWritable() {
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(num);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.num = in.readInt();
	}

	@Override
	public int compareTo(MyIntWritable o) {
		int minus = this.num - o.num;

		return minus * (-1);
	}

	@Override
	public int hashCode() {
		return this.num.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MyIntWritable) {
			return false;
		}
		MyIntWritable ok2 = (MyIntWritable) obj;
		return (this.num == ok2.num);
	}

	@Override
	public String toString() {
		return num + "";
	}

}