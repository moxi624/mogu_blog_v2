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
  `role_uid` varchar(32) DEFAULT NULL COMMENT '拥有角色Uid',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

/*Data for the table `t_admin` */

insert  into `t_admin`(`uid`,`user_name`,`pass_word`,`gender`,`avatar`,`email`,`birthday`,`mobile`,`valid_code`,`summary`,`login_count`,`last_login_time`,`last_login_ip`,`status`,`create_time`,`update_time`,`nick_name`,`qq_number`,`we_chat`,`occupation`,`github`,`gitee`,`role_uid`) values ('1f01cd1d2f474743b241d74008b12333','admin','$2a$10$2GretrSxlvw8492fpLh.veW9mRvElrh8V4oRFPOxIB5rMkdBl6GVS','1',',874939b5d7bd4f00b78a866a7c7f2a20','','2018-09-29',' ',NULL,'一个95后！正在潜心研究机器学习和Java后端技术，一边学习一边积累经验',599,'2020-01-05 13:34:17','192.168.80.1',1,'2018-09-08 11:05:05','2018-11-14 06:47:00','陌溪_','1595833114','','Java开发','https://github.com/moxi624','https://gitee.com/moxi159753','434994947c5a4ee3a710cd277357c7c3');

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

insert  into `t_blog`(`uid`,`title`,`summary`,`content`,`tag_uid`,`click_count`,`collect_count`,`file_uid`,`status`,`create_time`,`update_time`,`admin_uid`,`is_original`,`author`,`articles_part`,`blog_sort_uid`,`level`,`is_publish`) values ('51fa6be01a7296c4fc380f7780db9641','测试博客3','测试博客3','<p>测试博客</p>\n','5c939107ddb746b989156737805df625',0,0,'bb89f76717a471901a5436ddceef3143',1,'2020-01-02 18:52:50','2020-01-02 18:52:50','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','337806254f9c42999043de5c5ee09e77',3,'1'),('65732612f21df2ed75f2e8ec69fa34a1','测试博客5','测试博客5','<p>测试博客5</p>\n','5626932d452c2ad863d9b3cb0b69d22d',0,0,'bb89f76717a471901a5436ddceef3143',1,'2020-01-02 18:54:14','2020-01-02 18:54:14','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','db0d64ea7df409de5d2d747927cfa1a5',0,'1'),('76cde1fb56c91000de416c151881f336','测试博客3','测试博客3','<p>测试博客3</p>\n','1d1fd6d26c8e40a38637ef6126c45cd0',0,0,'bb89f76717a471901a5436ddceef3143',0,'2020-01-04 21:13:00','2020-01-04 21:13:00','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','2c93dfab0e754006866f8ed486923a41',3,'1'),('7dbba6700c80d07b78b6d06986a237e8','测试博客1','测试博客1','<p>测试博客1</p>\n','1d1fd6d26c8e40a38637ef6126c45cd0',0,0,'bb89f76717a471901a5436ddceef3143',1,'2020-01-02 18:46:56','2020-01-02 18:46:56','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','2c93dfab0e754006866f8ed486923a41',3,'1'),('93dfac7ee98dc81191560b324178a7cb','测试博客8','测试博客8','<p>测试博客8</p>\n','5c939107ddb746b989156737805df625',1,0,'f82156768b3a8c56b3f827b23939cd4e',1,'2020-01-02 18:55:29','2020-01-02 18:55:29','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','337806254f9c42999043de5c5ee09e77',1,'1'),('aab921f898b55128488a610876e8daad','测试博客6','测试博客6','<p>测试博客6</p>\n','f5d458db6a044eaebc22232efa1e3b54',0,0,'f82156768b3a8c56b3f827b23939cd4e',1,'2020-01-02 18:54:44','2020-01-02 18:54:44','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','093d8bdd01c84890a928e923d5c235fe',2,'1'),('c5c5b64d3b21ef1519e4ac5f35accf38','测试博客2','测试博客2','<p>测试博客2</p>\n','7e0e93ea6cdb44ae92e58f48e6496ed7',0,0,'f82156768b3a8c56b3f827b23939cd4e',1,'2020-01-02 18:47:57','2020-01-02 18:47:57','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','a03d7290b1c04b6eaf46659661b47032',2,'1'),('d7f0a2c8b181a41369c3f5939f29f9e2','测试4','测试4','<p>测试4</p>\n','a9a747d944c24845815356f72723ef8e',0,0,'f82156768b3a8c56b3f827b23939cd4e',1,'2020-01-02 18:53:22','2020-01-02 18:53:22','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','9d2019983d91490aaa758eddd7c07caf',1,'1'),('ea1ae6ffcb49500325b2c5a493f723f4','测试博客7','测试博客7','<p>测试博客7</p>\n','8d5ce3e0c0784b95adb7f9e7b76dca93',0,0,'bb89f76717a471901a5436ddceef3143',1,'2020-01-02 18:55:13','2020-01-02 18:55:13','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','a03d7290b1c04b6eaf46659661b47032',1,'1');

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

insert  into `t_blog_sort`(`uid`,`sort_name`,`content`,`create_time`,`update_time`,`status`,`sort`,`click_count`) values ('029d80ba36a04c96a89a80e2705031a2','测试分类','测试分类','2019-01-10 21:10:43','2019-01-10 21:10:43',0,0,0),('093d8bdd01c84890a928e923d5c235fe','软件推荐','软件推荐','2018-09-24 17:14:59','2018-09-24 17:14:59',1,6,1),('2c93dfab0e754006866f8ed486923a41','慢生活','慢生活，不是懒惰，放慢速度不是拖延时间，而是让我们在生活中寻找到平衡','2018-09-24 16:29:33','2018-09-24 16:29:33',1,49,7),('337806254f9c42999043de5c5ee09e77','技术新闻','发现世界的每一天','2018-12-29 10:42:11','2018-12-29 10:42:11',1,30,0),('6fe800110ac351e244ed125526e65b38','测试','测试','2019-12-08 16:22:28','2019-12-08 16:22:28',0,0,0),('9d2019983d91490aaa758eddd7c07caf','机器学习','机器学习','2018-11-21 20:56:02','2018-11-21 20:56:02',1,16,2),('a03d7290b1c04b6eaf46659661b47032','后端开发','后端开发专题','2018-12-29 10:35:43','2018-12-29 10:35:43',1,48,2),('ca28ffc94ea94fbda5571e0b242021e2','前端开发','前端开发专题','2018-12-29 10:35:58','2018-12-29 10:35:58',1,10,0),('db0d64ea7df409de5d2d747927cfa1a5','学习笔记','学习笔记','2019-08-30 10:50:03','2019-08-30 10:50:03',1,7,0),('e60df954efcd47c48463a504bb70bbe9','面试','面试专题','2018-12-19 21:16:30','2018-12-19 21:16:30',1,4,0);

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

