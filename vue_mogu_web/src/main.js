// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'

// import ElementUI from 'element-ui'
// import 'element-ui/lib/theme-chalk/index.css'
// import locale from 'element-ui/lib/locale/lang/en' // lang i18n
// Vue.use(ElementUI, { locale })

import App from './App'
import router from './router'
import store from './store'

// 引入公共JS
import "@/assets/iconfont/iconfont.css";
import '../static/css/ckeditor.css';
import '../static/css/index.css';
Vue.config.productionTip = false


import xss from 'xss'
// 定义全局XSS解决方法
Object.defineProperty(Vue.prototype, '$xss', {
  value: xss
})

Vue.directive('highlight', function (el) {
  let blocks = el.querySelectorAll('pre code');
  blocks.forEach((block) => {
    hljs.highlightBlock(block)
  })
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  //需要将store和vue实例进行关联，这里将其传递进去
  store,
  components: { App },
  template: '<App/>'
})
