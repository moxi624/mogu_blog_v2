import axios from "axios"
import web from './config/webconst'

export function getBlogByLevel (params) {
  return axios({
    url: web.WEB_API +'/index/getBlogByLevel',
    method: 'get',
    params
  })
}

export function getNewBlog (params) {
  return axios({
    url: web.WEB_API + '/index/getNewBlog',
    method: 'get',
    params
  })
}

export function getBlogByTime (params) {
  return axios({
    url: web.WEB_API + '/index/getBlogByTime',
    method: 'get',
    params
  })
}

export function getHotBlog (params) {
  return axios({
    url: web.WEB_API + '/index/getHotBlog',
    method: 'get',
    params
  })
}

export function getHotTag (params) {
  return axios({
    url: web.WEB_API + '/index/getHotTag',
    method: 'get',
    params
  })
}

export function getLink (params) {
  return axios({
    url: web.WEB_API + '/index/getLink',
    method: 'get',
    params
  })
}

export function addLinkCount (params) {
  return axios({
    url: web.WEB_API + '/index/addLinkCount',
    method: 'get',
    params
  })
}

export function getWebConfig (params) {
  return axios({
    url: web.WEB_API + '/index/getWebConfig',
    method: 'get',
    params
  })
}

export function getWebNavbar (params) {
  return axios({
    url: web.WEB_API + '/index/getWebNavbar',
    method: 'get',
    params
  })
}

export function recorderVisitPage (params) {
  return axios({
    url: web.WEB_API + '/index/recorderVisitPage',
    method: 'get',
    params
  })
}

export function fileUpload (urlIn,params) {
  return axios({
    url: urlIn,
    method: 'post',
    params
  })
}
