import request from '@/utils/request'

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
// export function searchBlog (params) {
//   return request({
//     url: process.env.ELASTICSEARCH + '/search/elasticSearchBlog',
//     method: 'get',
//     params
//   })
// }

/**
 * 通过solr搜索博客
 * @param params
 */
// export function searchBlog (params) {
//   return request({
//     url: process.env.ELASTICSEARCH + '/search/solrSearchBlog',
//     method: 'get',
//     params
//   })
// }

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
