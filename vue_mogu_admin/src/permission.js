import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth' // 验权
import { constantRouterMap } from './router/index'

const whiteList = ['/login'] // 不重定向白名单
const whiteListActiveList = ['/', '/dashboard', '/404', '/401']
const allList = []

router.beforeEach((to, from, next) => {
  if (allList.length === 0) {
    for (var a = 0; a < constantRouterMap.length; a++) {
      if (constantRouterMap[a].children) {
        var childrenList = constantRouterMap[a].children
        for (var b = 0; b < childrenList.length; b++) {
          allList.push(constantRouterMap[a].path + '/' + childrenList[b].path)
        }
      } else {
        allList.push(constantRouterMap[a].path)
      }
    }
  }

  // 向白名单中添加内容
  const activeList = []
  if (store.getters.menu.sonList) {
    const sonList = store.getters.menu.sonList
    for (var c = 0; c < sonList.length; c++) {
      activeList.push(sonList[c].url)
    }
  }

  NProgress.start()
  if (getToken()) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {
      if (store.getters.roles.length === 0) {
        store.dispatch('GetInfo').then(res => { // 拉取用户信息
          next()
        }).catch((err) => {
          store.dispatch('FedLogOut').then(() => {
            Message.error(err || 'Verification failed, please login again')
            next({ path: '/' })
          })
        })

        // store.dispatch('GetMenu').then(res => { // 菜单信息
        //   next()
        // }).catch((err) => {
        //   store.dispatch('FedLogOut').then(() => {
        //     Message.error(err || 'Verification failed, please login again')
        //     next({ path: '/' })
        //   })
        // })
      } else {
        if (whiteListActiveList.indexOf(to.path) !== -1) {
          next()
        } else if (activeList.indexOf(to.path) !== -1) {
          next()
        } else {
          if (allList.indexOf(to.path) !== -1) {
            next({ path: '/401' })
          } else {
            next({ path: '/404' })
          }
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
