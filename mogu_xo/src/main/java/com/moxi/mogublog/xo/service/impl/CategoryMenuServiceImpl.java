package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.CategoryMenu;
import com.moxi.mogublog.utils.RedisUtil;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.global.MessageConf;
import com.moxi.mogublog.xo.global.RedisConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.CategoryMenuMapper;
import com.moxi.mogublog.xo.service.CategoryMenuService;
import com.moxi.mogublog.xo.vo.CategoryMenuVO;
import com.moxi.mougblog.base.enums.EMenuType;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年11月23日10:42:30
 */
@Service
public class CategoryMenuServiceImpl extends SuperServiceImpl<CategoryMenuMapper, CategoryMenu> implements CategoryMenuService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    CategoryMenuService categoryMenuService;

    @Override
    public Map<String, Object> getPageList(CategoryMenuVO categoryMenuVO) {
        Map<String, Object> resultMap = new HashMap<>();
        QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(categoryMenuVO.getKeyword()) && !StringUtils.isEmpty(categoryMenuVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.NAME, categoryMenuVO.getKeyword().trim());
        }

        if (categoryMenuVO.getMenuLevel() != 0) {
            queryWrapper.eq(SQLConf.MENU_LEVEL, categoryMenuVO.getMenuLevel());
        }

        Page<CategoryMenu> page = new Page<>();
        page.setCurrent(categoryMenuVO.getCurrentPage());
        page.setSize(categoryMenuVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        IPage<CategoryMenu> pageList = categoryMenuService.page(page, queryWrapper);
        List<CategoryMenu> list = pageList.getRecords();

        List<String> ids = new ArrayList<>();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getParentUid())) {
                ids.add(item.getParentUid());
            }
        });

        if (ids.size() > 0) {
            Collection<CategoryMenu> parentList = categoryMenuService.listByIds(ids);
            Map<String, CategoryMenu> map = new HashMap<>();
            parentList.forEach(item -> {
                map.put(item.getUid(), item);
            });

            list.forEach(item -> {
                if (StringUtils.isNotEmpty(item.getParentUid())) {
                    item.setParentCategoryMenu(map.get(item.getParentUid()));
                }
            });

            resultMap.put(SysConf.OTHER_DATA, parentList);
        }
        pageList.setRecords(list);
        resultMap.put(SysConf.DATA, pageList);
        return resultMap;
    }

    @Override
    public List<CategoryMenu> getAllList(String keyword) {
        QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.MENU_LEVEL, "1");
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.eq(SQLConf.UID, keyword);
        }
        queryWrapper.orderByDesc(SQLConf.SORT);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.MENU_TYPE, EMenuType.MENU);
        List<CategoryMenu> list = categoryMenuService.list(queryWrapper);

        //获取所有的ID，去寻找他的子目录
        List<String> ids = new ArrayList<>();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUid())) {
                ids.add(item.getUid());
            }
        });

        QueryWrapper<CategoryMenu> childWrapper = new QueryWrapper<>();
        childWrapper.in(SQLConf.PARENT_UID, ids);
        childWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        Collection<CategoryMenu> childList = categoryMenuService.list(childWrapper);

        //获取所有的二级菜单，去寻找他的子按钮
        List<String> secondMenuUids = new ArrayList<>();
        childList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUid())) {
                secondMenuUids.add(item.getUid());
            }
        });

        QueryWrapper<CategoryMenu> buttonWrapper = new QueryWrapper<>();
        buttonWrapper.in(SQLConf.PARENT_UID, secondMenuUids);
        buttonWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        Collection<CategoryMenu> buttonList = categoryMenuService.list(buttonWrapper);

        Map<String, List<CategoryMenu>> map = new HashMap<>();
        buttonList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getParentUid())) {
                if (map.get(item.getParentUid()) == null) {
                    List<CategoryMenu> tempList = new ArrayList<>();
                    tempList.add(item);
                    map.put(item.getParentUid(), tempList);
                } else {
                    List<CategoryMenu> tempList = map.get(item.getParentUid());
                    tempList.add(item);
                    map.put(item.getParentUid(), tempList);
                }
            }
        });

        // 给二级菜单设置三级按钮
        childList.forEach(item -> {
            if (map.get(item.getUid()) != null) {
                List<CategoryMenu> tempList = map.get(item.getUid());
                Collections.sort(tempList, new Comparator<CategoryMenu>() {

                    /*
                     * int compare(CategoryMenu p1, CategoryMenu p2) 返回一个基本类型的整型，
                     * 返回负数表示：p1 小于p2，
                     * 返回0 表示：p1和p2相等，
                     * 返回正数表示：p1大于p2
                     */
                    @Override
                    public int compare(CategoryMenu o1, CategoryMenu o2) {

                        //按照CategoryMenu的Sort进行降序排列
                        if (o1.getSort() > o2.getSort()) {
                            return -1;
                        }
                        if (o1.getSort().equals(o2.getSort())) {
                            return 0;
                        }
                        return 1;
                    }

                });
                item.setChildCategoryMenu(tempList);
            }
        });


        // 给一级菜单设置二级菜单
        for (CategoryMenu parentItem : list) {
            List<CategoryMenu> tempList = new ArrayList<>();
            for (CategoryMenu item : childList) {

                if (item.getParentUid().equals(parentItem.getUid())) {
                    tempList.add(item);
                }
            }
            Collections.sort(tempList);
            parentItem.setChildCategoryMenu(tempList);
        }
        return list;
    }

    @Override
    public List<CategoryMenu> getButtonAllList(String keyword) {
        QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.MENU_LEVEL, "2");
        queryWrapper.orderByDesc(SQLConf.SORT);
        if (StringUtils.isNotEmpty(keyword)) {
            queryWrapper.eq(SQLConf.UID, keyword);
        }
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.eq(SQLConf.MENU_TYPE, EMenuType.MENU);
        List<CategoryMenu> list = categoryMenuService.list(queryWrapper);

        //获取所有的ID，去寻找他的子目录
        List<String> ids = new ArrayList<>();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUid())) {
                ids.add(item.getUid());
            }
        });

        QueryWrapper<CategoryMenu> childWrapper = new QueryWrapper<>();
        childWrapper.in(SQLConf.PARENT_UID, ids);
        childWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        Collection<CategoryMenu> childList = categoryMenuService.list(childWrapper);
        Set<String> secondUidSet = new HashSet<>();
        Map<String, List<CategoryMenu>> map = new HashMap<>();
        childList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getParentUid())) {

                secondUidSet.add(item.getParentUid());

                if (map.get(item.getParentUid()) == null) {
                    List<CategoryMenu> tempList = new ArrayList<>();
                    tempList.add(item);
                    map.put(item.getParentUid(), tempList);
                } else {
                    List<CategoryMenu> tempList = map.get(item.getParentUid());
                    tempList.add(item);
                    map.put(item.getParentUid(), tempList);
                }
            }
        });

        // 过滤不在Button列表中的二级菜单
        List<CategoryMenu> secondCategoryMenuList = new ArrayList<>();
        for (CategoryMenu secondCategoryMenu : list) {
            for (String uid : secondUidSet) {
                if (secondCategoryMenu.getUid().equals(uid)) {
                    secondCategoryMenuList.add(secondCategoryMenu);
                    break;
                }
            }
        }

        // 给二级菜单设置三级按钮
        secondCategoryMenuList.forEach(item -> {
            if (map.get(item.getUid()) != null) {
                List<CategoryMenu> tempList = map.get(item.getUid());
                Collections.sort(tempList);
                item.setChildCategoryMenu(tempList);
            }
        });
        return list;
    }

    @Override
    public String addCategoryMenu(CategoryMenuVO categoryMenuVO) {
        //如果是一级菜单，将父ID清空
        if (categoryMenuVO.getMenuLevel() == 1) {
            categoryMenuVO.setParentUid("");
        }
        CategoryMenu categoryMenu = new CategoryMenu();
        categoryMenu.setParentUid(categoryMenuVO.getParentUid());
        categoryMenu.setSort(categoryMenuVO.getSort());
        categoryMenu.setIcon(categoryMenuVO.getIcon());
        categoryMenu.setSummary(categoryMenuVO.getSummary());
        categoryMenu.setMenuLevel(categoryMenuVO.getMenuLevel());
        categoryMenu.setMenuType(categoryMenuVO.getMenuType());
        categoryMenu.setName(categoryMenuVO.getName());
        categoryMenu.setUrl(categoryMenuVO.getUrl());
        categoryMenu.setIsShow(categoryMenuVO.getIsShow());
        categoryMenu.setUpdateTime(new Date());
        categoryMenu.setIsJumpExternalUrl(categoryMenuVO.getIsJumpExternalUrl());
        categoryMenu.insert();
        return ResultUtil.successWithMessage(MessageConf.INSERT_SUCCESS);
    }

    @Override
    public String editCategoryMenu(CategoryMenuVO categoryMenuVO) {
        CategoryMenu categoryMenu = categoryMenuService.getById(categoryMenuVO.getUid());
        categoryMenu.setParentUid(categoryMenuVO.getParentUid());
        categoryMenu.setSort(categoryMenuVO.getSort());
        categoryMenu.setIcon(categoryMenuVO.getIcon());
        categoryMenu.setSummary(categoryMenuVO.getSummary());
        categoryMenu.setMenuLevel(categoryMenuVO.getMenuLevel());
        categoryMenu.setMenuType(categoryMenuVO.getMenuType());
        categoryMenu.setName(categoryMenuVO.getName());
        categoryMenu.setUrl(categoryMenuVO.getUrl());
        categoryMenu.setIsShow(categoryMenuVO.getIsShow());
        categoryMenu.setUpdateTime(new Date());
        categoryMenu.setIsJumpExternalUrl(categoryMenuVO.getIsJumpExternalUrl());
        categoryMenu.updateById();
        // 修改成功后，需要删除redis中所有的admin访问路径
        deleteAdminVisitUrl();
        return ResultUtil.successWithMessage(MessageConf.UPDATE_SUCCESS);
    }

    @Override
    public String deleteCategoryMenu(CategoryMenuVO categoryMenuVO) {
        QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.in(SQLConf.PARENT_UID, categoryMenuVO.getUid());
        Integer menuCount = categoryMenuService.count(queryWrapper);
        if (menuCount > 0) {
            return ResultUtil.errorWithMessage(MessageConf.CHILDREN_MENU_UNDER_THIS_MENU);
        }
        CategoryMenu categoryMenu = categoryMenuService.getById(categoryMenuVO.getUid());
        categoryMenu.setStatus(EStatus.DISABLED);
        categoryMenu.setUpdateTime(new Date());
        categoryMenu.updateById();
        // 修改成功后，需要删除redis中所有的admin访问路径
        deleteAdminVisitUrl();
        return ResultUtil.successWithMessage(MessageConf.DELETE_SUCCESS);
    }

    @Override
    public String stickCategoryMenu(CategoryMenuVO categoryMenuVO) {
        CategoryMenu categoryMenu = categoryMenuService.getById(categoryMenuVO.getUid());
        //查找出最大的那一个
        QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();
        //如果是二级菜单 或者 按钮，就在当前的兄弟中，找出最大的一个
        if (categoryMenu.getMenuLevel() == Constants.NUM_TWO || categoryMenu.getMenuType() == EMenuType.BUTTON) {
            queryWrapper.eq(SQLConf.PARENT_UID, categoryMenu.getParentUid());
        }
        queryWrapper.eq(SQLConf.MENU_LEVEL, categoryMenu.getMenuLevel());
        queryWrapper.orderByDesc(SQLConf.SORT);
        queryWrapper.last(SysConf.LIMIT_ONE);
        CategoryMenu maxSort = categoryMenuService.getOne(queryWrapper);
        if (StringUtils.isEmpty(maxSort.getUid())) {
            return ResultUtil.errorWithMessage(MessageConf.OPERATION_FAIL);
        }
        Integer sortCount = maxSort.getSort() + 1;
        categoryMenu.setSort(sortCount);
        categoryMenu.setUpdateTime(new Date());
        categoryMenu.updateById();
        return ResultUtil.successWithMessage(MessageConf.OPERATION_SUCCESS);
    }

    /**
     * 删除Redis中管理员的访问路径
     */
    private void deleteAdminVisitUrl() {
        Set<String> keys = redisUtil.keys(RedisConf.ADMIN_VISIT_MENU + "*");
        redisUtil.delete(keys);
    }
}
