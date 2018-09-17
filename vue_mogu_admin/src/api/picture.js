import request from '@/utils/request'

export function getPictureList(params) {
  return request({
    url: '/picture/getList',
    method: 'get',
    params
  })
}

export function addPicture(params) {
  return request({
    url: '/picture/add',
    method: 'post',
    params
  })
}

export function editPicture(params) {
  return request({
    url: '/picture/edit',
    method: 'post',
    params
  })
}

export function deletPicture(params) {
  return request({
    url: '/picture/delete',
    method: 'post',
    params
  })
}
