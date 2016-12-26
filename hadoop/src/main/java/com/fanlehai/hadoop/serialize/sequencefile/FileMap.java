package com.fanlehai.hadoop.serialize.sequencefile;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FileMap extends Mapper<Text, Text, Text, Text> {

	private boolean bProperty = false;
	/**
	 * Called once at the beginning of the task.
	 */
	protected void setup(Context context) throws IOException, InterruptedException {
		
	}

	public void map(Text ikey, Text ivalue, Context context) throws IOException, InterruptedException {

		System.out.println("key = " + ikey + "  |  value = " + ivalue);
		context.write(ikey, ivalue);
		
		if (!bProperty) {
			Properties props = System.getProperties();
			Enumeration<?> e = props.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = props.getProperty(key);
				System.out.println("Key:" + key + ",Value:" + value);
			}
			bProperty = true;
		}
		
	}

}
