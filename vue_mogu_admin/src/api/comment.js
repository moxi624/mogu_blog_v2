import request from '@/utils/request'

export function getList(params) {
  return request({
    url: process.env.ADMIN_API + '/comment/getList',
    method: 'get',
    params
  })
}
