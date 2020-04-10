package com.moxi.mogublog.picture.restapi;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.picture.entity.FileSort;
import com.moxi.mogublog.commons.feign.AdminFeignClient;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.utils.*;
import com.moxi.mougblog.base.enums.EOpenStatus;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.vo.FileVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * <p>
 * 图片上传RestApi
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-17
 */
@RestController
@RequestMapping("/file")
@Api(value = "文件RestApi", tags = {"FileRestApi"})
@Slf4j
public class FileRestApi {

    @Autowired
    private FileService fileService;
    @Autowired
    private FileSortService fileSortService;
    @Value(value = "${data.image.url}")
    private String imgHost;

    //获取上传路径
    @Value(value = "${file.upload.path}")
    private String path;

    @Autowired
    AdminFeignClient adminFeignClient;

    @Autowired
    FeignUtil feignUtil;

    /**
     * 图片路径前缀
     */
    @Value(value = "${data.image.url}")
    private String imgURL;


    @ApiOperation(value = "Hello_Picture", notes = "Hello_Picture")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(String urlString, int i) throws IOException {

//        QueryWrapper<com.moxi.mogublog.picture.entity.File> queryWrapper = new QueryWrapper<>();
//        List<com.moxi.mogublog.picture.entity.File> fileList = fileService.list(queryWrapper);
//        fileList.forEach(item ->{
//            String str = item.getPicUrl();
//            String[] array = str.split("/");
//            item.setQiNiuUrl(array[array.length - 1]);
//        });
//        fileService.updateBatchById(fileList);
        return "hello";
    }

    @ApiOperation(value = "截图上传", notes = "截图上传")
    @RequestMapping(value = "/cropperPicture", method = RequestMethod.POST)
    public String cropperPicture( @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<MultipartFile> filedatas = new ArrayList<>();

        filedatas.add(file);

        String platform = request.getParameter(SysConf.PLATFORM);
        String token = request.getParameter(SysConf.TOKEN);

        // 获取七牛云配置文件
        Map<String, String> qiNiuResultMap = new HashMap<>();

        // 判断是否是web端发送过来的请求
        if(SysConf.WEB.equals(platform)) {
            // 如果是调用web端获取配置的接口
            qiNiuResultMap = feignUtil.getQiNiuConfigByWebToken(token);
        } else {
            // 调用admin端获取配置接口
            qiNiuResultMap = feignUtil.getQiNiuConfig(token);
        }

        // 七牛云配置
        Map<String, String> qiNiuConfig = new HashMap<>();

        String uploadQiNiu = "";
        String uploadLocal = "";
        String localPictureBaseUrl = "";
        String qiNiuPictureBaseUrl = "";

        String qiNiuAccessKey = "";
        String qiNiuSecretKey = "";
        String qiNiuBucket = "";
        String qiNiuArea = "";
        String picturePriority = "";

        if(qiNiuConfig == null) {
            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
        } else {

            uploadQiNiu = qiNiuResultMap.get("uploadQiNiu");
            uploadLocal = qiNiuResultMap.get("uploadLocal");
            localPictureBaseUrl = qiNiuResultMap.get("localPictureBaseUrl");
            qiNiuPictureBaseUrl = qiNiuResultMap.get("qiNiuPictureBaseUrl");

            qiNiuAccessKey = qiNiuResultMap.get("qiNiuAccessKey");
            qiNiuSecretKey = qiNiuResultMap.get("qiNiuSecretKey");
            qiNiuBucket = qiNiuResultMap.get("qiNiuBucket");
            qiNiuArea = qiNiuResultMap.get("qiNiuArea");
            picturePriority = qiNiuResultMap.get("picturePriority");

            if("1".equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                    || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
                return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
            }

            if("1".equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
                return ResultUtil.result(SysConf.ERROR, "请先配置本地图片域名");
            }

            qiNiuConfig.put("qiNiuAccessKey", qiNiuAccessKey);
            qiNiuConfig.put("qiNiuSecretKey", qiNiuSecretKey);
            qiNiuConfig.put("qiNiuBucket", qiNiuBucket);
            qiNiuConfig.put("qiNiuArea", qiNiuArea);
            qiNiuConfig.put("uploadQiNiu", uploadQiNiu);
            qiNiuConfig.put("uploadLocal", uploadLocal);
        }

        String result = fileService.uploadImgs(path, request, filedatas, qiNiuConfig);

        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> picMap = (Map<String, Object>) JsonUtils.jsonToObject(result, Map.class);
        if ("success".equals(picMap.get("code"))) {
            List<Map<String, Object>> picData = (List<Map<String, Object>>) picMap.get("data");
            if (picData.size() > 0) {
                for (int i = 0; i < picData.size(); i++) {
                    Map<String, Object> item = new HashMap<>();

                    item.put(SysConf.UID,  picData.get(i).get(SysConf.UID));

                    if ("1".equals(picturePriority)) {
                        item.put(SysConf.URL,  qiNiuPictureBaseUrl + picData.get(i).get(SysConf.PIC_URL));
                    } else {
                        item.put(SysConf.URL,  localPictureBaseUrl + picData.get(i).get(SysConf.PIC_URL));
                    }
                    listMap.add(item);
                }
            }
        }

        return ResultUtil.result(SysConf.SUCCESS, listMap);
    }


