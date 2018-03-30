package com.wq.multi;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.wq.multi.MultipleThreadDownload.APP_PATH;
import static org.apache.commons.io.FileUtils.deleteDirectory;


/**
 * @author zenghui
 */
public class DownloadRunnable implements Runnable {
    private CountDownLatch countDownLatch;
    private String number;
    private String github;

    public DownloadRunnable(CountDownLatch countDownLatch, String number, String github) {
        this.countDownLatch = countDownLatch;
        this.number = number;
        this.github = github;
    }

    @Override
    public void run() {
        this.download(this.number, this.github);
        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(10));
        } catch (Exception e) {

        }
        this.countDownLatch.countDown();
    }

    private void download(String number, String github) {
        Runtime rt = Runtime.getRuntime();
        String subDir = number + "_" + github.substring(19, github.indexOf("/", 19));
        String command = "git clone " + github;
        try {
            File file = new File(APP_PATH + "/" + subDir);
            if (file.exists()) {
                try {
                    deleteDirectory(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            file.mkdirs();
            Process proc = rt.exec(command, null, new File(APP_PATH + "/" + subDir));
            System.out.println(proc.waitFor());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        }
    }

}
