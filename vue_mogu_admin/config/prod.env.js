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
  ELASTIC_SEARCH: '"http://47.115.54.30:5601"',
  EUREKA_API: '"http://47.115.54.30:8761"',
  RABBIT_MQ_ADMIN: '"http://47.115.54.30:15672"',
  DRUID_ADMIN: '"http://47.115.54.30:8601/druid/login.html"',
  BLOG_WEB_URL: '"http://demoweb.moguit.cn"',
  // 使用七牛云，不需要添加图片前缀
  BASE_IMAGE_URL: '""',

    //测试环境
    // ADMIN_API: '"http://192.168.1.101:8601"',
    // PICTURE_API: '"http://192.168.1.101:8602"',
    // WEB_API: '"http://192.168.1.101:8603"',
    // SOLR_API: '"http://192.168.1.101:8080/solr"',
    // BASE_IMAGE_URL: '"http://192.168.1.101:8600"',
    // BLOG_WEB_URL: '"http://192.168.1.101:9527"',


    // BLOG_WEB_URL: '"http://47.115.54.30:9527"',

    //生产环境 tomcat方式部署
    // ADMIN_API: '"http://47.115.54.30:8080/mogu_admin"',
    // PICTURE_API: '"http://47.115.54.30:8080/mogu_picture"',
    // WEB_API: '"http://47.115.54.30:8080/mogu_web"',
    // SOLR_API: '"http://47.115.54.30:8080/solr"',
    // BASE_IMAGE_URL: '"http://47.115.54.30:8600"',
    //   BLOG_WEB_URL: '"http://www.moguit.cn"',


}
