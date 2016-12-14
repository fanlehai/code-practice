package sort.secondary;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReducerSort extends Reducer<KeyComparator, Text, KeyComparator, Text> {

	public void reduce(KeyComparator _key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		
		StringBuilder stringBuilder = new StringBuilder();
		for (Text text : values) {
			stringBuilder.append(text.toString());
			stringBuilder.append(',');
		}
		context.write(_key, new Text(stringBuilder.toString()));
	}

}
