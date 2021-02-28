package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.CategoryMenu;
import com.moxi.mogublog.commons.entity.WebNavbar;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.WebNavbarMapper;
import com.moxi.mogublog.xo.service.WebNavbarService;
import com.moxi.mogublog.xo.vo.CategoryMenuVO;
import com.moxi.mogublog.xo.vo.WebNavbarVO;
import com.moxi.mougblog.base.enums.EMenuType;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 门户页导航栏 服务实现类
 *
 * @author 陌溪
 * @date 2021年2月22日17:11:28
 */
@Service
public class WebNavbarServiceImpl extends SuperServiceImpl<WebNavbarMapper, WebNavbar> implements WebNavbarService {

    @Autowired
    WebNavbarService webNavbarService;

    @Override
    public IPage<WebNavbar> getPageList(WebNavbarVO webNavbarVO) {
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<WebNavbar> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(webNavbarVO.getKeyword()) && !StringUtils.isEmpty(webNavbarVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.NAME, webNavbarVO.getKeyword().trim());
        }
        if (webNavbarVO.getNavbarLevel() != 0) {
            queryWrapper.eq(SQLConf.MENU_LEVEL, webNavbarVO.getNavbarLevel());
        }
        Page<WebNavbar> page = new Page<>();
        page.setCurrent(webNavbarVO.getCurrentPage());
        page.setSize(webNavbarVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        IPage<WebNavbar> pageList = webNavbarService.page(page, queryWrapper);
        return pageList;
    }

    @Override
    public List<WebNavbar> getAllList() {
        QueryWrapper<WebNavbar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.NAVBAR_LEVEL, Constants.STR_ONE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);

        List<WebNavbar> list = webNavbarService.list(queryWrapper);
        //获取所有的ID，去寻找他的子目录
        List<String> ids = new ArrayList<>();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUid())) {
                ids.add(item.getUid());
            }
        });
        QueryWrapper<WebNavbar> childWrapper = new QueryWrapper<>();
        childWrapper.in(SQLConf.PARENT_UID, ids);
        childWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        Collection<WebNavbar> childList = webNavbarService.list(childWrapper);

        // 给一级导航栏设置二级导航栏
        for (WebNavbar parentItem : list) {
            List<WebNavbar> tempList = new ArrayList<>();
            for (WebNavbar item : childList) {
                if (item.getParentUid().equals(parentItem.getUid())) {
                    tempList.add(item);
                }
            }
            Collections.sort(tempList);
            parentItem.setChildWebNavbar(tempList);
        }
        return list;
    }

    @Override
    public String addWebNavbar(WebNavbarVO webNavbarVO) {
        //如果是一级菜单，将父ID清空
        if (webNavbarVO.getNavbarLevel() == 1) {
            webNavbarVO.setParentUid("");
        }
        WebNavbar webNavbar = new WebNavbar();
        // 插入数据【使用Spring工具类提供的深拷贝】
        BeanUtils.copyProperties(webNavbarVO, webNavbar, SysConf.STATUS);
        webNavbar.insert();
        return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
    }

    @Override
    public String editWebNavbar(WebNavbarVO webNavbarVO) {
        //如果是一级菜单，将父ID清空
        if (webNavbarVO.getNavbarLevel() == 1) {
            webNavbarVO.setParentUid("");
        }
        WebNavbar webNavbar = webNavbarService.getById(webNavbarVO.getUid());
        // 插入数据【使用Spring工具类提供的深拷贝】
        BeanUtils.copyProperties(webNavbarVO, webNavbar, SysConf.STATUS, SysConf.UID);
        webNavbar.setUpdateTime(new Date());
        webNavbarService.updateById(webNavbar);
        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteWebNavbar(WebNavbarVO webNavbarVO) {
        QueryWrapper<WebNavbar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.in(SQLConf.PARENT_UID, webNavbarVO.getUid());
        Integer menuCount = webNavbarService.count(queryWrapper);
        if (menuCount > 0) {
            return ResultUtil.errorWithMessage(MessageConf.CHILDREN_MENU_UNDER_THIS_MENU);
        }
        WebNavbar webNavbar = webNavbarService.getById(webNavbarVO.getUid());
        webNavbar.setStatus(EStatus.DISABLED);
        webNavbar.setUpdateTime(new Date());
        webNavbar.updateById();
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

}
