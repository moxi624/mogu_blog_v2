
/*
增加排序字段
*/

USE `mogu_blog`;


ALTER TABLE t_tag ADD sort INT DEFAULT 0 COMMENT  '排序字段，越大越靠前';
ALTER TABLE t_picture_sort ADD sort INT DEFAULT 0 COMMENT  '排序字段，越大越靠前';
ALTER TABLE t_link ADD sort INT DEFAULT 0 COMMENT  '排序字段，越大越靠前';
ALTER TABLE t_blog_sort ADD sort INT DEFAULT 0 COMMENT  '排序字段，越大越靠前';
ALTER TABLE t_resource_sor ADD sort INT DEFAULT 0 COMMENT  '排序字段，越大越靠前';