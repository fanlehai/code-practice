package sort;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import utils.MRDPUtils;

public class LastAccessDateMap extends Mapper<Object, Text, Text, Text> {

	private Text outkey = new Text();

	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

		// Parse the input string into a nice map
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString());

		String date = parsed.get("LastAccessDate");
		if (date != null) {
			outkey.set(date);
			context.write(outkey, value);
		}
	}
}
