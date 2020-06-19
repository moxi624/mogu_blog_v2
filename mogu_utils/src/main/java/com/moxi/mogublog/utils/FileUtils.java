package com.moxi.mogublog.utils;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 文件有关的工具类
 *
 * @author xzx19950624@qq.com
 * @date 2017年10月2日12:16:27
 */
@Slf4j
public class FileUtils {

    // 图片类型
    private static List<String> pictureTypes = new ArrayList<>();

    static {
        pictureTypes.add("jpg");
        pictureTypes.add("jpeg");
        pictureTypes.add("bmp");
        pictureTypes.add("gif");
        pictureTypes.add("png");
    }

    /**
     * 判断是否是图片
     * @param fileName
     * @return
     */
    public static Boolean isPicture(String fileName) {
        if(StringUtils.isEmpty(fileName)) {
            return false;
        }
        String expandName = getPicExpandedName(fileName);
        return pictureTypes.contains(expandName);
    }

    /**
     * 判断是否为markdown文件
     * @param fileName
     * @return
     */
    public static Boolean isMarkdown(String fileName) {
        return fileName.indexOf(".md") > -1;
    }

    /**
     * Markdown转Html
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(markdown);
        String html = renderer.render(document);
        return html;
    }

    /**
     * Html 转 Markdown
     * @param html
     * @return
     */
    public static String htmlToMarkdown(String html) {
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(html);
        return markdown;
    }

    /**
     * 上传文件
     *
     * @param pathRoot 物理路径webapp所在路径
     * @return 返回图片上传的地址
     * @throws IOException
     * @throws IllegalStateException
     */
    public static String uploadFile(String pathRoot, String baseUrl, MultipartFile avatar) throws IllegalStateException, IOException {
        String path = "";
        if (!avatar.isEmpty()) {
            //获得文件类型（可以判断如果不是图片，禁止上传）  
            String contentType = avatar.getContentType();
            if (contentType.indexOf("image") != -1) {
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                path = baseUrl + StrUtils.getUUID() + "." + imageName;
                avatar.transferTo(new File(pathRoot + path));
            }
        }
        return path;
    }


    /**
     * 获取后缀名
     *
     * @param fileName
     * @return
     */
    public static String getPicExpandedName(String fileName) {
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

    /**
     * 从Request中获取文件
     * @return
     */
    public static List<MultipartFile> getMultipartFileList(HttpServletRequest request) {
        List<MultipartFile> files = new ArrayList<>();
        try {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            if (request instanceof MultipartHttpServletRequest) {
                // 将request变成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                Iterator<String> iter = multiRequest.getFileNames();
                // 检查form中是否有enctype="multipart/form-data"
                if (multipartResolver.isMultipart(request) && iter.hasNext()) {
                    // 获取multiRequest 中所有的文件名
                    while (iter.hasNext()) {
                        List<MultipartFile> fileRows = multiRequest
                                .getFiles(iter.next().toString());
                        if (fileRows != null && fileRows.size() != 0) {
                            for (MultipartFile file : fileRows) {
                                if (file != null && !file.isEmpty()) {
                                    files.add(file);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("解析MultipartRequest错误", ex);
        }
        return files;
    }

    public static void main(String[] args) {
        System.out.println(pictureTypes.contains("png"));
    }
}
