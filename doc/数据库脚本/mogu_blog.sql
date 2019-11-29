/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 10.1.39-MariaDB : Database - mogu_blog
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
  `valid_code` varchar(50) DEFAULT NULL COMMENT '邮箱验证码',
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
  `github` varchar(255) DEFAULT NULL COMMENT 'github地址',
  `gitee` varchar(255) DEFAULT NULL COMMENT 'gitee地址',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

/*Data for the table `t_admin` */

insert  into `t_admin`(`uid`,`user_name`,`pass_word`,`gender`,`avatar`,`email`,`birthday`,`mobile`,`valid_code`,`summary`,`login_count`,`last_login_time`,`last_login_ip`,`status`,`create_time`,`update_time`,`nick_name`,`qq_number`,`we_chat`,`occupation`,`github`,`gitee`) values ('1f01cd1d2f474743b241d74008b12333','admin','$2a$10$p11nLJkGyNuM43d/rlaIXeCm3SPtXb5y5SyC3DO8fEEUndyVHTtSK','1',',c1c09a7de5657168c2e843fb7f978368','','2018-09-29',' ',NULL,'一个95后！正在潜心研究机器学习和Java后端技术，一边学习一边积累经验',516,'2019-11-29 02:56:50','192.168.1.1',1,'2018-09-07 19:05:05','2018-11-13 14:47:00','陌溪_','1595833114','','Java开发','https://github.com/moxi624','https://gitee.com/moxi159753');

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

insert  into `t_admin_role`(`uid`,`admin_uid`,`role_uid`,`status`,`create_time`,`update_time`) values ('bd3ffb1012074783bd3311b897d6bc96','1f01cd1d2f474743b241d74008b12333','434994947c5a4ee3a710cd277357c7c3',1,'2018-10-16 04:17:32','2018-10-16 04:17:32');

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
  `level` tinyint(1) DEFAULT '0' COMMENT '推荐等级(0:正常)',
  `is_publish` varchar(1) DEFAULT '1' COMMENT '是否发布：0：否，1：是',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博客表';

/*Data for the table `t_blog` */

insert  into `t_blog`(`uid`,`title`,`summary`,`content`,`tag_uid`,`click_count`,`collect_count`,`file_uid`,`status`,`create_time`,`update_time`,`admin_uid`,`is_original`,`author`,`articles_part`,`blog_sort_uid`,`level`,`is_publish`) values ('0d542253b314fdb48d0192d066f0d8f2','测试博客6','测试博客6','<p>测试博客6</p>\n','5c939107ddb746b989156737805df625',0,0,'6befb0180d151cbe1a13c84bd824cd72',1,'2019-11-27 04:07:35','2019-11-27 04:07:35','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','337806254f9c42999043de5c5ee09e77',2,'1'),('15cd0d4e2a294a9a1818d498abe90acb','测试博客7','测试博客7','<p>测试博客7</p>\n','e2c7913050cf4ab9aa92902316aaf075',0,0,'c1c09a7de5657168c2e843fb7f978368',1,'2019-11-27 04:07:59','2019-11-27 04:07:59','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','2c93dfab0e754006866f8ed486923a41',2,'1'),('3f96b3ace5126ae542149ae76c985ddb','测试博客4','测试博客4','<p>测试博客4</p>\n','1d1fd6d26c8e40a38637ef6126c45cd0,e2c7913050cf4ab9aa92902316aaf075',0,0,'25ad1b97042e5df69bf7d40984bd28cf',1,'2019-11-27 04:06:57','2019-11-27 04:06:57','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','db0d64ea7df409de5d2d747927cfa1a5',3,'1'),('3faef70021f12d7eba6b80041d9ec0bd','测试博客8','测试博客8','<p>测试博客8</p>\n','5c939107ddb746b989156737805df625',1,1,'c1c09a7de5657168c2e843fb7f978368',1,'2019-11-27 04:08:12','2019-11-27 04:08:12','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','db0d64ea7df409de5d2d747927cfa1a5',1,'1'),('5ea7df524d104a37d9b34f424b8ebe00','测试博客2','测试博客2','<p>测试博客2</p>\n','a9a747d944c24845815356f72723ef8e',0,0,'d4304482662b385498ee7042b2666cbf',1,'2019-11-27 04:02:58','2019-11-27 04:02:58',NULL,'0','张三','www.baidu.com','a03d7290b1c04b6eaf46659661b47032',4,'1'),('6485f4aad562a4b1bade3cfe586c1aed','测试博客3','测试博客3','<p>测试博客3</p>\n','1d1fd6d26c8e40a38637ef6126c45cd0',0,0,'25ad1b97042e5df69bf7d40984bd28cf',1,'2019-11-27 04:06:40','2019-11-27 04:06:40','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','a03d7290b1c04b6eaf46659661b47032',3,'1'),('a130e79e2414df586bce0fd41eb34ba1','测试博客1','测试博客1','<p>测试博客1</p>\n','ca928e8718654aa5a802e2f69277b137',0,0,'d4304482662b385498ee7042b2666cbf',1,'2019-11-27 03:19:10','2019-11-27 03:19:10','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','a03d7290b1c04b6eaf46659661b47032',4,'1'),('f760df4f9d026560e18df7f5cb7935de','测试博客5','测试博客5','<p>测试博客5</p>\n','ca928e8718654aa5a802e2f69277b137',0,0,'6befb0180d151cbe1a13c84bd824cd72',1,'2019-11-27 04:07:20','2019-11-27 04:07:20','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','ca28ffc94ea94fbda5571e0b242021e2',0,'1');

