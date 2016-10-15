package mapreduce.base.writable.topK;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * 自定义一个WritableComparable实现类
 */
public class TopKWritable implements WritableComparable<TopKWritable>{
	private String word;
	private Long count;
	public Long getCount() {
		return count;
	}
	public void set(String word,Long count) {
		this.word = word;
		this.count = count;
	}
	public String getWord() {
		return word;
	}
	public TopKWritable(String word, Long count) {
		set( word, count);
	}

	public TopKWritable(){
		
	}
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(word);
		out.writeLong(count);
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		this.word = in.readUTF();
		this.count = in.readLong();
	}
	@Override
	public int compareTo(TopKWritable o) {
		int cmp = this.word.compareTo(o.getWord());
		if(cmp!=0){
			return cmp;
		}
		
		return this.count.compareTo(o.getCount());
	}
	@Override
	public String toString() {
		return  word + "\t" + count ;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
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
		TopKWritable other = (TopKWritable) obj;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
	
	
}
