DROP TABLE IF EXISTS `t_network_disk`;

CREATE TABLE `t_network_disk` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `admin_uid` varchar(32) NOT NULL COMMENT '管理员uid',
  `extend_name` varchar(255) DEFAULT NULL COMMENT '扩展名',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小',
  `is_dir` int(11) NOT NULL COMMENT '是否目录',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `local_url` varchar(255) DEFAULT NULL COMMENT '本地文件URL',
  `qi_niu_url` varchar(255) DEFAULT NULL COMMENT '七牛云URL',
  `file_old_name` varchar(255) DEFAULT NULL COMMENT '上传前文件名',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='网盘文件表';


DROP TABLE IF EXISTS `t_storage`;

CREATE TABLE `t_storage` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `admin_uid` varchar(32) NOT NULL COMMENT '管理员uid',
  `storage_size` bigint(20) NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='存储信息表';







