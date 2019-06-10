import request from '@/utils/request'

export function getPictureList(params) {
  return request({
    url: process.env.ADMIN_API + '/picture/getList',
    method: 'get',
    params
  })
}

export function addPicture(params) {
  return request({
    url: process.env.ADMIN_API + '/picture/add',
    method: 'post',
    params
  })
}

export function editPicture(params) {
  return request({
    url: process.env.ADMIN_API + '/picture/edit',
    method: 'post',
    params
  })
}

export function deletePicture(params) {
  return request({
    url: process.env.ADMIN_API + '/picture/delete',
    method: 'post',
    params
  })
}

export function setCover(params) {
  return request({
    url: process.env.ADMIN_API + '/picture/setCover',
    method: 'post',
    params
  })
}