package com.demo.reptile.test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Element;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 测试爬取https://blog.csdn.net/wozniakzhang/article/list/1?
 * 这是一个简单示例,可以作为入门,解析复杂的以及反爬虫的稍微复杂一丢丢,会用到正则表达式,以及httpclient哪些,都差不多,也不是很复杂
 *
 * @author 捡矿泉水瓶瓶的张大祥
 */
@Slf4j
public class CSDN {
    /**
     * 单纯返回CSDN访问数量
     *
     * @return
     */
    public static Integer accessNumber() {
        Document doc;
        Integer number = null;
        try {
            // 爬取的网站
            doc = Jsoup.connect("https://blog.csdn.net/wozniakzhang/article/list/1?").get();
            // HTML格式转文本格式(就是不带标签,只获取内容)
            String text = doc.text();
            // 这是获取到所有div里面的h2标签的元素集合,不同网站这里解析方式不一样而已
            Elements elements = doc.select("div.grade-box.clearfix > dl:nth-child(2) > dd");
            // 这是获取到所有div里面的h2标签的元素集合,不同网站这里解析方式不一样而已
            Elements elements2 = doc.select("#mainBox > main > div.article-list div");
            for (int i = 1; i < elements2.size(); i++) {
                //System.out.println(elements2.get(i).select("h4 > a").attr("href"));// 获取遍历的地址
            }

            // String attr = elements.attr("title");
            Document doc1 = Jsoup.connect("https://blog.csdn.net/wozniakzhang").get();
            //#asideProfile > div.data-info.d-flex.item-tiling > dl:nth-child(5)
            String attr =  doc1.select("#asideProfile > div.data-info.d-flex.item-tiling > dl:nth-child(5)").attr("title");
            if (attr == null || attr == "") {
                //没有就乱写一个
                log.error("暂时被封了.....");
                return 0;
            }
            //log.info("title  " + attr);
            number = Integer.valueOf(attr);
           // log.info("number  " + number);
            // System.out.println("\nCSDN访问量: " + number);// 获取元素属性的值
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

    /**
     * 定时爬取CSDN访的问量
     *
     * @return CSDN访的问量
     */
    public static Runnable RunnableDemo() {
        // 单位: 毫秒
        final long timeInterval = 8000;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Integer accessNumber = CSDN.accessNumber();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //System.out.println(sdf.format(new Date()) + "   CSDN访问量:  " + accessNumber);
                    try {
                        Thread.sleep(timeInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return runnable;
    }

    /**
     * 获取CSDN需要爬取的博客链接
     *
     * @param pageNum 博客页码
     * @return
     */
    public static List getUrList(Integer pageNum) {
        Document doc;
        List<String> urList = new ArrayList<String>();
        try {
            // 爬取的网站
            doc = Jsoup.connect("https://blog.csdn.net/wozniakzhang/article/list/" + pageNum + "?").get();
            // HTML格式转文本格式(就是不带标签,只获取内容)
            String text = doc.text();
            // 这是获取到所有div里面的h2标签的元素集合,不同网站这里解析方式不一样而已
            Elements elements2 = doc.select("#mainBox > main > div.article-list div");
            //System.out.println("第"+pageNum+"页博客数量:" + (((elements2.size()-1)/2)-1));
            for (int i = 2; i < elements2.size() - 1; i += 2) {
                //System.out.println("最初地址:  "+i+"  "+elements2.get(i).select("h4 > a").attr("href"));
                // 获取遍历的地址
                urList.add(elements2.get(i).select("h4 > a").attr("href"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回数量
        return urList;
    }


}
