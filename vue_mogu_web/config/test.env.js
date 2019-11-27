'use strict'
const merge = require('webpack-merge')
const devEnv = require('./dev.env')

module.exports = merge(devEnv, {
  NODE_ENV: '"testing"',
  
  //配置测试环境
	WEB_API: '"http://localhost:8603"',
  PICTURE_HOST: '"http://localhost:8600"',
  
})
