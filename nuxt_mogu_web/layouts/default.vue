<template>
  <div>
    <Header />
    <nuxt/>
    <Footer />
    <CdTop></CdTop>
  </div>
</template>
<script>
  import Header from '../components/BlogHead.vue'
  import Footer from '../components/BlogFooter.vue'
  import CdTop from '../components/CdTop.vue'
  export default {
    components: {
      Header,
      Footer,
      CdTop
    }
  }
</script>
<style>

</style>
