package com.moxi.mogublog.admin.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.CategoryMenu;
import com.moxi.mogublog.xo.service.CategoryMenuService;
import com.moxi.mougblog.base.enums.EStatus;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 菜单表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年9月24日15:45:18
 */

@RestController
@RequestMapping("/categoryMenu")
public class CategoryMenuRestApi {

    private static Logger log = LogManager.getLogger(AdminRestApi.class);
    @Autowired
    CategoryMenuService categoryMenuService;

    @ApiOperation(value = "获取菜单列表", notes = "获取菜单列表", response = String.class)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public String getList(HttpServletRequest request,
                          @ApiParam(name = "keyword", value = "关键字", required = false) @RequestParam(name = "keyword", required = false) String keyword,
                          @ApiParam(name = "menuLevel", value = "菜单级别", required = false) @RequestParam(name = "menuLevel", required = false, defaultValue = "0") Integer menuLevel,
                          @ApiParam(name = "currentPage", value = "当前页数", required = false) @RequestParam(name = "currentPage", required = false, defaultValue = "1") Long currentPage,
                          @ApiParam(name = "pageSize", value = "每页显示数目", required = false) @RequestParam(name = "pageSize", required = false, defaultValue = "10") Long pageSize) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<CategoryMenu>();
        if (StringUtils.isNotEmpty(keyword) && !StringUtils.isEmpty(keyword.trim())) {
            queryWrapper.like(SQLConf.NAME, keyword.trim());
        }

        if (menuLevel != 0) {
            queryWrapper.eq(SQLConf.MENU_LEVEL, menuLevel);
        }

        Page<CategoryMenu> page = new Page<>();
        page.setCurrent(currentPage);
        page.setSize(pageSize);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.SORT);
        IPage<CategoryMenu> pageList = categoryMenuService.page(page, queryWrapper);
        List<CategoryMenu> list = pageList.getRecords();

        List<String> ids = new ArrayList<String>();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getParentUid())) {
                ids.add(item.getParentUid());
            }
        });

        if (ids.size() > 0) {
            Collection<CategoryMenu> parentList = categoryMenuService.listByIds(ids);

            Map<String, CategoryMenu> map = new HashMap<String, CategoryMenu>();
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
        log.info("返回结果");

        resultMap.put(SysConf.DATA, pageList);
        return ResultUtil.result(SysConf.SUCCESS, resultMap);
    }

    @ApiOperation(value = "获取所有菜单列表", notes = "获取所有列表", response = String.class)
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public String getAll(HttpServletRequest request) {

        QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.MENU_LEVEL, "1");
        queryWrapper.orderByDesc(SQLConf.SORT);
        List<CategoryMenu> list = categoryMenuService.list(queryWrapper);

        //获取所有的ID，去寻找他的子目录
        List<String> ids = new ArrayList<String>();
        list.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUid())) {
                ids.add(item.getUid());
            }
        });

        QueryWrapper<CategoryMenu> childWrapper = new QueryWrapper<>();
        childWrapper.in(SQLConf.PARENT_UID, ids);
        Collection<CategoryMenu> childList = categoryMenuService.list(childWrapper);
        for (CategoryMenu parentItem : list) {

            List<CategoryMenu> tempList = new ArrayList<>();

            for (CategoryMenu item : childList) {

                if (item.getParentUid().equals(parentItem.getUid())) {
                    tempList.add(item);
                }
            }
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
            parentItem.setChildCategoryMenu(tempList);
        }
        return ResultUtil.result(SysConf.SUCCESS, list);
    }

    @OperationLogger(value = "增加菜单")
    @ApiOperation(value = "增加菜单", notes = "增加菜单", response = String.class)
    @PostMapping("/add")
    public String add(HttpServletRequest request,
                      @ApiParam(name = "categoryMenu", value = "菜单", required = false) @RequestBody(required = false) CategoryMenu categoryMenu) {

        if (StringUtils.isEmpty(categoryMenu.getName()) || StringUtils.isEmpty(categoryMenu.getUrl())) {
            return ResultUtil.result(SysConf.ERROR, "必填项不能为空");
        }
        //如果是一级菜单，将父ID清空
        if (categoryMenu.getMenuLevel() == 1) {
            categoryMenu.setParentUid("");
        }
        categoryMenu.insert();
        return ResultUtil.result(SysConf.SUCCESS, "添加成功");
    }

    @ApiOperation(value = "编辑菜单", notes = "编辑菜单", response = String.class)
    @PostMapping("/edit")
    public String edit(HttpServletRequest request,
                       @ApiParam(name = "categoryMenu", value = "菜单", required = false) @RequestBody(required = false) CategoryMenu categoryMenu) {

        if (StringUtils.isEmpty(categoryMenu.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        categoryMenu.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "编辑成功");
    }

    @OperationLogger(value = "删除菜单")
    @ApiOperation(value = "删除菜单", notes = "删除菜单", response = String.class)
    @PostMapping("/delete")
    public String delete(HttpServletRequest request,
                         @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid) {

        if (StringUtils.isEmpty(uid)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }
        CategoryMenu blogSort = categoryMenuService.getById(uid);
        blogSort.setStatus(EStatus.DISABLED);
        blogSort.updateById();
        return ResultUtil.result(SysConf.SUCCESS, "删除成功");
    }

    /**
     * 如果是一级菜单，直接置顶在最前面，二级菜单，就在一级菜单内置顶
     *
     * @author xzx19950624@qq.com
     * @date 2018年11月29日上午9:22:59
     */
    @OperationLogger(value = "置顶菜单")
    @ApiOperation(value = "置顶菜单", notes = "置顶菜单", response = String.class)
    @PostMapping("/stick")
    public String stick(HttpServletRequest request,
                        @ApiParam(name = "uid", value = "唯一UID", required = true) @RequestParam(name = "uid", required = true) String uid) {


        CategoryMenu categoryMenu = categoryMenuService.getById(uid);

        if (categoryMenu == null) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }

        //查找出最大的那一个
        QueryWrapper<CategoryMenu> queryWrapper = new QueryWrapper<>();

        //如果是二级目录，就在当前的兄弟中，找出最大的一个
        if (categoryMenu.getMenuLevel() == 2) {

            queryWrapper.eq(SQLConf.PARENT_UID, categoryMenu.getParentUid());

        }

        queryWrapper.eq(SQLConf.MENU_LEVEL, categoryMenu.getMenuLevel());

        queryWrapper.orderByDesc(SQLConf.SORT);

        CategoryMenu maxSort = categoryMenuService.getOne(queryWrapper);

        if (StringUtils.isEmpty(maxSort.getUid())) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        }

        Integer sortCount = maxSort.getSort() + 1;

        categoryMenu.setSort(sortCount);

        categoryMenu.updateById();

        return ResultUtil.result(SysConf.SUCCESS, "置顶成功");
    }

}

