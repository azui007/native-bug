package com.demo.test.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DstLock implements AutoCloseable {

    @Getter
    private final Object lock;

    @Getter
    private final DistributeLock locker;

    @Override
    public void close() throws Exception {

    }
}

