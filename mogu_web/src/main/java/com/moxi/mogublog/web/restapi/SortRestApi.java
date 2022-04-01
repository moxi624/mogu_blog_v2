package com.moxi.mogublog.web.restapi;


import com.moxi.mogublog.web.annotion.log.BussinessLog;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mougblog.base.enums.EBehavior;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 归档 RestApi
 *
 * @author 陌溪
 * @date 2019年10月24日15:29:35
 */
@RestController
@RequestMapping("/sort")
@Api(value = "博客归档相关接口", tags = {"博客归档相关接口"})
@Slf4j
public class SortRestApi {

    @Autowired
    BlogService blogService;

    /**
     * 获取归档的信息
     */
    @ApiOperation(value = "归档", notes = "归档")
    @GetMapping("/getSortList")
    public String getSortList() {
        log.info("获取归档日期");
        return blogService.getBlogTimeSortList();
    }

    @BussinessLog(value = "点击归档", behavior = EBehavior.VISIT_SORT)
    @ApiOperation(value = "通过月份获取文章", notes = "通过月份获取文章")
    @GetMapping("/getArticleByMonth")
    public String getArticleByMonth(@ApiParam(name = "monthDate", value = "归档的日期", required = false) @RequestParam(name = "monthDate", required = false) String monthDate) {
        log.info("通过月份获取文章列表");
        return blogService.getArticleByMonth(monthDate);
    }
}

