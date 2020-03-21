import request from '@/utils/request'

export function getMenuList(params) {
  return request({
    url: process.env.ADMIN_API + '/categoryMenu/getList',
    method: 'get',
    params
  })
}

export function getAllMenu(params) {
  return request({
    url: process.env.ADMIN_API + '/categoryMenu/getAll',
    method: 'get',
    params
  })
}

export function getButtonAll(params) {
  return request({
    url: process.env.ADMIN_API + '/categoryMenu/getButtonAll',
    method: 'get',
    params
  })
}

export function addMenu(params) {
  return request({
    url: process.env.ADMIN_API + '/categoryMenu/add',
    method: 'post',
    data: params
  })
}

export function editMenu(params) {
  return request({
    url: process.env.ADMIN_API + '/categoryMenu/edit',
    method: 'post',
    data: params
  })
}

export function deleteMenu(params) {
  return request({
    url: process.env.ADMIN_API + '/categoryMenu/delete',
    method: 'post',
    data: params
  })
}

export function stickMenu(params) {
  return request({
    url: process.env.ADMIN_API + '/categoryMenu/stick',
    method: 'post',
    data: params
  })
}

