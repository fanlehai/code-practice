package configure;

import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * 打印出所有的配置信息
 */

public class PrintConfig extends Configured implements Tool {

	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		for (Entry<String, String> entry : conf) {
			System.out.printf("%s=%s\n", entry.getKey(), entry.getValue());
		}
		return 0;
	}

	public static void main(String[] args) throws Exception {
		
		Properties props = System.getProperties();
		 Enumeration<?> e = props.propertyNames();  
         while (e.hasMoreElements()) {  
             String key = (String) e.nextElement();  
             String value = props.getProperty(key);  
             System.out.println("Key:" + key + ",Value:" + value);  
         }  
		
		
		int exitCode = ToolRunner.run(new PrintConfig(), args);
		System.exit(exitCode);
	}

}