insert  into `t_category_menu`(`uid`,`name`,`menu_level`,`summary`,`parent_uid`,`url`,`icon`,`sort`,`status`,`create_time`,`update_time`) values ('02ea2f9ef5d44f559fb66189b05f6769','Solr',2,'Solr监控中心','147cd431cbb9007bde87444d7987b151','/monitor/solr','index',0,1,'2018-11-29 11:55:39','2018-11-29 11:55:39'),('079f0cfdb7a7017d827f5c349983eebc','Eureka',2,'Eureka监控中心','147cd431cbb9007bde87444d7987b151','/monitor/eureka','authority',0,1,'2020-01-06 05:27:30','2020-01-06 05:27:30'),('0a035547bbec404eb3ee0ef43312148d','分类管理',2,'管理博客分类','49b42250abcb47ff876bad699cf34f03','/blog/blogSort','sort',10,1,'2018-11-26 03:07:14','2018-11-26 03:07:14'),('147cd431cbb9007bde87444d7987b151','监控中心',1,'监控中心',NULL,'/monitor','resource',0,1,'2020-01-05 13:25:32','2020-01-05 13:25:32'),('1f01cd1d2f474743b241d74008b12333','博客管理',2,'对博客进行增删改查','49b42250abcb47ff876bad699cf34f03','/blog/blog','edit',11,1,'2018-11-27 03:47:07','2018-11-27 03:47:07'),('2de247af3b0a459095e937d7ab9f5864','管理员管理',2,'管理员增删改查','d3a19221259d439b916f475e43edb13d','/authority/admin','user',0,1,'2018-11-25 19:09:21','2018-11-25 19:09:21'),('2fb47d3b6dbd44279c8206740a263543','网站配置',2,'网站配置','badf0010422b432ba6ec9c83a25012ed','/system/webConfig','web',0,1,'2018-11-28 19:59:04','2018-11-28 19:59:04'),('4337f63d13d84b9aba64b9d7a69fd066','异常日志',2,'异常日志','98b82be8785e41dc939b6a5517fdfa53','/log/exceptionLog','exception',0,1,'2018-11-28 20:01:36','2018-11-28 20:01:36'),('49b42250abcb47ff876bad699cf34f03','博客管理',1,'用于博客的一些相关操作',NULL,'/blog','edit',16,1,'2018-11-25 05:15:07','2018-11-25 05:15:07'),('4dea9c4f39d2480983e8c4333d35e036','图片类别管理',2,'图片类别管理','65e22f3d36d94bcea47478aba02895a1','/picture/pictureSort','picture',0,1,'2018-11-28 19:50:31','2018-11-28 19:50:31'),('5010ae46511e4c0b9f30d1c63ad3f0c1','角色管理',2,'管理用户角色信息','d3a19221259d439b916f475e43edb13d','/authority/role','peoples',0,1,'2018-11-25 19:10:34','2018-11-25 19:10:34'),('510483ce569b4fc88299f346147b1314','资源管理',1,'资源管理','','/resource','resource',2,1,'2018-11-28 19:42:13','2018-11-28 19:42:13'),('6228ff4e9ebd42c89599b322201a0345','反馈管理',2,'反馈管理','bcf4a9bc21c14b559bcb015fb7912266','/message/feedback','table',0,1,'2018-11-28 19:48:30','2018-11-28 19:48:30'),('65e22f3d36d94bcea47478aba02895a1','图片管理',1,'图片管理','','/picture','example',3,1,'2018-11-28 19:48:53','2018-11-28 19:48:53'),('6606b7e646d545e5a25c70b5e5fade9f','标签管理',2,'对博客标签进行管理','49b42250abcb47ff876bad699cf34f03','/blog/blogTag','tag',4,1,'2018-11-26 02:57:38','2018-11-26 02:57:38'),('78ab104b123f4950af14d65798afb756','收藏管理',2,'管理用户收藏','49b42250abcb47ff876bad699cf34f03','/blog/collect','example',8,1,'2018-11-25 19:07:48','2018-11-25 19:07:48'),('9002d1ae905c4cb79c2a485333dad2f7','友情链接',2,'友情链接','badf0010422b432ba6ec9c83a25012ed','/system/blogLink','blogLink',0,1,'2018-11-29 03:56:35','2018-11-29 03:56:35'),('93f7fd9a6e81735c47649e6b36042b5d','Druid',2,'Druid监控中心','147cd431cbb9007bde87444d7987b151','/monitor/druid','log',0,1,'2020-01-06 05:26:51','2020-01-06 05:26:51'),('9449ce5dd5e24b21a9d15f806cb36e87','分类管理',2,'分类管理','510483ce569b4fc88299f346147b1314','/resource/resourceSort','sort',0,1,'2018-11-29 03:43:27','2018-11-29 03:43:27'),('98b82be8785e41dc939b6a5517fdfa53','操作日志',1,'操作日志','','/log','log',9,1,'2018-11-28 20:00:19','2018-11-28 20:00:19'),('9beb7caa2c844b36a02789262dc76fbe','评论管理',2,'评论管理','bcf4a9bc21c14b559bcb015fb7912266','/message/comment','table',1,1,'2018-11-28 19:47:23','2018-11-28 19:47:23'),('9e91b4f993c946cba4bf720b2c1b2e90','用户日志',2,'用户Web端访问情况','98b82be8785e41dc939b6a5517fdfa53','/log/webVisit','user1',0,1,'2019-05-17 10:16:47','2019-05-17 10:16:47'),('a5902692a3ed4fd794895bf634f97b8e','操作日志',2,'操作日志','98b82be8785e41dc939b6a5517fdfa53','/log/log','log',0,1,'2018-11-28 20:01:02','2018-11-28 20:01:02'),('a9396f1a3fbdec3d4cb614f388a22bea','SpringBoot',2,'SpringBootAdmin监控中心','147cd431cbb9007bde87444d7987b151','/monitor/springBootAdmin','system',0,1,'2020-01-05 21:30:16','2020-01-05 21:30:16'),('aa225cdae6464bc0acebd732192f8362','菜单管理',2,'对页面菜单进行管理','d3a19221259d439b916f475e43edb13d','/authority/categoryMenu','example',0,1,'2018-11-25 11:12:01','2018-11-25 11:12:01'),('b511cae571834971a392ae4779270034','游客管理',2,'游客管理','c519725da92b42f3acf0cc9fad58c664','/user/visitor','table',2,1,'2018-11-28 19:54:28','2018-11-28 19:54:28'),('baace3dc03d34c54b81761dce8243814','接口管理',1,'接口管理','','/restapi','restapi',4,1,'2018-11-28 20:01:57','2018-11-28 20:01:57'),('badf0010422b432ba6ec9c83a25012ed','系统管理',1,'系统管理','','/system','system',11,1,'2018-11-28 19:54:47','2018-11-28 19:54:47'),('bcf4a9bc21c14b559bcb015fb7912266','消息管理',1,'消息管理','','/message','message1',6,1,'2018-11-28 19:45:29','2018-11-28 19:45:29'),('c519725da92b42f3acf0cc9fad58c664','用户管理',1,'用户管理','','/user','user1',15,1,'2018-11-28 19:51:47','2018-11-28 19:51:47'),('cbd7ba11c1b38c66b569405ed9185f35','RabbitMQ',2,'RabbitMQ监控中心','147cd431cbb9007bde87444d7987b151','/monitor/rabbitMQ','resource',0,1,'2020-01-05 13:29:39','2020-01-05 13:29:39'),('d3a19221259d439b916f475e43edb13d','权限管理',1,'对管理员权限分配进行管理','','/authority','authority',5,1,'2018-11-25 19:08:42','2018-11-25 19:08:42'),('d4d92c53d3614d00865e9219b8292a90','Picture接口',2,'Picture接口','baace3dc03d34c54b81761dce8243814','/restapi/pictureRestApi','table',0,1,'2018-11-28 20:04:33','2018-11-28 20:04:33'),('e4a482c089d04a30b6ecbaadb81b70f8','Admin接口',2,'Admin接口','baace3dc03d34c54b81761dce8243814','/restapi/adminRestApi','table',0,1,'2018-11-28 20:03:32','2018-11-28 20:03:32'),('f9276eb8e3274c8aa05577c86e4dc8c1','Web接口',2,'Web接口','baace3dc03d34c54b81761dce8243814','/restapi/webRestApi','table',0,1,'2018-11-28 20:04:52','2018-11-28 20:04:52'),('faccfe476b89483791c05019ad5b4906','关于我',2,'关于我','badf0010422b432ba6ec9c83a25012ed','/system/aboutMe','aboutMe',0,1,'2018-11-29 03:55:17','2018-11-29 03:55:17'),('fb4237a353d0418ab42c748b7c1d64c6','用户管理',2,'用户管理','c519725da92b42f3acf0cc9fad58c664','/user/user','table',1,1,'2018-11-28 19:52:20','2018-11-28 19:52:20'),('ffc6e9ca2cc243febf6d2f476b849163','视频管理',2,'视频管理','510483ce569b4fc88299f346147b1314','/resource/studyVideo','table',0,1,'2018-11-28 19:43:50','2018-11-28 19:43:50');

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

