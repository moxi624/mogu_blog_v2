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
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `pass_word` varchar(255) NOT NULL COMMENT '密码',
  `gender` varchar(1) DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(100) DEFAULT NULL COMMENT '个人头像',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `birthday` date DEFAULT NULL COMMENT '出生年月日',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机',
  `valid_code` varchar(50) DEFAULT NULL COMMENT '邮箱或手机验证码',
  `summary` varchar(200) DEFAULT NULL COMMENT '自我简介最多150字',
  `login_count` int(11) unsigned DEFAULT '0' COMMENT '登录次数',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT '127.0.0.1' COMMENT '最后登录IP',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `qq_number` varchar(255) DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) DEFAULT NULL COMMENT '微信号',
  `occupation` varchar(255) DEFAULT NULL COMMENT '职业',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

/*Data for the table `t_admin` */

insert  into `t_admin`(`uid`,`user_name`,`pass_word`,`gender`,`avatar`,`email`,`birthday`,`mobile`,`valid_code`,`summary`,`login_count`,`last_login_time`,`last_login_ip`,`status`,`create_time`,`update_time`,`nick_name`,`qq_number`,`we_chat`,`occupation`) values ('1f01cd1d2f474743b241d74008b12333','admin','$2a$10$kMixTzQXkqKWURKD.lzH3OfkVRz27b1w/9hM5Cn2yhOt0M1k7XQCq','1',',1720e3da8054434180032e3891b283dc','315606094@qq.com','2018-09-29','17679074120',NULL,'一个95后！一直潜心研究和学习Java后端技术，一边学习一边积累经验',0,'2018-10-02 19:03:45','127.0.0.1',1,'2018-09-04 19:05:05','2018-11-10 14:47:00','陌溪_','1595833114','moxi624','Java开发'),('1f01cd1d2f474743b241d74008b12334','张三','$2a$10$fzNZ9M/twQ16Hr.4EegYAenJfwNt0omOcYz9h8k8lX/vRMalDVTj.','0','','196430606@qq.com','2018-09-04','14798521755',NULL,NULL,0,'2018-09-07 19:04:53','127.0.0.1',1,'2018-09-04 19:05:12','2018-11-08 11:18:16',NULL,NULL,NULL,NULL),('1f01cd1d2f474743b241d74008b12335','赵六','$2a$10$xw37/WuRgBJaR06U6aynvOJtItlsxyKPo5hwaL1ZgQmXgtjD0EKla','1','','460929265@qq.com','2018-09-19','26151631845',NULL,NULL,0,'2018-09-07 19:06:03','127.0.0.1',1,'2018-08-07 19:06:11','2018-08-07 19:06:17',NULL,NULL,NULL,NULL),('434994947c5a4ee3a710cd277357c7c5','巴拉巴拉','$2a$10$TdvL8GnIMdddF7PDToTkU.y9NRPOGYSsIwhDu4aDjJR8q0hccYptu','2','','404033544@qq.com','2018-10-14','13803544785',NULL,'我是魔法师',0,NULL,'127.0.0.1',0,'2018-10-14 11:55:43','2018-10-14 11:55:43',NULL,NULL,NULL,NULL),('b1cf10413a60446cbf22cbc8c8f6d4dc','噜啦啦','$2a$10$xw37/WuRgBJaR06U6aynvOJtItlsxyKPo5hwaL1ZgQmXgtjD0EKla','1',NULL,'345678941@qq.com','2018-10-12','15078211234',NULL,NULL,0,'2018-10-12 19:45:42','string',1,'2018-10-12 19:51:37','2018-10-12 19:51:37',NULL,NULL,NULL,NULL);

/*Table structure for table `t_admin_role` */

DROP TABLE IF EXISTS `t_admin_role`;

