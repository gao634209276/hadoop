package mapreduce.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class TextPairWritable implements WritableComparable<TextPairWritable> {

	private Text first;
	private Text second;

	public TextPairWritable() {

	}
	
	public void set(Text first, Text second) {
		this.first = first;
		this.second = second;
	}

	public TextPairWritable(Text first, Text second) {
		set(first, second);
	}

	public Text getFirst() {
		return first;
	}

	public Text getSecond() {
		return second;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		first.write(out);
		second.write(out);

	}

	@Override
	public void readFields(DataInput in) throws IOException {
		first.readFields(in);
		second.readFields(in);
	}

	@Override
	public int compareTo(TextPairWritable o) {
		int cmp = this.first.compareTo(o.getFirst());
		if (0 != cmp) {
			return cmp;
		}
		return this.second.compareTo(o.getSecond());
	}

	public int hashCode() {
		return first.hashCode() * 163 + second.hashCode();
	}

	public boolean equals(Object obj) {
		if (obj instanceof TextPairWritable) {
			TextPairWritable pair = (TextPairWritable) obj;
			return first.equals(pair.getFirst()) && second.equals(pair.getSecond());
		}
		
		return false;

	}

	public String toString() {
		return first + "\t" + second;

	}
}
