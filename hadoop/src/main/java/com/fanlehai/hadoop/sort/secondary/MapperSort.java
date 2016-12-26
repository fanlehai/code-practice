package com.fanlehai.hadoop.sort.secondary;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MapperSort extends Mapper<LongWritable, Text, KeyComparator, Text> {

	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {

		String string = ivalue.toString();
		String[] strSplit = string.split(",");
		String strKey = "";
		String strValue = "";
		if (strSplit.length >= 3) {
			strKey = strSplit[0] + "-" + strSplit[1];
			strValue = strSplit[2];
		}

		KeyComparator data = new KeyComparator();
		data.setYearMonth(strKey);
		data.setCount(Integer.parseInt(strValue));
		context.write(data, new Text(strValue));

	}

}
