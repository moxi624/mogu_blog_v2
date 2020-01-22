import request from '@/utils/request'

export function getCommentList(params) {
  return request({
    url: process.env.ADMIN_API + '/comment/getList',
    method: 'post',
    data: params
  })
}

export function addComment(params) {
  return request({
    url: process.env.ADMIN_API + '/comment/add',
    method: 'post',
    data: params
  })
}

export function editComment(params) {
  return request({
    url: process.env.ADMIN_API + '/comment/edit',
    method: 'post',
    data: params
  })
}

export function deleteComment(params) {
  return request({
    url: process.env.ADMIN_API + '/comment/delete',
    method: 'post',
    data: params
  })
}

export function deleteBatchComment(params) {
  return request({
    url: process.env.ADMIN_API + '/comment/deleteBatch',
    method: 'post',
    data: params
  })
}

