import request from '@/utils/request'

export function getTagList(params) {
  return request({
    url: process.env.WEB_API + '/tag/getTagList',
    method: 'get',
    params
  })
}

export function getArticleByTagUid(params) {
  return request({
    url: process.env.WEB_API + '/tag/getArticleByTagUid',
    method: 'get',
    params
  })
}
