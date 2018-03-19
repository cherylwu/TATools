package com.wq.studentinfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App implements PageProcessor {

    static List<String> records = new ArrayList<>();
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public static void main(String[] args) throws IOException {
        // String url = "http://www.cnblogs.com/fish-li/archive/2012/07/17/ClownFish.html";
        String url = "http://www.cnblogs.com/cherylwu/p/8495419.html";
        // String filePath = "C:\\Users\\zhuiz\\Desktop\\助教-吴倩-武剑洁老师\\学生名单ToWuQian.xls";


        Document document = Jsoup.connect(url).get();
        String html = document.html();
        // System.out.println(html);
        String blogApp = html.substring(html.indexOf("currentBlogApp = '") + 18, html.indexOf("', cb_enable_mathjax")).trim();
        String postid = html.substring(html.indexOf("cb_entryId=") + 11, html.indexOf(",cb_blogApp"));

        Long ts = new Date().getTime();


        Spider.create(new App()).addUrl("http://www.cnblogs.com/mvc/blog/GetComments.aspx?postid=" + postid + "&blogApp=" + blogApp + "&pageIndex=1&anchorCommentId=0&_=" + ts,
                "http://www.cnblogs.com/mvc/blog/GetComments.aspx?postid=" + postid + "&blogApp=" + blogApp + "&pageIndex=2&anchorCommentId=0&_=" + ts,
                "http://www.cnblogs.com/mvc/blog/GetComments.aspx?postid=" + postid + "&blogApp=" + blogApp + "&pageIndex=3&anchorCommentId=0&_=" + ts,
                "http://www.cnblogs.com/mvc/blog/GetComments.aspx?postid=" + postid + "&blogApp=" + blogApp + "&pageIndex=4&anchorCommentId=0&_=" + ts).thread(5).run();

        // ArrayList<ArrayList<Object>> students =  getStudents(filePath);
        int flag = 0;
        // 处理record TODO
        for (String record : records) {
            System.out.println(record);
            String[] student;
            //String studentMsg ;
            /*student = record.split(" ");
            String studentGit = student[0];
            for(ArrayList<Object> item : students ){
                String num = item.get(2).toString();
                if(num.endsWith(student[1])){
                     //System.out.println(student[1] + ", "  + item.get(3).toString() + ", " + student[2]);
                     System.out.println(studentGit + ", " + student[1] + ", "  + item.get(3).toString());
                     flag ++;
                     continue;
                }

            }*/

        }
        // System.out.println(flag);
    }

    @Override
    public void process(Page page) {

        Document document = Jsoup.parse(page.getJson().jsonPath("$.commentsHtml").get());
        Elements elements = document.select(".blog_comment_body");
        int i = 0;
        for (Element element : elements) {
            String record = element.text();
            records.add(record);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
