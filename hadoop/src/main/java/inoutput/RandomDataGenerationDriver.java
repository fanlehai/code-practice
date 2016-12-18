package inoutput;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * 用mapreduce自己产生随机数据
 * 
 * 1. 需要一个继承与InputSplit的类，啥事也不做，只是告诉hadoop用我们创建的FakeInputSplit来分片；
 * 2. 需要一个继承与InputFormat的类，告诉hadoop的输入数据类型，分片等操作（RandomStackOverflowInputFormat）；
 * 3. 需要一个继承与RecordReader的类，`实际产生数据的类（RandomStackoverflowRecordReader）
 * 
 * RandomDataGenerationDriver <num map tasks> <num records per task> <word list> <output>
 * 
 * yarn jar gen.jar inoutput.RandomDataGenerationDriver 10 1 sequence/f.txt gen
 * 
 * 
 */

public class RandomDataGenerationDriver extends Configured implements Tool {

	public static class RandomStackOverflowInputFormat extends InputFormat<Text, NullWritable> {

		public static final String NUM_MAP_TASKS = "random.generator.map.tasks";
		public static final String NUM_RECORDS_PER_TASK = "random.generator.num.records.per.map.task";
		public static final String RANDOM_WORD_LIST = "random.generator.random.word.file";

		@Override
		public List<InputSplit> getSplits(JobContext job) throws IOException {

			// Get the number of map tasks configured for
			int numSplits = job.getConfiguration().getInt(NUM_MAP_TASKS, -1);
			if (numSplits <= 0) {
				throw new IOException(NUM_MAP_TASKS + " is not set.");
			}

			// Create a number of input splits equivalent to the number of tasks
			ArrayList<InputSplit> splits = new ArrayList<InputSplit>();
			for (int i = 0; i < numSplits; ++i) {
				splits.add(new FakeInputSplit());
			}

			return splits;
		}

		@Override
		public RecordReader<Text, NullWritable> createRecordReader(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			// Create a new RandomStackoverflowRecordReader and initialize it
			RandomStackoverflowRecordReader rr = new RandomStackoverflowRecordReader();
			rr.initialize(split, context);
			return rr;
		}

		public static void setNumMapTasks(Job job, int i) {
			job.getConfiguration().setInt(NUM_MAP_TASKS, i);
		}

		public static void setNumRecordPerTask(Job job, int i) {
			job.getConfiguration().setInt(NUM_RECORDS_PER_TASK, i);
		}

		public static void setRandomWordList(Job job, Path file) {
			// DistributedCache.addCacheFile(file.toUri(),
			// job.getConfiguration());
			try {
				job.addCacheFile(new URI(file.toString()));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public static class RandomStackoverflowRecordReader extends RecordReader<Text, NullWritable> {

			private int numRecordsToCreate = 0;
			private int createdRecords = 0;
			private Text key = new Text();
			private NullWritable value = NullWritable.get();
			private Random rndm = new Random();
			private ArrayList<String> randomWords = new ArrayList<String>();

			// This object will format the creation date string into a Date
			// object
			private SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

			@Override
			public void initialize(InputSplit split, TaskAttemptContext context)
					throws IOException, InterruptedException {

				// Get the number of records to create from the configuration
				this.numRecordsToCreate = context.getConfiguration().getInt(NUM_RECORDS_PER_TASK, -1);

				if (numRecordsToCreate < 0) {
					throw new InvalidParameterException(NUM_RECORDS_PER_TASK + " is not set.");
				}

				// Get the list of random words from the DistributedCache
				URI[] files = context.getCacheFiles();

				if (files.length == 0) {
					throw new InvalidParameterException("Random word list not set in cache.");
				} else {
					// Read the list of random words into a list
					String string = files[0].toString();
					if (string.lastIndexOf('/') != -1) {
						string = string.substring(string.lastIndexOf('/')+1, string.length());
					}
					BufferedReader rdr = new BufferedReader(new FileReader(string));

					String line;
					while ((line = rdr.readLine()) != null) {
						randomWords.add(line);
					}
					rdr.close();

					if (randomWords.size() == 0) {
						throw new IOException("Random word list is empty");
					}
				}
			}

			@Override
			public boolean nextKeyValue() throws IOException, InterruptedException {
				// If we still have records to create
				if (createdRecords < numRecordsToCreate) {
					// Generate random data
					int score = Math.abs(rndm.nextInt()) % 1500;
					int rowId = Math.abs(rndm.nextInt()) % 1000;
					int postId = Math.abs(rndm.nextInt()) % 1000;
					int userId = Math.abs(rndm.nextInt()) % 1000;
					String creationDate = frmt.format(Math.abs(rndm.nextLong()));

					// Create a string of text from the random words
					String text = getRandomText();

					String randomRecord = "<row Id=\"" + rowId + "\" PostId=\"" + postId + "\" Score=\"" + score
							+ "\" Text=\"" + text + "\" CreationDate=\"" + creationDate + "\" UserId\"=" + userId
							+ "\" />";

					key.set(randomRecord);
					++createdRecords;
					return true;
				} else {
					// Else, return false
					return false;
				}
			}

			/**
			 * Creates a random string of words from the list. 1-30 words per
			 * string.
			 * 
			 * @return A random string of words
			 */
			private String getRandomText() {
				StringBuilder bldr = new StringBuilder();
				int numWords = Math.abs(rndm.nextInt()) % 30 + 1;

				for (int i = 0; i < numWords; ++i) {
					bldr.append(randomWords.get(Math.abs(rndm.nextInt()) % randomWords.size()) + " ");
				}
				return bldr.toString();
			}

			@Override
			public Text getCurrentKey() throws IOException, InterruptedException {
				return key;
			}

			@Override
			public NullWritable getCurrentValue() throws IOException, InterruptedException {
				return value;
			}

			@Override
			public float getProgress() throws IOException, InterruptedException {
				return (float) createdRecords / (float) numRecordsToCreate;
			}

			@Override
			public void close() throws IOException {
				// nothing to do here...
			}
		}

		/**
		 * This class is very empty.
		 */
		public static class FakeInputSplit extends InputSplit implements Writable {

			public void readFields(DataInput arg0) throws IOException {
			}

			public void write(DataOutput arg0) throws IOException {
			}

			@Override
			public long getLength() throws IOException, InterruptedException {
				return 0;
			}

			@Override
			public String[] getLocations() throws IOException, InterruptedException {
				return new String[0];
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		int res = ToolRunner.run(conf, new RandomDataGenerationDriver(), args);
		if (res != 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("RandomDataGenerationDriver is done !");
		}
		System.exit(res);
	}

	public int run(String[] args) throws Exception {

		if (args.length != 4) {
			System.err.println(
					"Usage: RandomDataGenerationDriver <num map tasks> <num records per task> <word list> <output>");
			System.exit(1);
		}

		int numMapTasks = Integer.parseInt(args[0]);
		int numRecordsPerTask = Integer.parseInt(args[1]);
		Path wordList = new Path(args[2]);
		Path outputDir = new Path(args[3]);
		
		FileSystem.get(getConf()).delete(new Path(args[3].toString()), true);

		Job job = Job.getInstance(getConf(), "RandomDataGenerationDriver");
		job.setJarByClass(RandomDataGenerationDriver.class);

		job.setNumReduceTasks(0);

		job.setInputFormatClass(RandomStackOverflowInputFormat.class);

		RandomStackOverflowInputFormat.setNumMapTasks(job, numMapTasks);
		RandomStackOverflowInputFormat.setNumRecordPerTask(job, numRecordsPerTask);
		RandomStackOverflowInputFormat.setRandomWordList(job, wordList);

		TextOutputFormat.setOutputPath(job, outputDir);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		return job.waitForCompletion(true) ? 0 : 2;
	}
}
