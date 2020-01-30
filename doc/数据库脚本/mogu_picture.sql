/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 10.1.35-MariaDB : Database - mogu_picture
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
  `admin_uid` varchar(36) DEFAULT NULL COMMENT '管理员uid',
  `user_uid` varchar(36) DEFAULT NULL COMMENT '用户uid',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  `qi_niu_url` varchar(255) DEFAULT NULL COMMENT '七牛云地址',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件表';

/*Data for the table `t_file` */

insert  into `t_file`(`uid`,`file_old_name`,`pic_name`,`pic_url`,`pic_expanded_name`,`file_size`,`file_sort_uid`,`admin_uid`,`user_uid`,`status`,`create_time`,`update_time`,`qi_niu_url`) values ('b32d984f095394c91bb866fe926f366a','timg (1).jfif','1580393305973.jfif','/blog/admin/jfif/2020/1/30/1580393305973.jfif','jfif',5838,'a9a747d944c24845815356f72723ef8e','uid00000000000000000000000000000000','uid00000000000000000000000000000000',1,'2020-01-30 22:08:25','2020-01-30 22:08:25',NULL),('c54296fcb729599afec1d94d261a6229','timg (3).jfif','1580393306072.jfif','/blog/admin/jfif/2020/1/30/1580393306072.jfif','jfif',11940,'a9a747d944c24845815356f72723ef8e','uid00000000000000000000000000000000','uid00000000000000000000000000000000',1,'2020-01-30 22:08:26','2020-01-30 22:08:26',NULL),('dd34fc59ff945ca781cd46c23f7d7b65','u=3616726678,2800089975&fm=26&gp=0.jpg','1580393305054.jpg','/blog/admin/jpg/2020/1/30/1580393305054.jpg','jpg',11515,'a9a747d944c24845815356f72723ef8e','uid00000000000000000000000000000000','uid00000000000000000000000000000000',1,'2020-01-30 22:08:25','2020-01-30 22:08:25',NULL),('f858cf4768a4dd1697002bf4abf8b58f','https://avatars0.githubusercontent.com/u/18610136?v=4','1580393577428.jpg','/blog/admin/jpg/2020/1/30/1580393577428.jpg','jpg',0,'a9a747d944c24845815356f72723ef8e','uid00000000000000000000000000000000','uid00000000000000000000000000000000',1,'2020-01-30 22:13:07','2020-01-30 22:13:07',NULL);

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

insert  into `t_file_sort`(`uid`,`project_name`,`sort_name`,`url`,`status`,`create_time`,`update_time`) values ('a9a747d944c24845815356f72723ef8e','blog','admin','/blog/admin',1,'2018-09-20 22:41:57','2018-09-21 03:40:02'),('a9a747d944c24845815356f72723ef8f','blog','web','/blog/web',1,'2018-09-21 03:40:37','2018-09-21 03:40:40');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
