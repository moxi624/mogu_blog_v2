import request from '@/utils/request'

export function login(param) {
  return request({
    url: process.env.ADMIN_API + '/auth/login',
    method: 'post',
    data: param
  })
}

export function getInfo(token) {
  return request({
    url: process.env.ADMIN_API + '/auth/info',
    method: 'get',
    params: { token }
  })
}

export function getMenu() {
  return request({
    url: process.env.ADMIN_API + '/auth/getMenu',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: process.env.ADMIN_API + '/auth/logout',
    method: 'post'
  })
}

export function getWebSiteName() {
  return request({
    url: process.env.ADMIN_API + '/auth/getWebSiteName',
    method: 'get'
  })
}
