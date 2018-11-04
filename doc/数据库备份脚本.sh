#!/bin/bash

#此数据库脚本只备份最近7天的数据

baseDir="/disk1/mysql_server/data_dump/";

cd "$baseDir";

echo "开始备份数据库";

echo `mysqldump  -uroot -p 'password' --default-character-set=utf8  database > database_dump_$(date +%Y%m%d_%H%M%S).sql`;

echo "备份数据完成";

oldDate=`date --date='8 day ago' +%Y%m%d`;

#删除当前日期-8的备份

echo `rm -rf database_dump_$oldDate*`;

echo "删除$oldDate的备份成功"
