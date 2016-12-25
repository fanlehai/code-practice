package serialize.xml;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.*;

import javax.xml.stream.*;
import java.io.*;

import static javax.xml.stream.XMLStreamConstants.*;

public final class HadoopPropertyXMLMapReduce extends Configured implements Tool {
	private static final Logger log = LoggerFactory.getLogger(HadoopPropertyXMLMapReduce.class);

	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected void map(LongWritable key, Text value, Mapper.Context context)
				throws IOException, InterruptedException {
			String document = value.toString();
			System.out.println("'" + document + "'");
			try {
				XMLStreamReader reader = XMLInputFactory.newInstance()
						.createXMLStreamReader(new ByteArrayInputStream(document.getBytes()));
				String propertyName = "";
				String propertyValue = "";
				String currentElement = "";
				while (reader.hasNext()) {
					int code = reader.next();
					switch (code) {
					case START_ELEMENT:
						currentElement = reader.getLocalName();
						break;
					case CHARACTERS:
						if (currentElement.equalsIgnoreCase("name")) {
							propertyName += reader.getText();
						} else if (currentElement.equalsIgnoreCase("value")) {
							propertyValue += reader.getText();
						}
						break;
					}
				}
				reader.close();
				context.write(propertyName.trim(), propertyValue.trim());
			} catch (Exception e) {
				log.error("Error processing '" + document + "'", e);
			}
		}
	}

	public static void main(String... args) throws Exception {
		Configuration conf = new Configuration();

		int res = ToolRunner.run(conf, new HadoopPropertyXMLMapReduce(), args);
		if (res != 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("HadoopPropertyXMLMapReduce is done !");
		}
		System.exit(res);
	}

	@Override
	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			System.err.println("Usage: HadoopPropertyXMLMapReduce <in> <out>");
			ToolRunner.printGenericCommandUsage(System.err);
			System.exit(2);
		}

		String input = args[0];
		String output = args[1];

		getConf().set("key.value.separator.in.input.line", " ");
		getConf().set("xmlinput.start", "<property>");
		getConf().set("xmlinput.end", "</property>");

		Job job = Job.getInstance(getConf(), "HadoopPropertyXMLMapReduce");
		job.setJarByClass(HadoopPropertyXMLMapReduce.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(Map.class);
		job.setInputFormatClass(XmlInputFormat.class);
		job.setNumReduceTasks(0);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(input));
		Path outPath = new Path(output);
		FileOutputFormat.setOutputPath(job, outPath);
		outPath.getFileSystem(conf).delete(outPath, true);

		job.waitForCompletion(true);
	}
}
