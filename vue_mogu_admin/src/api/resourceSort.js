import request from '@/utils/request'

export function getResourceSortList(params) {
  return request({
    url: process.env.ADMIN_API + '/resourceSort/getList',
    method: 'post',
    data: params
  })
}

export function addResourceSort(params) {
  return request({
    url: process.env.ADMIN_API + '/resourceSort/add',
    method: 'post',
    data: params
  })
}

export function editResourceSort(params) {
  return request({
    url: process.env.ADMIN_API + '/resourceSort/edit',
    method: 'post',
    data: params
  })
}

export function deleteBatchResourceSort(params) {
  return request({
    url: process.env.ADMIN_API + '/resourceSort/deleteBatch',
    method: 'post',
    data: params
  })
}

export function stickResourceSort(params) {
  return request({
    url: process.env.ADMIN_API + '/resourceSort/stick',
    method: 'post',
    data: params
  })
}

