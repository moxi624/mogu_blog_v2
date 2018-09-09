import request from '@/utils/request'

export function login(param) {
  return request({
    url: '/admin/login',
    method: 'post',
    data: param
  })
}

export function getInfo(token) {
  return request({
    url: '/admin/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/admin/logout',
    method: 'post'
  })
}
