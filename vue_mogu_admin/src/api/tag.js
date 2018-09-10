import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/tag/getList',
    method: 'get',
    params
  })
}
