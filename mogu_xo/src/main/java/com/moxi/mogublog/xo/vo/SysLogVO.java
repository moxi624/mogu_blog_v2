package com.moxi.mogublog.xo.vo;

import com.moxi.mougblog.base.vo.BaseVO;
import lombok.Data;


/**
 * <p>
 * SysLogVO
 * </p>
 *
 * @author 陌溪
 * @since 2020年4月7日11:46:30
 */
@Data
public class SysLogVO extends BaseVO<SysLogVO> {

    /**
     * 操作用户名
     */
    private String userName;

    /**
     * 操作人uid
     */
    private String adminUid;

    /**
     * 请求IP
     */
    private String ip;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方式 GET POST
     */
    private String type;

    /**
     * 请求类路径
     */
    private String classPath;

    /**
     * 方法名
     */
    private String method;

    /**
     * 参数
     */
    private String params;

    /**
     * 描述
     */
    private String operation;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 方法请求花费的时间，单位毫秒
     */
    private Long spendTime;

    /**
     * 方法请求花费的时间，(包含起始时间和结束)
     */
    private String spendTimeStr;

}
