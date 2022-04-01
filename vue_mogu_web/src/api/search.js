import request from '@/utils/request'

/**
 * 获取搜索模式
 * @param params
 * @returns {*}
 */
export function getSearchModel (params) {
  return request({
    url: process.env.WEB_API + '/search/getSearchModel',
    method: 'get',
    params
  })
}

/**
 * 通过SQL搜索博客
 * @param params
 */
export function searchBlog (params) {
  return request({
    url: process.env.WEB_API + '/search/sqlSearchBlog',
    method: 'get',
    params
  })
}

/**
 * 通过ElasticSearch搜索博客
 * @param params
 */
export function searchBlogByES (params) {
  return request({
    url: process.env.SEARCH_API + '/search/elasticSearchBlog',
    method: 'get',
    params
  })
}

/**
 * 通过solr搜索博客
 * @param params
 */
export function searchBloBySolr (params) {
  return request({
    url: process.env.SEARCH_API + '/search/solrSearchBlog',
    method: 'get',
    params
  })
}

export function searchBlogByTag (params) {
  return request({
    url: process.env.WEB_API + '/search/searchBlogByTag',
    method: 'get',
    params
  })
}

export function searchBlogBySort (params) {
  return request({
    url: process.env.WEB_API + '/search/searchBlogBySort',
    method: 'get',
    params
  })
}

export function searchBlogByAuthor (params) {
  return request({
    url: process.env.WEB_API + '/search/searchBlogByAuthor',
    method: 'get',
    params
  })
}