    /**
     * 获取文件的信息接口
     * fileIds 获取文件信息的ids
     * code ids用什么分割的，默认“,”
     * @return
     */

    @ApiOperation(value = "通过fileIds获取图片信息接口", notes = "获取图片信息接口")
    @GetMapping("/getPicture")
    public String getPicture(
            @ApiParam(name = "fileIds", value = "文件ids", required = false) @RequestParam(name = "fileIds", required = false) String fileIds,
            @ApiParam(name = "code", value = "切割符", required = false) @RequestParam(name = "code", required = false) String code) {

        if (StringUtils.isEmpty(code)) {
            code = ",";
        }
        if (StringUtils.isEmpty(fileIds)) {
            return ResultUtil.result(SysConf.ERROR, "数据错误");
        } else {
            List<Map<String, Object>> list = new ArrayList<>();
            List<String> changeStringToString = StringUtils.changeStringToString(fileIds, code);
            QueryWrapper<com.moxi.mogublog.picture.entity.File> queryWrapper = new QueryWrapper<>();
            queryWrapper.in(SQLConf.UID, changeStringToString);
            List<com.moxi.mogublog.picture.entity.File> fileList = fileService.list(queryWrapper);
            if (fileList.size() > 0) {
                for (com.moxi.mogublog.picture.entity.File file : fileList) {
                    if (file != null) {
                        Map<String, Object> remap = new HashMap<>();

                        // 获取七牛云地址
                        remap.put(SysConf.QI_NIU_URL, file.getQiNiuUrl());

                        // 获取本地地址
                        remap.put(SysConf.URL, file.getPicUrl());

                        // 后缀名，也就是类型
                        remap.put(SysConf.EXPANDED_NAME, file.getPicExpandedName());

                        remap.put(SysConf.FILE_OLD_NAME, file.getFileOldName());

                        //名称
                        remap.put(SysConf.NAME, file.getPicName());
                        remap.put(SysConf.UID, file.getUid());
                        remap.put("file_old_name", file.getFileOldName());
                        list.add(remap);
                    }
                }
            }
            return ResultUtil.result(SysConf.SUCCESS, list);
        }
    }


