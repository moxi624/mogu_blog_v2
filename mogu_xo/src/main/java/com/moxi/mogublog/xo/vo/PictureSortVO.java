package com.moxi.mogublog.xo.vo;

import com.moxi.mougblog.base.vo.BaseVO;
import lombok.Data;

/**
 * 相册分类实体类
 *
 * @author xuzhixiang
 * @date 2018年9月17日16:10:38
 */
@Data
public class PictureSortVO extends BaseVO<PictureSortVO> {

    /**
     * 父UID
     */
    private String parentUid;

    /**
     * 分类名
     */
    private String name;

    /**
     * 分类图片Uid
     */
    private String fileUid;

    /**
     * 排序字段，数值越大，越靠前
     */
    private int sort;
}
