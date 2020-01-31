import request from '@/utils/request'

export function cropperPicture(params) {
  return request({
    url: 'http://localhost:8602/file/cropperPicture',
    method: 'POST',
    data: params
  })
}