/*Table structure for table `t_blog_sort` */

DROP TABLE IF EXISTS `t_blog_sort`;

CREATE TABLE `t_blog_sort` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `sort_name` varchar(255) DEFAULT NULL COMMENT '分类内容',
  `content` varchar(255) DEFAULT NULL COMMENT '分类简介',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `click_count` int(11) DEFAULT '0' COMMENT '点击数',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博客分类表';

/*Data for the table `t_blog_sort` */

insert  into `t_blog_sort`(`uid`,`sort_name`,`content`,`create_time`,`update_time`,`status`,`sort`,`click_count`) values ('029d80ba36a04c96a89a80e2705031a2','测试分类','测试分类','2019-01-10 21:10:43','2019-01-10 21:10:43',0,0,0),('093d8bdd01c84890a928e923d5c235fe','软件推荐','软件推荐','2018-09-24 17:14:59','2018-09-24 17:14:59',1,6,0),('2c93dfab0e754006866f8ed486923a41','慢生活','慢生活，不是懒惰，放慢速度不是拖延时间，而是让我们在生活中寻找到平衡','2018-09-24 16:29:33','2018-09-24 16:29:33',1,30,5),('337806254f9c42999043de5c5ee09e77','技术新闻','发现世界的每一天','2018-12-29 10:42:11','2018-12-29 10:42:11',1,30,0),('9d2019983d91490aaa758eddd7c07caf','机器学习','机器学习','2018-11-21 20:56:02','2018-11-21 20:56:02',1,16,1),('a03d7290b1c04b6eaf46659661b47032','后端开发','后端开发专题','2018-12-29 10:35:43','2018-12-29 10:35:43',1,48,1),('ca28ffc94ea94fbda5571e0b242021e2','前端开发','前端开发专题','2018-12-29 10:35:58','2018-12-29 10:35:58',1,10,0),('db0d64ea7df409de5d2d747927cfa1a5','学习笔记','学习笔记','2019-08-30 10:50:03','2019-08-30 10:50:03',1,7,0),('e60df954efcd47c48463a504bb70bbe9','面试','面试专题','2018-12-19 21:16:30','2018-12-19 21:16:30',1,4,0);

/*Table structure for table `t_category_menu` */

DROP TABLE IF EXISTS `t_category_menu`;

CREATE TABLE `t_category_menu` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `name` varchar(255) NOT NULL COMMENT '菜单名称',
  `menu_level` tinyint(1) DEFAULT NULL COMMENT '菜单级别',
  `summary` varchar(200) DEFAULT NULL COMMENT '简介',
  `parent_uid` varchar(32) DEFAULT NULL COMMENT '父uid',
  `url` varchar(255) DEFAULT NULL COMMENT 'url地址',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

/*Data for the table `t_category_menu` */

