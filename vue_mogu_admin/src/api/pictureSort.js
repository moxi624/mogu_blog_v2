import request from '@/utils/request'

export function getPictureSortList(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/getList',
    method: 'post',
    data: params
  })
}

export function addPictureSort(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/add',
    method: 'post',
    data: params
  })
}

export function editPictureSort(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/edit',
    method: 'post',
    data: params
  })
}

export function deletePictureSort(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/delete',
    method: 'post',
    data: params
  })
}

export function stickPictureSort(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/stick',
    method: 'post',
    data: params
  })
}

export function getPictureSortByUid(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/getPictureSortByUid',
    method: 'post',
    data: params
  })
}
