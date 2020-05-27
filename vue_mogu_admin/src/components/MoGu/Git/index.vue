<template>
  <div>
    <svg-icon class="giteeSvg" icon-class="gitee" @click.native="goto" style="height: 2.5em;font-size: 18px;cursor: pointer" />
  </div>
</template>

<script>
export default {
  name: 'MoGuGit',
  data() {
    return {
      url: 'https://gitee.com/moxi159753/mogu_blog_v2'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
<style scoped>

</style>
