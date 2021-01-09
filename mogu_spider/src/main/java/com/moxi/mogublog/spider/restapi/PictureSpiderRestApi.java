package com.moxi.mogublog.spider.restapi;

import com.moxi.mogublog.spider.entity.PictureSpider;
import com.moxi.mogublog.spider.pipeline.PicturePieline;
import com.moxi.mogublog.spider.processer.PictureProcesser;
import com.moxi.mogublog.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 图片爬取RestApi
 *
 * @author 陌溪
 * @date 2021年1月8日19:53:37
 */
@RestController
@RequestMapping("/spider")
@Api(value = "图片RestApi", tags = {"图片爬取相关接口"})
@Slf4j
public class PictureSpiderRestApi {

    @Autowired
    private PicturePieline picturePieline;
    @Autowired
    private PictureProcesser pictureProcesser;

    @ApiOperation(value = "通过爬虫爬取关键词图片", notes = "通过爬虫爬取关键词图片")
    @PostMapping("/spiderPicture")
    public String spiderPicture(@RequestBody PictureSpider pictureSpider) throws UnsupportedEncodingException {
        String searchUrl;
        if (pictureSpider.getCurrentPage() > 1) {
            searchUrl = "https://foter.com/search/instant/?q=" + pictureSpider.getKeyword() + "&page=" + pictureSpider.getCurrentPage();
        } else {
            searchUrl = "https://foter.com/search/instant/?q=" + pictureSpider.getKeyword();
        }
        String html = pictureProcesser.getHtml(searchUrl);
        List<String> imageUrlList = pictureProcesser.getImageUrl(html);
        List<String> imageSrcList = pictureProcesser.getImageSrc(imageUrlList);
        return ResultUtil.successWithData(imageSrcList);
    }
}

