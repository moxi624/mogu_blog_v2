package com.moxi.mogublog.picture.spider;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PicturePieline implements Pipeline {

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

    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取图片参数
        String imgSrc = resultItems.get("imgSrc");
        //获取关键词
        String searchKey = resultItems.get("searchKey");
        List imgSrcs = JSON.parseObject(imgSrc, List.class);
        if (CollectionUtil.isEmpty(imgSrcs)) {
            return;
        }
        //下载图片并上传到七牛云
        List localFileUrl = Download(imgSrcs);

    }
}
