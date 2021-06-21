import {SET_COMMENT_LIST, SET_WEB_CONFIG_DATA, SET_USER_TAG, SET_LOGIN_MESSAGE } from "./mutation-types";
export const state =()=>({
  // 评论列表
  commentList: [],
  // 用户标签字典 列表
  userTagDictList: [],
  // WebConfig网站配置
  webConfigData: {},
  // 登录消息，用于控制登录弹框
  loginMessage: "",
});

export const mutations={
  // 传入自定义参数
  [SET_COMMENT_LIST](state,commentList){
    state.commentList = commentList
  },
  // 传入自定义参数
  [SET_USER_TAG](state,userTagDictList){
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
}

export const getters = {
  getWebConfigData(state){
      return state.webConfigData;
  }
}