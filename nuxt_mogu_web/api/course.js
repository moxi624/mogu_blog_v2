import http from './public'
import qs from 'qs'
let config = require('~/config/sysConfig')
let apiURL = config.apiURL
let staticURL = config.staticURL
if (typeof window === 'undefined') {
  apiURL = config.backApiURL
  staticURL = config.backStaticURL
}
/*搜索*/
export const search_course = (page,size,params) => {
  //let loginRequest = querystring.stringify(params)
  let querys = qs.stringify(params);
  return http.requestQuickGet(apiURL+"/search/course/list/"+page+"/"+size+"?"+querys);
}
/*获取分类*/
export const sysres_category = () => {
  return http.requestQuickGet(staticURL+"/category/category.json");
}

