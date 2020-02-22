
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
