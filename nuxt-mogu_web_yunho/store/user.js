import {SET_USER_INFO, SET_LOGIN_STATE} from "./mutation-types";
export const state=()=>({
    // 用户信息
    userInfo: {},
    isLogin: false,
});

export const mutations = {
    // 传入自定义参数
    [SET_USER_INFO](state, userInfo) {
        state.userInfo = userInfo
      },
      [SET_LOGIN_STATE](state, isLogin) {
        state.isLogin = isLogin
      }
}
export const getters={
    getUserPhoto(state){
        if(state.isLogin) {
            if(state.userInfo.photoUrl) {
                return state.userInfo.photoUrl
            } else {
                return "https://gitee.com/moxi159753/wx_picture/raw/master/picture/favicon.png";
            }
        } else {
            return "/images/defaultAvatar.png"
        }
    }
}
