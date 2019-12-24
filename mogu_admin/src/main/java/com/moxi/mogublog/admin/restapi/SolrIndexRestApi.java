package com.moxi.mogublog.admin.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.service.BlogSearchService;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSysConf;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 索引维护api
 *
 * @author limboy
 * @create 2018-09-29 16:25
 */
@RestController
@RequestMapping("/search")
@Api(value = "索引维护RestApi", tags = {"solrIndexRestApi"})
public class SolrIndexRestApi {

    private static Logger log = LogManager.getLogger(SolrIndexRestApi.class);
    @Autowired
    private BlogSearchService blogSearchService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private PictureFeignClient pictureFeignClient;
    @Value(value = "${spring.data.solr.core}")
    private String collection;

    @ApiOperation(value = "搜索Blog", notes = "搜索Blog")
    @GetMapping("/searchBlog")
    public String searchBlog(HttpServletRequest request,
                             @ApiParam(name = "keywords", value = "关键字", required = true) @RequestParam(required = true) String keywords,
                             @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                             @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        if (StringUtils.isNotEmpty(keywords) && StringUtils.isEmpty(keywords.trim())) {
            return ResultUtil.result(SysConf.SUCCESS, "内容不能为空");
        }
        Map<String, Object> map = blogSearchService.search(collection, keywords, currentPage, pageSize);
        return ResultUtil.result(SysConf.SUCCESS, map);

    }

    @OperationLogger(value = "初始化solr索引")
    @ApiOperation(value = "初始化solr索引", notes = "初始化solr索引")
    @PostMapping("/initIndex")
    public String initIndex(HttpServletRequest request) {

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<Blog>();

        queryWrapper.eq(BaseSysConf.STATUS, EStatus.ENABLE);

        List<Blog> blogList = blogService.list(queryWrapper);

        final StringBuffer fileUids = new StringBuffer();

        blogList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                fileUids.append(item.getFileUid() + ",");
            }
        });

        String pictureList = null;

        if (fileUids != null) {
            pictureList = this.pictureFeignClient.getPicture(fileUids.toString(), ",");
        }

        List<Map<String, Object>> picList = WebUtils.getPictureMap(pictureList);

        Map<String, String> pictureMap = new HashMap<String, String>();

        picList.forEach(item -> {
            pictureMap.put(item.get("uid").toString(), item.get("url").toString());
        });

        for (Blog item : blogList) {

            //获取图片
            if (StringUtils.isNotEmpty(item.getFileUid())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getFileUid(), ",");
                List<String> pictureListTemp = new ArrayList<String>();

                pictureUidsTemp.forEach(picture -> {
                    pictureListTemp.add(pictureMap.get(picture));
                });
                item.setPhotoList(pictureListTemp);
            }
        }
        blogSearchService.initIndex(collection, blogList);
        log.info("初始化索引成功");
        return ResultUtil.result(SysConf.SUCCESS, "初始化索引成功");

    }

    @OperationLogger(value = "添加solr索引")
    @ApiOperation(value = "添加solr索引", notes = "添加solr索引")
    @PostMapping("/addIndex")
    public String addIndex(HttpServletRequest request, @RequestBody Blog blog) {

        if (blog == null || StringUtils.isEmpty(blog.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "UID不能为空");
        }
        blogSearchService.addIndex(collection, blog);
        log.info("新建solr索引");
        return ResultUtil.result(SysConf.SUCCESS, "新建solr索引成功");
    }

    @OperationLogger(value = "更新solr索引")
    @ApiOperation(value = "更新solr索引", notes = "更新solr索引")
    @PostMapping("/updateIndex")
    public String updateIndex(HttpServletRequest request, @RequestBody Blog blog) {

        if (blog == null || StringUtils.isEmpty(blog.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "UID不能为空");
        }
        blogSearchService.updateIndex(collection, blog);

        log.info("更新solr索引");
        return ResultUtil.result(SysConf.SUCCESS, "更新solr索引成功");

    }

    @OperationLogger(value = "删除solr索引")
    @ApiOperation(value = "删除solr索引", notes = "删除solr索引")
    @PostMapping("/deleteIndex")
    public String deleteIndex(HttpServletRequest request,
                              @ApiParam(name = "uid", value = "博客uid", required = true) @RequestParam(name = "uid", required = true) String uid) {

        blogSearchService.deleteIndex(collection, uid);
        log.info("删除部分索引");
        return ResultUtil.result(SysConf.SUCCESS, "删除索引成功");

    }

    @OperationLogger(value = "删除全部solr索引")
    @ApiOperation(value = "删除全部solr索引", notes = "删除全部solr索引")
    @PostMapping("/deleteAllIndex")
    public String deleteAllIndex(HttpServletRequest request) {

        blogSearchService.deleteAllIndex(collection);
        log.info("删除所有索引");
        return ResultUtil.result(SysConf.SUCCESS, "删除所有索引成功");
    }
}