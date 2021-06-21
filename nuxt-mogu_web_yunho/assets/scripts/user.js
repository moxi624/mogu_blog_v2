import axios from "axios"
import web from './config/webconst'

/**
 * 第三方登录
 * @param params
 */
export function login(params) {
  return axios({
    url: web.WEB_API + '/oauth/render',
    method: 'post',
    params
  })
}

export function authVerify(params) {
  return axios({
    url: web.WEB_API + '/oauth/verify/' + params,
    method: 'get',
  })
}

export function editUser(params) {
  return axios({
    url: web.WEB_API + '/oauth/editUser',
    method: 'post',
    data: params
  })
}

/**
 * 更新用户密码
 * @param params
 */
export function updateUserPwd(params) {
  return axios({
    url: web.WEB_API + '/oauth/updateUserPwd',
    method: 'post',
    data: params
  })
}

/**
 * 获取用户反馈
 * @param params
 */
export function getFeedbackList(params) {
  return axios({
    url: web.WEB_API + '/oauth/getFeedbackList',
    method: 'get',
    params
  })
}

/**
 * 新增反馈
 * @param params
 */
export function addFeedback(params) {
  return axios({
    url: web.WEB_API + '/oauth/addFeedback',
    method: 'post',
    data: params
  })
}

export function replyBlogLink(params) {
  return axios({
    url: web.WEB_API + '/oauth/replyBlogLink',
    method: 'post',
    data: params
  })
}

export function deleteUserAccessToken(params) {
  return axios({
    url: web.WEB_API + '/oauth/delete/' + params,
    method: 'post',
  })
}

/**
 * 本地登录
 * @param params
 */
export function localLogin(params) {
  return axios({
    url: web.WEB_API + '/login/login',
    method: 'post',
    data: params
  })
}

/**
 * 本地注册
 * @param params
 */
export function localRegister(params) {
  return axios({
    url: web.WEB_API + '/login/register',
    method: 'post',
    data: params
  })
}

export function logout(params) {
  return axios({
    url: web.WEB_API + '/user/logout',
    method: 'post',
    data: params
  })
}

/**
 * 获取微信公众号登录二维码
 * @param params
 * @returns {*}
 */
export function getWechatOrCodeTicket(params) {
  return axios({
    url: web.WEB_API + '/wechat/getWechatOrCodeTicket',
    method: 'get',
    data: params
  })
}

export function getUserLoginStatus(params) {
  return axios({
    url: web.WEB_API + '/wechat/getUserLoginStatus',
    method: 'get',
    params
  })
}
