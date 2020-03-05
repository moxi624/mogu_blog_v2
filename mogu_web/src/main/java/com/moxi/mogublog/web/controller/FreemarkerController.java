package com.moxi.mogublog.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.web.feign.PictureFeignClient;
import com.moxi.mogublog.web.global.SQLConf;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.web.util.WebUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.entity.WebConfig;
import com.moxi.mogublog.xo.service.*;
import com.moxi.mougblog.base.enums.ELevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * FreemarkController
 *
 * @author: 陌溪
 * @create: 2020-03-04-11:23
 */
@RequestMapping("freemarker")
@Controller
public class FreemarkerController {

    @Autowired
    WebUtils webUtils;

    @Autowired
    WebConfigService webConfigService;

    @Autowired
    BlogService blogService;

    @Autowired
    TagService tagService;

    @Autowired
    BlogSortService blogSortService;

    @Autowired
    LinkService linkService;

    @Autowired
    private PictureFeignClient pictureFeignClient;

    @RequestMapping("/info/{uid}")
    public String index(Map<String,Object> map, @PathVariable("uid") String uid) {
        // fc98d2ae7756d2587390ae441b82f52d
        List<Blog> sameBlog = blogService.getSameBlogByBlogUid(uid);
        sameBlog  = setBlog(sameBlog);

        List<Blog> thirdBlog = blogService.getBlogListByLevel(ELevel.THIRD);
        thirdBlog  = setBlog(thirdBlog);

        List<Blog> fourthBlog = blogService.getBlogListByLevel(ELevel.FOURTH);
        fourthBlog  = setBlog(fourthBlog);


        map.put("webConfig", webConfigService.getWebConfig());
        map.put("blog", blogService.getBlogByUid(uid));
        map.put("sameBlog", sameBlog);
        return "info";
    }

    /**
     * 设置博客的分类标签和内容
     *
     * @param list
     * @return
     */
    private List<Blog> setBlog(List<Blog> list) {
        final StringBuffer fileUids = new StringBuffer();
        List<String> sortUids = new ArrayList<>();
        List<String> tagUids = new ArrayList<>();

        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + SysConf.FILE_SEGMENTATION);
            }
            if (StringUtils.isNotEmpty(item.getBlogSortUid())) {
                sortUids.add(item.getBlogSortUid());
            }
            if (StringUtils.isNotEmpty(item.getTagUid())) {
                tagUids.add(item.getTagUid());
            }
        });
        String pictureList = null;

        if (fileUids != null) {
            pictureList = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtils.getPictureMap(pictureList);
        Collection<BlogSort> sortList = new ArrayList<>();
        Collection<Tag> tagList = new ArrayList<>();
        if (sortUids.size() > 0) {
            sortList = blogSortService.listByIds(sortUids);
        }
        if (tagUids.size() > 0) {
            tagList = tagService.listByIds(tagUids);
        }


        Map<String, BlogSort> sortMap = new HashMap<>();
        Map<String, Tag> tagMap = new HashMap<>();
        Map<String, String> pictureMap = new HashMap<>();

        sortList.forEach(item -> {
            sortMap.put(item.getUid(), item);
        });

        tagList.forEach(item -> {
            tagMap.put(item.getUid(), item);
        });

        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });


        for (Blog item : list) {

            //设置分类
            if (StringUtils.isNotEmpty(item.getBlogSortUid())) {
                item.setBlogSort(sortMap.get(item.getBlogSortUid()));
            }

            //获取标签
            if (StringUtils.isNotEmpty(item.getTagUid())) {
                List<String> tagUidsTemp = StringUtils.changeStringToString(item.getTagUid(), SysConf.FILE_SEGMENTATION);
                List<Tag> tagListTemp = new ArrayList<Tag>();

                tagUidsTemp.forEach(tag -> {
                    tagListTemp.add(tagMap.get(tag));
                });
                item.setTagList(tagListTemp);
            }

            //获取图片
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getFileUid(), SysConf.FILE_SEGMENTATION);
                List<String> pictureListTemp = new ArrayList<>();

                pictureUidsTemp.forEach(picture -> {
                    pictureListTemp.add(pictureMap.get(picture));
                });
                item.setPhotoList(pictureListTemp);
            }
        }
        return list;
    }
}
