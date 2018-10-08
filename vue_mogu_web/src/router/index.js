import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export const constantRouterMap = [
  { path: '/', component: () => import('@/views/index') },
  { path: '/index', component: () => import('@/views/index') },
  { path: '/info', component: () => import('@/views/info') },
  { path: '/about', component: () => import('@/views/about') },
  { path: '/life', component: () => import('@/views/life') },
  { path: '/list', component: () => import('@/views/list') },
  { path: '/share', component: () => import('@/views/share') },
  { path: '/time', component: () => import('@/views/time') }
]
export default new Router({
  routes: constantRouterMap
})
