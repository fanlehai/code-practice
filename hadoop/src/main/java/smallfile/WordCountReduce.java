package smallfile;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordCountReduce extends Reducer<Text, Text, Text, Text> {

	public void reduce(Text _key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		// process values
		int nSum = 0;
		for (Text val : values) {
			nSum += Integer.parseInt(val.toString());
		}
		context.write(_key, new Text(Integer.toString(nSum)));
	}

}
