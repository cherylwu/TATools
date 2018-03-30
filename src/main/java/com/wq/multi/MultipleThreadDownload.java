package com.wq.multi;

import java.io.*;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.collect.Maps.newHashMap;


/**
 * @author zenghui
 */
public class MultipleThreadDownload {

    static String STUDENT_INFO = "C:/students/studentInfo.txt";
    static String APP_PATH = "C:/test/apps";


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


    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        Map<String, String> students = getStudentInfo();
        int size = students.size();
        CountDownLatch countDown = new CountDownLatch(size);

        BossRunnable boss = new BossRunnable(countDown);
        for (Map.Entry<String, String> student : students.entrySet()) {
            executor.execute(new DownloadRunnable(countDown, student.getKey(), student.getValue()));
        }
        executor.execute(boss);
        executor.shutdown();

    }

    private static void buildFold(String number, String github) {
        String subDir = number + "_" + github.substring(19, github.indexOf("/", 19));
        File file = new File(APP_PATH + "/" + subDir);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();
    }
}
