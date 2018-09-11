import request from '@/utils/request'

export function getBlogList(params) {
  return request({
    url: '/blog/getList',
    method: 'get',
    params
  })
}
