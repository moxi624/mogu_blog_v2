import Vue from 'vue'
import Router from 'vue-router'
import HomeIndex from '@/views/home'
Vue.use(Router)

export const constantRouterMap = [
  {
    path: '/',
    component: HomeIndex,
    children: [
      { path: '/', component: () => import('@/views/index') },
      { path: '/about', component: () => import('@/views/about') },
      { path: '/life', component: () => import('@/views/life') },
      { path: '/list', component: () => import('@/views/list') },
      { path: '/sort', component: () => import('@/views/sort') },
      { path: '/share', component: () => import('@/views/share') },
      { path: '/classify', component: () => import('@/views/classify') },
      { path: '/tag', component: () => import('@/views/tag') },
      { path: '/time', component: () => import('@/views/time') },
      { path: '/info', component: () => import('@/views/info') },
      { path: '/messageBoard', component: () => import('@/views/messageBoard') },
    ]
  },
  { path: '/404', component: () => import('@/views/404') },
  { path: '/500', component: () => import('@/views/500') },
  { path: '/502', component: () => import('@/views/502') },
  { path: '/*', component: () => import('@/views/404') }

]

const router = new Router({
  routes: constantRouterMap
})

// router.beforeEach((to, from, next) => {
//   var reloaded = window.localStorage.getItem('reloaded') || '0'
//   if (to.path === '/about') {
//     window.localStorage.setItem('reloaded', reloaded)
//   } else if (from.path === '/about') {
//     var count = Number(reloaded) + 1
//     window.localStorage.setItem('reloaded', count)
//   }
//   next()
// })

export default router
