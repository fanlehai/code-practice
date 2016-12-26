package com.fanlehai.hadoop.counter;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.mapreduce.Cluster;

/*
 * 1.先运行CreateCounterDriver运行一个job
 * 2.根据JobID（job正在运行或者已经结束都可以）读取计算器的值
 * yarn jar count.jar counter.ReadCountFromJob job_1481352453042_0004
 */



public class ReadCountFromJob extends Configured implements Tool {
	
	  public int run(String[] args) throws Exception {
	    if (args.length != 1) {
	    	System.err.println("Usage: ReadCountFromJob <JobId>");
	      return -1;
	    }
	    
	    String jobID = args[0];

	    Cluster cluster = new Cluster(getConf());
	    Job job = cluster.getJob(JobID.forName(jobID));
	    if (job == null) {
	      System.err.printf("No job with ID %s found.\n", jobID);
	      return -1;
	    }
	    if (!job.isComplete()) {
	      System.err.printf("Job %s is not complete.\n", jobID);
	      return -1;
	    }
	    
	    for (Counter counter : job.getCounters().getGroup(CreateCounterMap.COUNT_LOCATION_STRING)) {
			System.out.println(counter.getDisplayName() + "\t"+ counter.getValue());
		}
	    
	    return 0;
	  }
	  
	  public static void main(String[] args) throws Exception {
	    int exitCode = ToolRunner.run(new ReadCountFromJob(), args);
	    System.exit(exitCode);
	  }
}
