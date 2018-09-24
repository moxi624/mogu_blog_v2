import request from '@/utils/request'

export function getIndex(params) {
  return request({
    url: process.env.WEB_API + '/index/getIndex',
    method: 'get',
    params
  })
}

