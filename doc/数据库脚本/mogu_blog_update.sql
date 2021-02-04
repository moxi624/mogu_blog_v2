
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
   @date 2020年4月19日16:01:34
*/
ALTER TABLE t_system_config ADD content_picture_priority TINYINT(1) NOT NULL DEFAULT 0 COMMENT '博客详情图片显示优先级（ 0:本地  1: 七牛云 2: Minio）';