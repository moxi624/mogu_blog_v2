package com.moxi.mogublog.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.moxi.mogublog.admin.global.WebConf;
import com.moxi.mogublog.utils.FileUtils;
/**
 * Ueditor后台配置
 * @author xuzhixiang
 * @date 2017年12月26日10:26:26
 *
 */
@Controller
public class UeditorController {
	
	@RequestMapping(value = {"/uploadimage"}, method = RequestMethod.POST)
	@ResponseBody
	public Object uploadimage(@RequestParam(value="upfile", required=false) MultipartFile photo) throws IllegalStateException, IOException{
		Map<String, Object> param = new HashMap<String, Object>();
		if(photo != null) {
			String path = FileUtils.uploadFile(WebConf.MoguDateUrl, WebConf.BlogImgDataUrl, photo);
			System.out.println(path);
			param.put("state", "SUCCESS");
			param.put("url", WebConf.ImgBaseUrl + path);
			param.put("size", photo.getSize());
			param.put("title", photo.getName());
			param.put("type", photo.getContentType());
			param.put("original", photo.getOriginalFilename());			
			
		} else 
		{
			System.out.println("photo 不能为空");
			param.put("state", "FAILER");
		}
		return param;
	}
	

}
