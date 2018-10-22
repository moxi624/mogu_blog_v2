import request from '@/utils/request'

export function init() {
  return request({
    url: process.env.ADMIN_API + '/index/init',
    method: 'get'
  })
}