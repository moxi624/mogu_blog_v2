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
  `person_resume` text COMMENT '履历',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

/*Data for the table `t_admin` */

insert  into `t_admin`(`uid`,`user_name`,`pass_word`,`gender`,`avatar`,`email`,`birthday`,`mobile`,`valid_code`,`summary`,`login_count`,`last_login_time`,`last_login_ip`,`status`,`create_time`,`update_time`,`nick_name`,`qq_number`,`we_chat`,`occupation`,`github`,`gitee`,`role_uid`,`person_resume`) values ('1f01cd1d2f474743b241d74008b12333','admin','$2a$10$BG.heUxePkV6Brr9/J5OxOF7fktPBc1R.9mnRX1bAmF3h4hvXfcw6','1','1624a75ad0e6c223aca257e2d2bfaba0','1595833114@qq.com','2018-09-29',' ',NULL,'一个95后！正在潜心研究机器学习和Java后端技术，一边学习一边积累经验',867,'2020-02-13 10:36:31','10.0.75.1',1,'2018-09-15 03:05:05','2018-11-20 22:47:00','陌溪_','1595833114','','Java开发','https://github.com/moxi624','https://gitee.com/moxi159753','434994947c5a4ee3a710cd277357c7c3','<h2>前言</h2>\n\n<p>目前博客源码已经开源至码云和Github中，感兴趣的小伙伴可以star关注一下下~</p>\n\n<p>Gitee地址：<a href=\"https://gitee.com/moxi159753/mogu_blog_v2\">https://gitee.com/moxi159753/mogu_blog_v2</a></p>\n\n<p>Github地址：<a href=\"https://github.com/moxi624/mogu_blog_v2\">https://github.com/moxi624/mogu_blog_v2</a></p>\n\n<hr />\n<p>&nbsp;</p>\n\n<h2>关于我</h2>\n\n<p>许志翔，目前就读于桂林电子科技大学，是一名研二的学生，所属计算机信息与安全学院，研究方向是教育大数据，是一名&quot;不顾正业&quot;的研究僧，沉迷于Java和Vue技术开发，梦想着进入BATJ，也将一直为此不断努力了~</p>\n\n<p>正宗95后，爱编程，爱旅游，爱生活，爱锻炼，从刚研究生入学后，就坚持着两件事，一个是写代码，一个就是每天5公里慢跑</p>\n\n<pre>\n<code>意志力和天生才华，都是人们在事实发生了之后再去赋予某个人的优点：杰森是位不可思议的网球选手，因此，他一定生下来就具有这种才华；杰尼年复一年地练习拉小提琴，每天坚持几个小时，因此，他一定有着令人难以置信的意志力。\n\n——《刻意练习：从新手到大师》</code></pre>\n\n<p>最近可能随着研究生生涯过半，已经要着手开始写小论文的事情了，博客项目的更新估计会变的比较缓慢，应该不会存在大版本的更新迭代了</p>\n\n<p>不过目前博客项目的技术功能也编写的差不多，可能还要做的就是SEO优化，后面一段时间应该注重于网站的稳定性和BUG的解决，要是小伙伴在使用的时候，发现了什么问题，欢迎私聊我，或者在QQ群里提出~</p>\n\n<p>在今年的8、9月份，也该着手于校招了，后面应该更多的时间沉淀在Java的基础学习了，当然如果有老哥有内推名额的话，欢迎推荐我一下下....&nbsp; &nbsp;卑微.jpg&nbsp;</p>\n\n<p><img src=\"http://image.moguit.cn/52552ed0efb245a9a67d5c9928d72e14\" /></p>\n\n<p>回顾2019年，每天也会在项目中，花费一些时间去提交代码，可能有的时候是增加了新功能，有的时候是解决一个BUG，到现在已经成为了一个习惯了，因此博客项目中也添加了比较多的有趣的功能，比如这个文章贡献度</p>\n\n<p><img src=\"http://image.moguit.cn/aaff67a315c547c5964f0aebb4e8ce23\" /></p>\n\n<p>哈哈哈，其实它和码云上的代码贡献度是一样的，每次发表一篇博客，就会标记出个点，点越大说明该天发表博客越多，可能是因为自己有些强迫症的原因，不过我也希望能够借此来激励自己养成每天写博客的习惯，通过分享自己学习到的东西，来和各位IT的前辈们共同进步。</p>\n\n<p>当然闲暇之余也会玩玩游戏，有空的时候会在酒馆搓搓炉石~，玩玩农药，有喜欢的小伙伴也可以一起，虽然我贼菜</p>\n\n<hr />\n<h2>项目起因</h2>\n\n<p>本博客项目由我和几个小伙伴参与开发的，最开始的搭建蘑菇博客的初心是为了巩固和学习Java开发的一些知识，因此项目的技术选型都是比较新颖的技术，可能这些技术并不一定适用于博客系统，但是我也想着能尽可能把更多的技术融合进来，毕竟通过自己手把手的操作一遍，也能够算是入门了。我也很庆幸我成功将自己的项目开源出来了，并且坚持下来，蘑菇博客起源是2018年9月，到想在也已经度过了1年半的岁月。我也从最开始只会一点点Jsp和Servlet就出去找工作的傻小子慢慢成长了，在读研之前，我也在公司里呆过，算上来好像将近快一年，很感谢之前在公司里的同事和领导，是他们带我入门企业级的项目开发，让我养成了很多Java项目开发的规范。</p>\n\n<p>起初项目开源在码云上，没有多少人关注，所以自己也是坚持做自己的喜欢的事，有的时候是看看论文，有的时候敲敲代码，在2019年12月14日，蘑菇博客被码云推荐了，上了首页</p>\n\n<p><img src=\"http://image.moguit.cn/49865d11fd4c4b289d87bf305b2dde0a\" /></p>\n\n<p>&nbsp;</p>\n\n<p>然后项目的关注度就开始上升了，有些小伙伴就开始关注项目的运行和部署了，所以我也花费了一些时间，整理了博客的开发、运行、部署的文档，希望每个小伙伴都能够通过本项目一起学习</p>\n\n<p>目前蘑菇博客已经有300star了，很高兴大家对蘑菇博客项目的认可</p>\n\n<p><img src=\"http://image.moguit.cn/f55b31b7e00b42eda71e88105c4e147a\" /></p>\n\n<p>同时因为更新比较勤快，项目也在码云&nbsp; 博客&nbsp; &nbsp;关键字搜索的第一个，不过未来要走得路还很长，我也希望能够认识更多志同道合的小伙伴，然后一起学习和交流</p>\n\n<p><img src=\"http://image.moguit.cn/36588a0c8bf04e9bb103eac0f432bfa7\" /></p>\n');

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

