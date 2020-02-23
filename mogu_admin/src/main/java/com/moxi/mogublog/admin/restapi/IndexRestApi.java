package com.moxi.mogublog.admin.restapi;

import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mogublog.xo.service.CommentService;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页RestApi
 *
 * @author xzx19950624@qq.com
 * @date 2018年10月22日下午3:27:24
 */
@RestController
@RequestMapping("/index")
@Api(value = "首页RestApi", tags = {"IndexRestApi"})
@Slf4j
public class IndexRestApi {

    @Autowired
    BlogService blogService;

    @Autowired
    CommentService commentService;

    @Autowired
    WebVisitService webVisitService;

    @Autowired
    UserService userService;

    @ApiOperation(value = "首页初始化数据", notes = "首页初始化数据", response = String.class)
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public String init() {
        Map<String, Object> map = new HashMap<>();

        Integer blogCount = blogService.getBlogCount(EStatus.ENABLE);
        Integer commentCount = commentService.getCommentCount(EStatus.ENABLE);
        Integer userCount = userService.getUserCount(EStatus.ENABLE);
        Integer visitCount = webVisitService.getWebVisitCount();

        map.put(SysConf.BLOG_COUNT, blogCount);
        map.put(SysConf.COMMENT_COUNT, commentCount);
        map.put(SysConf.USER_COUNT, userCount);
        map.put(SysConf.VISIT_COUNT, visitCount);

        return ResultUtil.result(SysConf.SUCCESS, map);
    }

    @ApiOperation(value = "获取最近一周用户独立IP数和访问量", notes = "获取最近一周用户独立IP数和访问量", response = String.class)
    @RequestMapping(value = "/getVisitByWeek", method = RequestMethod.GET)
    public String getVisitByWeek() {

        Map<String, Object> visitByWeek = webVisitService.getVisitByWeek();

        return ResultUtil.result(SysConf.SUCCESS, visitByWeek);
    }

    @ApiOperation(value = "获取每个标签下文章数目", notes = "获取每个标签下文章数目", response = String.class)
    @RequestMapping(value = "/getBlogCountByTag", method = RequestMethod.GET)
    public String getBlogCountByTag() {

        List<Map<String, Object>> blogCountByTag = blogService.getBlogCountByTag();

        return ResultUtil.result(SysConf.SUCCESS, blogCountByTag);
    }

    @ApiOperation(value = "获取每个分类下文章数目", notes = "获取每个分类下文章数目", response = String.class)
    @RequestMapping(value = "/getBlogCountByBlogSort", method = RequestMethod.GET)
    public String getBlogCountByBlogSort() {

        List<Map<String, Object>> blogCountByTag = blogService.getBlogCountByBlogSort();

        return ResultUtil.result(SysConf.SUCCESS, blogCountByTag);
    }

    @ApiOperation(value = "获取一年内的文章贡献数", notes = "获取一年内的文章贡献度", response = String.class)
    @RequestMapping(value = "/getBlogContributeCount", method = RequestMethod.GET)
    public String getBlogContributeCount() {

        Map<String, Object> resultMap = blogService.getBlogContributeCount();
        return ResultUtil.result(SysConf.SUCCESS, resultMap);
    }


}