insert  into `t_category_menu`(`uid`,`name`,`menu_level`,`summary`,`parent_uid`,`url`,`icon`,`sort`,`status`,`create_time`,`update_time`) values ('02ea2f9ef5d44f559fb66189b05f6769','索引管理',2,'索引管理','badf0010422b432ba6ec9c83a25012ed','/system/solrIndex','index',0,1,'2018-11-28 19:55:39','2018-11-28 19:55:39'),('0a035547bbec404eb3ee0ef43312148d','分类管理',2,'管理博客分类','49b42250abcb47ff876bad699cf34f03','/blog/blogSort','sort',10,1,'2018-11-26 03:07:14','2018-11-26 03:07:14'),('1f01cd1d2f474743b241d74008b12333','博客管理',2,'对博客进行增删改查','49b42250abcb47ff876bad699cf34f03','/blog/blog','edit',11,1,'2018-11-27 03:47:07','2018-11-27 03:47:07'),('25b772444f7548f8a3957e00e5eb4246','权限管理',2,'对权限进行管理','d3a19221259d439b916f475e43edb13d','/authority/authority','adminRole',0,1,'2018-11-25 19:11:22','2018-11-25 19:11:22'),('2de247af3b0a459095e937d7ab9f5864','管理员管理',2,'管理员增删改查','d3a19221259d439b916f475e43edb13d','/authority/admin','user',0,1,'2018-11-25 19:09:21','2018-11-25 19:09:21'),('2fb47d3b6dbd44279c8206740a263543','网站配置',2,'网站配置','badf0010422b432ba6ec9c83a25012ed','/system/webConfig','web',0,1,'2018-11-28 19:59:04','2018-11-28 19:59:04'),('4337f63d13d84b9aba64b9d7a69fd066','异常日志',2,'异常日志','98b82be8785e41dc939b6a5517fdfa53','/log/exceptionLog','exception',0,1,'2018-11-28 20:01:36','2018-11-28 20:01:36'),('49b42250abcb47ff876bad699cf34f03','博客管理',1,'用于博客的一些相关操作',NULL,'/blog','edit',14,1,'2018-11-25 05:15:07','2018-11-25 05:15:07'),('4dea9c4f39d2480983e8c4333d35e036','图片类别管理',2,'图片类别管理','65e22f3d36d94bcea47478aba02895a1','/picture/pictureSort','picture',0,1,'2018-11-28 19:50:31','2018-11-28 19:50:31'),('5010ae46511e4c0b9f30d1c63ad3f0c1','角色管理',2,'管理用户角色信息','d3a19221259d439b916f475e43edb13d','/authority/role','peoples',0,1,'2018-11-25 19:10:34','2018-11-25 19:10:34'),('510483ce569b4fc88299f346147b1314','资源管理',1,'资源管理','','/resource','resource',2,1,'2018-11-28 19:42:13','2018-11-28 19:42:13'),('6228ff4e9ebd42c89599b322201a0345','反馈管理',2,'反馈管理','bcf4a9bc21c14b559bcb015fb7912266','/message/feedback','table',0,1,'2018-11-28 19:48:30','2018-11-28 19:48:30'),('65e22f3d36d94bcea47478aba02895a1','图片管理',1,'图片管理','','/picture','example',3,1,'2018-11-28 19:48:53','2018-11-28 19:48:53'),('6606b7e646d545e5a25c70b5e5fade9f','标签管理',2,'对博客标签进行管理','49b42250abcb47ff876bad699cf34f03','/blog/blogTag','tag',4,1,'2018-11-26 02:57:38','2018-11-26 02:57:38'),('78ab104b123f4950af14d65798afb756','收藏管理',2,'管理用户收藏','49b42250abcb47ff876bad699cf34f03','/blog/collect','example',8,1,'2018-11-25 19:07:48','2018-11-25 19:07:48'),('9002d1ae905c4cb79c2a485333dad2f7','友情链接',2,'友情链接','badf0010422b432ba6ec9c83a25012ed','/system/blogLink','blogLink',0,1,'2018-11-29 03:56:35','2018-11-29 03:56:35'),('9449ce5dd5e24b21a9d15f806cb36e87','分类管理',2,'分类管理','510483ce569b4fc88299f346147b1314','/resource/resourceSort','sort',0,1,'2018-11-29 03:43:27','2018-11-29 03:43:27'),('98b82be8785e41dc939b6a5517fdfa53','操作日志',1,'操作日志','','/log','log',9,1,'2018-11-28 20:00:19','2018-11-28 20:00:19'),('9beb7caa2c844b36a02789262dc76fbe','评论管理',2,'评论管理','bcf4a9bc21c14b559bcb015fb7912266','/message/comment','table',1,1,'2018-11-28 19:47:23','2018-11-28 19:47:23'),('9e91b4f993c946cba4bf720b2c1b2e90','用户日志',2,'用户Web端访问情况','98b82be8785e41dc939b6a5517fdfa53','/log/webVisit','user1',0,1,'2019-05-17 10:16:47','2019-05-17 10:16:47'),('a5902692a3ed4fd794895bf634f97b8e','操作日志',2,'操作日志','98b82be8785e41dc939b6a5517fdfa53','/log/log','log',0,1,'2018-11-28 20:01:02','2018-11-28 20:01:02'),('aa225cdae6464bc0acebd732192f8362','菜单管理',2,'对页面菜单进行管理','d3a19221259d439b916f475e43edb13d','/authority/categoryMenu','example',0,1,'2018-11-25 11:12:01','2018-11-25 11:12:01'),('b511cae571834971a392ae4779270034','游客管理',2,'游客管理','c519725da92b42f3acf0cc9fad58c664','/user/visitor','table',0,1,'2018-11-28 19:54:28','2018-11-28 19:54:28'),('baace3dc03d34c54b81761dce8243814','接口管理',1,'接口管理','','/restapi','restapi',4,1,'2018-11-28 20:01:57','2018-11-28 20:01:57'),('badf0010422b432ba6ec9c83a25012ed','系统管理',1,'系统管理','','/system','system',11,1,'2018-11-28 19:54:47','2018-11-28 19:54:47'),('bcf4a9bc21c14b559bcb015fb7912266','消息管理',1,'消息管理','','/message','message1',6,1,'2018-11-28 19:45:29','2018-11-28 19:45:29'),('c519725da92b42f3acf0cc9fad58c664','用户管理',1,'用户管理','','/user','user1',13,1,'2018-11-28 19:51:47','2018-11-28 19:51:47'),('d3a19221259d439b916f475e43edb13d','权限管理',1,'对管理员权限分配进行管理','','/authority','authority',5,1,'2018-11-25 19:08:42','2018-11-25 19:08:42'),('d4d92c53d3614d00865e9219b8292a90','Picture接口',2,'Picture接口','baace3dc03d34c54b81761dce8243814','/restapi/pictureRestApi','table',0,1,'2018-11-28 20:04:33','2018-11-28 20:04:33'),('e4a482c089d04a30b6ecbaadb81b70f8','Admin接口',2,'Admin接口','baace3dc03d34c54b81761dce8243814','/restapi/adminRestApi','table',0,1,'2018-11-28 20:03:32','2018-11-28 20:03:32'),('f9276eb8e3274c8aa05577c86e4dc8c1','Web接口',2,'Web接口','baace3dc03d34c54b81761dce8243814','/restapi/webRestApi','table',0,1,'2018-11-28 20:04:52','2018-11-28 20:04:52'),('faccfe476b89483791c05019ad5b4906','关于我',2,'关于我','badf0010422b432ba6ec9c83a25012ed','/system/aboutMe','aboutMe',0,1,'2018-11-29 03:55:17','2018-11-29 03:55:17'),('fb4237a353d0418ab42c748b7c1d64c6','用户管理',2,'用户管理','c519725da92b42f3acf0cc9fad58c664','/user/user','table',1,1,'2018-11-28 19:52:20','2018-11-28 19:52:20'),('ffc6e9ca2cc243febf6d2f476b849163','视频管理',2,'视频管理','510483ce569b4fc88299f346147b1314','/resource/studyVideo','table',0,1,'2018-11-28 19:43:50','2018-11-28 19:43:50');

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

