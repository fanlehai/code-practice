package xml.mapreduce;

import java.io.IOException;

/*
 * 
 * 代码来源：https://github.com/ildac/xmlInputFormat.git
 * 
 */

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLInputFormat extends TextInputFormat {

	private static final Logger log = LoggerFactory.getLogger(XMLInputFormat.class);

	public static final String START_TAG_KEY = "xmlinput.start";
	public static final String END_TAG_KEY = "xmlinput.end";

	// - describe the type of information for map input keys and values

	// - specify how the input data should be partitioned
	// in this case it is the same as the TextInputFormat

	// - indicate the record instance that should read the data from source
	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext context) {

		try {
			return new XMLRecordReader();
		} catch (IOException ioe) {
			log.warn("Error while creating XmlRecordReader", ioe);
			return null;
		}
	}

}