insert  into `t_link`(`uid`,`title`,`summary`,`url`,`click_count`,`create_time`,`update_time`,`status`,`sort`) values ('5217d7212f9d487eab13deadca961402','蘑菇博客Git','蘑菇博客Git','https://gitee.com/moxi159753/mogu_blog_v2',17,'2018-12-16 18:25:11','2018-12-16 18:25:11',1,0),('72fbe9e48e97c4206848ff62605000a0','测试','测试','测试',1,'2019-12-11 13:01:37','2019-12-11 13:01:37',1,0),('bb418e0a9d27490bb71972bd8da5afa6','IT大本营','IT大本营 - 专注于技术分享的开发者社区','http://www.itarea.cn/',2,'2018-09-26 13:55:33','2018-09-26 13:55:33',0,0),('dcc01149be71492dabd55821c22f6061','Mybatis-plus','MyBatis-Plus 为简化开发而生','http://mp.baomidou.com/',4,'2018-09-26 18:52:58','2018-09-26 18:52:58',1,1),('eb8b05ef241caabb9cd6efa71c7dd66d','测试111','测试11','测试',0,'2019-12-11 13:00:25','2019-12-11 13:00:25',0,0);

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

insert  into `t_picture`(`uid`,`file_uid`,`pic_name`,`picture_sort_uid`,`status`,`create_time`,`update_time`) values ('0e31dffaf7472da7f3a786713668a7ea','0b1f3c49960d6596f35bc34985b81d07',NULL,'3f035952f857f7b35ef76f4fc687cd32',0,'2020-01-02 18:52:00','2020-01-02 18:52:00'),('48b49be9fd0d04b7d27527bed167c8ba','bb89f76717a471901a5436ddceef3143',NULL,'3f035952f857f7b35ef76f4fc687cd32',1,'2020-01-02 18:58:25','2020-01-02 18:58:25'),('6a71a89867b8bcc75aaa6c1921a7d658','f82156768b3a8c56b3f827b23939cd4e',NULL,'3f035952f857f7b35ef76f4fc687cd32',1,'2020-01-02 18:58:25','2020-01-02 18:58:25'),('9c06a33434352b767ebf8945dc58cf95','5529718c906473da0f7a139cb62cb9c8',NULL,'3f035952f857f7b35ef76f4fc687cd32',0,'2020-01-02 18:52:00','2020-01-02 18:52:00');

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

insert  into `t_picture_sort`(`uid`,`file_uid`,`name`,`status`,`create_time`,`update_time`,`parent_uid`,`sort`) values ('3f035952f857f7b35ef76f4fc687cd32','5529718c906473da0f7a139cb62cb9c8','测试类别',1,'2020-01-02 18:51:11','2020-01-02 18:51:11',NULL,0);

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

insert  into `t_role`(`uid`,`role_name`,`create_time`,`update_time`,`status`,`summary`,`category_menu_uids`) values ('1e63940c64d72e52019a4393cdb41a8f','123','2019-12-20 17:13:06','2019-12-20 17:13:06',0,'123','[]'),('434994947c5a4ee3a710cd277357c7c3','超级管理员','2018-10-15 23:56:26','2018-10-15 23:56:35',1,'超级管理员，管理全部菜单和功能','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"c519725da92b42f3acf0cc9fad58c664\",\"b511cae571834971a392ae4779270034\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"badf0010422b432ba6ec9c83a25012ed\",\"02ea2f9ef5d44f559fb66189b05f6769\",\"2fb47d3b6dbd44279c8206740a263543\",\"9002d1ae905c4cb79c2a485333dad2f7\",\"faccfe476b89483791c05019ad5b4906\",\"98b82be8785e41dc939b6a5517fdfa53\",\"4337f63d13d84b9aba64b9d7a69fd066\",\"9e91b4f993c946cba4bf720b2c1b2e90\",\"a5902692a3ed4fd794895bf634f97b8e\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"d3a19221259d439b916f475e43edb13d\",\"2de247af3b0a459095e937d7ab9f5864\",\"5010ae46511e4c0b9f30d1c63ad3f0c1\",\"aa225cdae6464bc0acebd732192f8362\",\"baace3dc03d34c54b81761dce8243814\",\"d4d92c53d3614d00865e9219b8292a90\",\"e4a482c089d04a30b6ecbaadb81b70f8\",\"f9276eb8e3274c8aa05577c86e4dc8c1\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\",\"510483ce569b4fc88299f346147b1314\",\"9449ce5dd5e24b21a9d15f806cb36e87\",\"ffc6e9ca2cc243febf6d2f476b849163\",\"147cd431cbb9007bde87444d7987b151\",\"079f0cfdb7a7017d827f5c349983eebc\",\"93f7fd9a6e81735c47649e6b36042b5d\",\"a9396f1a3fbdec3d4cb614f388a22bea\",\"cbd7ba11c1b38c66b569405ed9185f35\"]'),('434994947c5a4ee3a710cd277357c7c4','文章管理员','2018-10-14 23:56:21','2018-10-14 23:56:23',1,'管理文章','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\"]'),('915dd43cd5794a4475c3536737dbd219','12','2019-12-20 17:20:05','2019-12-20 17:20:05',0,'123111','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"c519725da92b42f3acf0cc9fad58c664\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"b511cae571834971a392ae4779270034\"]'),('d105da79260f4d6a8a03571e4a2b17bc','一般管理员','2019-05-28 16:43:26','2019-05-28 16:43:26',1,'一般管理员','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"c519725da92b42f3acf0cc9fad58c664\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"b511cae571834971a392ae4779270034\",\"badf0010422b432ba6ec9c83a25012ed\",\"02ea2f9ef5d44f559fb66189b05f6769\",\"2fb47d3b6dbd44279c8206740a263543\",\"9002d1ae905c4cb79c2a485333dad2f7\",\"faccfe476b89483791c05019ad5b4906\",\"98b82be8785e41dc939b6a5517fdfa53\",\"4337f63d13d84b9aba64b9d7a69fd066\",\"9e91b4f993c946cba4bf720b2c1b2e90\",\"a5902692a3ed4fd794895bf634f97b8e\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"baace3dc03d34c54b81761dce8243814\",\"d4d92c53d3614d00865e9219b8292a90\",\"e4a482c089d04a30b6ecbaadb81b70f8\",\"f9276eb8e3274c8aa05577c86e4dc8c1\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\"]'),('dbf73b14c0a615d77a8124f215c4d06d','123345343','2019-12-20 18:36:03','2019-12-20 18:36:03',0,'123','[]'),('f930922ac2d7e80561bf4f9e95209ee1','测试3453','2019-12-20 16:58:49','2019-12-20 16:58:49',0,'','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"c519725da92b42f3acf0cc9fad58c664\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"b511cae571834971a392ae4779270034\",\"badf0010422b432ba6ec9c83a25012ed\",\"02ea2f9ef5d44f559fb66189b05f6769\",\"2fb47d3b6dbd44279c8206740a263543\",\"9002d1ae905c4cb79c2a485333dad2f7\",\"faccfe476b89483791c05019ad5b4906\",\"98b82be8785e41dc939b6a5517fdfa53\",\"4337f63d13d84b9aba64b9d7a69fd066\",\"9e91b4f993c946cba4bf720b2c1b2e90\",\"a5902692a3ed4fd794895bf634f97b8e\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"d3a19221259d439b916f475e43edb13d\",\"25b772444f7548f8a3957e00e5eb4246\",\"2de247af3b0a459095e937d7ab9f5864\",\"5010ae46511e4c0b9f30d1c63ad3f0c1\",\"aa225cdae6464bc0acebd732192f8362\",\"baace3dc03d34c54b81761dce8243814\",\"d4d92c53d3614d00865e9219b8292a90\",\"e4a482c089d04a30b6ecbaadb81b70f8\",\"f9276eb8e3274c8aa05577c86e4dc8c1\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\",\"510483ce569b4fc88299f346147b1314\",\"9449ce5dd5e24b21a9d15f806cb36e87\",\"ffc6e9ca2cc243febf6d2f476b849163\"]');

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

