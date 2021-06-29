import axios from "axios"
import web from './config/webconst'

export function getBlogSortList(params) {
  return axios({
    url: web.WEB_API + '/classify/getBlogSortList',
    method: 'get',
    params
  })
}

export function getArticleByBlogSortUid(params) {
  return axios({
    url: web.WEB_API + '/classify/getArticleByBlogSortUid',
    method: 'get',
    params
  })
}