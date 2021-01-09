package com.moxi.mogublog.spider.processer;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * 爬取的博客页面处理
 *
 * @author 陌溪
 * @date 2021年1月8日16:47:34
 */
@Component
public class BlogProcesser implements PageProcessor {

    /**
     * 处理我们需要的页面
     */
    @Override
    public void process(Page page) {
        List<String> list = page.getHtml().regex("https://blog.csdn.net/[a-zA-Z0-9_]+/article/details/[0-9]{9}").all();
        this.saveBlogInfo(page);
        page.addTargetRequests(list);

	/*	if(list==null || list.size()==0){
			// 如果为空 表示这是图片详情页
			this.saveBlogInfo(page);
		}else {
			// 如果不为空 表示这是列表页 解析出详情页的url地址 放到任务队列中
			*//*for (Selectable selectable : list) {
				//获取url地址
			String details = 	selectable.links().toString();
			page.addTargetRequest(details);
			}*//*

			for (String details : list) {
				//获取url地址
				page.addTargetRequest(details);
			}
		}*/

    }

    private void saveBlogInfo(Page page) {

        //2、获取我们需要的内容： title和content
        String title = page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1/text()").toString();
        String content = page.getHtml().xpath("//*[@id=\"article_content\"]").toString();


        if (title != null) {
            page.putField("title", title);
            page.putField("content", content);
        } else {
            //跳过爬取
            page.setSkip(true);
        }

    }

    //站点信息设置
    @Override
    public Site getSite() {
        return Site.me().setCharset("utf8").setRetryTimes(2).setSleepTime(2000).setTimeOut(4000);
    }

}
