import request from '@/utils/request'

export function getLinkList(params) {
  return request({
    url: process.env.ADMIN_API + '/link/getList',
    method: 'get',
    params
  })
}

export function addLink(params) {
  return request({
    url: process.env.ADMIN_API + '/link/add',
    method: 'post',
    params
  })
}

export function editLink(params) {
  return request({
    url: process.env.ADMIN_API + '/link/edit',
    method: 'post',
    params
  })
}

export function deleteLink(params) {
  return request({
    url: process.env.ADMIN_API + '/link/delete',
    method: 'post',
    params
  })
}

export function stickLink(params) {
  return request({
    url: process.env.ADMIN_API + '/link/stick',
    method: 'post',
    params
  })
}
