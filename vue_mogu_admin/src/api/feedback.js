import request from '@/utils/request'

export function getList(params) {
  return request({
    url: process.env.ADMIN_API + '/feedback/getList',
    method: 'get',
    params
  })
}
