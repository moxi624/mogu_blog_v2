/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 10.1.35-MariaDB : Database - mogu_blog
*********************************************************************
*/

USE `mogu_blog`;

DROP TABLE IF EXISTS `t_web_config`;

CREATE TABLE `t_web_config` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `logo` varchar(32) NOT NULL COMMENT 'logo(文件UID)',
  `name` varchar(255) NOT NULL COMMENT '网站名称',
  `summary` varchar(255) NOT NULL COMMENT '介绍',
  `keyword` varchar(255) NOT NULL COMMENT '关键字',
  `author` varchar(255) NOT NULL COMMENT '作者',
  `recordNum` varchar(255) NOT NULL COMMENT '备案号',
  `startComment` tinyint(1) DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

