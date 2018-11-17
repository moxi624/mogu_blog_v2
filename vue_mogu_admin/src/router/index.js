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
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '仪表盘', icon: 'dashboard' }
    }]
  },

  {
    path: '/blog',
    component: Layout,
    redirect: '/blog/blog',
    name: '博客管理',
    meta: { title: '博客管理', icon: 'edit' },
    children: [
      {
        path: 'blog',
        name: '博客管理',
        component: () => import('@/views/blog/blog'),
        meta: { title: '博客管理', icon: 'blog' }
      },
      {
        path: 'blogTag',
        name: '标签管理',
        component: () => import('@/views/blog/blogTag'),
        meta: { title: '标签管理', icon: 'tag' }
      },
      {
        path: 'blogSort',
        name: '分类管理',
        component: () => import('@/views/blog/blogSort'),
        meta: { title: '分类管理', icon: 'sort' }
      },

      {
        path: 'collect',
        name: '收藏管理',
        component: () => import('@/views/blog/collect'),
        meta: { title: '收藏管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/authority',
    component: Layout,
    redirect: '/authority/admin',
    name: '权限管理',
    meta: { title: '权限管理', icon: 'authority' },
    children: [
      {
        path: 'admin',
        name: '管理员管理',
        component: () => import('@/views/authority/admin'),
        meta: { title: '管理员管理', icon: 'user' }
      },
      {
        path: 'role',
        name: '角色管理',
        component: () => import('@/views/authority/role'),
        meta: { title: '角色管理', icon: 'peoples' }
      },
      {
        path: 'authority',
        name: '权限管理',
        component: () => import('@/views/authority/authority'),
        meta: { title: '权限管理', icon: 'adminRole' }
      }
    ]
  },

  {
    path: '/resource',
    component: Layout,
    redirect: '/resource/resourceSort',
    name: '资源管理',
    meta: { title: '资源管理', icon: 'resource' },
    children: [
      {
        path: 'resourceSort',
        name: '分类管理',
        component: () => import('@/views/resource/resourceSort'),
        meta: { title: '分类管理', icon: 'table' }
      },
      {
        path: 'studyVideo',
        name: '视频管理',
        component: () => import('@/views/resource/studyVideo'),
        meta: { title: '视频管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/message',
    component: Layout,
    redirect: '/message/comment',
    name: '消息管理',
    meta: { title: '消息管理', icon: 'message1' },
    children: [
      {
        path: 'comment',
        name: '评论管理',
        component: () => import('@/views/message/comment'),
        meta: { title: '评论管理', icon: 'table' }
      },
      {
        path: 'feedback',
        name: '反馈管理',
        component: () => import('@/views/message/feedback'),
        meta: { title: '反馈管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/picture',
    component: Layout,
    redirect: '/picture/pictureSort',
    name: '图片管理',
    meta: { title: '图片管理', icon: 'example' },
    children: [
      {
        path: 'pictureSort',
        name: '图片类别管理',
        component: () => import('@/views/picture/pictureSort'),
        meta: { title: '图片管理', icon: 'picture' }
      },
      {
        path: 'picture',
        name: '图片管理',
        hidden: true,
        component: () => import('@/views/picture/picture'),
        meta: { title: '图片管理', icon: 'picture' }
      }
    ]
  },

  {
    path: '/user',
    component: Layout,
    redirect: '/user/user',
    name: '用户管理',
    meta: { title: '用户管理', icon: 'user1' },
    children: [
      {
        path: 'user',
        name: '用户管理',
        component: () => import('@/views/user/user'),
        meta: { title: '用户管理', icon: 'table' }
      },
      {
        path: 'visitor',
        name: '游客管理',
        component: () => import('@/views/user/visitor'),
        meta: { title: '游客管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/system',
    component: Layout,
    redirect: '/system/solrIndex',
    name: '系统管理',
    meta: { title: '系统管理', icon: 'system' },
    children: [
      {
        path: 'aboutMe',
        name: '关于我',
        component: () => import('@/views/system/aboutMe'),
        meta: { title: '关于我', icon: 'aboutMe' }
      },
      {
        path: 'solrIndex',
        name: '索引管理',
        component: () => import('@/views/system/solrIndex'),
        meta: { title: '索引管理', icon: 'index' }
      },
      {
        path: 'blogLink',
        name: '友情链接',
        component: () => import('@/views/system/blogLink'),
        meta: { title: '友情链接', icon: 'blogLink' }
      },
      {
        path: 'webConfig',
        name: '网站配置',
        component: () => import('@/views/system/webConfig'),
        meta: { title: '网站配置', icon: 'web' }
      }
    ]
  },

  {
    path: '/restapi',
    component: Layout,
    redirect: '/restapi/adminRestApi',
    name: '接口管理',
    meta: { title: '接口管理', icon: 'restapi' },
    children: [
      {
        path: 'adminRestApi',
        name: 'Admin接口',
        component: () => import('@/views/restapi/adminRestApi'),
        meta: { title: 'Admin接口', icon: 'table' }
      },
      {
        path: 'pictureRestApi',
        name: 'Picture接口',
        component: () => import('@/views/restapi/pictureRestApi'),
        meta: { title: 'Picture接口', icon: 'table' }
      },
      {
        path: 'webRestApi',
        name: 'Web接口',
        component: () => import('@/views/restapi/webRestApi'),
        meta: { title: 'Web接口', icon: 'table' }
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
