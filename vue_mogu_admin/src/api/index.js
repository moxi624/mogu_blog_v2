import request from '@/utils/request'

export function init() {
  return request({
    url: process.env.ADMIN_API + '/index/init',
    method: 'get'
  })
}

export function getVisitByWeek() {
  return request({
    url: process.env.ADMIN_API + '/index/getVisitByWeek',
    method: 'get'
  })
}

export function getBlogCountByTag() {
  return request({
    url: process.env.ADMIN_API + '/index/getBlogCountByTag',
    method: 'get'
  })
}

export function getBlogCountByBlogSort() {
  return request({
    url: process.env.ADMIN_API + '/index/getBlogCountByBlogSort',
    method: 'get'
  })
}

export function getBlogContributeCount() {
  return request({
    url: process.env.ADMIN_API + '/index/getBlogContributeCount',
    method: 'get'
  })
}
