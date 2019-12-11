import request from '@/utils/request'

export function getLinkList(params) {
  return request({
    url: process.env.ADMIN_API + '/link/getList',
    method: 'post',
    data: params
  })
}

export function addLink(params) {
  return request({
    url: process.env.ADMIN_API + '/link/add',
    method: 'post',
    data: params
  })
}

export function editLink(params) {
  return request({
    url: process.env.ADMIN_API + '/link/edit',
    method: 'post',
    data: params
  })
}

export function deleteLink(params) {
  return request({
    url: process.env.ADMIN_API + '/link/delete',
    method: 'post',
    data: params
  })
}

export function stickLink(params) {
  return request({
    url: process.env.ADMIN_API + '/link/stick',
    method: 'post',
    data: params
  })
}
