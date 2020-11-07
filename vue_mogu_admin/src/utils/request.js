import axios from 'axios'
import { Message, MessageBox, Loading } from 'element-ui'
import store from '../store'
import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: '', // api 的 base_url
  withCredentials: true, //允许后台的cookie传递到前端
  timeout: 100000 // 请求超时时间
})

// 传递token
service.defaults.headers.common['Authorization'] = getToken()

// 请求计数器
var requestNum = 0;
var loading = null;

// request拦截器
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      // 让每个请求携带自定义token 请根据实际情况自行修改
      config.headers.Authorization = getToken()
    }

    // 请求加1
    requestNum ++;

    if(loading == null) {
      loading = Loading.service({ fullscreen: true, text:'正在努力加载中~' });
    } else if (loading != null && requestNum > 0) {
      loading = Loading.service({ fullscreen: true, text:'正在努力加载中~' });
    }

    return config
  },
  error => {
    // Do something with request error
    console.log(error)
    Promise.reject(error)
    // 出错了直接关闭loading
    requestNum = 0
    if(loading) {
      loading.close();
    }
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非success和error是抛错 可结合自己业务进行修改
     */
    const res = response.data
    // 请求数减1
    requestNum --;
    if (loading == null || requestNum <= 0) {
      loading.close()
    }
    if (res.code === 'success' || res.code === 'error' || res.success) {
      // 请求完毕
      return response.data
    } else {
      // 出错了直接关闭loading
      requestNum = 0
      loading.close();
      if(res.code === 401) {
        MessageBox.confirm(
          'token已过期，可以取消继续留在该页面，或者重新登录',
          '确定登出',
          {
            confirmButtonText: '重新登录',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(() => {
          store.dispatch('FedLogOut').then(() => {
            location.reload() // 为了重新实例化vue-router对象 避免bug
          })
        })
        return Promise.reject('error')
      }else if(res.code === 402){
        // 接口没有权限访问时
        Message({
          message: res.data,
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject('error')
      } else {
        console.log("错误信息", res)
        Message({
          message: res.message,
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject(res.message)
      }
    }
  },
  error => {
    console.log('错误码', error)
    // 出错了直接关闭loading
    requestNum = 0
    loading.close();
    Message({
      message: error,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error.message)
  }
)

export default service
