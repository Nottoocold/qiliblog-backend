package com.zqqiliyc.framework.common.utils;

import cn.hutool.core.thread.ThreadUtil;
import com.zqqiliyc.framework.common.work.GenIdWorker;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
  * @author qili
  * @date 2025-07-23
  */
public class SnowFlakeUtilsTest {


    @Test
    public void test0() throws InterruptedException, ExecutionException {
        // 100个线程，每个线程生成10000个id
        int threads = 100;
        int qtyPerThread = 10000;

        CountDownLatch latch = ThreadUtil.newCountDownLatch(threads);
        List<Future<Set<Long>>> futures = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Future<Set<Long>> setFuture = ThreadUtil.execAsync(new GenIdWorker(latch, qtyPerThread));
            futures.add(setFuture);
        }

        latch.await();

        long size = 0;
        for (Future<Set<Long>> future : futures) {
            Set<Long> ids = future.get();
            size += ids.size();
        }

        Assert.assertEquals(threads * qtyPerThread, size);
    }
}