package com.wq.judge;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuqian
 */
public class FileUtil {
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 查找并获取指定目录下的所有一级文件夹
     */
    public static List<File> findSubDirs(File file) {
        List<File> fileList = new ArrayList<>();
        if (file == null || !file.exists()) {
            return fileList;
        } else {
            String[] names = file.list();

            for (String name : names) {
                File subFile = new File(file.getAbsolutePath(), name);
                if (subFile.isDirectory()) {
                    fileList.add(subFile);
                }
            }
            return fileList;
        }
    }

    public static List<File> findSubFiles(File file) {
        List<File> fileList = new ArrayList<>();
        if (file == null || !file.exists()) {
            return fileList;
        } else {
            String[] names = file.list();

            for (String name : names) {
                File subFile = new File(file.getAbsolutePath(), name);
                if (!subFile.isDirectory()) {
                    fileList.add(subFile);
                }
            }
            return fileList;
        }
    }

    /**
     * 获取文件内容，逐行获取
     *
     * @param file
     */
    public static List<String> getContentByline(File file) {
        List<String> content = new ArrayList<>();
        if (file == null || !file.exists()) {
            return content;
        } else if (file.getName().endsWith(".txt")) {

            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null) {
                    if (StringUtils.isNotEmpty(line)) {
                        content.add(line.trim());
                    }

                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        } else {
            System.out.println("暂不支持的文件类型v");
            return content;
        }
    }

    public static void main(String[] args) {
        // System.out.println(getContentByline(new File("C:\\test\\testCase\\testcase.txt")));
        /*File file = new File("C:\\test\\testCase\\testcase.txt");
        file.renameTo(new File("C:\\test\\testCase\\test.txt"));*/
        System.out.println(App.redirectToResultPath("C:\\test\\apps\\17071_handsomesnail\\WordCount\\BIN"));
        // System.out.println("C:\\test\\testCase\\test.txt");
        //File file = new File("C:\\test\\testCase\\test.txt");
        //file.renameTo(new File("C:\\test\\result\\17071_handsomesnail\\_result.txt"));
    }
}
