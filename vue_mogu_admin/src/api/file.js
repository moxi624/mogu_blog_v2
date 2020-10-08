import request from '@/utils/request'

/**
 * 获取文件列表
 * @param data
 */
export function getFileList(params) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/getFileList',
    method: 'post',
    data: params
  })
}

export function getFileTree(params) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/getFileTree',
    method: 'post',
    data: params
  })
}

/**
 * 获取存储占用
 * @param data
 */
export function getStorage(data) {
  return request({
    url: process.env.PICTURE_API + '/storage/getStorage',
    method: 'get',
    data
  })
}

/**
 * 解压文件
 * @param data
 */
export function unzipFile(data) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/unzipFile',
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
    url: process.env.PICTURE_API + '/networkDisk/deleteFile',
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
    url: process.env.PICTURE_API + '/networkDisk/selectFileByFileType',
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
    url: process.env.PICTURE_API + '/networkDisk/moveFile',
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
    url: process.env.PICTURE_API + '/networkDisk/createFile',
    method: 'post',
    data
  })
}

/**
 * 编辑文件
 * @param data
 */
export function editFile(data) {
  return request({
    url: process.env.PICTURE_API + '/networkDisk/edit',
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
    url: process.env.PICTURE_API + '/networkDisk/batchDeleteFile',
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
    url: process.env.PICTURE_API + '/networkDisk/batchMoveFile',
    method: 'post',
    data
  })
}
