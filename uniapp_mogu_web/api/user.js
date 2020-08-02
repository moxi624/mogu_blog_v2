import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'

// 申请友链
export function replyBlogLink (params) {
  return request.post(appConfig.WEB_API + '/oauth/replyBlogLink', params)  
}

// 新增反馈
export function addFeedback (params) {
  return request.post(appConfig.WEB_API + '/oauth/addFeedback', params)  
}
