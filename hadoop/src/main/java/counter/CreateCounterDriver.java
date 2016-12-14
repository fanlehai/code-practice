package counter;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * 【问题】：获取用户的Location，计算多少用户填写了Location字段
 * 
 *  运行参数：
 * yarn jar count.jar counter.CreateCounterDriver stackover-users-sample/part-r-00000 countLocation
 * 【测试数据】：StackOver的Users.xml，格式如下：
 * <?xml version="1.0" encoding="utf-8"?>
	<users>
	  <row Id="3615528" Reputation="1" CreationDate="2014-05-08T08:29:59.600" DisplayName="Mike" LastAccessDate="2016-07-25T18:57:54.603" WebsiteUrl="http://stolmet-zywiec.pl/" Location="Poland" AboutMe="" Views="1" UpVotes="0" DownVotes="0" ProfileImageUrl="https://www.gravatar.com/avatar/?s=128&d=identicon&r=PG&f=1" AccountId="4440990" />
	  <row Id="3615103" Reputation="15" CreationDate="2014-05-08T06:28:57.240" DisplayName="Ferragam" LastAccessDate="2016-07-11T12:27:28.937" WebsiteUrl="" AboutMe="" Views="0" UpVotes="1" DownVotes="0" ProfileImageUrl="https://www.gravatar.com/avatar/9b1e2803df438055a11f0035e3780cc1?s=128&d=identicon&r=PG&f=1" AccountId="4440465" />
	  <row Id="3614148" Reputation="1" CreationDate="2014-05-07T22:27:22.250" DisplayName="ocornejo77" LastAccessDate="2016-04-22T14:53:43.063" WebsiteUrl="" Location="Milán, Italia" AboutMe="<p>I'm a Computer Science Ph.D. student at Milano-Bicocca.</p>" Views="0" UpVotes="0" DownVotes="0" ProfileImageUrl="https://i.stack.imgur.com/objxt.jpg" AccountId="4439156" />
	  <row Id="3614019" Reputation="1" CreationDate="2014-05-07T21:30:17.430" DisplayName="user3614019" LastAccessDate="2016-04-19T16:25:53.750" Views="0" UpVotes="0" DownVotes="0" ProfileImageUrl="https://www.gravatar.com/avatar/?s=128&d=identicon&r=PG&f=1" AccountId="4438967" />
	  <row Id="3613414" Reputation="3" CreationDate="2014-05-07T18:08:15.627" DisplayName="Lestat" LastAccessDate="2016-09-02T21:14:59.063" WebsiteUrl="" AboutMe="" Views="1" UpVotes="0" DownVotes="0" ProfileImageUrl="https://www.gravatar.com/avatar/?s=128&d=identicon&r=PG&f=1" AccountId="4438152" />
	  ...
	  ...
	</users>
 * 
 * 【输出结果】：
 * 字符串计数器
 * CountLocationString
		Location=826
		NullOrEmpty=5159		
 * 枚举计数器，名字是枚举类型的全类路径
	counter.CreateCounterMap$Count
		HasLocation=826
		NullOrEmpty=5159
 * 
 * 1.自定义创建枚举型计数器
 * 	创建枚举类型，调用context.getCounter
 * 2.自定义创建字符串计数器
 * 	直接调用context.getCounter
 * 3.修改枚举型计数器的显示名称（默认枚举计数器显示枚举类的全类路径名称，不易读）
 * 创建一个名称为：counter.CreateCounterMap_Count.properties文件
 * 内容：
 * Count=CountLocationEnum
	HasLocation.name=HasLocation
	NullOrEmpty.name=NullOrEmpty
	
 * 修改后执行结果如下：
 * CountLocationEnum
		HasLocation=826
		NullOrEmpty=5159
 */

public class CreateCounterDriver extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		System.setProperty("hadoop.home.dir", "/Users/liuhai/lib/hadoop/hadoop-2.7.2");
		int res = ToolRunner.run(conf, new CreateCounterDriver(), args);
		if (res == 0) {
			System.err.println("something bad happened !");
		} else {
			System.out.println("CreateCounter is done !");
		}
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			printUsage();
		}

		FileSystem.get(new Configuration()).delete(new Path(args[1]), true);
		Job job = Job.getInstance(super.getConf(), "CreateCounter");

		job.setJarByClass(counter.CreateCounterDriver.class);
		
		job.setMapperClass(CreateCounterMap.class);
		
		job.setNumReduceTasks(0);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		return job.waitForCompletion(true) ? 1 : 0;
	}

	private void printUsage() {
		System.err.println("Usage: CreateCounter <in> <out>");
		ToolRunner.printGenericCommandUsage(System.err);
		System.exit(2);
	}

}
