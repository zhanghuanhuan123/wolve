package com.wolves.admin.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 与LeaderLatch相比， 通过LeaderSelectorListener可以对领导权进行控制， 在适当的时候释放领导权，这样每个节点都有可能获得领导权。 
 * 而LeaderLatch一根筋到死， 除非调用close方法，否则它不会释放领导权。
 */
public class Example2
{
	private static final String PATH = "/leader";
	private static final int COUNT = 5;
    static final String CONNECT_ADDR = "192.168.174.10:2181,192.168.174.11:2182,192.168.174.12:2183";
	public static void main(String[] args) throws Exception
	{
		CuratorFramework client = CuratorFrameworkFactory.newClient(CONNECT_ADDR,
                new ExponentialBackoffRetry(1000, 3));
		client.start();
		
		List<MyLeaderSelectorListenerAdapter> list = new ArrayList<MyLeaderSelectorListenerAdapter>();
		
		for (int i = 1; i <= COUNT; i++)
		{
			MyLeaderSelectorListenerAdapter example = new MyLeaderSelectorListenerAdapter(client, PATH, "Client #" + i);
			list.add(example);
		}
		
		TimeUnit.SECONDS.sleep(20);
		
		for (MyLeaderSelectorListenerAdapter listener : list)
		{
			listener.close();
		}
		
		client.close();
	}
}
class MyLeaderSelectorListenerAdapter extends LeaderSelectorListenerAdapter
{
	private final String name;
    private final LeaderSelector leaderSelector;
    
    public MyLeaderSelectorListenerAdapter(CuratorFramework client, String path, String name)
    {
    	this.name = name;
    	leaderSelector = new LeaderSelector(client, path, this);
    	
    	//保证在此实例释放领导权之后还可能获得领导权。
    	leaderSelector.autoRequeue();
    	
    	leaderSelector.start();
    }
    
    public void close()
    {
    	leaderSelector.close();
    }
    
    /**
     * 你可以在takeLeadership进行任务的分配等等，并且不要返回，如果你想要要此实例一直是leader的话可以加一个死循环。
     * 一旦此方法执行完毕之后，就会重新选举
     */
	public void takeLeadership(CuratorFramework client) throws Exception
	{
		System.out.println(name + " 当选为leader");
		TimeUnit.SECONDS.sleep(2);
	}
}