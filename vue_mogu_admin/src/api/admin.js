import request from '@/utils/request'

/**
 * 获取管理员列表
 * @param params
 */
export function getAdminList(params) {
  return request({
    url: process.env.ADMIN_API + '/admin/getList',
    method: 'get',
    params
  })
}

export function addAdmin(params) {
  return request({
    url: process.env.ADMIN_API + '/admin/add',
    method: 'post',
    data: params
  })
}

export function editAdmin(params) {
  return request({
    url: process.env.ADMIN_API + '/admin/edit',
    method: 'post',
    data: params
  })
}

export function deleteAdmin(params) {
  return request({
    url: process.env.ADMIN_API + '/admin/delete',
    method: 'post',
    params
  })
}

export function restPwdAdmin(params) {
  return request({
    url: process.env.ADMIN_API + '/admin/restPwd',
    method: 'post',
    params
  })
}

/**
 * 获取在线的管理员列表
 * @param params
 */
export function getOnlineAdminList(params) {
  return request({
    url: process.env.ADMIN_API + '/admin/getOnlineAdminList',
    method: 'get',
    params
  })
}

/**
 * 强退用户
 * @param params
 */
export function forceLogout(params) {
  return request({
    url: process.env.ADMIN_API + '/admin/forceLogout',
    method: 'post',
    data: params
  })
}
