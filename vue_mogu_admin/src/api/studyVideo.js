import request from '@/utils/request'

export function getStudyVideoList(params) {
  return request({
    url: process.env.ADMIN_API + '/studyVideo/getList',
    method: 'get',
    params
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

export function deleteStudyVideo(params) {
  return request({
    url: process.env.ADMIN_API + '/studyVideo/delete',
    method: 'post',
    params
  })
}
