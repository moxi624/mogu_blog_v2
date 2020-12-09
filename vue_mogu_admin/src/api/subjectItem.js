import request from '@/utils/request'

export function getSubjectItemList(params) {
  return request({
    url: process.env.ADMIN_API + '/subjectItem/getList',
    method: 'post',
    data: params
  })
}

export function addSubjectItemList(params) {
  return request({
    url: process.env.ADMIN_API + '/subjectItem/add',
    method: 'post',
    data: params
  })
}

export function editSubjectItem(params) {
  return request({
    url: process.env.ADMIN_API + '/subjectItem/edit',
    method: 'post',
    data: params
  })
}

export function deleteBatchSubjectItem(params) {
  return request({
    url: process.env.ADMIN_API + '/subjectItem/deleteBatch',
    method: 'post',
    data: params
  })
}

export function sortByCreateTime(params) {
  return request({
    url: process.env.ADMIN_API + '/subjectItem/sortByCreateTime',
    method: 'post',
    data: params
  })
}


