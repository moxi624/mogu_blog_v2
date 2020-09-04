package com.moxi.mogublog.xo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moxi.mogublog.commons.entity.SubjectItem;
import com.moxi.mogublog.xo.vo.SubjectItemVO;
import com.moxi.mougblog.base.service.SuperService;

import java.util.List;

/**
 * <p>
 * 专题item表 服务类
 * </p>
 *
 * @author 陌溪
 * @since 2020年8月23日07:56:21
 */
public interface SubjectItemService extends SuperService<SubjectItem> {

    /**
     * 获取专题item列表
     *
     * @param subjectItemVO
     * @return
     */
    public IPage<SubjectItem> getPageList(SubjectItemVO subjectItemVO);

    /**
     * 批量新增专题
     *
     * @param subjectItemVOList
     */
    public String addSubjectItemList(List<SubjectItemVO> subjectItemVOList);

    /**
     * 编辑专题item
     *
     * @param subjectItemVOList
     */
    public String editSubjectItemList(List<SubjectItemVO> subjectItemVOList);

    /**
     * 批量删除专题item
     *
     * @param subjectItemVOList
     */
    public String deleteBatchSubjectItem(List<SubjectItemVO> subjectItemVOList);

}
