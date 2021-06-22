//全局方法
import Vue from 'vue';
let show=() => {
    console.log("全局防范——show——");
}
Vue.prototype.$show=show;//由于目前是绑在vue的原型身上，所以服务端的钩子是不可以使用的，因为服务端的钩子中this不会指向vue

//全局变量
import commonUtil from '~/assets/scripts/util/commonUtil'
Vue.prototype.$commonUtil = commonUtil.FUNCTIONS
Vue.prototype.$ECode = commonUtil.ECode
Vue.prototype.$SysConf = commonUtil.SysConf

//全局指令
import highlight from '~/assets/scripts/directives/highlight'
Vue.directive('highlight',highlight)

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