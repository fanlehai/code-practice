package sort;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ValueReduce extends Reducer<Text, Text, Text, NullWritable> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		for (Text t : values) {
			context.write(t, NullWritable.get());
		}
	}
}
