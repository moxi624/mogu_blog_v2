import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/collect/getList',
    method: 'get',
    params
  })
}
