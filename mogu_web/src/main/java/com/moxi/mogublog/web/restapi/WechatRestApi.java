package com.moxi.mogublog.web.restapi;


import com.moxi.mogublog.utils.ResultUtil;
import com.moxi.mogublog.utils.wechat.SignUtil;
import com.moxi.mogublog.web.global.SysConf;
import com.moxi.mogublog.web.log.BussinessLog;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.WebConfigService;
import com.moxi.mougblog.base.enums.EBehavior;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信公众号 RestApi
 *
 * @author 陌溪
 * @date 2018年11月12日14:51:54
 */
@RestController
@RequestMapping("/wechat")
@Api(value = "关于我相关接口", tags = {"关于我相关接口"})
@Slf4j
public class WechatRestApi {

    @ApiOperation(value = "获取微信公众号状态", notes = "获取微信公众号状态")
    @GetMapping()
    public String wechatCheck(String signature, String timestamp, String echostr) {
        if(SignUtil.checkSignature(signature, timestamp, echostr)) {
            return echostr;
        }
        return "error";
    }


    @ApiOperation(value = "获取微信公众号登录二维码", notes = "获取微信公众号登录二维码")
    @GetMapping("/getWechatOrCodeTicket")
    public String getWechatOrCodeTicket() {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
        return ResultUtil.successWithData("http://image.moguit.cn/cb9ac3e6c1244a6f8c2cce667bd7c4ae");
    }


}

