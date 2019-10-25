import request from '@/utils/request'

export function getSortByMonth (params) {
  return request({
    url: process.env.WEB_API + '/sort/getSortByMonth',
    method: 'get',
    params
  })
}