package com.wolves.admin.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

public class SexyLocker<K> {
    private static Logger logger = LoggerFactory.getLogger(SexyLockUtil.class);

    private final ConcurrentMap<K, Semaphore> map = new ConcurrentHashMap<K, Semaphore>();
    private final ThreadLocal<Map<K, LockInfo>> local = new ThreadLocal<Map<K, LockInfo>>() {
        @Override
        protected Map<K, LockInfo> initialValue() {
            return new HashMap<K, LockInfo>();
        }
    };

    public void lock(K key) {
        if (key == null)
            return;
        LockInfo info = local.get().get(key);
        if (info == null) {
            Semaphore current = new Semaphore(1);
            current.acquireUninterruptibly();
            Semaphore previous = map.put(key, current);
            if (previous != null)
                previous.acquireUninterruptibly();
            local.get().put(key, new LockInfo(current));
        } else {
            info.lockCount++;
        }
    }

    public void unlock(K key) {
        if (key == null)
            return;
        LockInfo info = local.get().get(key);
        if (info != null && --info.lockCount == 0) {
            info.current.release();
            map.remove(key, info.current);
            local.get().remove(key);
        }
    }

    public void lock(K[] keys) {
        if (keys == null)
            return;
        for (K key : keys) {
            lock(key);
        }
    }

    public void unlock(K[] keys) {
        if (keys == null)
            return;
        for (K key : keys) {
            unlock(key);
        }
    }

    private static class LockInfo {
        private final Semaphore current;
        private int lockCount;

        private LockInfo(Semaphore current) {
            this.current = current;
            this.lockCount = 1;
        }
    }
}