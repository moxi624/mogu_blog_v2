import Vue from 'vue'
import App from './App'

import blogHome from './pages/home/home.vue'
Vue.component('blogHome',blogHome)

import blogTag from './pages/tag/home.vue'
Vue.component('blogTag',blogTag)

import blogSort from './pages/sort/home.vue'
Vue.component('blogSort',blogSort)

import blogClassify from './pages/classify/home.vue'
Vue.component('blogClassify',blogClassify)

import myCenter from './pages/my/home.vue'
Vue.component('myCenter',myCenter)

import cuCustom from './colorui/components/cu-custom.vue'
Vue.component('cu-custom',cuCustom)

import zhouWeiNavBar from "./components/zhouWei-navBar";
Vue.component("nav-bar", zhouWeiNavBar);

Vue.config.productionTip = false

// import hljs from 'highlight.js/lib/highlight';

// import 'highlight.js/styles/zenburn.css'

import 'static/css/ckeditor.css'
import 'static/css/emoji.css'
import 'static/css/index.css'

// 工具类注册
import prototype from './utils/prototype'
Vue.use(prototype)

// Vue.directive('highlight', function (el) {
//   let blocks = el.querySelectorAll('pre code');
//   console.log("获取的列表", el)
//   blocks.forEach((block) => {
//     hljs.highlightBlock(block)
//   })
// })

App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()

 



