package com.wq.multi.download;

import com.wq.judge.FileUtil;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.collect.Maps.newHashMap;


/**
 * @author zenghui
 */
public class MultipleThreadDownload {

    static String STUDENT_INFO = "C:/test/students/studentInfo.txt";
    static String APP_PATH = "C:/test/apps";
    static String TEST_CASE = "C:/test/testCase/test.txt";
    static String RESULT_PATH = "C:/test/result";
    static String RESULT_FILE_NAME = "result.txt";

    static String ROOT_PATH = "C:/test";
    static String ANSWER_PATH = "C:/test/answer";
    static String RUNNER_APP = "\\BIN\\wc.exe";

    public static void main(String[] args) {
        createDir();

        ExecutorService executor = Executors.newCachedThreadPool();

        // 获取学生Github项目
        Map<String, String> students = getStudentInfo();
        int size = students.size();
        if (size <= 0) {
            System.out.println("学生信息为空！！！！");
            return;
        }
        CountDownLatch countDown = new CountDownLatch(size);

        BossRunnable boss = new BossRunnable(countDown);

        for (Map.Entry<String, String> student : students.entrySet()) {
            String number = student.getKey();
            String github = student.getValue();
            String directory = APP_PATH + "/" + number + "_" + github.substring(19, github.indexOf("/", 19));

            executor.execute(new ExecuteRunnable(countDown, directory, github));
        }
        executor.execute(boss);
        executor.shutdown();
    }

    public static void createDir() {
        File appPath = new File(APP_PATH);
        File resPath = new File(RESULT_PATH);
        try {
            new File(ROOT_PATH).mkdirs();
            if (appPath.exists()) {
                FileUtils.deleteDirectory(appPath);
            }
            if (resPath.exists()) {
                FileUtils.deleteDirectory(resPath);
            }
            new File(APP_PATH).mkdirs();
            new File(ANSWER_PATH).mkdirs();
            new File(RESULT_PATH).mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }




    public static Map<String, String> getStudentInfo() {
        Map<String, String> students = newHashMap();
        File studentInfo = new File(STUDENT_INFO);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(studentInfo));
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = br.readLine();
            while (line != null) {
                String[] info = line.split(" ");
                students.put(info[0], info[1]);
                line = br.readLine();
            }
            return students;
        } catch (IOException e) {
            e.printStackTrace();
            return newHashMap();
        }
    }

    public static List<String> getTestCommands() {
        File testCase = new File(TEST_CASE);
        List<String> testCommand = FileUtil.getContentByline(testCase);
        return testCommand;
    }



}
