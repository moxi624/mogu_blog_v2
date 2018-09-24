import request from '@/utils/request'

export function getBlogSortList(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/getList',
    method: 'get',
    params
  })
}

export function addBlogSort(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/add',
    method: 'post',
    params
  })
}

export function editBlogSort(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/edit',
    method: 'post',
    params
  })
}

export function deleteBlogSort(params) {
  return request({
    url: process.env.ADMIN_API + '/blogSort/delete',
    method: 'post',
    params
  })
}
