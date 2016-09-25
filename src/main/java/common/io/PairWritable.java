package common.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableUtils;

public class PairWritable implements WritableComparable<PairWritable> {

	private String name;// Text
	private Integer age;// IntWritable

	public PairWritable() {

	}

	public PairWritable(String name, Integer age) {
		set(name, age);
	}

	public String getName() {
		return name;
	}

	public void set(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	public Integer getAge() {
		return age;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PairWritable other = (PairWritable) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(name);
		out.writeInt(age);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.name = in.readUTF();
		this.age = in.readInt();
	}

	@Override
	public int compareTo(PairWritable o) {
		int cmp = this.name.compareTo(o.getName());
		if (cmp != 0) {
			return cmp;
		}
		return this.age.compareTo(o.getAge());
	}

	public static class Comparator extends WritableComparator {

		public Comparator() {
			super(PairWritable.class);
		}

		/**
		 * ��һ���ֽ�����
		 * <p>
		 * byte[] b1, int s1, int l1
		 * <p>
		 * �ֽ����� ��ʼλ�� ���� �ڶ����ֽ�����
		 * <p>
		 * byte[] b2, int s2, int l2
		 * <p>
		 * �ֽ����� ��ʼλ�� ����
		 */
		@Override
		public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
			int n1 = WritableUtils.decodeVIntSize(b1[s1]);
			int n2 = WritableUtils.decodeVIntSize(b2[s2]);
			int cmp = WritableComparator.compareBytes(b1, s1 + n1, l1 - n1, b2, s2 + n2, l2 - n2);
			if (0 != cmp) {
				return cmp;
			}

			int thisValue = readInt(b1, l1 - s1 - n1);
			int thatValue = readInt(b2, l2 - s2 - n2);
			
			return (thisValue < thatValue ? -1 : (thisValue == thatValue ? 0 : 1));
		}
	}
	
	static {
		WritableComparator.define(PairWritable.class, new Comparator());
	}

}
