import commonUtil from './commonUtil'

export default {
  install(Vue, options) {
    Vue.prototype.$commonUtil = commonUtil.FUNCTIONS
  }
}
