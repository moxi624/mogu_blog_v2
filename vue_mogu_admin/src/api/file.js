import request from '@/utils/request'

/**
 * 获取文件列表
 * @param data
 */
export function getfilelist(params) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/getfilelist',
    method: 'post',
    data: params
  })
}

/**
 * 获取存储占用
 * @param data
 */
export function getstorage(data) {
  return request({
    url: process.env.PICTURE_API + '/storage/getstorage',
    method: 'get',
    data
  })
}

/**
 * 解压文件
 * @param data
 */
export function unzipfile(data) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/unzipfile',
    method: 'post',
    data
  })
}

/**
 * 删除文件
 * @param data
 */
export function deleteFile(data) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/deletefile',
    method: 'post',
    data
  })
}

/**
 * 通过文件类型选择文件
 * @param data
 */
export function selectFileByFileType(data) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/selectfilebyfiletype',
    method: 'post',
    data
  })
}

/**
 * 下载文件
 * @param data
 */
export function downloadFile(data) {
  return request({
    url: process.env.PICTURE_API + '/storage/downloadfile',
    method: 'post',
    data
  })
}

/**
 * 获取文件的树结构
 * @param data
 */
export function getFileTree(data) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/getfiletree',
    method: 'post',
    data
  })
}

/**
 * 移动文件
 * @param data
 */
export function moveFile(data) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/movefile',
    method: 'post',
    data
  })
}

/**
 * 创建文件
 * @param data
 */
export function createFile(data) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/createfile',
    method: 'post',
    data
  })
}

/**
 * 批量删除
 * @param data
 */
export function batchDeleteFile(data) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/batchdeletefile',
    method: 'post',
    data
  })
}

/**
 * 批量移动
 * @param data
 */
export function batchMoveFile(data) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/batchmovefile',
    method: 'post',
    data
  })
}
