/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 10.1.39-MariaDB : Database - mogu_picture
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mogu_picture` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `mogu_picture`;

/*Table structure for table `t_file` */

DROP TABLE IF EXISTS `t_file`;

CREATE TABLE `t_file` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `file_old_name` varchar(255) DEFAULT NULL COMMENT '旧文件名',
  `pic_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `pic_url` varchar(255) DEFAULT NULL COMMENT '文件地址',
  `pic_expanded_name` varchar(255) DEFAULT NULL COMMENT '文件扩展名',
  `file_size` int(20) DEFAULT '0' COMMENT '文件大小',
  `file_sort_uid` varchar(36) DEFAULT NULL COMMENT '文件分类uid',
  `admin_uid` varchar(36) NOT NULL COMMENT '管理员uid',
  `user_uid` varchar(36) NOT NULL COMMENT '用户uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件表';

/*Data for the table `t_file` */

insert  into `t_file`(`uid`,`file_old_name`,`pic_name`,`pic_url`,`pic_expanded_name`,`file_size`,`file_sort_uid`,`admin_uid`,`user_uid`,`status`,`create_time`,`update_time`) values ('25ad1b97042e5df69bf7d40984bd28cf','风景 - 7.jpg','1574824303041.jpg','/blog/admin/jpg/2019/11/27/1574824303041.jpg','jpg',2302437,'a9a747d944c24845815356f72723ef8e','uid00000000000000000000000000000000','uid00000000000000000000000000000000',1,'2019-11-27 03:11:43','2019-11-27 03:11:43'),('6befb0180d151cbe1a13c84bd824cd72','风景 - 6.jpg','1574824303102.jpg','/blog/admin/jpg/2019/11/27/1574824303102.jpg','jpg',1223652,'a9a747d944c24845815356f72723ef8e','uid00000000000000000000000000000000','uid00000000000000000000000000000000',1,'2019-11-27 03:11:43','2019-11-27 03:11:43'),('c1c09a7de5657168c2e843fb7f978368','img_0055.jpg','1574824303063.jpg','/blog/admin/jpg/2019/11/27/1574824303063.jpg','jpg',1319823,'a9a747d944c24845815356f72723ef8e','uid00000000000000000000000000000000','uid00000000000000000000000000000000',1,'2019-11-27 03:11:43','2019-11-27 03:11:43'),('d4304482662b385498ee7042b2666cbf','风景 - 1.jpg','1574824302936.jpg','/blog/admin/jpg/2019/11/27/1574824302936.jpg','jpg',854716,'a9a747d944c24845815356f72723ef8e','uid00000000000000000000000000000000','uid00000000000000000000000000000000',1,'2019-11-27 03:11:42','2019-11-27 03:11:42');

/*Table structure for table `t_file_sort` */

DROP TABLE IF EXISTS `t_file_sort`;

CREATE TABLE `t_file_sort` (
  `uid` varchar(36) NOT NULL COMMENT '唯一uid',
  `project_name` varchar(255) DEFAULT NULL COMMENT '项目名',
  `sort_name` varchar(255) DEFAULT NULL COMMENT '分类名',
  `url` varchar(255) DEFAULT NULL COMMENT '分类路径',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件分类表';

/*Data for the table `t_file_sort` */

insert  into `t_file_sort`(`uid`,`project_name`,`sort_name`,`url`,`status`,`create_time`,`update_time`) values ('a9a747d944c24845815356f72723ef8e','blog','admin','/blog/admin',1,'2018-09-20 14:41:57','2018-09-20 19:40:02'),('a9a747d944c24845815356f72723ef8f','blog','web','/blog/web',1,'2018-09-20 19:40:37','2018-09-20 19:40:40');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
