
/*
 在t_admin表，增加个人履历字段，用于关于我页面
 @date 2020年2月10日10:49:56
*/

ALTER TABLE  t_admin ADD person_resume TEXT COMMENT "个人履历"


/*
 在t_web_config表，增加显示的列表字段，用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端
 @date 2020年2月12日10:49:56
*/
ALTER TABLE  t_web_config ADD show_list VARCHAR(255) COMMENT "显示的列表（用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端）"


/*
 增加字典类型表 和 字典数据表
 @date 2020年2月15日20:23:16
*/

CREATE TABLE `t_sys_dict_type` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `oid` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增键oid',
  `dict_name` varchar(255) DEFAULT NULL COMMENT '字典名称',
  `dict_type` varchar(255) DEFAULT NULL COMMENT '字典类型',
  `create_by_uid` varchar(32) NOT NULL COMMENT '创建人UID',
  `update_by_uid` varchar(32) NOT NULL COMMENT '最后更新人UID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `is_publish` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否发布(1:是，0:否)',  
  PRIMARY KEY (`uid`),
  KEY `oid` (`oid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='字典类型表'


CREATE TABLE `t_sys_dict_data` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `oid` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增键oid',
  `dict_type_uid` varchar(255) DEFAULT NULL COMMENT '字典类型UID',
  `dict_label` varchar(255) DEFAULT NULL COMMENT '字典标签',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '字典键值',
  `css_class` varchar(255) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(255) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认（1是 0否）,默认为0',
  `create_by_uid` varchar(32) NOT NULL COMMENT '创建人UID',
  `update_by_uid` varchar(32) NOT NULL COMMENT '最后更新人UID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `is_publish` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否发布(1:是，0:否)',
  PRIMARY KEY (`uid`),
  KEY `oid` (`oid`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='字典数据表'



/*
   修改t_admin和t_user的email字段 默认为空
   @date 2020年2月17日10:17:31
*/
ALTER TABLE t_user MODIFY COLUMN email VARCHAR(60) DEFAULT NULL COMMENT '邮箱';
ALTER TABLE t_admin MODIFY COLUMN email VARCHAR(60) DEFAULT NULL COMMENT '邮箱';

ALTER TABLE  t_sys_dict_type ADD sort int(11) NOT NULL DEFAULT '0' COMMENT '排序字段';
ALTER TABLE  t_sys_dict_data ADD sort int(11) NOT NULL DEFAULT '0' COMMENT '排序字段';


/*
   修改t_category_menu表，增加is_hidden字段
   @date 2020年2月21日21:23:28
*/
ALTER TABLE  t_category_menu ADD is_show tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否显示 1:是 0:否';

/*
   修改t_user表，增加comment_status字段
   @date 2020年3月10日15:35:04
*/
ALTER TABLE  t_user ADD comment_status tinyint(1) NOT NULL DEFAULT 1 COMMENT '评论状态 1:正常 0:禁言';
ALTER TABLE  t_user ADD ip_source varchar(255) NULL COMMENT 'ip来源';
ALTER TABLE  t_user ADD browser varchar(255) NULL COMMENT '浏览器';
ALTER TABLE  t_user ADD os varchar(255) NULL COMMENT '操作系统';

/*
   修改t_user表，增加startEmailNotification字段
   @date 2020年3月13日09:29:45
*/
ALTER TABLE  t_user ADD start_email_notification tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启邮件通知 1:开启 0:关闭';


/*
   修改t_comment表，增加type字段
   @date 2020年3月13日09:29:45
*/
ALTER TABLE  t_comment ADD type tinyint(1) NOT NULL DEFAULT 0 COMMENT '评论类型 1:点赞 0:评论';

/*
   修改t_link表，增加link_status字段
   @date 2020年3月15日11:53:54
*/
ALTER TABLE  t_link ADD link_status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '友链状态： 0 申请， 1：上架，  2：下架';
ALTER TABLE  t_link ADD user_uid VARCHAR(32) NULL COMMENT '申请用户UID';
ALTER TABLE  t_link ADD admin_uid VARCHAR(32) NULL COMMENT '操作管理员UID';


/*
   修改t_feedback表
   @date 2020年3月16日08:31:49
*/
ALTER TABLE t_feedback ADD admin_uid varchar(32) NULL COMMENT '管理员uid';
ALTER TABLE t_feedback ADD title varchar(255) NULL COMMENT '标题';
ALTER TABLE t_feedback ADD reply VARCHAR(255) NULL COMMENT '回复';
ALTER TABLE t_feedback ADD feedback_status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '反馈状态： 0：已开启  1：进行中  2：已完成  3：已拒绝';


/*
   修改t_user表，增加user_tag字段
   @date 2020年3月18日08:55:44
*/
ALTER TABLE t_user ADD user_tag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '用户标签：0：普通用户，1：管理员，2：博主 等';


/*
   修改t_category_menu表，增加menu_type 菜单类型字段
   @date 2020年3月21日08:33:28
*/
ALTER TABLE t_category_menu ADD menu_type TINYINT(1) NOT NULL DEFAULT 0 COMMENT '菜单类型 0: 菜单   1: 按钮';

/*
   修改t_blog表，增加是否开启评论
   @date 2020年3月29日21:51:21
*/
ALTER TABLE t_blog ADD start_comment TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否开启评论(0:否 1:是)';

/*
   修改t_comment表，增加 firstCommentUid，一级评论UID
   @date 2020年3月31日23:33:17
*/
ALTER TABLE t_comment ADD first_comment_uid VARCHAR(32) NULL COMMENT '一级评论UID';


