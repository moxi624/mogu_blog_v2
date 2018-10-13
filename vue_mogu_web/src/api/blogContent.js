import request from '@/utils/request'

export function getBlogByUid (params) {
  return request({
    url: process.env.WEB_API + '/content/getBlogByUid',
    method: 'get',
    params
  })
}

export function getSameBlog (params) {
  return request({
    url: process.env.WEB_API + '/content/getSameBlog',
    method: 'get',
    params
  })
}