package com.moxi.mogublog.admin.restapi;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.admin.log.OperationLogger;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.utils.WebUtils;
import com.moxi.mogublog.xo.entity.WebConfig;
import com.moxi.mogublog.xo.service.WebConfigService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 网站配置表 RestApi
 * </p>
 *
 * @author xzx19950624@qq.com
 * @since 2018年11月11日15:19:28
 */
//@PreAuthorize("hasRole('Administrator')")
@Api(value="系统配置RestApi",tags={"WebConfigRestApi"})
@RestController
@RequestMapping("/webConfig")
public class WebConfigRestApi {
		
	@Autowired
	WebConfigService webConfigService;
	
	@Autowired
	private PictureFeignClient pictureFeignClient;
	
	@ApiOperation(value="获取网站配置", notes="获取网站配置")
	@GetMapping("/getWebConfig")
	public String getWebConfig(HttpServletRequest request) {

		QueryWrapper<WebConfig> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc(SQLConf.CREATE_TIME);
		WebConfig webConfig = webConfigService.getOne(queryWrapper);
		
		//获取图片
		if(webConfig !=null && StringUtils.isNotEmpty(webConfig.getLogo())) {
			String pictureList = this.pictureFeignClient.getPicture(webConfig.getLogo(), ",");
			webConfig.setPhotoList(WebUtils.getPicture(pictureList));
		}
		
		//获取支付宝收款二维码
		if(webConfig != null && StringUtils.isNotEmpty(webConfig.getAliPay())) {
			String pictureList = this.pictureFeignClient.getPicture(webConfig.getAliPay(), ",");
			if(WebUtils.getPicture(pictureList).size() > 0) {
				webConfig.setAliPayPhoto(WebUtils.getPicture(pictureList).get(0));	
			}
			
		}
		//获取微信收款二维码
		if(webConfig != null && StringUtils.isNotEmpty(webConfig.getWeixinPay())) {
			String pictureList = this.pictureFeignClient.getPicture(webConfig.getWeixinPay(), ",");
			if(WebUtils.getPicture(pictureList).size() > 0) {
				webConfig.setWeixinPayPhoto(WebUtils.getPicture(pictureList).get(0));	
			}
			
		}
		
		return ResultUtil.result(SysConf.SUCCESS, webConfig);		
	}
	
	@OperationLogger(value="修改网站配置")
	@ApiOperation(value="修改网站配置", notes="修改网站配置")
	@PostMapping("/editWebConfig")
	public String editWebConfig(HttpServletRequest request, @RequestBody WebConfig webConfig) {
		
		if(StringUtils.isEmpty(webConfig.getUid())) {
			webConfigService.save(webConfig);
		} else {
			webConfigService.updateById(webConfig);	
		}				
		return ResultUtil.result(SysConf.SUCCESS, "更新成功");		
	}
}

