import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '首页',
    hidden: true,
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index')
    }]
  },

  {
    path: '/example',
    component: Layout,
    redirect: '/example/blog',
    name: '博客管理',
    meta: { title: '博客管理', icon: 'example' },
    children: [
      {
        path: 'blog',
        name: '博客管理',
        component: () => import('@/views/blog/index'),
        meta: { title: '博客管理', icon: 'table' }
      },
      {
        path: 'tag',
        name: '标签管理',
        component: () => import('@/views/tag/index'),
        meta: { title: '标签管理', icon: 'table' }
      },
      {
        path: 'blogSort',
        name: '分类管理',
        component: () => import('@/views/blogSort/index'),
        meta: { title: '分类管理', icon: 'table' }
      },
      {
        path: 'link',
        name: '友链管理',
        component: () => import('@/views/link/index'),
        meta: { title: '友链管理', icon: 'table' }
      },
      {
        path: 'collect',
        name: '收藏管理',
        component: () => import('@/views/collect/index'),
        meta: { title: '收藏管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/example',
    component: Layout,
    redirect: '/example/comment',
    name: '消息管理',
    meta: { title: '消息管理', icon: 'example' },
    children: [
      {
        path: 'comment',
        name: '评论管理',
        component: () => import('@/views/comment/index'),
        meta: { title: '评论管理', icon: 'table' }
      },
      {
        path: 'feedback',
        name: '反馈管理',
        component: () => import('@/views/feedback/index'),
        meta: { title: '反馈管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/example',
    component: Layout,
    redirect: '/example/pictureSort',
    name: '图片管理',
    meta: { title: '图片管理', icon: 'example' },
    children: [
      {
        path: 'pictureSort',
        name: '图片类别管理',
        component: () => import('@/views/picture/pictureSort'),
        meta: { title: '图片管理', icon: 'table' }
      },
      {
        path: 'picture',
        name: '图片管理',
        hidden: true,
        component: () => import('@/views/picture/picture'),
        meta: { title: '图片管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/example',
    component: Layout,
    redirect: '/example/user',
    name: '用户管理',
    meta: { title: '用户管理', icon: 'example' },
    children: [
      {
        path: 'user',
        name: '用户管理',
        component: () => import('@/views/user/index'),
        meta: { title: '用户管理', icon: 'table' }
      },
      {
        path: 'visitor',
        name: '游客管理',
        component: () => import('@/views/visitor/index'),
        meta: { title: '游客管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/example',
    component: Layout,
    redirect: '/example/solrIndex',
    name: '系统管理',
    meta: { title: '系统管理', icon: 'example' },
    children: [
      {
        path: 'solrIndex',
        name: '索引管理',
        component: () => import('@/views/solrIndex/index'),
        meta: { title: '索引管理', icon: 'table' }
      }
    ]
  },

  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
