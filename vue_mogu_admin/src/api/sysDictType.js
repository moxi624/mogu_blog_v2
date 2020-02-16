import request from '@/utils/request'

export function getSysDictTypeList(params) {
  return request({
    url: process.env.ADMIN_API + '/sysDictType/getList',
    method: 'post',
    data: params
  })
}

export function addSysDictType(params) {
  return request({
    url: process.env.ADMIN_API + '/sysDictType/add',
    method: 'post',
    data: params
  })
}

export function editSysDictType(params) {
  return request({
    url: process.env.ADMIN_API + '/sysDictType/edit',
    method: 'post',
    data: params
  })
}

export function deleteBatchSysDictType(params) {
  return request({
    url: process.env.ADMIN_API + '/sysDictType/deleteBatch',
    method: 'post',
    data: params
  })
}
