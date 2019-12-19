import request from '@/utils/request'

export function getWebVisitList(params) {
  return request({
    url: process.env.ADMIN_API + '/webConfig/getList',
    method: 'post',
    data: params
  })
}
