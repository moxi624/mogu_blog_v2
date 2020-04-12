package com.moxi.mogublog.admin.restapi;


import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.security.AuthorityVerify;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.xo.service.PictureService;
import com.moxi.mogublog.xo.vo.PictureVO;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.Delete;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.validator.group.Insert;
import com.moxi.mougblog.base.validator.group.Update;
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
 * <p>
 * 图片表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年9月17日16:21:43
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureRestApi {

    @Autowired
    PictureService pictureService;

    @AuthorityVerify
    @ApiOperation(value = "获取图片列表", notes = "获取图片列表", response = String.class)
    @PostMapping(value = "/getList")
    public String getList(@Validated({GetList.class}) @RequestBody PictureVO pictureVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return ResultUtil.result(SysConf.SUCCESS, pictureService.getPageList(pictureVO));
    }

    @AuthorityVerify
    @OperationLogger(value = "增加图片")
    @ApiOperation(value = "增加图片", notes = "增加图片", response = String.class)
    @PostMapping("/add")
    public String add(@Validated({Insert.class}) @RequestBody PictureVO pictureVO, BindingResult result) {
        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return pictureService.addPicture(pictureVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "编辑图片")
    @ApiOperation(value = "编辑图片", notes = "编辑图片", response = String.class)
    @PostMapping("/edit")
    public String edit(@Validated({Update.class}) @RequestBody PictureVO pictureVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return pictureService.editPicture(pictureVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "删除图片")
    @ApiOperation(value = "删除图片", notes = "删除图片", response = String.class)
    @PostMapping("/delete")
    public String delete(@RequestBody PictureVO pictureVO, BindingResult result) {
        return pictureService.deleteBatchPicture(pictureVO);
    }

    @AuthorityVerify
    @OperationLogger(value = "通过图片Uid将图片设为封面")
    @ApiOperation(value = "通过图片Uid将图片设为封面", notes = "通过图片Uid将图片设为封面", response = String.class)
    @PostMapping("/setCover")
    public String setCover(@Validated({Update.class}) @RequestBody PictureVO pictureVO, BindingResult result) {
        // 参数校验
        ThrowableUtils.checkParamArgument(result);
        return pictureService.setPictureCover(pictureVO);
    }
}

