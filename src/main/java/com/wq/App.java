package com.wq;

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
    static Map<String, String> students = new HashMap<>();
    static List<String> studentApp = new ArrayList<>();

    public static void main(String[] args) {

        createDir();
        getStudentInfo();
        batchCreatDir();
        downloadApp();
        execute();
    }

    private static void execute() {
        String path = "";
        Runtime rt = Runtime.getRuntime();
        String command = "";
        for (String app : studentApp) {
            path = app + "/" + "WordCount" + "/BIN/";
            command = path + "wc.exe " + "-c ../../../../testCase/file1.c";
            try {
                System.out.println(command);
                rt.exec(command, null, new File(path));

            } catch (IOException e) {
                e.printStackTrace();
                continue;

            }
        }
    }

    public static void createDir() {

        try {
            new File(ROOT_PATH).mkdirs();
            new File(APP_PATH).mkdirs();
            new File(ANSWER_PATH).mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void batchCreatDir() {
        for (Map.Entry<String, String> entry : students.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            String subDir = entry.getKey() + "_" + entry.getValue().substring(19);
            new File(APP_PATH + "/" + subDir).mkdirs();
            studentApp.add(APP_PATH + "/" + subDir);
        }
    }

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

    // TODO 待优化 批量下载github项目时需要登录验证
    public static void downloadApp() {
        for (Map.Entry<String, String> entry : students.entrySet()) {
            String subDir = entry.getKey() + "_" + entry.getValue().substring(19);

            Runtime rt = Runtime.getRuntime();
            String command = "git clone " + entry.getValue() + "/WordCount.git";
            try {
                Process proc = rt.exec(command, null, new File(APP_PATH + "/" + subDir));
                System.out.println(proc.waitFor());
            } catch (IOException e) {

                e.printStackTrace();
                continue;
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

        }

    }
}
