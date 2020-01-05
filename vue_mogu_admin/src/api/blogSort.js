import request from '@/utils/request'

export function getBlogSortList(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/getList',
    method: 'post',
    data: params
  })
}

export function addBlogSort(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/add',
    method: 'post',
    data: params
  })
}

export function editBlogSort(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/edit',
    method: 'post',
    data: params
  })
}

export function deleteBatchBlogSort(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/deleteBatch',
    method: 'post',
    data: params
  })
}

export function stickBlogSort(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/stick',
    method: 'post',
    data: params
  })
}

export function blogSortByClickCount(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/blogSortByClickCount',
    method: 'post',
    params
  })
}

export function blogSortByCite(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/blogSortByCite',
    method: 'post',
    params
  })
}