insert  into `t_blog`(`uid`,`title`,`summary`,`content`,`tag_uid`,`click_count`,`collect_count`,`file_uid`,`status`,`create_time`,`update_time`,`admin_uid`,`is_original`,`author`,`articles_part`,`blog_sort_uid`,`level`,`is_publish`,`sort`) values ('0c3665bfbe334dc122f024e9dc405387','测试博客3','测试博客3','<p>测试博客3</p>\n','5c4c541e600ff422ccb371ee788f59d6',1,0,'adeaf35fdcd2d36ed143d95bc02b1cc2',1,'2020-01-30 22:09:26','2020-01-30 22:09:26','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',2,'1',1),('18515dc97b6b62f0f6bda2b56f133082','发大水','发送方','<p>法第三方</p>\n','5c4c541e600ff422ccb371ee788f59d6',0,0,'d134bdfe255b9aa8c2ce44da8297f6a6',0,'2020-02-07 10:50:15','2020-02-07 10:50:15','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('41862e8f8c091652364a4a6ebaff7d5e','测试博客10','测试博客10','<p>测试博客10</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',6,0,'d134bdfe255b9aa8c2ce44da8297f6a6',1,'2020-01-30 22:11:21','2020-01-30 22:11:21','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',1,'1',3),('4537a83843ae95bfc453d86f3e2360d0','啦啦啦','啦啦啦','<p>啦啦啦啦啦啦啦啦啦啦啦啦</p>\n','5c4c541e600ff422ccb371ee788f59d6',0,0,'70f5b89fe70a28d95e10c19c96bf2e85',0,'2020-02-07 10:59:41','2020-02-07 10:59:41','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('4a80e77576c2972543b8d4d7ddbfb61a','测试博客1','测试博客1','<p>测试博客1</p>\n','5c4c541e600ff422ccb371ee788f59d6',1,0,'7a08d34301b572a54b758f3f57809548',1,'2020-01-30 22:08:56','2020-01-30 22:08:56','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',1,'1',2),('4f0c05e15cca1f8ce2a41273b3db09ad','测试啦啦啦','测试啦啦啦','<p>测试啦啦啦测试啦啦啦</p>\n','5c4c541e600ff422ccb371ee788f59d6',0,0,'4e871894c11e04edbf2c4e22b4644730',0,'2020-02-07 10:57:49','2020-02-07 10:57:49','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('5177f5f90e8a62a185dbe9c3e9c275f8','测试博客9','测试博客9','<p>测试博客9</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',3,0,'adeaf35fdcd2d36ed143d95bc02b1cc2',1,'2020-01-30 22:11:08','2020-01-30 22:11:08','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',1,'1',4),('53181ddc4d560c2de1c591d0e2764c91','Nginx指南，学习nginx','Nginx指南，学习nginx','<p>Nginx指南，学习nginx</p>\n','5c4c541e600ff422ccb371ee788f59d6',4,0,'7a08d34301b572a54b758f3f57809548',1,'2020-02-04 16:37:38','2020-02-04 16:37:38','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('65035323d083f98f821a2508cb1e6e8e','测试博客20200207','测试博客20200207','<p>测试博客20200207</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',3,0,'adeaf35fdcd2d36ed143d95bc02b1cc2',1,'2020-02-07 10:43:07','2020-02-07 10:43:07','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('6621ad0f6edea49138277028650c1c41','测试博客55','测试博客55','<p>测试博客55测试博客55<img alt=\"\" src=\"http://localhost:8600//blog/admin/png/2020/2/10/1581343601351.png\" /></p>\n','5c4c541e600ff422ccb371ee788f59d6',4,0,'d134bdfe255b9aa8c2ce44da8297f6a6',1,'2020-02-07 10:48:36','2020-02-07 10:48:36','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('6ea38cd43001d0df3981534cb21536b3','测试博客2','测试博客2','<p>测试博客2</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',3,0,'c54296fcb729599afec1d94d261a6229',1,'2020-01-30 22:09:12','2020-01-30 22:09:12','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',2,'1',2),('9121cf89a93d298005e0833cb0893e69','测试博客8','测试博客8','<p>测试博客8</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',2,0,'7a08d34301b572a54b758f3f57809548',1,'2020-01-30 22:10:56','2020-01-30 22:10:56','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',1,'1',1),('935867d808f0e7ebe51fca94f7184711','测试博客7','测试博客7','<p>测试博客7</p>\n','5c4c541e600ff422ccb371ee788f59d6',2,0,'adeaf35fdcd2d36ed143d95bc02b1cc2',1,'2020-01-30 22:10:42','2020-01-30 22:10:42','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',3,'1',1),('acb315de23375abb97d2952f3635e8fb','测试博客5','测试博客5','<p>测试博客5</p>\n','5c4c541e600ff422ccb371ee788f59d6',1,0,'c54296fcb729599afec1d94d261a6229',1,'2020-01-30 22:10:12','2020-01-30 22:10:12','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('b4f27bed1bb1874e4b6083a67697f485','测试拉拉','测试拉拉','<p>测试拉拉</p>\n','5c4c541e600ff422ccb371ee788f59d6',0,0,NULL,0,'2020-02-07 10:55:20','2020-02-07 10:55:20','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',0,'1',0),('cda0a2b70d676534611eafafa8a6cbd4','测试博客6','测试博客6','<p>测试博客6</p>\n','6b0ba63beabccc91c4f8fb938984f8a3',3,0,'c54296fcb729599afec1d94d261a6229',1,'2020-01-30 22:10:28','2020-01-30 22:10:28','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',4,'1',0),('cdb5c5b17d8478a7171c59186a637316','测试博客4','测试博客4','<p>测试博客4</p>\n','5c4c541e600ff422ccb371ee788f59d6',2,0,'c54296fcb729599afec1d94d261a6229',1,'2020-01-30 22:09:40','2020-01-30 22:09:40','1f01cd1d2f474743b241d74008b12333','1','陌溪_','蘑菇博客','6a1c7a50c0e7b8e8657949bf02d5d0ca',3,'1',2);

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

insert  into `t_blog_sort`(`uid`,`sort_name`,`content`,`create_time`,`update_time`,`status`,`sort`,`click_count`) values ('6a1c7a50c0e7b8e8657949bf02d5d0ca','测试分类','测试分类','2020-01-30 22:06:54','2020-01-30 22:06:54',1,0,1);

/*Table structure for table `t_blog_spider` */

DROP TABLE IF EXISTS `t_blog_spider`;

CREATE TABLE `t_blog_spider` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='博客爬取表';

/*Data for the table `t_blog_spider` */

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

insert  into `t_category_menu`(`uid`,`name`,`menu_level`,`summary`,`parent_uid`,`url`,`icon`,`sort`,`status`,`create_time`,`update_time`) values ('02ea2f9ef5d44f559fb66189b05f6769','Solr',2,'Solr监控中心','147cd431cbb9007bde87444d7987b151','/monitor/solr','search',0,1,'2018-11-30 03:55:39','2018-11-30 03:55:39'),('079f0cfdb7a7017d827f5c349983eebc','Eureka',2,'Eureka监控中心','147cd431cbb9007bde87444d7987b151','/monitor/eureka','authority',0,1,'2020-01-06 05:27:30','2020-01-06 05:27:30'),('0a035547bbec404eb3ee0ef43312148d','分类管理',2,'管理博客分类','49b42250abcb47ff876bad699cf34f03','/blog/blogSort','sort',10,1,'2018-11-26 03:07:14','2018-11-26 03:07:14'),('147cd431cbb9007bde87444d7987b151','监控中心',1,'监控中心',NULL,'/monitor','monitor',0,1,'2020-01-06 13:25:32','2020-01-06 13:25:32'),('1d9a5030142e9fd7690f554c20e3bc90','推荐管理',2,'博客推荐管理','49b42250abcb47ff876bad699cf34f03','/blog/blogRecommend','example',0,1,'2020-01-28 10:06:32','2020-01-28 10:06:32'),('1f01cd1d2f474743b241d74008b12333','博客管理',2,'对博客进行增删改查','49b42250abcb47ff876bad699cf34f03','/blog/blog','edit',11,1,'2018-11-27 03:47:07','2018-11-27 03:47:07'),('2de247af3b0a459095e937d7ab9f5864','管理员管理',2,'管理员增删改查','d3a19221259d439b916f475e43edb13d','/authority/admin','user',0,1,'2018-11-25 19:09:21','2018-11-25 19:09:21'),('2fb47d3b6dbd44279c8206740a263543','网站配置',2,'网站配置','badf0010422b432ba6ec9c83a25012ed','/system/webConfig','web',0,1,'2018-11-28 19:59:04','2018-11-28 19:59:04'),('407a263eb12eff5aac31e9f62901cea0','Markdown',2,'Markdown编辑器','f4697cdf85920369179b90ff45a5982d','/test/Markdown','example',0,1,'2020-01-30 10:36:43','2020-01-30 10:36:43'),('4337f63d13d84b9aba64b9d7a69fd066','异常日志',2,'异常日志','98b82be8785e41dc939b6a5517fdfa53','/log/exceptionLog','exception',0,1,'2018-11-28 20:01:36','2018-11-28 20:01:36'),('49b42250abcb47ff876bad699cf34f03','博客管理',1,'用于博客的一些相关操作',NULL,'/blog','edit',20,1,'2018-11-25 05:15:07','2018-11-25 05:15:07'),('4dea9c4f39d2480983e8c4333d35e036','图片类别管理',2,'图片类别管理','65e22f3d36d94bcea47478aba02895a1','/picture/pictureSort','picture',0,1,'2018-11-28 19:50:31','2018-11-28 19:50:31'),('5010ae46511e4c0b9f30d1c63ad3f0c1','角色管理',2,'管理用户角色信息','d3a19221259d439b916f475e43edb13d','/authority/role','peoples',0,1,'2018-11-25 19:10:34','2018-11-25 19:10:34'),('505b4769b77617a314a3ed78e4acdff7','Zipkin',2,'Zipkin链路追踪','147cd431cbb9007bde87444d7987b151','/monitor/Zipkin','example',0,1,'2020-02-06 20:22:18','2020-02-06 20:22:18'),('510483ce569b4fc88299f346147b1314','资源管理',1,'资源管理','','/resource','resource',2,1,'2018-11-28 19:42:13','2018-11-28 19:42:13'),('6228ff4e9ebd42c89599b322201a0345','反馈管理',2,'反馈管理','bcf4a9bc21c14b559bcb015fb7912266','/message/feedback','table',0,1,'2018-11-28 19:48:30','2018-11-28 19:48:30'),('65e22f3d36d94bcea47478aba02895a1','图片管理',1,'图片管理','','/picture','example',3,1,'2018-11-28 19:48:53','2018-11-28 19:48:53'),('6606b7e646d545e5a25c70b5e5fade9f','标签管理',2,'对博客标签进行管理','49b42250abcb47ff876bad699cf34f03','/blog/blogTag','tag',4,1,'2018-11-26 02:57:38','2018-11-26 02:57:38'),('78ab104b123f4950af14d65798afb756','收藏管理',2,'管理用户收藏','49b42250abcb47ff876bad699cf34f03','/blog/collect','example',8,1,'2018-11-25 19:07:48','2018-11-25 19:07:48'),('78f24799307cb63bc3759413dadf4d1a','系统配置',2,'设置七牛云和邮箱等相关配置','badf0010422b432ba6ec9c83a25012ed','/system/systemConfig','web',0,1,'2020-01-21 09:29:04','2020-01-21 09:29:04'),('9002d1ae905c4cb79c2a485333dad2f7','友情链接',2,'友情链接','badf0010422b432ba6ec9c83a25012ed','/system/blogLink','blogLink',0,1,'2018-11-29 03:56:35','2018-11-29 03:56:35'),('93f7fd9a6e81735c47649e6b36042b5d','Druid',2,'Druid监控中心','147cd431cbb9007bde87444d7987b151','/monitor/druid','sql',0,1,'2020-01-06 13:26:51','2020-01-06 13:26:51'),('9449ce5dd5e24b21a9d15f806cb36e87','分类管理',2,'分类管理','510483ce569b4fc88299f346147b1314','/resource/resourceSort','sort',0,1,'2018-11-29 03:43:27','2018-11-29 03:43:27'),('98b82be8785e41dc939b6a5517fdfa53','操作日志',1,'操作日志','','/log','log',9,1,'2018-11-28 20:00:19','2018-11-28 20:00:19'),('9beb7caa2c844b36a02789262dc76fbe','评论管理',2,'评论管理','bcf4a9bc21c14b559bcb015fb7912266','/message/comment','table',1,1,'2018-11-28 19:47:23','2018-11-28 19:47:23'),('9e91b4f993c946cba4bf720b2c1b2e90','用户日志',2,'用户Web端访问情况','98b82be8785e41dc939b6a5517fdfa53','/log/webVisit','user1',0,1,'2019-05-17 10:16:47','2019-05-17 10:16:47'),('a5902692a3ed4fd794895bf634f97b8e','操作日志',2,'操作日志','98b82be8785e41dc939b6a5517fdfa53','/log/log','log',0,1,'2018-11-28 20:01:02','2018-11-28 20:01:02'),('a9396f1a3fbdec3d4cb614f388a22bea','SpringBoot',2,'SpringBootAdmin监控中心','147cd431cbb9007bde87444d7987b151','/monitor/springBootAdmin','system',0,1,'2020-01-05 21:30:16','2020-01-05 21:30:16'),('aa225cdae6464bc0acebd732192f8362','菜单管理',2,'对页面菜单进行管理','d3a19221259d439b916f475e43edb13d','/authority/categoryMenu','example',0,1,'2018-11-25 11:12:01','2018-11-25 11:12:01'),('acbb5d09da25e6c9e019cc361b35d159','Search接口',2,'Search接口','baace3dc03d34c54b81761dce8243814','/restapi/searchRestApi','example',0,1,'2020-01-19 19:56:23','2020-01-19 19:56:23'),('b511cae571834971a392ae4779270034','游客管理',2,'游客管理','c519725da92b42f3acf0cc9fad58c664','/user/visitor','table',2,1,'2018-11-28 19:54:28','2018-11-28 19:54:28'),('baace3dc03d34c54b81761dce8243814','接口管理',1,'接口管理','','/restapi','restapi',4,1,'2018-11-28 20:01:57','2018-11-28 20:01:57'),('badf0010422b432ba6ec9c83a25012ed','系统管理',1,'系统管理','','/system','system',19,1,'2018-11-28 19:54:47','2018-11-28 19:54:47'),('bcf4a9bc21c14b559bcb015fb7912266','消息管理',1,'消息管理','','/message','message1',6,1,'2018-11-28 19:45:29','2018-11-28 19:45:29'),('bfc9463e59a3ca250dcfc1c86627e034','ElasticSearch',2,'ElasticSearch监控页面','147cd431cbb9007bde87444d7987b151','/monitor/elasticSearch','example',0,1,'2020-01-15 22:58:00','2020-01-15 22:58:00'),('c519725da92b42f3acf0cc9fad58c664','用户管理',1,'用户管理','','/user','user1',15,1,'2018-11-28 19:51:47','2018-11-28 19:51:47'),('cbd7ba11c1b38c66b569405ed9185f35','RabbitMQ',2,'RabbitMQ监控中心','147cd431cbb9007bde87444d7987b151','/monitor/rabbitMQ','rabbitMq',0,1,'2020-01-05 21:29:39','2020-01-05 21:29:39'),('d3a19221259d439b916f475e43edb13d','权限管理',1,'对管理员权限分配进行管理','','/authority','authority',18,1,'2018-11-25 19:08:42','2018-11-25 19:08:42'),('d4d92c53d3614d00865e9219b8292a90','Picture接口',2,'Picture接口','baace3dc03d34c54b81761dce8243814','/restapi/pictureRestApi','table',0,1,'2018-11-28 20:04:33','2018-11-28 20:04:33'),('e4a482c089d04a30b6ecbaadb81b70f8','Admin接口',2,'Admin接口','baace3dc03d34c54b81761dce8243814','/restapi/adminRestApi','table',0,1,'2018-11-28 20:03:32','2018-11-28 20:03:32'),('f3a559635f9d46ee3356d072f5896fcb','图片裁剪',2,'用于图片裁剪','f4697cdf85920369179b90ff45a5982d','/test/CropperPicture','example',0,1,'2020-01-30 10:38:09','2020-01-30 10:38:09'),('f4697cdf85920369179b90ff45a5982d','测试页面',1,'用于一些功能的测试',NULL,'/test','example',17,1,'2020-01-30 10:36:00','2020-01-30 10:36:00'),('f9276eb8e3274c8aa05577c86e4dc8c1','Web接口',2,'Web接口','baace3dc03d34c54b81761dce8243814','/restapi/webRestApi','table',0,1,'2018-11-28 20:04:52','2018-11-28 20:04:52'),('faccfe476b89483791c05019ad5b4906','关于我',2,'关于我','badf0010422b432ba6ec9c83a25012ed','/system/aboutMe','aboutMe',0,1,'2018-11-29 03:55:17','2018-11-29 03:55:17'),('fb4237a353d0418ab42c748b7c1d64c6','用户管理',2,'用户管理','c519725da92b42f3acf0cc9fad58c664','/user/user','table',1,1,'2018-11-28 19:52:20','2018-11-28 19:52:20'),('ffc6e9ca2cc243febf6d2f476b849163','视频管理',2,'视频管理','510483ce569b4fc88299f346147b1314','/resource/studyVideo','table',0,1,'2018-11-28 19:43:50','2018-11-28 19:43:50');

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

insert  into `t_comment`(`uid`,`user_uid`,`to_uid`,`to_user_uid`,`content`,`blog_uid`,`status`,`create_time`,`update_time`,`source`) values ('2a48d0f7213b701a4397208c4d1b111f','865f7c001a6186507b3723701ff724d6',NULL,NULL,'测试评论1',NULL,1,'2020-01-30 22:16:44','2020-01-30 22:16:44','MESSAGE_BOARD'),('736118c677257c7e3c9c7b1fa3bd1fa6','865f7c001a6186507b3723701ff724d6','965a7c1fddf4a2106249595856ddd428','865f7c001a6186507b3723701ff724d6','测试；评论',NULL,0,'2020-02-13 09:07:29','2020-02-13 09:07:29','MESSAGE_BOARD'),('965a7c1fddf4a2106249595856ddd428','865f7c001a6186507b3723701ff724d6','ae6738a2b57d0d2ca90b1713b5db7cb8','865f7c001a6186507b3723701ff724d6','哈哈',NULL,0,'2020-02-13 09:07:23','2020-02-13 09:07:23','MESSAGE_BOARD'),('988dd05387f99ec69c464181495a81c4','865f7c001a6186507b3723701ff724d6',NULL,NULL,'测试评论2',NULL,1,'2020-01-30 22:16:49','2020-01-30 22:16:49','MESSAGE_BOARD'),('ae6738a2b57d0d2ca90b1713b5db7cb8','865f7c001a6186507b3723701ff724d6',NULL,NULL,'你好呀啊',NULL,1,'2020-02-13 09:07:13','2020-02-13 09:07:13','MESSAGE_BOARD'),('d0c6cb1586484b61e4e7e294ce7f6bd9','865f7c001a6186507b3723701ff724d6','ae6738a2b57d0d2ca90b1713b5db7cb8','865f7c001a6186507b3723701ff724d6','你好呀',NULL,0,'2020-02-13 09:07:37','2020-02-13 09:07:37','MESSAGE_BOARD'),('fadfdcc1ce36a5ef3538b4e85f76822a','39f8fe66120f239eda3a2148275b561c',NULL,NULL,'测试哈哈',NULL,1,'2020-02-08 21:24:27','2020-02-08 21:24:27','ABOUT');

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

insert  into `t_exception_log`(`uid`,`exception_json`,`exception_message`,`status`,`create_time`,`update_time`,`ip`,`ip_source`,`method`,`operation`,`params`) values ('21a0494e5182f0e3d0234a966c940e3d','{\"@type\":\"java.lang.IllegalArgumentException\",\"cause\":null,\"localizedMessage\":\"Value must not be null!\",\"message\":\"Value must not be null!\",\"stackTrace\":[{\"className\":\"org.springframework.util.Assert\",\"fileName\":\"Assert.java\",\"lineNumber\":198,\"methodName\":\"notNull\",\"nativeMethod\":false},{\"className\":\"org.springframework.data.redis.connection.lettuce.LettuceStringCommands\",\"fileName\":\"LettuceStringCommands.java\",\"lineNumber\":217,\"methodName\":\"setEx\",\"nativeMethod\":false},{\"className\":\"org.springframework.data.redis.connection.DefaultedRedisConnection\",\"fileName\":\"DefaultedRedisConnection.java\",\"lineNumber\":302,\"methodName\":\"setEx\",\"nativeMethod\":false},{\"className\":\"org.springframework.data.redis.connection.DefaultStringRedisConnection\",\"fileName\":\"DefaultStringRedisConnection.java\",\"lineNumber\":1003,\"methodName\":\"setEx\",\"nativeMethod\":false},{\"className\":\"org.springframework.data.redis.core.DefaultValueOperations$4\",\"fileName\":\"DefaultValueOperations.java\",\"lineNumber\":268,\"methodName\":\"potentiallyUsePsetEx\",\"nativeMethod\":false},{\"className\":\"org.springframework.data.redis.core.DefaultValueOperations$4\",\"fileName\":\"DefaultValueOperations.java\",\"lineNumber\":261,\"methodName\":\"doInRedis\",\"nativeMethod\":false},{\"className\":\"org.springframework.data.redis.core.RedisTemplate\",\"fileName\":\"RedisTemplate.java\",\"lineNumber\":228,\"methodName\":\"execute\",\"nativeMethod\":false},{\"className\":\"org.springframework.data.redis.core.RedisTemplate\",\"fileName\":\"RedisTemplate.java\",\"lineNumber\":188,\"methodName\":\"execute\",\"nativeMethod\":false},{\"className\":\"org.springframework.data.redis.core.AbstractOperations\",\"fileName\":\"AbstractOperations.java\",\"lineNumber\":96,\"methodName\":\"execute\",\"nativeMethod\":false},{\"className\":\"org.springframework.data.redis.core.DefaultValueOperations\",\"fileName\":\"DefaultValueOperations.java\",\"lineNumber\":256,\"methodName\":\"set\",\"nativeMethod\":false},{\"className\":\"com.moxi.mogublog.admin.log.LoggerAspect\",\"fileName\":\"LoggerAspect.java\",\"lineNumber\":104,\"methodName\":\"doBefore\",\"nativeMethod\":false},{\"className\":\"sun.reflect.NativeMethodAccessorImpl\",\"fileName\":\"NativeMethodAccessorImpl.java\",\"lineNumber\":-2,\"methodName\":\"invoke0\",\"nativeMethod\":true},{\"className\":\"sun.reflect.NativeMethodAccessorImpl\",\"fileName\":\"NativeMethodAccessorImpl.java\",\"lineNumber\":62,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"sun.reflect.DelegatingMethodAccessorImpl\",\"fileName\":\"DelegatingMethodAccessorImpl.java\",\"lineNumber\":43,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"java.lang.reflect.Method\",\"fileName\":\"Method.java\",\"lineNumber\":498,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.aspectj.AbstractAspectJAdvice\",\"fileName\":\"AbstractAspectJAdvice.java\",\"lineNumber\":644,\"methodName\":\"invokeAdviceMethodWithGivenArgs\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.aspectj.AbstractAspectJAdvice\",\"fileName\":\"AbstractAspectJAdvice.java\",\"lineNumber\":626,\"methodName\":\"invokeAdviceMethod\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.aspectj.AspectJMethodBeforeAdvice\",\"fileName\":\"AspectJMethodBeforeAdvice.java\",\"lineNumber\":44,\"methodName\":\"before\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor\",\"fileName\":\"MethodBeforeAdviceInterceptor.java\",\"lineNumber\":55,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.ReflectiveMethodInvocation\",\"fileName\":\"ReflectiveMethodInvocation.java\",\"lineNumber\":175,\"methodName\":\"proceed\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation\",\"fileName\":\"CglibAopProxy.java\",\"lineNumber\":747,\"methodName\":\"proceed\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor\",\"fileName\":\"AfterReturningAdviceInterceptor.java\",\"lineNumber\":55,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.ReflectiveMethodInvocation\",\"fileName\":\"ReflectiveMethodInvocation.java\",\"lineNumber\":175,\"methodName\":\"proceed\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation\",\"fileName\":\"CglibAopProxy.java\",\"lineNumber\":747,\"methodName\":\"proceed\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.aspectj.AspectJAfterThrowingAdvice\",\"fileName\":\"AspectJAfterThrowingAdvice.java\",\"lineNumber\":62,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.ReflectiveMethodInvocation\",\"fileName\":\"ReflectiveMethodInvocation.java\",\"lineNumber\":175,\"methodName\":\"proceed\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation\",\"fileName\":\"CglibAopProxy.java\",\"lineNumber\":747,\"methodName\":\"proceed\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.interceptor.ExposeInvocationInterceptor\",\"fileName\":\"ExposeInvocationInterceptor.java\",\"lineNumber\":93,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.ReflectiveMethodInvocation\",\"fileName\":\"ReflectiveMethodInvocation.java\",\"lineNumber\":186,\"methodName\":\"proceed\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation\",\"fileName\":\"CglibAopProxy.java\",\"lineNumber\":747,\"methodName\":\"proceed\",\"nativeMethod\":false},{\"className\":\"org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor\",\"fileName\":\"CglibAopProxy.java\",\"lineNumber\":689,\"methodName\":\"intercept\",\"nativeMethod\":false},{\"className\":\"com.moxi.mogublog.admin.restapi.BlogRestApi$$EnhancerBySpringCGLIB$$4ba39ab3\",\"fileName\":\"<generated>\",\"lineNumber\":-1,\"methodName\":\"edit\",\"nativeMethod\":false},{\"className\":\"sun.reflect.NativeMethodAccessorImpl\",\"fileName\":\"NativeMethodAccessorImpl.java\",\"lineNumber\":-2,\"methodName\":\"invoke0\",\"nativeMethod\":true},{\"className\":\"sun.reflect.NativeMethodAccessorImpl\",\"fileName\":\"NativeMethodAccessorImpl.java\",\"lineNumber\":62,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"sun.reflect.DelegatingMethodAccessorImpl\",\"fileName\":\"DelegatingMethodAccessorImpl.java\",\"lineNumber\":43,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"java.lang.reflect.Method\",\"fileName\":\"Method.java\",\"lineNumber\":498,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.method.support.InvocableHandlerMethod\",\"fileName\":\"InvocableHandlerMethod.java\",\"lineNumber\":190,\"methodName\":\"doInvoke\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.method.support.InvocableHandlerMethod\",\"fileName\":\"InvocableHandlerMethod.java\",\"lineNumber\":138,\"methodName\":\"invokeForRequest\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod\",\"fileName\":\"ServletInvocableHandlerMethod.java\",\"lineNumber\":106,\"methodName\":\"invokeAndHandle\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter\",\"fileName\":\"RequestMappingHandlerAdapter.java\",\"lineNumber\":888,\"methodName\":\"invokeHandlerMethod\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter\",\"fileName\":\"RequestMappingHandlerAdapter.java\",\"lineNumber\":793,\"methodName\":\"handleInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter\",\"fileName\":\"AbstractHandlerMethodAdapter.java\",\"lineNumber\":87,\"methodName\":\"handle\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.servlet.DispatcherServlet\",\"fileName\":\"DispatcherServlet.java\",\"lineNumber\":1040,\"methodName\":\"doDispatch\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.servlet.DispatcherServlet\",\"fileName\":\"DispatcherServlet.java\",\"lineNumber\":943,\"methodName\":\"doService\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.servlet.FrameworkServlet\",\"fileName\":\"FrameworkServlet.java\",\"lineNumber\":1006,\"methodName\":\"processRequest\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.servlet.FrameworkServlet\",\"fileName\":\"FrameworkServlet.java\",\"lineNumber\":909,\"methodName\":\"doPost\",\"nativeMethod\":false},{\"className\":\"javax.servlet.http.HttpServlet\",\"fileName\":\"HttpServlet.java\",\"lineNumber\":660,\"methodName\":\"service\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.servlet.FrameworkServlet\",\"fileName\":\"FrameworkServlet.java\",\"lineNumber\":883,\"methodName\":\"service\",\"nativeMethod\":false},{\"className\":\"javax.servlet.http.HttpServlet\",\"fileName\":\"HttpServlet.java\",\"lineNumber\":741,\"methodName\":\"service\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":231,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.tomcat.websocket.server.WsFilter\",\"fileName\":\"WsFilter.java\",\"lineNumber\":53,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":193,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":113,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":193,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":113,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":193,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"com.moxi.mogublog.admin.security.JwtAuthenticationTokenFilter\",\"fileName\":\"JwtAuthenticationTokenFilter.java\",\"lineNumber\":173,\"methodName\":\"doFilterInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":119,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":193,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"com.alibaba.druid.support.http.WebStatFilter\",\"fileName\":\"WebStatFilter.java\",\"lineNumber\":123,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":193,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":320,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.access.intercept.FilterSecurityInterceptor\",\"fileName\":\"FilterSecurityInterceptor.java\",\"lineNumber\":126,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.access.intercept.FilterSecurityInterceptor\",\"fileName\":\"FilterSecurityInterceptor.java\",\"lineNumber\":90,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.access.ExceptionTranslationFilter\",\"fileName\":\"ExceptionTranslationFilter.java\",\"lineNumber\":118,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.session.SessionManagementFilter\",\"fileName\":\"SessionManagementFilter.java\",\"lineNumber\":137,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.authentication.AnonymousAuthenticationFilter\",\"fileName\":\"AnonymousAuthenticationFilter.java\",\"lineNumber\":111,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter\",\"fileName\":\"SecurityContextHolderAwareRequestFilter.java\",\"lineNumber\":158,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.savedrequest.RequestCacheAwareFilter\",\"fileName\":\"RequestCacheAwareFilter.java\",\"lineNumber\":63,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"com.moxi.mogublog.admin.security.JwtAuthenticationTokenFilter\",\"fileName\":\"JwtAuthenticationTokenFilter.java\",\"lineNumber\":173,\"methodName\":\"doFilterInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":119,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.authentication.logout.LogoutFilter\",\"fileName\":\"LogoutFilter.java\",\"lineNumber\":116,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.CorsFilter\",\"fileName\":\"CorsFilter.java\",\"lineNumber\":92,\"methodName\":\"doFilterInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":119,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.header.HeaderWriterFilter\",\"fileName\":\"HeaderWriterFilter.java\",\"lineNumber\":92,\"methodName\":\"doHeadersAfter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.header.HeaderWriterFilter\",\"fileName\":\"HeaderWriterFilter.java\",\"lineNumber\":77,\"methodName\":\"doFilterInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":119,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.context.SecurityContextPersistenceFilter\",\"fileName\":\"SecurityContextPersistenceFilter.java\",\"lineNumber\":105,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter\",\"fileName\":\"WebAsyncManagerIntegrationFilter.java\",\"lineNumber\":56,\"methodName\":\"doFilterInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":119,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy$VirtualFilterChain\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":334,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":215,\"methodName\":\"doFilterInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.security.web.FilterChainProxy\",\"fileName\":\"FilterChainProxy.java\",\"lineNumber\":178,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.DelegatingFilterProxy\",\"fileName\":\"DelegatingFilterProxy.java\",\"lineNumber\":358,\"methodName\":\"invokeDelegate\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.DelegatingFilterProxy\",\"fileName\":\"DelegatingFilterProxy.java\",\"lineNumber\":271,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":193,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.RequestContextFilter\",\"fileName\":\"RequestContextFilter.java\",\"lineNumber\":100,\"methodName\":\"doFilterInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":119,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":193,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.FormContentFilter\",\"fileName\":\"FormContentFilter.java\",\"lineNumber\":93,\"methodName\":\"doFilterInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":119,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":193,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter\",\"fileName\":\"WebMvcMetricsFilter.java\",\"lineNumber\":108,\"methodName\":\"doFilterInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":119,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":193,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.CharacterEncodingFilter\",\"fileName\":\"CharacterEncodingFilter.java\",\"lineNumber\":201,\"methodName\":\"doFilterInternal\",\"nativeMethod\":false},{\"className\":\"org.springframework.web.filter.OncePerRequestFilter\",\"fileName\":\"OncePerRequestFilter.java\",\"lineNumber\":119,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":193,\"methodName\":\"internalDoFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.ApplicationFilterChain\",\"fileName\":\"ApplicationFilterChain.java\",\"lineNumber\":166,\"methodName\":\"doFilter\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.StandardWrapperValve\",\"fileName\":\"StandardWrapperValve.java\",\"lineNumber\":202,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.StandardContextValve\",\"fileName\":\"StandardContextValve.java\",\"lineNumber\":96,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.authenticator.AuthenticatorBase\",\"fileName\":\"AuthenticatorBase.java\",\"lineNumber\":526,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.StandardHostValve\",\"fileName\":\"StandardHostValve.java\",\"lineNumber\":139,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.valves.ErrorReportValve\",\"fileName\":\"ErrorReportValve.java\",\"lineNumber\":92,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.core.StandardEngineValve\",\"fileName\":\"StandardEngineValve.java\",\"lineNumber\":74,\"methodName\":\"invoke\",\"nativeMethod\":false},{\"className\":\"org.apache.catalina.connector.CoyoteAdapter\",\"fileName\":\"CoyoteAdapter.java\",\"lineNumber\":343,\"methodName\":\"service\",\"nativeMethod\":false},{\"className\":\"org.apache.coyote.http11.Http11Processor\",\"fileName\":\"Http11Processor.java\",\"lineNumber\":367,\"methodName\":\"service\",\"nativeMethod\":false},{\"className\":\"org.apache.coyote.AbstractProcessorLight\",\"fileName\":\"AbstractProcessorLight.java\",\"lineNumber\":65,\"methodName\":\"process\",\"nativeMethod\":false},{\"className\":\"org.apache.coyote.AbstractProtocol$ConnectionHandler\",\"fileName\":\"AbstractProtocol.java\",\"lineNumber\":860,\"methodName\":\"process\",\"nativeMethod\":false},{\"className\":\"org.apache.tomcat.util.net.NioEndpoint$SocketProcessor\",\"fileName\":\"NioEndpoint.java\",\"lineNumber\":1591,\"methodName\":\"doRun\",\"nativeMethod\":false},{\"className\":\"org.apache.tomcat.util.net.SocketProcessorBase\",\"fileName\":\"SocketProcessorBase.java\",\"lineNumber\":49,\"methodName\":\"run\",\"nativeMethod\":false},{\"className\":\"java.util.concurrent.ThreadPoolExecutor\",\"fileName\":\"ThreadPoolExecutor.java\",\"lineNumber\":1142,\"methodName\":\"runWorker\",\"nativeMethod\":false},{\"className\":\"java.util.concurrent.ThreadPoolExecutor$Worker\",\"fileName\":\"ThreadPoolExecutor.java\",\"lineNumber\":617,\"methodName\":\"run\",\"nativeMethod\":false},{\"className\":\"org.apache.tomcat.util.threads.TaskThread$WrappingRunnable\",\"fileName\":\"TaskThread.java\",\"lineNumber\":61,\"methodName\":\"run\",\"nativeMethod\":false},{\"className\":\"java.lang.Thread\",\"fileName\":\"Thread.java\",\"lineNumber\":745,\"methodName\":\"run\",\"nativeMethod\":false}],\"suppressed\":[]}','Value must not be null!',1,'2020-02-02 16:14:55','2020-02-02 16:14:55',NULL,NULL,NULL,NULL,'{\"adminUid\":\"1f01cd1d2f474743b241d74008b12333\",\"articlesPart\":\"蘑菇博客\",\"author\":\"陌溪_\",\"blogSort\":{\"clickCount\":0,\"content\":\"测试分类\",\"createTime\":1580422014000,\"sort\":0,\"sortName\":\"测试分类\",\"status\":1,\"uid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"updateTime\":1580422014000},\"blogSortUid\":\"6a1c7a50c0e7b8e8657949bf02d5d0ca\",\"content\":\"<p>测试博客9</p>\\n\",\"fileUid\":\"adeaf35fdcd2d36ed143d95bc02b1cc2\",\"isOriginal\":\"1\",\"isPublish\":\"1\",\"level\":1,\"photoList\":[\"http://localhost:8600//blog/admin/jpg/2020/2/1/1580534698003.jpg\"],\"sort\":0,\"status\":1,\"summary\":\"测试博客9\",\"tagList\":[{\"clickCount\":0,\"content\":\"测试标签2\",\"createTime\":1580422030000,\"sort\":0,\"status\":1,\"uid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"updateTime\":1580422030000}],\"tagUid\":\"6b0ba63beabccc91c4f8fb938984f8a3\",\"title\":\"测试博客9\",\"uid\":\"5177f5f90e8a62a185dbe9c3e9c275f8\",\"useSort\":0}');

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

insert  into `t_picture`(`uid`,`file_uid`,`pic_name`,`picture_sort_uid`,`status`,`create_time`,`update_time`) values ('0398f756b357eed1fe85e40d2a291e06','3302d7c58941602514f2812a20500a4c',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:26:44','2020-02-01 10:26:44'),('15cda71a209a1d5961473cffb83eff91','d2423d5fa8f4e7fca64f6d36c4697703',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:26:40','2020-02-01 10:26:40'),('1679f363cf3f2a1c70e397339f6bf28f','d134bdfe255b9aa8c2ce44da8297f6a6','undefined','481b95ba5cce396c9ec14544b0784751',1,'2020-01-30 22:08:26','2020-01-30 22:08:26'),('1a03785415b6eda344753bf93c10a24a','d37d3e699f46a2b3785600f7c4ac6b3f',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:23:11','2020-02-01 10:23:11'),('2afd4d538ac77a70c3e3bd96aac9366a','4e871894c11e04edbf2c4e22b4644730',NULL,'481b95ba5cce396c9ec14544b0784751',1,'2020-02-06 18:54:27','2020-02-06 18:54:27'),('377420b661da6a0d53075a20fdca5ddb','e4c42b7004e9e5751ebc89433fc8b712',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:23:21','2020-02-01 10:23:21'),('3d90ea0854f9856700ee1526d7578fbf','858184682eff563a349f595c6b44e5cb',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:46:00','2020-02-01 10:46:00'),('59b6fd6c4c1cc4f2ebe441024f504a8c','8dd7c4f4ff0e87f38e1cdcf6190768d2',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:22:48','2020-02-01 10:22:48'),('6dc3b84160804ccf0536bfce932c29e0','21bb95558a698f8f1348136effde44e3',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:20:30','2020-02-01 10:20:30'),('6ef278b547e77a8ba53119498725e7cc','05b355d8915d479ba7980e65378ddc7b',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:46:43','2020-02-01 10:46:43'),('730805fa92827552e8296b7e36a973df','70f5b89fe70a28d95e10c19c96bf2e85',NULL,'481b95ba5cce396c9ec14544b0784751',1,'2020-02-06 18:54:31','2020-02-06 18:54:31'),('aa56a5e805d619ae79e88d9ba5fa5b87','adeaf35fdcd2d36ed143d95bc02b1cc2','undefined','481b95ba5cce396c9ec14544b0784751',1,'2020-01-30 22:08:26','2020-01-30 22:08:26'),('ae0388aba027c118748663232773d419','bb4ba8de81fdcd837bff6d25045ace5f',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:46:57','2020-02-01 10:46:57'),('c8e267edce391e23e357f3528fa21e72','7a08d34301b572a54b758f3f57809548','undefined','481b95ba5cce396c9ec14544b0784751',1,'2020-01-30 22:08:26','2020-01-30 22:08:26'),('cae0fc2d7bd025f563cb651df774a9f4','ba08d19ef9f5b5113479b385903ee1de',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:46:57','2020-02-01 10:46:57'),('cdb12faf3f3bfb4cbf7d4c37a5646146','6aa29d78ca8d9fb98c74fff2027a2023',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:20:23','2020-02-01 10:20:23'),('ee2c4832ba5b9aae12643754d21ff01d','33f8407082a521fddb37204c4d4a9b5f',NULL,'481b95ba5cce396c9ec14544b0784751',0,'2020-02-01 10:46:53','2020-02-01 10:46:53');

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

insert  into `t_picture_sort`(`uid`,`file_uid`,`name`,`status`,`create_time`,`update_time`,`parent_uid`,`sort`) values ('481b95ba5cce396c9ec14544b0784751','c54296fcb729599afec1d94d261a6229','测试图片',1,'2020-01-30 22:08:10','2020-01-30 22:08:10',NULL,0);

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

insert  into `t_resource_sort`(`uid`,`file_uid`,`sort_name`,`content`,`click_count`,`status`,`create_time`,`update_time`,`parent_uid`,`sort`) values ('cf2fa0b814489b81eec18f2e7f879f19','dd34fc59ff945ca781cd46c23f7d7b65','测试分类','测试分类',NULL,1,'2020-02-01 12:35:35','2020-02-01 12:35:35',NULL,0);

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

insert  into `t_role`(`uid`,`role_name`,`create_time`,`update_time`,`status`,`summary`,`category_menu_uids`) values ('434994947c5a4ee3a710cd277357c7c3','超级管理员','2018-10-16 07:56:26','2018-10-16 07:56:35',1,'超级管理员，管理全部菜单和功能','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"1d9a5030142e9fd7690f554c20e3bc90\",\"badf0010422b432ba6ec9c83a25012ed\",\"2fb47d3b6dbd44279c8206740a263543\",\"78f24799307cb63bc3759413dadf4d1a\",\"9002d1ae905c4cb79c2a485333dad2f7\",\"faccfe476b89483791c05019ad5b4906\",\"d3a19221259d439b916f475e43edb13d\",\"2de247af3b0a459095e937d7ab9f5864\",\"5010ae46511e4c0b9f30d1c63ad3f0c1\",\"aa225cdae6464bc0acebd732192f8362\",\"f4697cdf85920369179b90ff45a5982d\",\"407a263eb12eff5aac31e9f62901cea0\",\"f3a559635f9d46ee3356d072f5896fcb\",\"c519725da92b42f3acf0cc9fad58c664\",\"b511cae571834971a392ae4779270034\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"98b82be8785e41dc939b6a5517fdfa53\",\"4337f63d13d84b9aba64b9d7a69fd066\",\"9e91b4f993c946cba4bf720b2c1b2e90\",\"a5902692a3ed4fd794895bf634f97b8e\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"baace3dc03d34c54b81761dce8243814\",\"acbb5d09da25e6c9e019cc361b35d159\",\"d4d92c53d3614d00865e9219b8292a90\",\"e4a482c089d04a30b6ecbaadb81b70f8\",\"f9276eb8e3274c8aa05577c86e4dc8c1\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\",\"510483ce569b4fc88299f346147b1314\",\"9449ce5dd5e24b21a9d15f806cb36e87\",\"ffc6e9ca2cc243febf6d2f476b849163\",\"147cd431cbb9007bde87444d7987b151\",\"02ea2f9ef5d44f559fb66189b05f6769\",\"079f0cfdb7a7017d827f5c349983eebc\",\"505b4769b77617a314a3ed78e4acdff7\",\"93f7fd9a6e81735c47649e6b36042b5d\",\"a9396f1a3fbdec3d4cb614f388a22bea\",\"bfc9463e59a3ca250dcfc1c86627e034\",\"cbd7ba11c1b38c66b569405ed9185f35\"]'),('434994947c5a4ee3a710cd277357c7c4','文章管理员','2018-10-15 07:56:21','2018-10-15 07:56:23',1,'管理文章','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\"]'),('d105da79260f4d6a8a03571e4a2b17bc','一般管理员','2019-05-29 00:43:26','2019-05-29 00:43:26',1,'一般管理员','[\"49b42250abcb47ff876bad699cf34f03\",\"1f01cd1d2f474743b241d74008b12333\",\"0a035547bbec404eb3ee0ef43312148d\",\"78ab104b123f4950af14d65798afb756\",\"6606b7e646d545e5a25c70b5e5fade9f\",\"c519725da92b42f3acf0cc9fad58c664\",\"fb4237a353d0418ab42c748b7c1d64c6\",\"b511cae571834971a392ae4779270034\",\"badf0010422b432ba6ec9c83a25012ed\",\"02ea2f9ef5d44f559fb66189b05f6769\",\"2fb47d3b6dbd44279c8206740a263543\",\"9002d1ae905c4cb79c2a485333dad2f7\",\"faccfe476b89483791c05019ad5b4906\",\"98b82be8785e41dc939b6a5517fdfa53\",\"4337f63d13d84b9aba64b9d7a69fd066\",\"9e91b4f993c946cba4bf720b2c1b2e90\",\"a5902692a3ed4fd794895bf634f97b8e\",\"bcf4a9bc21c14b559bcb015fb7912266\",\"9beb7caa2c844b36a02789262dc76fbe\",\"6228ff4e9ebd42c89599b322201a0345\",\"baace3dc03d34c54b81761dce8243814\",\"d4d92c53d3614d00865e9219b8292a90\",\"e4a482c089d04a30b6ecbaadb81b70f8\",\"f9276eb8e3274c8aa05577c86e4dc8c1\",\"65e22f3d36d94bcea47478aba02895a1\",\"4dea9c4f39d2480983e8c4333d35e036\"]');

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

insert  into `t_study_video`(`uid`,`file_uid`,`resource_sort_uid`,`name`,`summary`,`content`,`baidu_path`,`click_count`,`status`,`create_time`,`update_time`,`parent_uid`) values ('b396e08946a3571ab5de04dae522f255','dd34fc59ff945ca781cd46c23f7d7b65','cf2fa0b814489b81eec18f2e7f879f19','测试','测试','<p>测试</p>\n','测试','0',1,'2020-02-01 12:37:28','2020-02-01 12:37:28',NULL);

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

insert  into `t_system_config`(`uid`,`qi_niu_access_key`,`qi_niu_secret_key`,`email`,`email_user_name`,`email_password`,`smtp_address`,`smtp_port`,`status`,`create_time`,`update_time`,`qi_niu_bucket`,`qi_niu_area`,`upload_qi_niu`,`upload_local`,`picture_priority`,`qi_niu_picture_base_url`,`local_picture_base_url`) values ('37d492e35dc6e3fbb9dfedfd2079a123','','','mogublog@163.com',NULL,'',NULL,'',1,'2020-01-29 19:14:26','2020-01-29 19:14:26','  ','z2','0','1','0','','http://localhost:8600/');

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

insert  into `t_tag`(`uid`,`content`,`status`,`click_count`,`create_time`,`update_time`,`sort`) values ('5c4c541e600ff422ccb371ee788f59d6','测试标签',1,0,'2020-01-30 22:07:03','2020-01-30 22:07:03',0),('6b0ba63beabccc91c4f8fb938984f8a3','测试标签2',1,1,'2020-01-30 22:07:10','2020-01-30 22:07:10',0);

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

insert  into `t_user`(`uid`,`user_name`,`pass_word`,`gender`,`avatar`,`email`,`birthday`,`mobile`,`valid_code`,`summary`,`login_count`,`last_login_time`,`last_login_ip`,`status`,`create_time`,`update_time`,`nick_name`,`source`,`uuid`,`qq_number`,`we_chat`,`occupation`) values ('39f8fe66120f239eda3a2148275b561c','moguBlog_GITEE_848486','068449',NULL,'eaa1ffb98313f224495652dc995ace5a','moxi0624@163.com',NULL,NULL,NULL,NULL,2,'2020-02-09 09:03:34','10.0.75.1',1,'2020-02-08 21:24:01','2020-02-08 21:24:01','陌溪','GITEE','848486',NULL,NULL,NULL),('865f7c001a6186507b3723701ff724d6','moguBlog_GITHUB_18610136','051120',NULL,'f858cf4768a4dd1697002bf4abf8b58f','moxi0624@163.com',NULL,NULL,NULL,NULL,1,'2020-02-13 09:07:05','10.0.75.1',1,'2020-01-30 22:12:57','2020-01-30 22:12:57','Streamlet','GITHUB','18610136',NULL,NULL,NULL);

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
  `show_list` varchar(255) DEFAULT NULL COMMENT '显示的列表（用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端）',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_web_config` */

insert  into `t_web_config`(`uid`,`logo`,`name`,`summary`,`keyword`,`author`,`record_num`,`start_comment`,`status`,`create_time`,`update_time`,`title`,`ali_pay`,`weixin_pay`,`github`,`gitee`,`qq_number`,`qq_group`,`we_chat`,`email`,`show_list`) values ('a331e4933cf54afcbb8c0cb11ec0830e',',dd34fc59ff945ca781cd46c23f7d7b65','蘑菇博客','一个专注于技术分享的博客平台，大家以共同学习，乐于分享，拥抱开源的价值观进行学习交流','\"蘑菇博客,蘑菇社区,蘑菇技术社区,,蘑菇IT社区,IT社区,技术社区,Java技术分享,Spring教程,开发者社区','陌溪','赣ICP备18014504号','1',1,'2018-11-17 08:15:27','2018-11-17 08:15:27','一个专注于技术分享的博客平台','5e4365f45a4708e17f2fe300d3279bf5','6d83d6146896f9f27f07404fbb05c90f','https://github.com/moxi624','https://gitee.com/moxi159753','1595833114','950309755','','1595833114@qq.com','[\"1\",\"4\",\"5\"]');

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

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
