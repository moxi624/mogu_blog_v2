import {SET_COMMENT_LIST, SET_WEB_CONFIG_DATA, SET_USER_TAG, SET_LOGIN_MESSAGE,SET_USER_INFO,SET_TOKEN } from "./mutation-types";
import web from "~/assets/scripts/config/webconst";
export const state =()=>({
  // 评论列表
  commentList: [],
  // 用户标签字典 列表
  userTagDictList: [],
  // WebConfig网站配置
  webConfigData: {},
  // 登录消息，用于控制登录弹框
  loginMessage: "",
  token:""
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
  // 设置消息
  [SET_TOKEN] (state, token) {
    state.token = token
  }
}

export const getters = {
  getWebConfigData(state){
      return state.webConfigData;
  }
}
//actions 
export const actions = {
  nuxtServerInit(store,{app:{$cookies}}){
      // 初始化token到store中
      // console.log("nuxtServerInit",$cookies.get(web.key+"-"+web.token));
      //设置token
      let token = $cookies.get(web.key+"-"+web.token)?$cookies.get(web.key+"-"+web.token):'';
      store.commit(SET_TOKEN,token);
      //设置用户信息
      let user = $cookies.get(web.key+"-"+web.user)?$cookies.get(web.key+"-"+web.user):{};
      store.commit('user/'+SET_USER_INFO,user);
      //设置webconfig
      // console.log("nuxtServerInit",$cookies.get(web.key+"-"+web.webConfig));
      // console.log($cookies.get(web.key+"-"+web.webConfig));
      let webConfig = $cookies.get(web.key+"-"+web.webConfig)?$cookies.get(web.key+"-"+web.webConfig):{};
      store.commit(SET_WEB_CONFIG_DATA,webConfig);
  }
}