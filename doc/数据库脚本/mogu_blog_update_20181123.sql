
CREATE TABLE `t_category_menu` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `summary` varchar(200) DEFAULT NULL COMMENT '简介',
  `parentUid` varchar(32) DEFAULT NULL COMMENT '父uid',
  `url` varchar(255) DEFAULT NULL COMMENT 'url地址',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort` int DEFAULT 0 COMMENT '排序字段，越大越靠前',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';