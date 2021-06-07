package com.moxi.mogublog.utils.wechat;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 请求校验工具
 *
 * @author: 陌溪
 * @create: 2021-02-21-18:13
 */
public class SignUtil {

    // 与开发模式接口配置信息中的Token保持一致
    private static String token = "mogu2018";

    /**
     * 校验签名
     * @param signature 微信加密签名.
     * @param timestamp 时间戳.
     * @param nonce 随机数.
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        // 对token、timestamp、和nonce按字典排序.
        String[] paramArr = new String[] {token, timestamp, nonce};
        Arrays.sort(paramArr);

        // 将排序后的结果拼接成一个字符串.
        String content  = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);
        // 进行sha-1加密
        String ciphertext = encode(content);
        // 将sha1加密后的字符串与signature进行对比.
        return ciphertext != null ? ciphertext.equals(signature) : false;
    }

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * xml转map
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        InputStream ins = request.getInputStream();
        Document doc = reader.read(ins);
        Element root = doc.getRootElement();
        List<Element> list = (List<Element>)root.elements();
        for(Element e:list){
            map.put(e.getName(), e.getText());
        }
        ins.close();
        return map;
    }


}