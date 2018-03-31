package com.wq.multi.download;

import com.wq.judge.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.wq.judge.FileUtil.prepareEmptyDirectory;
import static com.wq.multi.download.MultipleThreadDownload.*;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * @author zenghui
 */
public class ExecuteRunnable implements Runnable {
    private CountDownLatch countDownLatch;
    private String directory;
    private String github;

    public ExecuteRunnable(CountDownLatch countDownLatch, String directory, String github) {
        this.countDownLatch = countDownLatch;
        this.directory = directory;
        this.github = github;
    }

    @Override
    public void run() {
        this.execute(this.directory, this.github);
        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(10));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.countDownLatch.countDown();
    }

    private void execute(String dir, String github) {
        Runtime rt = Runtime.getRuntime();
        String command = "git clone " + github;
        try {
            prepareEmptyDirectory(dir);
            long begin = System.currentTimeMillis();
            Process proc = rt.exec(command, null, new File(dir));
            proc.waitFor();
            long end = System.currentTimeMillis();
            System.out.println(command + "  -->(cost time:" + ((end - begin)) + "ms)");


            // 执行学生项目，可执行文件存放在项目目录的BIN目录下,名字为wc.exe
            List<String> testCommands = getTestCommands();
            if (isEmpty(testCommands)) {
                System.out.println("没有找到测试脚本！！！！");
                return;
            }

            List<File> repositories = FileUtil.findSubDirs(new File(dir));
            if (repositories.isEmpty()) {
                System.out.println("没有找到同学的项目信息！！！！-->" + dir);
                return;
            }
            String exe = repositories.get(0) + RUNNER_APP;
            File file = new File(exe);
            if (!file.exists()) {
                System.out.println("无法获取可执行文件-->" + exe);
            }
            for (String cmd : testCommands) {
                doTest(exe, cmd);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        }
    }


}
