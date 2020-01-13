import {SET_USER_INFO, SET_LOGIN_STATE} from "./mutation-types";

const app = {
  // 全局状态
  state: {
    // 用户信息
    userInfo: {},
    isLogin: false,
  },
  // getters是对数据的包装，例如对数据进行拼接，或者过滤
  getters: {
    //类似于计算属性
    getUserPhoto(state) {
      if(state.isLogin) {
        if(state.userInfo.photoUrl) {
          return process.env.PICTURE_HOST + state.userInfo.photoUrl
        } else {
          return "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";
        }
      } else {
        return "../../static/images/defaultAvatar.png"
      }
    }
  },
  // 如果我们需要更改store中的状态，一定要通过mutations来进行操作
  mutations: {

    // 传入自定义参数
    [SET_USER_INFO](state, userInfo) {
      state.userInfo = userInfo
    },
    [SET_LOGIN_STATE](state, isLogin) {
      state.isLogin = isLogin
    }
  },

  // actions是我们定义的一些操作，正常情况下，我们很少会直接调用mutation方法来改变state
  actions: {

  }
}
export default app
