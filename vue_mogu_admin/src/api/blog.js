import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/blog/getList',
    method: 'get',
    params
  })
}
