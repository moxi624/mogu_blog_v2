import router from './router'
import store from './store'
import { constantRouterMap } from './router/index'
import {getCookie} from "./utils/cookieUtils";

const whiteList = ['/login'] // 不重定向白名单
const whiteListActiveList = ['/', '/401', '/404', '/500']
const allList = []

router.beforeEach((to, from, next) => {

  console.log("allList", constantRouterMap)

  if (allList.length === 0) {
    for (var a = 0; a < constantRouterMap.length; a++) {
      if (constantRouterMap[a].children) {
        var childrenList = constantRouterMap[a].children
        for (var b = 0; b < childrenList.length; b++) {
          allList.push(childrenList[b].path)
        }
      } else {
        allList.push(constantRouterMap[a].path)
      }
    }
  }

  console.log("allList", allList)

  // 向白名单中添加内容
  // 从cookie中获取token
  let webNavbarList = JSON.parse(getCookie("webNavbarList"))
  console.log("转换后的json串", webNavbarList)
  const activeList = []
  if (webNavbarList) {
    for (var c = 0; c < webNavbarList.length; c++) {
      activeList.push(webNavbarList[c].url)
    }
  }

  if (whiteListActiveList.indexOf(to.path) !== -1) {
    next()
  } else if (activeList.indexOf(to.path) !== -1) {
    next()
  } else if (whiteList.indexOf(to.path) !== -1) {
    next()
  } else {
    if (allList.indexOf(to.path) !== -1) {
      next({ path: '/401' })
    } else {
      next({ path: '/404' })
    }
  }

})
