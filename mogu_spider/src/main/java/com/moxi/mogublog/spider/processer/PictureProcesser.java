package com.moxi.mogublog.spider.processer;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬取的图片页面处理
 *
 * @author 陌溪
 * @date 2021年1月8日16:47:34
 */
@Component
@Slf4j
public class PictureProcesser implements PageProcessor {

    // 获取img标签正则
    private static final String IMGURL_REG = "<(img|IMG)(.*?)>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "(src|SRC)=\"(.*?)\"";

    /**
     * 查找输入的关键词
     *
     * @param url
     * @return
     */
    public static String findSearchKey(String url) {
        // 按指定模式在字符串查找
        String reg = "\\?q=.*";
        // 创建 Pattern 对象
        // 现在创建 matcher对象 匹配的模式
        Pattern pattern = Pattern.compile(reg);
        Matcher m = pattern.matcher(url);
        if (m.find()) {
            String group = m.group(0);
            String replace = group.replace("?q=", "").replaceFirst("&.*", "");
            return replace;
        } else {
            return url;
        }
    }

    //下载图片
    private static List<String> Download(List<String> listImgSrc) {
        //开始时间
        Date begindate = new Date();
        ArrayList<String> localFile = new ArrayList<>();
        try {
            for (String url : listImgSrc) {
                //开始时间
                Date begindate2 = new Date();
                String imageName = url.substring(url.lastIndexOf("/") + 1);
                URL uri = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
                conn.setConnectTimeout(50000);
                conn.setReadTimeout(50000);
                conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
                conn.connect();
                InputStream in = conn.getInputStream();
                String pathname = "D:\\123\\" + imageName;
                File file = new File(pathname);
                FileOutputStream fo = new FileOutputStream(file);//文件输出流
                byte[] buf = new byte[1024];
                int length = 0;
                System.out.println("开始下载:" + url);
                while ((length = in.read(buf, 0, buf.length)) != -1) {
                    fo.write(buf, 0, length);
                }
                //关闭流
                in.close();
                fo.close();
                System.out.println(imageName + "下载完成");
                //结束时间
                Date overdate2 = new Date();
                double time = overdate2.getTime() - begindate2.getTime();
                System.out.println("耗时：" + time / 1000 + "s");
                localFile.add(pathname);
            }
            Date overdate = new Date();
            double time = overdate.getTime() - begindate.getTime();
            System.out.println("总耗时：" + time / 1000 + "s");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return localFile;
    }

    public static void main(String[] args) throws Exception {
        PictureProcesser pictureProcesser = new PictureProcesser();
        String html = pictureProcesser.getHtml("https://www.hippopx.com/zh/query?q=cat");
        List<String> imageUrl = pictureProcesser.getImageUrl(html);
        List<String> imageSrc = pictureProcesser.getImageSrc(imageUrl);
        System.out.println(JSON.toJSONString(imageSrc));
    }

    //获取ImageSrc地址
    public List<String> getImageSrc(List<String> listimageurl) {
        List<String> listImageSrc = new ArrayList<>();
        for (String image : listimageurl) {
            Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
            while (matcher.find()) {
                listImageSrc.add(matcher.group(2).trim());
            }
        }
        return listImageSrc;
    }

    @Override
    public void process(Page page) {
        int i = 50;
        //page计数是从2开始的
        //https://foter.com/search/instant/?q=cat&page=2
        Html html = page.getHtml();
        String url = page.getRequest().getUrl();
        List<String> list = html.regex("<img.*?src=.*?photos.*?/>").all();
        List<String> imageSrc = getImageSrc(list);
        String jsonString = JSON.toJSONString(imageSrc);
        if (CollectionUtil.isEmpty(imageSrc)) {
            page.putField("imgSrc", jsonString);
            page.putField("searchKey", findSearchKey(url));
        } else {
            //跳过爬取
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setCharset("utf8").setRetryTimes(2).setSleepTime(2000).setTimeOut(4000);
    }

    /**
     * 解析html
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String getHtml(String url) {
        InputStream in = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            //使用java.net.URL
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.
                    openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.setRequestProperty("Charsert", "UTF-8");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            //获取输入流
            in = connection.getInputStream();
            //流的包装
            isr = new InputStreamReader(in);
            br = new BufferedReader(isr);
            String line;
            //整行读取
            while ((line = br.readLine()) != null) {
                //添加到StringBuffer中
                sb.append(line, 0, line.length());
                //添加换行符
                sb.append('\n');
            }

        } catch (Exception e) {
            log.error("获取图片出现异常:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            //关闭各种流，先声明的后关闭
            try {
                if (in != null) {
                    in.close();
                }
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                log.error("获取图片出现异常:{}", e.getMessage());
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    //获取ImageUrl地址
    public List<String> getImageUrl(String html) {
        Pattern compile = Pattern.compile(IMGURL_REG);
        Matcher matcher = compile.matcher(html);
        List<String> listimgurl = new ArrayList<>();
        int temp = 0;
        while (matcher.find()) {
            temp ++;
            // 跳过第一张logo图片
            if (temp == 1) {
                continue;
            }
            listimgurl.add(matcher.group());
        }
        return listimgurl;
    }

}