insert  into `t_sys_log`(`uid`,`user_name`,`admin_uid`,`ip`,`url`,`type`,`class_path`,`method`,`params`,`operation`,`status`,`create_time`,`update_time`) values ('0230001a239173c321ff2ee071e8a21d','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/pictureSort/add','POST','com.moxi.mogublog.admin.restapi.PictureSortRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@410a0707], 参数2:测试类别, 参数3:null, 参数4:null, ','增加图片分类',1,'2020-01-02 18:51:11','2020-01-02 18:51:11'),('0d2ac6c384cd643d4b8f6314dd83719d','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@5f742572], 参数2:BlogVO(title=测试博客8, summary=测试博客8, content=<p>测试博客8</p>\n, tagUid=5c939107ddb746b989156737805df625, blogSortUid=337806254f9c42999043de5c5ee09e77, fileUid=5529718c906473da0f7a139cb62cb9c8, adminUid=null, isPublish=1, isOriginal=1, author=null, articlesPart=null, level=0, tagList=null, photoList=[/blog/admin/jfif/2020/1/2/1577962320420.jfif], blogSort=null, praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加博客',1,'2020-01-02 18:55:29','2020-01-02 18:55:29'),('0e8cac0dd1ec71c70644a6e4cbaa2dea','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@615d5975], 参数2:BlogVO(title=测试博客1, summary=测试博客1, content=<p>测试博客1</p>\n, tagUid=1d1fd6d26c8e40a38637ef6126c45cd0, blogSortUid=2c93dfab0e754006866f8ed486923a41, fileUid=null, adminUid=null, isPublish=1, isOriginal=1, author=null, articlesPart=null, level=0, tagList=null, photoList=null, blogSort=null, praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加博客',1,'2020-01-02 18:46:57','2020-01-02 18:46:57'),('158f4218f9a110d9fc6fdc3d17c0bf26','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/categoryMenu/add','POST','com.moxi.mogublog.admin.restapi.CategoryMenuRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@d03f582], 参数2:CategoryMenu(name=Eureka, menuLevel=2, summary=Eureka监控中心, icon=source, parentUid=147cd431cbb9007bde87444d7987b151, url=/monitor/eureka, sort=null, parentCategoryMenu=null, childCategoryMenu=null), ','增加菜单',1,'2020-01-05 13:27:30','2020-01-05 13:27:30'),('23b3fb04880f00f86983802aeaaa0efd','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/categoryMenu/add','POST','com.moxi.mogublog.admin.restapi.CategoryMenuRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@296d4678], 参数2:CategoryMenu(name=SpringBootAdmin, menuLevel=2, summary=SpringBootAdmin监控中心, icon=resource, parentUid=147cd431cbb9007bde87444d7987b151, url=/monitor/springBootAdmin, sort=null, parentCategoryMenu=null, childCategoryMenu=null), ','增加菜单',1,'2020-01-05 13:30:16','2020-01-05 13:30:16'),('24ce50a38cf4a82878fcf7897ac497b8','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/role/update','POST','com.moxi.mogublog.admin.restapi.RoleRestApi','update','参数1:RoleVO(roleName=超级管理员, summary=超级管理员，管理全部菜单和功能, categoryMenuUids=[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"c519725da92b42f3acf0cc9fad58c664\",\"b511cae571834971a392ae4779270034\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"badf0010422b432ba6ec9c83a25012ed\",\"02ea2f9ef5d44f559fb66189b05f6769\",\"2fb47d3b6dbd44279c8206740a263543\",\"9002d1ae905c4cb79c2a485333dad2f7\",\"faccfe476b89483791c05019ad5b4906\",\"98b82be8785e41dc939b6a5517fdfa53\",\"4337f63d13d84b9aba64b9d7a69fd066\",\"9e91b4f993c946cba4bf720b2c1b2e90\",\"a5902692a3ed4fd794895bf634f97b8e\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"d3a19221259d439b916f475e43edb13d\",\"2de247af3b0a459095e937d7ab9f5864\",\"5010ae46511e4c0b9f30d1c63ad3f0c1\",\"aa225cdae6464bc0acebd732192f8362\",\"baace3dc03d34c54b81761dce8243814\",\"d4d92c53d3614d00865e9219b8292a90\",\"e4a482c089d04a30b6ecbaadb81b70f8\",\"f9276eb8e3274c8aa05577c86e4dc8c1\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\",\"510483ce569b4fc88299f346147b1314\",\"9449ce5dd5e24b21a9d15f806cb36e87\",\"ffc6e9ca2cc243febf6d2f476b849163\",\"147cd431cbb9007bde87444d7987b151\",\"079f0cfdb7a7017d827f5c349983eebc\",\"93f7fd9a6e81735c47649e6b36042b5d\",\"a9396f1a3fbdec3d4cb614f388a22bea\",\"cbd7ba11c1b38c66b569405ed9185f35\"]), 参数2:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','更新角色信息',1,'2020-01-05 13:33:24','2020-01-05 13:33:24'),('2d1cc1fe0e3dfcb3da13e819702e1200','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@7fb9930c], 参数2:BlogVO(title=测试博客7, summary=测试博客7, content=<p>测试博客7</p>\n, tagUid=8d5ce3e0c0784b95adb7f9e7b76dca93, blogSortUid=a03d7290b1c04b6eaf46659661b47032, fileUid=bb89f76717a471901a5436ddceef3143, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=1, tagList=[Tag(content=建站系统, clickCount=3213, sort=4)], photoList=[/blog/admin/jfif/2020/1/2/1577962705437.jfif], blogSort=BlogSort(sortName=后端开发, content=后端开发专题, clickCount=2, sort=48), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-04 21:13:52','2020-01-04 21:13:52'),('2d41110eb0ecd980c060a8393a41819c','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@6d5967b7], 参数2:BlogVO(title=测试博客2, summary=测试博客2, content=<p>测试博客2</p>\n, tagUid=7e0e93ea6cdb44ae92e58f48e6496ed7, blogSortUid=a03d7290b1c04b6eaf46659661b47032, fileUid=5529718c906473da0f7a139cb62cb9c8, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=2, tagList=[Tag(content=Java, clickCount=1, sort=30)], photoList=[/blog/admin/jfif/2020/1/2/1577962320420.jfif], blogSort=BlogSort(sortName=后端开发, content=后端开发专题, clickCount=2, sort=48), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:53:40','2020-01-02 18:53:40'),('2e719087d4c64dab12c43c7eb9fd6cf1','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@6587f6bd], 参数2:BlogVO(title=测试博客1, summary=测试博客1, content=<p>测试博客1</p>\n, tagUid=1d1fd6d26c8e40a38637ef6126c45cd0, blogSortUid=2c93dfab0e754006866f8ed486923a41, fileUid=0b1f3c49960d6596f35bc34985b81d07, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=3, tagList=[Tag(content=Linux, clickCount=47, sort=31)], photoList=[/blog/admin/jfif/2020/1/2/1577962320328.jfif], blogSort=BlogSort(sortName=慢生活, content=慢生活，不是懒惰，放慢速度不是拖延时间，而是让我们在生活中寻找到平衡, clickCount=7, sort=49), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:53:48','2020-01-02 18:53:48'),('33c050f80283eafa62ef277b9e08e888','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@4d732cf0], 参数2:BlogVO(title=测试博客6, summary=测试博客6, content=<p>测试博客6</p>\n, tagUid=f5d458db6a044eaebc22232efa1e3b54, blogSortUid=093d8bdd01c84890a928e923d5c235fe, fileUid=f82156768b3a8c56b3f827b23939cd4e, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=2, tagList=[Tag(content=深度学习, clickCount=0, sort=7)], photoList=[/blog/admin/jfif/2020/1/2/1577962705270.jfif], blogSort=BlogSort(sortName=软件推荐, content=软件推荐, clickCount=1, sort=6), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-04 21:14:06','2020-01-04 21:14:06'),('35e68286e9913ff20611f0fb1bcf2355','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@1e9ae73e], 参数2:BlogVO(title=测试博客2, summary=测试博客2, content=<p>测试博客2</p>\n, tagUid=7e0e93ea6cdb44ae92e58f48e6496ed7, blogSortUid=a03d7290b1c04b6eaf46659661b47032, fileUid=f82156768b3a8c56b3f827b23939cd4e, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=2, tagList=[Tag(content=Java, clickCount=1, sort=30)], photoList=[/blog/admin/jfif/2020/1/2/1577962705270.jfif], blogSort=BlogSort(sortName=后端开发, content=后端开发专题, clickCount=2, sort=48), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:59:45','2020-01-02 18:59:45'),('3623403aaad8c82de9bbaf4a6d523d57','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@6a8ea82e], 参数2:BlogVO(title=测试博客7, summary=测试博客7, content=<p>测试博客7</p>\n, tagUid=8d5ce3e0c0784b95adb7f9e7b76dca93, blogSortUid=a03d7290b1c04b6eaf46659661b47032, fileUid=bb89f76717a471901a5436ddceef3143, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=0, tagList=[Tag(content=建站系统, clickCount=3213, sort=4)], photoList=[/blog/admin/jfif/2020/1/2/1577962705437.jfif], blogSort=BlogSort(sortName=后端开发, content=后端开发专题, clickCount=2, sort=48), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:58:49','2020-01-02 18:58:49'),('3e5db136909ff93a91771d569d6a4047','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@2ed25377], 参数2:BlogVO(title=测试博客3, summary=测试博客3, content=<p>测试博客</p>\n, tagUid=5c939107ddb746b989156737805df625, blogSortUid=337806254f9c42999043de5c5ee09e77, fileUid=5529718c906473da0f7a139cb62cb9c8, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=2, tagList=[Tag(content=机器学习, clickCount=0, sort=22)], photoList=[/blog/admin/jfif/2020/1/2/1577962320420.jfif], blogSort=BlogSort(sortName=技术新闻, content=发现世界的每一天, clickCount=0, sort=30), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:53:34','2020-01-02 18:53:34'),('44e928233cd37196f9cf9cf8c79a779c','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@d632b74], 参数2:BlogVO(title=测试博客8, summary=测试博客8, content=<p>测试博客8</p>\n, tagUid=5c939107ddb746b989156737805df625, blogSortUid=337806254f9c42999043de5c5ee09e77, fileUid=f82156768b3a8c56b3f827b23939cd4e, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=0, tagList=[Tag(content=机器学习, clickCount=0, sort=22)], photoList=[/blog/admin/jfif/2020/1/2/1577962705270.jfif], blogSort=BlogSort(sortName=技术新闻, content=发现世界的每一天, clickCount=0, sort=30), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:58:43','2020-01-02 18:58:43'),('47952477c99551f51077bee5d35b4f88','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@2bcc9478], 参数2:BlogVO(title=测试博客8, summary=测试博客8, content=<p>测试博客8</p>\n, tagUid=5c939107ddb746b989156737805df625, blogSortUid=337806254f9c42999043de5c5ee09e77, fileUid=f82156768b3a8c56b3f827b23939cd4e, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=1, tagList=[Tag(content=机器学习, clickCount=0, sort=22)], photoList=[/blog/admin/jfif/2020/1/2/1577962705270.jfif], blogSort=BlogSort(sortName=技术新闻, content=发现世界的每一天, clickCount=0, sort=30), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-04 21:13:34','2020-01-04 21:13:34'),('47f493fc9d9f80096cb1b71d524b0b80','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@177a1182], 参数2:BlogVO(title=测试博客6, summary=测试博客6, content=<p>测试博客6</p>\n, tagUid=f5d458db6a044eaebc22232efa1e3b54, blogSortUid=093d8bdd01c84890a928e923d5c235fe, fileUid=0b1f3c49960d6596f35bc34985b81d07, adminUid=null, isPublish=1, isOriginal=1, author=null, articlesPart=null, level=0, tagList=null, photoList=[/blog/admin/jfif/2020/1/2/1577962320328.jfif], blogSort=null, praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加博客',1,'2020-01-02 18:54:44','2020-01-02 18:54:44'),('4ab59868fce5203e7e03ef81308007a5','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@2a8c77fc], 参数2:BlogVO(title=测试博客3, summary=测试博客3, content=<p>测试博客3</p>\n, tagUid=1d1fd6d26c8e40a38637ef6126c45cd0, blogSortUid=2c93dfab0e754006866f8ed486923a41, fileUid=bb89f76717a471901a5436ddceef3143, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=3, tagList=[Tag(content=Linux, clickCount=47, sort=31)], photoList=[/blog/admin/jfif/2020/1/2/1577962705437.jfif], blogSort=BlogSort(sortName=慢生活, content=慢生活，不是懒惰，放慢速度不是拖延时间，而是让我们在生活中寻找到平衡, clickCount=7, sort=49), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加博客',1,'2020-01-04 21:13:02','2020-01-04 21:13:02'),('519a326f60229eb4bdbfba829aa2d440','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/todo/add','POST','com.moxi.mogublog.admin.restapi.TodoRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@3f04f3d0], 参数2:TodoVO(text=测试代办事项2, done=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加代办事项',1,'2020-01-02 19:01:00','2020-01-02 19:01:00'),('53c52a9b11d81ea06ccec7c38837d8d5','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@6eb37627], 参数2:BlogVO(title=测试博客5, summary=测试博客5, content=<p>测试博客5</p>\n, tagUid=5626932d452c2ad863d9b3cb0b69d22d, blogSortUid=db0d64ea7df409de5d2d747927cfa1a5, fileUid=0b1f3c49960d6596f35bc34985b81d07, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=0, tagList=[Tag(content=Linux, clickCount=47, sort=31)], photoList=[/blog/admin/jfif/2020/1/2/1577962320328.jfif], blogSort=BlogSort(sortName=慢生活, content=慢生活，不是懒惰，放慢速度不是拖延时间，而是让我们在生活中寻找到平衡, clickCount=7, sort=49), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加博客',1,'2020-01-02 18:54:14','2020-01-02 18:54:14'),('6cf598217f359831c0dc8830256f2547','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@8fffb1b], 参数2:BlogVO(title=测试博客5, summary=测试博客5, content=<p>测试博客5</p>\n, tagUid=5626932d452c2ad863d9b3cb0b69d22d, blogSortUid=db0d64ea7df409de5d2d747927cfa1a5, fileUid=bb89f76717a471901a5436ddceef3143, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=0, tagList=[Tag(content=学习笔记, clickCount=33, sort=9)], photoList=[/blog/admin/jfif/2020/1/2/1577962705437.jfif], blogSort=BlogSort(sortName=学习笔记, content=学习笔记, clickCount=0, sort=7), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:59:07','2020-01-02 18:59:07'),('7ac5d7d9e749d45e992c99d2b15d503f','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/categoryMenu/add','POST','com.moxi.mogublog.admin.restapi.CategoryMenuRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@52ba37b5], 参数2:CategoryMenu(name=Druid, menuLevel=2, summary=Druid监控中心, icon=resource, parentUid=147cd431cbb9007bde87444d7987b151, url=/monitor/druidMonitor, sort=null, parentCategoryMenu=null, childCategoryMenu=null), ','增加菜单',1,'2020-01-05 13:26:51','2020-01-05 13:26:51'),('7c420c9a2fc84329e175234a7d4dbfad','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/delete','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','delete','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@3821d7dd], 参数2:BlogVO(title=null, summary=null, content=null, tagUid=null, blogSortUid=null, fileUid=null, adminUid=null, isPublish=1, isOriginal=1, author=null, articlesPart=null, level=0, tagList=null, photoList=null, blogSort=null, praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','删除博客',1,'2020-01-04 21:13:07','2020-01-04 21:13:07'),('7c87ab379e443610f08ec49e7c2d2861','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@1b51922a], 参数2:BlogVO(title=测试博客1, summary=测试博客1, content=<p>测试博客1</p>\n, tagUid=1d1fd6d26c8e40a38637ef6126c45cd0, blogSortUid=2c93dfab0e754006866f8ed486923a41, fileUid=bb89f76717a471901a5436ddceef3143, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=3, tagList=[Tag(content=Linux, clickCount=47, sort=31)], photoList=[/blog/admin/jfif/2020/1/2/1577962705437.jfif], blogSort=BlogSort(sortName=慢生活, content=慢生活，不是懒惰，放慢速度不是拖延时间，而是让我们在生活中寻找到平衡, clickCount=7, sort=49), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:59:58','2020-01-02 18:59:58'),('7f49f499c57a5666f96df87af5034029','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@3c4f71bc], 参数2:BlogVO(title=测试4, summary=测试4, content=<p>测试4</p>\n, tagUid=a9a747d944c24845815356f72723ef8e, blogSortUid=9d2019983d91490aaa758eddd7c07caf, fileUid=0b1f3c49960d6596f35bc34985b81d07, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=1, tagList=[Tag(content=前端开发, clickCount=20, sort=10)], photoList=[/blog/admin/jfif/2020/1/2/1577962320328.jfif], blogSort=BlogSort(sortName=机器学习, content=机器学习, clickCount=2, sort=16), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:53:29','2020-01-02 18:53:29'),('852d0f088dd3c9bc22603085523709f6','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@42e11dd0], 参数2:BlogVO(title=测试博客3, summary=测试博客3, content=<p>测试博客</p>\n, tagUid=5c939107ddb746b989156737805df625, blogSortUid=337806254f9c42999043de5c5ee09e77, fileUid=bb89f76717a471901a5436ddceef3143, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=2, tagList=[Tag(content=机器学习, clickCount=0, sort=22)], photoList=[/blog/admin/jfif/2020/1/2/1577962705437.jfif], blogSort=BlogSort(sortName=技术新闻, content=发现世界的每一天, clickCount=0, sort=30), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:59:21','2020-01-02 18:59:21'),('891df3960f994514138d9104e54e3517','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@76e33dd5], 参数2:BlogVO(title=测试博客2, summary=测试博客2, content=<p>测试博客2</p>\n, tagUid=7e0e93ea6cdb44ae92e58f48e6496ed7, blogSortUid=a03d7290b1c04b6eaf46659661b47032, fileUid=5529718c906473da0f7a139cb62cb9c8, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=0, tagList=[Tag(content=Java, clickCount=1, sort=30)], photoList=[/blog/admin/jfif/2020/1/2/1577962320420.jfif], blogSort=BlogSort(sortName=后端开发, content=后端开发专题, clickCount=2, sort=48), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:52:21','2020-01-02 18:52:21'),('9bab5e18aaec03a56393dccee22036f6','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/todo/add','POST','com.moxi.mogublog.admin.restapi.TodoRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@400edbea], 参数2:TodoVO(text=测试代办事项1, done=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加代办事项',1,'2020-01-02 19:00:54','2020-01-02 19:00:54'),('9cb8b93fd480b8da16c49d31ba6dc505','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@6f3da0b9], 参数2:BlogVO(title=测试博客1, summary=测试博客1, content=<p>测试博客1</p>\n, tagUid=1d1fd6d26c8e40a38637ef6126c45cd0, blogSortUid=2c93dfab0e754006866f8ed486923a41, fileUid=0b1f3c49960d6596f35bc34985b81d07, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=0, tagList=[Tag(content=Linux, clickCount=47, sort=31)], photoList=[/blog/admin/jfif/2020/1/2/1577962320328.jfif], blogSort=BlogSort(sortName=慢生活, content=慢生活，不是懒惰，放慢速度不是拖延时间，而是让我们在生活中寻找到平衡, clickCount=7, sort=49), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:52:27','2020-01-02 18:52:27'),('9e8f722f1e26e2091462032cedf2b0d5','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/picture/add','POST','com.moxi.mogublog.admin.restapi.PictureRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@bc19cd1], 参数2:0b1f3c49960d6596f35bc34985b81d07,5529718c906473da0f7a139cb62cb9c8,, 参数3:3f035952f857f7b35ef76f4fc687cd32, ','增加图片',1,'2020-01-02 18:52:00','2020-01-02 18:52:00'),('9f5e79cce1ce5590849f22fc08b2d12b','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/picture/setCover','POST','com.moxi.mogublog.admin.restapi.PictureRestApi','setCover','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@72802412], 参数2:9c06a33434352b767ebf8945dc58cf95, 参数3:3f035952f857f7b35ef76f4fc687cd32, ','通过图片Uid将图片设为封面',1,'2020-01-02 18:52:12','2020-01-02 18:52:12'),('a44fc697c6a8a33b384b83a12707b734','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/picture/delete','POST','com.moxi.mogublog.admin.restapi.PictureRestApi','delete','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@643a621e], 参数2:0e31dffaf7472da7f3a786713668a7ea,9c06a33434352b767ebf8945dc58cf95, ','删除图片',1,'2020-01-02 18:57:58','2020-01-02 18:57:58'),('a747f4f47e6fe62efd0324c7fc43df19','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@78d0e041], 参数2:BlogVO(title=测试4, summary=测试4, content=<p>测试4</p>\n, tagUid=a9a747d944c24845815356f72723ef8e, blogSortUid=9d2019983d91490aaa758eddd7c07caf, fileUid=f82156768b3a8c56b3f827b23939cd4e, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=1, tagList=[Tag(content=前端开发, clickCount=20, sort=10)], photoList=[/blog/admin/jfif/2020/1/2/1577962705270.jfif], blogSort=BlogSort(sortName=机器学习, content=机器学习, clickCount=2, sort=16), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:59:14','2020-01-02 18:59:14'),('b31ae81abcb47a99dc9a0ef62c7bbc6e','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/categoryMenu/add','POST','com.moxi.mogublog.admin.restapi.CategoryMenuRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@1cc42fca], 参数2:CategoryMenu(name=RabbitMQ, menuLevel=2, summary=RabbitMQ监控中心, icon=resource, parentUid=147cd431cbb9007bde87444d7987b151, url=/monitor/rabbitMQ, sort=null, parentCategoryMenu=null, childCategoryMenu=null), ','增加菜单',1,'2020-01-05 13:29:39','2020-01-05 13:29:39'),('b7e9a0d943ec6c8eaa49c5c2c29f41ac','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@5320e573], 参数2:BlogVO(title=测试4, summary=测试4, content=<p>测试4</p>\n, tagUid=a9a747d944c24845815356f72723ef8e, blogSortUid=9d2019983d91490aaa758eddd7c07caf, fileUid=0b1f3c49960d6596f35bc34985b81d07, adminUid=null, isPublish=1, isOriginal=1, author=null, articlesPart=null, level=0, tagList=null, photoList=[/blog/admin/jfif/2020/1/2/1577962320328.jfif], blogSort=null, praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加博客',1,'2020-01-02 18:53:22','2020-01-02 18:53:22'),('ba4e68ded9f81d0b1871b0db8a2f85eb','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/picture/add','POST','com.moxi.mogublog.admin.restapi.PictureRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@7ab9b9c3], 参数2:f82156768b3a8c56b3f827b23939cd4e,bb89f76717a471901a5436ddceef3143,, 参数3:3f035952f857f7b35ef76f4fc687cd32, ','增加图片',1,'2020-01-02 18:58:25','2020-01-02 18:58:25'),('c26acd5bb60a1f7bac5ebefa6783734c','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@150761c4], 参数2:BlogVO(title=测试博客3, summary=测试博客3, content=<p>测试博客</p>\n, tagUid=5c939107ddb746b989156737805df625, blogSortUid=337806254f9c42999043de5c5ee09e77, fileUid=5529718c906473da0f7a139cb62cb9c8, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=0, tagList=[Tag(content=Linux, clickCount=47, sort=31)], photoList=[/blog/admin/jfif/2020/1/2/1577962320420.jfif], blogSort=BlogSort(sortName=慢生活, content=慢生活，不是懒惰，放慢速度不是拖延时间，而是让我们在生活中寻找到平衡, clickCount=7, sort=49), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加博客',1,'2020-01-02 18:52:50','2020-01-02 18:52:50'),('cf0ab7647f18e1b60ad9f4c857df140c','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@1eaa8a0], 参数2:BlogVO(title=测试博客3, summary=测试博客3, content=<p>测试博客</p>\n, tagUid=5c939107ddb746b989156737805df625, blogSortUid=337806254f9c42999043de5c5ee09e77, fileUid=bb89f76717a471901a5436ddceef3143, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=3, tagList=[Tag(content=机器学习, clickCount=0, sort=22)], photoList=[/blog/admin/jfif/2020/1/2/1577962705437.jfif], blogSort=BlogSort(sortName=技术新闻, content=发现世界的每一天, clickCount=0, sort=30), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:59:28','2020-01-02 18:59:28'),('d94a7107a335dc2f8c223e08cf863c9e','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@620ebc42], 参数2:BlogVO(title=测试博客7, summary=测试博客7, content=<p>测试博客7</p>\n, tagUid=8d5ce3e0c0784b95adb7f9e7b76dca93, blogSortUid=a03d7290b1c04b6eaf46659661b47032, fileUid=0b1f3c49960d6596f35bc34985b81d07, adminUid=null, isPublish=1, isOriginal=1, author=null, articlesPart=null, level=0, tagList=null, photoList=[/blog/admin/jfif/2020/1/2/1577962320328.jfif], blogSort=null, praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加博客',1,'2020-01-02 18:55:13','2020-01-02 18:55:13'),('e124f114d8d00bcb6ce46b9ac7ea410e','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@4a4a9857], 参数2:BlogVO(title=测试博客2, summary=测试博客2, content=<p>测试博客2</p>\n, tagUid=7e0e93ea6cdb44ae92e58f48e6496ed7, blogSortUid=a03d7290b1c04b6eaf46659661b47032, fileUid=null, adminUid=null, isPublish=1, isOriginal=1, author=null, articlesPart=null, level=0, tagList=null, photoList=null, blogSort=null, praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','增加博客',1,'2020-01-02 18:47:57','2020-01-02 18:47:57'),('e83ab45d2aa8951ec8e951877a6b8025','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/categoryMenu/add','POST','com.moxi.mogublog.admin.restapi.CategoryMenuRestApi','add','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@54a119ba], 参数2:CategoryMenu(name=监控中心, menuLevel=1, summary=监控中心, icon=resource, parentUid=null, url=/monitor, sort=null, parentCategoryMenu=null, childCategoryMenu=null), ','增加菜单',1,'2020-01-05 13:25:33','2020-01-05 13:25:33'),('fec729d35e91c871414156665be661ef','admin','1f01cd1d2f474743b241d74008b12333','192.168.80.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','参数1:SecurityContextHolderAwareRequestWrapper[ org.springframework.security.web.header.HeaderWriterFilter$HeaderWriterRequest@4e0ba829], 参数2:BlogVO(title=测试博客6, summary=测试博客6, content=<p>测试博客6</p>\n, tagUid=f5d458db6a044eaebc22232efa1e3b54, blogSortUid=093d8bdd01c84890a928e923d5c235fe, fileUid=f82156768b3a8c56b3f827b23939cd4e, adminUid=1f01cd1d2f474743b241d74008b12333, isPublish=1, isOriginal=1, author=陌溪_, articlesPart=蘑菇博客, level=0, tagList=[Tag(content=深度学习, clickCount=0, sort=7)], photoList=[/blog/admin/jfif/2020/1/2/1577962705270.jfif], blogSort=BlogSort(sortName=软件推荐, content=软件推荐, clickCount=1, sort=6), praiseCount=null, copyright=null, levelKeyword=null), 参数3:org.springframework.validation.BeanPropertyBindingResult: 0 errors, ','编辑博客',1,'2020-01-02 18:58:57','2020-01-02 18:58:57');

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

