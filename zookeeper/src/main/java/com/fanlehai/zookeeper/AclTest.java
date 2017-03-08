package com.fanlehai.zookeeper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class AclTest {

	ZooKeeper zk = null;
	CountDownLatch countDown = new CountDownLatch(1);
	private volatile boolean bConnect = false;

	private class DefaultWatcher implements Watcher {

		@Override
		public void process(WatchedEvent event) {
			// TODO Auto-generated method stub
			if (event.getType() == Event.EventType.None && event.getState() == KeeperState.SyncConnected) {
				countDown.countDown();
			}
		}

	}

	@Before
	public void testBefore() {

		try {
			String string = DigestAuthenticationProvider.generateDigest("admin:admin123");
			String string2 = string;
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			zk = new ZooKeeper("node11", 2000, new DefaultWatcher());
			countDown.await();
			bConnect = true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateAcl() throws NoSuchAlgorithmException, KeeperException, InterruptedException {

		if (bConnect) {
			List<ACL> acls = new ArrayList<ACL>(2);

			Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin123"));
			ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);

			Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest("guest:guest123"));
			ACL acl2 = new ACL(ZooDefs.Perms.READ, id2);

			acls.add(acl1);
			acls.add(acl2);

			String string = "just for test";

			// zk.create("/test", new byte[0], acls, CreateMode.PERSISTENT);
			zk.create("/testq", string.getBytes(), acls, CreateMode.PERSISTENT);
		}

	}

	@Test
	public void testReadAcl() {
		if (bConnect) {
			zk.addAuthInfo("digest", "guest:guest123".getBytes());
			try {
				Stat stat = new Stat();
				byte[] value = zk.getData("/testq", null, stat);
				String string = new String(value);
				System.out.println(string);
				System.out.println(stat);
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
