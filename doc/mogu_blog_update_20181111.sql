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
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_web_config` */

insert  into `t_web_config`(`uid`,`logo`,`name`,`summary`,`keyword`,`author`,`record_num`,`start_comment`,`status`,`create_time`,`update_time`) values ('a331e4933cf54afcbb8c0cb11ec0830e','','蘑菇博客','一个开源的个人博客','蘑菇博客','许志翔','赣ICP备18014504号','0',1,'2018-11-13 00:15:27','2018-11-13 00:15:27');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
