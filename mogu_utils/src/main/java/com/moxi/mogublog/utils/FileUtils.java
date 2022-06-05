package com.moxi.mogublog.utils;

import com.moxi.mogublog.utils.global.Constants;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * 文件操作工具类
 *
 * @author 陌溪
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
     *
     * @param fileName
     * @return
     */
    public static Boolean isPicture(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return false;
        }
        String expandName = getPicExpandedName(fileName);
        return pictureTypes.contains(expandName);
    }

    /**
     * 判断是否为markdown文件
     *
     * @param fileName
     * @return
     */
    public static Boolean isMarkdown(String fileName) {
        return fileName.indexOf(".md") > -1;
    }

    /**
     * Markdown转Html
     *
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        MutableDataSet options = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(
                AutolinkExtension.create(),
                EmojiExtension.create(),
                StrikethroughExtension.create(),
                TaskListExtension.create(),
                TablesExtension.create()
        ))
                // set GitHub table parsing options
                .set(TablesExtension.WITH_CAPTION, false)
                .set(TablesExtension.COLUMN_SPANS, false)
                .set(TablesExtension.MIN_HEADER_ROWS, 1)
                .set(TablesExtension.MAX_HEADER_ROWS, 1)
                .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
                .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
                .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)

                // setup emoji shortcut options
                // uncomment and change to your image directory for emoji images if you have it setup
                //.set(EmojiExtension.ROOT_IMAGE_PATH, emojiInstallDirectory())
                .set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB)
                .set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.IMAGE_ONLY)
                // other options
                ;
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(markdown);
        String html = renderer.render(document);
        return html;
    }

    /**
     * Html 转 Markdown
     *
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
                path = baseUrl + StringUtils.getUUID() + "." + imageName;
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
     * 根据OriginalFilename获取FileName【去除文件后缀】
     * @param originalFilename
     * @return
     */
    public static String getFileName(String originalFilename) {
        String fileName = "";
        if (StringUtils.isNotBlank(originalFilename) &&
                StringUtils.contains(originalFilename, ".")) {
            fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        }
        return fileName;
    }

    /**
     * 从Request中获取文件
     *
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

    public static final String[] IMG_FILE = {
            Constants.FILE_SUFFIX_BMP,
            Constants.FILE_SUFFIX_JPG,
            Constants.FILE_SUFFIX_PNG,
            Constants.FILE_SUFFIX_TIF,
            Constants.FILE_SUFFIX_GIF,
            Constants.FILE_SUFFIX_JPEG,
            Constants.FILE_SUFFIX_WEBP};
    public static final String[] DOC_FILE = {
            Constants.FILE_SUFFIX_DOC,
            Constants.FILE_SUFFIX_DOCX,
            Constants.FILE_SUFFIX_TXT,
            Constants.FILE_SUFFIX_HLP,
            Constants.FILE_SUFFIX_WPS,
            Constants.FILE_SUFFIX_RTF,
            Constants.FILE_SUFFIX_XLS,
            Constants.FILE_SUFFIX_XLSX,
            Constants.FILE_SUFFIX_PPT,
            Constants.FILE_SUFFIX_PPTX,
            Constants.FILE_SUFFIX_JAVA,
            Constants.FILE_SUFFIX_HTML,
            Constants.FILE_SUFFIX_PDF,
            Constants.FILE_SUFFIX_MD,
            Constants.FILE_SUFFIX_SQL,
            Constants.FILE_SUFFIX_CSS,
            Constants.FILE_SUFFIX_JS,
            Constants.FILE_SUFFIX_VUE,
            Constants.FILE_SUFFIX_JAVA};
    public static final String[] VIDEO_FILE = {
            Constants.FILE_SUFFIX_AVI,
            Constants.FILE_SUFFIX_MP4,
            Constants.FILE_SUFFIX_MPG,
            Constants.FILE_SUFFIX_MOV,
            Constants.FILE_SUFFIX_SWF,
            Constants.FILE_SUFFIX_3GP,
            Constants.FILE_SUFFIX_RM,
            Constants.FILE_SUFFIX_RMVB,
            Constants.FILE_SUFFIX_WMV,
            Constants.FILE_SUFFIX_MKV};
    public static final String[] MUSIC_FILE = {
            Constants.FILE_SUFFIX_WAV,
            Constants.FILE_SUFFIX_AIF,
            Constants.FILE_SUFFIX_AU,
            Constants.FILE_SUFFIX_MP3,
            Constants.FILE_SUFFIX_RAM,
            Constants.FILE_SUFFIX_WMA,
            Constants.FILE_SUFFIX_MMF,
            Constants.FILE_SUFFIX_AMR,
            Constants.FILE_SUFFIX_AAC,
            Constants.FILE_SUFFIX_FLAC};
    public static String[] ALL_FILE = {
            Constants.FILE_SUFFIX_BMP,
            Constants.FILE_SUFFIX_JPG,
            Constants.FILE_SUFFIX_PNG,
            Constants.FILE_SUFFIX_TIF,
            Constants.FILE_SUFFIX_GIF,
            Constants.FILE_SUFFIX_JPEG,
            Constants.FILE_SUFFIX_WEBP,

            Constants.FILE_SUFFIX_DOC,
            Constants.FILE_SUFFIX_DOCX,
            Constants.FILE_SUFFIX_TXT,
            Constants.FILE_SUFFIX_HLP,
            Constants.FILE_SUFFIX_WPS,
            Constants.FILE_SUFFIX_RTF,
            Constants.FILE_SUFFIX_XLS,
            Constants.FILE_SUFFIX_XLSX,
            Constants.FILE_SUFFIX_PPT,
            Constants.FILE_SUFFIX_PPTX,
            Constants.FILE_SUFFIX_JAVA,
            Constants.FILE_SUFFIX_HTML,
            Constants.FILE_SUFFIX_PDF,
            Constants.FILE_SUFFIX_MD,
            Constants.FILE_SUFFIX_SQL,
            Constants.FILE_SUFFIX_CSS,
            Constants.FILE_SUFFIX_JS,
            Constants.FILE_SUFFIX_VUE,
            Constants.FILE_SUFFIX_JAVA,

            Constants.FILE_SUFFIX_AVI,
            Constants.FILE_SUFFIX_MP4,
            Constants.FILE_SUFFIX_MPG,
            Constants.FILE_SUFFIX_MOV,
            Constants.FILE_SUFFIX_SWF,
            Constants.FILE_SUFFIX_3GP,
            Constants.FILE_SUFFIX_RM,
            Constants.FILE_SUFFIX_RMVB,
            Constants.FILE_SUFFIX_WMV,
            Constants.FILE_SUFFIX_MKV,

            Constants.FILE_SUFFIX_WAV,
            Constants.FILE_SUFFIX_AIF,
            Constants.FILE_SUFFIX_AU,
            Constants.FILE_SUFFIX_MP3,
            Constants.FILE_SUFFIX_RAM,
            Constants.FILE_SUFFIX_WMA,
            Constants.FILE_SUFFIX_MMF,
            Constants.FILE_SUFFIX_AMR,
            Constants.FILE_SUFFIX_AAC,
            Constants.FILE_SUFFIX_FLAC
    };

//    static {
//        ALL_FILE = (String[]) ArrayUtils.addAll(ALL_FILE, IMG_FILE);
//        ALL_FILE = (String[]) ArrayUtils.addAll(ALL_FILE, DOC_FILE);
//        ALL_FILE = (String[]) ArrayUtils.addAll(ALL_FILE, VIDEO_FILE);
//        ALL_FILE = (String[]) ArrayUtils.addAll(ALL_FILE, MUSIC_FILE);
//    }

    /**
     * 判断一个文件是否存在，不存在就创建它 Method execute,只能建最后面那个目录
     *
     * @param path
     * @return
     */
    public void creatFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            log.error("该目录不存在");
        } else {
            file.mkdir();
        }
    }

    /**
     * 从文件名中得到其后缀名
     *
     * @param filename
     * @return 后缀名
     */
    public static String getFileSuffix(String filename) {
        String suffix;
        suffix = filename.substring(
                filename.lastIndexOf(Constants.SYMBOL_POINT) + 1);
        return suffix;
    }

    /**
     * 通过其后缀名判断其是否合法,合法后缀名为常见的
     *
     * @param suffix 后缀名
     * @return 合法返回true，不合法返回false
     */
    public static boolean isSafe(String suffix) {
        suffix = suffix.toLowerCase();
        for (int i = 0; i < ALL_FILE.length; i++) {
            if (ALL_FILE[i].equals(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过其后缀名判断其是否是图片
     *
     * @param suffix 后缀名
     * @return 合法返回true，不合法返回false
     */
    public static boolean isPic(String suffix) {
        suffix = suffix.toLowerCase();
        for (int i = 0; i < IMG_FILE.length; i++) {
            if (IMG_FILE[i].equals(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 进行上传文件的相关操作
     *
     * @param file
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static int uploadFile(File file, String fileLocation) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        int result = 1;
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(new File(fileLocation));
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(fos);

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            try {
                try {
                    if (null != bis) {
                        bis.close();
                        bis = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    result = 0;
                }
                try {
                    if (null != bos) {
                        bos.close();
                        bos = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    result = 0;
                }
                fis.close();
                fos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                result = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        } finally {
            try {
                if (null != bis) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = 0;

            }
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = 0;

            }
        }
        return result;
    }

    /**
     * 计算文件大小，将long类型转换为String类型
     *
     * @param fileSize
     * @return
     */
    public static String getFileStringSize(long fileSize) {
        //size不能为0？
        double temp = 0.0;
        String ssize = "";
        temp = (double) fileSize / Constants.NUM_1024;
        if (temp >= Constants.NUM_1024) {
            temp = temp / Constants.NUM_1024;
            if (temp >= Constants.NUM_1024) {
                temp = temp / Constants.NUM_1024;
                ssize = temp + "000";
                ssize = ssize.substring(Constants.NUM_ZERO, ssize.indexOf(Constants.SYMBOL_POINT) + Constants.NUM_THREE) + "GB";
            } else {
                ssize = temp + "000";
                ssize = ssize.substring(Constants.NUM_ZERO, ssize.indexOf(Constants.SYMBOL_POINT) + Constants.NUM_THREE) + "MB";
            }
        } else {
            ssize = temp + "000";
            ssize = ssize.substring(Constants.NUM_ZERO, ssize.indexOf(Constants.SYMBOL_POINT) + Constants.NUM_THREE) + "KB";
        }
        return ssize;
    }

    public static void main(String[] args) {
        System.out.println(pictureTypes.contains("png"));
    }
}