insert  into `t_tag`(`uid`,`content`,`status`,`click_count`,`create_time`,`update_time`,`sort`) values ('0b51c75ed5744cdcadefe0ad947be9b6','数据库',1,54,'2019-11-16 16:40:32','2019-11-16 16:40:32',6),('1c76b9848f5f4d71a5e88b20dbaf38f4','RabbitMQ',1,15,'2019-11-16 16:40:32','2019-11-16 16:40:32',6),('1d1fd6d26c8e40a38637ef6126c45cd0','Linux',1,47,'2019-11-16 16:40:32','2019-11-16 16:40:32',31),('2a31dd6c2b1b464e9e222a1198bc739a','虚拟机',1,1,'2019-11-16 16:40:32','2019-11-16 16:40:32',2),('2f5779e877da48958c985d69b311d0d6','大数据',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',5),('3c16b9093e9b1bfddbdfcb599b23d835','Nginx',1,312,'2019-11-16 16:40:32','2019-11-16 16:40:32',1),('4cfb9e50945185847d7c55660d6bab3d','string',1,0,'2019-12-08 16:37:47','2019-12-08 16:37:47',0),('53c5a0f3142e4f54820315936f78383b','Spring Boot',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',3),('5626932d452c2ad863d9b3cb0b69d22d','学习笔记',1,33,'2019-11-16 16:40:32','2019-11-16 16:40:32',9),('5c939107ddb746b989156737805df625','机器学习',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',22),('6d35ddd5075f4c0e885ffb2e3b3a0365','Tomcat',1,454,'2019-11-16 16:40:32','2019-11-16 16:40:32',3),('7e0e93ea6cdb44ae92e58f48e6496ed7','Java',1,1,'2019-11-16 16:40:32','2019-11-16 16:40:32',30),('8c9d43de144245eb8176854eca5ae244','AI',1,44,'2019-11-16 16:40:32','2019-11-16 16:40:32',0),('8d5ce3e0c0784b95adb7f9e7b76dca93','建站系统',1,3213,'2019-11-16 16:40:32','2019-11-16 16:40:32',4),('a9a747d944c24845815356f72723ef8e','前端开发',1,20,'2019-11-16 16:40:32','2019-11-16 16:40:32',10),('ca928e8718654aa5a802e2f69277b137','面试',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',8),('d3c3fc43f38445389c970ff0732a6586','NLP',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',4),('dececd440fdc4fa28dffe6404e696dd4','Python',1,45,'2019-11-16 16:40:32','2019-11-16 16:40:32',1),('e2c7913050cf4ab9aa92902316aaf075','校园生活',1,2,'2019-11-16 16:40:32','2019-11-16 16:40:32',13),('e81bc2dca42c4031be7d66fef4a71e16','Spring Cloud',1,11,'2019-11-16 16:40:32','2019-11-16 16:40:32',4),('ebf63989f11741bc89494c52fc6bae4c','Docker',1,454,'2019-11-16 16:40:32','2019-11-16 16:40:32',2),('f5d458db6a044eaebc22232efa1e3b54','深度学习',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',7),('f90d3c2fd9434302951130e897a89164','Vue',1,15,'2019-11-16 16:40:32','2019-11-16 16:40:32',6),('fb72516226474cf0bfa0f310bfa75426','Scala',1,0,'2019-11-16 16:40:32','2019-11-16 16:40:32',1);

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

