package com.zqqiliyc.framework.common.work;

import com.zqqiliyc.framework.common.utils.SnowFlakeUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @author qili
 * @date 2025-07-23
 */
public class GenIdWorker implements Callable<Set<Long>> {
    private final CountDownLatch countDownLatch;
    private final int generateIdCount;

    public GenIdWorker(CountDownLatch countDownLatch, int generateIdCount) {
        this.countDownLatch = countDownLatch;
        this.generateIdCount = generateIdCount;
    }

    @Override
    public Set<Long> call() throws Exception {
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < generateIdCount; i++) {
            ids.add(SnowFlakeUtils.genId());
        }
        countDownLatch.countDown();
        return ids;
    }
}
