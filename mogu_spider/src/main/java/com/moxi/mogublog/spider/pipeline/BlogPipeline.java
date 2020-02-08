package com.moxi.mogublog.spider.pipeline;

import com.moxi.mogublog.spider.entity.BlogSpider;
import com.moxi.mogublog.spider.mapper.BlogSpiderMapper;
import com.moxi.mogublog.spider.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

@Component
public class BlogPipeline implements Pipeline {


    private final String SAVE_PATH = "C:/Users/King/Desktop/tensquare/webmgicFile/piantuUrl/youxi/";
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private BlogSpiderMapper blogDao;

    @Override
    public void process(ResultItems res, Task task) {
        //获取title和content
        String title = res.get("title");
        String content = res.get("content");
        System.out.println("title: " + title);
        System.out.println("content: " + content);
        if (!StringUtils.isEmpty(title) && !StringUtils.isEmpty(content)) {

            try {
                BlogSpider blog = new BlogSpider();
                blog.setUid(idWorker.nextId() + ""); //主键
                blog.setTitle(title);                   //标题
                blog.setSummary("爬取到的页面");
                blog.setContent(content);              //博客内容
                blog.setTagUid("1");
                blog.setClickCount(0); //点击数
                blog.setCollectCount(0); //收藏数
                blog.setStatus(1);     //状态1
                Date now = new Date();
                blog.setCreateTime(now);//创建时间
                blog.setUpdateTime(now);
                blog.setAdminUid("1f01cd1d2f474743b241d74008b12333");//管理员uid 写死
                blog.setAuthor("作者");
                blog.setArticlesPart("辉皇博客");
                blog.setBlogSortUid("1");
                blog.setLevel(1);
                blog.setIsPublish("1");
                blog.setSort(0);
                blog.insert();

                //下载到本地
                //DownloadUtil.download("http://pic.netbian.com"+fileUrl,fileName,SAVE_PATH);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
