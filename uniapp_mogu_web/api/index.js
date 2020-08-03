import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'

export function getBlogByLevel (params) {
  return request.get(appConfig.WEB_API + '/index/getBlogByLevel', params)  
}

export function getNewBlog (params) {
  return request.get(appConfig.WEB_API + '/index/getNewBlog', params)  
}

export function getLink (params) {
  return request.get(appConfig.WEB_API + '/index/getLink', params)  
}