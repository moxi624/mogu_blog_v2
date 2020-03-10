package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.MessageConf;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.admin.util.WebUtils;
import com.moxi.mogublog.utils.MD5Utils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.User;
import com.moxi.mogublog.xo.service.UserService;
import com.moxi.mogublog.xo.vo.UserVO;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.exception.ThrowableUtils;
import com.moxi.mougblog.base.validator.group.Delete;
import com.moxi.mougblog.base.validator.group.GetList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 RestApi
 * </p>
 *
 * @author 陌溪
 * @since 2020年1月4日21:29:09
 */
@RestController
@Api(value = "用户RestApi", tags = {"UserRestApi"})
@RequestMapping("/user")
@Slf4j
public class UserRestApi {

    @Autowired
    WebUtils webUtils;

    @Autowired
    UserService userService;

    @Value(value = "${DEFAULE_PWD}")
    private String DEFAULE_PWD;

    @Autowired
    private PictureFeignClient pictureFeignClient;

    @ApiOperation(value = "获取用户列表", notes = "获取用户列表", response = String.class)
    @PostMapping("/getList")
    public String getList(@Validated({GetList.class}) @RequestBody UserVO userVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 查询用户名
        if (StringUtils.isNotEmpty(userVO.getKeyword()) && !StringUtils.isEmpty(userVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.USER_NAME, userVO.getKeyword().trim()).or().like(SQLConf.NICK_NAME, userVO.getKeyword().trim());
        }
        if (StringUtils.isNotEmpty(userVO.getSource()) && !StringUtils.isEmpty(userVO.getSource().trim())) {
            queryWrapper.eq(SQLConf.SOURCE, userVO.getSource().trim());
        }
        if (userVO.getCommentStatus() != null) {
            queryWrapper.eq(SQLConf.COMMENT_STATUS, userVO.getCommentStatus());
        }
        queryWrapper.select(User.class, i -> !i.getProperty().equals(SQLConf.PASS_WORD));
        Page<User> page = new Page<>();
        page.setCurrent(userVO.getCurrentPage());
        page.setSize(userVO.getPageSize());
        queryWrapper.ne(SQLConf.STATUS, EStatus.DISABLED);

        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<User> pageList = userService.page(page, queryWrapper);

        List<User> list = pageList.getRecords();

        final StringBuffer fileUids = new StringBuffer();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                fileUids.append(item.getAvatar() + SysConf.FILE_SEGMENTATION);
            }
        });

        Map<String, String> pictureMap = new HashMap<>();
        String pictureResult = null;

        if (fileUids != null) {
            pictureResult = this.pictureFeignClient.getPicture(fileUids.toString(), SysConf.FILE_SEGMENTATION);
        }
        List<Map<String, Object>> picList = webUtils.getPictureMap(pictureResult);

        picList.forEach(item -> {
            pictureMap.put(item.get(SQLConf.UID).toString(), item.get(SQLConf.URL).toString());
        });

        for (User item : list) {


            //获取图片
            if (StringUtils.isNotEmpty(item.getAvatar())) {
                List<String> pictureUidsTemp = StringUtils.changeStringToString(item.getAvatar(), SysConf.FILE_SEGMENTATION);
                List<String> pictureListTemp = new ArrayList<>();
                pictureUidsTemp.forEach(picture -> {
                    if (pictureMap.get(picture) != null && pictureMap.get(picture) != "") {
                        pictureListTemp.add(pictureMap.get(picture));
                    }
                });
                if (pictureListTemp.size() > 0) {
                    item.setPhotoUrl(pictureListTemp.get(0));
                }
            }
        }

        log.info("获取用户列表");
        return ResultUtil.result(SysConf.SUCCESS, pageList);
    }


    @OperationLogger(value = "删除用户")
    @ApiOperation(value = "删除用户", notes = "删除用户", response = String.class)
    @PostMapping("/delete")
    public String delete(@Validated({Delete.class}) @RequestBody UserVO userVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        User user = userService.getById(userVO.getUid());
        user.setStatus(EStatus.DISABLED);
        user.updateById();
        return ResultUtil.result(SysConf.SUCCESS, MessageConf.DELETE_SUCCESS);
    }

    @OperationLogger(value = "禁言/解禁用户")
    @ApiOperation(value = "禁言/解禁用户", notes = "禁言/解禁用户", response = String.class)
    @PostMapping("/freeze")
    public String freezeUser(@Validated({Delete.class}) @RequestBody UserVO userVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        User user = userService.getById(userVO.getUid());

        if (user.getCommentStatus() == SysConf.ZERO) {
            user.setCommentStatus(SysConf.ONE);
        } else if (user.getStatus() == SysConf.ONE) {
            user.setCommentStatus(SysConf.ZERO);
        }
        user.updateById();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }

    @OperationLogger(value = "重置用户密码")
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码", response = String.class)
    @PostMapping("/resetUserPassword")
    public String resetUserPassword(@Validated({Delete.class}) @RequestBody UserVO userVO, BindingResult result) {

        // 参数校验
        ThrowableUtils.checkParamArgument(result);

        User user = userService.getById(userVO.getUid());
        user.setPassWord(MD5Utils.string2MD5(DEFAULE_PWD));
        user.updateById();

        return ResultUtil.result(SysConf.SUCCESS, MessageConf.OPERATION_SUCCESS);
    }
}