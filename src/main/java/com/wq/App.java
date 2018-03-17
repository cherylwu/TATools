package com.wq;

import java.io.*;
import java.util.HashMap;
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

    public static void main(String[] args) {

        createDir();
        getStudentInfo();
        batchCreatDir();
        // downloadApp();
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
                rt.exec(command, null, new File(APP_PATH + "/" + subDir));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
