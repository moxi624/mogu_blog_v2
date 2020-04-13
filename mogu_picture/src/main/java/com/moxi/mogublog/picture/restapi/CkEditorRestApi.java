package com.moxi.mogublog.picture.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.picture.entity.FileSort;
import com.moxi.mogublog.commons.feign.AdminFeignClient;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.picture.util.Aboutfile;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.utils.*;
import com.moxi.mougblog.base.enums.EStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/ckeditor")
@Slf4j
public class CkEditorRestApi {

    /**
     * 获取基本路径
     */
    @Value(value = "${file.upload.path}")
    private String basePath;

    /**
     * 图片路径前缀
     */
    @Value(value = "${data.image.url}")
    private String imgURL;

    /**
     * 图像存放路径
     * 图像存放路径
     */
    private String uploadImageUrl = "ckEditorUploadImg";

    /**
     * 文件存放路径
     */
    private String uploadFileUrl = "ckEditorUploadFile";


    //获取上传路径
    @Value(value = "${file.upload.path}")
    private String path;

    @Autowired
    FileSortService fileSortService;

    @Autowired
    FileService fileService;

    @Autowired
    AdminFeignClient adminFeignClient;

    @Autowired
    FeignUtil feignUtil;

    /**
     * 图像中的图片上传
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/imgUpload", method = RequestMethod.POST)
    public Object imgUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String token = request.getParameter(SysConf.TOKEN);

        // 从Redis中获取七牛云配置文件
        Map<String, String> qiNiuResultMap = feignUtil.getQiNiuConfig(token);

        // 七牛云配置
        Map<String, String> qiNiuConfig = new HashMap<>();

        if(qiNiuConfig == null) {
            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
        } else {
            String uploadQiNiu = qiNiuResultMap.get(SysConf.UPLOAD_QI_NIU);
            String uploadLocal = qiNiuResultMap.get(SysConf.UPLOAD_LOCAL);
            String localPictureBaseUrl = qiNiuResultMap.get(SysConf.LOCAL_PICTURE_BASE_URL);
            String qiNiuPictureBaseUrl = qiNiuResultMap.get(SysConf.QI_NIU_PICTURE_BASE_URL);
            String qiNiuAccessKey = qiNiuResultMap.get(SysConf.QI_NIU_ACCESS_KEY);
            String qiNiuSecretKey = qiNiuResultMap.get(SysConf.QI_NIU_SECRET_KEY);
            String qiNiuBucket = qiNiuResultMap.get(SysConf.QI_NIU_BUCKET);
            String qiNiuArea = qiNiuResultMap.get(SysConf.QI_NIU_AREA);

            if("1".equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                    || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
                return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
            }

            if("1".equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
                return ResultUtil.result(SysConf.ERROR, "请先配置本地图片域名");
            }

            qiNiuConfig.put(SysConf.QI_NIU_ACCESS_KEY, qiNiuAccessKey);
            qiNiuConfig.put(SysConf.QI_NIU_SECRET_KEY, qiNiuSecretKey);
            qiNiuConfig.put(SysConf.QI_NIU_BUCKET, qiNiuBucket);
            qiNiuConfig.put(SysConf.QI_NIU_AREA, qiNiuArea);
            qiNiuConfig.put(SysConf.UPLOAD_QI_NIU, uploadQiNiu);
            qiNiuConfig.put(SysConf.UPLOAD_LOCAL, uploadLocal);
            qiNiuConfig.put(SysConf.PICTURE_PRIORITY, qiNiuResultMap.get(SysConf.PICTURE_PRIORITY));
            qiNiuConfig.put(SysConf.LOCAL_PICTURE_BASE_URL, qiNiuResultMap.get(SysConf.LOCAL_PICTURE_BASE_URL));
            qiNiuConfig.put(SysConf.QI_NIU_PICTURE_BASE_URL, qiNiuResultMap.get(SysConf.QI_NIU_PICTURE_BASE_URL));
        }

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> errorMap = new HashMap<>();

        //引用自己设计的一个工具类
        Aboutfile af = new Aboutfile();

        // 转换成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 取得request中的所有文件名
        Iterator<String> iter = multiRequest.getFileNames();
        while (iter.hasNext()) {
            MultipartFile file = multiRequest.getFile(iter.next());
            if (file != null) {

                //获取旧名称
                String oldName = file.getOriginalFilename();

                //获取扩展名
                String expandedName = FileUtils.getPicExpandedName(oldName);

                //判断是否是图片
                if (!af.isPic(expandedName)) {
                    map.put(SysConf.UPLOADED, 0);
                    errorMap.put(SysConf.MESSAGE, "请上传正确的图片");
                    map.put(SysConf.ERROR, errorMap);
                    return map;
                }

                //对图片大小进行限制
                if (file.getSize() > (10 * 1024 * 1024)) {
                    map.put(SysConf.UPLOADED, 0);
                    errorMap.put(SysConf.MESSAGE, "图片大小不能超过10M");
                    map.put(SysConf.ERROR, errorMap);
                    return map;
                }

                // 设置图片上传服务必要的信息
                request.setAttribute(SysConf.USER_UID, SysConf.DEFAULT_UID);
                request.setAttribute(SysConf.ADMIN_UID, SysConf.DEFAULT_UID);
                request.setAttribute(SysConf.PROJECT_NAME, SysConf.BLOG);
                request.setAttribute(SysConf.SORT_NAME, SysConf.ADMIN);

                List<MultipartFile> fileData = new ArrayList<>();
                fileData.add(file);
                String result = fileService.uploadImgs(basePath, request, fileData, qiNiuConfig);
                Map<String, Object> resultMap = JsonUtils.jsonToMap(result);
                String code = resultMap.get(SysConf.CODE).toString();
                if(SysConf.SUCCESS.equals(code)) {
                    List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get(SysConf.DATA);
                    if(resultList.size() > 0) {
                        Map<String, Object> picture = resultList.get(0);
                        String fileName = picture.get(SysConf.PIC_NAME).toString();

                        map.put(SysConf.UPLOADED, 1);
                        map.put(SysConf.FILE_NAME, fileName);

                        // 设置显示方式
                        if("1".equals(qiNiuConfig.get(SysConf.PICTURE_PRIORITY))) {

                            String qiNiuPictureBaseUrl = qiNiuConfig.get(SysConf.QI_NIU_PICTURE_BASE_URL);
                            String qiNiuUrl = picture.get(SysConf.QI_NIU_URL).toString();

                            map.put(SysConf.URL, qiNiuPictureBaseUrl + qiNiuUrl);
                        } else {

                            String localPictureBaseUrl = qiNiuConfig.get(SysConf.LOCAL_PICTURE_BASE_URL);

                            // 设置图片服务根域名

                            String url = localPictureBaseUrl + picture.get(SysConf.PIC_URL).toString();

                            map.put(SysConf.URL, url);
                        }

                    }
                    return map;
                } else {
                    map.put(SysConf.UPLOADED, 0);
                    errorMap.put(SysConf.MESSAGE, "上传失败");
                    map.put(SysConf.ERROR, errorMap);
                    return map;
                }

            }
        }
        return null;

    }

    /**
     * 复制的图片上传
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/imgUploadByUrl", method = RequestMethod.POST)
    public synchronized Object imgUploadByUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 获取token
        String token = request.getParameter(SysConf.TOKEN);

        if(StringUtils.isEmpty(token)) {
            return ResultUtil.result(SysConf.ERROR, "未读取到token");
        }

        String [] params = token.split("\\?url=");

        // 七牛云配置
        Map<String, String> qiNiuConfig = new HashMap<>();

        // 从Redis中获取七牛云配置文件
        Map<String, String> resultMap = feignUtil.getQiNiuConfig(token);

        String uploadQiNiu = resultMap.get(SysConf.UPLOAD_QI_NIU);
        String uploadLocal = resultMap.get(SysConf.UPLOAD_LOCAL);
        String localPictureBaseUrl = resultMap.get(SysConf.LOCAL_PICTURE_BASE_URL);
        String qiNiuPictureBaseUrl = resultMap.get(SysConf.QI_NIU_PICTURE_BASE_URL);
        String qiNiuAccessKey = resultMap.get(SysConf.QI_NIU_ACCESS_KEY);
        String qiNiuSecretKey = resultMap.get(SysConf.QI_NIU_SECRET_KEY);
        String qiNiuBucket = resultMap.get(SysConf.QI_NIU_BUCKET);
        String qiNiuArea = resultMap.get(SysConf.QI_NIU_AREA);

        if("1".equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
        }

        if("1".equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
            return ResultUtil.result(SysConf.ERROR, "请先配置本地图片域名");
        }

        qiNiuConfig.put(SysConf.QI_NIU_ACCESS_KEY, qiNiuAccessKey);
        qiNiuConfig.put(SysConf.QI_NIU_SECRET_KEY, qiNiuSecretKey);
        qiNiuConfig.put(SysConf.QI_NIU_BUCKET, qiNiuBucket);
        qiNiuConfig.put(SysConf.QI_NIU_AREA, qiNiuArea);
        qiNiuConfig.put(SysConf.UPLOAD_QI_NIU, uploadQiNiu);
        qiNiuConfig.put(SysConf.UPLOAD_LOCAL, uploadLocal);
        qiNiuConfig.put(SysConf.PICTURE_PRIORITY, resultMap.get(SysConf.PICTURE_PRIORITY));
        qiNiuConfig.put(SysConf.LOCAL_PICTURE_BASE_URL, resultMap.get(SysConf.LOCAL_PICTURE_BASE_URL));
        qiNiuConfig.put(SysConf.QI_NIU_PICTURE_BASE_URL, resultMap.get(SysConf.QI_NIU_PICTURE_BASE_URL));

        String userUid = "uid00000000000000000000000000000000";
        String adminUid = "uid00000000000000000000000000000000";
        String projectName = "blog";
        String sortName = "admin";

        // 设置图片上传服务必要的信息
        request.setAttribute(SysConf.USER_UID, userUid);
        request.setAttribute(SysConf.ADMIN_UID, adminUid);
        request.setAttribute(SysConf.PROJECT_NAME, projectName);
        request.setAttribute(SysConf.SORT_NAME, sortName);

        // 需要上传的URL
        String itemUrl = params[1];

        //projectName现在默认base
        if (StringUtils.isEmpty(projectName)) {
            projectName = "base";
        }

        // TODO 这里可以检测用户上传，如果不是网站的用户或会员就不能调用
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
        java.io.File dest = null;

        FileOutputStream os = null;

        // 输入流
        InputStream inputStream = null;

        // 判断是否能够上传至本地
        if ("1".equals(uploadLocal)) {
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

                /**
                 * User-Agent是什么呢？
                 * User-Agent是Http协议中的一部分，属于头域的组成部分，User Agent也简称UA。用较为普通的一点来说，
                 * 是一种向访问网站提供你所使用的浏览器类型、操作系统、浏览器内核等信息的标识。通过这个标识，
                 * 用户所访问的网站可以显示不同的排版从而为用户提供更好的体验或者进行信息统计。
                 */

