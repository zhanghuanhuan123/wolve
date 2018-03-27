package com.wolves.admin.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 必须启动LeaderLatch: leaderLatch.start();
 * 一旦启动， LeaderLatch会和其它使用相同latch path的其它LeaderLatch交涉，然后随机的选择其中一个作为leader。 
 * 你可以随时查看一个给定的实例是否是leader:
 */
public class Example1
{
    static final String CONNECT_ADDR = "192.168.174.10:2181,192.168.174.11:2182,192.168.174.12:2183";
	private static final String PATH = "/leader";
	private static final int COUNT = 5;
	public static void main(String[] args) throws Exception
	{
		CuratorFramework client = CuratorFrameworkFactory.newClient(CONNECT_ADDR, new ExponentialBackoffRetry(1000, 3));
        client.getConnectionStateListenable().addListener(new ConnectionStateListener(){
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
            }
        });
		client.start();

        List<LeaderLatch> list = new ArrayList<LeaderLatch>();

		for (int i = 1; i <= COUNT; i++)
		{
			LeaderLatch example = new LeaderLatch(client, PATH, "Client #" + i);
			example.start();
			list.add(example);
		}

		TimeUnit.SECONDS.sleep(20);

		LeaderLatch leader = null;
		for (LeaderLatch ll : list)
		{
			if (ll.hasLeadership())
			{
				leader = ll;
			}
			System.out.println(ll.getId() + "\t" + ll.hasLeadership());
		}
		;

		TimeUnit.SECONDS.sleep(2);
		
		list.remove(leader);
		//leader.close();
		
		TimeUnit.SECONDS.sleep(2);

		System.out.println("========================");
		
		for (LeaderLatch ll : list)
		{
			System.out.println(ll.getId() + "\t" + ll.hasLeadership());
		}

		for (LeaderLatch ll : list)
		{
			ll.close();
		}

		client.close();
	}

}
