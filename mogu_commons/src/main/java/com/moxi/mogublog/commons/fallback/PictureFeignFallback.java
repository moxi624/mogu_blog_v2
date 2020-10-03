package com.moxi.mogublog.commons.fallback;

import com.moxi.mogublog.commons.feign.PictureFeignClient;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mougblog.base.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 图片服务兜底方法
 *
 * @author: 陌溪
 * @create: 2020-10-03-20:54
 */
@Component
@Slf4j
public class PictureFeignFallback implements PictureFeignClient {

    @Override
    public String getPicture(String fileIds, String code) {
        log.error("图片服务异常，获取图片失败，服务降级返回，传入参数：fileIds: {}", fileIds);
        return ResultUtil.result("error", "获取图片失败，服务降级返回");
    }

    @Override
    public String uploadPicsByUrl(FileVO fileVO) {
        log.error("图片服务异常，更新图片失败，服务降级返回，传入参数：fileVO: {}", fileVO.toString());
        return ResultUtil.result("error", "更新图片失败，服务降级返回");
    }
}
