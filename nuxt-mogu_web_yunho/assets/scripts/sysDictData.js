import axios from "axios"
import web from './config/webconst'
export function getListByDictType(params) {
  return axios({
    url: web.WEB_API + '/sysDictData/getListByDictType',
    method: 'post',
    params
  })
}

export function getListByDictTypeList(params) {
  return axios({
    url: web.WEB_API + '/sysDictData/getListByDictTypeList',
    method: 'post',
    data: params
  })
}
