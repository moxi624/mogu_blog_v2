import { request } from '../utils/request.js'
import { appConfig } from '../config/config.js'

// 截图上传
export function cropperPicture (params) {
  return request.post(appConfig.PICTURE_API + '/file/cropperPicture', params)  
}
