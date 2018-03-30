package com.wq.multi;

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
        }
        System.out.println("download finished!");
    }
}