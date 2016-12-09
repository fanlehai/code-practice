package smallfile;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WordCountMap extends Mapper<LongWritable, Text, Text, Text> {

	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {

		StringTokenizer stringTokenizer = new StringTokenizer(ivalue.toString());
		int nCount = 0;
		while (stringTokenizer.hasMoreTokens()) {
			stringTokenizer.nextToken();
			nCount++;
		}
		context.write(new Text("sum : "), new Text(Integer.toString(nCount)));

	}

}
