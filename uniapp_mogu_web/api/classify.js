import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'

export function getBlogSortList (params) {
  return request.get(appConfig.WEB_API + '/classify/getBlogSortList', params)  
}

export function getArticleByBlogSortUid (params) {
  return request.get(appConfig.WEB_API + '/classify/getArticleByBlogSortUid', params)  
}