import request from '@/utils/request'

export function getSubjectList(params) {
  return request({
    url: process.env.ADMIN_API + '/subject/getList',
    method: 'post',
    data: params
  })
}

export function addSubject(params) {
  return request({
    url: process.env.ADMIN_API + '/subject/add',
    method: 'post',
    data: params
  })
}

export function editSubject(params) {
  return request({
    url: process.env.ADMIN_API + '/subject/edit',
    method: 'post',
    data: params
  })
}

export function deleteBatchSubject(params) {
  return request({
    url: process.env.ADMIN_API + '/subject/deleteBatch',
    method: 'post',
    data: params
  })
}


