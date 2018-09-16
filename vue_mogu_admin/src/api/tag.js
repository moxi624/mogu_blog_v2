import request from '@/utils/request'

export function getTagList(params) {
  return request({
    url: '/tag/getList',
    method: 'get',
    params
  })
}

export function addTag(params) {
  return request({
    url: '/tag/add',
    method: 'post',
    params
  })
}

export function editTag(params) {
  return request({
    url: '/tag/edit',
    method: 'post',
    params
  })
}

export function deleteTag(params) {
  return request({
    url: '/tag/delete',
    method: 'post',
    params
  })
}
