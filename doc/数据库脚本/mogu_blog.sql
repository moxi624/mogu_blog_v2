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
  `email` varchar(255) NOT NULL COMMENT '邮箱',
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
  `role_uid` varchar(32) DEFAULT NULL COMMENT '拥有的角色uid',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

/*Data for the table `t_admin` */

insert  into `t_admin`(`uid`,`user_name`,`pass_word`,`gender`,`avatar`,`email`,`birthday`,`mobile`,`valid_code`,`summary`,`login_count`,`last_login_time`,`last_login_ip`,`status`,`create_time`,`update_time`,`nick_name`,`qq_number`,`we_chat`,`occupation`,`github`,`gitee`,`role_uid`) values ('1f01cd1d2f474743b241d74008b12333','admin','$2a$10$BG.heUxePkV6Brr9/J5OxOF7fktPBc1R.9mnRX1bAmF3h4hvXfcw6','1',',81a5d814b81dff62181d048772ba0293','1595833114@qq.com','2018-09-29',' ',NULL,'一个95后！正在潜心研究机器学习和Java后端技术，一边学习一边积累经验',810,'2020-01-30 22:04:52','10.0.75.1',1,'2018-09-08 19:05:05','2018-11-14 14:47:00','陌溪_','1595833114','','Java开发','https://github.com/moxi624','https://gitee.com/moxi159753','434994947c5a4ee3a710cd277357c7c3');

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
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序字段',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博客表';

/*Data for the table `t_blog` */

