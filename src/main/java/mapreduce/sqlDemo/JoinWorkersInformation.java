package mapreduce.sqlDemo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class JoinWorkersInformation {

	public static class DataMapper extends
			Mapper<LongWritable, Text, LongWritable, WorkerInformation> {

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			System.out.println("Map Methond Invoked!!!");

			String inputData = value.toString();
			String[] data = inputData.split("\t");
			if (data.length <= 3) { // department
				WorkerInformation department = new WorkerInformation();
				department.setDepartmentNo(data[0]);
				department.setDepartmentName(data[1]);
				department.setFlag(0);
				context.write(new LongWritable(Long.valueOf(department.getDepartmentNo())), department);
			} else { // worker

				WorkerInformation worker = new WorkerInformation();
				worker.setWorkerNo(data[0]);
				worker.setWorkerName(data[1]);
				worker.setDepartmentNo(data[7]);
				worker.setFlag(1);
				context.write(new LongWritable(Long.valueOf(worker.getDepartmentNo())), worker);
			}

		}

	}

	public static class DataReducer extends
			Reducer<LongWritable, WorkerInformation, LongWritable, Text> {

		public void reduce(Text key, Iterable<WorkerInformation> values,
		                   Context context) throws IOException, InterruptedException {
			System.out.println("Reduce Methond Invoked!!!");
			LongWritable resultKey = new LongWritable(0);
			Text resultValue = new Text();

			WorkerInformation department = null;
			List<WorkerInformation> workerList = new ArrayList<WorkerInformation>();

			for (WorkerInformation item : values) {
				if (0 == item.getFlag()) {
					department = new WorkerInformation(item);
				} else {
					workerList.add(new WorkerInformation(item));

				}
			}
			for (WorkerInformation worker : workerList) {
				worker.setDepartmentNo(department.getDepartmentNo());
				worker.setDepartmentName(department.getDepartmentName());
				resultValue.set(worker.toString());
				context.write(resultKey, resultValue);

			}

		}

	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length < 2) {
			System.err
					.println("Usage: JoinWorkersInformation <in> [<in>...] <out>");
			System.exit(2);
		}

		Job job = Job.getInstance(conf, "URLLog");
		job.setJarByClass(JoinWorkersInformation.class);
		job.setMapperClass(DataMapper.class);
		job.setReducerClass(DataReducer.class);

		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(WorkerInformation.class);

		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);
		for (int i = 0; i < otherArgs.length - 1; ++i) {
			FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
		}
		FileOutputFormat.setOutputPath(job, new Path(
				otherArgs[otherArgs.length - 1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}

class WorkerInformation implements WritableComparable {
	private String workerNo = "";
	private String workerName = "";
	private String departmentNo = "";
	private String departmentName = "";
	private int flag = 0; // 0 department，1 worker

	public WorkerInformation() {

	}

	public WorkerInformation(String workerNo, String workerName,
	                         String departmentNo, String departmentName, int flag) {
		super();
		this.workerNo = workerNo;
		this.workerName = workerName;
		this.departmentNo = departmentNo;
		this.departmentName = departmentName;
		this.flag = flag;
	}

	public WorkerInformation(WorkerInformation info) {
		this.workerNo = info.departmentNo;
		this.workerName = info.workerName;
		this.departmentNo = info.departmentNo;
		this.departmentName = info.departmentName;
		this.flag = info.flag;
	}

	@Override
	public void readFields(DataInput input) throws IOException {
		this.workerNo = input.readUTF();
		this.workerName = input.readUTF();
		this.departmentNo = input.readUTF();
		this.departmentName = input.readUTF();
		this.flag = input.readInt();

	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeUTF(this.workerNo);
		output.writeUTF(this.workerName);
		output.writeUTF(this.departmentNo);
		output.writeUTF(this.departmentName);
		output.writeInt(this.flag);
	}

	@Override
	public int compareTo(Object o) {

		return 0;
	}

	@Override
	public String toString() {
		return this.workerNo + "   " + this.workerName + " "
				+ this.departmentNo + "  " + this.departmentName;
	}

	public String getWorkerNo() {
		return workerNo;
	}

	public void setWorkerNo(String workerNo) {
		this.workerNo = workerNo;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getDepartmentNo() {
		return departmentNo;
	}

	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