insert  into `t_comment`(`uid`,`user_uid`,`to_uid`,`to_user_uid`,`user_name`,`content`,`blog_uid`,`status`,`create_time`,`update_time`) values ('0500ba8e97b34528ae18c18616cf93e7','093d8bdd01c84890a928e923d5c235fe',NULL,NULL,'你好','编辑测试博客','093d8bdd01c84890a928e923d5c235fe',1,'2018-10-13 16:35:30','2018-10-13 16:35:30');

/*Table structure for table `t_exception_log` */

DROP TABLE IF EXISTS `t_exception_log`;

CREATE TABLE `t_exception_log` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `exception_json` mediumtext COMMENT '异常对象json格式',
  `exception_message` mediumtext COMMENT '异常信息',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_exception_log` */

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
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='友情链接表';

/*Data for the table `t_link` */

insert  into `t_link`(`uid`,`title`,`summary`,`url`,`click_count`,`create_time`,`update_time`,`status`,`sort`) values ('5217d7212f9d487eab13deadca961402','蘑菇博客Git','蘑菇博客Git','https://gitee.com/moxi159753/mogu_blog_v2',17,'2018-12-16 18:25:11','2018-12-16 18:25:11',1,0),('bb418e0a9d27490bb71972bd8da5afa6','IT大本营','IT大本营 - 专注于技术分享的开发者社区','http://www.itarea.cn/',2,'2018-09-26 13:55:33','2018-09-26 13:55:33',0,0),('dcc01149be71492dabd55821c22f6061','Mybatis-plus','MyBatis-Plus 为简化开发而生','http://mp.baomidou.com/',4,'2018-09-26 18:52:58','2018-09-26 18:52:58',1,0);

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

insert  into `t_picture`(`uid`,`file_uid`,`pic_name`,`picture_sort_uid`,`status`,`create_time`,`update_time`) values ('07c07c6ae56f8f94639048dd7ebf12d0','6befb0180d151cbe1a13c84bd824cd72',NULL,'5e5428e876cbbc0bb30c241d3959531a',1,'2019-11-27 03:11:43','2019-11-27 03:11:43'),('8f66f438df4196b470a71858127ef855','25ad1b97042e5df69bf7d40984bd28cf',NULL,'5e5428e876cbbc0bb30c241d3959531a',1,'2019-11-27 03:11:43','2019-11-27 03:11:43'),('8fd094a6136aac44a3032ee0ac545dfb','d4304482662b385498ee7042b2666cbf',NULL,'5e5428e876cbbc0bb30c241d3959531a',1,'2019-11-27 03:11:43','2019-11-27 03:11:43'),('d98cb9d2c03dcf629e5e243865dea79f','c1c09a7de5657168c2e843fb7f978368',NULL,'5e5428e876cbbc0bb30c241d3959531a',1,'2019-11-27 03:11:43','2019-11-27 03:11:43');

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
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图片分类表';

/*Data for the table `t_picture_sort` */

insert  into `t_picture_sort`(`uid`,`file_uid`,`name`,`status`,`create_time`,`update_time`,`parent_uid`,`sort`) values ('5e5428e876cbbc0bb30c241d3959531a','6befb0180d151cbe1a13c84bd824cd72','JavaScript',1,'2019-11-27 03:09:42','2019-11-27 03:09:42',NULL,0),('fe1237a484f44c87fa04630b01dba316','d4304482662b385498ee7042b2666cbf','Java',1,'2019-11-27 03:09:26','2019-11-27 03:09:26',NULL,0);

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
  `parent_uid` varchar(32) DEFAULT NULL COMMENT '父UID',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源分类表';

/*Data for the table `t_resource_sort` */

insert  into `t_resource_sort`(`uid`,`file_uid`,`sort_name`,`content`,`click_count`,`status`,`create_time`,`update_time`,`parent_uid`,`sort`) values ('232de7c82bac472aa90a122dd7e62ca0','4addade94eab49d9b3b13e120d593c82','JavaScript','JavaScript',NULL,1,'2018-10-21 10:26:35','2018-10-21 10:26:35',NULL,0),('2aa2b0f2402643c0a11bdf00b8fda94d','e06331bc9fff4a7186439f2a34c199af','Mysql','Mysql',NULL,1,'2018-10-21 10:18:34','2018-10-21 10:18:34',NULL,0),('2f05ae3ee71f40efab6fa419a12ca560','282f7a1ae2594c09b296daaa961313e3','Spring','Java学习资源',NULL,1,'2018-10-20 11:42:43','2018-10-20 11:42:43',NULL,0),('4cd13528dc634f26813548cf3bf0dcea','371c9ed84b5f42bfa329dbbf4bcff917','Java','java干货',NULL,1,'2018-10-20 11:46:22','2018-10-20 11:46:22',NULL,0),('53739b1ffa74485a93dd26dc4743ed2a','244a1dfd59a7417e8ca2bf9116416036','JQuery','JQuery',NULL,1,'2018-10-21 10:28:20','2018-10-21 10:28:20',NULL,0),('5d3d3e71ca564cfcb7353af37fa732b9','1a3dcc42f0064935b00c02cf5f41c50f','HTML+CSS','HTML+CSS',NULL,1,'2018-10-21 10:16:51','2018-10-21 10:16:51',NULL,0),('820ab06295154d4c8acf53a0e5d58e80','e70306aa4b2f47918a3d20643a242de5','VUE','VUE',NULL,1,'2018-10-21 10:27:14','2018-10-21 10:27:14',NULL,0);

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `uid` varchar(32) NOT NULL COMMENT '角色id',
  `role_name` varchar(255) NOT NULL COMMENT '角色名',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `summary` varchar(255) DEFAULT NULL COMMENT '角色介绍',
  `category_menu_uids` text COMMENT '角色管辖的菜单的UID',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_role` */

