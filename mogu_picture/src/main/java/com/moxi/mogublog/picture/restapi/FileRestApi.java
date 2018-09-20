package com.moxi.mogublog.picture.restapi;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.picture.entity.FileSort;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.utils.DateUtil;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.enums.EStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  图片上传RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-17
 */
@RestController
@RequestMapping("/file")
@Api(value="文件RestApi",tags={"FileRestApi"})
public class FileRestApi {
	

	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileSortService fileSortService;
	
	@Value(value="${data.image.url}")
	private String img_host;
	
	@Value(value="${file.upload.path}") //获取上传路径
	private String path;
	
	Logger log = Logger.getLogger(FileRestApi.class);
	/**
	 * 上传图片接口   传入 userId sysUserId ,有那个传哪个，记录是谁传的
	 * 	projectName 传入的项目名称如 cfw 默认是cfw 
	 * 	sortName 传入的模块名， 如 shop ，user ,等，不在数据库中记录的是不会上传的
	 * @param filedata
	 * @param response
	 * @param request
	 * @return file对象
	 */
	
	@ApiOperation(value="上传图片接口", notes="上传图片接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "filedata", value = "文件", required = true, dataType = "MultipartFile"),
			@ApiImplicitParam(name = "userId", value = "用户UID", required = false, dataType = "String"),
			@ApiImplicitParam(name = "sysUserId", value = "管理员UID", required = false, dataType = "String"),
			@ApiImplicitParam(name = "projectName", value = "项目名", required = false, dataType = "String"),
			@ApiImplicitParam(name = "sortName", value = "模块名", required = false, dataType = "String")
	})

	@GetMapping("/picture")
	public Object uploadPic(HttpServletResponse response, HttpServletRequest request, 
			@RequestParam("filedata")MultipartFile filedata) {
		//上传者id
		String userId = request.getParameter("userId");
		String sysUserId = request.getParameter("sysUserId");
		String projectName = request.getParameter("projectName");//项目名   
		String sortName = request.getParameter("sortName");//模块名
		
		//projectName现在默认cfw
		if(StringUtils.isEmpty(projectName)) {
			projectName = "cfw";
		}

		//不登录就不让上传，先不做要求
		/*if(userId>0) {//用户登录的，去数据不？？？？service不在一起不好取
			
		}else{
			if(sysUserId>0) {//
			}else {//没登录，不能上传
				msg = "请登录";
				return ResultUtil.result(SysConf.ERROR, msg);
			}
		}*/
		log.info("====fileSorts====" + projectName + " ###### " + sortName);
		QueryWrapper<FileSort> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(SQLConf.SORT_NAME, sortName);
		queryWrapper.eq(SQLConf.PROJECT_NAME, projectName);
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		List<FileSort> fileSorts = fileSortService.list(queryWrapper);	
				
		System.out.println("fileSorts"+JsonUtils.objectToJson(fileSorts));
		FileSort fileSort = null;
		if(fileSorts.size()>0) {
			fileSort = fileSorts.get(0);
			log.info("====fileSort===="+JsonUtils.objectToJson(fileSort));
		}
		
		if(fileSort!= null) {
			
		}else {
			return ResultUtil.result(SysConf.ERROR, "文件不被允许上传");
		}
		
		String sortUrl = fileSort.getUrl();
		//String 
		if(StringUtils.isEmpty(sortUrl)) {
			sortUrl = "cfw/common/";
		}else {
			sortUrl =fileSort.getUrl(); 
		}
		String oldName = filedata.getOriginalFilename();
		long size = filedata.getSize();
		//以前的文件名
		log.info("上传文件====："+oldName);
		//文件大小
    	log.info("文件大小====："+size);
    	//获取扩展名，默认是jpg
    	String picExpandedName =getPicExpandedName(oldName);
		//获取新文件名
    	String newFileName=String.valueOf(System.currentTimeMillis()+"."+picExpandedName);
		
    	//文件路径问题
    	log.info("path===="+path);
    	log.info("sortUrl===="+sortUrl);
    	
    	String newPath = path +sortUrl+"/"+picExpandedName+"/"+DateUtil.getYears()+"/"+DateUtil.getMonth()+"/" +DateUtil.getDay()+"/";
    	//path = path.replaceAll("//", "/");
    	
    	String picurl = sortUrl+"/"+picExpandedName+"/"+DateUtil.getYears()+"/"+DateUtil.getMonth()+"/" +DateUtil.getDay()+"/"+newFileName;
    	log.info("newPath===="+newPath);
    	
    	String saveUrl = newPath +newFileName;
    	
    	File file1=new File(newPath);
		  if(!file1.exists()){
			 file1.mkdirs();
		  }
    	
    	 try {
			File saveFile = new File(saveUrl); 
			saveFile.createNewFile();
			 filedata.transferTo(saveFile);
		} catch (Exception e) {
			log.info("==上传文件异常===url:"+saveUrl+"-----");
			e.printStackTrace();
			return ResultUtil.result(SysConf.ERROR, "文件上传失败");
		}
    	
    	 com.moxi.mogublog.picture.entity.File file = new com.moxi.mogublog.picture.entity.File();
    	
    	 file.setCreateTime(new Date(System.currentTimeMillis()));
    	 //file.setFileSortId(fileSort.getFileSortId());
    	 file.setFileSortId(1l);
    	 file.setFileOldName(oldName);
    	 file.setFileSize(size);
    	 file.setPicExpandedName(picExpandedName);
    	 file.setPicName(newFileName);
    	 file.setPicUrl(picurl);
    	 file.setStatus(1);
    	 file.setUserUid(userId);
    	 file.setAdminUid(sysUserId);

    	Boolean save = fileService.save(file);
    	if(save) {
    		return ResultUtil.result(SysConf.SUCCESS, file);	
    	} else {
    		return ResultUtil.result(SysConf.ERROR, "上传失败");
    	}
		
		
	}
	
	
	/**
	 * 获取后缀名
	 * @param fileName
	 * @return
	 */
	private static String getPicExpandedName(String fileName){
		String ext="";
		if(StringUtils.isNotBlank(fileName)&&
			StringUtils.contains(fileName, ".")){
				ext=StringUtils.substring(fileName, fileName.lastIndexOf(".")+1);
		}
		ext = ext.toLowerCase();
		if(ext==null || ext.length()<1)
			ext = "jpg";
			
		return ext;
	}

	
	/**
	 * 获取文件的信息接口
	 *  fileIds 获取文件信息的ids
	 *  code ids用什么分割的，默认“,”
	 * @param response
	 * @param request
	 * @return
	 */

	@RequestMapping("/getPicture")
	public Object getPicture(HttpServletResponse response,HttpServletRequest request ) {
		String fileIds = request.getParameter("fileIds");
		String code = request.getParameter("code");
		if(StringUtils.isEmpty(code)) {
			code = ",";
		}
		if(StringUtils.isEmpty(fileIds)) {
			return ResultUtil.result(SysConf.ERROR,"数据错误");
		}else {
			List<Map<String, Object>> list = new ArrayList<>();
			List<String> changeStringToString = StringUtils.changeStringToString(fileIds, code);
			QueryWrapper<com.moxi.mogublog.picture.entity.File> queryWrapper = new QueryWrapper<>();
			queryWrapper.in(SQLConf.UID, changeStringToString);
			List<com.moxi.mogublog.picture.entity.File> fileList = fileService.list(queryWrapper);	
			if(fileList.size()>0) {
				for(com.moxi.mogublog.picture.entity.File file : fileList) {						
					if(file!=null) {
						Map<String, Object> remap = new HashMap<>();
						remap.put("url", file.getPicUrl());//，地址
						remap.put("expandedName", file.getPicExpandedName());//后缀名，也就是类型
						remap.put("name", file.getPicName());//名称
						remap.put("fileId", file.getUid());
						list.add(remap);
					}	
				}					
			}
			return ResultUtil.result(SysConf.SUCCESS,list);
		}
	}
	

	/**
	 * 文件下载 
	 *   fileId ，
	 *   fileName  可以不写，不写下载的文件名就是上传的文件名
	 * @param request
	 * @param response
	 */

	@RequestMapping("downloadFile")
	public Object downloadFile( HttpServletRequest request ,HttpServletResponse response) {
		String fileId = request.getParameter("fileId");
		
		if(StringUtils.isEmpty(fileId)) {
			
			com.moxi.mogublog.picture.entity.File oneById = fileService.getById(fileId);
			if(oneById!=null) {
				String fileName = request.getParameter("fileName");//aim_test//文件名,不传就用默认的，或者是oldName
				if(StringUtils.isEmpty(fileName)) {
					fileName = oneById.getFileOldName();//以前的名字
				}
				
				String fileRealPath = path + oneById.getPicUrl();
				File file = new File(fileRealPath);
				response.setContentType("application/force-download"); //设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
					
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
					log.info("文件下载成功 realUrl:   "+fileRealPath);
					return ResultUtil.result(SysConf.SUCCESS, null);
				} catch (Exception e) {
					e.printStackTrace();
					log.info("============文件下载出现异常==========");
					return ResultUtil.result(SysConf.ERROR, "文件下载出现异常");
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		log.info("文件下载失败fileId=   "+fileId);
		return ResultUtil.result(SysConf.ERROR, "参数错误");
	}


	/**
	 * 多文件上传
	 * 上传图片接口   传入 userId sysUserId ,有那个传哪个，记录是谁传的,
	 * 	projectName 传入的项目名称如 cfw 默认是cfw 
	 * 	sortName 传入的模块名， 如 shop ，user ,等，不在数据库中记录的是不会上传的
	 * @param filedata
	 * @param response
	 * @param request
	 * @return 
	 */
	@RequestMapping("/pictures")
	public synchronized Object uploadPics(HttpServletResponse response,HttpServletRequest request, List<MultipartFile> filedatas) {
		//上传者id    
		String userId = request.getParameter("userId");
		String sysUserId = request.getParameter("sysUserId");
		String projectName = request.getParameter("projectName");//项目名   
		String sortName = request.getParameter("sortName");//模块名
		
		//projectName现在默认cfw
		if(StringUtils.isEmpty(projectName)) {
			projectName = "cfw";
		}
		
		//不登录就不让上传，先不做要求
		/*if(userId>0) {//用户登录的，去数据不？？？？service不在一起不好取
			
		}else{
			if(sysUserId>0) {//
			}else {//没登录，不能上传
				msg = "请登录";
				return ResultUtil.result(SysConf.ERROR, msg);
			}
		}*/
		log.info("####### fileSorts" + projectName + " ###### " + sortName);
		
		QueryWrapper<FileSort> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(SQLConf.SORT_NAME, sortName);
		queryWrapper.eq(SQLConf.PROJECT_NAME, projectName);
		queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
		List<FileSort> fileSorts = fileSortService.list(queryWrapper);	
		
		System.out.println("fileSorts"+JsonUtils.objectToJson(fileSorts));
		FileSort fileSort = null;
		if(fileSorts.size()>0) {
			fileSort = fileSorts.get(0);
			log.info("====fileSort===="+JsonUtils.objectToJson(fileSort));
		}
		
		if(fileSort!= null) {
			
		}else {
			return ResultUtil.result(SysConf.ERROR, "文件不被允许上传");
		}
		
		String sortUrl = fileSort.getUrl();
		//String 
		if(StringUtils.isEmpty(sortUrl)) {
			sortUrl = "cfw/common/";
		}else {
			sortUrl =fileSort.getUrl(); 
		}
		
		List<com.moxi.mogublog.picture.entity.File> lists = new ArrayList<>();
		//文件上传
		if(filedatas!=null&&filedatas.size()>0) {
			for(MultipartFile filedata:filedatas) {
				
				String oldName = filedata.getOriginalFilename();
				long size = filedata.getSize();
				//以前的文件名
				log.info("上传文件====：" + oldName);
				//文件大小
				log.info("文件大小====：" + size);
				//获取扩展名，默认是jpg
				String picExpandedName = getPicExpandedName(oldName);
				//获取新文件名
				String newFileName = String.valueOf(System.currentTimeMillis() + "." + picExpandedName);
				log.info(newFileName+":"+oldName);
				//文件路径问题
				log.info("path====" + path);
				log.info("sortUrl====" + sortUrl);
				String newPath = path + sortUrl + "/" + picExpandedName + "/" + DateUtil.getYears() + "/"
						+ DateUtil.getMonth() + "/" + DateUtil.getDay() + "/";
				//path = path.replaceAll("//", "/");
				String picurl = sortUrl + "/" + picExpandedName + "/" + DateUtil.getYears() + "/"
						+ DateUtil.getMonth() + "/" + DateUtil.getDay() + "/" + newFileName;
				log.info("newPath====" + newPath);
				String saveUrl = newPath + newFileName;
				File file1 = new File(newPath);
				if (!file1.exists()) {
					file1.mkdirs();
				}
				try {
					File saveFile = new File(saveUrl);
					saveFile.createNewFile();
					filedata.transferTo(saveFile);
				} catch (Exception e) {
					log.info("==上传文件异常===url:" + saveUrl + "-----");
					e.printStackTrace();
					return ResultUtil.result(SysConf.ERROR, "文件上传失败");
				}
				com.moxi.mogublog.picture.entity.File file = new com.moxi.mogublog.picture.entity.File();
				file.setCreateTime(new Date(System.currentTimeMillis()));
				//file.setFileSortId(fileSort.getFileSortId());
				file.setFileSortId(1l);
				file.setFileOldName(oldName);
				file.setFileSize(size);
				file.setPicExpandedName(picExpandedName);
				file.setPicName(newFileName);
				file.setPicUrl(picurl);
				file.setStatus(EStatus.ENABLE);
				file.setUserUid(userId);
				file.setAdminUid(sysUserId);
				fileService.save(file);//保存完的数据
				lists.add(file);		    	 
			}
			 //保存成功返回数据
			return ResultUtil.result(SysConf.SUCCESS, lists);
		}
		return ResultUtil.result(SysConf.ERROR, "请上传图片");
	}
}

