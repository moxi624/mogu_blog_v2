import request from '@/utils/request'

export function getBlogByLevel (params) {
  return request({
    url: process.env.WEB_API + '/index/getBlogByLevel',
    method: 'get',
    params
  })
}

export function getNewBlog (params) {
  return request({
    url: process.env.WEB_API + '/index/getNewBlog',
    method: 'get',
    params
  })
}

export function getHotBlog (params) {
  return request({
    url: process.env.WEB_API + '/index/getHotBlog',
    method: 'get',
    params
  })
}

export function getHotTag (params) {
  return request({
    url: process.env.WEB_API + '/index/getHotTag',
    method: 'get',
    params
  })
}

export function getLink (params) {
  return request({
    url: process.env.WEB_API + '/index/getLink',
    method: 'get',
    params
  })
}
