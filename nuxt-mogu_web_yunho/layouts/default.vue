<template>
  <div>
    <!-- 将包含客户端代码的组件设置为非SSR方式，否则nuxt编译不通过 -->
    <no-ssr>
    <web-header></web-header>
    </no-ssr>
    <Nuxt />
    <web-footer></web-footer>
  </div>
</template>
<script>
import WebHeader from './web-header.vue'
import WebFooter from './web-footer.vue'
export default {
  name:'default',
  components:{
    WebHeader,WebFooter
  }
  // mounted(){
  //   console.log(window);
  // }
}
</script>

<style>

</style>
