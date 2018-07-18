// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

//引入公共JS
import jquery from '../static/js/jquery.min.js'
import easyfader from  '../static/js/jquery.easyfader.min.js'
import scrollReveal from '../static/js/scrollReveal.js'
import common from '../static/js/common.js'

/*引入公共样式*/
import baseCss from '../static/css/base.css'; 
import indexCss from  '../static/css/index.css'; 
import mCss from  '../static/css/m.css';

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
