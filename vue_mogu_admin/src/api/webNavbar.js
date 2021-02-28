import request from '@/utils/request'

export function getWebNavbarList(params) {
  return request({
    url: process.env.ADMIN_API + '/webNavbar/getList',
    method: 'get',
    params
  })
}

export function getWebNavbarAllList(params) {
  return request({
    url: process.env.ADMIN_API + '/webNavbar/getAllList',
    method: 'get',
    params
  })
}

export function addWebNavbar(params) {
  return request({
    url: process.env.ADMIN_API + '/webNavbar/add',
    method: 'post',
    data: params
  })
}

export function editWebNavbar(params) {
  return request({
    url: process.env.ADMIN_API + '/webNavbar/edit',
    method: 'post',
    data: params
  })
}

export function deleteWebNavbar(params) {
  return request({
    url: process.env.ADMIN_API + '/webNavbar/delete',
    method: 'post',
    data: params
  })
}

