import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'

export function getSortList (params) {
  return request.get(appConfig.WEB_API + '/sort/getSortList', params)  
}

export function getArticleByMonth (params) {
  return request.get(appConfig.WEB_API + '/sort/getArticleByMonth', params)  
}