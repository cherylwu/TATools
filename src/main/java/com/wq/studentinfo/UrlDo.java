package com.wq.studentinfo;

public class UrlDo {
    public static void main(String[] args) {
        String record = "https://github.com/lzwk 17079 http://www.cnblogs.com/MrZhang145689/";
        String[] student;
        student = record.split(" ");
        String studentMsg = student[1] + ", " + "张武科" + ", " + student[2];
        System.out.println(studentMsg);
    }
}
