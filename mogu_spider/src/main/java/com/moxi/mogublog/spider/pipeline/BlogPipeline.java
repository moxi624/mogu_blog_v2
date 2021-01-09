package com.moxi.mogublog.spider.pipeline;

import com.moxi.mogublog.spider.entity.BlogSpider;
import com.moxi.mogublog.spider.mapper.BlogSpiderMapper;
import com.moxi.mogublog.spider.util.IdWorker;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 博客传输管道
 *
 * @author 陌溪
 * @date 2021年1月8日16:40:07
 */
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
                blog.setUid(idWorker.nextId() + "");
                blog.setTitle(title);
                blog.setSummary(title);
                blog.setContent(content);
                blog.setTagUid("5c4c541e600ff422ccb371ee788f59d6");
                blog.setClickCount(0);
                blog.setCollectCount(0);
                blog.setStatus(EStatus.ENABLE);
                blog.setAdminUid("1f01cd1d2f474743b241d74008b12333");
                blog.setAuthor("陌溪");
                blog.setArticlesPart("蘑菇博客");
                blog.setBlogSortUid("6a1c7a50c0e7b8e8657949bf02d5d0ca");
                blog.setLevel(0);
                blog.setIsPublish(EPublish.PUBLISH);
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
