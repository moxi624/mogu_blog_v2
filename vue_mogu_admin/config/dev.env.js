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
  SENTINEL_ADMIN: '"http://localhost:8070/sentinel/"',
  ELASTIC_SEARCH: '"http://localhost:5601"',
  EUREKA_API: '"http://localhost:8761"',
  RABBIT_MQ_ADMIN: '"http://localhost:15672"',
  DRUID_ADMIN: '"http://localhost:8601/druid/login.html"',
  BLOG_WEB_URL: '"http://localhost:9527"',

})