CREATE TABLE `t_admin_role` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `admin_uid` varchar(32) NOT NULL COMMENT '管理员id',
  `role_uid` varchar(32) NOT NULL COMMENT '角色id',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_admin_role` */

insert  into `t_admin_role`(`uid`,`admin_uid`,`role_uid`,`status`,`create_time`,`update_time`) values ('bd3ffb1012074783bd3311b897d6bc96','1f01cd1d2f474743b241d74008b12333','434994947c5a4ee3a710cd277357c7c4',1,'2018-10-14 20:17:32','2018-10-14 20:17:32'),('eefd69c58c79490cbad06240b6f84952','1f01cd1d2f474743b241d74008b12333','434994947c5a4ee3a710cd277357c7c3',1,'2018-10-14 20:17:32','2018-10-14 20:17:32'),('eefd69c58c79490cbad06240b6f84953','1f01cd1d2f474743b241d74008b12334','434994947c5a4ee3a710cd277357c7c3',1,'0000-00-00 00:00:00','0000-00-00 00:00:00');

/*Table structure for table `t_blog` */

DROP TABLE IF EXISTS `t_blog`;

CREATE TABLE `t_blog` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `title` varchar(200) DEFAULT NULL COMMENT '博客标题',
  `summary` varchar(200) DEFAULT NULL COMMENT '博客简介',
  `content` longtext COMMENT '博客内容',
  `tag_uid` varchar(255) DEFAULT NULL COMMENT '标签uid',
  `click_count` int(11) DEFAULT '0' COMMENT '博客点击数',
  `collect_count` int(11) DEFAULT '0' COMMENT '博客收藏数',
  `file_uid` varchar(255) DEFAULT NULL COMMENT '标题图片uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `is_original` varchar(1) DEFAULT '1' COMMENT '是否原创（0:不是 1：是）',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `articles_part` varchar(255) DEFAULT NULL COMMENT '文章出处',
  `blog_sort_uid` varchar(32) DEFAULT NULL COMMENT '博客分类UID',
  `level` int(11) DEFAULT NULL COMMENT '等级',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博客表';

/*Data for the table `t_blog` */

insert  into `t_blog`(`uid`,`title`,`summary`,`content`,`tag_uid`,`click_count`,`collect_count`,`file_uid`,`status`,`create_time`,`update_time`,`admin_uid`,`is_original`,`author`,`articles_part`,`blog_sort_uid`,`level`) values ('1f01cd1d2f474743b241d74008bcdbea','测试1','测试简介1','<p>这是一篇测试博客1</p>\n','1f01cd1d2f474743b241d74008bcdbe2',26,1232,'1720e3da8054434180032e3891b283dc',1,'2018-11-09 12:39:57','2018-11-09 12:39:57','1f01cd1d2f474743b241d74008b12334','1','七七','蘑菇博客','1f01cd1d2f474743b241d74008bcd125',0),('1f01cd1d2f474743b241d74008bcdbec','测试博客','测试简介','<p>这是一篇测试博客</p>\n','1f01cd1d2f474743b241d74008bcdbe1',312312,1321312,'3de09539e8154cafaabe4d3876d04963',1,'2018-09-29 18:48:58','2018-09-29 18:48:58','1f01cd1d2f474743b241d74008b12333','1','哈哈','蘑菇博客','1f01cd1d2f474743b241d74008bcd124',0),('1f01cd1d2f474743b241d74008bcdbee','测试2','测试简介2','这是一篇测试博客2','1f01cd1d2f474743b241d74008bcdbe3',5416,640,'1f01cd1d2f474743b241d74008bcdb13',1,'0000-00-00 00:00:00','0000-00-00 00:00:00','1f01cd1d2f474743b241d74008b12335','1','巴巴','蘑菇博客','1f01cd1d2f474743b241d74008bcd126',NULL);

/*Table structure for table `t_blog_sort` */

DROP TABLE IF EXISTS `t_blog_sort`;

CREATE TABLE `t_blog_sort` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `sort_name` varchar(255) DEFAULT NULL COMMENT '分类内容',
  `content` varchar(255) DEFAULT NULL COMMENT '分类简介',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博客分类表';

/*Data for the table `t_blog_sort` */

insert  into `t_blog_sort`(`uid`,`sort_name`,`content`,`create_time`,`update_time`,`status`) values ('1f01cd1d2f474743b241d74008bcd124','慢生活','诗和远方','2018-09-29 18:59:09','2018-09-29 18:59:11',1),('1f01cd1d2f474743b241d74008bcd125','随想','哈哈吧看你','2018-09-29 18:59:54','2018-09-29 18:59:57',1),('1f01cd1d2f474743b241d74008bcd126','读者','散文','2018-09-19 19:00:34','2018-09-25 19:00:37',1);

/*Table structure for table `t_collect` */

DROP TABLE IF EXISTS `t_collect`;

CREATE TABLE `t_collect` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) NOT NULL COMMENT '用户的uid',
  `blog_uid` varchar(32) NOT NULL COMMENT '博客的uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收藏表';

/*Data for the table `t_collect` */

/*Table structure for table `t_comment` */

DROP TABLE IF EXISTS `t_comment`;

CREATE TABLE `t_comment` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '用户uid',
  `to_uid` varchar(32) DEFAULT NULL COMMENT '回复某条评论的uid',
  `to_user_uid` varchar(32) DEFAULT NULL COMMENT '回复某个人的uid',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `content` varchar(1000) DEFAULT NULL COMMENT '评论内容',
  `blog_uid` varchar(32) DEFAULT NULL COMMENT '博客uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';

/*Data for the table `t_comment` */

/*Table structure for table `t_feedback` */

DROP TABLE IF EXISTS `t_feedback`;

CREATE TABLE `t_feedback` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) NOT NULL COMMENT '用户uid',
  `content` varchar(1000) DEFAULT NULL COMMENT '反馈的内容',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='反馈表';

/*Data for the table `t_feedback` */

/*Table structure for table `t_link` */

DROP TABLE IF EXISTS `t_link`;

CREATE TABLE `t_link` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `title` varchar(255) DEFAULT NULL COMMENT '友情链接标题',
  `summary` varchar(255) DEFAULT NULL COMMENT '友情链接介绍',
  `url` varchar(255) DEFAULT NULL COMMENT '友情链接URL',
  `click_count` int(11) DEFAULT '0' COMMENT '点击数',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='友情链接表';

/*Data for the table `t_link` */

/*Table structure for table `t_permission` */

DROP TABLE IF EXISTS `t_permission`;

CREATE TABLE `t_permission` (
  `uid` varchar(32) NOT NULL COMMENT '权限id',
  `permission_name` varchar(255) NOT NULL COMMENT '权限名',
  `url` varchar(255) DEFAULT NULL COMMENT '权限url',
  `parent_id` varchar(255) NOT NULL COMMENT '父节点id',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_permission` */

