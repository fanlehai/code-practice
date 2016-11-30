package sample;

import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RandomSampleMap extends Mapper<LongWritable, Text, NullWritable, Text> {
	
	private float filterPercentage;
    private Random rands = new Random();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
      filterPercentage = context.getConfiguration().getFloat(RandomSampleDriver.FILTER_PERCENTAGE_KEY, 0.0f);
    }

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
      if (rands.nextFloat() < filterPercentage) {
        context.write(NullWritable.get(), value);
      }
    }

}
