package com.wq.studentinfo;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class Test implements PageProcessor {

    public static final String URL_LIST = "^http://www\\.cnblogs\\.com/cherylwu/$";
    // public static final String URL_POST = "http://www\\.cnblogs\\.com/cherylwu/p/\\d+\\.html";
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public static void main(String[] args) {
        Spider.create(new Test()).addUrl("http://www.cnblogs.com/cherylwu/").thread(5).run();
    }

    @Override
    public void process(Page page) {


        //列表页
        if (page.getUrl().regex(URL_LIST).match()) {
            //Elements elements = page.getHtml().getDocument().getElementsByClass("postTitle");
            Elements elements = page.getHtml().getDocument().getElementsByClass("postTitle2");
            for (Element element : elements) {
                // System.out.println(element.text());
                String url = element.attr("href");
                page.addTargetRequest(url);
            }
        } else {
            System.out.println(page.getHtml().getDocument().getElementById("cnblogs_post_body"));
        }

    }

    @Override
    public Site getSite() {
        return site;
    }
}