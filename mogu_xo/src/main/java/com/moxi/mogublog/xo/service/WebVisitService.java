package com.moxi.mogublog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.mogublog.commons.entity.WebVisit;
import com.moxi.mogublog.xo.vo.WebVisitVO;
import com.moxi.mougblog.base.service.SuperService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Web访问记录 服务类
 *
 * @author 陌溪
 * @date 2018年12月8日09:42:05
 */
public interface WebVisitService extends SuperService<WebVisit> {

    /**
     * 增加访问记录（异步接口）
     *
     * @param userUid
     * @param request
     * @param behavior
     * @param moduleUid
     * @param otherData
     */
    public void addWebVisit(String userUid, HttpServletRequest request, String behavior, String moduleUid, String otherData);

    /**
     * 获取今日网站访问人数
     *
     * @return
     */
    public int getWebVisitCount();

    /**
     * 获取近七天的访问量
     *
     * @return {
     * date: ["2019-6-20","2019-6-21","2019-6-22","2019-6-23","2019-6-24",,"2019-6-25","2019-6-26"]
     * pv: [10,5,6,7,5,3,2]
     * uv: [5,3,4,4,5,2,1]
     * }
     * 注：PV表示访问量   UV表示独立用户数
     */
    public Map<String, Object> getVisitByWeek();

    /**
     * 获取访问列表
     *
     * @param webVisitVO
     * @return
     */
    public IPage<WebVisit> getPageList(WebVisitVO webVisitVO);
}
