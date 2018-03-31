package com.wq.multi.download;

import com.wq.judge.FileUtil;
import org.apache.commons.io.FileUtils;

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

    /**
     * 运行exe wc.exe -c -l -w ../../../../testCase/file1.c
     *
     * @param exe     可执行文件地址
     * @param command 格式为：序号 -c -l -w ../../../../testCase/file1.c , 例如：1 c -l -w ../../../../testCase/file1.c
     */
    public static void doTest(String exe, String command) {

        File executor = new File(exe);
        if (!executor.exists()) {
            System.out.println("无法获取可执行文件-->" + exe);
            return;
        }
        Runtime rt = Runtime.getRuntime();
        try {
            // 获取测试用例序号，用于重命名测试生成的结果文件
            String number = command.split(" ")[0];
            String fullCommand = exe + command.substring(command.indexOf(" "));

            System.out.println(fullCommand);
            Process proc = rt.exec(fullCommand, null, new File(executor.getParent()));
            proc.waitFor();

            File result = new File(executor.getParent(), RESULT_FILE_NAME);

            if (result.exists()) {
                result.renameTo(new File(executor.getParent(), number + "_" + RESULT_FILE_NAME));
            } else {
                System.out.println("未能获取到结果文件，command->" + fullCommand);
                new File(executor.getParent(), number + "_" + RESULT_FILE_NAME).createNewFile();
                FileUtils.writeStringToFile(new File(executor.getParent(), number + "_" + RESULT_FILE_NAME), "未能获取到结果文件，command->" + fullCommand);
            }
            File answer = new File(ANSWER_PATH, number + "_" + RESULT_FILE_NAME);

            File reNamedResult = new File(executor.getParent(), number + "_" + RESULT_FILE_NAME);
            record(checkAnswer(reNamedResult, answer), command, reNamedResult, answer);


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void record(boolean flag, String command, File result, File answer) {
        try {
            new File(result.getParent() + "\\score.txt").createNewFile();
            File score = new File(result.getParent() + "\\score.txt");
            StringBuilder sb = new StringBuilder();
            sb.append("command-->" + command).append("\n")
                    .append("Answer-->" + command).append("\n")
                    .append(FileUtils.readFileToString(answer)).append("\n")
                    .append("result-->").append("\n")
                    .append(FileUtils.readFileToString(result)).append("\n")
                    .append("result is" + flag).append("\n");
            FileUtils.writeStringToFile(score, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 对比执行答案和预期标准答案
     */
    private static boolean checkAnswer(File result, File answer) {
        try {
            return FileUtils.contentEquals(result, answer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\test\\answer\\1_result.txt");
        System.out.println(FileUtils.readFileToString(file));
    }

}
