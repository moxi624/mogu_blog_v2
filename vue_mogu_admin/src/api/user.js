import request from '@/utils/request'

export function getUserList(params) {
  return request({
    url: process.env.ADMIN_API + '/user/getList',
    method: 'post',
    data: params
  })
}

export function addUser(params) {
  return request({
    url: process.env.ADMIN_API + '/user/add',
    method: 'post',
    data: params
  })
}


export function editUser(params) {
  return request({
    url: process.env.ADMIN_API + '/user/edit',
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

export function resetUserPassword(params) {
  return request({
    url: process.env.ADMIN_API + '/user/resetUserPassword',
    method: 'post',
    data: params
  })
}
