
import request from '../utils/request'
let config = require('~/config/sysConfig')

export function searchBlog (params) {
  return request({
    url: process.env.WEB_API + '/search/searchBlog',
    method: 'get',
    params
  })
}

export function searchBlogByTag (params) {
  return request({
    url: process.env.WEB_API + '/search/searchBlogByTag',
    method: 'get',
    params
  })
}

export function searchBlogBySort (params) {
  return request({
    url: process.env.WEB_API + '/search/searchBlogBySort',
    method: 'get',
    params
  })
}

export function searchBlogByAuthor (params) {
  return request({
    url: process.env.WEB_API + '/search/searchBlogByAuthor',
    method: 'get',
    params
  })
}
