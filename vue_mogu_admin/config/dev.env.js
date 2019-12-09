'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',

  //开发环境
  ADMIN_API: '"http://localhost:8601"',
  PICTURE_API: '"http://localhost:8602"',
  WEB_API: '"http://localhost:8603"',
  SOLR_API: '"http://localhost:8080/solr"',
  BASE_IMAGE_URL: '"http://localhost:8600"',
  BLOG_WEB_URL: '"http://localhost:9527"',  

  //开发环境
  // ADMIN_API: '"http://localhost:8080/mogu_admin"',
  // PICTURE_API: '"http://localhost:8080/mogu_picture"',
  // WEB_API: '"http://localhost:8080/mogu_web"',
  // SOLR_API: '"http://localhost:8080/solr"'

})
