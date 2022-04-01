package com.moxi.mogublog.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.commons.entity.*;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.xo.global.RedisConf;
import com.moxi.mogublog.xo.global.SQLConf;
import com.moxi.mogublog.xo.global.SysConf;
import com.moxi.mogublog.xo.mapper.WebVisitMapper;
import com.moxi.mogublog.xo.service.*;
import com.moxi.mogublog.xo.vo.WebVisitVO;
import com.moxi.mougblog.base.enums.EBehavior;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSysConf;
import com.moxi.mougblog.base.global.Constants;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户访问记录表 服务实现类
 *
 * @author 陌溪
 * @date 2018-09-08
 */
@Service
public class WebVisitServiceImpl extends SuperServiceImpl<WebVisitMapper, WebVisit> implements WebVisitService {

    @Resource
    private WebVisitMapper webVisitMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogSortService blogSortService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Async
    @Override
    public void addWebVisit(String userUid, HttpServletRequest request, String behavior, String moduleUid, String otherData) {

        //增加记录（可以考虑使用AOP）
        Map<String, String> map = IpUtils.getOsAndBrowserInfo(request);
        String os = map.get("OS");
        String browser = map.get("BROWSER");
        WebVisit webVisit = new WebVisit();
        String ip = IpUtils.getIpAddr(request);
        webVisit.setIp(ip);

        //从Redis中获取IP来源
        String jsonResult = stringRedisTemplate.opsForValue().get("IP_SOURCE:" + ip);
        if (StringUtils.isEmpty(jsonResult)) {
            String addresses = IpUtils.getAddresses("ip=" + ip, "utf-8");
            if (StringUtils.isNotEmpty(addresses)) {
                webVisit.setIpSource(addresses);
                stringRedisTemplate.opsForValue().set("IP_SOURCE" + BaseSysConf.REDIS_SEGMENTATION + ip, addresses, 24, TimeUnit.HOURS);
            }
        } else {
            webVisit.setIpSource(jsonResult);
        }
        webVisit.setOs(os);
        webVisit.setBrowser(browser);
        webVisit.setUserUid(userUid);
        webVisit.setBehavior(behavior);
        webVisit.setModuleUid(moduleUid);
        webVisit.setOtherData(otherData);
        webVisit.insert();
    }

    @Override
    public int getWebVisitCount() {
        // 获取今日开始和结束时间
        String startTime = DateUtils.getToDayStartTime();
        String endTime = DateUtils.getToDayEndTime();
        return webVisitMapper.getIpCount(startTime, endTime);
    }

    @Override
    public Map<String, Object> getVisitByWeek() {
        // 从Redis中获取一周访问量
        String weekVisitJson = redisUtil.get(RedisConf.DASHBOARD + Constants.SYMBOL_COLON + RedisConf.WEEK_VISIT);
        if (StringUtils.isNotEmpty(weekVisitJson)) {
            Map<String, Object> weekVisitMap = JsonUtils.jsonToMap(weekVisitJson);
            return weekVisitMap;
        }

        // 获取到今天结束的时间
        String todayEndTime = DateUtils.getToDayEndTime();
        //获取最近七天的日期
        Date sevenDaysDate = DateUtils.getDate(todayEndTime, -6);
        String sevenDays = DateUtils.getOneDayStartTime(sevenDaysDate);
        // 获取最近七天的数组列表
        List<String> sevenDaysList = DateUtils.getDaysByN(7, "yyyy-MM-dd");
        // 获得最近七天的访问量
        List<Map<String, Object>> pvMap = webVisitMapper.getPVByWeek(sevenDays, todayEndTime);
        // 获得最近七天的独立用户
        List<Map<String, Object>> uvMap = webVisitMapper.getUVByWeek(sevenDays, todayEndTime);

        Map<String, Object> countPVMap = new HashMap<>();
        Map<String, Object> countUVMap = new HashMap<>();

        for (Map<String, Object> item : pvMap) {
            countPVMap.put(item.get("DATE").toString(), item.get("COUNT"));
        }
        for (Map<String, Object> item : uvMap) {
            countUVMap.put(item.get("DATE").toString(), item.get("COUNT"));
        }
        // 访问量数组
        List<Integer> pvList = new ArrayList<>();
        // 独立用户数组
        List<Integer> uvList = new ArrayList<>();

        for (String day : sevenDaysList) {
            if (countPVMap.get(day) != null) {
                Number pvNumber = (Number) countPVMap.get(day);
                Number uvNumber = (Number) countUVMap.get(day);
                pvList.add(pvNumber.intValue());
                uvList.add(uvNumber.intValue());
            } else {
                pvList.add(0);
                uvList.add(0);
            }
        }
        Map<String, Object> resultMap = new HashMap<>(Constants.NUM_THREE);
        // 不含年份的数组格式
        List<String> resultSevenDaysList = DateUtils.getDaysByN(7, "MM-dd");
        resultMap.put("date", resultSevenDaysList);
        resultMap.put("pv", pvList);
        resultMap.put("uv", uvList);

        //TODO 可能会存在短期的数据不一致的问题，即零点时不能准时更新，而是要在0:10才会重新刷新纪录。 后期考虑加入定时器处理这个问题
        // 将一周访问量存入Redis中【过期时间10分钟】
        redisUtil.setEx(RedisConf.DASHBOARD + Constants.SYMBOL_COLON + RedisConf.WEEK_VISIT, JsonUtils.objectToJson(resultMap), 10, TimeUnit.MINUTES);
        return resultMap;
    }

