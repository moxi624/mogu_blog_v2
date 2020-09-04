'use strict'
module.exports = {
  NODE_ENV: '"production"',

  //生产环境
  ADMIN_API: '"http://47.115.54.30:8601"',
  PICTURE_API: '"http://47.115.54.30:8602"',
  WEB_API: '"http://47.115.54.30:8603"',
  Search_API: '"http://47.115.54.30:8605"',
  SPRING_BOOT_ADMIN: '"http://47.115.54.30:8606/wallboard"',
  SOLR_API: '"http://47.115.54.30:8080/solr"',
  Zipkin_Admin: '"http://47.115.54.30:9411/zipkin/"',
  ELASTIC_SEARCH: '"http://47.115.54.30:5601"',
  EUREKA_API: '"http://47.115.54.30:8761"',
  RABBIT_MQ_ADMIN: '"http://47.115.54.30:15672"',
  DRUID_ADMIN: '"http://47.115.54.30:8601/druid/login.html"',
  // 有域名
  BLOG_WEB_URL: '"http://demoweb.moguit.cn"',
  // 没有域名
  // BLOG_WEB_URL: '"http://47.115.54.30:9527"',
}
