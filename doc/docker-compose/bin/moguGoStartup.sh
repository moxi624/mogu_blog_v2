#!/usr/bin/env bash

echo '=====开始安装蘑菇博客环境====='

echo '=====开始运行mysql====='
docker-compose -f ../yaml/mysql.yml up -d
echo '=====mysql正在进行初始化====='
./wait-for-it.sh localhost:3306 --timeout=60  -- echo "=====mysql已经准备就绪====="

echo '=====开始运行redis====='
docker-compose -f ../yaml/redis.yml up -d

echo '=====开始部署mogu_data====='
docker-compose -f ../yaml/mogu_data.yml up -d

echo '=====开始部署minio====='
docker-compose -f ../yaml/minio.yml up -d

echo '======================'
echo '=====开始运行后台====='
echo '======================'

echo '=====开始运行mogu_go====='
docker-compose -f ../yaml/mogu_blog_go.yml up -d

echo '=====开始运行前台====='
echo '======================'

echo '=====开始运行vue_mogu_admin====='
docker-compose -f ../yaml/vue_mogu_admin.yml up -d


echo '=====开始运行vue_mogu_web====='
docker-compose -f ../yaml/vue_mogu_web.yml up -d

echo '================================================================='
echo '=====【蘑菇博客Go版启动成功】====='
echo '================================================================='
