import request from '@/utils/request'

export function getCommentList (params) {
  return request({
    url: process.env.WEB_API + '/web/comment/getList',
    method: 'post',
    data: params
  })
}

export function addComment (params) {
  return request({
    url: process.env.WEB_API + '/web/comment/add',
    method: 'post',
    data: params
  })
}

export function deleteBatchComment (params) {
  return request({
    url: process.env.WEB_API + '/web/comment/deleteBatch',
    method: 'post',
    data: params
  })
}
