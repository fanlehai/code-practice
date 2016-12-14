package serialize.sequencefile;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FileMap extends Mapper<Text, Text, Text, Text> {

	public void map(Text ikey, Text ivalue, Context context) throws IOException, InterruptedException {

		System.out.println("key = "+ikey+"  |  value = "+ivalue);
		context.write(ikey, ivalue);
	}

}
