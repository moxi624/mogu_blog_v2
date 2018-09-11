import request from '@/utils/request'

export function getBlogList(params) {
  return request({
    url: '/blog/getList',
    method: 'get',
    params
  })
}

export function addBlog(params) {
  return request({
    url: '/blog/add',
    method: 'post',
    params
  })
}

export function editBlog(params) {
  return request({
    url: '/blog/edit',
    method: 'post',
    params
  })
}

export function deleteBlog(params) {
  return request({
    url: '/blog/delete',
    method: 'post',
    params
  })
}
