import request from '@/utils/request'

export function getRoleList(params) {
  return request({
    url: process.env.ADMIN_API + '/role/getList',
    method: 'post',
    data: params
  })
}

export function addRole(params) {
  return request({
    url: process.env.ADMIN_API + '/role/add',
    method: 'post',
    data: params
  })
}

export function editRole(params) {
  return request({
    url: process.env.ADMIN_API + '/role/edit',
    method: 'post',
    data: params
  })
}

export function deleteRole(params) {
  return request({
    url: process.env.ADMIN_API + '/role/delete',
    method: 'post',
    data: params
  })
}
