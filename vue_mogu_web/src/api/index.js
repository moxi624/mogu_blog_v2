import request from '@/utils/request'

export function getBanner (params) {
  return request({
    url: process.env.WEB_API + '/index/getBanner',
    method: 'get',
    params
  })
}

export function getTopic (params) {
  return request({
    url: process.env.WEB_API + '/index/getTopic',
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

export function getHotTag (params) {
  return request({
    url: process.env.WEB_API + '/index/getHotTag',
    method: 'get',
    params
  })
}
