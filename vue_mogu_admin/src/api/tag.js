import request from '@/utils/request'

export function getTagList(params) {
  return request({
    url: process.env.ADMIN_API + '/tag/getList',
    method: 'get',
    params
  })
}

export function addTag(params) {
  return request({
    url: process.env.ADMIN_API + '/tag/add',
    method: 'post',
    params
  })
}

export function editTag(params) {
  return request({
    url: process.env.ADMIN_API + '/tag/edit',
    method: 'post',
    params
  })
}

export function deleteTag(params) {
  return request({
    url: process.env.ADMIN_API + '/tag/delete',
    method: 'post',
    params
  })
}
