import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'

export function getMe (params) {
  return request.get(appConfig.WEB_API + '/about/getMe', params)  
}

// 获取网站配置
export function getWebConfig (params) {
  return request.get(appConfig.WEB_API + '/index/getWebConfig', params)  
}