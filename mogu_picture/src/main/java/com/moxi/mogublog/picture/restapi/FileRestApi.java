package com.moxi.mogublog.picture.restapi;


import com.moxi.mogublog.commons.entity.SystemConfig;
import com.moxi.mogublog.picture.form.SearchPictureForm;
import com.moxi.mogublog.picture.service.FileService;
import com.moxi.mogublog.picture.spider.PicturePieline;
import com.moxi.mogublog.picture.spider.PictureProcesser;
import com.moxi.mogublog.picture.util.FeignUtil;
import com.moxi.mogublog.picture.util.MinioUtil;
import com.moxi.mougblog.base.validator.group.GetList;
import com.moxi.mougblog.base.vo.FileVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import us.codecraft.webmagic.Spider;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件上传接口 【总的文件接口，需要调用本地文件、七牛云、Minio上传服务】
 *
 * @author 陌溪
 * @date 2020年10月21日15:32:03
 */
@RestController
@RequestMapping("/file")
@Api(value = "文件服务相关接口", tags = {"文件服务相关接口"})
@Slf4j
public class FileRestApi {

    @Autowired
    MinioUtil minioUtil;
    @Autowired
    private FeignUtil feignUtil;
    @Autowired
    private FileService fileService;
    @Autowired
    private PicturePieline picturePieline;
    @Autowired
    private PictureProcesser pictureProcesser;

    @ApiOperation(value = "截图上传", notes = "截图上传")
    @RequestMapping(value = "/cropperPicture", method = RequestMethod.POST)
    public String cropperPicture(@RequestParam("file") MultipartFile file) {
        List<MultipartFile> multipartFileList = new ArrayList<>();
        multipartFileList.add(file);
        return fileService.cropperPicture(multipartFileList);
    }

    /**
     * 获取文件的信息接口
     * fileIds 获取文件信息的ids
     * code ids用什么分割的，默认“,”
     *
     * @return
     */
    @ApiOperation(value = "通过fileIds获取图片信息接口", notes = "获取图片信息接口")
    @GetMapping("/getPicture")
    public String getPicture(
            @ApiParam(name = "fileIds", value = "文件ids", required = false) @RequestParam(name = "fileIds", required = false) String fileIds,
            @ApiParam(name = "code", value = "切割符", required = false) @RequestParam(name = "code", required = false) String code) {
        log.info("获取图片信息: {}", fileIds);
        return fileService.getPicture(fileIds, code);
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
        // 获取系统配置文件
        SystemConfig systemConfig = feignUtil.getSystemConfig();
        return fileService.batchUploadFile(request, filedatas, systemConfig);
    }

    /**
     * 通过URL将图片上传到自己服务器中【用于Github和Gitee的头像上传】
     *
     * @param fileVO
     * @param result
     * @return
     */
    @ApiOperation(value = "通过URL上传图片", notes = "通过URL上传图片")
    @PostMapping("/uploadPicsByUrl")
    public Object uploadPicsByUrl(@Validated({GetList.class}) @RequestBody FileVO fileVO, BindingResult result) {
        return fileService.uploadPictureByUrl(fileVO);
    }


    /**
     * Ckeditor图像中的图片上传
     *
     * @return
     */
    @ApiOperation(value = "图像中的图片上传", notes = "图像中的图片上传")
    @RequestMapping(value = "/ckeditorUploadFile", method = RequestMethod.POST)
    public Object ckeditorUploadFile(HttpServletRequest request) {
        return fileService.ckeditorUploadFile(request);
    }

    /**
     * Ckeditor复制的图片上传
     *
     * @return
     */
    @ApiOperation(value = "复制的图片上传", notes = "复制的图片上传")
    @RequestMapping(value = "/ckeditorUploadCopyFile", method = RequestMethod.POST)
    public synchronized Object ckeditorUploadCopyFile() {
        return fileService.ckeditorUploadCopyFile();
    }

    /**
     * Ckeditor工具栏 “插入\编辑超链接”的文件上传
     *
     * @return
     */
    @ApiOperation(value = "工具栏的文件上传", notes = "工具栏的文件上传")
    @RequestMapping(value = "/ckeditorUploadToolFile", method = RequestMethod.POST)
    public Object ckeditorUploadToolFile(HttpServletRequest request) {
        return fileService.ckeditorUploadToolFile(request);
    }

    private Spider spider;

    /**
     * 通过爬虫爬取关键词上传图片到自己服务器中
     *
     * @return
     */
//    @ApiOperation(value = "通过爬虫爬取关键词上传图片", notes = "通过爬虫上传图片")
//    @PostMapping("/spiderPics")
//    public Object spiderPics(@RequestBody SearchPictureForm form) {
//        //开启蜘蛛爬取内容
//        String[] urls = new String[form.getCount()];
//        String searchUrl = "https://foter.com/search/instant/?q=" + form.getSearchKey();
//        urls[0] = searchUrl;
//        for (int i = 1; i < form.getCount(); i++) {
//            int pageIndex = i + 1;
//            urls[i] = searchUrl + "&page=" + pageIndex;
//        }
//        spider = Spider.create(pictureProcesser)
//                .addUrl(urls)
//                .addPipeline(picturePieline)
//                .setScheduler(new QueueScheduler())
//                .thread(10);
//
//        spider.start();
//        return "开始爬取";
//    }
    @ApiOperation(value = "通过爬虫爬取关键词上传图片", notes = "通过爬虫上传图片")
    @PostMapping("/spiderPics")
    public Object spiderPics(@RequestBody SearchPictureForm form) {
        //开启蜘蛛爬取内容
        String[] urls = new String[form.getCount()];
        String searchUrl = "https://foter.com/search/instant/?q=" + form.getSearchKey();
        urls[0] = searchUrl;
        ArrayList<String> imageUrls = new ArrayList<>();
        for (int i = 1; i < form.getCount(); i++) {
            int pageIndex = i + 1;
            urls[i] = searchUrl + "&page=" + pageIndex;
        }
        for (String url : urls) {
            String html = null;
            try {
                html = getHtml(url);
            } catch (Exception e) {
                continue;
            }
            List<String> imageUrl = getImageUrl(html);
            imageUrls.addAll(imageUrl);
        }
        fileService.uploadPictureByUrl(form, imageUrls);
        return "";
//        spider = Spider.create(pictureProcesser)
//                .addUrl(urls)
//                .addPipeline(picturePieline)
//                .setScheduler(new QueueScheduler())
//                .thread(10);
//
//        spider.start();
//        return "开始爬取";
    }

    /**
     * 解析html
     *
     * @param url
     * @return
     * @throws Exception
     */
    private static String getHtml(String url) throws Exception {
        URL url1 = new URL(url);//使用java.net.URL
        HttpURLConnection connection = (HttpURLConnection) url1.
                openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        InputStream in = connection.getInputStream();//获取输入流
        InputStreamReader isr = new InputStreamReader(in);//流的包装
        BufferedReader br = new BufferedReader(isr);

        String line;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {//整行读取
            sb.append(line, 0, line.length());//添加到StringBuffer中
            sb.append('\n');//添加换行符
        }
        //关闭各种流，先声明的后关闭
        br.close();
        isr.close();
        in.close();
        return sb.toString();
    }

    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*?src=.*?photos.*?/>";

    //获取ImageUrl地址
    private static List<String> getImageUrl(String html) {
        Pattern compile = Pattern.compile(IMGURL_REG);
        Matcher matcher = compile.matcher(html);
        List<String> listimgurl = new ArrayList<String>();
        while (matcher.find()) {
            listimgurl.add(matcher.group());
        }
        return listimgurl;
    }
}

