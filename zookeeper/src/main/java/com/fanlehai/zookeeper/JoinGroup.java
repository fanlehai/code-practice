package com.fanlehai.zookeeper;
//cc JoinGroup A program that joins a group

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

/*
 * 命令行参数：node11 zoo child
 * 
 * java -classpath ./:/Users/liuhai/.m2/repository/org/apache/zookeeper/zookeeper/3.5.2-alpha/zookeeper-3.5.2-alpha.jar:/Users/liuhai/.m2/repository/commons-cli/commons-cli/1.2/commons-cli-1.2.jar:/Users/liuhai/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/Users/liuhai/.m2/repository/org/slf4j/slf4j-api/1.7.5/slf4j-api-1.7.5.jar:/Users/liuhai/.m2/repository/org/slf4j/slf4j-log4j12/1.7.5/slf4j-log4j12-1.7.5.jar:/Users/liuhai/.m2/repository/io/netty/netty/3.10.5.Final/netty-3.10.5.Final.jar:/Users/liuhai/.m2/repository/net/java/dev/javacc/javacc/5.0/javacc-5.0.jar com.fanlehai.zookeeper.JoinGroup node11 zoo chilc
 */

// vv JoinGroup
public class JoinGroup extends ConnectionWatcher {

	public void join(String groupName, String memberName) throws KeeperException, InterruptedException {
		String path = "/" + groupName + "/" + memberName;
		String createdPath = zk.create(path, null/* data */, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println("Created " + createdPath);
	}

	public static void main(String[] args) throws Exception {
		JoinGroup joinGroup = new JoinGroup();
		joinGroup.connect(args[0]);
		joinGroup.join(args[1], args[2]);
		Thread.sleep(Long.MAX_VALUE);
	}

}
// ^^ JoinGroup
