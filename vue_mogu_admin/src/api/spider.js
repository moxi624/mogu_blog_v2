import request from '@/utils/request'

export function pictureSpider(params) {
  return request({
    url: process.env.Spider_API + '/spider/spiderPicture',
    method: 'post',
    data: params
  })
}

