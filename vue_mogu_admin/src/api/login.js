import request from '@/utils/request'

export function login(param) {
  return request({
    url: process.env.ADMIN_API + '/admin/login',
    method: 'post',
    data: param
  })
}

export function getInfo(token) {
  return request({
    url: process.env.ADMIN_API + '/admin/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: process.env.ADMIN_API + '/admin/logout',
    method: 'post'
  })
}
