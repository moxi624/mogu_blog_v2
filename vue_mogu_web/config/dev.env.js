'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',

	WEB_API: '"http://localhost:8603"',
	PICTURE_HOST: '"http://localhost:8600"',
	ELASTICSEARCH: '"http://localhost:8605"',

})
