export const state = ()=>({
    err:1,
    data:{}
})
//mutations 其中的state执行home中的state
export const mutations={
    M_UPDATE_HOME(state,payload){
        state.err = payload.err;
        state.data = payload.data;
    }
}

//actions  其中的state执行home中的state
export const actions = {
    A_UPDATE_HOME({commit,state},payload){
        //异步处理
        commit('M_UPDATE_HOME',{err:0,data:{title:"home 模块 action 所传递的数据"}})
    }
}
