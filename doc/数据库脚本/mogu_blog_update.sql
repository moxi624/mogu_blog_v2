
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


/*
   修改t_picture_sort表，增加 isShow，是否显示
   @date 2020年4月19日16:01:34
*/
ALTER TABLE t_picture_sort ADD is_show TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否显示，1：是，0，否';

/*
   修改t_web_config表，增加 start_email_notification，一级评论UID
   @date 2020年4月29日11:50:19
*/
ALTER TABLE  t_user ADD start_email_notification tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启邮件通知 1:开启 0:关闭';


/*
 在t_web_config表，增加登录方式列表（用于控制前端登录方式，如账号密码,码云,Github,QQ,微信）
 @date 2020年2月12日10:49:56
*/
ALTER TABLE  t_web_config ADD login_type_list VARCHAR(255) COMMENT "登录方式列表（用于控制前端登录方式，如账号密码,码云,Github,QQ,微信）";



/*
   外部URL，菜单栏点击打开新的页面
*/
ALTER TABLE  t_category_menu ADD is_jump_external_url tinyint(1) DEFAULT 0 COMMENT "是否跳转外部链接 0：否，1：是(如果跳转外链，那么路由则为外部url)";


/*
	参数配置表
*/

CREATE TABLE `t_sys_params` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `params_type` varchar(1) NOT NULL DEFAULT '1' COMMENT '配置类型 是否系统内置(1:，是 0:否)',
  `params_name` varchar(255) DEFAULT NULL COMMENT '参数名称',
  `params_key` varchar(255) DEFAULT NULL COMMENT '参数键名',
  `params_value` varchar(255) DEFAULT NULL COMMENT '参数键值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='参数配置表'


/*
	网站配置增加字段
*/
ALTER TABLE  t_web_config ADD open_mobile_comment TINYINT(1) NOT NULL DEFAULT 0 COMMENT "是否开启移动端评论(0:否， 1:是)";
ALTER TABLE  t_web_config ADD open_admiration TINYINT(1) NOT NULL DEFAULT 0 COMMENT "是否开启赞赏(0:否， 1:是)";
ALTER TABLE  t_web_config ADD open_mobile_admiration TINYINT(1) NOT NULL DEFAULT 0 COMMENT "是否开启移动端赞赏(0:否， 1:是)";


/*
	专题表
	@date 2020年8月27日07:57:27
*/

CREATE TABLE `t_subject` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `subject_name` varchar(255) DEFAULT NULL COMMENT '专题名称',
  `summary` varchar(255) DEFAULT NULL COMMENT '简介',
  `file_uid` varchar(32) DEFAULT NULL COMMENT '封面图片UID',
  `click_count` int(11) NOT NULL DEFAULT '0' COMMENT '专题点击数',
  `collect_count` int(11) NOT NULL DEFAULT '0' COMMENT '专题收藏数',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='专题表'


/*
	专题Item表
	@date 2020年8月27日07:57:27
*/

CREATE TABLE `t_subject_item` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `subject_uid` varchar(32) NOT NULL COMMENT '专题uid',
  `blog_uid` varchar(32) NOT NULL COMMENT '博客uid',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='专题Item表'


/*
	系统配置表 增加编辑器模式
*/
ALTER TABLE  t_system_config ADD editor_model TINYINT(1) NOT NULL DEFAULT 0 COMMENT "编辑器模式，(0：富文本编辑器CKEditor，1：markdown编辑器Veditor)";


/*
*	友情链接表添加 邮箱和图标字段
*/
ALTER TABLE  t_link ADD email VARCHAR(255) DEFAULT NULL COMMENT "站长邮箱";
ALTER TABLE  t_link ADD file_uid VARCHAR(255) DEFAULT NULL COMMENT "网站图标";

/*
* 系统配置增加主题颜色字段
* @date 2020年10月11日19:54:51
*/
ALTER TABLE t_system_config add theme_color varchar(10) default '#409EFF' not null COMMENT "主题颜色";


/*
	系统配置表 增加编辑器模式
	@date 2020年10月21日17:17:24
*/
ALTER TABLE  t_system_config ADD minio_end_point varchar(255) default NULL COMMENT "Minio远程连接地址";
ALTER TABLE  t_system_config ADD minio_access_key varchar(255) default NULL COMMENT "Minio公钥";
ALTER TABLE  t_system_config ADD minio_Secret_key varchar(255) default NULL COMMENT "Minio私钥";
ALTER TABLE  t_system_config ADD minio_bucket varchar(255) default NULL COMMENT "Minio桶";
ALTER TABLE  t_system_config ADD upload_minio TINYINT(1) NOT NULL DEFAULT 0 COMMENT "图片是否上传Minio (0:否， 1：是)";
ALTER TABLE  t_system_config ADD minio_picture_base_url varchar(255) default NULL COMMENT "Minio服务器文件域名前缀";


/*
	系统配置表 增加仪表盘弹框
	@date 2020年11月5日21:47:48
*/
ALTER TABLE  t_system_config ADD open_dashboard_notification TINYINT(1) NOT NULL DEFAULT 0 COMMENT "是否开启仪表盘通知(0:否， 1:是)";
ALTER TABLE  t_system_config ADD dashboard_notification longtext NULL COMMENT "仪表盘通知【用于首次登录弹框】";


/*
	博客表 增加外链
	@date 2020年11月7日10:49:29
*/
ALTER TABLE  t_blog ADD type TINYINT(1) NOT NULL DEFAULT 0 COMMENT "类型【0 博客， 1：推广】";
ALTER TABLE  t_blog ADD outside_link varchar(1024) NULL COMMENT "外链【如果是推广，那么将跳转到外链】";


/*
	博客表 增加oid字段
	@date 2020年11月13日10:11:09
*/
alter table t_blog add oid int auto_increment UNIQUE comment '唯一oid';


