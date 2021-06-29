import axios from "axios"
import web from './config/webconst'

export function getSubjectList(params) {
  return axios({
    url: web.WEB_API + '/subject/getList',
    method: 'post',
    data: params
  })
}

export function getSubjectItemList(params) {
  return axios({
    url: web.WEB_API + '/subject/getItemList',
    method: 'post',
    data: params
  })
}