    /**
     * 文件下载
     * fileId ，
     * fileName  可以不写，不写下载的文件名就是上传的文件名
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "图片下载接口", notes = "图片下载接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileIds", value = "fileIds", required = false, dataType = "String")
    })
    @GetMapping("downloadFile")
    public Object downloadFile(HttpServletRequest request, HttpServletResponse response) {

        String fileId = request.getParameter("fileId");

        if (StringUtils.isEmpty(fileId)) {

            com.moxi.mogublog.picture.entity.File oneById = fileService.getById(fileId);
            if (oneById != null) {
                //aim_test//文件名,不传就用默认的，或者是oldName
                String fileName = request.getParameter("fileName");

                if (StringUtils.isEmpty(fileName)) {
                    //以前的名字
                    fileName = oneById.getFileOldName();
                }

                String fileRealPath = path + oneById.getPicUrl();

                File file = new File(fileRealPath);

                //设置强制下载不打开
                response.setContentType("application/force-download");

                // 设置文件名
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

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
                    log.info("文件下载成功 realUrl:   " + fileRealPath);
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
        log.info("文件下载失败fileId=   " + fileId);
        return ResultUtil.result(SysConf.ERROR, "参数错误");
    }


    /**
     * 多文件上传
     * 上传图片接口   传入 userId sysUserId ,有那个传哪个，记录是谁传的,
     * projectName 传入的项目名称如 base 默认是base
     * sortName 传入的模块名， 如 admin，user ,等，不在数据库中记录的是不会上传的
     *
     * @return
     */
    @ApiOperation(value = "多图片上传接口", notes = "多图片上传接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filedatas", value = "文件数据", required = true),
            @ApiImplicitParam(name = "userUid", value = "用户UID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sysUserId", value = "管理员UID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "projectName", value = "项目名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sortName", value = "模块名", required = false, dataType = "String")
    })
    @PostMapping("/pictures")
    public synchronized Object uploadPics(HttpServletRequest request, List<MultipartFile> filedatas) {

        String token = request.getParameter(SysConf.TOKEN);

        // 获取七牛云配置文件
        Map<String, String> qiNiuResultMap = feignUtil.getQiNiuConfig(token);

        // 七牛云配置
        Map<String, String> qiNiuConfig = new HashMap<>();

        String uploadQiNiu = "";
        String uploadLocal = "";
        String localPictureBaseUrl = "";
        String qiNiuPictureBaseUrl = "";

        String qiNiuAccessKey = "";
        String qiNiuSecretKey = "";
        String qiNiuBucket = "";
        String qiNiuArea = "";
        String picturePriority = "";

        if(qiNiuConfig == null) {
            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
        } else {

            uploadQiNiu = qiNiuResultMap.get("uploadQiNiu");
            uploadLocal = qiNiuResultMap.get("uploadLocal");
            localPictureBaseUrl = qiNiuResultMap.get("localPictureBaseUrl");
            qiNiuPictureBaseUrl = qiNiuResultMap.get("qiNiuPictureBaseUrl");

            qiNiuAccessKey = qiNiuResultMap.get("qiNiuAccessKey");
            qiNiuSecretKey = qiNiuResultMap.get("qiNiuSecretKey");
            qiNiuBucket = qiNiuResultMap.get("qiNiuBucket");
            qiNiuArea = qiNiuResultMap.get("qiNiuArea");
            picturePriority = qiNiuResultMap.get("picturePriority");

            if("1".equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                    || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
                return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
            }

            if("1".equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
                return ResultUtil.result(SysConf.ERROR, "请先配置本地图片域名");
            }

            qiNiuConfig.put("qiNiuAccessKey", qiNiuAccessKey);
            qiNiuConfig.put("qiNiuSecretKey", qiNiuSecretKey);
            qiNiuConfig.put("qiNiuBucket", qiNiuBucket);
            qiNiuConfig.put("qiNiuArea", qiNiuArea);
            qiNiuConfig.put("uploadQiNiu", uploadQiNiu);
            qiNiuConfig.put("uploadLocal", uploadLocal);
        }

       return fileService.uploadImgs(path, request, filedatas, qiNiuConfig);
    }

    /**
     * 通过URL将图片上传到自己服务器中
     * @param fileVO
     * @param result
     * @return
     */
    @ApiOperation(value = "通过URL上传图片", notes = "通过URL上传图片")
    @PostMapping("/uploadPicsByUrl")
    public synchronized Object uploadPicsByUrl(HttpServletRequest request, @Validated({GetList.class}) @RequestBody FileVO fileVO, BindingResult result) {

        String token = request.getParameter(SysConf.TOKEN);

        // 获取七牛云配置文件
        Map<String, String> qiNiuResultMap = feignUtil.getQiNiuConfig(token);

        // 七牛云配置
        Map<String, String> qiNiuConfig = new HashMap<>();

        String uploadQiNiu = "";
        String uploadLocal = "";
        String localPictureBaseUrl = "";
        String qiNiuPictureBaseUrl = "";

        String qiNiuAccessKey = "";
        String qiNiuSecretKey = "";
        String qiNiuBucket = "";
        String qiNiuArea = "";
        String picturePriority = "";

        if(qiNiuConfig == null) {
            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
        } else {

            uploadQiNiu = qiNiuResultMap.get("uploadQiNiu");
            uploadLocal = qiNiuResultMap.get("uploadLocal");
            localPictureBaseUrl = qiNiuResultMap.get("localPictureBaseUrl");
            qiNiuPictureBaseUrl = qiNiuResultMap.get("qiNiuPictureBaseUrl");

            qiNiuAccessKey = qiNiuResultMap.get("qiNiuAccessKey");
            qiNiuSecretKey = qiNiuResultMap.get("qiNiuSecretKey");
            qiNiuBucket = qiNiuResultMap.get("qiNiuBucket");
            qiNiuArea = qiNiuResultMap.get("qiNiuArea");
            picturePriority = qiNiuResultMap.get("picturePriority");

            if("1".equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                    || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
                return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
            }

            if("1".equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
                return ResultUtil.result(SysConf.ERROR, "请先配置本地图片域名");
            }

            qiNiuConfig.put("qiNiuAccessKey", qiNiuAccessKey);
            qiNiuConfig.put("qiNiuSecretKey", qiNiuSecretKey);
            qiNiuConfig.put("qiNiuBucket", qiNiuBucket);
            qiNiuConfig.put("qiNiuArea", qiNiuArea);
            qiNiuConfig.put("uploadQiNiu", uploadQiNiu);
            qiNiuConfig.put("uploadLocal", uploadLocal);
        }

        String userUid = fileVO.getUserUid();
        String adminUid = fileVO.getAdminUid();
        String projectName = fileVO.getProjectName();
        String sortName = fileVO.getSortName();
        List<String> urlList = fileVO.getUrlList();

        //projectName现在默认base
        if (StringUtils.isEmpty(projectName)) {
            projectName = "base";
        }

        //这里可以检测用户上传，如果不是网站的用户或会员就不能调用
        if (StringUtils.isEmpty(userUid) && StringUtils.isEmpty(adminUid)) {
            return ResultUtil.result(SysConf.ERROR, "请先注册");
        } else {

        }

        log.info("####### fileSorts" + projectName + " ###### " + sortName);

        QueryWrapper<FileSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.SORT_NAME, sortName);
        queryWrapper.eq(SQLConf.PROJECT_NAME, projectName);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        List<FileSort> fileSorts = fileSortService.list(queryWrapper);

        System.out.println("fileSorts" + JsonUtils.objectToJson(fileSorts));

        FileSort fileSort = null;
        if (fileSorts.size() > 0) {
            fileSort = fileSorts.get(0);
            log.info("====fileSort====" + JsonUtils.objectToJson(fileSort));
        } else {
            return ResultUtil.result(SysConf.ERROR, "文件不被允许上传");
        }

        String sortUrl = fileSort.getUrl();

        //判断url是否为空，如果为空，使用默认
        if (StringUtils.isEmpty(sortUrl)) {
            sortUrl = "base/common/";
        } else {
            sortUrl = fileSort.getUrl();
        }

        List<com.moxi.mogublog.picture.entity.File> lists = new ArrayList<>();

        //文件上传
        if (urlList != null && urlList.size() > 0) {
            for (String itemUrl : urlList) {

                //获取新文件名(默认为jpg)
                String newFileName = String.valueOf(System.currentTimeMillis() + ".jpg");

                //文件绝对路径
                String newPath = path + sortUrl + "/jpg/" + DateUtils.getYears() + "/"
                        + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/";
                //文件相对路径
                String picurl = sortUrl + "/jpg/" + DateUtils.getYears() + "/"
                        + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/" + newFileName;
                String saveUrl = newPath + newFileName;
                log.info("newPath:" + newPath);
                log.info("saveUrl:" + saveUrl);

                // 将图片上传到本地服务器中以及七牛云中
                BufferedOutputStream out = null;
                QiniuUtil qn = new QiniuUtil();
                String qiNiuUrl = "";
                List<String> list = new ArrayList<>();
                java.io.File dest= null;

                FileOutputStream os = null;

                // 输入流
                InputStream inputStream = null;

                // 判断是否能够上传至本地
                if("1".equals(uploadLocal)) {
                    // 判断文件是否存在
                    File file1 = new File(newPath);
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    try {
                        // 构造URL
                        URL url = new URL(itemUrl);

                        // 打开连接
                        URLConnection con = url.openConnection();

                        // 设置10秒
                        con.setConnectTimeout(10000);
                        con.setReadTimeout(10000);

                        // 当获取的相片无法正常显示的时候，需要给一个默认图片
                        inputStream = con.getInputStream();

                        // 1K的数据缓冲
                        byte[] bs = new byte[1024];
                        // 读取到的数据长度
                        int len;

                        File file = new File(saveUrl);
                        os = new FileOutputStream(file, true);
                        // 开始读取
                        while ((len = inputStream.read(bs)) != -1) {
                            os.write(bs, 0, len);
                        }


                    } catch (Exception e) {
                        log.info("==上传文件异常===url:" + saveUrl + "-----");
                        e.printStackTrace();
                        return ResultUtil.result(SysConf.ERROR, "获取图片超时，文件上传失败");
                    } finally {
                        try {
                            // 完毕，关闭所有链接
                            os.close();
                            inputStream.close();
                        }catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                }

                // 上传七牛云，判断是否能够上传七牛云
                if("1".equals(uploadQiNiu)) {
                    try {
                        File pdfFile = new File(saveUrl);
                        FileInputStream fileInputStream = new FileInputStream(pdfFile);
                        MultipartFile fileData = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
                                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                        // 上传七牛云
                        // 创建一个临时目录文件
                        String tempFiles = "temp/" + newFileName;
                        dest = new java.io.File(tempFiles);
                        if (!dest.getParentFile().exists()) {
                            dest.getParentFile().mkdirs();
                        }
                        out = new BufferedOutputStream(new FileOutputStream(dest));
                        out.write(fileData.getBytes());
                        qn = new QiniuUtil();

                        // TODO 不关闭流，小图片就无法显示？
                        out.flush();
                        out.close();

                        qiNiuUrl = qn.uploadQiniu(dest, qiNiuConfig);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
                    } finally {
                        if (dest != null && dest.getParentFile().exists()) {
                            dest.delete();
                        }
                    }
                }


                com.moxi.mogublog.picture.entity.File file = new com.moxi.mogublog.picture.entity.File();

                file.setCreateTime(new Date(System.currentTimeMillis()));
                file.setFileSortUid(fileSort.getUid());
                file.setFileOldName(itemUrl);
                file.setFileSize(0L);
                file.setPicExpandedName("jpg");
                file.setPicName(newFileName);

                String url = "";

                // 设置本地图片
                file.setPicUrl(localPictureBaseUrl + picurl);

                // 设置七牛云图片
                file.setQiNiuUrl(qiNiuPictureBaseUrl + qiNiuUrl);

                file.setStatus(EStatus.ENABLE);
                file.setUserUid(userUid);
                file.setAdminUid(adminUid);
                fileService.save(file);
                lists.add(file);
            }
            //保存成功返回数据
            return ResultUtil.result(SysConf.SUCCESS, lists);
        }
        return ResultUtil.result(SysConf.ERROR, "请上传图片");
    }


    /**
     * 通过URL将图片上传到自己服务器中（用于Github和Gitee的头像上传）
     * @param fileVO
     * @param result
     * @return
     */
    @ApiOperation(value = "通过URL上传图片", notes = "通过URL上传图片")
    @PostMapping("/uploadPicsByUrl2")
    public Object uploadPicsByUrl2(@Validated({GetList.class}) @RequestBody FileVO fileVO, BindingResult result) {

        Map<String, Object> resultMap = fileVO.getSystemConfig();
        String uploadQiNiu = resultMap.get(SQLConf.UPLOAD_QI_NIU).toString();
        String uploadLocal = resultMap.get(SQLConf.UPLOAD_LOCAL).toString();
        String localPictureBaseUrl = resultMap.get(SQLConf.LOCAL_PICTURE_BASE_URL).toString();
        String qiNiuPictureBaseUrl = resultMap.get(SQLConf.QI_NIU_PICTURE_BASE_URL).toString();
        String qiNiuAccessKey = resultMap.get(SQLConf.QI_NIU_ACCESS_KEY).toString();
        String qiNiuSecretKey = resultMap.get(SQLConf.QI_NIU_SECRET_KEY).toString();
        String qiNiuBucket = resultMap.get(SQLConf.QI_NIU_BUCKET).toString();
        String qiNiuArea = resultMap.get(SQLConf.QI_NIU_AREA).toString();

        if(EOpenStatus.OPEN.equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
        }

        if(EOpenStatus.OPEN.equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
            return ResultUtil.result(SysConf.ERROR, "请先配置本地图片域名");
        }

        // 七牛云配置
        Map<String, String> qiNiuConfig = new HashMap<>();
        qiNiuConfig.put(SysConf.QI_NIU_ACCESS_KEY, qiNiuAccessKey);
        qiNiuConfig.put(SysConf.QI_NIU_SECRET_KEY, qiNiuSecretKey);
        qiNiuConfig.put(SysConf.QI_NIU_BUCKET, qiNiuBucket);
        qiNiuConfig.put(SysConf.QI_NIU_AREA, qiNiuArea);
        qiNiuConfig.put(SysConf.UPLOAD_QI_NIU, uploadQiNiu);
        qiNiuConfig.put(SysConf.UPLOAD_LOCAL, uploadLocal);

        String userUid = fileVO.getUserUid();
        String adminUid = fileVO.getAdminUid();
        String projectName = fileVO.getProjectName();
        String sortName = fileVO.getSortName();
        List<String> urlList = fileVO.getUrlList();

        //projectName现在默认base
        if (StringUtils.isEmpty(projectName)) {
            projectName = "base";
        }

        //这里可以检测用户上传，如果不是网站的用户或会员就不能调用
        if (StringUtils.isEmpty(userUid) && StringUtils.isEmpty(adminUid)) {
            return ResultUtil.result(SysConf.ERROR, "请先注册");
        } else {

        }

        log.info("####### fileSorts" + projectName + " ###### " + sortName);

        QueryWrapper<FileSort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.SORT_NAME, sortName);
        queryWrapper.eq(SQLConf.PROJECT_NAME, projectName);
        queryWrapper.eq(SQLConf.STATUS, EStatus.ENABLE);
        List<FileSort> fileSorts = fileSortService.list(queryWrapper);

        System.out.println("fileSorts" + JsonUtils.objectToJson(fileSorts));

        FileSort fileSort = null;
        if (fileSorts.size() > 0) {
            fileSort = fileSorts.get(0);
            log.info("====fileSort====" + JsonUtils.objectToJson(fileSort));
        } else {
            return ResultUtil.result(SysConf.ERROR, "文件不被允许上传");
        }

        String sortUrl = fileSort.getUrl();

        //判断url是否为空，如果为空，使用默认
        if (StringUtils.isEmpty(sortUrl)) {
            sortUrl = "base/common/";
        } else {
            sortUrl = fileSort.getUrl();
        }

        List<com.moxi.mogublog.picture.entity.File> lists = new ArrayList<>();

        //文件上传
        if (urlList != null && urlList.size() > 0) {
            for (String itemUrl : urlList) {

                //获取新文件名(默认为jpg)
                String newFileName = String.valueOf(System.currentTimeMillis() + ".jpg");

                //文件绝对路径
                String newPath = path + sortUrl + "/jpg/" + DateUtils.getYears() + "/"
                        + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/";
                //文件相对路径
                String picurl = sortUrl + "/jpg/" + DateUtils.getYears() + "/"
                        + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/" + newFileName;
                String saveUrl = newPath + newFileName;
                log.info("newPath:" + newPath);
                log.info("saveUrl:" + saveUrl);

                // 将图片上传到本地服务器中以及七牛云中
                BufferedOutputStream out = null;
                QiniuUtil qn = new QiniuUtil();
                String qiNiuUrl = "";
                List<String> list = new ArrayList<>();
                java.io.File dest= null;

                FileOutputStream os = null;

                // 输入流
                InputStream inputStream = null;

                // 判断是否能够上传至本地
                if("1".equals(uploadLocal)) {
                    // 判断文件是否存在
                    File file1 = new File(newPath);
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    try {
                        // 构造URL
                        URL url = new URL(itemUrl);

                        // 打开连接
                        URLConnection con = url.openConnection();

                        // 设置用户代理
                        con.setRequestProperty("User-agent", "	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");

                        // 设置10秒
                        con.setConnectTimeout(10000);
                        con.setReadTimeout(10000);

                        // 当获取的相片无法正常显示的时候，需要给一个默认图片
                        inputStream = con.getInputStream();

                        // 1K的数据缓冲
                        byte[] bs = new byte[1024];
                        // 读取到的数据长度
                        int len;

                        File file = new File(saveUrl);
                        os = new FileOutputStream(file, true);
                        // 开始读取
                        while ((len = inputStream.read(bs)) != -1) {
                            os.write(bs, 0, len);
                        }


                    } catch (Exception e) {
                        log.info("==上传文件异常===url:" + saveUrl + "-----");
                        e.printStackTrace();
                        return ResultUtil.result(SysConf.ERROR, "获取图片超时，文件上传失败");
                    } finally {
                        try {
                            // 完毕，关闭所有链接
                            os.close();
                            inputStream.close();
                        }catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                }

                // 上传七牛云，判断是否能够上传七牛云
                if("1".equals(uploadQiNiu)) {
                    try {
                        File pdfFile = new File(saveUrl);
                        FileInputStream fileInputStream = new FileInputStream(pdfFile);
                        MultipartFile fileData = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
                                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);

                        // 上传七牛云
                        // 创建一个临时目录文件
                        String tempFiles = "temp/" + newFileName;
                        dest = new java.io.File(tempFiles);
                        if (!dest.getParentFile().exists()) {
                            dest.getParentFile().mkdirs();
                        }
                        out = new BufferedOutputStream(new FileOutputStream(dest));
                        out.write(fileData.getBytes());
                        qn = new QiniuUtil();

                        // TODO 不关闭流，小图片就无法显示？
                        out.flush();
                        out.close();

                        qiNiuUrl = qn.uploadQiniu(dest, qiNiuConfig);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
                    } finally {
                        if (dest != null && dest.getParentFile().exists()) {
                            dest.delete();
                        }
                    }
                }


                com.moxi.mogublog.picture.entity.File file = new com.moxi.mogublog.picture.entity.File();

                file.setCreateTime(new Date(System.currentTimeMillis()));
                file.setFileSortUid(fileSort.getUid());
                file.setFileOldName(itemUrl);
                file.setFileSize(0L);
                file.setPicExpandedName("jpg");
                file.setPicName(newFileName);

                String url = "";

                // 设置本地图片
                file.setPicUrl(picurl);

                // 设置七牛云图片
                file.setQiNiuUrl(qiNiuUrl);

                file.setStatus(EStatus.ENABLE);
                file.setUserUid(userUid);
                file.setAdminUid(adminUid);
                fileService.save(file);
                lists.add(file);
            }
            //保存成功返回数据
            return ResultUtil.result(SysConf.SUCCESS, lists);
        }
        return ResultUtil.result(SysConf.ERROR, "请上传图片");
    }

}

