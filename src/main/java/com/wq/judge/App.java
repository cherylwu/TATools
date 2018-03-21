package com.wq.judge;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App {

    static String ROOT_PATH = "C:/test";
    static String APP_PATH = "C:/test/apps";
    static String ANSWER_PATH = "C:/test/answer";
    static String STUDENT_INFO = "C:/students/studentInfo.txt";
    static String TEST_CASE = "C:/test/testcase/command.txt";

    static Map<String, String> students = new HashMap<>();
    static List<String> studentApp = new ArrayList<>();
    //static List<String> testCommand = new ArrayList<>();

    public static void main(String[] args) {

        createDir();
        getStudentInfo();
        batchCreatDir();
        downloadApp();
        execute();
        //checkAnswer();
    }

    /**
     * 对比执行答案和预期标准答案
     */
    private static void checkAnswer() {

    }

    /**
     * 运行exe，测试测试用例wc.exe -c -l -w ../../../../testCase/file1.c
     */
    private static void execute() {
        String path = "";
        String projectName = "";
        int startChar;
        int endChar;
        Runtime rt = Runtime.getRuntime();
        String command = "";

        for (String app : studentApp) {
            //获取学生项目名
            startChar = app.indexOf("/",19);
            endChar = app.lastIndexOf(".git");
            projectName = app.substring(startChar, endChar);
            if (app.indexOf("handsomesnail") > -1) {
                // TODO 获取同学项目目录下的BIN目录路径
                path = app + "/" + projectName + "/BIN/";
                //path = app + "/" + "WordCount" + "/BIN/";
                // TODO 读取txt文件中的测试用例
                //command = path + "wc.exe " + "-c -l -w ../../../../testCase/file1.c";

                File testCase = new File(TEST_CASE);
                try {
                    InputStreamReader reader = new InputStreamReader(new FileInputStream(testCase));
                    BufferedReader br = new BufferedReader(reader);
                    String line = br.readLine();
                    while (line != null){

                        //testCommand.add(line);
                        command = path + line;
                        System.out.println(command);
                        rt.exec(command, null, new File(path));
                        line = br.readLine();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //https://github.com/chaseMengdi/Software-Quality-Test.git
            }

        }
    }

    public static void readCommand() {
        /*File testCase = new File(TEST_CASE);
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(testCase));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null){
                testCommand.add(line);
                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    /**
     * 创建基础文件夹
     */
    public static void createDir() {
        File appPath = new File(APP_PATH);
        try {
            new File(ROOT_PATH).mkdirs();
            if (appPath.exists()) {
                FileUtil.deleteDir(appPath);
            }
            new File(APP_PATH).mkdirs();
            new File(ANSWER_PATH).mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 批量创建存放学生作业的文件夹
     */
    public static void batchCreatDir() {
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
            studentApp.add(APP_PATH + "/" + subDir);
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
                    System.out.println(command);
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
