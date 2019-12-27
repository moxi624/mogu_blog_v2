import request from '@/utils/request'

export function login(params) {
  return request({
    url: process.env.WEB_API + '/oauth/render',
    method: 'post',
    params
  })
}

export function authVerify(params) {
  return request({
    url: process.env.WEB_API + '/oauth/verify/' + params,
    method: 'get',
  })
}

export function deleteUserAccessToken(params) {
  return request({
    url: process.env.WEB_API + '/oauth/delete/' + params,
    method: 'post',
  })
}

export function register(params) {
  return request({
    url: process.env.WEB_API + '/user/register',
    method: 'post',
    data: params
  })
}

export function logout(params) {
  return request({
    url: process.env.WEB_API + '/user/logout',
    method: 'post',
    data: params
  })
}
