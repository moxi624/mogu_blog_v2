import request from '@/utils/request'

export function getMe (params) {
  return request({
    url: process.env.WEB_API + '/about/getMe',
    method: 'get',
    params
  })
}

export function getContact (params) {
    return request({
      url: process.env.WEB_API + '/about/getContact',
      method: 'get',
      params
    })
  }