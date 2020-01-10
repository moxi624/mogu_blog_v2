import request from '@/utils/request'

export function getStudyVideoList(params) {
  return request({
    url: process.env.ADMIN_API + '/studyVideo/getList',
    method: 'post',
    data: params
  })
}

export function addStudyVideo(params) {
  return request({
    url: process.env.ADMIN_API + '/studyVideo/add',
    method: 'post',
    data: params
  })
}

export function editStudyVideo(params) {
  return request({
    url: process.env.ADMIN_API + '/studyVideo/edit',
    method: 'post',
    data: params
  })
}

export function deleteBatchStudyVideo(params) {
  return request({
    url: process.env.ADMIN_API + '/studyVideo/deleteBatch',
    method: 'post',
    data: params
  })
}