insert  into `t_permission`(`uid`,`permission_name`,`url`,`parent_id`,`icon`,`create_time`,`update_time`,`status`) values ('1f01cd1d2f474743b241d74008b18881','a','www.baidu.com','0','图1','2018-10-15 10:31:31','2018-10-15 10:31:34',1),('1f01cd1d2f474743b241d74008b18882','b','www.baidu.com','0','图2','2018-10-15 10:32:24','2018-10-15 10:32:27',1),('1f01cd1d2f474743b241d74008b18883','c','www.baidu.com','0','图3','2018-10-15 10:32:57','2018-10-15 10:32:59',1),('1f01cd1d2f474743b241d74008b18884','d','www.google.com','1f01cd1d2f474743b241d74008b18881','图4','2018-10-15 10:33:49','2018-10-15 10:33:53',1),('1f01cd1d2f474743b241d74008b18885','e','www.bibili.com','1f01cd1d2f474743b241d74008b18882','图5','2018-10-15 10:34:38','2018-10-15 10:35:14',1),('1f01cd1d2f474743b241d74008b18886','f','www.bibili.com','1f01cd1d2f474743b241d74008b18883','图6','2018-10-15 10:34:59','2018-10-15 10:35:17',1),('1f01cd1d2f474743b241d74008b18887','g','www.google.com','1f01cd1d2f474743b241d74008b18881','图7','2018-10-15 10:36:36','2018-10-15 10:37:46',1),('1f01cd1d2f474743b241d74008b18888','h','www.qq.com','1f01cd1d2f474743b241d74008b18882','图8','2018-10-15 10:37:22','2018-10-15 10:37:50',1),('1f01cd1d2f474743b241d74008b18889','i','www.cloud.com','1f01cd1d2f474743b241d74008b18883','图9','2018-10-15 10:38:39','2018-10-15 10:38:41',1),('1f01cd1d2f474743b241d74008b18890','j','www.mogu.com','1f01cd1d2f474743b241d74008b18884','图10','2018-10-15 10:39:39','2018-10-15 10:39:44',1),('1f01cd1d2f474743b241d74008b18891','k','www.csdn.com','1f01cd1d2f474743b241d74008b18886','图11','2018-10-15 10:40:25','2018-10-15 10:40:27',1);

/*Table structure for table `t_picture` */

DROP TABLE IF EXISTS `t_picture`;

CREATE TABLE `t_picture` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `file_uid` varchar(32) DEFAULT NULL COMMENT '图片uid',
  `pic_name` varchar(255) DEFAULT NULL COMMENT '图片名',
  `picture_sort_uid` varchar(32) DEFAULT NULL COMMENT '分类uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片表';

/*Data for the table `t_picture` */

insert  into `t_picture`(`uid`,`file_uid`,`pic_name`,`picture_sort_uid`,`status`,`create_time`,`update_time`) values ('ade13958bb37482c8ada589d5f0d7fb0','3de09539e8154cafaabe4d3876d04963',NULL,'18628ad2216a478292fdce27e8707543',1,'2018-11-09 12:39:36','2018-11-09 12:39:36'),('ba535b5ef1e74d299ee77fb186d2d76c','1720e3da8054434180032e3891b283dc',NULL,'18628ad2216a478292fdce27e8707543',1,'2018-11-09 12:39:36','2018-11-09 12:39:36');

