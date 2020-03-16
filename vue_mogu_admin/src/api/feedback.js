import request from '@/utils/request'

export function getFeedbackList(params) {
  return request({
    url: process.env.ADMIN_API + '/feedback/getList',
    method: 'post',
    data: params
  })
}

export function editFeedback(params) {
  return request({
    url: process.env.ADMIN_API + '/feedback/edit',
    method: 'post',
    data: params
  })
}

export function deleteBatchFeedback(params) {
  return request({
    url: process.env.ADMIN_API + '/feedback/deleteBatch',
    method: 'post',
    data: params
  })
}
