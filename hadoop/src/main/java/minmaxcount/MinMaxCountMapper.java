package minmaxcount;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import utils.*;
import java.util.Map;
import java.util.Date;

public class MinMaxCountMapper extends Mapper<LongWritable, Text, Text, MinMaxCountTuple> {

	// Our output key and value Writables
	private Text outUserId = new Text();
	private MinMaxCountTuple outTuple = new MinMaxCountTuple();

	// This object will format the creation date string into a Date object
	private final static SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Override
	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {
		// Parse the input string into a nice map
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(ivalue.toString());

		// Grab the "CreationDate" field since it is what we are finding
		// the min and max value of
		String strDate = parsed.get("CreationDate");

		// Grab the “UserID” since it is what we are grouping by
		String userId = parsed.get("UserId");

		// .get will return null if the key is not there
		if (strDate == null || userId == null) {
			// skip this record
			return;
		}

		try {
			// Parse the string into a Date object
			Date creationDate = frmt.parse(strDate);

			// Set the minimum and maximum date values to the creationDate
			outTuple.setMin(creationDate);
			outTuple.setMax(creationDate);

			// Set the comment count to 1
			outTuple.setCount(1);

			// Set our user ID as the output key
			outUserId.set(userId);

			// Write out the user ID with min max dates and count
			context.write(outUserId, outTuple);
		} catch (ParseException e) {
			// An error occurred parsing the creation Date string
			// skip this record
		}
	}

}