/*Table structure for table `t_picture_sort` */

DROP TABLE IF EXISTS `t_picture_sort`;

CREATE TABLE `t_picture_sort` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `file_uid` varchar(32) DEFAULT NULL COMMENT '分类图片uid',
  `name` varchar(255) DEFAULT NULL COMMENT '分类名',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `parent_uid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片分类表';

/*Data for the table `t_picture_sort` */

insert  into `t_picture_sort`(`uid`,`file_uid`,`name`,`status`,`create_time`,`update_time`,`parent_uid`) values ('18628ad2216a478292fdce27e8707543','null','cc',1,'2018-11-09 11:03:11','2018-11-09 11:03:11',NULL),('99f4a679b21649288ce6150ebe3a521d','null','cc',0,'2018-11-09 11:03:10','2018-11-09 11:03:10',NULL);

/*Table structure for table `t_resource_sort` */

DROP TABLE IF EXISTS `t_resource_sort`;

CREATE TABLE `t_resource_sort` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `file_uid` varchar(32) DEFAULT NULL COMMENT '分类图片uid',
  `sort_name` varchar(255) DEFAULT NULL COMMENT '分类名',
  `content` varchar(255) DEFAULT NULL COMMENT '分类介绍',
  `click_count` varchar(255) DEFAULT NULL COMMENT '点击数',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `parent_uid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源分类表';

/*Data for the table `t_resource_sort` */

insert  into `t_resource_sort`(`uid`,`file_uid`,`sort_name`,`content`,`click_count`,`status`,`create_time`,`update_time`,`parent_uid`) values ('232de7c82bac472aa90a122dd7e62ca0','4addade94eab49d9b3b13e120d593c82','JavaScript','JavaScript',NULL,1,'2018-10-21 10:26:35','2018-10-21 10:26:35',NULL),('2aa2b0f2402643c0a11bdf00b8fda94d','e06331bc9fff4a7186439f2a34c199af','Mysql','Mysql',NULL,1,'2018-10-21 10:18:34','2018-10-21 10:18:34',NULL),('2f05ae3ee71f40efab6fa419a12ca560','282f7a1ae2594c09b296daaa961313e3','Spring','Java学习资源',NULL,1,'2018-10-20 11:42:43','2018-10-20 11:42:43',NULL),('4cd13528dc634f26813548cf3bf0dcea','371c9ed84b5f42bfa329dbbf4bcff917','Java','java干货',NULL,1,'2018-10-20 11:46:22','2018-10-20 11:46:22',NULL),('53739b1ffa74485a93dd26dc4743ed2a','244a1dfd59a7417e8ca2bf9116416036','JQuery','JQuery',NULL,1,'2018-10-21 10:28:20','2018-10-21 10:28:20',NULL),('5d3d3e71ca564cfcb7353af37fa732b9','1a3dcc42f0064935b00c02cf5f41c50f','HTML+CSS','HTML+CSS',NULL,1,'2018-10-21 10:16:51','2018-10-21 10:16:51',NULL),('820ab06295154d4c8acf53a0e5d58e80','e70306aa4b2f47918a3d20643a242de5','VUE','VUE',NULL,1,'2018-10-21 10:27:14','2018-10-21 10:27:14',NULL);

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `uid` varchar(32) NOT NULL COMMENT '角色id',
  `role_name` varchar(255) NOT NULL COMMENT '角色名',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_role` */

insert  into `t_role`(`uid`,`role_name`,`create_time`,`update_time`,`status`) values ('434994947c5a4ee3a710cd277357c7c3','ROLE_Administrator','2018-10-14 15:56:26','2018-10-14 15:56:35',1),('434994947c5a4ee3a710cd277357c7c4','ROLE_Administrators','2018-10-14 15:56:21','2018-10-14 15:56:23',1);

/*Table structure for table `t_role_permission` */

DROP TABLE IF EXISTS `t_role_permission`;

CREATE TABLE `t_role_permission` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `role_uid` varchar(32) NOT NULL COMMENT '角色id',
  `permission_uid` varchar(32) NOT NULL COMMENT '权限id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_role_permission` */

