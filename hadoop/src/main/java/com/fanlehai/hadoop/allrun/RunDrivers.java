package com.fanlehai.hadoop.allrun;

//import org.apache.hadoop.util.ProgramDriver;
import com.fanlehai.hadoop.utils.*;


public class RunDrivers {

	public static void main(String argv[]){
	int exitCode = -1;
    ProgramDriver pgd = new ProgramDriver();
    try {
      pgd.addClass("mmc", com.fanlehai.hadoop.minmaxcount.MinMaxCountDriver.class, 
                   "用户获取数据集中最大值、最小值和计数的MapReduce程序。");
      pgd.addClass("rs", com.fanlehai.hadoop.sample.RandomSampleDriver.class, 
              "用于获取一个大文件子集的MapReduce程序。");
      pgd.addClass("tos", com.fanlehai.hadoop.sort.TotalOrderSort.class, 
              "根据key对文件进行全排序（使用多个reduce）的MapReduce程序。");
      pgd.addClass("toss", com.fanlehai.hadoop.sort.TotalOrderSortSlow.class, 
              "根据key对文件进行全排序(只使用一个reduce)的MapReduce程序。");
      exitCode = pgd.run(argv);
    }
    catch(Throwable e){
      e.printStackTrace();
    }
    
    System.exit(exitCode);
	}
    
}
