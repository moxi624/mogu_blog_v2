import request from '@/utils/request'

export function getResourceSortList(params) {
  return request({
    url: process.env.ADMIN_API + '/resourceSort/getList',
    method: 'get',
    params
  })
}

export function addResourceSort(params) {
  return request({
    url: process.env.ADMIN_API + '/resourceSort/add',
    method: 'post',
    params
  })
}

export function editResourceSort(params) {
  return request({
    url: process.env.ADMIN_API + '/resourceSort/edit',
    method: 'post',
    params
  })
}

export function deleteResourceSort(params) {
  return request({
    url: process.env.ADMIN_API + '/resourceSort/delete',
    method: 'post',
    params
  })
}

export function stickResourceSort(params) {
  return request({
    url: process.env.ADMIN_API + '/resourceSort/stick',
    method: 'post',
    params
  })
}

