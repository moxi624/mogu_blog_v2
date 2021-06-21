import axios from "axios"
import web from './config/webconst'
export function getMe (params) {
  return axios({
    url: web.WEB_API + '/about/getMe',
    method: 'get',
    params
  })
}

export function getContact (params) {
    return axios({
      url: web.WEB_API + '/about/getContact',
      method: 'get',
      params
    })
  }