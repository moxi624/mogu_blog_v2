import request from '@/utils/request'

export function getUserList(params) {
  return request({
    url: process.env.ADMIN_API + '/user/getList',
    method: 'post',
    data: params
  })
}

export function deleteUser(params) {
  return request({
    url: process.env.ADMIN_API + '/user/delete',
    method: 'post',
    data: params
  })
}

export function freezeUser(params) {
  return request({
    url: process.env.ADMIN_API + '/user/freeze',
    method: 'post',
    data: params
  })
}
