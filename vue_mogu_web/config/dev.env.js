'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  
 	// BASE_BLOG_API: '"http://localhost:9527"',
 	// PICTURE_API: '"http://localhost:8602"',
	// WEB_API: '"http://localhost:8603"',

	BASE_BLOG_API: '"http://localhost:8080/mogu_blog"',
	PICTURE_API: '"http://localhost:8080/mogu_picture"',
	WEB_API: '"http://localhost:8080/mogu_web"',

})
