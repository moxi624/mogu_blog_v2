import axios from "axios"
import web from './config/webconst'

export function getCommentList (params) {
  return axios({
    url: web.WEB_API + '/web/comment/getList',
    method: 'post',
    data: params
  })
}

export function getCommentListByUser (params) {
  return axios({
    url: web.WEB_API + '/web/comment/getListByUser',
    method: 'post',
    data: params
  })
}

export function getPraiseListByUser (params) {
  return axios({
    url: web.WEB_API + '/web/comment/getPraiseListByUser',
    method: 'post',
    data: params
  })
}

export function addComment (params) {
  return axios({
    url: web.WEB_API + '/web/comment/add',
    method: 'post',
    data: params
  })
}

export function deleteComment (params) {
  return axios({
    url: web.WEB_API + '/web/comment/delete',
    method: 'post',
    data: params
  })
}

export function reportComment (params) {
  return axios({
    url: web.WEB_API + '/web/comment/report',
    method: 'post',
    data: params
  })
}

export function getUserReceiveCommentCount (params) {
  return axios({
    url: web.WEB_API + '/web/comment/getUserReceiveCommentCount',
    method: 'get',
    params
  })
}

export function readUserReceiveCommentCount (params) {
  return axios({
    url: web.WEB_API + '/web/comment/readUserReceiveCommentCount',
    method: 'post',
    params
  })
}

