import request from '@/utils/request'

export function uploadPictures(data) {
  // 批量上传文件
  return request({
    url: process.env.PICTURE_API + '/pictures',
    method: 'post',
    data
  })
}
