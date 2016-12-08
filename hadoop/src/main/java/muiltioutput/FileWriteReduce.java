package muiltioutput;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class FileWriteReduce extends Reducer<Text, Text, Text, Text> {

	private MultipleOutputs<Text, Text> multipleOutputs;

	protected void setup(Context context) throws IOException, InterruptedException {
		multipleOutputs = new MultipleOutputs<Text, Text>(context);
	}

	public void reduce(Text _key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		for (Text val : values) {
			multipleOutputs.write(_key, val, _key.toString());
		}
	}

	protected void cleanup(Context context) throws IOException, InterruptedException {
		multipleOutputs.close();
	}

}
