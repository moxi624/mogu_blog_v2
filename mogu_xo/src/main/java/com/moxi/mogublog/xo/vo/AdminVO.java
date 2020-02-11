package com.moxi.mogublog.xo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moxi.mougblog.base.vo.BaseVO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * AdminVO
 *
 * @author: 陌溪
 * @create: 2019-12-03-22:29
 */
@Data
public class AdminVO extends BaseVO<AdminVO> {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别(1:男2:女)
     */
    private String gender;

    /**
     * 个人头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 出生年月日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /**
     * 手机
     */
    private String mobile;

    /**
     * QQ号
     */
    private String qqNumber;

    /**
     * 微信号
     */
    private String weChat;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 自我简介最多150字
     */
    private String summary;

    /**
     * 个人履历
     */
    private String personResume;

    /**
     * github地址
     */
    private String github;

    /**
     * gitee地址
     */
    private String gitee;
}
