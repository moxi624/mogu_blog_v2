import request from '@/utils/request'

export function getCommentList(params) {
  return request({
    url: process.env.ADMIN_API + '/comment/getList',
    method: 'get',
    params
  })
}

export function addComment(params) {
  return request({
    url: process.env.ADMIN_API + '/comment/add',
    method: 'post',
    params
  })
}

export function editComment(params) {
  return request({
    url: process.env.ADMIN_API + '/comment/edit',
    method: 'post',
    params
  })
}

export function deleteComment(params) {
  return request({
    url: process.env.ADMIN_API + '/comment/delete',
    method: 'post',
    params
  })
}

