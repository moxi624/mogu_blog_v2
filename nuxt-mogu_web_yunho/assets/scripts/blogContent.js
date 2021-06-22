import axios from "axios"
import web from './config/webconst'

export function getBlogByUid (params) {
  return axios({
    url: web.WEB_API + '/content/getBlogByUid',
    method: 'get',
    params
  })
}

export function getSameBlogByTagUid (params) {
  return axios({
    url: web.WEB_API + '/content/getSameBlogByTagUid',
    method: 'get',
    params
  })
}

export function getSameBlogByBlogUid (params) {
  return axios({
    url: web.WEB_API + '/content/getSameBlogByBlogUid',
    method: 'get',
    params
  })
}

export function praiseBlogByUid (params) {
  return axios({
    url: web.WEB_API + '/content/praiseBlogByUid',
    method: 'get',
    params
  })
}

export function getBlogPraiseCountByUid (params) {
  return axios({
    url: web.WEB_API + '/content/getBlogPraiseCountByUid',
    method: 'get',
    params
  })
}