package com.demo.test.lock;

import java.util.concurrent.TimeUnit;

public interface DistributeLock {


    DstLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) ;


    DstLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws Exception;


    void unlock(DstLock lock) ;

}
