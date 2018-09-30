import request from '@/utils/request'

export function searchBlog(params) {
  return request({
    url: process.env.ADMIN_API + '/search/searchBlog',
    method: 'get',
    params
  })
}

export function initIndex(params) {
  return request({
    url: process.env.ADMIN_API + '/search/initIndex',
    method: 'post',
    params
  })
}

