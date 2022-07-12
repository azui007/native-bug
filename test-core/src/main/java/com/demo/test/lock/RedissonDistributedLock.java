package com.demo.test.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.util.concurrent.TimeUnit;


@ConditionalOnClass(RedissonClient.class)
public class RedissonDistributedLock implements DistributeLock {

    private static final String LOCK_KEY_PREFIX = "LOCK_KEY";

    @Autowired
    private RedissonClient redisson;

    public DstLock getLock(String key, boolean isFair) {
        RLock lock;
        if(isFair) {
            lock = redisson.getFairLock(LOCK_KEY_PREFIX + ":" + key);
        } else {
            lock = redisson.getLock(LOCK_KEY_PREFIX + ":" + key);
        }
        return new DstLock(lock,this);
    }

    @Override
    public DstLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair)  {
        DstLock dstLock = getLock(key,isFair);
        RLock lock = (RLock) dstLock.getLock();
        lock.lock(leaseTime,unit);
        return dstLock;
    }

    @Override
    public DstLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws Exception {
        DstLock dstLock = getLock(key,isFair);
        RLock lock = (RLock) dstLock.getLock();
        if(lock.tryLock(waitTime,leaseTime,unit)){
            return dstLock;
        }
        return null;
    }

    @Override
    public void unlock(DstLock lock) {
        if(lock != null) {
            if(lock instanceof RLock) {
                RLock rLock = (RLock) lock;
                if(rLock.isLocked()) {
                    rLock.unlock();
                }
            }
        }
    }
}
