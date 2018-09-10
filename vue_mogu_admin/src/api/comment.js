import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/comment/getList',
    method: 'get',
    params
  })
}