insert  into `t_role`(`uid`,`role_name`,`create_time`,`update_time`,`status`,`summary`,`category_menu_uids`) values ('434994947c5a4ee3a710cd277357c7c3','超级管理员','2018-10-15 23:56:26','2018-10-15 23:56:35',1,'超级管理员，管理全部菜单和功能','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"c519725da92b42f3acf0cc9fad58c664\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"b511cae571834971a392ae4779270034\",\"badf0010422b432ba6ec9c83a25012ed\",\"02ea2f9ef5d44f559fb66189b05f6769\",\"2fb47d3b6dbd44279c8206740a263543\",\"9002d1ae905c4cb79c2a485333dad2f7\",\"faccfe476b89483791c05019ad5b4906\",\"98b82be8785e41dc939b6a5517fdfa53\",\"4337f63d13d84b9aba64b9d7a69fd066\",\"9e91b4f993c946cba4bf720b2c1b2e90\",\"a5902692a3ed4fd794895bf634f97b8e\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"d3a19221259d439b916f475e43edb13d\",\"25b772444f7548f8a3957e00e5eb4246\",\"2de247af3b0a459095e937d7ab9f5864\",\"5010ae46511e4c0b9f30d1c63ad3f0c1\",\"aa225cdae6464bc0acebd732192f8362\",\"baace3dc03d34c54b81761dce8243814\",\"d4d92c53d3614d00865e9219b8292a90\",\"e4a482c089d04a30b6ecbaadb81b70f8\",\"f9276eb8e3274c8aa05577c86e4dc8c1\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\",\"510483ce569b4fc88299f346147b1314\",\"9449ce5dd5e24b21a9d15f806cb36e87\",\"ffc6e9ca2cc243febf6d2f476b849163\"]'),('434994947c5a4ee3a710cd277357c7c4','文章管理员','2018-10-14 23:56:21','2018-10-14 23:56:23',1,'管理文章','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\"]'),('d105da79260f4d6a8a03571e4a2b17bc','一般管理员','2019-05-28 16:43:26','2019-05-28 16:43:26',1,'一般管理员','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"c519725da92b42f3acf0cc9fad58c664\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"b511cae571834971a392ae4779270034\",\"badf0010422b432ba6ec9c83a25012ed\",\"02ea2f9ef5d44f559fb66189b05f6769\",\"2fb47d3b6dbd44279c8206740a263543\",\"9002d1ae905c4cb79c2a485333dad2f7\",\"faccfe476b89483791c05019ad5b4906\",\"98b82be8785e41dc939b6a5517fdfa53\",\"4337f63d13d84b9aba64b9d7a69fd066\",\"9e91b4f993c946cba4bf720b2c1b2e90\",\"a5902692a3ed4fd794895bf634f97b8e\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"baace3dc03d34c54b81761dce8243814\",\"d4d92c53d3614d00865e9219b8292a90\",\"e4a482c089d04a30b6ecbaadb81b70f8\",\"f9276eb8e3274c8aa05577c86e4dc8c1\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\"]');

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

