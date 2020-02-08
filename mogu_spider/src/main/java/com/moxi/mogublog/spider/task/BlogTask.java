package com.moxi.mogublog.spider.task;

import com.moxi.mogublog.spider.pipeline.BlogPipeline;
import com.moxi.mogublog.spider.processer.BlogProcesser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * 定时任务类
 */
//@Component
public class BlogTask {

    @Autowired
    private BlogProcesser blogProcesser;

    @Autowired
    private BlogPipeline blogPipeline;


    /**
     * 爬取文章: 爬取数据库分类
     */
    //@Scheduled(cron = "0/20 * * * * ?")
    //initialDelay 任务启动后多久后执行
    //fixedDelay 多久执行一次
    @Scheduled(initialDelay = 1000, fixedDelay = 100 * 1000)
    public void webArticleTask() {
        //开启蜘蛛爬取内容
        Spider.create(blogProcesser)
                .addUrl("https://www.csdn.net/")
                .addPipeline(blogPipeline)
                .setScheduler(new QueueScheduler())
                .thread(10)
                .run();
    }

}
