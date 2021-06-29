//全局方法
import Vue from 'vue';
let show=() => {
    console.log("全局防范——show——");
}
Vue.prototype.$show=show;//由于目前是绑在vue的原型身上，所以服务端的钩子是不可以使用的，因为服务端的钩子中this不会指向vue
import {formatData,dateFormat,timeAgo,setStore,getStore,removeStore,getAllStore,clearStore} 
from '~/assets/scripts/util/webUtils';
Vue.prototype.$formatData=formatData;
Vue.prototype.$dateFormat=dateFormat;
Vue.prototype.$timeAgo=timeAgo;
Vue.prototype.$setStore=setStore;
Vue.prototype.$getStore=getStore;
Vue.prototype.$removeStore=removeStore;
Vue.prototype.$getAllStore=getAllStore;
Vue.prototype.$clearStore=clearStore;

//全局变量
import commonUtil from '~/assets/scripts/util/commonUtil'
Vue.prototype.$commonUtil = commonUtil.FUNCTIONS
Vue.prototype.$ECode = commonUtil.ECode
Vue.prototype.$SysConf = commonUtil.SysConf

//全局指令
// import highlight from '~/assets/scripts/directives/highlight'
// Vue.directive('highlight',highlight)
Vue.directive('highlight', function (el) {
    let blocks = el.querySelectorAll('pre code');
    blocks.forEach((block) => {
      hljs.highlightBlock(block)
    })
  })

import xss from 'xss'
// 定义全局XSS解决方法
Object.defineProperty(Vue.prototype, '$xss', {
  value: xss
})

// //全局组件
// import UcButton from '~/components/global/uc-button/uc-button.vue'
// Vue.component('uc-button',UcButton)

// //混入methods
Vue.mixin({
    methods:{
        $seo(title,content,desc,payload=[]){
            return{
                title,
                meta:[{
                    hid:"keywords-h",
                    name:"keywords",
                    content
                },{
                    hid:"description",
                    name:"description",
                    desc
                }
                ].concat(payload)
            }
        }
    }    
})
// 这里是 为了在 asyncData 方法中使用
export default ({ app }, inject) => {
    // Set the function directly on the context.app object
    app.$ECode = commonUtil.ECode // 名称
};