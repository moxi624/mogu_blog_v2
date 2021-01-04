#!/usr/bin/env bash

echo '=====开始安装蘑菇博客环境====='

echo '=====开始运行mysql====='
docker-compose -f ../yaml/mysql.yml up -d
echo '=====mysql正在进行初始化====='
./wait-for-it.sh http://localhost:3306 --timeout=60  -- echo "=====mysql已经准备就绪====="

echo '=====开始部署portainer可视化工具====='
docker-compose -f ../yaml/portainer.yml up -d

echo '=====开始运行nacos====='
docker-compose -f ../yaml/nacos.yml up -d
echo '=====nacos正在进行初始化,请等待...====='
./wait-for-it.sh http://localhost:8848 --timeout=60  -- echo "=====nacos已经准备就绪====="

echo '=====开始运行rabbitmq====='
docker-compose -f ../yaml/rabbitmq.yml up -d

echo '=====开始运行redis====='
docker-compose -f ../yaml/redis.yml up -d

echo '=====开始部署mogu_data====='
docker-compose -f ../yaml/mogu_data.yml up -d

echo '=====开始运行zipkin====='
docker-compose -f ../yaml/zipkin.yml up -d

echo '=====开始运行sentinel====='
docker-compose -f ../yaml/sentinel.yml up -d

echo '=====开始运行ELK====='
#docker-compose -f ../yaml/elk.yml up -d

echo '======================'
echo '=====开始运行后台====='
echo '======================'

echo '=====开始运行mogu_monitor====='
docker-compose -f ../yaml/mogu_gateway.yml up -d

echo '=====开始运行mogu_admin====='
docker-compose -f ../yaml/mogu_admin.yml up -d

echo '=====开始运行mogu_picture====='
docker-compose -f ../yaml/mogu_picture.yml up -d

echo '=====开始运行mogu_sms====='
docker-compose -f ../yaml/mogu_sms.yml up -d

echo '=====开始运行mogu_web====='
docker-compose -f ../yaml/mogu_web.yml up -d

echo '=====开始运行mogu_search====='
docker-compose -f ../yaml/mogu_search.yml up -d

echo '=====开始运行mogu_monitor====='
docker-compose -f ../yaml/mogu_monitor.yml up -d

echo '执行完成 日志目录: ./log'


echo '======================'
echo '=====开始运行前台====='
echo '======================'

echo '=====开始运行vue_mogu_admin====='
docker-compose -f ../yaml/vue_mogu_admin.yml up -d


echo '=====开始运行vue_mogu_web====='
docker-compose -f ../yaml/vue_mogu_web.yml up -d

echo '======================================================'
echo '=====所有服务已经启动【请检查是否存在错误启动的】====='
echo '======================================================'
