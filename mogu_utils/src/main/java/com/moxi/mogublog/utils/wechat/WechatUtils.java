//package com.moxi.mogublog.utils.wechat;
//
//import com.alibaba.fastjson.JSONObject;
//import com.bhudy.entity.BhudyPlugin;
//import com.bhudy.service.BhudyPluginService;
//import org.jdom2.Document;
//import org.jdom2.Element;
//import org.jdom2.JDOMException;
//import org.jdom2.input.SAXBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerConfigurationException;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import java.io.IOException;
//import java.io.StringReader;
//import java.io.StringWriter;
//import java.security.MessageDigest;
//import java.util.*;
//
///**
// * Created by Administrator on 2019/8/23/023.
// */
//@Component
//public final class WeChatUtils {
//
//    @Autowired
//    private WeChatUtils(BhudyPluginService bhudyPluginService) {
//        WeChatUtils.bhudyPluginService = bhudyPluginService;
//    }
//
//    private static BhudyPluginService bhudyPluginService; // 获取微信appid的接口
//
//    private static Map<String, String> tokenMap = null;
//
//    public static String wxGzhWorkOrder = "xxxxx"; // 工单状态通知Key
//    public static String weGzhReport = "xxxxxx"; // 报告生成通知Key
//
//
//    private static String authorizationCode = "authorization_code"; // 微信调用接口使用参数authorization_code
//    private static String clientCredential = "client_credential"; // 微信调用接口使用参数client_credential
//
//
//    /**
//     * 发送报告生成通知
//     * 报告生成通知Id: P9U-LYY4qtcKKqSoDb7sfqK4GlFQvDu8G5JxWOTkUQk
//     * <p>
//     * {{first.DATA}}
//     * 报告类型：{{keyword1.DATA}}
//     * 生成时间：{{keyword2.DATA}}
//     * {{remark.DATA}}
//     *
//     * @return 是否发送成功
//     */
//    public static boolean sendWeGzhReport(String openId, String first, String remark, Map<String, Object> keywordMap) {
//        return sendWxGzh(openId, WeChatUtils.weGzhReport, first, remark, keywordMap);
//    }
//
//    /**
//     * 发送微信公众号工单消息提醒
//     * 工单模板信息id: i5JtheQBLYM9VyByYR2EqrGlbdZiiFZVyA7rndbOAuM
//     * <p>
//     * {{first.DATA}}
//     * 工单编号：{{keyword1.DATA}}
//     * 工单标题：{{keyword2.DATA}}
//     * 时间：{{keyword3.DATA}}
//     * {{remark.DATA}}
//     *
//     * @return 是否发送成功
//     */
//    public static boolean sendWxGzhWorkOrder(String openId, String first, String remark, Map<String, Object> keywordMap) {
//        return sendWxGzh(openId, WeChatUtils.wxGzhWorkOrder, first, remark, keywordMap);
//    }
//
//    /**
//     * 发送微信公众号信息
//     * <p>
//     * 参数  是否必填   说明
//     * touser  是  接收者openid
//     * template_id 是  模板ID
//     * url 否  模板跳转链接（海外帐号没有跳转能力）
//     * miniprogram 否  跳小程序所需数据，不需跳小程序可不用传该数据
//     * appid   是  所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
//     * pagepath    否  所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），要求该小程序已发布，暂不支持小游戏
//     * data    是  模板数据
//     * color   否  模板内容字体颜色，不填默认为黑色
//     * <p>
//     * json数据模板
//     * {"touser":"OPENID","template_id":"ngqIpbwh8bUfcSsECmogfXcV14J0tQlEpBO27izEYtY","url":"http://weixin.qq.com/download",
//     * "miniprogram":{"appid":"xiaochengxuappid12345","pagepath":"index?foo=bar"},"data":{"first":{"value":"恭喜你购买成功！","color":"#173177"},
//     * "keyword1":{"value":"巧克力","color":"#173177"},"keyword2":{"value":"39.8元","color":"#173177"},"keyword3":{"value":"2014年9月22日","color":"#173177"},
//     * "remark":{"value":"欢迎再次购买！","color":"#173177"}}}
//     *
//     * @param openId     接收消息的用户openid
//     * @param templateId 消息模板id
//     * @param first      标题
//     * @param remark     结尾
//     * @param keywordMap 内容map
//     * @return 是否发送成功
//     */
//    public static boolean sendWxGzh(String openId, String templateId, String first, String remark, Map<String, Object> keywordMap) {
//        if (openId == null || openId.equals("")) return false;
//
//        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + WeChatUtils.getToken(BhudyPlugin.TENCENT_TAS);
//        Map<String, Object> map = new HashMap<>();
//        map.put("touser", openId);
//        map.put("template_id", templateId);
//        map.put("url", null);
//
//        Map<String, Object> miniprogramMap = new HashMap<>();
//
//
//        miniprogramMap.put("appid", bhudyPluginService.getBhudyPluginDataByType(BhudyPlugin.TENCENT_TAS).get(BhudyPlugin.appId));
//        //miniprogramMap.put("pagepath", "index");
//        map.put("miniprogram", miniprogramMap);
//
//        Map<String, Object> dataMap = new HashMap<>();
//
//        Map firstMap = new HashMap<>();
//        firstMap.put("value", first);
//        dataMap.put("first", firstMap);
//
//        for (Map.Entry entrySet : keywordMap.entrySet()) {
//            Map keyword1Map = new HashMap<>();
//            keyword1Map.put("value", entrySet.getValue());
//            dataMap.put(entrySet.getKey().toString(), keyword1Map);
//        }
//
//        Map remarkMap = new HashMap<>();
//        remarkMap.put("value", remark);
//        dataMap.put("remark", remarkMap);
//
//        map.put("data", dataMap);
//
//        Map<String, Object> resultMap = WeChatUtils.wxReqDataPost(url, JSONObject.toJSONString(map));
//        return resultMap != null;
//    }
//
//    /**
//     * 获取用户基本信息(UnionID机制)
//     * 在关注者与公众号产生消息交互后，公众号可获得关注者的OpenID（加密后的微信号，每个用户对每个公众号的OpenID是唯一的。对于不同公众号，同一用户的openid不同）。公众号可通过本接口来根据OpenID获取用户基本信息，包括昵称、头像、性别、所在城市、语言和关注时间。
//     * 请注意，如果开发者有在多个公众号，或在公众号、移动应用之间统一用户帐号的需求，需要前往微信开放平台（open.weixin.qq.com）绑定公众号后，才可利用UnionID机制来满足上述需求。
//     * <p>
//     * 参数  是否必须   说明
//     * access_token    是  调用接口凭证
//     * openid  是  普通用户的标识，对当前公众号唯一
//     * lang    否  返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
//     * ***********************************
//     *
//     * @return <br>
//     * 参数  说明
//     * subscribe   用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
//     * openid  用户的标识，对当前公众号唯一
//     * nickname    用户的昵称
//     * sex 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
//     * city    用户所在城市
//     * country 用户所在国家
//     * province    用户所在省份
//     * language    用户的语言，简体中文为zh_CN
//     * headimgurl  用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
//     * subscribe_time  用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
//     * unionid 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
//     * remark  公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
//     * groupid 用户所在的分组ID（兼容旧的用户分组接口）
//     * tagid_list  用户被打上的标签ID列表
//     * subscribe_scene 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENEPROFILE LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
//     * qr_scene    二维码扫码场景（开发者自定义）
//     * qr_scene_str    二维码扫码场景描述（开发者自定义）
//     * UnionID
//     */
//    public static Map<String, Object> getUserInfo(String openId, Integer type) {
//        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + WeChatUtils.getToken(type) + "&openid=" + openId + "&lang=zh_CN";
//        Map resultMap = WeChatUtils.wxReqDataGet(url);
//        return resultMap;
//    }
//
//    /**
//     * 获取公众号用户列表
//     * 公众号可通过本接口来获取帐号的关注者列表，关注者列表由一串OpenID（加密后的微信号，每个用户对每个公众号的OpenID是唯一的）组成。一次拉取调用最多拉取10000个关注者的OpenID，可以通过多次拉取的方式来满足需求。
//     * <p>
//     * 参数  是否必须   说明
//     * access_token    是  调用接口凭证
//     * next_openid 是  第一个拉取的OPENID，不填默认从头开始拉取
//     *
//     * @param nextOpenid 第一个拉取的OPENID，不填默认从头开始拉取
//     * @return <br>
//     * 参数    说明
//     * total   关注该公众账号的总用户数
//     * count   拉取的OPENID个数，最大值为10000
//     * data    列表数据，OPENID的列表
//     * next_openid 拉取列表的最后一个用户的OPENID
//     */
//    public static Map<String, Object> getOpenIdListMap(String nextOpenid) {
//        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + getToken(BhudyPlugin.TENCENT_TAS);
//        if (nextOpenid != null && nextOpenid.equals("")) {
//            url += "&next_openid=" + nextOpenid;
//        }
//
//        Map resultMap = WeChatUtils.wxReqDataGet(url);
//        return resultMap;
//    }
//
//    /**
//     * @param type 1是公众号请求
//     * @return * * *
//     * 属性 类型 说明
//     * access_token    string 获取到的凭证
//     * expires_in  number 凭证有效时间，单位：秒。目前是7200秒之内的值。
//     * errcode number 错误码
//     * errmsg  string 错误信息
//     * <p>
//     * =====================================
//     * <p>
//     * errcode 的合法值
//     * 值   说明 最低版本
//     * -1  系统繁忙，此时请开发者稍候再试
//     * 0   请求成功
//     * 40001   AppSecret 错误或者 AppSecret 不属于这个小程序，请开发者确认 AppSecret 的正确性
//     * 40002   请确保 grant_type 字段值为 client_credential
//     * 40013   不合法的 AppID，请开发者检查 AppID 的正确性，避免异常字符，注意大小写
//     */
//    public static String getToken(Integer type) {
//        Map<String, String> tokenMap = WeChatUtils.tokenMap;
//        // 微信的access_token有效期是7200秒，所以我们的过期时间要比微信的快token
//        if (tokenMap != null && Utils.formatLong(tokenMap.get("date")) >= (new Date().getTime() + (7200 * 1000))) {
//            return tokenMap.get("access_token");
//        }
//
//        Map<String, String> weChatMap = bhudyPluginService.getBhudyPluginDataByType(type);
//        String url = "https://api.weixin.qq.com/cgi-bin/token?appid=" + weChatMap.get(BhudyPlugin.appId) + "&secret=" + weChatMap.get(BhudyPlugin.appSecret) + "&grant_type=" + clientCredential;
//
//        Map<String, Object> resultMap = WeChatUtils.wxReqDataGet(url);
//        if (resultMap == null) throw new BhudyException(BhudyExceptionCode.CODE_29);
//
//        String token = (String) resultMap.get("access_token");
//        WeChatUtils.tokenMap = new HashMap<>();
//        WeChatUtils.tokenMap.put("date", String.valueOf(new Date().getTime()));
//        WeChatUtils.tokenMap.put("access_token", token);
//
//        return token;
//    }
//
//
//    /**
//     * @param code string 是    登录时获取的 code
//     * @return * * *
//     * 属性  类型 说明
//     * openid  string 用户唯一标识
//     * session_key string 会话密钥
//     * unionid string 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
//     * errcode number 错误码
//     * errmsg  string 错误信息
//     * <p>
//     * ===================================
//     * <p>
//     * errcode 的合法值
//     * 值   说明 最低版本
//     * -1  系统繁忙，此时请开发者稍候再试
//     * 0   请求成功
//     * 40029   code 无效
//     * 45011   频率限制，每个用户每分钟100次
//     */
//    public static Map<String, Object> getOpenIdAndSessionKey(String code, Integer type) {
//        Map<String, String> weChatMap = bhudyPluginService.getBhudyPluginDataByType(type);
//        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + weChatMap.get(BhudyPlugin.appId) + "&secret=" + weChatMap.get(BhudyPlugin.appSecret) + "&grant_type=" + authorizationCode + "&js_code=" + code;
//        return WeChatUtils.wxReqDataGet(url);
//    }
//
//
//    /**
//     * 微信Post请求
//     * 如果微信端返回错误码或者没有返回数据，这个方法直接返回null
//     * 该方法没有使用线程，可能会卡死
//     *
//     * @param url    请求的url
//     * @param params
//     * @return
//     */
//    public static Map<String, Object> wxReqDataPost(String url, String params) {
//        try {
//            Map<String, Object> reulstMap = Utils.reqPost(url, params);
//            if (reulstMap == null || (reulstMap.containsKey("errcode") && (Integer) reulstMap.get("errcode") != 0)) {
//                return null;
//            }
//            return reulstMap;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    /**
//     * 微信Get请求
//     * 如果微信端返回错误码或者没有返回数据，这个方法直接返回null
//     * 该方法没有使用线程，可能会卡死
//     *
//     * @param url 请求的url
//     * @return
//     */
//    public static Map<String, Object> wxReqDataGet(String url) {
//        try {
//            Map<String, Object> reulstMap = Utils.reqGetMap(url);
//            if (reulstMap == null || (reulstMap.containsKey("errcode") && (Integer) reulstMap.get("errcode") != 0)) {
//                return null;
//            }
//            return reulstMap;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//
//    /**
//     * 接收事件推送
//     * 用法@RequestMapping(value = "/wx/api/v1/receiveEventPush.do", method = RequestMethod.POST, consumes = {"application/xml", "text/xml"}, produces = {"application/xml;charset=utf-8", "text/xml;charset=utf-8"})
//     * public Object receiveEventPush(@RequestBody DOMSource domSource) {
//     * return WxUtils.receiveEventPush(domSource);
//     * }
//     *
//     * @param domSource
//     * @return * * *
//     * <p>
//     * 目录 MsgType Event
//     * 1 关注/取消关注事件
//     * 2 扫描带参数二维码事件
//     * 3 上报地理位置事件
//     * 4 自定义菜单事件
//     * 5 点击菜单拉取消息时的事件推送
//     * 6 点击菜单跳转链接时的事件推送
//     * <p>
//     * <p>
//     * 》》》1.关注/取消关注事件《《《
//     * 参数  描述
//     * ToUserName  开发者微信号
//     * FromUserName    发送方帐号（一个OpenID）
//     * CreateTime  消息创建时间 （整型）
//     * MsgType 消息类型，event
//     * Event   事件类型，subscribe(订阅)、unsubscribe(取消订阅)
//     * <p>
//     * <p>
//     * 》》》2.扫描带参数二维码事件《《《
//     * 参数  描述
//     * ToUserName  开发者微信号
//     * FromUserName    发送方帐号（一个OpenID）
//     * CreateTime  消息创建时间 （整型）
//     * MsgType 消息类型，event
//     * Event   事件类型，subscribe
//     * EventKey    事件KEY值，qrscene_为前缀，后面为二维码的参数值
//     * Ticket  二维码的ticket，可用来换取二维码图片
//     * <p>
//     * <p>
//     * 》》》3.上报地理位置事件《《《
//     * 参数  描述
//     * ToUserName  开发者微信号
//     * FromUserName    发送方帐号（一个OpenID）
//     * CreateTime  消息创建时间 （整型）
//     * MsgType 消息类型，event
//     * Event   事件类型，LOCATION
//     * Latitude    地理位置纬度
//     * Longitude   地理位置经度
//     * Precision   地理位置精度
//     * <p>
//     * <p>
//     * 》》》5.点击菜单拉取消息时的事件推送《《《
//     * 参数  描述
//     * ToUserName  开发者微信号
//     * FromUserName    发送方帐号（一个OpenID）
//     * CreateTime  消息创建时间 （整型）
//     * MsgType 消息类型，event
//     * Event   事件类型，CLICK
//     * EventKey    事件KEY值，与自定义菜单接口中KEY值对应
//     * <p>
//     * <p>
//     * 》》》6.点击菜单跳转链接时的事件推送《《《
//     * 参数  描述
//     * ToUserName  开发者微信号
//     * FromUserName    发送方帐号（一个OpenID）
//     * CreateTime  消息创建时间 （整型）
//     * MsgType 消息类型，event
//     * Event   事件类型，VIEW
//     * EventKey    事件KEY值，设置的跳转URL
//     * <p>
//     * <p>
//     * MsgType
//     * 消息类型，接收事件推送为event
//     * 消息类型，文本为text
//     * 消息类型，图片为image
//     * 消息类型，语音为voice
//     * 消息类型，视频为video
//     * 消息类型，音乐为music
//     * 消息类型，图文为news
//     */
//    public static String receiveEventPush(DOMSource domSource) {
//        Map<String, Object> xmlMap = domSourceToMap(domSource);
//
//        if (xmlMap == null) return "";
//
//        String msgType = (String) xmlMap.get("MsgType");
//        String fromUserName = (String) xmlMap.get("FromUserName");
//        String toUserName = (String) xmlMap.get("ToUserName");
//        String event = (String) xmlMap.get("Event");
//        String text;
//        if (msgType.equals("event")) {
//            if (event.equals("subscribe")) {
//                // 关注事件
//                text = "关注事件";
//            } else if (event.equals("unsubscribe")) {
//                // 取消关注事件
//                text = "取消关注事件";
//            } else {
//                text = "对不起，无法识别消息类型";
//            }
//        } else if (msgType.equals("text")) {
//            // 消息类型，为text
//            text = "消息类型，文本为text";
//        } else if (msgType.equals("image")) {
//            // 消息类型，图片为image
//            text = "消息类型，图片为image";
//        } else if (msgType.equals("voice")) {
//            // 消息类型，语音为voice
//            text = "消息类型，语音为voice";
//        } else if (msgType.equals("video")) {
//            // 消息类型，视频为video
//            text = "消息类型，视频为video";
//        } else if (msgType.equals("music")) {
//            // 消息类型，音乐为music
//            text = "消息类型，音乐为music";
//        } else if (msgType.equals("news")) {
//            // 消息类型，图文为news
//            text = "消息类型，图文为news";
//        } else {
//            text = "对不起，无法识别消息类型";
//        }
//
//        String returnData = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
//                "    <xml>" +
//                "        <Content>" + text + "</Content>" +
//                "        <ToUserName>" + fromUserName + "</ToUserName>" +
//                "        <FromUserName>" + toUserName + "</FromUserName>" +
//                "        <CreateTime>" + new Date().getTime() / 1000 + "</CreateTime>" +
//                "        <MsgType>text</MsgType>" +
//                "    </xml>";
//
//        //输出格式化后的json
//        return returnData;
//    }
//
//    private static Map<String, Object> domSourceToMap(DOMSource domSource) {
//        try {
//            StringWriter writer = new StringWriter();
//            StreamResult result = new StreamResult(writer);
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer transformer = tf.newTransformer();
//            transformer.transform(domSource, result);
//
//            SAXBuilder sb = new SAXBuilder();
//            Document doc = sb.build(new StringReader(writer.toString()));
//            Element root = doc.getRootElement();
//
//            JSONObject json = new JSONObject();
//            json.put(root.getName(), iterateElement(root));
//
//            Map<String, Object> dataMap = JSONObject.parseObject(json.toJSONString(), Map.class);
//            Map<String, Object> xmlMap = (Map<String, Object>) dataMap.get("xml");
//            return xmlMap;
//        } catch (TransformerConfigurationException e) {
//            e.printStackTrace();
//        } catch (TransformerException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JDOMException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    private static JSONObject iterateElement(Element element) {
//        List<Element> node = element.getChildren();
//        JSONObject obj = new JSONObject();
//        List list = null;
//        for (Element child : node) {
//            list = new LinkedList();
//            String text = child.getTextTrim();
//            if (text == null || text.equals("")) {
//                if (child.getChildren().size() == 0) {
//                    continue;
//                }
//                if (obj.containsKey(child.getName())) {
//                    list = (List) obj.get(child.getName());
//                }
//                list.add(iterateElement(child)); //遍历child的子节点
//                obj.put(child.getName(), list);
//            } else {
//                if (obj.containsKey(child.getName())) {
//                    Object value = obj.get(child.getName());
//                    try {
//                        list = (List) value;
//                    } catch (ClassCastException e) {
//                        list.add(value);
//                    }
//                }
//                if (child.getChildren().size() == 0) { //child无子节点时直接设置text
//                    obj.put(child.getName(), text);
//                } else {
//                    list.add(text);
//                    obj.put(child.getName(), list);
//                }
//            }
//        }
//        return obj;
//    }
//
//    /**
//     * 验证微信绑定服务器的方法
//     *
//     * @param signature
//     * @param timestamp
//     * @param nonce
//     * @return
//     */
//    public static boolean checkSignature(String signature, String timestamp, String nonce) {
//        //1.定义数组存放tooken，timestamp,nonce
//        String[] arr = {"7i6L5SEu4NPuYiGVAXMy0ZnFxd6", timestamp, nonce};
//
//        //2.对数组进行排序
//        Arrays.sort(arr);
//
//        //3.生成字符串
//        StringBuffer sb = new StringBuffer();
//        for (String s : arr) {
//            sb.append(s);
//        }
//
//        //4.sha1加密,网上均有现成代码
//        String temp = getSha(sb.toString());
//
//        //5.将加密后的字符串，与微信传来的加密签名比较，返回结果
//        return temp.equals(signature);
//    }
//
//
//    public static String getSha(String str) {
//        if (str == null || str.length() == 0) {
//            return null;
//        }
//        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//        try {
//
//            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
//
//            mdTemp.update(str.getBytes("UTF-8"));
//
//
//            byte[] md = mdTemp.digest();
//
//            int j = md.length;
//
//            char buf[] = new char[j * 2];
//
//            int k = 0;
//
//            for (int i = 0; i < j; i++) {
//
//                byte byte0 = md[i];
//
//                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
//
//                buf[k++] = hexDigits[byte0 & 0xf];
//
//            }
//
//            return new String(buf);
//
//        } catch (Exception e) {
//
//            // TODO: handle exception
//
//            return null;
//
//        }
//
//    }
//
//
//}