package counter;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import utils.*;

public class CreateCounterMap extends Mapper<LongWritable, Text, Text, NullWritable> {

	public static final String COUNT_LOCATION_STRING = "CountLocationString";

	enum Count {
	    HasLocation,
	    NullOrEmpty
	  }

	private boolean bOut = false;
	
	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {
		if (!bOut) {
			System.out.println(ivalue);	
			bOut = true;
		}
		
		// 解析xml输入数据的每一个标签
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(ivalue.toString());

		// 获取Location字段值
		String location = parsed.get("Location");

		if (location != null && !location.isEmpty()) {
			// 枚举型计数器
			context.getCounter(Count.HasLocation).increment(1);
			// 字符串计数器
			context.getCounter(COUNT_LOCATION_STRING, "Location").increment(1);;
			context.write(new Text(location), NullWritable.get());
		} else {
			// 枚举型计数器
			context.getCounter(Count.NullOrEmpty).increment(1);
			// 字符串计数器
			context.getCounter(COUNT_LOCATION_STRING, "NullOrEmpty").increment(1);
		}
		
	}

}
