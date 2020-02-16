package com.moxi.mogublog.xo.service.impl;

import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mogublog.utils.IpUtils;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.WebVisit;
import com.moxi.mogublog.xo.mapper.WebVisitMapper;
import com.moxi.mogublog.xo.service.WebVisitService;
import com.moxi.mougblog.base.global.BaseSysConf;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户访问记录表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Service
public class WebVisitServiceImpl extends SuperServiceImpl<WebVisitMapper, WebVisit> implements WebVisitService {

    @Autowired
    WebVisitMapper webVisitMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 不含年份的数组格式
        List<String> resultSevenDaysList = DateUtils.getDaysByN(7, "MM-dd");

        resultMap.put("date" , resultSevenDaysList);
        resultMap.put("pv" , pvList);
        resultMap.put("uv" , uvList);

        return resultMap;
    }

}
