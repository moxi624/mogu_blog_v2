'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',

  //开发环境
  FILE_API: '"http://localhost:8600/"',
  ADMIN_API: '"http://localhost:8601"',
  PICTURE_API: '"http://localhost:8602"',
  WEB_API: '"http://localhost:8603"',
  Search_API: '"http://localhost:8605"',
  SPRING_BOOT_ADMIN: '"http://localhost:8606/wallboard"',
  SOLR_API: '"http://localhost:8080/solr"',
  Zipkin_Admin: '"http://localhost:9411/zipkin/"',
  ELASTIC_SEARCH: '"http://localhost:5601"',
  EUREKA_API: '"http://localhost:8761"',
  RABBIT_MQ_ADMIN: '"http://localhost:15672"',
  DRUID_ADMIN: '"http://localhost:8601/druid/login.html"',
  BLOG_WEB_URL: '"http://localhost:9527"',
  // 使用七牛云，不需要添加图片前缀
  BASE_IMAGE_URL: '""',
  // 使用本地存储服务
  // BASE_IMAGE_URL: '"http://localhost:8600"',

  //开发环境
  // ADMIN_API: '"http://localhost:8080/mogu_admin"',
  // PICTURE_API: '"http://localhost:8080/mogu_picture"',
  // WEB_API: '"http://localhost:8080/mogu_web"',
  // SOLR_API: '"http://localhost:8080/solr"'

})
