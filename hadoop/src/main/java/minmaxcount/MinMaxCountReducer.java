package minmaxcount;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import io.*;


public class MinMaxCountReducer extends Reducer<Text, MinMaxCountTuple, Text, MinMaxCountTuple> {

	private MinMaxCountTuple result = new MinMaxCountTuple();

	public void reduce(Text _key, Iterable<MinMaxCountTuple> values, Context context)
			throws IOException, InterruptedException {
		// Initialize our result
		result.setMin(null);
		result.setMax(null);
		int sum = 0;

		// Iterate through all input values for this key
		for (MinMaxCountTuple val : values) {

			// If the value's min is less than the result's min
			// Set the result's min to value's
			if (result.getMin() == null || val.getMin().compareTo(result.getMin()) < 0) {
				result.setMin(val.getMin());
			}

			// If the value's max is less than the result's max
			// Set the result's max to value's
			if (result.getMax() == null || val.getMax().compareTo(result.getMax()) > 0) {
				result.setMax(val.getMax());
			}

			// Add to our sum the count for val
			sum += val.getCount();
		}

		// Set our count to the number of input values
		result.setCount(sum);

		context.write(_key, result);
	}

}
