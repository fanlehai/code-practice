package com.fanlehai.hadoop.sort.secondary;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class FirstPartitioner extends Partitioner<KeyComparator, Text> {

	public int getPartition(KeyComparator key, Text value, int numPartitions) {

		return Math.abs(key.getYearMonth().hashCode() % numPartitions);
	};
}
