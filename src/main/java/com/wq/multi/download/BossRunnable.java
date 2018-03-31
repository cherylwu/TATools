package com.wq.multi.download;

import java.util.concurrent.CountDownLatch;

/**
 * @author zenghui
 */
public class BossRunnable implements Runnable {

    private CountDownLatch countDownLatch;

    public BossRunnable(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            this.countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("execute finished!");
    }
}