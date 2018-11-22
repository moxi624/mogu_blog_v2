import request from '@/utils/request'

export function getLogList(params) {
  return request({
    url: process.env.ADMIN_API + '/log/getLogList',
    method: 'get',
    params
  })
}

export function getExceptionLogList(params) {
  return request({
    url: process.env.ADMIN_API + '/log/getExceptionList',
    method: 'get',
    params
  })
}
