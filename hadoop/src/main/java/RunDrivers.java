
//import org.apache.hadoop.util.ProgramDriver;
import utils.*;


public class RunDrivers {

	public static void main(String argv[]){
	int exitCode = -1;
    ProgramDriver pgd = new ProgramDriver();
    try {
    System.out.println(argv[0]+"test1");
      pgd.addClass("minmaxcount", minmaxcount.MinMaxCountDriver.class, 
                   "A map/reduce program that counts the commerts in the input files.");
      pgd.addClass("randomsample", sample.RandomSampleDriver.class, 
              "用于获取一个大文件子集的MapReduce程序。");
      System.out.println(argv[0]+"test2");  
      exitCode = pgd.run(argv);
      System.out.println(argv[0]+"test3");
    }
    catch(Throwable e){
      e.printStackTrace();
    }
    
    System.exit(exitCode);
	}
    
}