insert  into `t_todo`(`uid`,`admin_uid`,`text`,`done`,`status`,`create_time`,`update_time`) values ('01a8b58ea3324267ae1075fd752b0759','1f01cd1d2f474743b241d74008b12333','admin必须是超级管理员，不能修改',1,0,'2019-07-19 09:21:33','2019-07-19 09:21:33'),('02f6640eb3ae492d95fdc4d688e0e903','1f01cd1d2f474743b241d74008b12333','修改Maven依赖项',1,0,'2019-07-23 11:14:20','2019-07-23 11:14:20'),('05a9906261764709a7e817a9120b8c2d','1f01cd1d2f474743b241d74008b12333','学习Vue服务器缓存',1,0,'2019-07-06 11:02:38','2019-07-06 11:02:38'),('0853381344dc4efc9cabb7a3ece703dd','1f01cd1d2f474743b241d74008b12333','下拉到某个值的时候，右边不会无限下降',1,0,'2019-07-06 11:00:24','2019-07-06 11:00:24'),('26040c961cc04ea2a831dff84fb3f1bd','1f01cd1d2f474743b241d74008b12333','增加误操作还原功能',1,0,'2019-07-05 09:59:41','2019-07-05 09:59:41'),('3aa3976df88a47538b3d6561df10ab0b','1f01cd1d2f474743b241d74008b12333','三点半看电影',1,0,'2019-06-29 13:53:11','2019-06-29 13:53:11'),('48e9dde0df1447c798edc19bd0d1822d','1f01cd1d2f474743b241d74008b12333','readme图片使用相对路径',1,0,'2019-07-13 16:21:28','2019-07-13 16:21:28'),('4b4159b3cc024637a534203548144d78','1f01cd1d2f474743b241d74008b12333','三级推荐中，手势没有变换',1,0,'2019-07-05 10:36:55','2019-07-05 10:36:55'),('5851016f274d4f53b5bc217b02a7dad6','1f01cd1d2f474743b241d74008b12333','友链点击跳转',1,0,'2019-07-23 11:17:46','2019-07-23 11:17:46'),('66fafd5f330243d1be34efb770d96d8b','1f01cd1d2f474743b241d74008b12333','图片管理选择封面时，还存在问题',1,0,'2019-07-18 21:33:26','2019-07-18 21:33:26'),('79a9e82cc2d44e7895774290f9551c0b','1f01cd1d2f474743b241d74008b12333','菜单管理中，父菜单无法显示',1,0,'2019-07-18 22:28:27','2019-07-18 22:28:27'),('890e95ed05ca4e91906a4db63d39db0f','1f01cd1d2f474743b241d74008b12333','相关文章出现标题过长显示异常的问题',1,0,'2019-07-05 10:32:44','2019-07-05 10:32:44'),('a3547ab8a10e47bdb5c7838b941ca336','1f01cd1d2f474743b241d74008b12333','管理员管理选择角色不要使用搜索，提升用户体验',1,0,'2019-07-19 09:23:06','2019-07-19 09:23:06'),('c092133d39344a0283bd9cfe545f0888','1f01cd1d2f474743b241d74008b12333','搜索页面搜索内容失败',1,0,'2019-07-09 22:53:03','2019-07-09 22:53:03'),('ce803bf4c6efb1b8a7b824878181ae3f','1f01cd1d2f474743b241d74008b12333','测试代办事项1',0,1,'2020-01-02 19:00:54','2020-01-02 19:00:54'),('ed2dd4f751c8eb5740b0f61d7ef394e6','1f01cd1d2f474743b241d74008b12333','测试代办事项2',0,1,'2020-01-02 19:01:00','2020-01-02 19:01:00'),('efec18f693b29c7f83c7b359fc9baf6b','1f01cd1d2f474743b241d74008b12333','123',0,0,'2019-12-18 22:31:59','2019-12-18 22:31:59'),('f73b3192a9ff4b5780f854c81078d338','1f01cd1d2f474743b241d74008b12333','阅读Embedding的作用',1,0,'2019-07-01 11:11:36','2019-07-01 11:11:36');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `pass_word` varchar(32) NOT NULL COMMENT '密码',
  `gender` tinyint(1) unsigned DEFAULT NULL COMMENT '性别(1:男2:女)',
  `avatar` varchar(100) DEFAULT NULL COMMENT '个人头像',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
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
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `source` varchar(255) DEFAULT NULL COMMENT '资料来源',
  `uuid` varchar(255) DEFAULT NULL COMMENT '平台uuid',
  `qq_number` varchar(20) DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) DEFAULT NULL COMMENT '微信号',
  `occupation` varchar(255) DEFAULT NULL COMMENT '职业',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `t_user` */

