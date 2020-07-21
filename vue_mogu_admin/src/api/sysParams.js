import request from '@/utils/request'

export function getSysParamsList(params) {
  return request({
    url: process.env.ADMIN_API + '/sysParams/getList',
    method: 'post',
    data: params
  })
}

export function addSysParams(params) {
  return request({
    url: process.env.ADMIN_API + '/sysParams/add',
    method: 'post',
    data: params
  })
}

export function editSysParams(params) {
  return request({
    url: process.env.ADMIN_API + '/sysParams/edit',
    method: 'post',
    data: params
  })
}

export function deleteBatchSysParams(params) {
  return request({
    url: process.env.ADMIN_API + '/sysParams/deleteBatch',
    method: 'post',
    data: params
  })
}
