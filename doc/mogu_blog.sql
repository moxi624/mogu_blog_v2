/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 10.1.35-MariaDB : Database - mogu_blog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mogu_blog` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `mogu_blog`;

/*Table structure for table `t_admin` */

DROP TABLE IF EXISTS `t_admin`;

CREATE TABLE `t_admin` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `pass_word` varchar(36) NOT NULL COMMENT '密码',
  `gender` tinyint(1) unsigned DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(100) DEFAULT NULL COMMENT '个人头像',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `birthday` date DEFAULT NULL COMMENT '出生年月日',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机',
  `valid_code` varchar(50) DEFAULT NULL COMMENT '邮箱验证码',
  `summary` varchar(200) DEFAULT NULL COMMENT '自我简介最多150字',
  `login_count` int(11) unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

/*Data for the table `t_admin` */

insert  into `t_admin`(`uid`,`user_name`,`pass_word`,`gender`,`avatar`,`email`,`birthday`,`mobile`,`valid_code`,`summary`,`login_count`,`last_login_time`,`last_login_ip`,`status`,`create_time`,`update_time`) values ('5821462bc29a4570ad80e87f3aa3f02d','admin','21232f297a57a5a743894a0e4a801fc3',1,'测试头像','xzx19950624@qq.com','2018-09-20','1597531973','123465789','测试表情',1,'2018-09-20 14:49:00','127.0.0.1',1,'0000-00-00 00:00:00','0000-00-00 00:00:00');

/*Table structure for table `t_blog` */

DROP TABLE IF EXISTS `t_blog`;

CREATE TABLE `t_blog` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `title` varchar(200) DEFAULT NULL COMMENT '博客标题',
  `summary` varchar(200) DEFAULT NULL COMMENT '博客简介',
  `content` longtext COMMENT '博客内容',
  `tag_uid` varchar(36) DEFAULT NULL COMMENT '标签uid',
  `click_count` int(11) DEFAULT '0' COMMENT '博客点击数',
  `collect_count` int(11) DEFAULT '0' COMMENT '博客收藏数',
  `file_uid` varchar(36) DEFAULT NULL COMMENT '标题图片uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博客表';

/*Data for the table `t_blog` */

insert  into `t_blog`(`uid`,`title`,`summary`,`content`,`tag_uid`,`click_count`,`collect_count`,`file_uid`,`status`,`create_time`,`update_time`) values ('1caabfedccc44916aef97ea636470118','测试2','测试简介','测试内容',NULL,1,0,NULL,1,'2018-09-20 15:51:49','2018-09-20 15:51:49'),('5354ece1473f408cad1847fe2d55c9a1','测试3','测试3','测试3','a9a747d944c24845815356f72723ef8e',1,0,NULL,1,'2018-09-20 16:42:16','2018-09-20 16:42:16'),('5821462bc29a4570ad80e87f3aa3f02d','测试博客','测试简介','测试内容','a9a747d944c24845815356f72723ef8e',12,0,NULL,1,'2018-09-20 14:47:53','2018-09-20 14:47:53');

/*Table structure for table `t_collect` */

DROP TABLE IF EXISTS `t_collect`;

CREATE TABLE `t_collect` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(36) NOT NULL COMMENT '用户的uid',
  `blog_uid` varchar(36) NOT NULL COMMENT '博客的uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收藏表';

/*Data for the table `t_collect` */

/*Table structure for table `t_comment` */

DROP TABLE IF EXISTS `t_comment`;

CREATE TABLE `t_comment` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(36) DEFAULT NULL COMMENT '用户uid',
  `to_uid` varchar(36) DEFAULT NULL COMMENT '回复某条评论的uid',
  `to_user_uid` varchar(36) DEFAULT NULL COMMENT '回复某个人的uid',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `content` varchar(1000) DEFAULT NULL COMMENT '评论内容',
  `blog_uid` varchar(36) DEFAULT NULL COMMENT '博客uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';

/*Data for the table `t_comment` */

/*Table structure for table `t_feedback` */

DROP TABLE IF EXISTS `t_feedback`;

CREATE TABLE `t_feedback` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(36) NOT NULL COMMENT '用户uid',
  `content` varchar(1000) DEFAULT NULL COMMENT '反馈的内容',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='反馈表';

/*Data for the table `t_feedback` */

/*Table structure for table `t_picture` */

DROP TABLE IF EXISTS `t_picture`;

CREATE TABLE `t_picture` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `file_uid` varchar(36) DEFAULT NULL COMMENT '图片uid',
  `pic_name` varchar(255) DEFAULT NULL COMMENT '图片名',
  `picture_sort_uid` varchar(36) DEFAULT NULL COMMENT '分类uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片表';

/*Data for the table `t_picture` */

/*Table structure for table `t_picture_sort` */

DROP TABLE IF EXISTS `t_picture_sort`;

CREATE TABLE `t_picture_sort` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `file_uid` varchar(36) DEFAULT NULL COMMENT '分类图片uid',
  `name` varchar(255) DEFAULT NULL COMMENT '分类名',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `parent_uid` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片分类表';

/*Data for the table `t_picture_sort` */

insert  into `t_picture_sort`(`uid`,`file_uid`,`name`,`status`,`create_time`,`update_time`,`parent_uid`) values ('5a281aa35fa9408bb07c2fc9a990701f','发顺丰的','法式沙发',1,'2018-09-20 20:04:47','2018-09-20 20:04:47',NULL),('dd6728afaec34b64a12f560ca9931a7b','bbbb','测试',1,'2018-09-20 19:34:40','2018-09-20 19:34:40',NULL);

/*Table structure for table `t_tag` */

DROP TABLE IF EXISTS `t_tag`;

CREATE TABLE `t_tag` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `content` varchar(1000) DEFAULT NULL COMMENT '标签内容',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `click_count` int(11) DEFAULT '0' COMMENT '标签简介',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签表';

/*Data for the table `t_tag` */

insert  into `t_tag`(`uid`,`content`,`status`,`click_count`,`create_time`,`update_time`) values ('a9a747d944c24845815356f72723ef8e','测试标签',1,2,'2018-09-20 14:51:39','2018-09-20 14:51:39');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `pass_word` varchar(36) NOT NULL COMMENT '密码',
  `gender` tinyint(1) unsigned DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(100) DEFAULT NULL COMMENT '个人头像',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `birthday` date DEFAULT NULL COMMENT '出生年月日',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机',
  `valid_code` varchar(50) DEFAULT NULL COMMENT '邮箱验证码',
  `summary` varchar(200) DEFAULT NULL COMMENT '自我简介最多150字',
  `login_count` int(11) unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `t_user` */

/*Table structure for table `t_visitor` */

DROP TABLE IF EXISTS `t_visitor`;

CREATE TABLE `t_visitor` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `login_count` int(11) unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游客表';

/*Data for the table `t_visitor` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