    @Override
    public IPage<WebVisit> getPageList(WebVisitVO webVisitVO) {
        QueryWrapper<WebVisit> queryWrapper = new QueryWrapper<>();

        // 得到所有的枚举对象
        EBehavior[] arr = EBehavior.values();

        // 设置关键字查询
        if (!StringUtils.isEmpty(webVisitVO.getKeyword()) && !StringUtils.isEmpty(webVisitVO.getKeyword().trim())) {
            String behavior = "";
            for (int a = 0; a < arr.length; a++) {
                // 设置行为名称
                if (arr[a].getContent().equals(webVisitVO.getKeyword().trim())) {
                    behavior = arr[a].getBehavior();
                }
            }

            queryWrapper.like(SQLConf.IP, webVisitVO.getKeyword().trim()).or().eq(SQLConf.BEHAVIOR, behavior);
        }

        // 设置起始时间段
        if (!StringUtils.isEmpty(webVisitVO.getStartTime())) {
            String[] time = webVisitVO.getStartTime().split(SysConf.FILE_SEGMENTATION);
            if (time.length == 2) {
                queryWrapper.between(SQLConf.CREATE_TIME, DateUtils.str2Date(time[0]), DateUtils.str2Date(time[1]));
            }
        }

        Page<WebVisit> page = new Page<>();
        page.setCurrent(webVisitVO.getCurrentPage());
        page.setSize(webVisitVO.getPageSize());
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
        IPage<WebVisit> pageList = webVisitMapper.selectPage(page, queryWrapper);

        List<WebVisit> list = pageList.getRecords();
        List<String> blogUids = new ArrayList<>();
        List<String> blogOids = new ArrayList<>();
        List<String> tagUids = new ArrayList<>();
        List<String> sortUids = new ArrayList<>();
        List<String> linkUids = new ArrayList<>();

        list.forEach(item -> {
            // 当点击博客或者点赞博客时
            if (item.getBehavior().equals(EBehavior.BLOG_CONTNET.getBehavior())
                    || item.getBehavior().equals(EBehavior.BLOG_PRAISE.getBehavior())) {
                // 从日志中提取出oid和uid
                if(StringUtils.isNotEmpty(item.getModuleUid())) {
                    blogUids.add(item.getModuleUid());
                } else if(StringUtils.isNotEmpty(item.getOtherData())) {
                    blogOids.add(item.getOtherData());
                }
            } else if (item.getBehavior().equals(EBehavior.BLOG_SORT.getBehavior()) || item.getBehavior().equals(EBehavior.VISIT_CLASSIFY.getBehavior())) {
                sortUids.add(item.getModuleUid());
            } else if (item.getBehavior().equals(EBehavior.BLOG_TAG.getBehavior()) || item.getBehavior().equals(EBehavior.VISIT_TAG.getBehavior()) ) {
                tagUids.add(item.getModuleUid());
            } else if (item.getBehavior().equals(EBehavior.FRIENDSHIP_LINK.getBehavior())) {
                linkUids.add(item.getModuleUid());
            }
        });
        Collection<Blog> blogList = new ArrayList<>();
        Collection<Blog> blogListByOid = new ArrayList<>();
        Collection<Tag> tagList = new ArrayList<>();
        Collection<BlogSort> sortList = new ArrayList<>();
        Collection<Link> linkList = new ArrayList<>();


        if (blogUids.size() > 0) {
            blogList = blogService.listByIds(blogUids);
        }

        if (blogOids.size() > 0) {
            QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
            blogQueryWrapper.in(SQLConf.OID, blogOids);
            blogListByOid = blogService.list(blogQueryWrapper);
        }

        if (tagUids.size() > 0) {
            tagList = tagService.listByIds(tagUids);
        }

        if (sortUids.size() > 0) {
            sortList = blogSortService.listByIds(sortUids);
        }

        if (linkUids.size() > 0) {
            linkList = linkService.listByIds(linkUids);
        }

        Map<String, String> contentMap = new HashMap<>();
        blogList.forEach(item -> {
            contentMap.put(item.getUid(), item.getTitle());
        });

        blogListByOid.forEach(item -> {
            contentMap.put(item.getOid() + "", item.getTitle());
        });

        tagList.forEach(item -> {
            contentMap.put(item.getUid(), item.getContent());
        });

        sortList.forEach(item -> {
            contentMap.put(item.getUid(), item.getSortName());
        });

        linkList.forEach(item -> {
            contentMap.put(item.getUid(), item.getTitle());
        });

        list.forEach(item -> {

            for (int a = 0; a < arr.length; a++) {
                // 设置行为名称
                if (arr[a].getBehavior().equals(item.getBehavior())) {
                    item.setBehaviorContent(arr[a].getContent());
                    break;
                }
            }

            if (item.getBehavior().equals(EBehavior.BLOG_CONTNET.getBehavior()) ||
                    item.getBehavior().equals(EBehavior.BLOG_PRAISE.getBehavior()) ||
                    item.getBehavior().equals(EBehavior.BLOG_SORT.getBehavior()) ||
                    item.getBehavior().equals(EBehavior.BLOG_TAG.getBehavior()) ||
                    item.getBehavior().equals(EBehavior.VISIT_TAG.getBehavior()) ||
                    item.getBehavior().equals(EBehavior.VISIT_CLASSIFY.getBehavior()) ||
                    item.getBehavior().equals(EBehavior.FRIENDSHIP_LINK.getBehavior())) {

                //从map中获取到对应的名称
                if (StringUtils.isNotEmpty(item.getModuleUid())) {
                    item.setContent(contentMap.get(item.getModuleUid()));
                } else {
                    // 从otherData中获取博客oid
                    item.setContent(contentMap.get(item.getOtherData()));
                }
            } else {
                item.setContent(item.getOtherData());
            }
        });
        pageList.setRecords(list);
        return pageList;
    }

}
