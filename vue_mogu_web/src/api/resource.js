import request from '@/utils/request'

export function getStudyVideoBySort (params) {
  return request({
    url: process.env.WEB_API + '/resource/getStudyVideoBySort',
    method: 'get',
    params
  })
}