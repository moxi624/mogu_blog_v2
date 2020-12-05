package com.moxi.mogublog.admin.restapi;


import com.moxi.mogublog.admin.annotion.AuthorityVerify.AuthorityVerify;
import com.moxi.mogublog.admin.annotion.AvoidRepeatableCommit.AvoidRepeatableCommit;
import com.moxi.mogublog.admin.annotion.OperationLogger.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.service.LinkService;
import com.moxi.mogublog.xo.vo.LinkVO;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.Delete;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.validator.group.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 友链表 RestApi
 *
 * @author 陌溪
 * @date 2018-09-08
 */
@RestController
@Api(value = "友情链接相关接口", tags = {"友情链接相关接口"})
@RequestMapping("/link")
@Slf4j
public class LinkRestApi {

    @Autowired
    LinkService linkService;

    @AuthorityVerify
    @ApiOperation(value = "获取友链列表", notes = "获取友链列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody LinkVO linkVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        log.info("获取友链列表");
        return ResultUtil.successWithData(linkService.getPageList(linkVO));
    }

    @AvoidRepeatableCommit
    @AuthorityVerify
    @OperationLogger(value = "增加友链")
    @ApiOperation(value = "增加友链", notes = "增加友链", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody LinkVO linkVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return linkService.addLink(linkVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "编辑友链")
    @ApiOperation(value = "编辑友链", notes = "编辑友链", response = String.class)
    @PostMapping("/edit")
    public String edit(@Validated({Update.class}) @RequestBody LinkVO linkVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return linkService.editLink(linkVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "删除友链")
    @ApiOperation(value = "删除友链", notes = "删除友链", response = String.class)
    @PostMapping("/delete")
    public String delete(@Validated({Delete.class}) @RequestBody LinkVO linkVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return linkService.deleteLink(linkVO);
    }

    @AuthorityVerify
    @ApiOperation(value = "置顶友链", notes = "置顶友链", response = String.class)
    @PostMapping("/stick")
    public String stick(@Validated({Delete.class}) @RequestBody LinkVO linkVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return linkService.stickLink(linkVO);
    }
}