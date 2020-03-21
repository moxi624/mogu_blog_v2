import request from '@/utils/request'

export function getWebVisitList(params) {
  return request({
    url: process.env.ADMIN_API + '/webVisit/getList',
    method: 'post',
    data: params
  })
}