insert  into `t_user`(`uid`,`user_name`,`pass_word`,`gender`,`avatar`,`email`,`birthday`,`mobile`,`valid_code`,`summary`,`login_count`,`last_login_time`,`last_login_ip`,`status`,`create_time`,`update_time`,`nick_name`,`source`,`uuid`,`qq_number`,`we_chat`,`occupation`) values ('0b51c75ed5744cdcadefe0ad947be9b5','mogu','123465',1,'f82156768b3a8c56b3f827b23939cd4e','1595833114@qq.com','2020-01-04','13766335110','123456','我是简介',0,'2020-01-04 22:15:27','127.0.0.1',1,'2020-01-04 22:48:19','2020-01-04 22:48:19','用户名','github','123465789','1595833114','123456','Java开发');

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

insert  into `t_web_config`(`uid`,`logo`,`name`,`summary`,`keyword`,`author`,`record_num`,`start_comment`,`status`,`create_time`,`update_time`,`title`,`ali_pay`,`weixin_pay`) values ('a331e4933cf54afcbb8c0cb11ec0830e','','蘑菇博客','','蘑菇博客,蘑菇社区,蘑菇技术社区,,蘑菇IT社区,IT社区,技术社区,Java技术分享,Spring教程,开发者社区','','赣ICP备18014504号','1',1,'2018-11-18 00:15:27','2018-11-18 00:15:27','一个专注于技术分享的博客平台','1c6bec634049139bdde04fcd4f0b729d','fdc6c11c61ce4785811dc9ee7a6ad705');

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
  `os` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(255) DEFAULT NULL COMMENT '浏览器及版本',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Web访问记录表';

