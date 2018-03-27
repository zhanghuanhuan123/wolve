package com.wolves.admin.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;


public class SexyLockUtil<K> {
    private static Logger logger = LoggerFactory.getLogger(SexyLockUtil.class);

    private SexyLocker<K> sexyLocker =new SexyLocker<K>();

    public KeyLocker<K> getKeyLocker(K key) {
        return new KeyLocker<K>(sexyLocker, key);
    }

    public static class KeyLocker<K> {
        private UUID uuid = UUID.randomUUID();
        private SexyLocker sexyLocker;
        private K key;

        public KeyLocker(SexyLocker sexyLocker, K key) {
            this.sexyLocker = sexyLocker;
            this.key = key;
        }


        public boolean lockWithRetry(int expireTime, int retryTimes) {
            Random random = new Random();
            for (int i = 0; i < retryTimes; i++) {
                if (this.lock()) {
                    return true;
                } else {
                    try {
                        Thread.sleep(random.nextInt(50));
                    } catch (InterruptedException e) {

                    }
                }
            }
            return false;
        }

        public boolean lock() {
           try {
               this.sexyLocker.lock(key);
               return true;
           } catch (Exception e) {
               return false;
           }

        }

        public boolean unlock() {
            try {
                this.sexyLocker.unlock(key);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}