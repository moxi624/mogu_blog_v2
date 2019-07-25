// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

//引入公共JS
import $ from 'jquery'
import easyfader from '../static/js/jquery.easyfader.min.js'
import scrollReveal from '../static/js/scrollReveal.js'
import common from '../static/js/common.js'
// import nav from '../static/js/nav.js'

Vue.config.productionTip = false

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
  components: { App },
  template: '<App/>'
})
