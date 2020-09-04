import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'

export function getTagList (params) {
  return request.get(appConfig.WEB_API + '/tag/getTagList', params)  
}

export function getArticleByTagUid (params) {
  return request.get(appConfig.WEB_API + '/tag/getArticleByTagUid', params)  
}