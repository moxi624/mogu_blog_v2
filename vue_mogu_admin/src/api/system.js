import request from '@/utils/request'

export function getMe(params) {
  return request({
    url: process.env.ADMIN_API + '/system/getMe',
    method: 'get',
    params
  })
}

export function editMe(params) {
  return request({
    url: process.env.ADMIN_API + '/system/editMe',
    method: 'post',
    data: params
  })
}

export function changePwd(params) {
  return request({
    url: process.env.ADMIN_API + '/system/changePwd',
    method: 'post',
    data: params
  })
}
