package com.moxi.mogublog.picture.restapi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.picture.entity.FileSort;
import com.moxi.mogublog.picture.global.SQLConf;
import com.moxi.mogublog.picture.global.SysConf;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.service.FileSortService;
import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mogublog.utils.JsonUtils;
import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.vo.FileVO;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
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
public class FileRestApi {

    Logger log = LoggerFactory.getLogger(FileRestApi.class);

    @Autowired
    private FileService fileService;
    @Autowired
    private FileSortService fileSortService;
    @Value(value = "${data.image.url}")
    private String imgHost;
    @Value(value = "${file.upload.path}") //获取上传路径
    private String path;

    /**
     * 获取后缀名
     *
     * @param fileName
     * @return
     */
    private static String getPicExpandedName(String fileName) {
        String ext = "";
        if (StringUtils.isNotBlank(fileName) &&
                StringUtils.contains(fileName, ".")) {
            ext = StringUtils.substring(fileName, fileName.lastIndexOf(".") + 1);
        }
        ext = ext.toLowerCase();
        if (ext == null || ext.length() < 1) {
            ext = "jpg";
        }

        return ext;
    }

    @ApiOperation(value = "Hello_Picture", notes = "Hello_Picture")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(String urlString, int i) throws IOException {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream inputStream = null;
        // 当获取的相片无法正常显示的时候，需要给一个默认图片
        try {
            inputStream = con.getInputStream();
        } catch (Exception exception) {
            return ResultUtil.result(SysConf.ERROR, "无法获取头像");
        }

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        String filename = "D:\\" + i + ".jpg";  //下载路径及下载图片名称
        File file = new File(filename);
        FileOutputStream os = new FileOutputStream(file, true);
        // 开始读取
        while ((len = inputStream.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        System.out.println(i);
        // 完毕，关闭所有链接
        os.close();
        inputStream.close();
        return "hello";
    }

    /**
     * 获取文件的信息接口
     * fileIds 获取文件信息的ids
     * code ids用什么分割的，默认“,”
     *
     * @param response
     * @param request
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
                        remap.put("url", file.getPicUrl());//，地址
                        remap.put("expandedName", file.getPicExpandedName());//后缀名，也就是类型
                        remap.put("name", file.getPicName());//名称
                        remap.put("uid", file.getUid());
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
                String fileName = request.getParameter("fileName");//aim_test//文件名,不传就用默认的，或者是oldName
                if (StringUtils.isEmpty(fileName)) {
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
     * sortName 传入的模块名， 如 shop ，user ,等，不在数据库中记录的是不会上传的
     *
     * @param filedata
     * @param response
     * @param request
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
    public synchronized Object uploadPics(HttpServletResponse response, HttpServletRequest request, List<MultipartFile> filedatas) {
        //上传者id
        String userUid = request.getParameter("userUid"); //如果是用户上传，则包含用户uid
        String adminUid = request.getParameter("adminUid"); //如果是管理员上传，则包含管理员uid
        String projectName = request.getParameter("projectName");//项目名
        String sortName = request.getParameter("sortName");//模块名

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
        if (filedatas != null && filedatas.size() > 0) {
            for (MultipartFile filedata : filedatas) {

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
                log.info(newFileName + ":" + oldName);
                //文件路径问题
                log.info("path====" + path);
                String newPath = path + sortUrl + "/" + picExpandedName + "/" + DateUtils.getYears() + "/"
                        + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/";
                //path = path.replaceAll("//", "/");
                String picurl = sortUrl + "/" + picExpandedName + "/" + DateUtils.getYears() + "/"
                        + DateUtils.getMonth() + "/" + DateUtils.getDay() + "/" + newFileName;
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
                file.setFileSortUid(fileSort.getUid());
                file.setFileOldName(oldName);
                file.setFileSize(size);
                file.setPicExpandedName(picExpandedName);
                file.setPicName(newFileName);
                file.setPicUrl(picurl);
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

    @ApiOperation(value = "通过URL上传图片", notes = "通过URL上传图片")
    @PostMapping("/uploadPicsByUrl")
    public synchronized Object uploadPicsByUrl(@Validated({GetList.class}) @RequestBody FileVO fileVO, BindingResult result) {

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

                    // 输入流
                    InputStream inputStream = null;

                    // 当获取的相片无法正常显示的时候，需要给一个默认图片
                    inputStream = con.getInputStream();

                    // 1K的数据缓冲
                    byte[] bs = new byte[1024];
                    // 读取到的数据长度
                    int len;

                    File file = new File(saveUrl);
                    FileOutputStream os = new FileOutputStream(file, true);
                    // 开始读取
                    while ((len = inputStream.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }
                    // 完毕，关闭所有链接
                    os.close();
                    inputStream.close();

                } catch (Exception e) {
                    log.info("==上传文件异常===url:" + saveUrl + "-----");
                    e.printStackTrace();
                    return ResultUtil.result(SysConf.ERROR, "文件上传失败");
                }
                com.moxi.mogublog.picture.entity.File file = new com.moxi.mogublog.picture.entity.File();

                file.setCreateTime(new Date(System.currentTimeMillis()));
                file.setFileSortUid(fileSort.getUid());
                file.setFileOldName(itemUrl);
                file.setFileSize(0L);
                file.setPicExpandedName("jpg");
                file.setPicName(newFileName);
                file.setPicUrl(picurl);
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

