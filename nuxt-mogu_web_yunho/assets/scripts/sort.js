import axios from "axios"
import web from './config/webconst'

export function getSortList (params) {
  return axios({
    url: web.WEB_API + '/sort/getSortList',
    method: 'get',
    params
  })
}

export function getArticleByMonth (params) {
  return axios({
    url: web.WEB_API + '/sort/getArticleByMonth',
    method: 'get',
    params
  })
}