import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/user/getList',
    method: 'get',
    params
  })
}
