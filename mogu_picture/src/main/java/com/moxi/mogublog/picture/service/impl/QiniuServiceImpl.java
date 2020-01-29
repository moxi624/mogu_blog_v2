package com.moxi.mogublog.picture.service.impl;


import com.moxi.mogublog.picture.service.QiniuService;
import com.moxi.mogublog.picture.util.QiniuUtil;
import com.moxi.mogublog.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 七牛云实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2020年1月20日20:05:45
 */
@Service
@Slf4j
public class QiniuServiceImpl implements QiniuService {

    @Override
    public Map<String, List<String>> uploadImgs(MultipartFile[] file, Map<String, String> qiNiuConfig) {
        Map<String, List<String>> resultMap = new HashMap<>();
        List<String> list = new LinkedList<>();
        String result = null;

        for (int i = 0; i < file.length; i++) {
            String fileName = file[i].getOriginalFilename();

            // 创建一个临时目录文件
            String tempFiles = "temp/"+fileName;
            File dest = new File(tempFiles);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            BufferedOutputStream out = null;
            QiniuUtil qn = new QiniuUtil();

            try {
                out = new BufferedOutputStream(new FileOutputStream(dest));
                out.write(file[i].getBytes());
                result = qn.uploadQiniu(dest, qiNiuConfig);

            } catch (Exception e) {
                log.error(e.getMessage());
            } finally{
                try {
                    if (null != out) {
                        out.flush();
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (dest.getParentFile().exists()) {
                    dest.delete();
                }
            }
        }
        log.info("imagesList == " + list);
        if (list.isEmpty()) {
            list.add("error");
        }
        resultMap.put("result",list);
        return resultMap;
    }
}
