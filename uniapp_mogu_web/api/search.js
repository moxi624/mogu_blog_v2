import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'

/**
 * 通过SQL搜索博客
 * @param params
 */
export function searchBlog (params) {
  return request.get(appConfig.WEB_API + '/search/sqlSearchBlog', params)  
}