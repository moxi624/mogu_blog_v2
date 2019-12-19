import request from '@/utils/request'

export function getList(params) {
  return request({
    url: process.env.ADMIN_API + '/todo/getList',
    method: 'post',
    data: params
  })
}

export function addTodo(params) {
  return request({
    url: process.env.ADMIN_API + '/todo/add',
    method: 'post',
    data: params
  })
}

export function editTodo(params) {
  return request({
    url: process.env.ADMIN_API + '/todo/edit',
    method: 'post',
    data: params
  })
}

export function deleteTodo(params) {
  return request({
    url: process.env.ADMIN_API + '/todo/delete',
    method: 'post',
    data: params
  })
}

export function toggleAll(params) {
  return request({
    url: process.env.ADMIN_API + '/todo/toggleAll',
    method: 'post',
    data: params
  })
}
