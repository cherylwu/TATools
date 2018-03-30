package com.wq.judge;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author wuqian
 */
public class App {

    static String ROOT_PATH = "C:/test";
    static String APP_PATH = "C:/test/apps";
    static String ANSWER_PATH = "C:/test/answer";
    static String RESULT_PATH = "C:/test/result";
    static String STUDENT_INFO = "C:/students/studentInfo.txt";
    static String TEST_CASE = "C:/test/testCase/test.txt";

    static Map<String, String> students = new HashMap<>();

    static List<String> resultPath = new ArrayList<>();

    public static void main(String[] args) {

        createDir();
        getStudentInfo();
        batchCreatDir();
        /*downloadApp();
        execute();


        checkAnswers();*/
    }

    private static void checkAnswers() {
        for (String path : resultPath) {
            List<File> results = FileUtil.findSubFiles(new File(path));
            try {
                new File(path, "score.txt").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (File result : results) {
                if (result.getName().indexOf("result") > 0) {
                    System.out.println(result.getName().split("_")[0] + "_answer.txt");
                    if (checkAnswer(result, new File(ANSWER_PATH, result.getName().split("_")[0] + "_answer.txt"))) {
                        try {
                            Files.write(Paths.get(new File(path, "score.txt").getPath()), (result.getName() + ": correct!").getBytes(), StandardOpenOption.APPEND);
                            Files.write(Paths.get(new File(path, "score.txt").getPath()), System.getProperty("line.separator").getBytes(), StandardOpenOption.APPEND);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Files.write(Paths.get(new File(path, "score.txt").getPath()), (result.getName() + ": wrong!\n").getBytes(), StandardOpenOption.APPEND);
                            Files.write(Paths.get(new File(path, "score.txt").getPath()), System.getProperty("line.separator").getBytes(), StandardOpenOption.APPEND);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
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

    /**
     * 运行exe，测试测试用例wc.exe -c -l -w ../../../../testCase/file1.c
     */
    private static void execute() {
        List<String> studentApp = new ArrayList<>();
        String path = "";
        for (String app : studentApp) {
            File file = new File(app);
            List<File> subDirs = FileUtil.findSubDirs(file);
            if (!subDirs.isEmpty()) {
                path = subDirs.get(0) + "\\BIN\\";
                doTest(path, TEST_CASE);
            }
        }
    }

    private static void doTest(String path, String testCaePath) {
        System.out.println(testCaePath);
        File testCase = new File(testCaePath);
        List<String> testCommand = FileUtil.getContentByline(testCase);
        int flag = 0;
        for (String command : testCommand) {
            flag++;
            Runtime rt = Runtime.getRuntime();
            try {
                System.out.println(path + command);
                Process proc = rt.exec(path + command, null, new File(path));
                proc.waitFor();


                //System.out.println(path + "result.txt");
                File result = new File(path, "result.txt");

                if (result.exists()) {
                    result.renameTo(new File(redirectToResultPath(path), flag + "_result.txt"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static String redirectToResultPath(String binPath) {
        if (StringUtils.isNotEmpty(binPath)) {
            return (RESULT_PATH + "/" + binPath.substring(13, binPath.indexOf("\\", 13))).replace("/", "\\");
        } else {
            return EMPTY;
        }
    }

    /**
     * 创建基础文件夹
     */
    public static void createDir() {
        File appPath = new File(APP_PATH);
        File resPath = new File(RESULT_PATH);
        try {
            new File(ROOT_PATH).mkdirs();
            if (appPath.exists()) {
                FileUtil.deleteDir(appPath);
            }
            if (resPath.exists()) {
                FileUtil.deleteDir(resPath);
            }
            new File(APP_PATH).mkdirs();
            new File(ANSWER_PATH).mkdirs();
            new File(RESULT_PATH).mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 批量创建存放学生作业的文件夹
     */
    public static void batchCreatDir() {
        List<String> studentApp = new ArrayList<>();
        for (Map.Entry<String, String> entry : students.entrySet()) {
            String url = entry.getValue();
            System.out.println("Key = " + entry.getKey() + ", Value = " + url);
            String subDir = "";
            if (url.endsWith(".git")) {
                subDir = entry.getKey() + "_" + url.substring(19, url.indexOf("/", 19));
            } else {
                subDir = entry.getKey() + "_" + url.substring(19);
            }

            new File(APP_PATH + "/" + subDir).mkdirs();
            new File(RESULT_PATH + "/" + subDir).mkdirs();
            System.out.println(APP_PATH + "/" + subDir);
            studentApp.add(APP_PATH + "/" + subDir);
            resultPath.add(RESULT_PATH + "/" + subDir);

        }
    }

    /**
     * 获取学生GitHub信息
     */
    public static void getStudentInfo() {
        students.clear();
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
            System.out.println(students);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 批量下载同学作业
     */
    public static void downloadApp() {
        for (Map.Entry<String, String> entry : students.entrySet()) {
            Runtime rt = Runtime.getRuntime();
            if (entry.getValue().endsWith(".git")) {
                String subDir = entry.getKey() + "_" + entry.getValue().substring(19, entry.getValue().indexOf("/", 19));
                String command = "git clone " + entry.getValue();
                try {
                    System.out.println(APP_PATH + "/" + subDir);
                    Process proc = rt.exec(command, null, new File(APP_PATH + "/" + subDir));
                    System.out.println(proc.waitFor());
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
            } else {
                continue;
            }
        }

    }
}
