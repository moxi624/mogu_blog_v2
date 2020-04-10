import request from '@/utils/request'

export function getBlogList(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/getList',
    method: 'post',
    data: params
  })
}

export function addBlog(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/add',
    method: 'post',
    data: params
  })
}

export function uploadLocalBlog(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/uploadLocalBlog',
    method: 'post',
    data: params
  })
}


export function editBlog(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/edit',
    method: 'post',
    data: params
  })
}

export function editBatchBlog(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/editBatch',
    method: 'post',
    data: params
  })
}

export function deleteBlog(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/delete',
    method: 'post',
    data: params
  })
}

export function deleteBatchBlog(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/deleteBatch',
    method: 'post',
    data: params
  })
}
