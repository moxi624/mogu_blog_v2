import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

// element使用CDN全局引入，因此这里可以注释
// import ElementUI from 'element-ui'
// import 'element-ui/lib/theme-chalk/index.css'
// import locale from 'element-ui/lib/locale/lang/en'

import prototype from './utils/prototype'
import '@/styles/index.scss' // global css

import App from './App'
import router from './router'
import store from './store'
import '@/icons' // icon
import '@/permission' // permission control

// 引入自定义指令
import './directive'

// 添加粒子特效
import VueParticles from 'vue-particles'

// Vue.use(ElementUI, { locale })

import xss from 'xss'
// 定义全局XSS解决方法
Object.defineProperty(Vue.prototype, '$xss', {
  value: xss
})

Vue.use(VueParticles)
Vue.use(prototype)
Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
