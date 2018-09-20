import request from '@/utils/request'

export function getPictureSortList(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/getList',
    method: 'get',
    params
  })
}

export function addPictureSort(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/add',
    method: 'post',
    params
  })
}

export function editPictureSort(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/edit',
    method: 'post',
    params
  })
}

export function deletPictureSort(params) {
  return request({
    url: process.env.ADMIN_API + '/pictureSort/delete',
    method: 'post',
    params
  })
}