insert  into `t_role_permission`(`uid`,`role_uid`,`permission_uid`,`create_time`,`update_time`,`status`) values ('04361faacc8e459ab3ff0e18caf48480','434994947c5a4ee3a710cd277357c7c1','1f01cd1d2f474743b241d74008b18890','2018-10-15 17:11:27','2018-10-15 17:11:27',1),('05297613eb8045f0b4f882dd1ed5492c','434994947c5a4ee3a710cd277357c7c1','1f01cd1d2f474743b241d74008b18881','2018-10-15 17:11:27','2018-10-15 17:11:27',1),('518db98b12da45cf85f3edc0f3e05bb9','434994947c5a4ee3a710cd277357c7c1','1f01cd1d2f474743b241d74008b18887','2018-10-15 17:11:27','2018-10-15 17:11:27',1),('7e87ce65de4a45d3989c79a4184b9850','434994947c5a4ee3a710cd277357c7c1','1f01cd1d2f474743b241d74008b18884','2018-10-15 17:11:27','2018-10-15 17:11:27',1),('8cef1d2e6d0c4216b4df68a266f4b2ac','434994947c5a4ee3a710cd277357c7c1','1f01cd1d2f474743b241d74008b18891','2018-10-15 17:11:27','2018-10-15 17:11:27',1);

/*Table structure for table `t_study_video` */

DROP TABLE IF EXISTS `t_study_video`;

CREATE TABLE `t_study_video` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `file_uid` varchar(32) DEFAULT NULL COMMENT '视频封面图片uid',
  `resource_sort_uid` varchar(255) DEFAULT NULL COMMENT '资源分类UID',
  `name` varchar(255) DEFAULT NULL COMMENT '视频名称',
  `summary` varchar(255) DEFAULT NULL COMMENT '视频简介',
  `content` varchar(255) DEFAULT NULL COMMENT '分类介绍',
  `baidu_path` varchar(255) DEFAULT NULL COMMENT '百度云完整路径',
  `click_count` varchar(255) DEFAULT NULL COMMENT '点击数',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `parent_uid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学习视频表';

/*Data for the table `t_study_video` */

insert  into `t_study_video`(`uid`,`file_uid`,`resource_sort_uid`,`name`,`summary`,`content`,`baidu_path`,`click_count`,`status`,`create_time`,`update_time`,`parent_uid`) values ('05a35691c38f4879a910912e6c248562','dde8800dd17c4b8c9de9169fbb0a49f9','2f05ae3ee71f40efab6fa419a12ca560','黑马全套','黑马全套学习视频','黑马全套学习视频','黑马全套','1',1,'2018-10-21 17:24:48','2018-10-21 17:24:48',NULL),('133e2a76080047508d026e42f03fce96','ee98646f1d684fa4b74f4bff1e1c6f2d','4cd13528dc634f26813548cf3bf0dcea','测试','测试','黑马全套学习视频','黑马全套','0',1,'2018-10-21 01:31:07','2018-10-21 01:31:07',NULL),('853585d787e0426d9196e0e1ad31a19c','95ebbd393dc9432a9d65ac0d7b35cd5c','4cd13528dc634f26813548cf3bf0dcea','哈哈哈','哈哈哈','黑马全套学习视频','黑马全套','0',1,'2018-10-20 21:43:56','2018-10-20 21:43:56',NULL),('eb9d821be06d435499d09cf114c8b50e','ebb8f56a177f4ed89c9020b43610fd6e','4cd13528dc634f26813548cf3bf0dcea','测试','测试','<p>黑马全套</p>\n','黑马全套','1',1,'2018-10-23 21:42:01','2018-10-23 21:42:01',NULL),('fd6c9f0ce6ca415cb56f62d186123808','44704768e62f48bb9a04ae1cac453dd8','820ab06295154d4c8acf53a0e5d58e80','测试','测试','黑马全套学习视频','黑马全套','0',1,'2018-10-21 21:48:37','2018-10-21 21:48:37',NULL);

/*Table structure for table `t_tag` */

DROP TABLE IF EXISTS `t_tag`;

CREATE TABLE `t_tag` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `content` varchar(1000) DEFAULT NULL COMMENT '标签内容',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `click_count` int(11) DEFAULT '0' COMMENT '标签简介',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签表';

/*Data for the table `t_tag` */

insert  into `t_tag`(`uid`,`content`,`status`,`click_count`,`create_time`,`update_time`) values ('1f01cd1d2f474743b241d74008bcdbe1','java',1,0,'2018-09-29 19:06:58','2018-09-29 19:07:00'),('1f01cd1d2f474743b241d74008bcdbe2','spring',1,0,'0000-00-00 00:00:00','0000-00-00 00:00:00'),('1f01cd1d2f474743b241d74008bcdbe3','vue',1,0,'0000-00-00 00:00:00','0000-00-00 00:00:00');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `pass_word` varchar(32) NOT NULL COMMENT '密码',
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
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
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
