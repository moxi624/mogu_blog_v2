import request from '@/utils/request'

export function getSystemConfig(params) {
  return request({
    url: process.env.ADMIN_API + '/systemConfig/getSystemConfig',
    method: 'get',
    params
  })
}

export function editSystemConfig(params) {
  return request({
    url: process.env.ADMIN_API + '/systemConfig/editSystemConfig',
    method: 'post',
    data: params
  })
}
