import request from '@/utils/request'

export function searchBlog (params) {
  return request({
    url: process.env.WEB_API + '/search/searchBlog',
    method: 'get',
    params
  })
}