                // 设置用户代理
                con.setRequestProperty("User-agent", "	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0");

                // 设置5秒超时
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

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
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }

        // 上传七牛云，判断是否能够上传七牛云
        if ("1".equals(uploadQiNiu)) {
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
                qiNiuUrl = qn.uploadQiniu(dest, qiNiuConfig);

            } catch (Exception e) {
                log.error(e.getMessage());
                return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
            } finally {
                try {
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }

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
        file.setPicExpandedName(SysConf.JPG);
        file.setPicName(newFileName);
        // 设置本地图片
        file.setPicUrl(localPictureBaseUrl + picurl);

        // 设置七牛云图片
        file.setQiNiuUrl(qiNiuPictureBaseUrl + qiNiuUrl);
        file.setStatus(EStatus.ENABLE);
        file.setUserUid(userUid);
        file.setAdminUid(adminUid);
        fileService.save(file);

        Map<String, Object> result = new HashMap<>();
        result.put(SysConf.UPLOADED, 1);
        result.put(SysConf.FILE_NAME, newFileName);
        // 设置显示方式
        if("1".equals(qiNiuConfig.get(SysConf.PICTURE_PRIORITY))) {
            result.put(SysConf.URL, qiNiuPictureBaseUrl + qiNiuUrl);
        } else {
            // 设置图片服务根域名
            String url = localPictureBaseUrl + picurl;
            result.put(SysConf.URL, url);
        }
        return result;
    }

    /**
     * 工具栏“插入\编辑超链接”的文件上传
     *
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public Object fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String token = request.getParameter(SysConf.TOKEN);

        // 从Redis中获取七牛云配置文件
        Map<String, String> qiNiuResultMap = feignUtil.getQiNiuConfig(token);

        // 七牛云配置
        Map<String, String> qiNiuConfig = new HashMap<>();

        if(qiNiuConfig == null) {
            return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
        } else {
            String uploadQiNiu = qiNiuResultMap.get(SysConf.UPLOAD_QI_NIU);
            String uploadLocal = qiNiuResultMap.get(SysConf.UPLOAD_LOCAL);
            String localPictureBaseUrl = qiNiuResultMap.get(SysConf.LOCAL_PICTURE_BASE_URL);
            String qiNiuPictureBaseUrl = qiNiuResultMap.get(SysConf.QI_NIU_PICTURE_BASE_URL);
            String qiNiuAccessKey = qiNiuResultMap.get(SysConf.QI_NIU_ACCESS_KEY);
            String qiNiuSecretKey = qiNiuResultMap.get(SysConf.QI_NIU_SECRET_KEY);
            String qiNiuBucket = qiNiuResultMap.get(SysConf.QI_NIU_BUCKET);
            String qiNiuArea = qiNiuResultMap.get(SysConf.QI_NIU_AREA);

            if("1".equals(uploadQiNiu) && (StringUtils.isEmpty(qiNiuPictureBaseUrl) || StringUtils.isEmpty(qiNiuAccessKey)
                    || StringUtils.isEmpty(qiNiuSecretKey) || StringUtils.isEmpty(qiNiuBucket) || StringUtils.isEmpty(qiNiuArea))) {
                return ResultUtil.result(SysConf.ERROR, "请先配置七牛云");
            }

            if("1".equals(uploadLocal) && StringUtils.isEmpty(localPictureBaseUrl)) {
                return ResultUtil.result(SysConf.ERROR, "请先配置本地图片域名");
            }

            qiNiuConfig.put(SysConf.QI_NIU_ACCESS_KEY, qiNiuAccessKey);
            qiNiuConfig.put(SysConf.QI_NIU_SECRET_KEY, qiNiuSecretKey);
            qiNiuConfig.put(SysConf.QI_NIU_BUCKET, qiNiuBucket);
            qiNiuConfig.put(SysConf.QI_NIU_AREA, qiNiuArea);
            qiNiuConfig.put(SysConf.UPLOAD_QI_NIU, uploadQiNiu);
            qiNiuConfig.put(SysConf.UPLOAD_LOCAL, uploadLocal);
            qiNiuConfig.put(SysConf.PICTURE_PRIORITY, qiNiuResultMap.get(SysConf.PICTURE_PRIORITY));
            qiNiuConfig.put(SysConf.LOCAL_PICTURE_BASE_URL, qiNiuResultMap.get(SysConf.LOCAL_PICTURE_BASE_URL));
            qiNiuConfig.put(SysConf.QI_NIU_PICTURE_BASE_URL, qiNiuResultMap.get(SysConf.QI_NIU_PICTURE_BASE_URL));
        }

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> errorMap = new HashMap<>();
        //引用自己设计的一个工具类
        Aboutfile af = new Aboutfile();

        // 转换成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        // 取得request中的所有文件名
        Iterator<String> iter = multiRequest.getFileNames();
        while (iter.hasNext()) {
            MultipartFile file = multiRequest.getFile(iter.next());
            if (file != null) {

                // 获取旧名称
                String oldName = file.getOriginalFilename();
                // 获取扩展名
                String expandedName = FileUtils.getPicExpandedName(oldName);
                // 判断是否安全文件
                if (!af.isSafe(expandedName)) {
                    map.put(SysConf.UPLOADED, 0);
                    errorMap.put(SysConf.MESSAGE, "请上传正确格式的文件");
                    map.put(SysConf.ERROR, errorMap);
                    return map;
                }

                //对文件大小进行限制
                if (file.getSize() > (50 * 1024 * 1024)) {
                    map.put(SysConf.UPLOADED, 0);
                    errorMap.put(SysConf.MESSAGE, "文件大小不能超过50M");
                    map.put(SysConf.ERROR, errorMap);
                    return map;
                }

                // 设置文件上传服务必要的信息
                request.setAttribute(SysConf.USER_UID, "uid00000000000000000000000000000000");
                request.setAttribute(SysConf.ADMIN_UID, "uid00000000000000000000000000000000");
                request.setAttribute(SysConf.PROJECT_NAME, "blog");
                request.setAttribute(SysConf.SORT_NAME, "admin");

                List<MultipartFile> fileData = new ArrayList<>();
                fileData.add(file);
                String result = fileService.uploadImgs(basePath, request, fileData, qiNiuConfig);
                Map<String, Object> resultMap = JsonUtils.jsonToMap(result);
                String code = resultMap.get(SysConf.CODE).toString();
                if(SysConf.SUCCESS.equals(code)) {
                    List<HashMap<String, Object>> resultList = (List<HashMap<String, Object>>) resultMap.get(SysConf.DATA);
                    if(resultList.size() > 0) {
                        Map<String, Object> picture = resultList.get(0);
                        String fileName = picture.get(SysConf.PIC_NAME).toString();
                        map.put(SysConf.UPLOADED, 1);
                        map.put(SysConf.FILE_NAME, fileName);

                        // 设置显示方式
                        if("1".equals(qiNiuConfig.get(SysConf.PICTURE_PRIORITY))) {

                            String qiNiuPictureBaseUrl = qiNiuConfig.get(SysConf.QI_NIU_PICTURE_BASE_URL);
                            String qiNiuUrl = qiNiuPictureBaseUrl + picture.get(SysConf.QI_NIU_URL).toString();

                            map.put(SysConf.URL, qiNiuUrl);
                        } else {

                            String localPictureBaseUrl = qiNiuConfig.get(SysConf.LOCAL_PICTURE_BASE_URL);

                            // 设置图片服务根域名

                            String url = localPictureBaseUrl + picture.get(SysConf.PIC_URL).toString();

                            map.put(SysConf.URL, url);
                        }
                    }
                    return map;
                } else {
                    map.put(SysConf.UPLOADED, 0);
                    errorMap.put(SysConf.MESSAGE, "上传失败");
                    map.put(SysConf.ERROR, errorMap);
                    return map;
                }

            }
        }
        return null;
    }
}
