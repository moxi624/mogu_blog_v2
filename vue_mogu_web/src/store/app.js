import {SET_COMMENT_LIST, SET_WEB_CONFIG_DATA, SET_USER_TAG, SET_LOGIN_MESSAGE } from "./mutation-types";

const app = {
  // 全局状态
  state: {
    // 评论列表
    commentList: [],
    // 用户标签字典 列表
    userTagDictList: [],
    // WebConfig网站配置
    webConfigData: {},
    // 登录消息，用于控制登录弹框
    loginMessage: "",
  },
  // getters是对数据的包装，例如对数据进行拼接，或者过滤
  getters: {
    //类似于计算属性
    // 增加的方法
  },
  // 如果我们需要更改store中的状态，一定要通过mutations来进行操作
  mutations: {

    // 传入自定义参数
    [SET_COMMENT_LIST](state, commentList) {
      state.commentList = commentList
    },

    // 传入自定义参数
    [SET_USER_TAG](state, userTagDictList) {
      state.userTagDictList = userTagDictList
    },

    // 设置WebConfig
    [SET_WEB_CONFIG_DATA](state, webConfigData) {
      state.webConfigData = webConfigData
    },

    // 设置消息
    [SET_LOGIN_MESSAGE] (state, loginMessage) {
      state.loginMessage = loginMessage
    },
  },

  // actions是我们定义的一些操作，正常情况下，我们很少会直接调用mutation方法来改变state
  actions: {

  }
}
export default app
