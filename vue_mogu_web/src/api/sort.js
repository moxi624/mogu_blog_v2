import request from '@/utils/request'

export function getSortList (params) {
  return request({
    url: process.env.WEB_API + '/sort/getSortList',
    method: 'get',
    params
  })
}

export function getArticleByMonth (params) {
  return request({
    url: process.env.WEB_API + '/sort/getArticleByMonth',
    method: 'get',
    params
  })
}