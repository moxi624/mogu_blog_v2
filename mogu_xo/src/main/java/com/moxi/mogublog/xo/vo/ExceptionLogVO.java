package com.moxi.mogublog.xo.vo;

import com.moxi.mougblog.base.vo.BaseVO;
import lombok.Data;

/**
 * <p>
 * ExceptionLogVO
 * </p>
 *
 * @author 陌溪
 * @since 2020年4月7日11:45:40
 */
@Data
public class ExceptionLogVO extends BaseVO<ExceptionLogVO> {

    /**
     * 操作IP
     */
    private String ip;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 描述
     */
    private String operation;

    /**
     * 参数
     */
    private String params;

    /**
     * 异常对象json格式
     */
    private String exceptionJson;

    /**
     * 异常简单信息,等同于e.getMessage
     */
    private String exceptionMessage;

    /**
     * 开始时间
     */
    private String startTime;
}
