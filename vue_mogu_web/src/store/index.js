import Vue from 'vue'
import Vuex from 'vuex'
import app from './app'
import user from './user'

//让vuex生效
Vue.use(Vuex)

const store = new Vuex.Store({

  // 将app和user放在store中
  modules: {
    app,
    user
  }
})

export default  store
