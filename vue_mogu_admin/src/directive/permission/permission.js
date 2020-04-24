import store from '@/store'

export default {
  inserted(el, binding, vnode) {
    const { value } = binding
    if(store.getters.buttonMap.size == undefined) {
      // 初始加载的时候，还没有写入到vuex中，因此使用setInterval在下次dom更新时生效
      let intervalID = setInterval(() => {
        try{
          // 得到按钮上的路径
          const buttonMap = store.getters.buttonMap
          if (value && value.length > 0) {
            const path = value
            const hasPermission = buttonMap.get(path)
            // 没有权限时，是否显示按钮，取决于button管理的上 是否显示 的设置
            if (!hasPermission) {
              el.parentNode && el.parentNode.removeChild(el)
            }
          }
          // 清空触发器
          if(store.getters.buttonMap.size != undefined) {
            clearInterval(intervalID);
          }
        } catch (e) {
            console.log("vuex中的PathMap暂未初始化")
        }
      },500);
    } else {
      // 得到按钮上的路径
      const buttonMap = store.getters.buttonMap
      if (value && value.length > 0) {
        const path = value
        const hasPermission = buttonMap.get(path)
        if (!hasPermission) {
          el.parentNode && el.parentNode.removeChild(el)
        }
      } else {
        throw new Error(`need roles! Like v-permission="'admin'"`)
      }
    }
  }
}