insert  into `t_study_video`(`uid`,`file_uid`,`resource_sort_uid`,`name`,`summary`,`content`,`baidu_path`,`click_count`,`status`,`create_time`,`update_time`,`parent_uid`) values ('05a35691c38f4879a910912e6c248562','dde8800dd17c4b8c9de9169fbb0a49f9','2f05ae3ee71f40efab6fa419a12ca560','学习学习','学习学习','<p>黑马全套学习视频</p>\n','黑马全套','1',1,'2018-10-22 01:24:48','2018-10-22 01:24:48',NULL),('133e2a76080047508d026e42f03fce96','ee98646f1d684fa4b74f4bff1e1c6f2d','4cd13528dc634f26813548cf3bf0dcea','测试','测试','黑马全套学习视频','黑马全套','0',1,'2018-10-21 01:31:07','2018-10-21 01:31:07',NULL),('853585d787e0426d9196e0e1ad31a19c','95ebbd393dc9432a9d65ac0d7b35cd5c','4cd13528dc634f26813548cf3bf0dcea','哈哈哈','哈哈哈','黑马全套学习视频','黑马全套','0',1,'2018-10-20 21:43:56','2018-10-20 21:43:56',NULL),('eb9d821be06d435499d09cf114c8b50e','ebb8f56a177f4ed89c9020b43610fd6e','4cd13528dc634f26813548cf3bf0dcea','测试','测试','<p>黑马全套</p>\n','黑马全套','1',1,'2018-10-23 21:42:01','2018-10-23 21:42:01',NULL),('fd6c9f0ce6ca415cb56f62d186123808','44704768e62f48bb9a04ae1cac453dd8','820ab06295154d4c8acf53a0e5d58e80','测试','测试','黑马全套学习视频','黑马全套','0',1,'2018-10-21 21:48:37','2018-10-21 21:48:37',NULL);

