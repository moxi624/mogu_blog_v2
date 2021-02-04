package com.moxi.mogublog.picture.spider;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PictureProcesser implements PageProcessor {

    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*?src=.*?photos.*?/>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "/photos.*?\\?";

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
        // 现在创建 matcher 对象
        Pattern pattern = Pattern.compile(reg);// 匹配的模式
        Matcher m = pattern.matcher(url);
        if (m.find()) {
            String group = m.group(0);
            String replace = group.replace("?q=", "").replaceFirst("&.*", "");
            return replace;
        } else {
            return url;
        }
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

    //获取ImageSrc地址
    private static List<String> getImageSrc(List<String> listimageurl) {
        List<String> listImageSrc = new ArrayList<String>();
        for (String image : listimageurl) {
            Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
            while (matcher.find()) {
                listImageSrc.add("https://foter.com" + matcher.group().substring(0, matcher.group().length() - 1));
            }
        }
        return listImageSrc;
    }

    /**
     * 获取UUID，去掉了-
     *
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
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
                String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
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

    private static void add(List<String> localFile) throws QiniuException {
        for (String ss : localFile) {
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Zone.zone2());
            //生成上传凭证，然后准备上传
            String accessKey = "KPTcX6IBYXrR8wpE0VvcUXBu4XkC0XyhquFivGYe";
            String secretKey = "bQcxUBc_c8evOPKZMxiJ2luHTROcRha3krWJmvR3";
            String bucket = "mogublogforsjf";
            //...其他参数参考类注释
            UploadManager uploadManager = new UploadManager(cfg);
            String key = getUUID();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            File localFilePath = new File(ss);
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        }
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

    public String uploadQiniu(File localFilePath) throws QiniuException {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //生成上传凭证，然后准备上传
        String accessKey = "KPTcX6IBYXrR8wpE0VvcUXBu4XkC0XyhquFivGYe";
        String secretKey = "bQcxUBc_c8evOPKZMxiJ2luHTROcRha3krWJmvR3";
        String bucket = "mogublogforsjf";
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        String key = getUUID();
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        Response response = uploadManager.put(localFilePath, key, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return putRet.key;
    }
}