/*Data for the table `t_web_visit` */

insert  into `t_web_visit`(`uid`,`user_uid`,`ip`,`behavior`,`module_uid`,`other_data`,`status`,`create_time`,`update_time`,`os`,`browser`) values ('0267919e5a54850584e9196e6351c6ff',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-04 21:13:56','2020-01-04 21:13:56','Windows','Chrome-79.0.3945.88'),('0abf228a469bf684ce514daaca4d636b',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-05 13:21:28','2020-01-05 13:21:28','Windows','Chrome-79.0.3945.88'),('1bdefbea29eb9d0e17e651cfe884c9a5',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-05 09:37:23','2020-01-05 09:37:23','Windows','Chrome-79.0.3945.88'),('1e7f5d1548c4ab3eb987151a023a7b6b',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-02 20:17:24','2020-01-02 20:17:24','Windows','Chrome-79.0.3945.88'),('23daa45305b03d3175f801b1744c78ed',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-05 09:42:40','2020-01-05 09:42:40','Windows','Chrome-79.0.3945.88'),('26b49748b23dd8c87d0d708ae634e1f0',NULL,'192.168.80.1','blog_content','93dfac7ee98dc81191560b324178a7cb',NULL,1,'2020-01-05 13:21:33','2020-01-05 13:21:33','Windows','Chrome-79.0.3945.88'),('43f22a79b47076d9686f61938c899439',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-02 20:19:09','2020-01-02 20:19:09','Windows','Chrome-79.0.3945.88'),('46a20b4666d4a0ee636aecd0b1252f89',NULL,'192.168.80.1','visit_page',NULL,'INFO',1,'2020-01-05 13:21:33','2020-01-05 13:21:33','Windows','Chrome-79.0.3945.88'),('4d5a3795743faa56bf2fc8e6e243520b',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-05 13:21:31','2020-01-05 13:21:31','Windows','Chrome-79.0.3945.88'),('4efb43b542e1cb20e9e2050323723b9a',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-05 13:21:13','2020-01-05 13:21:13','Windows','Chrome-79.0.3945.88'),('54dabd5fe5781f5fde9a066c1d7e970b',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-02 20:16:43','2020-01-02 20:16:43','Windows','Chrome-79.0.3945.88'),('61553e898a892117576e705d0383b173',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-04 21:14:21','2020-01-04 21:14:21','Windows','Chrome-79.0.3945.88'),('705a798d60c7b4db4feba7d921e07624',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-02 20:15:52','2020-01-02 20:15:52','Windows','Chrome-79.0.3945.88'),('85f485a8aa9f7ece2d6b552fdeb22000',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-05 13:21:09','2020-01-05 13:21:09','Windows','Chrome-79.0.3945.88'),('9807ba7a1ccf92d190bf82e057be24db',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-02 20:16:58','2020-01-02 20:16:58','Windows','Chrome-79.0.3945.88'),('9d6dd07251bd3b172c714b506da71460',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-04 21:13:02','2020-01-04 21:13:02','Windows','Chrome-79.0.3945.88'),('b7376508ab056a41c948927eb36dd0d4',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-02 20:14:41','2020-01-02 20:14:41','Windows','Chrome-79.0.3945.88'),('bfffa98cdd71b91f8c36d1f314de53e7',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-02 22:21:40','2020-01-02 22:21:40','Windows','Chrome-79.0.3945.88'),('c37929ae669902cc6fa427282a567987',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-04 21:13:38','2020-01-04 21:13:38','Windows','Chrome-79.0.3945.88'),('c97692239b8eec1608fb4ab7ac62005e',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-04 21:22:00','2020-01-04 21:22:00','Windows','Chrome-79.0.3945.88'),('cbefe49c491cc10e54c42ed6e16767ea',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-02 20:19:11','2020-01-02 20:19:11','Windows','Chrome-79.0.3945.88'),('d1ca4300bd2d69944684bc1faf029899',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-02 20:04:37','2020-01-02 20:04:37','Windows','Chrome-79.0.3945.88'),('e6c45eb83e63b7777e8cca7bc1534aba',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-04 21:13:11','2020-01-04 21:13:11','Windows','Chrome-79.0.3945.88'),('fc24799841651cc21844678586130db4',NULL,'192.168.80.1','visit_page',NULL,'INDEX',1,'2020-01-02 20:04:23','2020-01-02 20:04:23','Windows','Chrome-79.0.3945.88');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