insert  into `t_blog`(`uid`,`title`,`summary`,`content`,`tag_uid`,`click_count`,`collect_count`,`file_uid`,`status`,`create_time`,`update_time`,`admin_uid`,`is_original`,`author`,`articles_part`,`blog_sort_uid`,`level`,`is_publish`,`sort`) values ('0c3665bfbe334dc122f024e9dc405387','测试博客3','测试博客3','<p>测试博客3</p>\n','5c4c541e600ff422ccb371ee788f59d6',1,0,'b32d984f095394c91bb866fe926f366a',1,'2020-01-30 22:09:26','2020-01-30 22:09:26','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',2,'1',0),('41862e8f8c091652364a4a6ebaff7d5e','测试博客10','测试博客10','<p>测试博客10</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',1,0,'c54296fcb729599afec1d94d261a6229',1,'2020-01-30 22:11:21','2020-01-30 22:11:21','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',1,'1',0),('4a80e77576c2972543b8d4d7ddbfb61a','测试博客1','测试博客1','<p>测试博客1</p>\n','5c4c541e600ff422ccb371ee788f59d6',1,0,'dd34fc59ff945ca781cd46c23f7d7b65',1,'2020-01-30 22:08:56','2020-01-30 22:08:56','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',1,'1',0),('5177f5f90e8a62a185dbe9c3e9c275f8','测试博客9','测试博客9','<p>测试博客9</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',1,0,'b32d984f095394c91bb866fe926f366a',1,'2020-01-30 22:11:08','2020-01-30 22:11:08','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('6ea38cd43001d0df3981534cb21536b3','测试博客2','测试博客2','<p>测试博客2</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',1,0,'c54296fcb729599afec1d94d261a6229',1,'2020-01-30 22:09:12','2020-01-30 22:09:12','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',2,'1',0),('9121cf89a93d298005e0833cb0893e69','测试博客8','测试博客8','<p>测试博客8</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',1,0,'dd34fc59ff945ca781cd46c23f7d7b65',1,'2020-01-30 22:10:56','2020-01-30 22:10:56','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('935867d808f0e7ebe51fca94f7184711','测试博客7','测试博客7','<p>测试博客7</p>\n','5c4c541e600ff422ccb371ee788f59d6',1,0,'b32d984f095394c91bb866fe926f366a',1,'2020-01-30 22:10:42','2020-01-30 22:10:42','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('acb315de23375abb97d2952f3635e8fb','测试博客5','测试博客5','<p>测试博客5</p>\n','5c4c541e600ff422ccb371ee788f59d6',1,0,'c54296fcb729599afec1d94d261a6229',1,'2020-01-30 22:10:12','2020-01-30 22:10:12','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('cda0a2b70d676534611eafafa8a6cbd4','测试博客6','测试博客6','<p>测试博客6</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',1,0,'c54296fcb729599afec1d94d261a6229',1,'2020-01-30 22:10:28','2020-01-30 22:10:28','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('cdb5c5b17d8478a7171c59186a637316','测试博客4','测试博客4','<p>测试博客4</p>\n','5c4c541e600ff422ccb371ee788f59d6',1,0,'c54296fcb729599afec1d94d261a6229',1,'2020-01-30 22:09:40','2020-01-30 22:09:40','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',3,'1',0);

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

insert  into `t_blog_sort`(`uid`,`sort_name`,`content`,`create_time`,`update_time`,`status`,`sort`,`click_count`) values ('6a1c7a50c0e7b8e8657949bf02d5d0ca','测试分类','测试分类','2020-01-30 22:06:54','2020-01-30 22:06:54',1,0,0);

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

insert  into `t_category_menu`(`uid`,`name`,`menu_level`,`summary`,`parent_uid`,`url`,`icon`,`sort`,`status`,`create_time`,`update_time`) values ('02ea2f9ef5d44f559fb66189b05f6769','Solr',2,'Solr监控中心','147cd431cbb9007bde87444d7987b151','/monitor/solr','search',0,1,'2018-11-30 03:55:39','2018-11-30 03:55:39'),('079f0cfdb7a7017d827f5c349983eebc','Eureka',2,'Eureka监控中心','147cd431cbb9007bde87444d7987b151','/monitor/eureka','authority',0,1,'2020-01-06 05:27:30','2020-01-06 05:27:30'),('0a035547bbec404eb3ee0ef43312148d','分类管理',2,'管理博客分类','49b42250abcb47ff876bad699cf34f03','/blog/blogSort','sort',10,1,'2018-11-26 03:07:14','2018-11-26 03:07:14'),('147cd431cbb9007bde87444d7987b151','监控中心',1,'监控中心',NULL,'/monitor','monitor',0,1,'2020-01-06 13:25:32','2020-01-06 13:25:32'),('1d9a5030142e9fd7690f554c20e3bc90','推荐管理',2,'博客推荐管理','49b42250abcb47ff876bad699cf34f03','/blog/blogRecommend','example',0,1,'2020-01-28 10:06:32','2020-01-28 10:06:32'),('1f01cd1d2f474743b241d74008b12333','博客管理',2,'对博客进行增删改查','49b42250abcb47ff876bad699cf34f03','/blog/blog','edit',11,1,'2018-11-27 03:47:07','2018-11-27 03:47:07'),('2de247af3b0a459095e937d7ab9f5864','管理员管理',2,'管理员增删改查','d3a19221259d439b916f475e43edb13d','/authority/admin','user',0,1,'2018-11-25 19:09:21','2018-11-25 19:09:21'),('2fb47d3b6dbd44279c8206740a263543','网站配置',2,'网站配置','badf0010422b432ba6ec9c83a25012ed','/system/webConfig','web',0,1,'2018-11-28 19:59:04','2018-11-28 19:59:04'),('407a263eb12eff5aac31e9f62901cea0','Markdown',2,'Markdown编辑器','f4697cdf85920369179b90ff45a5982d','/test/Markdown','example',0,1,'2020-01-30 10:36:43','2020-01-30 10:36:43'),('4337f63d13d84b9aba64b9d7a69fd066','异常日志',2,'异常日志','98b82be8785e41dc939b6a5517fdfa53','/log/exceptionLog','exception',0,1,'2018-11-28 20:01:36','2018-11-28 20:01:36'),('49b42250abcb47ff876bad699cf34f03','博客管理',1,'用于博客的一些相关操作',NULL,'/blog','edit',20,1,'2018-11-25 05:15:07','2018-11-25 05:15:07'),('4dea9c4f39d2480983e8c4333d35e036','图片类别管理',2,'图片类别管理','65e22f3d36d94bcea47478aba02895a1','/picture/pictureSort','picture',0,1,'2018-11-28 19:50:31','2018-11-28 19:50:31'),('5010ae46511e4c0b9f30d1c63ad3f0c1','角色管理',2,'管理用户角色信息','d3a19221259d439b916f475e43edb13d','/authority/role','peoples',0,1,'2018-11-25 19:10:34','2018-11-25 19:10:34'),('510483ce569b4fc88299f346147b1314','资源管理',1,'资源管理','','/resource','resource',2,1,'2018-11-28 19:42:13','2018-11-28 19:42:13'),('6228ff4e9ebd42c89599b322201a0345','反馈管理',2,'反馈管理','bcf4a9bc21c14b559bcb015fb7912266','/message/feedback','table',0,1,'2018-11-28 19:48:30','2018-11-28 19:48:30'),('65e22f3d36d94bcea47478aba02895a1','图片管理',1,'图片管理','','/picture','example',3,1,'2018-11-28 19:48:53','2018-11-28 19:48:53'),('6606b7e646d545e5a25c70b5e5fade9f','标签管理',2,'对博客标签进行管理','49b42250abcb47ff876bad699cf34f03','/blog/blogTag','tag',4,1,'2018-11-26 02:57:38','2018-11-26 02:57:38'),('78ab104b123f4950af14d65798afb756','收藏管理',2,'管理用户收藏','49b42250abcb47ff876bad699cf34f03','/blog/collect','example',8,1,'2018-11-25 19:07:48','2018-11-25 19:07:48'),('78f24799307cb63bc3759413dadf4d1a','系统配置',2,'设置七牛云和邮箱等相关配置','badf0010422b432ba6ec9c83a25012ed','/system/systemConfig','web',0,1,'2020-01-21 09:29:04','2020-01-21 09:29:04'),('9002d1ae905c4cb79c2a485333dad2f7','友情链接',2,'友情链接','badf0010422b432ba6ec9c83a25012ed','/system/blogLink','blogLink',0,1,'2018-11-29 03:56:35','2018-11-29 03:56:35'),('93f7fd9a6e81735c47649e6b36042b5d','Druid',2,'Druid监控中心','147cd431cbb9007bde87444d7987b151','/monitor/druid','sql',0,1,'2020-01-06 13:26:51','2020-01-06 13:26:51'),('9449ce5dd5e24b21a9d15f806cb36e87','分类管理',2,'分类管理','510483ce569b4fc88299f346147b1314','/resource/resourceSort','sort',0,1,'2018-11-29 03:43:27','2018-11-29 03:43:27'),('98b82be8785e41dc939b6a5517fdfa53','操作日志',1,'操作日志','','/log','log',9,1,'2018-11-28 20:00:19','2018-11-28 20:00:19'),('9beb7caa2c844b36a02789262dc76fbe','评论管理',2,'评论管理','bcf4a9bc21c14b559bcb015fb7912266','/message/comment','table',1,1,'2018-11-28 19:47:23','2018-11-28 19:47:23'),('9e91b4f993c946cba4bf720b2c1b2e90','用户日志',2,'用户Web端访问情况','98b82be8785e41dc939b6a5517fdfa53','/log/webVisit','user1',0,1,'2019-05-17 10:16:47','2019-05-17 10:16:47'),('a5902692a3ed4fd794895bf634f97b8e','操作日志',2,'操作日志','98b82be8785e41dc939b6a5517fdfa53','/log/log','log',0,1,'2018-11-28 20:01:02','2018-11-28 20:01:02'),('a9396f1a3fbdec3d4cb614f388a22bea','SpringBoot',2,'SpringBootAdmin监控中心','147cd431cbb9007bde87444d7987b151','/monitor/springBootAdmin','system',0,1,'2020-01-05 21:30:16','2020-01-05 21:30:16'),('aa225cdae6464bc0acebd732192f8362','菜单管理',2,'对页面菜单进行管理','d3a19221259d439b916f475e43edb13d','/authority/categoryMenu','example',0,1,'2018-11-25 11:12:01','2018-11-25 11:12:01'),('acbb5d09da25e6c9e019cc361b35d159','Search接口',2,'Search接口','baace3dc03d34c54b81761dce8243814','/restapi/searchRestApi','example',0,1,'2020-01-19 19:56:23','2020-01-19 19:56:23'),('b511cae571834971a392ae4779270034','游客管理',2,'游客管理','c519725da92b42f3acf0cc9fad58c664','/user/visitor','table',2,1,'2018-11-28 19:54:28','2018-11-28 19:54:28'),('baace3dc03d34c54b81761dce8243814','接口管理',1,'接口管理','','/restapi','restapi',4,1,'2018-11-28 20:01:57','2018-11-28 20:01:57'),('badf0010422b432ba6ec9c83a25012ed','系统管理',1,'系统管理','','/system','system',19,1,'2018-11-28 19:54:47','2018-11-28 19:54:47'),('bcf4a9bc21c14b559bcb015fb7912266','消息管理',1,'消息管理','','/message','message1',6,1,'2018-11-28 19:45:29','2018-11-28 19:45:29'),('bfc9463e59a3ca250dcfc1c86627e034','ElasticSearch',2,'ElasticSearch监控页面','147cd431cbb9007bde87444d7987b151','/monitor/elasticSearch','example',0,1,'2020-01-15 22:58:00','2020-01-15 22:58:00'),('c519725da92b42f3acf0cc9fad58c664','用户管理',1,'用户管理','','/user','user1',15,1,'2018-11-28 19:51:47','2018-11-28 19:51:47'),('cbd7ba11c1b38c66b569405ed9185f35','RabbitMQ',2,'RabbitMQ监控中心','147cd431cbb9007bde87444d7987b151','/monitor/rabbitMQ','rabbitMq',0,1,'2020-01-05 21:29:39','2020-01-05 21:29:39'),('d3a19221259d439b916f475e43edb13d','权限管理',1,'对管理员权限分配进行管理','','/authority','authority',18,1,'2018-11-25 19:08:42','2018-11-25 19:08:42'),('d4d92c53d3614d00865e9219b8292a90','Picture接口',2,'Picture接口','baace3dc03d34c54b81761dce8243814','/restapi/pictureRestApi','table',0,1,'2018-11-28 20:04:33','2018-11-28 20:04:33'),('e4a482c089d04a30b6ecbaadb81b70f8','Admin接口',2,'Admin接口','baace3dc03d34c54b81761dce8243814','/restapi/adminRestApi','table',0,1,'2018-11-28 20:03:32','2018-11-28 20:03:32'),('f3a559635f9d46ee3356d072f5896fcb','图片裁剪',2,'用于图片裁剪','f4697cdf85920369179b90ff45a5982d','/test/CropperPicture','example',0,1,'2020-01-30 10:38:09','2020-01-30 10:38:09'),('f4697cdf85920369179b90ff45a5982d','测试页面',1,'用于一些功能的测试',NULL,'/test','example',17,1,'2020-01-30 10:36:00','2020-01-30 10:36:00'),('f9276eb8e3274c8aa05577c86e4dc8c1','Web接口',2,'Web接口','baace3dc03d34c54b81761dce8243814','/restapi/webRestApi','table',0,1,'2018-11-28 20:04:52','2018-11-28 20:04:52'),('faccfe476b89483791c05019ad5b4906','关于我',2,'关于我','badf0010422b432ba6ec9c83a25012ed','/system/aboutMe','aboutMe',0,1,'2018-11-29 03:55:17','2018-11-29 03:55:17'),('fb4237a353d0418ab42c748b7c1d64c6','用户管理',2,'用户管理','c519725da92b42f3acf0cc9fad58c664','/user/user','table',1,1,'2018-11-28 19:52:20','2018-11-28 19:52:20'),('ffc6e9ca2cc243febf6d2f476b849163','视频管理',2,'视频管理','510483ce569b4fc88299f346147b1314','/resource/studyVideo','table',0,1,'2018-11-28 19:43:50','2018-11-28 19:43:50');

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
  `content` varchar(225) DEFAULT NULL COMMENT '评论内容',
  `blog_uid` varchar(32) DEFAULT NULL COMMENT '博客uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `source` varchar(255) NOT NULL COMMENT '评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';

/*Data for the table `t_comment` */

insert  into `t_comment`(`uid`,`user_uid`,`to_uid`,`to_user_uid`,`content`,`blog_uid`,`status`,`create_time`,`update_time`,`source`) values ('2a48d0f7213b701a4397208c4d1b111f','865f7c001a6186507b3723701ff724d6',NULL,NULL,'测试评论1',NULL,1,'2020-01-30 22:16:44','2020-01-30 22:16:44','MESSAGE_BOARD'),('988dd05387f99ec69c464181495a81c4','865f7c001a6186507b3723701ff724d6',NULL,NULL,'测试评论2',NULL,1,'2020-01-30 22:16:49','2020-01-30 22:16:49','MESSAGE_BOARD');

/*Table structure for table `t_comment_report` */

DROP TABLE IF EXISTS `t_comment_report`;

CREATE TABLE `t_comment_report` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `user_uid` varchar(32) DEFAULT NULL COMMENT '举报人uid',
  `report_comment_uid` varchar(32) DEFAULT NULL COMMENT '被举报的评论Uid',
  `report_user_uid` varchar(32) DEFAULT NULL COMMENT '被举报的用户uid',
  `content` varchar(1000) DEFAULT NULL COMMENT '举报的原因',
  `progress` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '进展状态: 0 未查看   1: 已查看  2：已处理',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论举报表';

/*Data for the table `t_comment_report` */

/*Table structure for table `t_exception_log` */

DROP TABLE IF EXISTS `t_exception_log`;

CREATE TABLE `t_exception_log` (
  `uid` varchar(32) NOT NULL COMMENT '唯一uid',
  `exception_json` mediumtext COMMENT '异常对象json格式',
  `exception_message` mediumtext COMMENT '异常信息',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `ip` varchar(20) DEFAULT NULL COMMENT 'ip地址',
  `ip_source` varchar(100) DEFAULT NULL COMMENT 'ip来源',
  `method` varchar(255) DEFAULT NULL COMMENT '请求方法',
  `operation` varchar(100) DEFAULT NULL COMMENT '方法描述',
  `params` longtext COMMENT '请求参数',
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

insert  into `t_link`(`uid`,`title`,`summary`,`url`,`click_count`,`create_time`,`update_time`,`status`,`sort`) values ('5217d7212f9d487eab13deadca961402','博客Gitee','蘑菇博客Gitee','https://gitee.com/moxi159753/mogu_blog_v2',23,'2018-12-17 02:25:11','2018-12-17 02:25:11',1,0),('8eff079bd3857879daf8401c52d4a2de','博客Github','蘑菇博客Github','https://github.com/moxi624/mogu_blog_v2',5,'2019-12-06 20:50:05','2019-12-06 20:50:05',1,1),('dcc01149be71492dabd55821c22f6061','Mybatis-plus','MyBatis-Plus 为简化开发而生','http://mp.baomidou.com/',12,'2018-09-27 02:52:58','2018-09-27 02:52:58',1,0);

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

insert  into `t_picture`(`uid`,`file_uid`,`pic_name`,`picture_sort_uid`,`status`,`create_time`,`update_time`) values ('1679f363cf3f2a1c70e397339f6bf28f','c54296fcb729599afec1d94d261a6229',NULL,'481b95ba5cce396c9ec14544b0784751',1,'2020-01-30 22:08:26','2020-01-30 22:08:26'),('aa56a5e805d619ae79e88d9ba5fa5b87','b32d984f095394c91bb866fe926f366a',NULL,'481b95ba5cce396c9ec14544b0784751',1,'2020-01-30 22:08:26','2020-01-30 22:08:26'),('c8e267edce391e23e357f3528fa21e72','dd34fc59ff945ca781cd46c23f7d7b65',NULL,'481b95ba5cce396c9ec14544b0784751',1,'2020-01-30 22:08:26','2020-01-30 22:08:26');

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

insert  into `t_picture_sort`(`uid`,`file_uid`,`name`,`status`,`create_time`,`update_time`,`parent_uid`,`sort`) values ('481b95ba5cce396c9ec14544b0784751','null','测试图片',1,'2020-01-30 22:08:10','2020-01-30 22:08:10',NULL,0);

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

insert  into `t_role`(`uid`,`role_name`,`create_time`,`update_time`,`status`,`summary`,`category_menu_uids`) values ('434994947c5a4ee3a710cd277357c7c3','超级管理员','2018-10-16 07:56:26','2018-10-16 07:56:35',1,'超级管理员，管理全部菜单和功能','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"1d9a5030142e9fd7690f554c20e3bc90\",\"c519725da92b42f3acf0cc9fad58c664\",\"b511cae571834971a392ae4779270034\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"badf0010422b432ba6ec9c83a25012ed\",\"2fb47d3b6dbd44279c8206740a263543\",\"78f24799307cb63bc3759413dadf4d1a\",\"9002d1ae905c4cb79c2a485333dad2f7\",\"faccfe476b89483791c05019ad5b4906\",\"98b82be8785e41dc939b6a5517fdfa53\",\"4337f63d13d84b9aba64b9d7a69fd066\",\"9e91b4f993c946cba4bf720b2c1b2e90\",\"a5902692a3ed4fd794895bf634f97b8e\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"d3a19221259d439b916f475e43edb13d\",\"2de247af3b0a459095e937d7ab9f5864\",\"5010ae46511e4c0b9f30d1c63ad3f0c1\",\"aa225cdae6464bc0acebd732192f8362\",\"baace3dc03d34c54b81761dce8243814\",\"acbb5d09da25e6c9e019cc361b35d159\",\"d4d92c53d3614d00865e9219b8292a90\",\"e4a482c089d04a30b6ecbaadb81b70f8\",\"f9276eb8e3274c8aa05577c86e4dc8c1\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\",\"510483ce569b4fc88299f346147b1314\",\"9449ce5dd5e24b21a9d15f806cb36e87\",\"ffc6e9ca2cc243febf6d2f476b849163\",\"02ea2f9ef5d44f559fb66189b05f6769\",\"079f0cfdb7a7017d827f5c349983eebc\",\"93f7fd9a6e81735c47649e6b36042b5d\",\"a9396f1a3fbdec3d4cb614f388a22bea\",\"cbd7ba11c1b38c66b569405ed9185f35\",\"f4697cdf85920369179b90ff45a5982d\",\"407a263eb12eff5aac31e9f62901cea0\",\"f3a559635f9d46ee3356d072f5896fcb\"]'),('434994947c5a4ee3a710cd277357c7c4','文章管理员','2018-10-15 07:56:21','2018-10-15 07:56:23',1,'管理文章','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\"]'),('d105da79260f4d6a8a03571e4a2b17bc','一般管理员','2019-05-29 00:43:26','2019-05-29 00:43:26',1,'一般管理员','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"c519725da92b42f3acf0cc9fad58c664\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"b511cae571834971a392ae4779270034\",\"badf0010422b432ba6ec9c83a25012ed\",\"02ea2f9ef5d44f559fb66189b05f6769\",\"2fb47d3b6dbd44279c8206740a263543\",\"9002d1ae905c4cb79c2a485333dad2f7\",\"faccfe476b89483791c05019ad5b4906\",\"98b82be8785e41dc939b6a5517fdfa53\",\"4337f63d13d84b9aba64b9d7a69fd066\",\"9e91b4f993c946cba4bf720b2c1b2e90\",\"a5902692a3ed4fd794895bf634f97b8e\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"baace3dc03d34c54b81761dce8243814\",\"d4d92c53d3614d00865e9219b8292a90\",\"e4a482c089d04a30b6ecbaadb81b70f8\",\"f9276eb8e3274c8aa05577c86e4dc8c1\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\"]');

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
  `params` longtext COMMENT '请求参数',
  `operation` varchar(32) DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `ip_source` varchar(255) DEFAULT NULL COMMENT 'ip来源',
  `spend_time` int(11) DEFAULT '0' COMMENT '方法请求花费的时间',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_sys_log` */

insert  into `t_sys_log`(`uid`,`user_name`,`admin_uid`,`ip`,`url`,`type`,`class_path`,`method`,`params`,`operation`,`status`,`create_time`,`update_time`,`ip_source`,`spend_time`) values ('1491f56841c977ab3cde4499b96e64ed','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','{\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客6</p>\\n\",\"fileUid\":\"c54296fcb729599afec1d94d261a6229\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":0,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393306072.jfif\"],\"summary\":\"测试博客6\",\"tagUid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"title\":\"测试博客6\",\"useSort\":0}','增加博客',1,'2020-01-30 22:10:28','2020-01-30 22:10:28','XX|XX|内网IP|内网IP',119),('1ccb0e73367c823273ba283b485dd3bb','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','{\"adminUid\":\"1f01cd1d2f474743b241d74008b12333\",\"articlesPart\":\"蘑菇博客\",\"author\":\"陌溪_\",\"blogSort\":{\"clickCount\":0,\"content\":\"测试分类\",\"createTime\":1580422014000,\"sort\":0,\"sortName\":\"测试分类\",\"status\":1,\"uid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"updateTime\":1580422014000},\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客3</p>\\n\",\"fileUid\":\"b32d984f095394c91bb866fe926f366a\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":2,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393305973.jfif\"],\"sort\":0,\"status\":1,\"summary\":\"测试博客3\",\"tagList\":[{\"clickCount\":0,\"content\":\"测试标签\",\"createTime\":1580422023000,\"sort\":0,\"status\":1,\"uid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"updateTime\":1580422023000}],\"tagUid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"title\":\"测试博客3\",\"uid\":\"0c3665bfbe334dc122f024e9dc405387\",\"useSort\":0}','编辑博客',1,'2020-01-30 22:09:57','2020-01-30 22:09:57','XX|XX|内网IP|内网IP',111),('1d094766bdf09cd0dac99d86fb46af42','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','{\"adminUid\":\"1f01cd1d2f474743b241d74008b12333\",\"articlesPart\":\"蘑菇博客\",\"author\":\"陌溪_\",\"blogSort\":{\"clickCount\":0,\"content\":\"测试分类\",\"createTime\":1580422014000,\"sort\":0,\"sortName\":\"测试分类\",\"status\":1,\"uid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"updateTime\":1580422014000},\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客1</p>\\n\",\"fileUid\":\"dd34fc59ff945ca781cd46c23f7d7b65\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":1,\"photoList\":[\"http://localhost:8600//blog/admin/jpg/2020/1/30/1580393305054.jpg\"],\"sort\":0,\"status\":1,\"summary\":\"测试博客1\",\"tagList\":[{\"clickCount\":0,\"content\":\"测试标签\",\"createTime\":1580422023000,\"sort\":0,\"status\":1,\"uid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"updateTime\":1580422023000}],\"tagUid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"title\":\"测试博客1\",\"uid\":\"4a80e77576c2972543b8d4d7ddbfb61a\",\"useSort\":0}','编辑博客',1,'2020-01-30 22:09:49','2020-01-30 22:09:49','XX|XX|内网IP|内网IP',139),('365b704c449b957a6b2dda7ecc8e84e3','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/tag/add','POST','com.moxi.mogublog.admin.restapi.TagRestApi','add','{\"content\":\"测试标签2\"}','增加标签',1,'2020-01-30 22:07:10','2020-01-30 22:07:10','XX|XX|内网IP|内网IP',50),('3f1fdf2b5e5e449c7759f6d6bcfe643f','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','{\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客8</p>\\n\",\"fileUid\":\"dd34fc59ff945ca781cd46c23f7d7b65\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":0,\"photoList\":[\"http://localhost:8600//blog/admin/jpg/2020/1/30/1580393305054.jpg\"],\"summary\":\"测试博客8\",\"tagUid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"title\":\"测试博客8\",\"useSort\":0}','增加博客',1,'2020-01-30 22:10:56','2020-01-30 22:10:56','XX|XX|内网IP|内网IP',51),('4b8ddeb0586ef88c27e8c9c8649fd8e5','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blogSort/add','POST','com.moxi.mogublog.admin.restapi.BlogSortRestApi','add','{\"content\":\"测试分类\",\"sortName\":\"测试分类\"}','增加博客分类',1,'2020-01-30 22:06:54','2020-01-30 22:06:54','XX|XX|内网IP|内网IP',155),('4eb19c39aeea50f766f7940809f06071','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','{\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客3</p>\\n\",\"fileUid\":\"b32d984f095394c91bb866fe926f366a\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":0,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393305973.jfif\"],\"summary\":\"测试博客3\",\"tagUid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"title\":\"测试博客3\",\"useSort\":0}','增加博客',1,'2020-01-30 22:09:26','2020-01-30 22:09:26','XX|XX|内网IP|内网IP',146),('56458454fc9083e2a3aa0af0e228ffd5','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','{\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客4</p>\\n\",\"fileUid\":\"c54296fcb729599afec1d94d261a6229\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":0,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393306072.jfif\"],\"summary\":\"测试博客4\",\"tagUid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"title\":\"测试博客4\",\"useSort\":0}','增加博客',1,'2020-01-30 22:09:40','2020-01-30 22:09:40','XX|XX|内网IP|内网IP',341),('5b58bcb80fc2e80e0635d7c591abfa67','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/systemConfig/editSystemConfig','POST','com.moxi.mogublog.admin.restapi.SystemConfigRestApi','editSystemConfig','{\"email\":\"mogublog@163.com\",\"emailPassword\":\"\",\"localPictureBaseUrl\":\"http://localhost:8600/\",\"picturePriority\":\"0\",\"qiNiuAccessKey\":\"\",\"qiNiuArea\":\"z2\",\"qiNiuBucket\":\"\",\"qiNiuPictureBaseUrl\":\"\",\"qiNiuSecretKey\":\"\",\"smtpPort\":\"\",\"status\":1,\"uid\":\"37d492e35dc6e3fbb9dfedfd2079a123\",\"uploadLocal\":\"1\",\"uploadQiNiu\":\"0\"}','修改系统配置',1,'2020-01-30 22:25:50','2020-01-30 22:25:50','XX|XX|内网IP|内网IP',134),('6bb7561c80d0b93d48b99c34078d7a01','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','{\"adminUid\":\"1f01cd1d2f474743b241d74008b12333\",\"articlesPart\":\"蘑菇博客\",\"author\":\"陌溪_\",\"blogSort\":{\"clickCount\":0,\"content\":\"测试分类\",\"createTime\":1580422014000,\"sort\":0,\"sortName\":\"测试分类\",\"status\":1,\"uid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"updateTime\":1580422014000},\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客10</p>\\n\",\"fileUid\":\"c54296fcb729599afec1d94d261a6229\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":1,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393306072.jfif\"],\"sort\":0,\"status\":1,\"summary\":\"测试博客10\",\"tagList\":[{\"clickCount\":0,\"content\":\"测试标签2\",\"createTime\":1580422030000,\"sort\":0,\"status\":1,\"uid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"updateTime\":1580422030000}],\"tagUid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"title\":\"测试博客10\",\"uid\":\"41862e8f8c091652364a4a6ebaff7d5e\",\"useSort\":0}','编辑博客',1,'2020-01-30 22:11:37','2020-01-30 22:11:37','XX|XX|内网IP|内网IP',131),('6cdead952b841b8476670522ba049627','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/tag/add','POST','com.moxi.mogublog.admin.restapi.TagRestApi','add','{\"content\":\"测试标签\"}','增加标签',1,'2020-01-30 22:07:03','2020-01-30 22:07:03','XX|XX|内网IP|内网IP',66),('6da192bf1131a780c08a8491ae365ff7','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/picture/add','POST','com.moxi.mogublog.admin.restapi.PictureRestApi','add',NULL,'增加图片',1,'2020-01-30 22:08:26','2020-01-30 22:08:26','XX|XX|内网IP|内网IP',153),('7066413fb1a016def874a08afd0c24a6','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/systemConfig/editSystemConfig','POST','com.moxi.mogublog.admin.restapi.SystemConfigRestApi','editSystemConfig','{\"email\":\"mogublog@163.com\",\"emailPassword\":\"\",\"localPictureBaseUrl\":\"http://localhost:8600/\",\"picturePriority\":\"0\",\"qiNiuAccessKey\":\"\",\"qiNiuArea\":\"z2\",\"qiNiuBucket\":\"\",\"qiNiuPictureBaseUrl\":\"\",\"qiNiuSecretKey\":\"\",\"smtpPort\":\"\",\"status\":1,\"uid\":\"37d492e35dc6e3fbb9dfedfd2079a123\",\"uploadLocal\":\"1\",\"uploadQiNiu\":\"0\"}','修改系统配置',1,'2020-01-30 22:06:31','2020-01-30 22:06:31','XX|XX|内网IP|内网IP',126),('72b62b1a5873247e78d429bbd594de2f','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','{\"adminUid\":\"1f01cd1d2f474743b241d74008b12333\",\"articlesPart\":\"蘑菇博客\",\"author\":\"陌溪_\",\"blogSort\":{\"clickCount\":0,\"content\":\"测试分类\",\"createTime\":1580422014000,\"sort\":0,\"sortName\":\"测试分类\",\"status\":1,\"uid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"updateTime\":1580422014000},\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客5</p>\\n\",\"fileUid\":\"c54296fcb729599afec1d94d261a6229\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":0,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393306072.jfif\"],\"sort\":0,\"status\":1,\"summary\":\"测试博客5\",\"tagList\":[{\"clickCount\":0,\"content\":\"测试标签\",\"createTime\":1580422023000,\"sort\":0,\"status\":1,\"uid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"updateTime\":1580422023000}],\"tagUid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"title\":\"测试博客5\",\"uid\":\"cdb5c5b17d8478a7171c59186a637316\",\"useSort\":0}','增加博客',1,'2020-01-30 22:10:12','2020-01-30 22:10:12','XX|XX|内网IP|内网IP',123),('8171f06cd9b22a2e6d427c9b06b16780','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','{\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客10</p>\\n\",\"fileUid\":\"c54296fcb729599afec1d94d261a6229\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":0,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393306072.jfif\"],\"summary\":\"测试博客10\",\"tagUid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"title\":\"测试博客10\",\"useSort\":0}','增加博客',1,'2020-01-30 22:11:21','2020-01-30 22:11:21','XX|XX|内网IP|内网IP',107),('ac6b370d0b02e42da247dac933310ec5','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','{\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客7</p>\\n\",\"fileUid\":\"b32d984f095394c91bb866fe926f366a\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":0,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393305973.jfif\"],\"summary\":\"测试博客7\",\"tagUid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"title\":\"测试博客7\",\"useSort\":0}','增加博客',1,'2020-01-30 22:10:42','2020-01-30 22:10:42','XX|XX|内网IP|内网IP',137),('c25a601cc9323f435ae690dfd263082b','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','{\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客1</p>\\n\",\"fileUid\":\"dd34fc59ff945ca781cd46c23f7d7b65\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":0,\"photoList\":[\"http://localhost:8600//blog/admin/jpg/2020/1/30/1580393305054.jpg\"],\"summary\":\"测试博客1\",\"tagUid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"title\":\"测试博客1\",\"useSort\":0}','增加博客',1,'2020-01-30 22:08:56','2020-01-30 22:08:56','XX|XX|内网IP|内网IP',160),('d088b94b7c3949902d86b5b8f872db3d','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/webConfig/editWebConfig','POST','com.moxi.mogublog.admin.restapi.WebConfigRestApi','editWebConfig','{\"aliPay\":\"5ce92301acca4cc5858cdb03f7389767\",\"author\":\"陌溪\",\"email\":\"1595833114@qq.com\",\"gitee\":\"https://gitee.com/moxi159753\",\"github\":\"https://github.com/moxi624\",\"keyword\":\"\\\"蘑菇博客,蘑菇社区,蘑菇技术社区,,蘑菇IT社区,IT社区,技术社区,Java技术分享,Spring教程,开发者社区\",\"logo\":\",dd34fc59ff945ca781cd46c23f7d7b65\",\"name\":\"蘑菇博客\",\"photoList\":[\"http://localhost:8600//blog/admin/jpg/2020/1/30/1580393305054.jpg\"],\"qqGroup\":\"950309755\",\"qqNumber\":\"1595833114\",\"recordNum\":\"赣ICP备18014504号\",\"startComment\":\"1\",\"status\":1,\"summary\":\"一个专注于技术分享的博客平台，大家以共同学习，乐于分享，拥抱开源的价值观进行学习交流\",\"title\":\"一个专注于技术分享的博客平台\",\"uid\":\"a331e4933cf54afcbb8c0cb11ec0830e\",\"weChat\":\"\",\"weixinPay\":\"fdc6c11c61ce4785811dc9ee7a6ad705\"}','修改网站配置',1,'2020-01-30 22:11:49','2020-01-30 22:11:49','XX|XX|内网IP|内网IP',135),('d57797e82cce55e8b36530bb60097a79','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','{\"adminUid\":\"1f01cd1d2f474743b241d74008b12333\",\"articlesPart\":\"蘑菇博客\",\"author\":\"陌溪_\",\"blogSort\":{\"clickCount\":0,\"content\":\"测试分类\",\"createTime\":1580422014000,\"sort\":0,\"sortName\":\"测试分类\",\"status\":1,\"uid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"updateTime\":1580422014000},\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客4</p>\\n\",\"fileUid\":\"c54296fcb729599afec1d94d261a6229\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":3,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393306072.jfif\"],\"sort\":0,\"status\":1,\"summary\":\"测试博客4\",\"tagList\":[{\"clickCount\":0,\"content\":\"测试标签\",\"createTime\":1580422023000,\"sort\":0,\"status\":1,\"uid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"updateTime\":1580422023000}],\"tagUid\":\"5c4c541e600ff422ccb371ee788f59d6\",\"title\":\"测试博客4\",\"uid\":\"cdb5c5b17d8478a7171c59186a637316\",\"useSort\":0}','编辑博客',1,'2020-01-30 22:10:01','2020-01-30 22:10:01','XX|XX|内网IP|内网IP',47),('e189ea31cf3af5d7cfc009ec41edd9db','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/systemConfig/editSystemConfig','POST','com.moxi.mogublog.admin.restapi.SystemConfigRestApi','editSystemConfig','{\"email\":\"mogublog@163.com\",\"emailPassword\":\"\",\"localPictureBaseUrl\":\"http://localhost:8600/\",\"picturePriority\":\"0\",\"qiNiuAccessKey\":\"\",\"qiNiuArea\":\"z2\",\"qiNiuBucket\":\"\",\"qiNiuPictureBaseUrl\":\"http://****.com\",\"qiNiuSecretKey\":\"\",\"smtpPort\":\"\",\"status\":1,\"uid\":\"37d492e35dc6e3fbb9dfedfd2079a123\",\"uploadLocal\":\"1\",\"uploadQiNiu\":\"0\"}','修改系统配置',1,'2020-01-30 22:06:23','2020-01-30 22:06:23','XX|XX|内网IP|内网IP',195),('e476908686eee28305512bd7ab35ca24','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','{\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客2</p>\\n\",\"fileUid\":\"c54296fcb729599afec1d94d261a6229\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":0,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393306072.jfif\"],\"summary\":\"测试博客2\",\"tagUid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"title\":\"测试博客2\",\"useSort\":0}','增加博客',1,'2020-01-30 22:09:12','2020-01-30 22:09:12','XX|XX|内网IP|内网IP',149),('ecef96da33e98eb9e4ba0785c98bb2c8','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/pictureSort/add','POST','com.moxi.mogublog.admin.restapi.PictureSortRestApi','add',NULL,'增加图片分类',1,'2020-01-30 22:08:10','2020-01-30 22:08:10','XX|XX|内网IP|内网IP',122),('f52462dba8ae547719914637922d8122','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/add','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','add','{\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客9</p>\\n\",\"fileUid\":\"b32d984f095394c91bb866fe926f366a\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":0,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393305973.jfif\"],\"summary\":\"测试博客9\",\"tagUid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"title\":\"测试博客9\",\"useSort\":0}','增加博客',1,'2020-01-30 22:11:08','2020-01-30 22:11:08','XX|XX|内网IP|内网IP',140),('f905d8f4c5e1e9ce1120fccd3e6e5b33','admin','1f01cd1d2f474743b241d74008b12333','10.0.75.1','/blog/edit','POST','com.moxi.mogublog.admin.restapi.BlogRestApi','edit','{\"adminUid\":\"1f01cd1d2f474743b241d74008b12333\",\"articlesPart\":\"蘑菇博客\",\"author\":\"陌溪_\",\"blogSort\":{\"clickCount\":0,\"content\":\"测试分类\",\"createTime\":1580422014000,\"sort\":0,\"sortName\":\"测试分类\",\"status\":1,\"uid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"updateTime\":1580422014000},\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客2</p>\\n\",\"fileUid\":\"c54296fcb729599afec1d94d261a6229\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":2,\"photoList\":[\"http://localhost:8600//blog/admin/jfif/2020/1/30/1580393306072.jfif\"],\"sort\":0,\"status\":1,\"summary\":\"测试博客2\",\"tagList\":[{\"clickCount\":0,\"content\":\"测试标签2\",\"createTime\":1580422030000,\"sort\":0,\"status\":1,\"uid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"updateTime\":1580422030000}],\"tagUid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"title\":\"测试博客2\",\"uid\":\"6ea38cd43001d0df3981534cb21536b3\",\"useSort\":0}','编辑博客',1,'2020-01-30 22:09:53','2020-01-30 22:09:53','XX|XX|内网IP|内网IP',120);

/*Table structure for table `t_system_config` */

DROP TABLE IF EXISTS `t_system_config`;

CREATE TABLE `t_system_config` (
  `uid` varchar(32) NOT NULL COMMENT '主键',
  `qi_niu_access_key` varchar(255) DEFAULT NULL COMMENT '七牛云公钥',
  `qi_niu_secret_key` varchar(255) DEFAULT NULL COMMENT '七牛云私钥',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱账号',
  `email_user_name` varchar(255) DEFAULT NULL COMMENT '邮箱发件人用户名',
  `email_password` varchar(255) DEFAULT NULL COMMENT '邮箱密码',
  `smtp_address` varchar(20) DEFAULT NULL COMMENT 'SMTP地址',
  `smtp_port` varchar(6) DEFAULT '1' COMMENT 'SMTP端口',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `qi_niu_bucket` varchar(255) DEFAULT NULL COMMENT '七牛云上传空间',
  `qi_niu_area` varchar(10) DEFAULT NULL COMMENT '七牛云存储区域 华东（z0），华北(z1)，华南(z2)，北美(na0)，东南亚(as0)',
  `upload_qi_niu` varchar(1) DEFAULT '1' COMMENT '图片是否上传七牛云 (0:否， 1：是)',
  `upload_local` varchar(1) DEFAULT '1' COMMENT '图片是否上传本地存储 (0:否， 1：是)',
  `picture_priority` varchar(1) DEFAULT '1' COMMENT '图片显示优先级（ 1 展示 七牛云,  0 本地）',
  `qi_niu_picture_base_url` varchar(255) DEFAULT NULL COMMENT '七牛云域名前缀：http://images.moguit.cn',
  `local_picture_base_url` varchar(255) DEFAULT NULL COMMENT '本地服务器域名前缀：http://localhost:8600',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置表';

/*Data for the table `t_system_config` */

insert  into `t_system_config`(`uid`,`qi_niu_access_key`,`qi_niu_secret_key`,`email`,`email_user_name`,`email_password`,`smtp_address`,`smtp_port`,`status`,`create_time`,`update_time`,`qi_niu_bucket`,`qi_niu_area`,`upload_qi_niu`,`upload_local`,`picture_priority`,`qi_niu_picture_base_url`,`local_picture_base_url`) values ('37d492e35dc6e3fbb9dfedfd2079a123','','','mogublog@163.com',NULL,'',NULL,'',1,'2020-01-29 19:14:26','2020-01-29 19:14:26','','z2','0','1','0','','http://localhost:8600/');

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

insert  into `t_tag`(`uid`,`content`,`status`,`click_count`,`create_time`,`update_time`,`sort`) values ('5c4c541e600ff422ccb371ee788f59d6','测试标签',1,0,'2020-01-30 22:07:03','2020-01-30 22:07:03',0),('6b0ba63beabccc91c4f8fb938984f8a3','测试标签2',1,0,'2020-01-30 22:07:10','2020-01-30 22:07:10',0);

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
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `source` varchar(255) DEFAULT NULL COMMENT '资料来源',
  `uuid` varchar(255) DEFAULT NULL COMMENT '平台uuid',
  `qq_number` varchar(20) DEFAULT NULL COMMENT 'QQ号',
  `we_chat` varchar(255) DEFAULT NULL COMMENT '微信号',
  `occupation` varchar(255) DEFAULT NULL COMMENT '职业',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `t_user` */

insert  into `t_user`(`uid`,`user_name`,`pass_word`,`gender`,`avatar`,`email`,`birthday`,`mobile`,`valid_code`,`summary`,`login_count`,`last_login_time`,`last_login_ip`,`status`,`create_time`,`update_time`,`nick_name`,`source`,`uuid`,`qq_number`,`we_chat`,`occupation`) values ('865f7c001a6186507b3723701ff724d6','moguBlog_GITHUB_18610136','051120',NULL,'f858cf4768a4dd1697002bf4abf8b58f','moxi0624@163.com',NULL,NULL,NULL,NULL,0,'2020-01-30 22:13:08','10.0.75.1',1,'2020-01-30 22:12:57','2020-01-30 22:12:57','Streamlet','GITHUB','18610136',NULL,NULL,NULL);

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
  `github` varchar(255) DEFAULT NULL COMMENT 'github地址',
  `gitee` varchar(255) DEFAULT NULL COMMENT 'gitee地址',
  `qq_number` varchar(20) DEFAULT NULL COMMENT 'QQ号',
  `qq_group` varchar(20) DEFAULT NULL COMMENT 'QQ群',
  `we_chat` varchar(255) DEFAULT NULL COMMENT '微信号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_web_config` */

insert  into `t_web_config`(`uid`,`logo`,`name`,`summary`,`keyword`,`author`,`record_num`,`start_comment`,`status`,`create_time`,`update_time`,`title`,`ali_pay`,`weixin_pay`,`github`,`gitee`,`qq_number`,`qq_group`,`we_chat`,`email`) values ('a331e4933cf54afcbb8c0cb11ec0830e',',dd34fc59ff945ca781cd46c23f7d7b65','蘑菇博客','一个专注于技术分享的博客平台，大家以共同学习，乐于分享，拥抱开源的价值观进行学习交流','\"蘑菇博客,蘑菇社区,蘑菇技术社区,,蘑菇IT社区,IT社区,技术社区,Java技术分享,Spring教程,开发者社区','陌溪','赣ICP备18014504号','1',1,'2018-11-17 08:15:27','2018-11-17 08:15:27','一个专注于技术分享的博客平台','5ce92301acca4cc5858cdb03f7389767','fdc6c11c61ce4785811dc9ee7a6ad705','https://github.com/moxi624','https://gitee.com/moxi159753','1595833114','950309755','','1595833114@qq.com');

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
  `browser` varchar(255) DEFAULT NULL COMMENT '浏览器',
  `ip_source` varchar(255) DEFAULT NULL COMMENT 'ip来源',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Web访问记录表';

/*Data for the table `t_web_visit` */

insert  into `t_web_visit`(`uid`,`user_uid`,`ip`,`behavior`,`module_uid`,`other_data`,`status`,`create_time`,`update_time`,`os`,`browser`,`ip_source`) values ('048a6142560b740046507a974adf41b4',NULL,'10.0.75.1','blog_content','935867d808f0e7ebe51fca94f7184711',NULL,1,'2020-01-30 22:10:42','2020-01-30 22:10:42','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('0e36d8bacf3517a4144e286713e2789f',NULL,'10.0.75.1','blog_content','cdb5c5b17d8478a7171c59186a637316',NULL,1,'2020-01-30 22:09:40','2020-01-30 22:09:40','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('14879342e71e2d4545a912746c56f0c9',NULL,'10.0.75.1','blog_content','0c3665bfbe334dc122f024e9dc405387',NULL,1,'2020-01-30 22:09:27','2020-01-30 22:09:27','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('19f1bbe37fdc0580b7fe64fc9804329d',NULL,'10.0.75.1','blog_content','0c3665bfbe334dc122f024e9dc405387',NULL,1,'2020-01-30 22:09:57','2020-01-30 22:09:57','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('1a837029603f62f558eb31b18ddb7448',NULL,'10.0.75.1','visit_page',NULL,'TIME',1,'2020-01-30 22:12:31','2020-01-30 22:12:31','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('1d00010b1dbdc7b16828b846bb6993f6',NULL,'10.0.75.1','blog_content','acb315de23375abb97d2952f3635e8fb',NULL,1,'2020-01-30 22:10:12','2020-01-30 22:10:12','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('2606ab54fb10992698fede123650653d',NULL,'10.0.75.1','blog_content','41862e8f8c091652364a4a6ebaff7d5e',NULL,1,'2020-01-30 22:11:37','2020-01-30 22:11:37','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('3583589070789ba2fc5561480512c815',NULL,'10.0.75.1','blog_content','9121cf89a93d298005e0833cb0893e69',NULL,1,'2020-01-30 22:10:56','2020-01-30 22:10:56','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('3c0ccdf45865c02f70000394011e081b',NULL,'10.0.75.1','visit_page',NULL,'INDEX',1,'2020-01-30 22:13:08','2020-01-30 22:13:08','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('3e833269a24b77eb4517bfccd084a4ae',NULL,'10.0.75.1','blog_content','5177f5f90e8a62a185dbe9c3e9c275f8',NULL,1,'2020-01-30 22:11:08','2020-01-30 22:11:08','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('4dcb75800db9046e4d9cf61f3e21bd52',NULL,'10.0.75.1','visit_page',NULL,'LIST',1,'2020-01-30 22:12:25','2020-01-30 22:12:25','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('60886be78a75877e25ee08beeb08a8b3',NULL,'10.0.75.1','blog_content','6ea38cd43001d0df3981534cb21536b3',NULL,1,'2020-01-30 22:09:53','2020-01-30 22:09:53','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('6d12d5efcd8abf89118c4e1ad39699bb',NULL,'10.0.75.1','blog_content','6ea38cd43001d0df3981534cb21536b3',NULL,1,'2020-01-30 22:09:12','2020-01-30 22:09:12','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('917922ab53ea58d9b8658b07f2ff809c',NULL,'10.0.75.1','blog_content','4a80e77576c2972543b8d4d7ddbfb61a',NULL,1,'2020-01-30 22:08:57','2020-01-30 22:08:57','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('96b40fe43f1fafa02eedfa793b8e594d',NULL,'10.0.75.1','blog_content','cda0a2b70d676534611eafafa8a6cbd4',NULL,1,'2020-01-30 22:10:29','2020-01-30 22:10:29','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('9bdc642790386e050df6b0bb3b7da12d',NULL,'10.0.75.1','visit_page',NULL,'ABOUT',1,'2020-01-30 22:12:27','2020-01-30 22:12:27','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('a1323a7e4e91fddf1d237d0890985237',NULL,'10.0.75.1','visit_sort',NULL,'2020年01月',1,'2020-01-30 22:12:29','2020-01-30 22:12:29','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('aa943ae567b4c71603b39d962564e85f',NULL,'10.0.75.1','blog_content','4a80e77576c2972543b8d4d7ddbfb61a',NULL,1,'2020-01-30 22:09:49','2020-01-30 22:09:49','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('ac6063a72a2f397b07119846d40dc51c',NULL,'10.0.75.1','blog_content','41862e8f8c091652364a4a6ebaff7d5e',NULL,1,'2020-01-30 22:11:21','2020-01-30 22:11:21','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('b88ea87d054dce86cd4187e73e0b71f9',NULL,'10.0.75.1','visit_classify','6a1c7a50c0e7b8e8657949bf02d5d0ca','测试分类',1,'2020-01-30 22:12:30','2020-01-30 22:12:30','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('bdd8a1ab6dc0c8bd7f34f729be736e44',NULL,'10.0.75.1','visit_page',NULL,'LIST',1,'2020-01-30 22:12:24','2020-01-30 22:12:24','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('bf4030ad0bc095ce007b6163a25288fd',NULL,'10.0.75.1','visit_page',NULL,'CLASSIFY',1,'2020-01-30 22:12:30','2020-01-30 22:12:30','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('c009bb9392df047374ad39c8ba2051a5',NULL,'10.0.75.1','blog_content','cdb5c5b17d8478a7171c59186a637316',NULL,1,'2020-01-30 22:10:02','2020-01-30 22:10:02','UnKnown, More-Info: Java/1.8.0_121','UnKnown, More-Info: Java/1.8.0_121','XX|XX|内网IP|内网IP'),('d8fc3f4f66470ca340c25addf397b23d',NULL,'10.0.75.1','visit_page',NULL,'ABOUT',1,'2020-01-30 22:12:26','2020-01-30 22:12:26','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('daadc3fb5dc8b1f9a4575470da6b4f46',NULL,'10.0.75.1','visit_page',NULL,'INDEX',1,'2020-01-30 22:11:57','2020-01-30 22:11:57','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('f0675722da1fe73fe519d185369569ec',NULL,'10.0.75.1','visit_page',NULL,'INDEX',1,'2020-01-30 22:04:45','2020-01-30 22:04:45','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP'),('f10d0c07af93a413a26a3f8136538059',NULL,'10.0.75.1','visit_page',NULL,'SORT',1,'2020-01-30 22:12:29','2020-01-30 22:12:29','Windows','Chrome-79.0.3945.88','XX|XX|内网IP|内网IP');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
