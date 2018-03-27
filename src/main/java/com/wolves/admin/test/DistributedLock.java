package com.wolves.admin.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.RevocationListener;
import org.apache.curator.framework.recipes.locks.Revoker;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

import java.util.Map;
import java.util.WeakHashMap;

public class DistributedLock{
    private InterProcessMutex lock;//重入的,排他的.
    private Map<Thread,Boolean> lockedThread = new WeakHashMap<Thread, Boolean>();
    private String lockPath;  
    private ConnectionStateListener stateListener = new StateListener();
    private RevocationListener<InterProcessMutex> revocationListener;
  
    public DistributedLock(CuratorFramework client, String path){
        lockPath = path;  
        revocationListener = new RevocationListener<InterProcessMutex>() {  
            @Override  
            public void revocationRequested(InterProcessMutex forLock) {  
                if(!forLock.isAcquiredInThisProcess()){  
                    return;  
                }  
                try{  
                    forLock.release();  
                }catch(Exception e){  
                    e.printStackTrace();  
                }  
            }  
        };  
        lock = createLock(client);  
        lock.makeRevocable(revocationListener);  
        client.getConnectionStateListenable().addListener(stateListener);  
    }  
  
    public boolean lock(){  
        try{  
            lock.acquire();  
            lockedThread.put(Thread.currentThread(),Boolean.TRUE);  
        } catch (Exception e){  
            //  
        }  
        return false;  
    }  
  
    public void unlock(){  
        try{  
            lock.release();  
        }catch (Exception e){  
            //  
        }  
    }  
  
    private InterProcessMutex createLock(CuratorFramework client){  
        lock = new InterProcessMutex(client,lockPath);  
        //协同中断,如果其他线程/进程需要此锁中断时,调用此listener.  
        lock.makeRevocable(revocationListener);
        client.getConnectionStateListenable().addListener(stateListener);  
        return lock;  
    }  
  
    class StateListener implements ConnectionStateListener{
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            if(Boolean.FALSE.equals(lockedThread.get(Thread.currentThread()))){  
                return;//如果当前lock没有获取锁,则忽略
            }  
            switch (newState){  
                case LOST:  
                    //一旦丢失链接,就意味着zk server端已经删除了锁数据
                    lockedThread.clear();  
                    lock = createLock(client);//must be rebuild  
                    break;  
                default:  
                    System.out.println(newState.toString());  
            }  
        }  
    }  
}  