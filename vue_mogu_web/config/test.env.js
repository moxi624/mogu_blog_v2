'use strict'
const merge = require('webpack-merge')
const devEnv = require('./dev.env')

module.exports = merge(devEnv, {
  NODE_ENV: '"testing"',
  
  //配置测试环境
 	PICTURE_API: '"http://localhost:8602"',
	WEB_API: '"http://localhost:8603"',
})