/*Table structure for table `t_sys_log` */

DROP TABLE IF EXISTS `t_sys_log`;

CREATE TABLE `t_sys_log` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `ip` varchar(50) DEFAULT NULL COMMENT '请求ip地址',
  `url` varchar(255) DEFAULT NULL COMMENT '请求url',
  `type` varchar(32) DEFAULT NULL COMMENT '请求方式',
  `class_path` varchar(255) DEFAULT NULL COMMENT '请求类路径',
  `method` varchar(32) DEFAULT NULL COMMENT '请求方法名',
  `params` text COMMENT '请求参数',
  `operation` varchar(32) DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_sys_log` */

/*Table structure for table `t_tag` */

DROP TABLE IF EXISTS `t_tag`;

CREATE TABLE `t_tag` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `content` varchar(1000) DEFAULT NULL COMMENT '标签内容',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `click_count` int(11) DEFAULT '0' COMMENT '标签简介',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `sort` int(11) DEFAULT '0' COMMENT '排序字段，越大越靠前',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签表';

/*Data for the table `t_tag` */

insert  into `t_tag`(`uid`,`content`,`status`,`click_count`,`create_time`,`update_time`,`sort`) values ('0b51c75ed5744cdcadefe0ad947be9b6','数据库',1,54,'2019-11-16 16:40:32','2019-11-16 16:40:32',6),('1c76b9848f5f4d71a5e88b20dbaf38f4','RabbitMQ',1,15,'2019-11-16 16:40:32','2019-11-16 16:40:32',6),('1d1fd6d26c8e40a38637ef6126c45cd0','Linux',1,45,'2019-11-16 16:40:32','2019-11-16 16:40:32',23),('2a31dd6c2b1b464e9e222a1198bc739a','虚拟机',1,1,'2019-11-16 16:40:32','2019-11-16 16:40:32',2),('2f5779e877da48958c985d69b311d0d6','大数据',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',5),('3c16b9093e9b1bfddbdfcb599b23d835','Nginx',1,312,'2019-11-16 16:40:32','2019-11-16 16:40:32',1),('53c5a0f3142e4f54820315936f78383b','Spring Boot',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',3),('5626932d452c2ad863d9b3cb0b69d22d','学习笔记',1,33,'2019-11-16 16:40:32','2019-11-16 16:40:32',9),('5c939107ddb746b989156737805df625','机器学习',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',22),('6d35ddd5075f4c0e885ffb2e3b3a0365','Tomcat',1,454,'2019-11-16 16:40:32','2019-11-16 16:40:32',3),('7e0e93ea6cdb44ae92e58f48e6496ed7','Java',1,1,'2019-11-16 16:40:32','2019-11-16 16:40:32',30),('8c9d43de144245eb8176854eca5ae244','AI',1,44,'2019-11-16 16:40:32','2019-11-16 16:40:32',0),('8d5ce3e0c0784b95adb7f9e7b76dca93','建站系统',1,3213,'2019-11-16 16:40:32','2019-11-16 16:40:32',4),('a9a747d944c24845815356f72723ef8e','前端开发',1,20,'2019-11-16 16:40:32','2019-11-16 16:40:32',10),('ca928e8718654aa5a802e2f69277b137','面试',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',8),('d3c3fc43f38445389c970ff0732a6586','NLP',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',4),('dececd440fdc4fa28dffe6404e696dd4','Python',1,45,'2019-11-16 16:40:32','2019-11-16 16:40:32',1),('e2c7913050cf4ab9aa92902316aaf075','校园生活',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',13),('e81bc2dca42c4031be7d66fef4a71e16','Spring Cloud',1,10,'2019-11-16 16:40:32','2019-11-16 16:40:32',4),('ebf63989f11741bc89494c52fc6bae4c','Docker',1,454,'2019-11-16 16:40:32','2019-11-16 16:40:32',2),('f5d458db6a044eaebc22232efa1e3b54','深度学习',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',7),('f90d3c2fd9434302951130e897a89164','Vue',1,15,'2019-11-16 16:40:32','2019-11-16 16:40:32',6),('fb72516226474cf0bfa0f310bfa75426','Scala',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',1);

/*Table structure for table `t_todo` */

DROP TABLE IF EXISTS `t_todo`;

CREATE TABLE `t_todo` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `admin_uid` varchar(32) DEFAULT NULL COMMENT '管理员uid',
  `text` varchar(255) DEFAULT NULL COMMENT '内容',
  `done` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '表示事项是否完成（0：未完成 1：已完成）',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代办事项表';

/*Data for the table `t_todo` */

insert  into `t_todo`(`uid`,`admin_uid`,`text`,`done`,`status`,`create_time`,`update_time`) values ('3d12f6707ef54f84f72f7b67561f39c9','1f01cd1d2f474743b241d74008b12333','测试',0,1,'2019-11-27 03:01:58','2019-11-27 03:01:58');

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

/*Table structure for table `t_web_config` */

DROP TABLE IF EXISTS `t_web_config`;

CREATE TABLE `t_web_config` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `logo` varchar(255) NOT NULL COMMENT 'logo(文件UID)',
  `name` varchar(255) NOT NULL COMMENT '网站名称',
  `summary` varchar(255) NOT NULL COMMENT '介绍',
  `keyword` varchar(255) NOT NULL COMMENT '关键字',
  `author` varchar(255) NOT NULL COMMENT '作者',
  `record_num` varchar(255) NOT NULL COMMENT '备案号',
  `start_comment` varchar(1) DEFAULT '1' COMMENT '是否开启评论(0:否 1:是)',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `ali_pay` varchar(32) DEFAULT NULL COMMENT '支付宝收款码FileId',
  `weixin_pay` varchar(32) DEFAULT NULL COMMENT '微信收款码FileId',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_web_config` */