/*
	菜单表 增加新的数据
	@date 2021年1月9日16:58:36
*/
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('9bbe311a4ccb087560e6e2c6d40cf271','图片爬取','2','用于标题图片的爬取','7668dabe69473f59d1516d84cb99d583','/spider/pictureSpider','el-icon-picture-outline','0','1','2021-01-08 22:08:16','2021-01-08 22:08:16','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('7668dabe69473f59d1516d84cb99d583','爬虫管理','1','爬虫管理',NULL,'/spider','el-icon-search','0','1','2021-01-08 22:07:12','2021-01-08 22:07:12','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('5bf9bd28d387ef923f2c5d11ec01fbbd','按创建时间排序','3','按创建时间排序','7cb1a6b7462832bf831a18a28eea94cd','/subjectItem/sortByCreateTime',NULL,'0','1','2020-12-08 20:38:10','2020-12-08 20:38:10','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('af0e753d3ea0adf5cd8cf1dd55f162c2','接口聚合','2','聚合所有模块的接口','baace3dc03d34c54b81761dce8243814','http://localhost:8607/doc.html','el-icon-ice-cream-round','5','1','2020-12-05 15:42:51','2020-12-05 15:42:51','1','0','1');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('065cda845549289b2afcd0129d87c2c0','新增用户','3','新增用户','fb4237a353d0418ab42c748b7c1d64c6','/user/add',NULL,'0','1','2020-09-29 20:40:09','2020-09-29 20:40:30','1','1','0');


/*
   修改t_system_config 表，增加 contentPicturePriority 字段，博客详情图片显示
*/
ALTER TABLE t_system_config ADD content_picture_priority TINYINT(1) NOT NULL DEFAULT 0 COMMENT '博客详情图片显示优先级（ 0:本地  1: 七牛云 2: Minio）';


/*
   增加菜单导航栏
   @date 2021年2月28日21:19:06
*/
CREATE TABLE `t_web_navbar` (
  `uid` VARCHAR(96) DEFAULT NULL,
  `name` VARCHAR(765) DEFAULT NULL,
  `navbar_level` TINYINT(1) DEFAULT NULL,
  `summary` VARCHAR(600) DEFAULT NULL,
  `parent_uid` VARCHAR(96) DEFAULT NULL,
  `url` VARCHAR(765) DEFAULT NULL,
  `icon` VARCHAR(150) DEFAULT NULL,
  `is_show` TINYINT(1) DEFAULT NULL,
  `is_jump_external_url` TINYINT(1) DEFAULT NULL,
  `sort` INT(11) DEFAULT NULL,
  `status` TINYINT(1) DEFAULT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=INNODB DEFAULT CHARSET=utf8;

insert  into `t_web_navbar`(`uid`,`name`,`navbar_level`,`summary`,`parent_uid`,`url`,`icon`,`is_show`,`is_jump_external_url`,`sort`,`status`,`create_time`,`update_time`) values ('e186d3225e1405a0ee73995347b1c239','首页',1,'首页',NULL,'/','el-icon-remove',1,0,7,1,'2021-02-23 13:17:30','2021-02-23 17:05:31'),('c3e10b3c8d576ed24387934d5d0c0b81','关于我',1,'关于我',NULL,'/about','el-icon-user',1,0,6,1,'2021-02-23 13:18:43','2021-02-23 18:06:14'),('55bca78a37694c5a72b2910adde12d96','归档',1,'归档',NULL,'/sort','el-icon-camera',1,0,5,1,'2021-02-23 13:19:11','2021-02-23 15:30:37'),('abe6c960aa65fba7f728480fd933807f','分类',1,'博客分类',NULL,'/classify','el-icon-folder-checked',1,0,4,1,'2021-02-23 13:19:38','2021-02-23 15:30:43'),('0de9cfa4227c80530e43c534712156f5','标签',1,'博客标签',NULL,'/tag','el-icon-headset',1,0,3,1,'2021-02-23 13:20:01','2021-02-23 15:30:47'),('40277498960cfc2fb428c1ee4429676c','专题',1,'博客专题',NULL,'/subject','el-icon-data-analysis',1,0,2,1,'2021-02-23 13:20:22','2021-02-23 15:30:50'),('0d8a13137502ed28649888cfea40ee80','留言板',1,'留言板',NULL,'/messageBoard','el-icon-money',1,0,1,1,'2021-02-23 13:20:47','2021-02-23 15:30:53'),('94d59134ab64aac6d7994c80c6698f8a','博客源码',1,'博客源码',NULL,'https://gitee.com/moxi159753/mogu_blog_v2','el-icon-zoom-in',1,1,0,1,'2021-02-23 18:07:24','2021-02-27 13:05:34'),('a26eb81119b47bb34f733c4c38432021','博客详情',1,'博客详情页',NULL,'/info','el-icon-user',0,0,0,1,'2021-02-23 18:40:54','2021-02-23 18:40:54'),('7fb0684401ff99073c64dc956a9cb28a','博客列表',1,'博客列表',NULL,'/list','el-icon-picture-outline-round',0,0,0,1,'2021-02-23 18:57:19','2021-02-23 18:57:19'),('25bdfd2a854ea4af1bf1adcd676a711b','学习笔记',2,'学习笔记','94d59134ab64aac6d7994c80c6698f8a','https://gitee.com/moxi159753/LearningNotes','el-icon-folder-opened',1,1,0,1,'2021-02-27 12:43:40','2021-02-27 12:43:40'),('51f9db7329ccd382255c01f4ef371108','博客源码',2,'蘑菇博客源码','94d59134ab64aac6d7994c80c6698f8a','https://gitee.com/moxi159753/mogu_blog_v2','el-icon-tickets',1,1,0,1,'2021-02-27 13:06:13','2021-02-27 13:06:13');

/*
   t_category_menu增加菜单导航栏相关数据
   @date 2021年2月28日21:19:06
*/
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('1dd262b88b63e8f6bd9a6ca72ed0622c','导航栏管理 删除','3','导航栏管理 删除','6275bc5189e2e595b621d744d68278af','/webNavbar/delete',NULL,'0','1','2021-02-23 13:02:12','2021-02-23 13:02:12','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('21a411858fc22b5feb4c44fcad00e529','导航栏管理 编辑','3','导航栏管理 编辑','6275bc5189e2e595b621d744d68278af','/webNavbar/edit',NULL,'0','1','2021-02-23 13:01:36','2021-02-23 13:01:36','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('aa6c5d513421aa50cac1ee9ec647d100','导航栏管理 新增','3','导航栏管理 新增','6275bc5189e2e595b621d744d68278af','/webNavbar/add',NULL,'0','1','2021-02-23 13:01:16','2021-02-23 13:01:16','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('ffe445828071ce87a851ad58100f1340','导航栏管理 分页查询','3','导航栏管理 分页查询','6275bc5189e2e595b621d744d68278af','/webNavbar/getList',NULL,'0','1','2021-02-23 13:00:52','2021-02-23 13:00:52','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('b7fc36f7efc9738bddc9b09fedeccf60','导航栏管理 查询全部','3','导航栏管理 查询全部','6275bc5189e2e595b621d744d68278af','/webNavbar/getAllList',NULL,'0','1','2021-02-23 13:00:24','2021-02-23 13:00:24','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('6275bc5189e2e595b621d744d68278af','导航栏管理','2','导航栏管理','4fe7725159ced4a238b816a4595109d1','/web/webNavbar','el-icon-c-scale-to-original','0','1','2021-02-22 18:26:13','2021-02-22 18:26:13','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('4fe7725159ced4a238b816a4595109d1','门户管理','1','管理门户页面',NULL,'/web','el-icon-help','0','1','2021-02-22 18:25:34','2021-02-22 18:25:34','1','0','0');


/*
   修改t_system_config 表，增加 openEmailActivate 字段，是否开启注册用户邮件激活功能【0 关闭，1 开启】
   @date 2021年4月9日18:14:28
*/
ALTER TABLE t_system_config ADD open_email_activate TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否开启用户邮件激活功能【0 关闭，1 开启】';


/*
   修改t_system_config 表，增加 openEmailActivate 字段，是否开启注册用户邮件激活功能【0 关闭，1 开启】
   @date 2021年4月9日18:14:28
*/
ALTER TABLE t_blog ADD user_uid VARCHAR(32) DEFAULT NULL COMMENT '投稿用户UID';
ALTER TABLE t_blog ADD article_source TINYINT(1) NOT NULL DEFAULT 0 COMMENT '文章来源【0 后台添加，1 用户投稿】';


/*
   修改t_web_config 表，增加 open_create_blog 字段，是否开启用户创作(0:否， 1:是)
   @date 2021年4月9日18:14:28
*/
ALTER TABLE  t_web_config ADD open_create_blog TINYINT(1) NOT NULL DEFAULT 0 COMMENT "是否开启用户创作(0:否， 1:是)";


/*
   新增问答标签表
   @date 2021年4月26日21:22:08
*/
CREATE TABLE `t_question_tag` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `parent_uid` varchar(32) DEFAULT NULL COMMENT '父uid',
  `name` varchar(100) DEFAULT NULL COMMENT '标签名',
  `summary` varchar(1000) DEFAULT NULL COMMENT '标签简介',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `click_count` int(11) DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NULL COMMENT '创建时间',
  `update_time` timestamp NULL COMMENT '更新时间',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='问答标签表';


/*
   新增问答表
   @date 2021年4月26日21:38:20
*/
CREATE TABLE `t_question` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `oid` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一oid',
  `title` varchar(200) DEFAULT NULL COMMENT '问答标题',
  `summary` varchar(200) DEFAULT NULL COMMENT '问答简介',
  `content` longtext COMMENT '问答内容',
  `question_tag_uid` varchar(255) DEFAULT NULL COMMENT '问答uid',
  `click_count` int(11) DEFAULT '0' COMMENT '问答点击数',
  `collect_count` int(11) DEFAULT '0' COMMENT '问答收藏数',
  `reply_count` int(11) DEFAULT '0' COMMENT '回答次数',
  `file_uid` varchar(255) DEFAULT NULL COMMENT '标题图片uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL COMMENT '创建时间',
  `update_time` timestamp NULL COMMENT '更新时间',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户UID',
  `is_publish` varchar(1) DEFAULT '1' COMMENT '是否发布：0：否，1：是',
  `question_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '问答状态，0:创建，1:进行，2:已采纳',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  `open_comment` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  PRIMARY KEY (`uid`,`oid`),
  KEY `oid` (`oid`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='问答表';


/*
   菜单表新增数据
   @date 2021年5月12日19:14:20
*/
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('4e3bf287105637b86e27d8b4fd4e5f42','查询','3','问答管理 查询','3218dc3afad673f411baf774e22f3deb','/question/getList',NULL,'0','1','2021-04-28 19:16:45','2021-04-28 19:16:45','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('b0b7373219566e42618ee6269123bced','批量删除','3','问答管理 批量删除','3218dc3afad673f411baf774e22f3deb','/question/deleteBatch',NULL,'0','1','2021-04-28 19:06:59','2021-04-28 19:06:59','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('8f61d6c126c299016e8165629832ec05','编辑','3','问答管理 编辑','3218dc3afad673f411baf774e22f3deb','/question/edit',NULL,'0','1','2021-04-28 19:06:40','2021-04-28 19:06:40','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('f7913b2aae2b9c0dc3268a374036826e','新增','3','问答管理 新增','3218dc3afad673f411baf774e22f3deb','/question/add',NULL,'0','1','2021-04-28 19:06:24','2021-04-28 19:06:24','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('287ba89d9609e9f36ec60835a7dddfc7','删除','3','问答管理 删除','3218dc3afad673f411baf774e22f3deb','/question/delete',NULL,'0','1','2021-04-28 19:05:19','2021-04-28 19:05:19','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('3218dc3afad673f411baf774e22f3deb','问答管理','2','问答管理','0ef5e85a09b8987a2a723b40a9f77160','/question/question','el-icon-document','0','1','2021-04-28 18:41:20','2021-04-28 18:41:20','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('01704cae6dd21267e1a57089a53cf455','批量删除','3','问答标签 批量删除','94d5a5438d972e7661d3f6e262cf9773','/questionTag/deleteBatch',NULL,'0','1','2021-04-28 10:26:29','2021-04-28 10:31:29','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('f59f750fe16aac77e7435367bc95e2ba','删除','3','问答标签 删除','94d5a5438d972e7661d3f6e262cf9773','/questionTag/delete',NULL,'0','1','2021-04-27 23:35:19','2021-04-28 10:31:22','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('ba92c0532a96dfac964f91eec16c10e3','编辑','3','问答标签 编辑','94d5a5438d972e7661d3f6e262cf9773','/questionTag/edit',NULL,'0','1','2021-04-27 23:33:19','2021-04-27 23:33:19','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('da6f0cc43343ee9f8a741b700b67fcaf','查询','3','问答标签查询','94d5a5438d972e7661d3f6e262cf9773','/questionTag/getList',NULL,'0','1','2021-04-27 23:32:28','2021-04-27 23:32:28','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('758a05100a203f73ea6f1861ed155828','新增','3','问答标签新增','94d5a5438d972e7661d3f6e262cf9773','/questionTag/add',NULL,'0','1','2021-04-27 23:31:13','2021-04-27 23:31:13','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('94d5a5438d972e7661d3f6e262cf9773','问答标签','2','问答标签管理','0ef5e85a09b8987a2a723b40a9f77160','/question/questionTag','el-icon-collection-tag','0','1','2021-04-27 23:04:36','2021-04-27 23:04:36','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('0ef5e85a09b8987a2a723b40a9f77160','问答管理','1','问答管理',NULL,'/question','el-icon-headset','9','1','2021-04-27 23:03:55','2021-05-04 10:31:14','1','0','0');

/*
   导航栏表新增数据
   @date 2021年5月12日19:14:20
*/
insert into `t_web_navbar` (`uid`, `name`, `navbar_level`, `summary`, `parent_uid`, `url`, `icon`, `is_show`, `is_jump_external_url`, `sort`, `status`, `create_time`, `update_time`) values('fcda0fc94ab6c8da6a970e1f654e9ffb','问答','1','问答',NULL,'/question','el-icon-table-lamp','1','0','3','1','2021-05-05 16:57:20','2021-05-05 16:59:11');


insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('d01f71985808c64372f18a653df34fb0','28','问答状态','sys_question_status','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','问答状态','1','2021-05-05 12:49:42','2021-05-05 12:49:42','1','0');

insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('f775d45cb2ca89e0487654a60900bd31','75','d01f71985808c64372f18a653df34fb0','解决','2',NULL,'success','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','问答状态 解决','1','2021-05-05 12:53:34','2021-05-05 12:53:34','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('827c3607cb4f028030bdf48a2528c694','74','d01f71985808c64372f18a653df34fb0','回答','1',NULL,'primary','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','问答状态 回答','1','2021-05-05 12:53:17','2021-05-05 12:53:17','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('8232af8905226ca2567c61c6f15f3cb6','73','d01f71985808c64372f18a653df34fb0','创建','0',NULL,'info','1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','问答状态 创建','1','2021-05-05 12:50:20','2021-05-05 12:50:20','1','0');


insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('12708eed0b5a266dfad751e1e816ad48','29','数据状态','sys_data_status','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','数据状态：删除 冻结 正常','1','2021-05-13 21:49:24','2021-05-13 21:49:24','1','0');

insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('bdb5158bcb7c81ddc1f05fbb9d543b8e','78','12708eed0b5a266dfad751e1e816ad48','冻结','2',NULL,'warning','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','数据状态  冻结','1','2021-05-13 22:38:02','2021-05-13 22:39:49','1','2');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('c182c5e1895a31daa078e1aa919632c1','77','12708eed0b5a266dfad751e1e816ad48','正常','1',NULL,'success','1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','数据状态  正常','1','2021-05-13 22:37:45','2021-05-13 22:50:37','1','3');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('fb907e22b25c96e1a0da7bc5dada8670','76','12708eed0b5a266dfad751e1e816ad48','删除','0',NULL,'danger','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','数据状态 删除','1','2021-05-13 22:36:43','2021-05-13 22:39:52','1','1');


/*
 在t_web_config表，增加友链申请模板字段
 @date 2021年5月17日11:05:59
*/
ALTER TABLE  t_web_config ADD link_apply_template VARCHAR(2018) COMMENT "友链申请模板,添加友链申请模板格式";

/*
 在t_blog_sort表，增加icon图标
 @date 2021年5月17日11:05:59
*/
ALTER TABLE t_blog_sort ADD icon VARCHAR(32) DEFAULT NULL COMMENT '分类icon图标';
insert into `t_sys_params` (`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) values('b677d256d98ced4d057f2691215df949','1','最热分类数','HOT_BLOG_SORT_COUNT','首页显示的最热分类数','5','1','2021-06-02 18:20:37','2021-06-02 18:58:39','0');

/*
   修改t_web_config 表，增加 open_create_question 字段，是否开启问答 (0:否， 1:是)
   @date 2021年4月9日18:14:28
*/
ALTER TABLE  t_web_config ADD open_create_question TINYINT(1) NOT NULL DEFAULT 0 COMMENT "是否开启问答 (0:否， 1:是)";


/*
   新增问答模板表
   @date 2021年6月9日08:24:07
*/
CREATE TABLE `t_question_template` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `name` varchar(200) DEFAULT NULL COMMENT '模板名称',
  `summary` varchar(200) DEFAULT NULL COMMENT '模板简介',
  `content` longtext COMMENT '模板内容',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL COMMENT '创建时间',
  `update_time` timestamp NULL COMMENT '更新时间',
  `is_publish` varchar(1) DEFAULT '1' COMMENT '是否发布：0：否，1：是',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='问答模板表';

insert into `t_question_template` (`uid`, `name`, `summary`, `content`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('732b83c9661290b868142ac4f7f877ed','开发实战相关','开发实战相关','<h2>问题描述</h2>\n\n<p>&nbsp;</p>\n\n<h2>问题出现的环境背景及自己尝试过哪些方法</h2>\n\n<p>&nbsp;</p>\n\n<h2>相关代码</h2>\n\n<p>粘贴代码文本（请勿用截图）</p>\n\n<p>&nbsp;</p>\n\n<h2>你期待的结果是什么？实际看到的错误信息又是什么？</h2>\n','1','2021-06-09 17:08:09','2021-06-09 18:47:05','1','0');
insert into `t_question_template` (`uid`, `name`, `summary`, `content`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('90a2b545283d8d564a1a990955e735b1','编程问题相关','编程问题相关','<h2>题目描述</h2>\n\n<p>&nbsp;</p>\n\n<h2>题目来源及自己的思路</h2>\n\n<p>&nbsp;</p>\n\n<h2>相关代码</h2>\n\n<p>粘贴代码文本（请勿用截图）</p>\n\n<p>&nbsp;</p>\n\n<h2>你期待的结果是什么？实际看到的错误信息又是什么？</h2>\n','1','2021-06-09 18:53:14','2021-06-09 18:53:14','1','0');
insert into `t_question_template` (`uid`, `name`, `summary`, `content`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('c92dcaeda36b513c20e41218d6ad26c4','工具软件相关','工具软件相关','<h2>问题描述</h2>\n\n<p>&nbsp;</p>\n\n<h2>问题出现的平台版本及自己尝试过哪些方法</h2>\n\n<p>&nbsp;</p>\n\n<h2>相关代码</h2>\n\n<p>粘贴代码文本（请勿用截图）</p>\n\n<p>&nbsp;</p>\n\n<h2>你期待的结果是什么？实际看到的错误信息又是什么？</h2>\n','1','2021-06-09 18:53:50','2021-06-09 18:53:50','1','0');


INSERT INTO `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES('f8d4b1a29db0086a1465e745f78dc489','获取列表','3','问答模板 获取列表','2cfdc779041f420b309c4da77ec0c7db','/questionTemplate/getList',NULL,'0','1','2021-06-09 09:56:14','2021-06-09 09:56:14','1','1','0');
INSERT INTO `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES('14df8d2b303589f0797cab35ebe3ae4b','批量删除','3','问答模板 批量删除','2cfdc779041f420b309c4da77ec0c7db','/questionTemplate/deleteBatch',NULL,'0','1','2021-06-09 09:55:47','2021-06-09 09:55:47','1','1','0');
INSERT INTO `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES('d1e93bf542e130c221f3b29ff5507ae3','编辑','3','问答模板 编辑','2cfdc779041f420b309c4da77ec0c7db','/questionTemplate/edit',NULL,'0','1','2021-06-09 09:55:16','2021-06-09 09:55:16','1','1','0');
INSERT INTO `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES('b6d9fcb52c1513b78187c025384e2ad5','新增','3','新增问答模板','2cfdc779041f420b309c4da77ec0c7db','/questionTemplate/add',NULL,'0','1','2021-06-09 09:54:54','2021-06-09 09:54:54','1','1','0');
INSERT INTO `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES('2cfdc779041f420b309c4da77ec0c7db','问答模板','2','问答模板','0ef5e85a09b8987a2a723b40a9f77160','/question/questionTemplate','el-icon-notebook-1','0','1','2021-06-09 08:32:24','2021-06-09 08:32:24','1','0','0');

/*
 在t_question表，增加问答模板字段
 @date 2021年6月9日19:56:57
*/
ALTER TABLE  t_question ADD question_template_uid VARCHAR(32) COMMENT "问答模板UID";

/*
 在t_question表，增加问答来源【0 后台添加，1 门户添加】
 @date 2021年6月9日19:56:57
*/
ALTER TABLE t_question ADD question_source TINYINT(1) NOT NULL DEFAULT 1 COMMENT '问答来源【0 后台添加，1 用户添加】';


/*

 @date 2021年6月13日19:56:57
*/
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('13ff59a74df2979c0c98a2f810c8bb8c','29','文章来源','sys_article_source','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','文章来源','1','2021-06-13 11:06:22','2021-06-13 11:28:40','1','0');

insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('332f266d0f5e06a8169ed8d7d39ef04b','77','13ff59a74df2979c0c98a2f810c8bb8c','后台上架','0',NULL,'warning','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','后台上架','1','2021-06-13 11:23:55','2021-06-13 11:56:55','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('24f8476073d8f76093e2b344ccb5708b','76','13ff59a74df2979c0c98a2f810c8bb8c','用户投稿','1',NULL,'primary','1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','用户投稿','1','2021-06-13 11:21:55','2021-06-13 11:21:55','1','0');


/*
   新增用户关注表
   @date 2021年6月13日16:17:02
*/
CREATE TABLE `t_user_watch` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '关注人UID',
  `to_user_uid` varchar(32) DEFAULT NULL COMMENT '被关注者UID',
  `is_admin` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否是管理员：0否，1是',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL COMMENT '创建时间',
  `update_time` timestamp NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='用户关注表';


/*
   新增通知表
   @date 2021年8月7日07:47:47
*/
CREATE TABLE `t_notice` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户uid',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `notice_status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '通知状态：0:已创建，1:已查看',
  `notice_type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '通知类型',
  `context` text COMMENT '通知内容',
  `business_uid` varchar(32) DEFAULT NULL COMMENT '业务uid',
  `business_type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '业务类型 【博客，问答，评论】',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL COMMENT '创建时间',
  `update_time` timestamp NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='通知表';


/*
   加载校验
   @date 2021年8月7日07:48:04
*/
ALTER TABLE t_user ADD loading_valid TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否通过加载校验【0 未通过，1 已通过】';

/*
   增加敏感词
   @date 2021年10月27日08:45:22
*/
insert into `t_sys_params` (`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) values('5522a84437c971392ba4ff9ffd2121f5','1','敏感词','SYS_SENSITIVE_WORD','敏感词','垃圾;辣鸡;','1','2021-10-26 12:59:36','2021-10-26 22:09:12','0');

insert into `t_sys_params` (`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) values('614dea888800aaf04d4f321e4399c636','1','问答发布限制数','USER_PUBLISH_QUESTION_COUNT','问答发布限制数','2','1','2021-10-29 00:09:35','2021-10-29 00:09:35','0');
insert into `t_sys_params` (`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) values('4ce28737a02c37cea2a1ac5eb36304c9','1','博客发布限制数','USER_PUBLISH_BLOG_COUNT','用户单日博客发布限制','2','1','2021-10-29 00:09:03','2021-10-29 00:09:40','0');


/*
   新增用户签到表
   @date 2021年11月27日07:47:47
*/
CREATE TABLE `t_sign_in` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户uid',
  `latest_sign_date` varchar(32) DEFAULT NULL COMMENT '最近的签到时间',
  `consecutive_sign_count` int DEFAULT 0 COMMENT '连续签到次数',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL COMMENT '创建时间',
  `update_time` timestamp NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='用户签到表';


/*
   获取/消耗积分日志表
   @date 2021年11月27日07:47:47
*/
CREATE TABLE `t_credits_log` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户uid',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户uid',
  `resource_uid` varchar(32) DEFAULT NULL COMMENT '操作的资源uid',
  `action_code` varchar(32) DEFAULT NULL COMMENT '动作码',
  `action_name` varchar(32) DEFAULT NULL COMMENT '动作名称',
  `old_credits` int DEFAULT 0 COMMENT '旧的积分数',
  `new_credits` int DEFAULT 0 COMMENT '新的积分数',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL COMMENT '创建时间',
  `update_time` timestamp NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='获取/消耗积分日志表';


/*
   用户表新增积分功能
   @date 2021年11月27日07:47:47
*/
ALTER TABLE t_user ADD credits int NOT NULL DEFAULT 0 COMMENT '积分';


/*
   用户表新增背景功能
   @date 2021年11月27日07:47:47
*/
ALTER TABLE t_user ADD background_file_uid varchar(32) DEFAULT NULL COMMENT '个人中心背景图片';
ALTER TABLE t_admin ADD user_uid varchar(32) DEFAULT NULL COMMENT '管理员绑定的用户【绑定的用户后，将以该用户的名义进行创作】';

insert into `t_sys_params` (`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) values('37cb9333403e391030927d915908b007','1','用户积分排行榜','USER_TOP_N','用户积分排行榜人数','4','1','2021-11-28 19:20:24','2021-11-28 19:36:34','0');






/*
   新增审批字段
   @date 2021年11月27日07:47:47
*/
ALTER TABLE t_blog ADD audit_status tinyint(1) unsigned NOT NULL DEFAULT 1 COMMENT '审批状态【0：待审批，1：审核未通过，2：审核通过】';
ALTER TABLE t_blog ADD audit_name varchar(32) DEFAULT NULL COMMENT '审批人';
ALTER TABLE t_blog ADD reject_reason varchar(255) DEFAULT NULL COMMENT '审批拒绝原因';
ALTER TABLE t_blog ADD audit_time timestamp NULL COMMENT '审批时间';

insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('38b324261de1cedbb45c30eeee276f70','32','审批状态','sys_audit_status','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','审批状态','1','2021-11-30 08:31:04','2021-11-30 08:31:04','1','0');

insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('64ca3257cd6bae549ca82ffd65bbf90d','86','38b324261de1cedbb45c30eeee276f70','待审批','0',NULL,'danger','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2021-11-30 08:55:42','2021-11-30 08:56:08','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('23b587cf1116c6db057c52e910d47dfd','85','38b324261de1cedbb45c30eeee276f70','审批拒绝','1',NULL,'danger','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','审批拒绝','1','2021-11-30 08:32:15','2021-11-30 08:56:04','1','1');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('8ebc1d994bffcf334bfebe1e941387d1','84','38b324261de1cedbb45c30eeee276f70','审批通过','2',NULL,'success','1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','审批通过','1','2021-11-30 08:31:57','2021-11-30 08:55:50','1','2');

insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('532f3e66d5ab6b6b75133963195d4df2','审批','3','博客审批','1f01cd1d2f474743b241d74008b12333','/blog/batchAuditBlog',NULL,'0','1','2021-11-30 08:51:12','2021-11-30 08:51:36','1','1','0');


/**
  新增 发布/下架 菜单权限

 */
INSERT INTO `mogu_blog_business`.`t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('53c3783f1b8d78d78aef4ba803de9c23', '发布/下架博客', 3, '发布/下架博客', '1f01cd1d2f474743b241d74008b12333', '/blog/publish', NULL, 0, 1, '2021-12-01 17:30:59', '2021-12-01 17:30:59', 1, 1, 0);

/*
   用户表新增编辑模式字段【切换富文本编辑器和markdown编辑器】
   @date 2021年12月1日07:47:47
*/
ALTER TABLE t_user ADD editor_model tinyint(1) unsigned NOT NULL DEFAULT 1 COMMENT '编辑器模式，(0：富文本编辑器CKEditor，1：markdown编辑器Veditor)';


/*
   更新字段的字符格式，支持emoji表情
   @date 2021年12月3日00:30:47
*/
ALTER TABLE t_user MODIFY COLUMN occupation VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '职业';
ALTER TABLE t_user MODIFY COLUMN summary VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '自我简介最多150字';



ALTER TABLE t_blog MODIFY COLUMN title VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '博客标题';
ALTER TABLE t_blog MODIFY COLUMN author VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '作者';
ALTER TABLE t_blog MODIFY COLUMN summary VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '博客简介';
ALTER TABLE t_blog MODIFY COLUMN content TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '博客内容';


ALTER TABLE t_question MODIFY COLUMN title VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '博客标题';
ALTER TABLE t_question MODIFY COLUMN summary VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '博客简介';
ALTER TABLE t_question MODIFY COLUMN content TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '博客内容';

ALTER TABLE t_web_visit MODIFY COLUMN other_data TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '附加数据(比如搜索内容)';

ALTER TABLE t_web_config MODIFY COLUMN  NAME VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '网站名称';
ALTER TABLE t_web_config MODIFY COLUMN  title VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '标题';
ALTER TABLE t_web_config MODIFY COLUMN summary VARCHAR(255)  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '介绍';
ALTER TABLE t_web_config MODIFY COLUMN keyword VARCHAR(255)  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '关键字';
ALTER TABLE t_web_config MODIFY COLUMN author VARCHAR(255)  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '作者';
ALTER TABLE t_web_config MODIFY COLUMN record_num VARCHAR(255)  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备案号';



/*
 Navicat Premium Data Transfer

 Source Server         : 社区版
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : 23.224.143.222:3306
 Source Schema         : mogu_blog_business

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 04/12/2021 19:28:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_report
-- ----------------------------
DROP TABLE IF EXISTS `t_report`;
CREATE TABLE `t_report`  (
                             `uid` VARCHAR(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '主键',
                             `report_type` VARCHAR(50) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '分类（博客/问答/评论）',
                             `report_user_uid` VARCHAR(50) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '被举报人uid',
                             `report_content_uid` VARCHAR(50) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT 'uid',
                             `user_uid` VARCHAR(50) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '举报人uid',
                             `progress` TINYINT(0) UNSIGNED NOT NULL DEFAULT 0 COMMENT '进展状态: 0 未处理   1: 已查看  2：已处理',
                             `status` TINYINT(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态  （0 删除  1正常）',
                             `create_time` TIMESTAMP(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `update_time` TIMESTAMP(0) NULL DEFAULT NULL COMMENT '更新时间',
                             `violation_type` VARCHAR(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '违规类型',
                             `content` VARCHAR(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '举报内容',
                             PRIMARY KEY (`uid`) USING BTREE,
                             UNIQUE INDEX `id`(`uid`) USING BTREE
) ENGINE = INNODB CHARACTER SET = utf8mb4  COMMENT = '内容举报信息' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `mogu_blog_business`.`t_sys_dict_type`(`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('023eed2b528c585cea65fcac1a46db3c', 34, '评论举报类型', 'sys_comment_report_type', '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', '评论举报类型', 1, '2021-12-03 15:33:50', '2021-12-03 15:33:59', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_type`(`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('221d5f68584e937e7a9a04f544eb2fb7', 33, '内容举报类型', 'sys_content_report_type', '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', '内容举报类型', 1, '2021-12-03 15:24:11', '2021-12-03 15:32:50', '1', 0);

INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('331a17f267fd3d33d489528aca002ca8', 98, '023eed2b528c585cea65fcac1a46db3c', '违法违禁', '0', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:34:22', '2021-12-03 15:34:22', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('397dabc1d6679e8917b0bd22d3b70a45', 105, '023eed2b528c585cea65fcac1a46db3c', '涉嫌个人隐私', '7', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:35:28', '2021-12-03 15:35:28', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('46eba4b9e3b596a9b4cb8a622d7dadc2', 102, '023eed2b528c585cea65fcac1a46db3c', '血腥暴力', '4', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:34:54', '2021-12-03 15:34:54', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('603e0699d21efc7d4dc4391ccbb63107', 99, '023eed2b528c585cea65fcac1a46db3c', '色情', '1', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:34:29', '2021-12-03 15:34:29', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('7968decd1305ef2770a51653ffef0954', 103, '023eed2b528c585cea65fcac1a46db3c', '人身攻击', '5', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:35:04', '2021-12-03 15:35:04', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('7f3b25f9e33411907e8bcbe22dcf3151', 104, '023eed2b528c585cea65fcac1a46db3c', '引战', '6', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:35:17', '2021-12-03 15:35:17', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('9ac5eeb64aaa1f246905f3236d7be96a', 106, '023eed2b528c585cea65fcac1a46db3c', '有其他问题', '8', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:35:39', '2021-12-03 15:35:39', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('a4fea13a55ee91e76002a0fe9ab30182', 100, '023eed2b528c585cea65fcac1a46db3c', '低俗', '2', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:34:36', '2021-12-03 15:34:36', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('eb41378fca0eb8f298f8bfbdd469e970', 101, '023eed2b528c585cea65fcac1a46db3c', '赌博诈骗', '3', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:34:44', '2021-12-03 15:34:44', '1', 0);

INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('027cda93d736123d9553e4edf692c5e7', 97, '221d5f68584e937e7a9a04f544eb2fb7', '有其他问题', '10', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:30:55', '2021-12-03 15:30:55', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('24a86c58140ac5290cc0853a725a62f2', 92, '221d5f68584e937e7a9a04f544eb2fb7', '人身攻击', '5', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:27:52', '2021-12-03 15:27:52', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('331a3276054a75c143fd53429a8c8e45', 89, '221d5f68584e937e7a9a04f544eb2fb7', '低俗', '2', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:27:15', '2021-12-03 15:27:15', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('4388c150a36826f1ef7aa18c164eda9b', 87, '221d5f68584e937e7a9a04f544eb2fb7', '违法违禁', '0', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:26:43', '2021-12-03 15:26:43', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('663c62090001705f84eccaff3b2d85de', 96, '221d5f68584e937e7a9a04f544eb2fb7', '转载/原创类型错误', '9', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:30:41', '2021-12-03 15:30:41', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('76d6cba2e899fb63f37706f1af6957c4', 91, '221d5f68584e937e7a9a04f544eb2fb7', '血腥暴力', '4', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:27:34', '2021-12-03 15:28:37', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('7f81e25214cb16cc8d41fa5bea3ba60f', 88, '221d5f68584e937e7a9a04f544eb2fb7', '色情', '1', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:27:05', '2021-12-03 15:27:05', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('910e6216ed547a2c7a4feddfd4bf66f7', 95, '221d5f68584e937e7a9a04f544eb2fb7', '不良封面/标题', '8', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:29:11', '2021-12-03 15:30:18', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('952a40a014f56a1fc6f445e7ae51679c', 94, '221d5f68584e937e7a9a04f544eb2fb7', '青少年不良信息', '7', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:29:00', '2021-12-03 15:29:00', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('b481e56f99b336b426fba98c8025d7f9', 93, '221d5f68584e937e7a9a04f544eb2fb7', '引战', '6', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:28:49', '2021-12-03 15:28:49', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('f17c906835e1963e21e4f866d66da5eb', 90, '221d5f68584e937e7a9a04f544eb2fb7', '赌博诈骗', '3', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-03 15:27:25', '2021-12-03 15:27:25', '1', 0);


/*
   是否开启加载校验【控制阅读更多按钮】
   @date 2021年12月9日22:35:36
*/
ALTER TABLE t_blog ADD open_loading_valid TINYINT(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否开启加载校验，(0：不开启，1：开启)';
ALTER TABLE t_web_config ADD open_loading_valid TINYINT(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否开启加载校验，(0：不开启，1：开启)';
ALTER TABLE t_web_config ADD loading_valid_text TEXT NULL COMMENT '加载校验文本';
ALTER TABLE t_web_config ADD loading_valid_file_uid VARCHAR(32) NULL COMMENT '加载校验图uid';



/**
  更新审核的url
 */
UPDATE `mogu_blog_business`.`t_category_menu` SET `name` = '审批', `menu_level` = 3, `summary` = '博客审批', `parent_uid` = '1f01cd1d2f474743b241d74008b12333', `url` = '/blog/audit', `icon` = NULL, `sort` = 0, `status` = 1, `create_time` = '2021-11-30 08:51:12', `update_time` = '2021-12-15 16:34:48', `is_show` = 1, `menu_type` = 1, `is_jump_external_url` = 0 WHERE `uid` = '532f3e66d5ab6b6b75133963195d4df2';



ALTER TABLE t_credits_log ADD change_credits int(11)  DEFAULT 0 COMMENT '变更的积分';
update t_credits_log set change_credits = new_credits - old_credits;
update t_credits_log set action_name = '每日签到'  where action_name= '签到';
update t_credits_log set action_name = '创作文章'  where action_name= '文章';
update t_credits_log set action_name = '参与评论'  where action_name= '评论';
update t_credits_log set action_name = '发起问答'  where action_name= '问答';

/**
  2021/12/18 签到更新
 */

DROP TABLE IF EXISTS `t_sign_in_record`;
CREATE TABLE `t_sign_in_record`  (
     `uid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一uid',
     `user_uid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户uid',
     `sign_date` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签到时间',
     `sign_type` tinyint(1) NULL DEFAULT NULL COMMENT '签到类型(0:每日签到，1:补签)',
     `status` tinyint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态',
     `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
     `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
     PRIMARY KEY (`uid`) USING BTREE,
     UNIQUE INDEX `user_uid&sign_date`(`user_uid`, `sign_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户签到表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO t_sign_in_record ( uid, user_uid, sign_date, sign_type, `status`, create_time, update_time )
 (
    SELECT uid, user_uid, INSERT ( INSERT ( latest_sign_date, 5, 0, "-" ), 8, 0, "-" ) AS sign_date, 0 AS sign_type, 1 AS STATUS,NOW( ) AS create_time,NOW( ) AS update_time
    FROM  t_sign_in
)


/*
	用户权益记录表；包括：VIP特权、签到卡、兑换的资源；
*/
CREATE TABLE `t_user_equity_record`  (
     `uid` VARCHAR(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一uid',
     `user_uid` VARCHAR(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户uid',
	 `equity_uid` VARCHAR(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权益uid',
     `equity_type` TINYINT(1) NULL DEFAULT NULL COMMENT '权益类型(1:VIP特权, 2:签到卡, 3:兑换的资源)',
	 `is_permanent` TINYINT(1) DEFAULT 0 COMMENT '是否永久生效（0：否，1：是）',
	 `use_status` TINYINT(1) DEFAULT 0 COMMENT '使用状态（0：未使用，1：已使用，2：已过期）',
	 `start_time` TIMESTAMP(0) NULL DEFAULT NULL COMMENT '权益生效时间',
     `end_time` TIMESTAMP(0) NULL DEFAULT NULL COMMENT '权益截止时间',
     `status` TINYINT(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态',
     `create_time` TIMESTAMP(0) NULL DEFAULT NULL COMMENT '创建时间',
     `update_time` TIMESTAMP(0) NULL DEFAULT NULL COMMENT '更新时间',
     PRIMARY KEY (`uid`) USING BTREE,
	 KEY `index_user_status` (`user_uid`, `use_status`)
) ENGINE = INNODB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户权益记录表' ROW_FORMAT = DYNAMIC;




/*
   增加媒资模块
   @date 2021年12月19日
*/


-- 媒资字典类型

INSERT INTO `t_sys_dict_type`(`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('fef03e6a511b6ddf533a91b2e818e219', 38, '语言', 'lang', '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', '语言', 1, '2021-12-16 22:10:35', '2021-12-16 22:10:35', '1', 0);
INSERT INTO `t_sys_dict_type`(`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('4f919b583611d14057a98a2101fd7fce', 37, '演员标签', 'actor_label', '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', '演员标签', 1, '2021-12-16 21:07:48', '2021-12-16 21:07:48', '1', 0);
INSERT INTO `t_sys_dict_type`(`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('7cb2bbba02dfd05abeadedd66d7cbe7c', 36, '媒资类型', 'media_type', '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', '媒资类型', 1, '2021-12-14 22:42:49', '2021-12-14 22:42:49', '1', 0);
INSERT INTO `t_sys_dict_type`(`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('f550e45ae76d8cb786fb0b8b1fb34c51', 35, '国家', 'media_country', '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', '国家', 1, '2021-12-14 22:40:04', '2021-12-14 22:40:04', '1', 0);


-- 媒资字典数据

INSERT INTO `t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('f2efa764c3a220da4b7b00d822631653', 112, 'fef03e6a511b6ddf533a91b2e818e219', '国语', 'chinese', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-16 22:11:02', '2021-12-16 22:11:02', '1', 0);
INSERT INTO `t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('d66e4a98c27535ddcdb323b9290ee61a', 111, '4f919b583611d14057a98a2101fd7fce', '演员', 'actor', NULL, 'default', 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-16 21:08:31', '2021-12-16 21:08:31', '1', 0);
INSERT INTO `t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('fd982b80d063549ecde380fafb70852e', 110, '7cb2bbba02dfd05abeadedd66d7cbe7c', '动漫', 'cartoon', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-14 22:44:01', '2021-12-14 22:44:01', '1', 0);
INSERT INTO `t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('30caeb517c6eda287e2b888c1e4648c1', 109, '7cb2bbba02dfd05abeadedd66d7cbe7c', '电影', 'film', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-14 22:43:46', '2021-12-14 22:43:46', '1', 0);
INSERT INTO `t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('92501946fdff342e2aedf4d94e6a55d3', 108, '7cb2bbba02dfd05abeadedd66d7cbe7c', '电视剧', 'teleplay', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-14 22:43:28', '2021-12-14 22:43:28', '1', 0);
INSERT INTO `t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('d64917f4692e3fe0ca1b9d2255b61dab', 107, 'f550e45ae76d8cb786fb0b8b1fb34c51', '中国', 'china', NULL, 'primary', 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2021-12-14 22:40:55', '2021-12-14 22:40:55', '1', 0);






-- 媒资模块菜单  信息 分类 标签 演员

INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('2db5aad78770ae5b59cf9ce81d0b4256', '媒资演员', 2, '媒资演员', '2f0c03ed96e59c2d1a236862934c2d16', '/media/actor', 'el-icon-s-release', 0, 1, '2021-12-16 21:03:42', '2021-12-16 21:03:42', 1, 0, 0);
INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('2df4f2ce43c3116d818bff87561aa408', '媒资分类', 2, '媒资分类', '2f0c03ed96e59c2d1a236862934c2d16', '/media/category', 'el-icon-s-flag', 0, 1, '2021-12-16 20:40:23', '2021-12-16 20:40:23', 1, 0, 0);
INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('497aa1e33366f2fad55ef1bbce5ef32c', '媒资标签', 2, '媒资标签', '2f0c03ed96e59c2d1a236862934c2d16', '/media/tag', 'el-icon-platform-eleme', 0, 1, '2021-12-16 19:02:31', '2021-12-16 19:02:31', 1, 0, 0);
INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('a5b8df2fa78f665b0c8efb0cbb05bf5b', '编辑媒资信息', 2, '编辑媒资信息', '2f0c03ed96e59c2d1a236862934c2d16', '/media/info/detail/11111111111111111111111111111111', 'el-icon-phone-outline', 0, 1, '2021-12-15 21:28:12', '2021-12-18 15:14:43', 1, 0, 0);
INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('9c04a7dc87a998ce967d017533b7b288', '添加媒资信息', 2, '添加媒资信息', '2f0c03ed96e59c2d1a236862934c2d16', '/media/info/detail/add', 'el-icon-edit', 0, 1, '2021-12-15 20:03:21', '2021-12-15 22:01:39', 1, 0, 0);
INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('2ac1bf4465c67addebeee76e749c82b1', '信息管理', 2, '信息管理', '2f0c03ed96e59c2d1a236862934c2d16', '/media/info', 'el-icon-info', 0, 1, '2021-12-14 22:24:52', '2021-12-14 22:24:52', 1, 0, 0);
INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('2f0c03ed96e59c2d1a236862934c2d16', '媒资管理', 1, '媒资管理', NULL, '/media', 'el-icon-s-management', 0, 1, '2021-12-14 22:19:52', '2021-12-14 22:19:52', 1, 0, 0);



-- 创建媒资模块表结构

-- ----------------------------
-- Table structure for t_media_actor
-- ----------------------------
DROP TABLE IF EXISTS `t_media_actor`;
CREATE TABLE `t_media_actor`  (
                                  `uid` varchar(32)   NOT NULL COMMENT '主键',
                                  `name` varchar(50)   NOT NULL COMMENT '姓名',
                                  `avatar` varchar(255)   NULL DEFAULT NULL COMMENT '头像',
                                  `description` varchar(1000)   NULL DEFAULT NULL COMMENT '简述',
                                  `awards` varchar(255)   NULL DEFAULT NULL COMMENT '奖项',
                                  `label` varchar(100)   NULL DEFAULT NULL COMMENT '标签 ',
                                  `create_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '创建人',
                                  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '更新人',
                                  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
                                  `remark` varchar(255)   NULL DEFAULT NULL COMMENT '备注',
                                  `collect_count` int(0) NULL DEFAULT NULL COMMENT '收藏数',
                                  `click_count` int(0) NULL DEFAULT NULL COMMENT '点击数',
                                  `status` tinyint(1) NULL DEFAULT NULL,
                                  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '演员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_media_category
-- ----------------------------
DROP TABLE IF EXISTS `t_media_category`;
CREATE TABLE `t_media_category`  (
                                     `uid` varchar(32)   NOT NULL COMMENT '唯一uid',
                                     `name` varchar(255)   NULL DEFAULT NULL COMMENT '分类内容',
                                     `content` varchar(255)   NULL DEFAULT NULL COMMENT '分类简介',
                                     `status` tinyint unsigned NOT NULL COMMENT '状态',
                                     `sort` int(0) NULL DEFAULT 0 COMMENT '排序字段，越大越靠前',
                                     `create_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '创建人',
                                     `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '更新人',
                                     `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
                                     `remark` varchar(255)   NULL DEFAULT NULL COMMENT '备注',
                                     PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '媒资分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_media_info
-- ----------------------------
DROP TABLE IF EXISTS `t_media_info`;
CREATE TABLE `t_media_info`  (
                                 `uid` varchar(32)   NOT NULL COMMENT '主键',
                                 `images` varchar(255)   NULL DEFAULT NULL COMMENT '封面',
                                 `title` varchar(255)   NOT NULL COMMENT '标题',
                                 `type` varchar(20)   NULL DEFAULT NULL COMMENT '类型',
                                 `country` varchar(255)   NULL DEFAULT NULL COMMENT '国家',
                                 `tag_id` varchar(255)   NULL DEFAULT NULL COMMENT '标签',
                                 `description` varchar(1000)   NULL DEFAULT NULL COMMENT '描述',
                                 `publish_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '发布人uid',
                                 `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
                                 `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态（0发布 1停用）',
                                 `create_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '创建人',
                                 `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                 `update_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '更新人',
                                 `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                 `del_flag` char(1)   NULL DEFAULT NULL COMMENT '删除标志（0代表存在 1代表删除）',
                                 `remark` varchar(500)   NULL DEFAULT NULL COMMENT '备注',
                                 `click_count` int(0) NULL DEFAULT 0 COMMENT '阅读数',
                                 `comment_count` int(0) NULL DEFAULT NULL COMMENT '评论数',
                                 `follow_count` int(0) NULL DEFAULT NULL COMMENT '关注数',
                                 `collection_count` int(0) NULL DEFAULT NULL COMMENT '收藏数',
                                 `support_count` int(0) NULL DEFAULT NULL COMMENT '支持数',
                                 `oppose_count` int(0) NULL DEFAULT NULL COMMENT '反对数',
                                 `share_count` int(0) NULL DEFAULT NULL COMMENT '分享数',
                                 `open_comment` tinyint(1) NULL DEFAULT NULL COMMENT '是否可以评论',
                                 `open_download` tinyint(1) NULL DEFAULT NULL COMMENT '是否可以下载',
                                 `price` decimal(19, 2) NULL DEFAULT NULL COMMENT '价格',
                                 `rate` double(10, 1) NULL DEFAULT NULL COMMENT '评分',
  `category_uid` varchar(32)   NULL DEFAULT NULL COMMENT '分类uid',
  `category_name` varchar(255)   NULL DEFAULT NULL COMMENT '分类名称',
  `qrcode_path` varchar(255)   NULL DEFAULT NULL COMMENT '电影专属二维码',
  `open_password` tinyint(1) NULL DEFAULT NULL COMMENT '是否开启二维码 0 关闭 1开启',
  `password` varchar(50)   NULL DEFAULT NULL COMMENT '文章私密访问时的秘钥',
  `stills` varchar(1000)   NULL DEFAULT NULL COMMENT '剧照',
  `total_video_length` bigint(0) NULL DEFAULT NULL COMMENT '视频总长度',
  `summary` varchar(800)   NULL DEFAULT NULL COMMENT '简介',
  `en` varchar(100)   NULL DEFAULT NULL COMMENT '拼音',
  `letter` varchar(100)   NULL DEFAULT NULL COMMENT '首字母大写',
  `lang` varchar(30)   NULL DEFAULT NULL COMMENT '语言',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `wm_movie_pk_index`(`uid`) USING BTREE COMMENT '主键唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '媒资信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_media_info_actor
-- ----------------------------
DROP TABLE IF EXISTS `t_media_info_actor`;
CREATE TABLE `t_media_info_actor`  (
                                       `actor_uid` varchar(32)   NOT NULL COMMENT '演员uid',
                                       `media_info_uid` varchar(32)   NOT NULL,
                                       `type` varchar(100)   NULL DEFAULT NULL COMMENT '主演actor  演员',
                                       `remark` varchar(255)   NULL DEFAULT NULL COMMENT '备注',
                                       `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                       `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                       `uid` varchar(32)   NOT NULL COMMENT '主键',
                                       `status` tinyint(1) NULL DEFAULT NULL,
                                       PRIMARY KEY (`uid`) USING BTREE,
                                       INDEX `wm_movie_actor_index_actor_id`(`actor_uid`) USING BTREE,
                                       INDEX `wm_movie_actor_index_movie_id`(`media_info_uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '演员关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_media_play_record
-- ----------------------------
DROP TABLE IF EXISTS `t_media_play_record`;
CREATE TABLE `t_media_play_record`  (
                                        `uid` varchar(32)   NOT NULL COMMENT 'uid',
                                        `user_uid` varchar(64)   NULL DEFAULT NULL COMMENT '用户id',
                                        `video_uid` varchar(64)   NULL DEFAULT NULL COMMENT '视频id',
                                        `custom_uid` varchar(64)   NULL DEFAULT NULL COMMENT '自定义id',
                                        `play_duration` bigint(0) NULL DEFAULT NULL COMMENT '播放时长',
                                        `play_position` bigint(0) NULL DEFAULT NULL COMMENT '最后播放位置',
                                        `ip` varchar(255)   NULL DEFAULT NULL COMMENT 'ip地址',
                                        `province` varchar(255)   NULL DEFAULT NULL COMMENT '省份名称',
                                        `city` varchar(255)   NULL DEFAULT NULL COMMENT '城市名称',
                                        `referer` varchar(255)   NULL DEFAULT NULL COMMENT '来源域名',
                                        `device` varchar(255)   NULL DEFAULT NULL COMMENT '设备类型',
                                        `operating_system` varchar(255)   NULL DEFAULT NULL COMMENT '操作系统',
                                        `browser` varchar(255)   NULL DEFAULT NULL COMMENT '浏览器类型',
                                        `terminal` varchar(255)   NULL DEFAULT NULL COMMENT '终端类型',
                                        `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                        `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
                                        PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '播放记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_media_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_media_tag`;
CREATE TABLE `t_media_tag`  (
                                `uid` varchar(32)   NOT NULL COMMENT '主键',
                                `content` varchar(100)   NULL DEFAULT NULL COMMENT '标签内容',
                                `status` tinyint unsigned NULL COMMENT '状态',
                                `click_count` int(0) NULL DEFAULT 0 COMMENT '标签点击数',
                                `sort` int(0) NULL DEFAULT 0 COMMENT '排序字段，越大越靠前',
                                `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
                                `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
                                `create_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '创建人',
                                `update_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '更新人',
                                `remark` varchar(255)   NULL DEFAULT NULL COMMENT '备注',
                                `is_default` char(1)   NULL DEFAULT NULL COMMENT '是否默认（Y是 N否）',
                                `css_class` varchar(100)   NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
                                `list_class` varchar(100)   NULL DEFAULT NULL COMMENT '表格回显样式',
                                PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '媒资标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_media_video
-- ----------------------------
DROP TABLE IF EXISTS `t_media_video`;
CREATE TABLE `t_media_video`  (
                                  `uid` varchar(32)   NOT NULL COMMENT '主键',
                                  `title` varchar(100)   NULL DEFAULT NULL COMMENT '标题',
                                  `url` varchar(255)   NULL DEFAULT NULL COMMENT 'url地址',
                                  `ext` varchar(255)   NULL DEFAULT NULL COMMENT '文件后缀',
                                  `length` varchar(20)   NULL DEFAULT NULL COMMENT '播放长度',
                                  `type` varchar(255)   NULL DEFAULT NULL COMMENT '类型',
                                  `storage_type` char(1)   NULL DEFAULT NULL COMMENT '存储类型',
                                  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                  `create_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '创建人',
                                  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                  `update_by_uid` varchar(32)   NULL DEFAULT NULL COMMENT '更新人',
                                  `del_flag` char(1)   NULL DEFAULT NULL COMMENT '删除标志',
                                  `remark` varchar(500)   NULL DEFAULT NULL COMMENT '备注',
                                  `media_info_uid` varchar(32)   NULL DEFAULT NULL COMMENT '电影id',
                                  `filesize` bigint(0) NULL DEFAULT NULL COMMENT '文件大小字节数',
                                  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态1 待转换  2 正在转换 3转换失败 4 转化成功',
                                  `super_definition_url` varchar(255)   NULL DEFAULT NULL COMMENT '超清',
                                  `high_definition_url` varchar(255)   NULL DEFAULT NULL COMMENT '高清',
                                  `standard_definition_url` varchar(255)   NULL DEFAULT NULL COMMENT '标清',
                                  `error_msg` varchar(1000)   NULL DEFAULT NULL COMMENT '转换错误信息',
                                  PRIMARY KEY (`uid`) USING BTREE,
                                  UNIQUE INDEX `wm_movie_video_pk_index`(`uid`) USING BTREE COMMENT '主键唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '媒资视频表' ROW_FORMAT = Dynamic;


/*
	权限接口
*/
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('c9c161637d313c17915dbdc546b7d308','批量发放签到卡','3','批量发放签到卡','4157683aa3d88dbe7ed6501838155ca6','/userEquityRecord/sendSignInCard',NULL,'0','1','2021-12-20 09:05:23','2021-12-20 09:05:23','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('4157683aa3d88dbe7ed6501838155ca6','接口测试','2','接口测试','f4697cdf85920369179b90ff45a5982d','/user','el-icon-bell','0','1','2021-12-20 09:03:53','2021-12-20 09:03:53','0','0','0');


update   t_blog set audit_time = create_time WHERE audit_time IS NULL and audit_status = 2



/*
   用户动态表
   @date 2021年12月25日23:32:39
*/
CREATE TABLE `t_user_moment` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户uid',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `audit_name` varchar(255) DEFAULT NULL COMMENT '审核人名称',
  `reject_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '审核拒绝原因',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '审核状态，0：未审核，1：审核拒绝，2：审核通过',
  `content` text COMMENT '动态内容',
  `is_publish` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否发布：0：否，1：是',
  `file_uids` varchar(255) DEFAULT NULL COMMENT '文件uid列表',
  `topic_uids` varchar(255) DEFAULT NULL COMMENT '话题uid列表',
  `url` varchar(255) DEFAULT NULL COMMENT 'URL链接',
  `url_info` varchar(255) DEFAULT NULL COMMENT 'URL链接信息',
  `click_count` int(11) DEFAULT '0' COMMENT '点击数',
  `comment_count` int(11) DEFAULT '0' COMMENT '评论数',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `open_comment` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开启评论',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  PRIMARY KEY (`uid`),
  KEY `index_user_status` (`user_uid`,`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户动态表';


/*
   用户话题表
   @date 2021年12月25日23:32:39
*/
CREATE TABLE `t_user_moment_topic` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户uid',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `content` text COMMENT '内容',
  `is_publish` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否发布：0：否，1：是',
  `file_uid` varchar(255) DEFAULT NULL COMMENT '文件uid',
  `click_count` int(11) DEFAULT '0' COMMENT '点击数',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户话题表';

insert  into `t_user_moment_topic`(`uid`,`user_uid`,`admin_uid`,`content`,`is_publish`,`file_uid`,`click_count`,`remark`,`status`,`create_time`,`update_time`,`sort`) values ('2972c039a80a68a317eff1690fbb39f7',NULL,NULL,'下班打卡',1,NULL,0,NULL,1,'2021-12-26 10:22:54','2021-12-26 10:22:54',0),('7f19c6130a4692ba6d32d6a2b52dc251',NULL,NULL,'小目标',1,NULL,0,NULL,1,'2021-12-26 10:23:16','2021-12-26 10:23:16',0),('8b16142e28a9b17af44152c37e03745b',NULL,NULL,'打工人日常',1,NULL,0,NULL,1,'2021-12-26 10:23:10','2021-12-26 10:23:10',0),('91f62808de113aa8f8f35d43401414ba',NULL,NULL,'职场经验',1,NULL,0,NULL,1,'2021-12-26 10:23:04','2021-12-26 10:23:04',0),('9d34b3677294aad8503a6fdeeb1bf2f2',NULL,NULL,'段子笑话',1,NULL,0,NULL,1,'2021-12-26 10:23:22','2021-12-26 10:23:22',0),('d2558d4d84fc07ba64c5f18f8ec37088',NULL,NULL,'代码之美',1,'6237f22deb8e81c77e8c3a6581795e60',0,NULL,1,'2021-12-26 09:58:37','2021-12-26 10:22:26',0),('ea91f2a47f3720994b0ba10639cc7c5c',NULL,NULL,'养生之道',1,'8bf401d62306c8a4c313c70a95c22ebb',0,NULL,1,'2021-12-26 09:58:51','2021-12-26 10:22:41',0),('ed10a7820067f6bbde0957137abcf78b',NULL,NULL,'求职&Offer',1,NULL,0,NULL,1,'2021-12-26 10:22:48','2021-12-26 10:22:48',0);



insert into `t_web_navbar` (`uid`, `name`, `navbar_level`, `summary`, `parent_uid`, `url`, `icon`, `is_show`, `is_jump_external_url`, `sort`, `status`, `create_time`, `update_time`) values('18118fb6fc0e5678495559e135ef754e','蘑菇圈','1','蘑菇圈',NULL,'/moment','el-icon-ice-cream','1','0','6','1','2021-12-26 16:11:25','2021-12-26 16:11:40');

insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('f1cc0474c4b136150c2d931db4f04eec','批量删除','3','批量删除','9ed31a9ddcc3ded8ee140f7053639880','/userMomentTopic/deleteBatch',NULL,'0','1','2021-12-26 08:59:13','2021-12-26 08:59:13','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('19cdfe6cb4562cd89bcb890c55f45b4d','编辑话题','3','编辑话题','9ed31a9ddcc3ded8ee140f7053639880','/userMomentTopic/edit',NULL,'0','1','2021-12-26 08:58:55','2021-12-26 08:58:55','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('fce8f5d23f27962d93f929bb629ddf07','新增话题','3','新增话题','9ed31a9ddcc3ded8ee140f7053639880','/userMomentTopic/add',NULL,'0','1','2021-12-26 08:58:32','2021-12-26 08:58:32','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('a2a4e1a20b6ceed65848d8c691a629d3','查询列表','3','查询话题列表','9ed31a9ddcc3ded8ee140f7053639880','/userMomentTopic/getList',NULL,'0','1','2021-12-26 08:58:17','2021-12-26 08:58:17','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('9ed31a9ddcc3ded8ee140f7053639880','话题管理','2','话题管理','91cec763e93e67c4b187d708dcffa0d5','/moment/userMomentTopic','el-icon-sugar','0','1','2021-12-26 08:42:11','2021-12-26 08:46:25','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('4be965a04e47c25a276e9a9d70c45d95','动态管理','2','动态管理','91cec763e93e67c4b187d708dcffa0d5','/moment/userMoment','el-icon-ice-cream','1','1','2021-12-26 08:41:36','2021-12-26 08:42:20','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('91cec763e93e67c4b187d708dcffa0d5','动态管理','1','动态管理',NULL,'/moment','el-icon-ship','0','1','2021-12-26 08:41:03','2021-12-26 08:41:03','1','0','0');


/**
     业务类型字段，用于区分 module_uid 的类型
**/
ALTER TABLE t_web_visit ADD biz_type VARCHAR(3) NULL COMMENT '业务类型';


/**
  增加收藏表
**/
DROP TABLE IF EXISTS `t_collect`;
CREATE TABLE `t_collect` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) NOT NULL COMMENT '用户的uid',
  `collect_type` varchar(3) NOT NULL COMMENT '收藏类型：1博客，2：问答',
  `biz_uid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务的uid',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收藏表';

/**
  更新动态
**/
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('2932a9a766496a5c561110e1144438d6','批量删除用户动态','3','批量删除用户动态','4be965a04e47c25a276e9a9d70c45d95','/userMoment/deleteBatch',NULL,'0','1','2022-01-07 08:33:09','2022-01-07 08:33:09','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('a1f64a6b0ef25e559507af7a5308ee87','编辑用户动态','3','编辑用户动态','4be965a04e47c25a276e9a9d70c45d95','/userMoment/edit',NULL,'0','1','2022-01-07 08:32:47','2022-01-07 08:32:47','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('0e915d49518c44b4b1d54da6965b0b6a','获取用户动态','3','获取用户动态','4be965a04e47c25a276e9a9d70c45d95','/userMoment/getList',NULL,'0','1','2022-01-07 08:32:11','2022-01-07 08:32:11','1','1','0');

/*
* 更新动态表，支持emoji表情
**/
ALTER TABLE t_user_moment MODIFY COLUMN content text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '动态内容';



/**
  media_video 存储类型字段增加字典
**/

INSERT INTO `t_sys_dict_type`(`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('2bb2299f9324edbf38e5ac3cf4a8a265', 39, 'media_video_storage_type', 'media_video_storage_type', '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', 'media_video_storage_type', 1, '2022-01-17 19:04:01', '2022-01-17 19:04:01', '1', 0);
INSERT INTO `t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('5ba531e016f8f615e596d251cde42132', 114, '2bb2299f9324edbf38e5ac3cf4a8a265', 'B站存储', 'bilibiliStorage', NULL, NULL, 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2022-01-17 19:07:06', '2022-01-17 19:07:06', '1', 0);
INSERT INTO `t_sys_dict_data`(`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('4a8fd31ade4afa68f3fae53d6648bf9f', 113, '2bb2299f9324edbf38e5ac3cf4a8a265', '本地存储', 'localStorage', NULL, 'default', 0, '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', NULL, 1, '2022-01-17 19:06:08', '2022-01-17 19:06:08', '1', 0);

/**
  新增点赞记录表
 */
CREATE TABLE `t_user_praise_record`  (
    `uid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '点赞记录uid',
    `resource_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源类型',
    `resource_uid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源uid',
    `user_uid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户uid',
    `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `status` tinyint(0) UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态',
    `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
    `praise_type` int(0) NULL DEFAULT 1 COMMENT '点赞类型(目前默认为1 点赞 )\r\n1  点赞\r\n0  点踩',
    PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户点赞表' ROW_FORMAT = Dynamic;


ALTER TABLE t_media_info ADD sort int(9) UNSIGNED NOT NULL DEFAULT 1 COMMENT '排序字段';


/*
* 更新举报表, 增加举报标题、内容
**/
ALTER TABLE t_report MODIFY COLUMN report_content text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '举报标题、内容';


CREATE TABLE `t_problem` (
  `uid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一uid',
  `oid` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一oid',
  `title` varchar(200) DEFAULT NULL COMMENT '问题标题',
  `summary` varchar(200) DEFAULT NULL COMMENT '问题简介',
  `content` longtext COMMENT '问题内容',
  `answer` longtext COMMENT '问题标准答案',
  `problem_difficulty` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '问题难度(0：默认，1: 简单，2：中等，3：困难)',
  `problem_type` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '问题类型(0：默认, 1: 简答题，2：选择题，3：不定项，4: 填空，5：编程题)',
  `click_count` int(11) DEFAULT '0' COMMENT '问题点击数',
  `collect_count` int(11) DEFAULT '0' COMMENT '问题收藏数',
  `comment_count` int(11) DEFAULT '0' COMMENT '评论次数',
  `pass_count` int(11) DEFAULT '0' COMMENT '问题通过数',
  `no_pass_count` int(11) DEFAULT '0' COMMENT '问题未通过数',
  `visit_count` int(11) DEFAULT '0' COMMENT '面试中出现的次数',
  `file_uid` varchar(255) DEFAULT NULL COMMENT '标题图片uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户UID',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  `open_comment` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `audit_status` tinyint(3) DEFAULT '1' COMMENT '是否通过审核（0：否，1：是）',
  `audit_name` varchar(255) DEFAULT NULL COMMENT '审核人',
  `audit_time` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `is_selection` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否精选',
  `is_publish` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否上架',
  `publish_time` timestamp NULL DEFAULT NULL COMMENT '上架时间',
  `source` tinyint(3) NOT NULL DEFAULT '1' COMMENT '来源',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `problem_tag_uid` varchar(300) DEFAULT NULL COMMENT '问题标签uid列表',
  `has_answer` varchar(3) DEFAULT '1' COMMENT '是否有答案',
  PRIMARY KEY (`uid`,`oid`),
  KEY `oid` (`oid`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8 COMMENT='问题表';


CREATE TABLE `t_problem_tag` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `parent_uid` varchar(32) DEFAULT NULL COMMENT '父uid',
  `name` varchar(100) DEFAULT NULL COMMENT '标签名',
  `summary` varchar(1000) DEFAULT NULL COMMENT '标签简介',
  `tag_type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '标签类型（推荐、热门、语言、知识、岗位、公司、目标）',
  `is_selection` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否精选',
  `is_publish` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否上架',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `click_count` int(11) DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `tag_level` tinyint(3) NOT NULL DEFAULT '1' COMMENT '标签等级',
  `is_jump_external_url` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否跳转外链',
  `url` varchar(1024) DEFAULT NULL COMMENT '外链',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='问题标签表';

/*Data for the table `t_problem_tag` */

insert  into `t_problem_tag`(`uid`,`parent_uid`,`name`,`summary`,`tag_type`,`is_selection`,`is_publish`,`status`,`click_count`,`create_time`,`update_time`,`sort`,`tag_level`,`is_jump_external_url`,`url`,`icon`) values ('0f06446523567b97719abcb9e7e86cb8','25b5e02f3c55a2e21fe30d6b26b0f12a','Python',NULL,1,1,1,1,0,'2022-03-10 21:48:05','2022-03-10 21:48:19',0,2,0,NULL,'el-icon-document-copy'),('1be75f1aa7de54019c007116788acde6','25b5e02f3c55a2e21fe30d6b26b0f12a','C#',NULL,1,1,1,1,0,'2022-03-10 21:49:25','2022-03-10 21:49:25',0,2,0,NULL,'el-icon-suitcase'),('25b5e02f3c55a2e21fe30d6b26b0f12a','78e5872643cf7f20e8cf3bf0a6a546bd','编程语言','测试',1,1,1,1,0,'2022-03-09 22:26:23','2022-03-12 13:08:40',0,1,0,'','el-icon-hot-water'),('29322984a2856dc50fabf841ba8f1429','25b5e02f3c55a2e21fe30d6b26b0f12a','Spring','测试2',1,1,1,1,0,'2022-03-08 00:07:15','2022-03-10 21:47:29',0,2,0,NULL,'el-icon-monitor'),('2dd493646f4003ee5b33543e41103374','c1dffd546ccf1b9b4b74b1bbf1b9ad72','数据研发',NULL,3,1,1,1,0,'2022-03-12 13:01:35','2022-03-12 13:01:35',0,2,0,NULL,'el-icon-guide'),('339342fac35a330d8b245f4833a470bc','25b5e02f3c55a2e21fe30d6b26b0f12a','SpringCloud',NULL,1,1,1,1,0,'2022-03-12 12:57:14','2022-03-12 12:57:14',0,2,0,NULL,'el-icon-monitor'),('344d7548aedd359739ac349f66845cce',NULL,'公司',NULL,5,1,1,1,0,'2022-03-10 08:40:30','2022-03-12 13:08:49',0,1,0,'','el-icon-setting'),('5a1f71f3474cb91c0350b2cf2780b2b8','c1dffd546ccf1b9b4b74b1bbf1b9ad72','后端',NULL,3,1,1,1,0,'2022-03-11 22:57:00','2022-03-11 22:57:13',0,2,0,NULL,'el-icon-brush'),('62140acb480e891a2960d6d688701fe2',NULL,'学科知识','123123',5,1,1,1,0,'2022-03-09 22:25:45','2022-03-10 08:36:41',0,1,0,NULL,'el-icon-camera'),('78e5872643cf7f20e8cf3bf0a6a546bd',NULL,'测试','简介',2,1,0,0,0,'2022-03-07 23:42:57','2022-03-09 22:51:26',0,1,0,NULL,NULL),('8222c32ed152174ec7507baa505f5ae6','25b5e02f3c55a2e21fe30d6b26b0f12a','JavaScript',NULL,1,1,1,1,0,'2022-03-12 12:58:19','2022-03-12 12:58:19',0,2,0,NULL,'el-icon-brush'),('b071f7e171659b9d00933713ae9d6358','25b5e02f3c55a2e21fe30d6b26b0f12a','Java',NULL,1,1,1,1,0,'2022-03-10 21:46:26','2022-03-10 21:46:26',0,2,0,NULL,'el-icon-headset'),('b1f54f51afa684e291cb891d28e4b293','25b5e02f3c55a2e21fe30d6b26b0f12a','C++',NULL,1,1,1,1,0,'2022-03-10 21:49:01','2022-03-10 21:49:01',0,2,0,NULL,'el-icon-umbrella'),('be2dc47825ac385b3169fa723429b33b','c1dffd546ccf1b9b4b74b1bbf1b9ad72','客户端',NULL,3,1,1,1,0,'2022-03-12 13:00:11','2022-03-12 13:00:11',0,2,0,NULL,'el-icon-reading'),('bfcd9405bf0fbe56602a95e4a11a479c','25b5e02f3c55a2e21fe30d6b26b0f12a','C',NULL,1,1,1,1,0,'2022-03-10 21:49:45','2022-03-10 21:49:45',0,2,0,NULL,'el-icon-paperclip'),('c1dffd546ccf1b9b4b74b1bbf1b9ad72',NULL,'领域方向',NULL,3,1,1,1,0,'2022-03-10 21:51:02','2022-03-10 21:51:22',0,1,0,NULL,'el-icon-guide'),('ca1472462c08aab2ef6557e5c0b40480','c1dffd546ccf1b9b4b74b1bbf1b9ad72','算法',NULL,3,1,1,1,0,'2022-03-12 13:00:36','2022-03-12 13:00:36',0,2,0,NULL,'el-icon-shopping-cart-full'),('d47f466bfac3e9a2c72effbc45a88826','25b5e02f3c55a2e21fe30d6b26b0f12a','Html',NULL,1,1,1,1,0,'2022-03-12 12:58:47','2022-03-12 12:58:47',0,2,0,NULL,'el-icon-mouse'),('d797ddcad3d42d8e8f3eee738a175f47','25b5e02f3c55a2e21fe30d6b26b0f12a','SpringBoot',NULL,1,1,1,1,0,'2022-03-12 12:56:41','2022-03-12 12:56:41',0,2,0,NULL,'el-icon-umbrella'),('dec59884b4fa25a222242437779d5dec','25b5e02f3c55a2e21fe30d6b26b0f12a','Golang',NULL,1,1,1,1,0,'2022-03-10 21:48:40','2022-03-10 21:48:40',0,2,0,NULL,'el-icon-coordinate'),('df73fcbfca44d203fb3b861ae98f83c7','c1dffd546ccf1b9b4b74b1bbf1b9ad72','前端',NULL,3,1,1,1,0,'2022-03-12 12:59:28','2022-03-12 13:02:49',0,2,0,NULL,'el-icon-data-analysis'),('f8c9bcf5ab7d4b221bc95d68b92578bf','c1dffd546ccf1b9b4b74b1bbf1b9ad72','测试',NULL,3,1,1,1,0,'2022-03-12 13:01:01','2022-03-12 13:01:01',0,2,0,NULL,'el-icon-shopping-bag-2');


CREATE TABLE `t_problem_tag_relation` (
  `uid` varchar(32) NOT NULL,
  `problem_uid` varchar(32) DEFAULT NULL,
  `tag_uid` varchar(32) DEFAULT NULL,
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题-标签关系表';


CREATE TABLE `t_problem_user_record` (
  `uid` varchar(32) NOT NULL,
  `problem_uid` varchar(32) NOT NULL COMMENT '问题uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户uid',
  `degree` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '问题掌握程度：1：未掌握、2：见过、3：掌握',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题记录表';


insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('47168d1d098f33ad399e888357111131','查询全部','3','查询全部','738e45a12078989b94b2abee158d9e4f','/problemTag/getAllTagList',NULL,'0','1','2022-03-12 13:25:02','2022-03-12 13:25:02','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('b8bf0deb9a877d82234d47302577c402','删除','3','删除','aa7aaa75c5903b5d8cb9d050b6019425','/problem/deleteBatch',NULL,'0','1','2022-03-07 23:37:15','2022-03-07 23:37:15','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('07b8dbecd898acc58c6d107c1f4999c8','编辑','3','编辑','aa7aaa75c5903b5d8cb9d050b6019425','/problem/edit',NULL,'0','1','2022-03-07 23:36:49','2022-03-07 23:36:49','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('33a9e79895a992da72852042e79bf98f','新增','3','新增','aa7aaa75c5903b5d8cb9d050b6019425','/problem/add',NULL,'0','1','2022-03-07 23:36:26','2022-03-07 23:36:26','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('bd87f71877824f8c0b5b8e6526a292d5','查询列表','3','查询列表','aa7aaa75c5903b5d8cb9d050b6019425','/problem/getList',NULL,'0','1','2022-03-07 23:36:10','2022-03-07 23:36:10','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('86329a7d5a883b1992ba0dd5e917fdd4','删除','3','删除','738e45a12078989b94b2abee158d9e4f','/problemTag/deleteBatch',NULL,'0','1','2022-03-07 23:35:42','2022-03-07 23:40:55','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('5f60ef4c9e95bd2eee8c8e6a2ac527c1','编辑','3','编辑','738e45a12078989b94b2abee158d9e4f','/problemTag/edit',NULL,'0','1','2022-03-07 23:35:25','2022-03-07 23:41:00','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('4539b90601c5b62ead64f8389f96e111','新增','3','新增','738e45a12078989b94b2abee158d9e4f','/problemTag/add',NULL,'0','1','2022-03-07 23:35:14','2022-03-07 23:41:05','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('c3990b24f9d36c51a2ee87f1ee72753a','查询列表','3','新增','738e45a12078989b94b2abee158d9e4f','/problemTag/getList',NULL,'0','1','2022-03-07 23:34:49','2022-03-07 23:40:50','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('738e45a12078989b94b2abee158d9e4f','问题标签','2','问题标签','43b17421337d3f578ea99da475a0d05a','/problem/problemTag','el-icon-price-tag','0','1','2022-03-07 23:28:12','2022-03-07 23:37:52','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('aa7aaa75c5903b5d8cb9d050b6019425','问题管理','2','问题管理','43b17421337d3f578ea99da475a0d05a','/problem/problem','el-icon-data-board','0','1','2022-03-07 23:27:12','2022-03-07 23:42:15','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('43b17421337d3f578ea99da475a0d05a','问题管理','1','问题管理',NULL,'/problem','el-icon-data-line','5','1','2022-03-07 23:24:31','2022-03-07 23:38:48','1','0','0');



insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('15d9c45bb8cca6549cd0889760c77d06','44','是否精选','sys_selection','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','是否精选','1','2022-03-10 22:27:03','2022-03-10 22:27:03','1','0');
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('3d9496b946d3230de0e08e65f6ccab3d','43','标签等级','sys_tag_level','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','标签等级','1','2022-03-09 21:39:35','2022-03-09 21:39:40','1','0');
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('9be390a549d541c4b16e4ffe960afbab','42','问题类型','sys_problem_type','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','问题类型','1','2022-03-08 08:44:26','2022-03-08 08:44:26','1','0');
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('7e8f7eac894ac2f8bd816e4f9501b687','41','问题难度','sys_problem_difficulty','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','问题难度','1','2022-03-08 08:44:10','2022-03-08 08:44:10','1','0');
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('95d42152a689342aec8bfd646e8cd5ae','40','问题标签类型','sys_problem_tag_type','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','问题标签类型','1','2022-03-07 23:44:30','2022-03-07 23:47:15','1','0');


insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('4655145fd05903936cfa48d0109dceec','133','15d9c45bb8cca6549cd0889760c77d06','否','0',NULL,'info','1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-10 22:27:39','2022-03-10 22:27:39','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('ea78da5eb9deb8fc6763a2e0a7b4e696','132','15d9c45bb8cca6549cd0889760c77d06','是','1',NULL,'warning','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-10 22:27:23','2022-03-10 22:27:48','1','1');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('1579f6530c7f9adfca06c089528270fd','131','3d9496b946d3230de0e08e65f6ccab3d','二级标签','2',NULL,'warning','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-09 21:40:09','2022-03-09 21:40:09','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('1218066532399d15960cba90eecfc05f','130','3d9496b946d3230de0e08e65f6ccab3d','一级标签','1',NULL,'success','1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','一级标签','1','2022-03-09 21:39:57','2022-03-09 21:40:12','1','1');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('e97037896a9fb28fa834b95357144595','129','9be390a549d541c4b16e4ffe960afbab','编程题','5',NULL,'danger','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-08 09:02:25','2022-03-09 07:13:00','1','5');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('a7eef2ce843c2660d72d8a91eab24abb','128','9be390a549d541c4b16e4ffe960afbab','填空题','4',NULL,'warning','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-08 09:01:36','2022-03-09 07:13:15','1','6');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('83e0f0a7d32eb19d69ad0bb4a7dec7db','127','9be390a549d541c4b16e4ffe960afbab','不定项','3',NULL,'primary','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-08 09:01:25','2022-03-09 07:12:48','1','3');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('3cf836839f8bf4f001dcb88afc519dee','126','9be390a549d541c4b16e4ffe960afbab','选择题','2',NULL,'info','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-08 09:01:18','2022-03-09 07:13:09','1','4');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('eb04997919dfbc6f57649237b5440266','125','9be390a549d541c4b16e4ffe960afbab','简答题','1',NULL,'success','1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-08 09:01:12','2022-03-09 07:12:40','1','7');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('545c34626907b1473edc8a1ee6616127','124','9be390a549d541c4b16e4ffe960afbab','默认','0',NULL,'default','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-08 09:01:04','2022-03-10 21:53:35','0','2');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('6df00798282740ba0b7a02208c9588e4','123','7e8f7eac894ac2f8bd816e4f9501b687','困难','3',NULL,'danger','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-08 09:00:41','2022-03-09 07:12:17','1','1');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('47b475605ad8e2b405a905a5f8e32397','122','7e8f7eac894ac2f8bd816e4f9501b687','中等','2',NULL,'warning','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-08 09:00:34','2022-03-09 07:12:10','1','2');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('a2a90c911d39ab210deac1ff8eaac858','121','7e8f7eac894ac2f8bd816e4f9501b687','简单','1',NULL,'success','1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-08 08:48:26','2022-03-09 07:12:01','1','3');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('09942f0481608a5df21cf1baa6698bb4','120','7e8f7eac894ac2f8bd816e4f9501b687','默认','0',NULL,'primary','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-08 08:48:17','2022-03-10 21:53:25','0','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('95ca03db5b9d99170f05e103ea12774e','119','95d42152a689342aec8bfd646e8cd5ae','目标','5',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-07 23:45:54','2022-03-07 23:45:54','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('03ab210508462a072f7e32dd1b3c5503','118','95d42152a689342aec8bfd646e8cd5ae','公司','4',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-07 23:45:47','2022-03-07 23:45:47','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('68f691e6a80031a335d9881af93ee8b7','117','95d42152a689342aec8bfd646e8cd5ae','岗位','3',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-07 23:45:37','2022-03-07 23:45:37','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('a4cea7bec13362853ada65128eaba258','116','95d42152a689342aec8bfd646e8cd5ae','知识','2',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-07 23:45:24','2022-03-07 23:45:24','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('340ed68a96b935992ac78a21093beed7','115','95d42152a689342aec8bfd646e8cd5ae','语言','1',NULL,NULL,'1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-07 23:45:14','2022-03-07 23:55:11','1','0');


/**
* 新增支持的聊天相关
* 2022年3月20日17:55:50
**/
CREATE TABLE `t_im_message` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `room_id` varchar(32) DEFAULT NULL COMMENT '聊天室会话id',
  `from_user_id` varchar(32) DEFAULT NULL COMMENT '消息发送人',
  `from_user_nick_name` varchar(255) DEFAULT NULL COMMENT '发送人昵称',
  `form_user_avatar_uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '发送人头像',
  `message_level` int(5) DEFAULT NULL COMMENT '消息级别(聊天消息/公告/通知/警告)',
  `message` text COMMENT '消息内容',
  `send_time` datetime DEFAULT NULL COMMENT '消息发送时间',
  `category` int(5) DEFAULT NULL COMMENT '消息类别(文本消息、语音消息、图片消息)',
  `duration` int(10) DEFAULT NULL COMMENT '语音长度(秒)',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '已读',
  `to_user_id` varchar(32) DEFAULT NULL COMMENT '消息接收人',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `message_type` int(5) DEFAULT NULL COMMENT '消息类型 私聊/群聊/回执',
  PRIMARY KEY (`uid`),
  KEY `from_user_id` (`from_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天记录表';



CREATE TABLE `t_room` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `room_type` int(5) DEFAULT NULL COMMENT '类型 群组/个人',
  `belong_user_id` varchar(32) DEFAULT NULL COMMENT '会话所属用户',
  `receive_id` varchar(32) DEFAULT NULL COMMENT '消息接收人',
  `avatar` varchar(255) DEFAULT NULL COMMENT '会话头像',
  `title` varchar(255) DEFAULT NULL COMMENT '会话标题',
  `session_id` varchar(32) DEFAULT NULL COMMENT '会话消息记录id',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert  into `t_room`(`uid`,`room_type`,`belong_user_id`,`receive_id`,`avatar`,`title`,`session_id`,`status`,`create_time`,`update_time`) values ('10001',10002,'10000','10001','http://picture.moguit.cn//blog/admin/jpg/2020/12/21/1608539694443.jpg','蘑菇社区交流群','10001',1,NULL,NULL);

UPDATE t_web_config SET chat_type_list='[\"4\",\"1\",\"3\",\"2\"]' WHERE uid = 'a331e4933cf54afcbb8c0cb11ec0830e';

/**
* 新增支持的聊天类型列表（用于控制 表情、语音、图片、通话、视频 是否显示在前端）
* 2022年3月20日17:55:50
**/
ALTER TABLE  t_web_config ADD chat_type_list VARCHAR(1024) COMMENT "支持的聊天类型列表（用于控制 表情、语音、图片、通话、视频 是否显示在前端）";
ALTER TABLE  t_web_config ADD open_chat VARCHAR(1) default '1' COMMENT "是否开启聊天";


insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('4e3a1aabea86cb91a2e2b83a124698da','批量删除','3','批量删除','af915a5ba123f7ca2333292ea79d4dcc','/room/deleteBatch',NULL,'0','1','2022-03-19 15:34:20','2022-03-19 15:34:20','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('c1aa39fc577a9d22a43adeba0d382ded','删除','3','删除','af915a5ba123f7ca2333292ea79d4dcc','/room/delete',NULL,'0','1','2022-03-19 15:34:03','2022-03-19 15:34:03','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('aa3513aecc0db1b56d5ef16367275806','获取列表','3','获取列表','af915a5ba123f7ca2333292ea79d4dcc','/room/getList',NULL,'0','1','2022-03-19 15:33:43','2022-03-19 15:33:43','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('355f18e56762aef630dff4fcec406e0f','批量删除','3','批量删除','651c01ea81d077899823e394a6c38259','/imMessage/deleteBatch',NULL,'0','1','2022-03-19 15:33:18','2022-03-19 15:33:18','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('afaa75a7eef83d978ceefdc8c0556248','删除','3','删除','651c01ea81d077899823e394a6c38259','/imMessage/deleteBatch',NULL,'0','1','2022-03-19 15:33:06','2022-03-19 15:33:06','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('29271615c6001f62a3131187a68211c4','获取列表','3','获取列表','651c01ea81d077899823e394a6c38259','/imMessage/getList',NULL,'0','1','2022-03-19 15:32:48','2022-03-19 15:32:48','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('af915a5ba123f7ca2333292ea79d4dcc','房间管理','2','房间管理','bcf4a9bc21c14b559bcb015fb7912266','/message/room','el-icon-bangzhu','0','1','2022-03-19 15:31:39','2022-03-19 15:31:39','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('651c01ea81d077899823e394a6c38259','聊天管理','2','聊天管理','bcf4a9bc21c14b559bcb015fb7912266','/message/chat','el-icon-chat-round','0','1','2022-03-19 15:31:00','2022-03-19 15:31:00','1','0','0');


insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('85096ec6932a94918ea8bda576fc57a9','47','聊天类别','sys_message_category','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','聊天类别','1','2022-03-19 16:24:42','2022-03-19 16:24:42','1','0');
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('607d5932ce14260474faad549eb996d5','46','聊天消息类型','sys_message_type','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','聊天消息类型','1','2022-03-19 16:21:11','2022-03-19 16:21:23','1','0');
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('7b866788143c93541e71f4a29da1579b','45','聊天消息级别','sys_message_level','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','聊天消息级别','1','2022-03-19 15:47:14','2022-03-19 15:47:30','1','0');

insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('57a38c1b5f291e22326b8b903f680738','144','7b866788143c93541e71f4a29da1579b','错误','3005',NULL,'danger','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-20 09:54:01','2022-03-20 09:54:01','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('87a63de746fc760552aec03e7cc8b233','143','85096ec6932a94918ea8bda576fc57a9','图片消息','20003',NULL,'warning','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-19 16:25:45','2022-03-19 16:25:45','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('6f6cff7c1caf1f7819bed79fca23fdd5','142','85096ec6932a94918ea8bda576fc57a9','语音消息','20002',NULL,'info','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-19 16:25:18','2022-03-19 16:25:18','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('333726c5093e641216101e971cc5f375','141','85096ec6932a94918ea8bda576fc57a9','文本消息','20001',NULL,'primary','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-19 16:25:01','2022-03-19 16:25:31','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('2a2e8a2961dbd88de58968432f4291b9','140','607d5932ce14260474faad549eb996d5','已读','10003',NULL,'info','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-19 16:24:09','2022-03-20 09:55:12','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('a57fef0c9552598d1cf31b1c4b643456','139','607d5932ce14260474faad549eb996d5','群聊','10002',NULL,'success','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-19 16:23:35','2022-03-19 21:29:13','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('4ef781feb96eaf984da18684e7aafacf','138','607d5932ce14260474faad549eb996d5','私聊','10001',NULL,'danger','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-19 16:23:17','2022-03-19 21:29:16','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('1f1f2b66ab6312c413fcf6b4036fc0d8','137','7b866788143c93541e71f4a29da1579b','警告','30004',NULL,'warning','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-19 15:48:48','2022-03-20 09:54:50','1','1');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('e783bfb4a7d1fc2454c0d7f84f469e92','136','7b866788143c93541e71f4a29da1579b','通知','30003',NULL,'info','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-19 15:48:27','2022-03-20 09:54:43','1','2');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('9543ceb0644a15ccce5ea9c5c09bc78b','135','7b866788143c93541e71f4a29da1579b','公告','30002',NULL,'info','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-19 15:48:12','2022-03-20 09:54:38','1','3');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('4dc6a25266278661a593cbfdfe6a3951','134','7b866788143c93541e71f4a29da1579b','聊天消息','30001',NULL,'success','1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-03-19 15:47:51','2022-03-20 09:54:32','1','4');



insert into `t_sys_params` (`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) values('5e148d83036eea64aacb1d1a67507129','1','新用户活跃限制时间','NEW_USER_LIMIT_TIME','新用户活跃（包括：评论、发表文章，发动态）限制时间（单位：小时）','3','1','2022-03-21 22:31:21','2022-03-21 22:49:47','0');


/**
* 2022年3月31日09:34:51
* 增加操作类型
**/
ALTER TABLE  t_im_message ADD operator_type int(10) COMMENT "操作类型(已接听/拒接/已取消/未接听)";


/*
 在t_problem表，增加审批拒绝字段
 @date 2022年4月2日15:58:56
*/
ALTER TABLE  t_problem ADD reject_reason VARCHAR(255)  COMMENT "审批拒绝原因";

/**
    新增面经单日发表数限制
    @date 2022年4月2日15:58:56
 */
INSERT INTO `t_sys_params`(`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) VALUES ('e24e6f5796a54a7eafa3507b4dc65691', '1', '面经发布限制数', 'USER_PUBLISH_PROBLEM_COUNT', '用户单日面经发布限制', '100', 1, '2022-04-02 17:32:21', '2022-04-02 17:32:21', 0);


/**
    公众号登录二维码
 */
ALTER TABLE t_web_config add wechat_file_uid varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '微信公众号二维码FileUid';

/**
    增加公众号相关菜单
    @date 2022年4月8日22:58:54
 */

insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('2c393fcf534d6b395e713894f0d6b4e8','百度SEO推送','3','百度SEO推送','1f01cd1d2f474743b241d74008b12333','/blog/pushBatch',NULL,'0','1','2022-03-09 13:46:02','2022-03-09 13:46:02','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('d74aa29eab0db15eb19106306fe67b8e','审核','3','问题审核','aa7aaa75c5903b5d8cb9d050b6019425','/problem/audit',NULL,'0','1','2022-04-08 08:13:37','2022-04-08 08:13:37','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('3cec32eba4af74163185627b0232372e','消息管理','2','消息管理','8043b6ada11b4e1416906ce0cb44d613','/wechat/WxMsg','el-icon-knife-fork','0','1','2022-04-05 18:44:53','2022-04-05 18:44:53','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('3e6ff13b28650b844285ac15e54f3555','粉丝管理','2','粉丝管理','8043b6ada11b4e1416906ce0cb44d613','/wechat/WxUser','el-icon-dessert','0','1','2022-04-05 18:44:29','2022-04-05 18:44:29','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('ca4fc5889e9b92021bca8bdfe9000ed9','带参二维码','2','带参二维码','8043b6ada11b4e1416906ce0cb44d613','/wechat/WxQrcode','el-icon-fork-spoon','0','1','2022-04-05 18:44:06','2022-04-05 18:44:06','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('9ea7cd9929681053bf097efec09c79ab','素材管理','2','素材管理','8043b6ada11b4e1416906ce0cb44d613','/wechat/WxAssets','el-icon-service','0','1','2022-04-05 18:43:25','2022-04-05 18:43:25','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('cb2775862534ba2ee927a20a88f68885','模板消息','2','模板消息','8043b6ada11b4e1416906ce0cb44d613','/wechat/WxMsgTemplate','el-icon-heavy-rain','0','1','2022-04-05 18:42:02','2022-04-05 18:42:02','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('44790780099c5f9d1ed37275f917e81a','回复规则','2','回复规则','8043b6ada11b4e1416906ce0cb44d613','/wechat/WxMsgReplyRule','el-icon-chat-line-round','0','1','2022-04-05 18:41:40','2022-04-05 18:41:40','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('7621dbd590ba264784b247c3c0b6b633','菜单管理','2','菜单管理','8043b6ada11b4e1416906ce0cb44d613','/wechat/WxMenu','el-icon-document-copy','0','1','2022-04-05 18:40:34','2022-04-05 18:40:34','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('1d62bb5e2750cbdb4de04037f6c47a03','账号管理','2','公众号账号管理','8043b6ada11b4e1416906ce0cb44d613','/wechat/WxAccount','el-icon-headset','0','1','2022-04-05 11:35:42','2022-04-05 11:35:42','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('8043b6ada11b4e1416906ce0cb44d613','公众号管理','1','公众号管理',NULL,'/wechat','el-icon-pie-chart','0','1','2022-04-05 11:34:56','2022-04-05 11:34:56','1','0','0');


/**
* 新增账号绑定表
* date： 2022年4月17日12:59:22
*
**/
CREATE TABLE `t_user_account` (
  `uid` varchar(32) NOT NULL COMMENT '随机Uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户Uid',
  `account_id` varchar(32) DEFAULT NULL COMMENT '第三方唯一标识',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '来源',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `bind_time` timestamp NULL DEFAULT NULL COMMENT '第三方账号绑定时间',
  `unbind_time` timestamp NULL DEFAULT NULL COMMENT '第三方账号解绑时间',
  `avatar` varchar(100) DEFAULT NULL COMMENT '个人头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `gender` tinyint unsigned DEFAULT NULL COMMENT '性别(1:男2:女)',
  `nick_name` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方用户昵称',
  `summary` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '简介',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB COMMENT='账号绑定表';


/*
    新增菜单：通用修改 以及相关按钮
*/
INSERT INTO `mogu_blog_business`.`t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('c1a165420fa022ef13bdadada87706d6', '对比', 3, '对比', '3197007522cdeef636d698247ee92d8d', '/generalEdit/compare', NULL, 0, 1, '2022-04-19 18:32:45', '2022-04-19 18:32:45', 1, 1, 0);
INSERT INTO `mogu_blog_business`.`t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('4dcc4ade08ecebd466214a435c03edfc', '查询列表', 3, '查询列表', '3197007522cdeef636d698247ee92d8d', '/generalEdit/getGeneralEditList', NULL, 0, 1, '2022-04-18 21:29:03', '2022-04-18 21:29:03', 1, 1, 0);
INSERT INTO `mogu_blog_business`.`t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('f49cc032c66c2b94404520869a28c2d6', '审核', 3, '审核', '3197007522cdeef636d698247ee92d8d', '/generalEdit/audit', NULL, 0, 1, '2022-04-18 21:28:25', '2022-04-18 21:28:25', 1, 1, 0);
INSERT INTO `mogu_blog_business`.`t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('8c7e4bfa80c601d5a5253a046504b70c', '删除', 3, '删除', '3197007522cdeef636d698247ee92d8d', '/generalEdit/deleteBatch', NULL, 0, 1, '2022-04-18 21:27:51', '2022-04-19 18:32:22', 1, 1, 0);
INSERT INTO `mogu_blog_business`.`t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('3197007522cdeef636d698247ee92d8d', '勘误管理', 2, '勘误管理', 'badf0010422b432ba6ec9c83a25012ed', '/system/generalEdit', 'el-icon-edit', 0, 1, '2022-04-18 21:26:48', '2022-04-18 21:35:32', 1, 0, 0);

/**
    新增字典数据
 */
INSERT INTO `mogu_blog_business`.`t_sys_dict_type`(`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('14bdfc5ff077304063e0e14d600b9ac9', 49, '修改类型', 'sys_biz_type', '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', '修改类型', 1, '2022-04-14 14:34:54', '2022-04-14 14:34:54', '1', 0);
INSERT INTO `mogu_blog_business`.`t_sys_dict_type`(`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) VALUES ('dcf31bf524a698d2e4149ed7e1a7e5df', 48, '是否公开', 'sys_isopen_status', '1f01cd1d2f474743b241d74008b12333', '1f01cd1d2f474743b241d74008b12333', '试卷是否公开', 1, '2022-04-13 09:38:06', '2022-04-13 09:38:06', '1', 0);

insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('6a141c20a9e2d17ce2c6ec73f8759816','146','dcf31bf524a698d2e4149ed7e1a7e5df','私有','2',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','私有','1','2022-04-17 17:14:05','2022-04-17 17:21:40','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('36bd2e8818678efe7ff3b900bb88cb7a','145','dcf31bf524a698d2e4149ed7e1a7e5df','公开','1',NULL,NULL,'1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','公开','1','2022-04-17 17:13:54','2022-04-17 17:33:56','1','1');

CREATE TABLE `t_general_edit` (
  `uid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一uid',
  `oid` int NOT NULL AUTO_INCREMENT COMMENT '唯一oid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户UID',
  `biz_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '博客/面经类型(0：面经,1: 博客)',
  `biz_uid` varchar(32) DEFAULT NULL COMMENT '博客/面经id',
  `reason` varchar(200) DEFAULT NULL COMMENT '修改原因',
  `summary` longtext COMMENT '题目',
  `old_content` longtext COMMENT '修改前内容',
  `content` longtext COMMENT '修改内容',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `audit_status` tinyint DEFAULT '1' COMMENT '是否通过审核（0：否，1：是）',
  `audit_name` varchar(255) DEFAULT NULL COMMENT '审核人',
  `audit_time` timestamp NULL DEFAULT NULL COMMENT '审核时间',
  `reject_reason` varchar(255) DEFAULT NULL COMMENT '审批拒绝原因',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`,`oid`),
  KEY `oid` (`oid`)
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb3 COMMENT='通用内容修改表';


CREATE TABLE `t_exam` (
  `uid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '唯一uid',
  `oid` int NOT NULL AUTO_INCREMENT COMMENT '唯一oid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户UID',
  `title` varchar(200) DEFAULT NULL COMMENT '名称',
  `description` longtext COMMENT '试卷描述',
  `click_count` int DEFAULT '0' COMMENT '试卷点击数',
  `collect_count` int DEFAULT '0' COMMENT '试卷收藏数',
  `exam_tag_uid` varchar(300) DEFAULT NULL COMMENT '问题标签uid列表',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序字段',
  `is_open` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否公开(0：是，1：否)',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`,`oid`),
  KEY `oid` (`oid`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb3 COMMENT='试卷表';


CREATE TABLE `t_problem_exam_relation` (
  `uid` varchar(32) NOT NULL,
  `problem_uid` varchar(32) DEFAULT NULL,
  `exam_uid` varchar(32) DEFAULT NULL,
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='试卷-面经关系表';



CREATE TABLE `t_exam_tag_relation` (
  `uid` varchar(32) NOT NULL,
  `exam_uid` varchar(32) DEFAULT NULL,
	`tag_uid` varchar(32) DEFAULT NULL,
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='试卷-标签关系表';


/**
* 菜单表新增数据
* date： 2022年4月17日12:59:22
**/
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('37c06226e1e1e3e6d78de8c6bbd93303','删除选中媒资信息','3','删除选中媒资信息','2ac1bf4465c67addebeee76e749c82b1','/media/info/deleteBatch',NULL,'0','1','2022-04-17 12:53:15','2022-04-17 12:53:15','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('7b44346592af336d5571472bc8605675','编辑媒资信息','3','编辑媒资信息','2ac1bf4465c67addebeee76e749c82b1','/media/info/edit',NULL,'0','1','2022-04-17 12:53:04','2022-04-17 12:53:04','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('46e59f37c44e3a1033f6a809ec76d3ce','增加媒资信息','3','增加媒资信息','2ac1bf4465c67addebeee76e749c82b1','/media/info/add',NULL,'0','1','2022-04-17 12:51:54','2022-04-17 12:51:54','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('ae1e6c2dafe701ef7fb66dbb7e4d14ee','新增、修改媒资信息','3','新增、修改媒资信息','2ac1bf4465c67addebeee76e749c82b1','/media/info',NULL,'0','1','2022-04-17 12:51:04','2022-04-17 12:51:04','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('3b5697a69ce77c100fd96f9a5f4ed52e','获取媒资信息列表','3','获取媒资信息列表','2ac1bf4465c67addebeee76e749c82b1','/media/info/list',NULL,'0','1','2022-04-17 12:50:37','2022-04-17 12:51:28','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('085210060c39a62dd90021433b9c7cae','删除媒资标签详细信息','3','删除媒资标签详细信息','497aa1e33366f2fad55ef1bbce5ef32c','/media/tag/deleteBatch',NULL,'0','1','2022-04-17 12:47:47','2022-04-17 12:47:47','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('722dcede99951b3f4306ce663ec0db61','新增、编辑媒资标签详细信息','3','新增、编辑媒资标签详细信息','497aa1e33366f2fad55ef1bbce5ef32c','/media/tag',NULL,'0','1','2022-04-17 12:47:30','2022-04-17 12:47:30','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('4102bddf9a825ff219306e3def204e22','获取媒资标签列表','3','获取媒资标签列表','497aa1e33366f2fad55ef1bbce5ef32c','/media/tag/list',NULL,'0','1','2022-04-17 12:47:08','2022-04-17 12:47:08','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('3820db8fd34de498ea2f9cb754dd47b6','删除媒资分类详细信息','3','删除媒资分类详细信息','2df4f2ce43c3116d818bff87561aa408','/media/category/deleteBatch',NULL,'0','1','2022-04-17 12:45:59','2022-04-17 12:45:59','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('412349a431f77f472eae28092a7b6133','新增/编辑媒资分类','3','新增/编辑媒资分类','2df4f2ce43c3116d818bff87561aa408','/media/category',NULL,'0','1','2022-04-17 12:45:42','2022-04-17 12:45:42','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('06b08a1f69e10fb36e49def6d800bec2','获取媒资分类列表','3','获取媒资分类列表','2df4f2ce43c3116d818bff87561aa408','/media/category/list',NULL,'0','1','2022-04-17 12:45:12','2022-04-17 12:45:12','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('a31d4d28a6704c28582d4fcea201c771','删除媒资演员详细信息','3','删除媒资演员详细信息','2db5aad78770ae5b59cf9ce81d0b4256','/media/actor/deleteBatch',NULL,'0','1','2022-04-17 12:44:45','2022-04-17 12:49:34','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('2821d02f1bc07549d8eeacf11cdee7da','新增/修改媒资演员','3','新增/修改媒资演员','2db5aad78770ae5b59cf9ce81d0b4256','/media/actor',NULL,'0','1','2022-04-17 12:44:25','2022-04-17 12:49:42','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('0ac5dd1f0956a7edb29426f2214e1b42','查询没有被选择演员列表','3','查询没有被选择演员列表','2db5aad78770ae5b59cf9ce81d0b4256','/media/actor/notSelectedList',NULL,'0','1','2022-04-17 12:43:37','2022-04-17 12:49:48','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('c04937927c471679a85dc3c4073b29ad','获取媒资演员列表','3','获取媒资演员列表','2db5aad78770ae5b59cf9ce81d0b4256','/media/actor/list',NULL,'0','1','2022-04-17 12:43:20','2022-04-17 12:49:23','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('f1132436b37cc5ae0d7234ecc26c3ab4','处理举报信息','3','处理举报信息','427b8bc714e54985b0463706fd62647c','/report/handle',NULL,'0','1','2022-04-17 12:33:06','2022-04-17 12:33:06','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('6b4796b6f1cf4eea38e1567a00aae812','获取举报信息','3','获取举报信息','427b8bc714e54985b0463706fd62647c','/report/list',NULL,'0','1','2022-04-17 12:32:54','2022-04-17 12:32:54','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('cd4b958b4da862d21bcf869026f6e65d','获取举报列表','3','获取举报列表','427b8bc714e54985b0463706fd62647c','/report/getList',NULL,'0','1','2022-04-17 12:32:36','2022-04-17 12:32:36','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('bad1c855cbb34661dbfe2c00116ff316','获取批量素材','3','获取批量素材','9ea7cd9929681053bf097efec09c79ab','/manage/wxAssets/materialFileBatchGet',NULL,'0','1','2022-04-17 11:59:33','2022-04-17 11:59:33','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('b0f43643f6cfbea18446936585123428','删除消息','3','删除消息','3cec32eba4af74163185627b0232372e','/manage/wxMsg/delete',NULL,'0','1','2022-04-17 11:55:58','2022-04-17 11:55:58','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('0c81ff3da8114a7aec0cb65a31bdc475','回复消息','3','回复消息','3cec32eba4af74163185627b0232372e','/manage/wxMsg/reply',NULL,'0','1','2022-04-17 11:55:44','2022-04-17 11:55:44','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('09bbcd3c523fcfe20e574d0313230feb','消息详情','3','消息详情','3cec32eba4af74163185627b0232372e','/manage/wxMsg/info',NULL,'0','1','2022-04-17 11:55:13','2022-04-17 11:55:13','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('3793b111dfc7a9d8002faa17d7dc712a','查询列表','3','查询列表','3cec32eba4af74163185627b0232372e','/manage/wxMsg/list',NULL,'0','1','2022-04-17 11:54:59','2022-04-17 11:54:59','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('b289856c3d27cbc48596daebe39df6e6','批量移除标签','3','批量移除标签','3e6ff13b28650b844285ac15e54f3555','/manage/wxUserTags/batchUnTagging',NULL,'0','1','2022-04-17 11:53:11','2022-04-17 11:53:11','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('5e72458091c030f20630d03fce5468c0','批量给用户打标签','3','批量给用户打标签','3e6ff13b28650b844285ac15e54f3555','/manage/wxUserTags/batchTagging',NULL,'0','1','2022-04-17 11:52:57','2022-04-17 11:52:57','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('0ebb4d088478908f1c6cffd3da6f167b','删除用户标签','3','删除用户标签','3e6ff13b28650b844285ac15e54f3555','/manage/wxUserTags/delete',NULL,'0','1','2022-04-17 11:52:44','2022-04-17 11:52:44','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('b5595b4e8f95417b033d0004b6458310','修改或保存用户标签','3','修改或保存用户标签','3e6ff13b28650b844285ac15e54f3555','/manage/wxUserTags/save',NULL,'0','1','2022-04-17 11:52:11','2022-04-17 11:52:11','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('ade18fda0d3e62f19396892e52fd1ceb','公众号用户标签列表','3','公众号用户标签列表','3e6ff13b28650b844285ac15e54f3555','/manage/wxUserTags/list',NULL,'0','1','2022-04-17 11:51:51','2022-04-17 11:51:51','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('8f046a737c9f83e916461fff8d6b0d1e','删除','3','删除','3e6ff13b28650b844285ac15e54f3555','/manage/wxUser/delete',NULL,'0','1','2022-04-17 11:49:15','2022-04-17 11:49:15','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('db133734d4d698d5c6a2fb5feccd9307','同步用户列表到数据库','3','同步用户列表到数据库','3e6ff13b28650b844285ac15e54f3555','/manage/wxUser/info',NULL,'0','1','2022-04-17 11:49:00','2022-04-17 11:49:00','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('dbb6eda95fe689b64e8398248dcd403b','详情','3','详情','3e6ff13b28650b844285ac15e54f3555','/manage/wxUser/info',NULL,'0','1','2022-04-17 11:48:51','2022-04-17 11:48:51','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('fb1bbdecb08d32a6e7e7d591b84bc92d','列表通过id查询','3','列表通过id查询','3e6ff13b28650b844285ac15e54f3555','/manage/wxUser/listByIds',NULL,'0','1','2022-04-17 11:48:38','2022-04-17 11:48:38','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('1478e58fb7fe8ebe57d1a04bf2a21266','列表','3','列表','3e6ff13b28650b844285ac15e54f3555','/manage/wxUser/list',NULL,'0','1','2022-04-17 11:48:04','2022-04-17 11:48:04','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('bd25afc6a0dcb3131a768c9146b7f0de','删除','3','删除','ca4fc5889e9b92021bca8bdfe9000ed9','/manage/wxQrCode/delete',NULL,'0','1','2022-04-17 11:47:30','2022-04-17 11:47:30','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('19584964dca7a0bdd26ae185b3bf52fc','查询详情','3','查询详情','ca4fc5889e9b92021bca8bdfe9000ed9','/manage/wxQrCode/info',NULL,'0','1','2022-04-17 11:46:56','2022-04-17 11:46:56','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('2776390c304f6681d2c4d59abc6b705d','查询列表','3','查询列表','ca4fc5889e9b92021bca8bdfe9000ed9','/manage/wxQrCode/list',NULL,'0','1','2022-04-17 11:46:31','2022-04-17 11:46:31','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('5427b15cacd623659bef235ec15f3d1e','创建二维码','3','创建二维码','ca4fc5889e9b92021bca8bdfe9000ed9','/manage/wxQrCode/createTicket',NULL,'0','1','2022-04-17 11:46:11','2022-04-17 11:46:11','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('cca1027f0430c09d83658b4955854de8','创建、更新菜单','3','创建、更新菜单','7621dbd590ba264784b247c3c0b6b633','/manage/wxMenu/updateMenu',NULL,'0','1','2022-04-17 11:43:58','2022-04-17 11:43:58','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('557582809fd4773d293e6c990283c61a','获取公众号菜单','3','获取公众号菜单','7621dbd590ba264784b247c3c0b6b633','/manage/wxMenu/getMenu',NULL,'0','1','2022-04-17 11:43:43','2022-04-17 11:43:43','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('44cdf082a736f0c6cceed2b056fcf0bb','删除素材','3','删除素材','9ea7cd9929681053bf097efec09c79ab','/manage/wxAssets/materialDelete',NULL,'0','1','2022-04-17 11:43:13','2022-04-17 11:43:13','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('5344ef6e94267831297bb08219632d5e','添加多媒体永久素材','3','添加多媒体永久素材','9ea7cd9929681053bf097efec09c79ab','/manage/wxAssets/materialFileUpload',NULL,'0','1','2022-04-17 11:43:03','2022-04-17 11:43:03','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('5c634b7128e67baad4d3521f23d62944','修改图文素材文章','3','修改图文素材文章','9ea7cd9929681053bf097efec09c79ab','/manage/wxAssets/materialArticleUpdate',NULL,'0','1','2022-04-17 11:42:29','2022-04-17 11:42:29','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('89c8163c98e0d9630b6320049d352fa7','修改图文素材文章','3','修改图文素材文章','9ea7cd9929681053bf097efec09c79ab','/manage/wxAssets/materialNewsUpload',NULL,'0','1','2022-04-17 11:42:16','2022-04-17 11:42:16','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('40d45a315cd07c3cf28adccacd17f402','添加图文永久素材','3','添加图文永久素材','9ea7cd9929681053bf097efec09c79ab','/manage/wxAssets/materialNewsUpload',NULL,'0','1','2022-04-17 11:41:03','2022-04-17 11:41:03','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('e8f0a7873929a19add86e7f271f5b354','分页获取图文素材列表','3','分页获取图文素材列表','9ea7cd9929681053bf097efec09c79ab','/manage/wxAssets/materialNewsBatchGet',NULL,'0','1','2022-04-17 11:40:50','2022-04-17 11:40:50','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('4925f1b98665a2c3a3fc6a9733a729f6','非图文素材列表','3','根据类别分页获取非图文素材列表','9ea7cd9929681053bf097efec09c79ab','/manage/wxAssets/materialNewsInfo',NULL,'0','1','2022-04-17 11:40:33','2022-04-17 11:40:33','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('7c6f920adea82a20cce0c01f74bc0af1','文章素材总数','3','文章素材总数','9ea7cd9929681053bf097efec09c79ab','/manage/wxAssets/materialCount',NULL,'0','1','2022-04-17 11:40:05','2022-04-17 11:40:05','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('17ea72113701c0bed7563ba46adebea4','批量向用户发送模板消息','3','通过用户筛选条件（一般使用标签筛选），将消息发送给数据库中所有符合筛选条件的用户','cb2775862534ba2ee927a20a88f68885','/manage/msgTemplate/sendMsgBatch',NULL,'0','1','2022-04-17 11:25:22','2022-04-17 11:25:22','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('4d88f83577ee2b02ad0a5044051b4259','同步公众号模板','3','同步公众号模板','cb2775862534ba2ee927a20a88f68885','/manage/msgTemplate/syncWxTemplate',NULL,'0','1','2022-04-17 11:25:06','2022-04-17 11:25:06','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('5753f2f56c3a9cddc4b8881bf5cbf6a3','删除','3','删除','cb2775862534ba2ee927a20a88f68885','/manage/msgTemplate/delete',NULL,'0','1','2022-04-17 11:24:41','2022-04-17 11:24:41','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('c6f29b2ba0f91b678dfcace3606b01af','修改','3','修改','cb2775862534ba2ee927a20a88f68885','/manage/msgTemplate/update',NULL,'0','1','2022-04-17 11:24:26','2022-04-17 11:24:26','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('c0ebf532df8c42944c8fc4dd690c548c','保存','3','保存','cb2775862534ba2ee927a20a88f68885','/manage/msgTemplate/save',NULL,'0','1','2022-04-17 11:24:10','2022-04-17 11:24:10','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('2f8e39e3fd341f01385e0d7f61bb9d34','通过名称获取详情','3','通过名称获取详情','cb2775862534ba2ee927a20a88f68885','/manage/msgTemplate/getByName',NULL,'0','1','2022-04-17 11:23:49','2022-04-17 11:23:49','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('8a694655421104a43e65867f09f43fb4','模板信息','3','模板信息','cb2775862534ba2ee927a20a88f68885','/manage/msgTemplate/info',NULL,'0','1','2022-04-17 11:21:31','2022-04-17 11:21:31','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('7275db286a64e757f15c12c63e462476','消息模板','3','消息模板','cb2775862534ba2ee927a20a88f68885','/manage/msgTemplate/list',NULL,'0','1','2022-04-17 11:21:06','2022-04-17 11:21:06','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('a8bcc62d4476bfc5e1f6d8db110ff787','详情','3','详情','cb2775862534ba2ee927a20a88f68885','/manage/msgTemplate/info',NULL,'0','1','2022-04-17 11:16:32','2022-04-17 11:16:32','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('dff34b603a6739bbf986e647a5a56c4d','消息模板查询','3','消息模板查询','cb2775862534ba2ee927a20a88f68885','/manage/msgTemplate/list',NULL,'0','1','2022-04-17 11:16:15','2022-04-17 11:16:15','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('72ce150e29851712cbdd3b6986680d57','删除','3','删除','44790780099c5f9d1ed37275f917e81a','/manage/msgReplyRule/delete',NULL,'0','1','2022-04-17 11:14:49','2022-04-17 11:14:49','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('ad78015b0113ad1521b2732ed1e07a81','修改','3','修改','44790780099c5f9d1ed37275f917e81a','/manage/msgReplyRule/update',NULL,'0','1','2022-04-17 11:14:35','2022-04-17 11:14:35','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('a822d7216c5d404306d6d142842c9f54','保存','3','保存','44790780099c5f9d1ed37275f917e81a','/manage/msgReplyRule/save',NULL,'0','1','2022-04-17 11:14:20','2022-04-17 11:14:20','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('cbbb0f3e13e80c4079154022ebf711cc','查询信息','3','查询信息','44790780099c5f9d1ed37275f917e81a','/manage/msgReplyRule/info',NULL,'0','1','2022-04-17 11:14:09','2022-04-17 11:14:09','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('14090884fcf803a8f2796d1242a6c1c0','自动回复规则查询','3','自动回复规则查询','44790780099c5f9d1ed37275f917e81a','/manage/msgReplyRule/list',NULL,'0','1','2022-04-17 11:13:34','2022-04-17 11:13:34','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('b447ab2ea5ffbc4c9c4567e8870902e3','账号删除','3','账号删除','1d62bb5e2750cbdb4de04037f6c47a03','/manage/wxAccount/delete',NULL,'0','1','2022-04-17 10:55:07','2022-04-17 10:55:07','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('c890d29df0a53c4f54b539e23a3db35f','保存账号','3','保存账号','1d62bb5e2750cbdb4de04037f6c47a03','/manage/wxAccount/save',NULL,'0','1','2022-04-17 10:54:00','2022-04-17 10:54:00','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('4bc2d7c15f11c8755ae30c9fdd0e8e83','查询账号信息','3','查询账号信息','1d62bb5e2750cbdb4de04037f6c47a03','/manage/wxAccount/info',NULL,'0','1','2022-04-17 10:53:01','2022-04-17 10:53:40','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('862dd6145129a931fd72c5701827123c','查询账号列表','3','查询账号列表','1d62bb5e2750cbdb4de04037f6c47a03','/manage/wxAccount/list',NULL,'0','1','2022-04-17 10:52:21','2022-04-17 10:52:21','1','1','0');



ALTER TABLE t_picture_sort add level int(2) default 1 COMMENT '图片类别等级';



/**
* 修改账号表，新增绑定和解绑时间
* date：2022年5月1日16:08:52
**/
ALTER TABLE t_user_account add bind_time timestamp NULL DEFAULT NULL COMMENT '第三方账号绑定时间';
ALTER TABLE t_user_account add unbind_time timestamp NULL DEFAULT NULL COMMENT '第三方账号解绑时间';
ALTER TABLE t_user_account add avatar varchar(100) NULL COMMENT '个人头像';
ALTER TABLE t_user_account add email varchar(100) NULL COMMENT '邮箱';
ALTER TABLE t_user_account add gender tinyint(3) unsigned NULL COMMENT '性别(1:男2:女)';
ALTER TABLE t_user_account ADD nick_name VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '第三方用户昵称';
ALTER TABLE t_user_account ADD summary VARCHAR(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '简介';


/**
* 增加第三方账号刷数 和 默认头像
* date：2022年5月1日16:08:52
**/
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('3ece698b2f2f95b715b220ca8caf961a','第三方账号刷数','3','第三方账号刷数','fb4237a353d0418ab42c748b7c1d64c6','/user/flushUserAccount',NULL,'0','1','2022-05-01 16:05:23','2022-05-01 16:05:23','1','1','0');
insert into `t_sys_params` (`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) values('3d89c12cad7c719c2a9ee1214388355b','1','用户默认头像','USER_DEFAULT_AVATAR','用户默认头像','8a0d166d95895d258777c3a751ce95d5,76b93ea63169cddb8f269a30021902bc','1','2022-05-01 14:41:50','2022-05-01 14:51:26','0');


/**
* 新增菜单
* date：2022年5月2日15:59:30
**/
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('d3d7173b5aa22a9c09eca930420f7bb5','编辑课程标签','3','编辑课程标签','497aa1e33366f2fad55ef1bbce5ef32c','/media/tag/edit',NULL,'0','1','2022-05-02 10:52:20','2022-05-02 10:52:20','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('15d1ce9c32d3e292be5b326c69f147db','新增课程标签','3','新增课程标签','497aa1e33366f2fad55ef1bbce5ef32c','/media/tag/add',NULL,'0','1','2022-05-02 10:52:07','2022-05-02 10:52:07','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('96cb5f4b6e8f8e657a50d2d549a0ee23','编辑课程分类','3','编辑课程分类','2df4f2ce43c3116d818bff87561aa408','/media/category/edit',NULL,'0','1','2022-05-02 10:51:12','2022-05-02 10:51:12','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('e353405ee72fa94644ba794e28eb7563','新增课程分类','3','新增课程分类','2df4f2ce43c3116d818bff87561aa408','/media/category/add',NULL,'0','1','2022-05-02 10:50:56','2022-05-02 10:50:56','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('f7bc5b79a1c2fc83cd11285a5ec00f9e','更新媒资演员详细信息','3','更新媒资演员详细信息','2db5aad78770ae5b59cf9ce81d0b4256','/media/actor/edit',NULL,'0','1','2022-05-02 10:49:24','2022-05-02 10:49:24','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('89345272ec086feb9d06dec012645387','新增媒资演员详细信息','3','新增媒资演员详细信息','2db5aad78770ae5b59cf9ce81d0b4256','/media/actor/add',NULL,'0','1','2022-05-02 10:49:05','2022-05-02 10:49:05','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('c5dcc4e5c8b0162832a27fb9b0ec34bc','积分补偿','3','积分补偿','fb4237a353d0418ab42c748b7c1d64c6','/credits/compensation',NULL,'0','1','2022-05-02 10:24:34','2022-05-02 10:24:34','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('d387a26cf7d6c27dd0f34502cae5ac31','查看积分记录','3','查看积分记录','fb4237a353d0418ab42c748b7c1d64c6','/credits/queryCredits',NULL,'0','1','2022-05-02 10:22:37','2022-05-02 10:22:37','1','1','0');


insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('cdf3d58c826013bb9ae221255975d3ad','50','资源类型','sys_resource_type','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','资源类型','1','2022-04-29 23:59:49','2022-04-29 23:59:49','1','0');

insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('9a4df01a79d0e8799867ec9a21a930c8','153','20a4dd3551aa6264f7e43c2274820763','WECHAT','WECHAT',NULL,'primary','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','公众号','1','2022-05-02 10:14:36','2022-05-02 10:14:36','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('1f3c3372c4a0faac43fee3b01ff4287c','152','cdf3d58c826013bb9ae221255975d3ad','博客','1',NULL,'danger','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-04-30 00:01:38','2022-04-30 00:01:38','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('99d6c265ea6ddef47becec003c83ffa8','151','cdf3d58c826013bb9ae221255975d3ad','问答','2',NULL,'warning','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-04-30 00:01:08','2022-04-30 00:01:08','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('7d74e91cf4e88cc33c7b8fed83bc7765','150','cdf3d58c826013bb9ae221255975d3ad','媒资','3',NULL,'info','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-04-30 00:01:00','2022-04-30 00:01:00','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('3bfab1b13a5d9d68703df5526e759fdc','149','cdf3d58c826013bb9ae221255975d3ad','动态','4',NULL,'success','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-04-30 00:00:41','2022-04-30 00:00:41','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('30d46df9a66a0be424ca1ff512448bcc','148','cdf3d58c826013bb9ae221255975d3ad','评论','5',NULL,'primary','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-04-30 00:00:25','2022-04-30 00:00:25','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('a06f23b1e8c9acae3bbc8241a84cadbd','147','cdf3d58c826013bb9ae221255975d3ad','问题','6',NULL,'default','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-04-30 00:00:11','2022-04-30 00:00:31','1','0');



/**
* 搜索增加默认条件
* date：2022年5月2日15:59:30
**/
insert into `t_sys_params` (`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) values('f9f44c5e8555df5abff712c011c350f9','1','搜索最大分页数','SYS_SEARCH_SIZE',NULL,'20','1','2022-05-07 08:28:41','2022-05-07 08:28:41','0');



/**
* 增加用户等级字典
* date：2022年5月15日17:55:39
**/
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('86cff62632f4028f45da55c099eeb351','51','用户等级','sys_user_level','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','用户等级','1','2022-05-15 13:37:32','2022-05-15 13:37:32','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('eaffa465fcb8e44a29373f2480435bdf','159','86cff62632f4028f45da55c099eeb351','Lv6','6',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','登峰造极','1','2022-05-15 17:10:54','2022-05-15 17:17:57','1','6');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('7b30e6238d8fcc6504f6321509f34d8e','158','86cff62632f4028f45da55c099eeb351','Lv1','1',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','初出茅庐','1','2022-05-15 13:39:03','2022-05-15 17:18:09','1','1');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('88f83b78e969811daf1f9355b8611534','157','86cff62632f4028f45da55c099eeb351','Lv2','2',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','崭露头角','1','2022-05-15 13:38:50','2022-05-15 17:17:42','1','2');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('4b449856dccad98c6b0b790a343a7ed6','156','86cff62632f4028f45da55c099eeb351','Lv3','3',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','小有名气','1','2022-05-15 13:38:44','2022-05-15 17:18:16','1','3');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('3079c18f14c6438081a4d2755457adf3','155','86cff62632f4028f45da55c099eeb351','Lv4','4',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','游刃有余','1','2022-05-15 13:38:37','2022-05-15 17:18:33','1','4');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('51935e3d4043c739da2433e5742a5f8c','154','86cff62632f4028f45da55c099eeb351','Lv5','5',NULL,NULL,'0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','大显身手','1','2022-05-15 13:38:24','2022-05-15 17:18:50','1','5');

/**
后台站内信通知标识位
 */
 ALTER TABLE t_notice ADD  `is_black` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否前台通知 【0 前台  1 后台】';
 /**
  * 站内信添加权限菜单
  */
INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('a7a2a649297321c7f380773b6836c4cf', '站内信通知', 3, '站内信通知', '78f24799307cb63bc3759413dadf4d1a', '/notice/getBlackNoticeList', NULL, 0, 1, '2022-06-09 17:52:47', '2022-06-09 17:52:47', 1, 1, 0);
INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('bb14abf219b2511c1a4f5266b7c70107', '获取后台站内信消息数量', 3, '获取后台站内信消息数量', '78f24799307cb63bc3759413dadf4d1a', '/notice/searchAllNoticeCount', NULL, 0, 1, '2022-06-10 11:07:01', '2022-06-10 11:09:42', 1, 1, 0);
INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('fff30b473103181c8efd360512dc617e', '删除站内信小红点提示', 3, '删除站内信小红点提示', '78f24799307cb63bc3759413dadf4d1a', '/notice/deleteRedisBatchNotice', NULL, 0, 1, '2022-06-10 11:06:44', '2022-06-10 11:06:44', 1, 1, 0);
INSERT INTO `t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('7ea8c0c039397baaba9260fc16a49826', '批量删除后台通知', 3, '批量删除后台通知', '78f24799307cb63bc3759413dadf4d1a', '/notice/deleteBatchBlackNotice', NULL, 0, 1, '2022-06-10 11:06:26', '2022-06-10 11:06:26', 1, 1, 0);



/**
用于阿里oss上传文件
 */
ALTER TABLE t_system_config ADD aliyun_bucket_name varchar(255) NULL comment "阿里云对象存储桶名";
ALTER TABLE t_system_config ADD aliyun_endpoint varchar(255) NULL  comment "阿里云对象存储Endpoint";
ALTER TABLE t_system_config ADD aliyun_access_key varchar(255) NULL comment "阿里云对象存储公钥";
ALTER TABLE t_system_config ADD aliyun_access_key_secret varchar(255) NULL comment "阿里云对象存储私钥";
ALTER TABLE t_system_config ADD upload_alioss varchar(255) NULL comment "是否上传阿里云对象存储";
ALTER TABLE t_system_config ADD alioss_picture_base_url varchar(255) NULL comment "阿里云对象存储访问域名";
ALTER TABLE t_system_config ADD aliyun_catalog varchar(255) NULL comment "阿里云对象存储上传目录";


insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('f8ed0a519458b60584dc661149bc89c6','160','904965b87673d2dd762c9ac2b6726813','阿里云存储','3',NULL,'success','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','阿里云oss','1','2022-06-12 00:41:53','2022-06-12 12:29:20','1','0');

/**
热搜白名单
 */
INSERT INTO `t_sys_params`(`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) VALUES ('16f04204768bc80eef70d9bcfe1c049a', '1', '热搜白名单', 'SYS_WHITELIST_WORD', '热搜白名单', '一键部署,蘑菇博客,', 1, '2022-06-20 17:05:57', '2022-06-20 17:05:57', 40);


CREATE TABLE `t_hot_search` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '搜索内容',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户UID',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `user_ip` varchar(32) DEFAULT NULL COMMENT '用户ip',
  `status` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='热搜表';

/**
用户等级每日发表数限制
 */
INSERT INTO `mogu_blog_business`.`t_sys_params`(`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) VALUES ('490837a7212a050f5c6d403337c6ebc9', '1', '用户等级限制发布数量', 'LEVEL_LIMIT_PUBLISH', '用户等级限制发布数量', '{\n	\"Lv1\": {\n		\"blogCount\": \"1\",\n		\"questionCount\": \"1\",\n		\"problemCount\": \"3\",\n		\"momentCount\": \"2\",\n		\"commentCount\": \"5\"\n	},\n	\"Lv2\": {\n		\"blogCount\": \"5\",\n		\"questionCount\": \"5\",\n		\"problemCount\": \"5\",\n		\"momentCount\": \"5\",\n		\"commentCount\": \"15\"\n	},\n	\"Lv3\": {\n		\"blogCount\": \"10\",\n		\"questionCount\": \"10\",\n		\"problemCount\": \"10\",\n		\"momentCount\": \"10\",\n		\"commentCount\": \"25\"\n	},\n	\"Lv4\": {\n		\"blogCount\": \"15\",\n		\"questionCount\": \"15\",\n		\"problemCount\": \"15\",\n		\"momentCount\": \"15\",\n		\"commentCount\": \"35\"\n	},\n	\"Lv5\": {\n		\"blogCount\": \"99\",\n		\"questionCount\": \"99\",\n		\"problemCount\": \"99\",\n		\"momentCount\": \"99\",\n		\"commentCount\": \"99\"\n	}\n}', 1, '2022-06-29 13:37:43', '2022-06-29 14:46:39', 0);


/**
    热搜黑名单
 */
 INSERT INTO `mogu_blog_business`.`t_sys_params`(`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) VALUES ('ccc99f7f56e1015dc157553f02c8b704', '1', '热搜黑名单', 'SYS_HOTSEARCH_BLACKLIST', '热搜黑名单', '暗黑夜神,', 1, '2022-06-25 09:45:18', '2022-06-25 09:45:18', 0);
/**
    广告敏感词
 */
 INSERT INTO `mogu_blog_business`.`t_sys_params`(`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) VALUES ('cda12e5f3fe2c98a0e7e39ae1e70828f', '1', '广告敏感词', 'SYS_SENSITIVE_SLOGAN', '广告敏感词', '加群;vx群;', 1, '2022-06-30 10:15:05', '2022-06-30 10:15:05', 0);

/**
    正则规则参数
 */
 INSERT INTO `mogu_blog_business`.`t_sys_params`(`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) VALUES ('6c9c032eb7ee43ba455de07708b8dde7', '1', '广告过滤正则表达式', 'SYS_REGULAR_SLOGAN', '广告过滤正则表达式', '[1-9]\\d*,[\\u4e00-\\u9fa5],[a-zA-Z]+$', 1, '2022-07-01 09:15:55', '2022-07-01 09:15:55', 0);
/**
    动态审核按钮权限
 */
 INSERT INTO `mogu_blog_business`.`t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('77b56f4c4d2f0a3916ade3facc38679e', '动态审批', 3, '动态审批', '4be965a04e47c25a276e9a9d70c45d95', '/userMoment/audit', NULL, 0, 1, '2022-06-30 15:39:37', '2022-06-30 15:39:37', 1, 1, 0);

/**
    问答审核按钮
 */
 INSERT INTO `mogu_blog_business`.`t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('7bc4d7843e4bbe52d4e303b7ccd33435', '问答审核', 3, '问答审核', '3218dc3afad673f411baf774e22f3deb', '/question/audit', NULL, 0, 1, '2022-07-06 15:07:44', '2022-07-06 15:07:44', 1, 1, 0);

/**
    评论管理
 */
INSERT INTO `mogu_blog_business`.`t_category_menu`(`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) VALUES ('09bd176f1c98f2c0d72f1782f11b37b0', '评论审核', 3, '评论审核', '9beb7caa2c844b36a02789262dc76fbe', '/comment/audit', NULL, 0, 1, '2022-07-11 15:54:21', '2022-07-11 15:54:21', 1, 1, 0);

/**
    新增博客特权文章字段
 */
ALTER TABLE  t_blog ADD is_vip tinyint DEFAULT '0' COMMENT '是否是特权文章（0：普通文章，1 ：特权文章）';

/*
    评论表新增审核相关
	2022年7月12日09:17:13
*/
ALTER TABLE t_comment ADD audit_status tinyint(1) unsigned NOT NULL DEFAULT 1 COMMENT '审批状态【0：待审批，1：审核未通过，2：审核通过】';
ALTER TABLE t_comment ADD audit_name varchar(32) DEFAULT NULL COMMENT '审批人';
ALTER TABLE t_comment ADD reject_reason varchar(255) DEFAULT NULL COMMENT '审批拒绝原因';
ALTER TABLE t_comment ADD audit_time timestamp NULL COMMENT '审批时间';


ALTER TABLE t_question ADD audit_status tinyint(1) unsigned NOT NULL DEFAULT 1 COMMENT '审批状态【0：待审批，1：审核未通过，2：审核通过】';
ALTER TABLE t_question ADD audit_name varchar(32) DEFAULT NULL COMMENT '审批人';
ALTER TABLE t_question ADD reject_reason varchar(255) DEFAULT NULL COMMENT '审批拒绝原因';
ALTER TABLE t_question ADD audit_time timestamp NULL COMMENT '审批时间';


/*
    增加热搜模块
	2022年8月7日07:53:12
*/
insert into `t_web_navbar` (`uid`, `name`, `navbar_level`, `summary`, `parent_uid`, `url`, `icon`, `is_show`, `is_jump_external_url`, `sort`, `status`, `create_time`, `update_time`) values('704f10018dea3a73ff51cdf2232f6bfe','热搜','1','蘑菇热搜',NULL,'/hot','el-icon-umbrella','1','0','0','1','2022-08-06 15:28:02','2022-08-06 15:28:02');



/*
   增加 t_subject_sort，专题分类表
   @date 2022年9月25日21:58:36
*/
CREATE TABLE `t_subject_sort` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `name` varchar(100) DEFAULT NULL COMMENT '分类名',
  `summary` varchar(1000) DEFAULT NULL COMMENT '分类简介',
  `is_selection` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否精选',
  `is_publish` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '是否上架',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `click_count` int(11) DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `file_uid` varchar(32) DEFAULT NULL COMMENT '分类图',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='专题分类表';


ALTER TABLE t_subject ADD subject_sort_uid varchar(32) DEFAULT NULL COMMENT '专栏分类uid';
ALTER TABLE t_subject ADD is_selection tinyint(3) DEFAULT '0' COMMENT '是否精选';
ALTER TABLE t_subject ADD is_publish tinyint(3) DEFAULT '1' COMMENT '是否上架';


/*
   增加 is_only_subject_show，是否仅专栏可见
   @date 2022年9月27日08:10:54
*/
ALTER TABLE t_blog ADD is_only_subject_show tinyint(1) DEFAULT '0' COMMENT '是否仅专栏可见：0 否，1 是';

/**
* 增加专栏分类操作
**/
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('6b903750192647a642b8f14b0b4665e7','新增','3','新增','7e1e3576f5b03c4b73703d806768ca40','/subjectSort/add','','0','1','2022-09-25 22:29:33','2022-09-25 22:29:33','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('2f5f2da010c4e135a680e6c74091cca7','编辑','3','编辑','7e1e3576f5b03c4b73703d806768ca40','/subjectSort/edit','','0','1','2022-09-25 22:29:17','2022-09-25 22:29:17','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('a6e76ae931ee18683687e62c5f8af83c','删除','3','删除','7e1e3576f5b03c4b73703d806768ca40','/subjectSort/deleteBatch','','0','1','2022-09-25 22:28:55','2022-09-25 22:28:55','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('a816cd962d672c66e6e07552ce669e4d','查询列表','3','查询列表','7e1e3576f5b03c4b73703d806768ca40','/subjectSort/getList','','0','1','2022-09-25 22:28:39','2022-09-25 22:28:39','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('7e1e3576f5b03c4b73703d806768ca40','专栏分类','2','专栏分类','49b42250abcb47ff876bad699cf34f03','/blog/subjectSort','el-icon-document-copy','0','1','2022-09-25 22:27:44','2022-09-25 22:33:38','1','0','0');



ALTER TABLE t_subject ADD author varchar(255) NULL COMMENT '专栏作者';
ALTER TABLE t_subject ADD is_original tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否原创（0:否 1：是）';
ALTER TABLE t_subject ADD origin  varchar(255) NULL COMMENT '专栏来源';


/*
* 消息是否撤回： 0 否，1 是
*/
ALTER TABLE t_im_message ADD is_withdraw tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否撤回（0:否 1：是）';
/*
* 消息撤回时间
*/
insert into `t_sys_params` (`uid`, `params_type`, `params_name`, `params_key`, `remark`, `params_value`, `status`, `create_time`, `update_time`, `sort`) values('292727ff13623e3120469f772bb928bb','1','聊天撤回时间','MESSAGE_WITHDRAW_TIME','聊天撤回时间【秒】','600','1','2022-10-16 15:56:26','2022-10-16 15:56:26','0');


insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('7532654b47cce1100bc39e11adad636d','列表查询','3','图片素材','5b41f68e29832c098b73968263562021','/file/getList','','0','1','2022-10-23 22:09:32','2022-10-23 22:09:32','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('5b41f68e29832c098b73968263562021','素材管理','2','文件管理','65e22f3d36d94bcea47478aba02895a1','/picture/file','el-icon-tickets','0','1','2022-10-23 22:08:46','2022-10-23 22:08:46','1','0','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('d4f926240b17c78e10b2c65af08295e6','树形获取分类','3','树形获取分类','2df4f2ce43c3116d818bff87561aa408','/media/category/getAll',NULL,'0','1','2022-10-22 10:50:37','2022-10-22 10:50:37','1','1','0');


/*
* 用户表情包
*/
CREATE TABLE `t_emoticon` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户uid',
  `file_uid` varchar(32) DEFAULT NULL COMMENT '表情包文件uid',
  `is_selection` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否精选',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `click_count` int(11) DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`uid`),
  KEY `idx_user_uid` (`user_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表情包表';


insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('902cf0ff0e434799f68f46f6a7bed865','批量删除','3','批量删除','5b41f68e29832c098b73968263562021','/file/batchDeleteFile','','0','1','2022-10-30 13:22:14','2022-10-30 13:36:57','1','1','0');


/*
* 增加用户等级图片
* date：2022年12月14日23:27:40
*/
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('3fd14dd477698b2b73886577dd6dbdb3','52','用户等级图片','user_level_images','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','用户等级对应的图片','1','2022-12-14 09:11:32','2022-12-14 09:11:32','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('fac632cc85c0de20bd1e24e9e1d6c909','170','3fd14dd477698b2b73886577dd6dbdb3','10','https://oos.moguit.cn/image/lv10.png',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-14 09:17:40','2022-12-14 09:28:23','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('8a0622f5a58e12a8d2886cdb1f1481e4','169','3fd14dd477698b2b73886577dd6dbdb3','9','https://oos.moguit.cn/image/lv9.png',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-14 09:17:29','2022-12-14 09:28:18','1','1');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('b2e57ac0c8b55206f7958839b71e8b39','168','3fd14dd477698b2b73886577dd6dbdb3','8','https://oos.moguit.cn/image/lv8.png',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-14 09:17:17','2022-12-14 09:28:13','1','2');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('705146c71e6fe31205c889b160b50888','167','3fd14dd477698b2b73886577dd6dbdb3','7','https://oos.moguit.cn/image/lv7.png',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-14 09:17:05','2022-12-14 09:28:01','1','3');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('cd90bac485c399e861ab90ea35ecd7df','166','3fd14dd477698b2b73886577dd6dbdb3','6','https://oos.moguit.cn/image/lv6.png',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-14 09:16:51','2022-12-14 09:27:56','1','4');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('09e6c620488d239dc144840596731137','165','3fd14dd477698b2b73886577dd6dbdb3','5','https://oos.moguit.cn/image/lv5.png',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-14 09:16:39','2022-12-14 09:27:51','1','5');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('9366b8b31757fe086f18653eb6aaac74','164','3fd14dd477698b2b73886577dd6dbdb3','4','https://oos.moguit.cn/image/lv4.png',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-14 09:16:27','2022-12-14 09:27:41','1','7');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('22872ded4ca39c9fae2405fcbbfd34f9','163','3fd14dd477698b2b73886577dd6dbdb3','3','https://oos.moguit.cn/image/lv3.png',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-14 09:16:16','2022-12-14 09:27:36','1','8');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('41393ac816e429f81faabca74556a651','162','3fd14dd477698b2b73886577dd6dbdb3','2','https://oos.moguit.cn/image/lv2.png',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-14 09:16:09','2022-12-14 09:27:31','1','9');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('d83fe7e4c478cef99c054175bd098190','161','3fd14dd477698b2b73886577dd6dbdb3','1','https://oos.moguit.cn/image/lv1.png',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-14 09:15:37','2022-12-14 09:27:27','1','10');


/*
* 增加成就系统【任务系统】
* date：2022年12月17日21:11:42
*/
CREATE TABLE `t_task_config` (
  `uid` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一uid',
  `market_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '活动id',
  `group_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务描述',
  `rule` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务规则',
  `icon` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务icon',
  `step_config` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务步骤',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `button` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '按钮',
  `limit_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '限制类型',
  `limit_cron` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '限制类型cron',
  `page_show` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '页面展示',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `status` int DEFAULT '0' COMMENT '状态',
  `is_publish` varchar(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否发布(1:是，0:否)',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='任务配置表';


insert  into `t_task_config`(`uid`,`market_name`,`group_id`,`name`,`description`,`rule`,`icon`,`step_config`,`start_time`,`end_time`,`button`,`limit_type`,`limit_cron`,`page_show`,`create_time`,`update_time`,`sort`,`status`,`is_publish`) values 
('21824c9b86dc8ad6c378cacfb664f275','task','DAY','【每周】连续签到7天','奖励10积分','<h5 class=\"f30 c333 guide_icon1\">操作指引</h5> <DIV class=\"text\">1、点击手机“设置-通知-移动云盘”或“应用权限”-“中国移动云盘”；','el-icon-s-tools','[{\"type\":\"click\",\"ext\":\"task\",\"button\":{\"app\":{\"text\":\"已完成\"}}}]','2022-12-17 15:14:15','2022-12-25 15:14:22','{\"app\":{\"itemBg\":\"#fff4eb\",\"btnBg\":\"#fca029\",\"text\":\"去完成\",\"action\":\"popjump\",\"link\":\"mcloud://guideToNotification\",\"popBtn\":\"去开启\"}}','DAY','',NULL,'2022-12-17 15:15:03','2022-12-25 12:31:52',1,1,'1'),
('21824c9b86dc8ad6c378cacfb664f276','task','DAY','发布两篇文章','每日文章','<h5 class=\"f30 c333 guide_icon1\">操作指引</h5> <DIV class=\"text\">1、点击手机“设置-通知-移动云盘”或“应用权限”-“中国移动云盘”；','el-icon-video-play','[{\"type\":\"click\",\"ext\":\"task\"},{\"type\":\"blog\",\"ext\":{\"type\":\"test\",\"target\":2},\"button\":{\"app\":{\"itemBg\":\"#fefbf2\",\"btnBg\":\"#fca029\",\"text\":\"去写文章\"},\"out\":{\"itemBg\":\"#fefbf2\",\"text\":\"去写文章\"}}}]','2022-12-17 15:14:15','2022-12-25 15:14:22','{\"app\":{\"itemBg\":\"#fff4eb\",\"btnBg\":\"#fca029\",\"text\":\"去完成\",\"action\":\"popjump\",\"link\":\"mcloud://guideToNotification\",\"popBtn\":\"去开启\"}}','DAY','',NULL,'2022-12-17 15:15:03','2022-12-22 08:42:34',0,1,'1'),
('4071c31babac68fc182e394d5f5e104a','task','DAY','【奖励】发表一篇动态','奖励签到卡一张','\"\\\"<h5 class=\\\\\\\"f30 c333 guide_icon1\\\\\\\">操作指引</h5> <DIV class=\\\\\\\"text\\\\\\\">1、点击手机“设置-通知-移动云盘”或“应用权限”-“中国移动云盘”；\\\"\"','el-icon-view','[{\"type\":\"moment\",\"ext\":{\"target\": 1}},{\"type\":\"award\",\"ext\":{\"type\":\"signInCard\", \"value\": \"1\"}}]','2022-12-24 21:30:34','2014-10-07 21:30:34',NULL,'DAY',NULL,NULL,'2022-12-24 21:30:34','2022-12-24 21:30:34',0,1,'1'),
('4e1b95c0e010d22a9d72cb05ef27dea2','task','DAY','发布两条评论','奖励10积分','\"\\\"<h5 class=\\\\\\\"f30 c333 guide_icon1\\\\\\\">操作指引</h5> <DIV class=\\\\\\\"text\\\\\\\">1、点击手机“设置-通知-移动云盘”或“应用权限”-“中国移动云盘”；\\\"\"','el-icon-delete','[{\"type\":\"click\",\"ext\":\"task\"},{\"type\":\"comment\",\"ext\":{\"type\":\"test\",\"target\":2},\"button\":{\"app\":{\"itemBg\":\"#fefbf2\",\"btnBg\":\"#fca029\",\"text\":\"去写文章\"},\"out\":{\"itemBg\":\"#fefbf2\",\"text\":\"去写文章\"}}}]','2022-12-19 23:50:17','2059-02-19 06:18:33',NULL,'DAY',NULL,NULL,'2022-12-19 23:31:37','2022-12-25 12:31:28',0,1,'1'),
('4e1b95c0e010d22a9d72cb05ef27dea3','achieve','DAY','发布两条评论','奖励10积分','\"\\\"<h5 class=\\\\\\\"f30 c333 guide_icon1\\\\\\\">操作指引</h5> <DIV class=\\\\\\\"text\\\\\\\">1、点击手机“设置-通知-移动云盘”或“应用权限”-“中国移动云盘”；\\\"\"','el-icon-delete','[{\"type\":\"click\",\"ext\":\"task\"},{\"type\":\"comment\",\"ext\":{\"type\":\"test\",\"target\":2},\"button\":{\"app\":{\"itemBg\":\"#fefbf2\",\"btnBg\":\"#fca029\",\"text\":\"去写文章\"},\"out\":{\"itemBg\":\"#fefbf2\",\"text\":\"去写文章\"}}}]','2022-12-19 23:50:17','2059-02-19 06:18:33',NULL,'DAY',NULL,NULL,'2022-12-19 23:31:37','2022-12-25 12:09:17',0,1,'1'),
('5ee8ad101d491e660f0e709ba0bb3252','task','DAY','【每日】发送两篇动态','奖励10积分（进度：%s/2）','\"\\\"<h5 class=\\\\\\\"f30 c333 guide_icon1\\\\\\\">操作指引</h5> <DIV class=\\\\\\\"text\\\\\\\">1、点击手机“设置-通知-移动云盘”或“应用权限”-“中国移动云盘”；\\\"\"','el-icon-food','[{\"type\":\"moment\",\"ext\":{\"target\": 2}},{\"type\":\"award\",\"ext\":{\"type\":\"signInCard\", \"value\": \"1\"}}]','2022-12-25 13:01:35','2014-10-08 13:01:35',NULL,'DAY',NULL,NULL,'2022-12-25 13:01:35','2022-12-25 13:01:35',0,1,'1'),
('be41bb61e2e4deae565f4bd7d8290436','task','DAY','【奖励】每日评论','奖励10积分','\"\\\"<h5 class=\\\\\\\"f30 c333 guide_icon1\\\\\\\">操作指引</h5> <DIV class=\\\\\\\"text\\\\\\\">1、点击手机“设置-通知-移动云盘”或“应用权限”-“中国移动云盘”；\\\"\"','el-icon-school','[{\"type\":\"comment\",\"ext\":{\"target\": 1}},{\"type\":\"award\",\"ext\":{\"type\":\"credits\", \"value\": \"10\"}}]','2022-12-24 18:10:19','2014-10-07 18:10:19',NULL,'DAY',NULL,NULL,'2022-12-24 18:10:19','2022-12-24 18:57:29',0,1,'1'),
('d101d9f0d3182ffbced4cbc24c19333f','achieve','EVER','【成就】发表10篇文章','成就，发表10篇文章','\"\\\"<h5 class=\\\\\\\"f30 c333 guide_icon1\\\\\\\">操作指引</h5> <DIV class=\\\\\\\"text\\\\\\\">1、点击手机“设置-通知-移动云盘”或“应用权限”-“中国移动云盘”；\\\"\"','el-icon-camera','[{\"type\":\"blog\",\"ext\":{\"target\":10}},{\"type\":\"award\",\"ext\":{\"type\":\"credits\", \"value\": \"10\"}},{\"type\":\"blog\",\"ext\":{\"target\":15}},{\"type\":\"award\",\"ext\":{\"type\":\"credits\", \"value\": \"20\"}}]','2022-12-24 10:46:57','2014-10-07 10:46:57',NULL,'EVER',NULL,NULL,'2022-12-24 10:46:57','2022-12-24 15:37:00',0,1,'1');


CREATE TABLE `t_user_task_record` (
  `uid` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一uid',
  `task_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务id',
  `market_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '活动id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户id',
  `state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '状态',
  `current_step` int DEFAULT NULL COMMENT '当前步骤',
  `current_process` int DEFAULT NULL COMMENT '当前步骤进度',
  `last_update` bigint NOT NULL COMMENT '最后一次更新时间',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `sort` int DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `status` int DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='任务记录表';


insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('40363dc12cc449c5e67bd236e64bf8a4','获取列表','3','获取列表','db6e9ac34b749070297a5d8ce2f8bf3f','/task/getPageList','','0','1','2022-12-25 19:01:55','2022-12-25 19:01:55','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('00a26e4f81142dd0a2af3e52cea80260','删除任务','3','删除任务','db6e9ac34b749070297a5d8ce2f8bf3f','/task/deleteBatch','','0','1','2022-12-25 19:00:56','2022-12-25 19:00:56','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('f887bdde0236829a98bd9146dde7cd3f','编辑任务','3','编辑任务','db6e9ac34b749070297a5d8ce2f8bf3f','/task/edit','','0','1','2022-12-25 19:00:31','2022-12-25 19:00:31','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('f742aa555af3d4a0f02359578565f46d','新增任务','3','新增任务','db6e9ac34b749070297a5d8ce2f8bf3f','/task/add','','0','1','2022-12-25 19:00:14','2022-12-25 19:00:14','1','1','0');
insert into `t_category_menu` (`uid`, `name`, `menu_level`, `summary`, `parent_uid`, `url`, `icon`, `sort`, `status`, `create_time`, `update_time`, `is_show`, `menu_type`, `is_jump_external_url`) values('db6e9ac34b749070297a5d8ce2f8bf3f','任务配置','2','任务配置','badf0010422b432ba6ec9c83a25012ed','/system/taskConfig','el-icon-collection-tag','0','1','2022-12-19 09:38:51','2022-12-19 09:42:01','1','0','0');



insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('387969f9898948a15c376bf661c11741','55','任务标识','sys_task_sign','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','任务标识，标识是任务，还是成就','1','2022-12-22 08:43:25','2022-12-22 08:43:25','1','0');
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('0a1bc5453273be626f9504131c271d36','54','任务动作','sys_task_action','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','任务动作','1','2022-12-19 22:28:44','2022-12-19 22:40:08','1','0');
insert into `t_sys_dict_type` (`uid`, `oid`, `dict_name`, `dict_type`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('76f62b5d476416c0c9b591a2b55a2bfb','53','任务类型','sys_task_type','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','任务类型【每日任务，每周任务，每月任务，新手任务】','1','2022-12-19 22:14:07','2022-12-19 22:14:51','1','0');

insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('944324304facb6723e37f2ffa601cfea','188','387969f9898948a15c376bf661c11741','任务','task',NULL,'warning','1','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','标识为普通的任务','1','2022-12-22 08:47:01','2022-12-22 08:51:17','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('27a5cc5de4524867bc3d13f51305c07a','187','387969f9898948a15c376bf661c11741','成就','achieve',NULL,'primary','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','表示该任务为成就【一般是一次性的任务】','1','2022-12-22 08:45:28','2022-12-22 08:47:16','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('e929ea041ea0bf09b656e5b7d96e67bc','186','76f62b5d476416c0c9b591a2b55a2bfb','定时任务','CRON',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-19 23:01:04','2022-12-19 23:01:04','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('c43e7be6a425414b0866435d72a0ff91','185','0a1bc5453273be626f9504131c271d36','添加表情包','EMOTICON',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','添加表情包','1','2022-12-19 22:34:11','2022-12-19 22:34:11','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('4ef3525b6218f3b575ce80db18e9f29a','184','0a1bc5453273be626f9504131c271d36','上传文件','FILE',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','上传文件','1','2022-12-19 22:33:47','2022-12-19 22:33:47','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('b2b1df4bfb0eee677371745862a7e308','183','0a1bc5453273be626f9504131c271d36','收藏','COLLECT',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','收藏资源','1','2022-12-19 22:33:01','2022-12-19 22:33:01','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('afb44346f5d2062ec03fe19b1bb4027f','182','0a1bc5453273be626f9504131c271d36','点击关注','WATCH',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','关注','1','2022-12-19 22:32:40','2022-12-19 22:32:40','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('72a9accbc5f1c2ef60fa9e5c4747f356','181','0a1bc5453273be626f9504131c271d36','点赞','PRAISE',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','点赞','1','2022-12-19 22:32:19','2022-12-19 22:32:19','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('f23419aafe4be4d771458648bbec404c','180','0a1bc5453273be626f9504131c271d36','发表评论','COMMENT',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','发表评论','1','2022-12-19 22:31:11','2022-12-19 22:31:11','1','0');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('2d521cbfbf88ac8477000977a398c163','179','0a1bc5453273be626f9504131c271d36','发表面经','PROBLEM',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','发表面经','1','2022-12-19 22:30:54','2022-12-19 22:30:54','1','2');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('9ccfbe902f2d72925564c39c25403637','178','0a1bc5453273be626f9504131c271d36','发表动态','MOMENT',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-19 22:30:13','2022-12-19 22:30:13','1','4');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('18e9ef25e17770cf4f648a97ab9686a5','177','0a1bc5453273be626f9504131c271d36','发表问答','QUESTION',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','发表问答','1','2022-12-19 22:29:47','2022-12-19 22:30:32','1','3');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('df5923ea932fc191844259aa11311222','176','0a1bc5453273be626f9504131c271d36','发表文章','ARTICLE',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','发表文章','1','2022-12-19 22:29:30','2022-12-19 22:30:23','1','5');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('6f319a7660203b63c35fcdcf8570c9b9','175','0a1bc5453273be626f9504131c271d36','签到','SIGN',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333',NULL,'1','2022-12-19 22:29:08','2022-12-19 22:29:08','1','6');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('00cdd4b6d4e95077f9181dad616e099a','174','76f62b5d476416c0c9b591a2b55a2bfb','每月任务','MONTH',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','每月可以领取','1','2022-12-19 22:23:43','2022-12-19 23:00:52','1','1');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('bd33510f0814fa72bd52499aa59d370b','173','76f62b5d476416c0c9b591a2b55a2bfb','每周任务','WEEK',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','每周可领取','1','2022-12-19 22:23:31','2022-12-19 22:23:31','1','2');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('d6146714056f4914fcfe9dd0f1d5b99f','172','76f62b5d476416c0c9b591a2b55a2bfb','每日任务','DAY',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','每天可以领取','1','2022-12-19 22:22:30','2022-12-19 22:23:08','1','3');
insert into `t_sys_dict_data` (`uid`, `oid`, `dict_type_uid`, `dict_label`, `dict_value`, `css_class`, `list_class`, `is_default`, `create_by_uid`, `update_by_uid`, `remark`, `status`, `create_time`, `update_time`, `is_publish`, `sort`) values('09f269aa7d800bf782fa864358476a8d','171','76f62b5d476416c0c9b591a2b55a2bfb','一次性任务','EVER',NULL,'','0','1f01cd1d2f474743b241d74008b12333','1f01cd1d2f474743b241d74008b12333','一次性任务，用户做完就没有了','1','2022-12-19 22:22:15','2022-12-24 17:00:02','1','4');





/*
* 勋章表
* date：2022年12月27日08:51:42
*/
CREATE TABLE `t_medal` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `file_url` varchar(255) DEFAULT NULL COMMENT '勋章图标url',
  `medal_classify_uid` varchar(32) DEFAULT NULL COMMENT '勋章分类uid',
  `name` varchar(32) DEFAULT NULL COMMENT '勋章名称',
  `summary` varchar(255) DEFAULT NULL COMMENT '勋章简介',
  `is_publish` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否上架',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='勋章表';


/*
* 勋章分类表
* date：2022年12月28日08:58:05
*/
CREATE TABLE `t_medal_classify` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `parent_uid` varchar(32) NOT NULL COMMENT '父uid',
  `name` varchar(32) DEFAULT NULL COMMENT '分类名称',
  `summary` varchar(255) DEFAULT NULL COMMENT '分类简介',
  `is_publish` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否上架',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='勋章分类表';


/*
* 收藏表和点赞表
* date：2023年1月1日19:46:44
*/
ALTER TABLE t_collect ADD to_user_uid varchar(32) NULL COMMENT '被收藏的用户uid';

ALTER TABLE t_user_praise_record ADD to_user_uid varchar(32) NULL COMMENT '被点赞/点踩的用户uid';


ALTER TABLE t_medal ADD medal_type tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '勋章类型【创作，活跃，成就，限定】';