import http from './public'
import qs from 'qs'
/*登陆*/
export const login = params => {
  //let loginRequest = querystring.stringify(params)
  let loginRequest = qs.stringify(params);

  return http.requestPostForm('/api/auth/login',loginRequest);
}
/*退出*/
export const logout = params => {
  sessionStorage.removeItem('activeUser');
  return http.requestPost('/api/auth/userlogout');
}



