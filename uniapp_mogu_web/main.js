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

Vue.config.productionTip = false


App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()

 



