import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/feedback/getList',
    method: 'get',
    params
  })
}
