package common.rank;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

public class SequenceFileWriterExample {

	
	public static void main(String[] args) {
	
		String intputDir = args[0];
		Path outPath = new Path(args[1]);
		String compressionType = args[2];
		Configuration conf = new Configuration();
		Path outputPath = new Path(args[1]);
		
		
		outputPath.getFileSystem(conf).delete(outputPath,true);
		Collection<File> listFiles = FileUtils.listFile(new File(inputDir),new String[]{"html"} );
		for (File file : listFiles) {
			String fileName = file.getName();
			String content = FileUtils.readFileToString(file);
			Text key = new Text();
			key.set(fileName);
			Text value = new Text();
			
			writer.append(key,value);
			
		}
		writer.close();
	}
}
