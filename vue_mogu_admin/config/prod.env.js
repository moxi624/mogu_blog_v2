'use strict'
module.exports = {
  NODE_ENV: '"production"',

    //生产环境
    ADMIN_API: '"http://101.132.194.128:8601"',
    PICTURE_API: '"http://101.132.194.128:8602"',
    WEB_API: '"http://101.132.194.128:8603"',
    SOLR_API: '"http://101.132.194.128:8080/solr"',
    BASE_IMAGE_URL: '"http://picture.moguit.cn"',
    BLOG_WEB_URL: '"http://www.moguit.cn"',

    //测试环境
    // ADMIN_API: '"http://192.168.1.101:8601"',
    // PICTURE_API: '"http://192.168.1.101:8602"',
    // WEB_API: '"http://192.168.1.101:8603"',
    // SOLR_API: '"http://192.168.1.101:8080/solr"',
    // BASE_IMAGE_URL: '"http://192.168.1.101:8600"',
    // BLOG_WEB_URL: '"http://192.168.1.101:9527"',


    // BLOG_WEB_URL: '"http://101.132.194.128:9527"',

    //生产环境 tomcat方式部署
    // ADMIN_API: '"http://101.132.194.128:8080/mogu_admin"',
    // PICTURE_API: '"http://101.132.194.128:8080/mogu_picture"',
    // WEB_API: '"http://101.132.194.128:8080/mogu_web"',
    // SOLR_API: '"http://101.132.194.128:8080/solr"',
    // BASE_IMAGE_URL: '"http://101.132.194.128:8600"',
    //   BLOG_WEB_URL: '"http://www.moguit.cn"',


}