insert  into `t_web_config`(`uid`,`logo`,`name`,`summary`,`keyword`,`author`,`record_num`,`start_comment`,`status`,`create_time`,`update_time`,`title`,`ali_pay`,`weixin_pay`) values ('a331e4933cf54afcbb8c0cb11ec0830e',',d4304482662b385498ee7042b2666cbf','蘑菇博客','一个专注于技术分享的博客平台，大家以共同学习，乐于分享，拥抱开源的价值观进行学习交流','蘑菇博客,蘑菇社区,蘑菇技术社区,,蘑菇IT社区,IT社区,技术社区,Java技术分享,Spring教程,开发者社区','许志翔','赣ICP备18014504号','0',1,'2018-11-17 08:15:27','2018-11-17 08:15:27','一个专注于技术分享的博客平台','5ce92301acca4cc5858cdb03f7389767','fdc6c11c61ce4785811dc9ee7a6ad705');

/*Table structure for table `t_web_visit` */

DROP TABLE IF EXISTS `t_web_visit`;

CREATE TABLE `t_web_visit` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `user_uid` varchar(255) DEFAULT NULL COMMENT '用户uid',
  `ip` varchar(255) DEFAULT NULL COMMENT '访问ip地址',
  `behavior` varchar(255) DEFAULT NULL COMMENT '用户行为',
  `module_uid` varchar(255) DEFAULT NULL COMMENT '模块uid（文章uid，标签uid，分类uid）',
  `other_data` varchar(255) DEFAULT NULL COMMENT '附加数据(比如搜索内容)',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Web访问记录表';

/*Data for the table `t_web_visit` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
