import request from '@/utils/request'

/**
 * 初始化ElasticSearch索引
 * @param params
 */
export function initElasticIndex(params) {
  return request({
    url: process.env.ADMIN_API + '/search/initElasticIndex',
    method: 'post',
    params
  })
}

