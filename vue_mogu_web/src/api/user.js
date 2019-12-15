import request from '@/utils/request'

export function login(params) {
  return request({
    url: process.env.WEB_API + '/user/login',
    method: 'post',
    data: params
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