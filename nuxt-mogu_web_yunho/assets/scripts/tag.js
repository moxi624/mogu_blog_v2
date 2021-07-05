import axios from "axios"
import web from './config/webconst'

export function getTagList(params) {
  return axios({
    url: web.WEB_API + '/tag/getTagList',
    method: 'get',
    params
  })
}

export function getArticleByTagUid(params) {
  return axios({
    url: web.WEB_API + '/tag/getArticleByTagUid',
    method: 'get',
    params
  })
}
