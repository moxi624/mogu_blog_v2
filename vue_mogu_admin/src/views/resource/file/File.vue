<template>
  <div class="file-wrapper">
    <el-container class="el-container">
      <el-aside width="auto">
        <AsideMenu/>
      </el-aside>
      <el-container>
        <el-main class="el-main">
          <FileList/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import AsideMenu from './components/AsideMenu/AsideMenu'
import FileList from './components/FileList'

export default {
  name: 'File',
  components: {
    AsideMenu,
    FileList
  }
}
</script>

<style lang="stylus" scoped>
@import '~@/assets/styles/varibles.styl'
@import '~@/assets/styles/mixins.styl'
.file-wrapper
  width 100% !important
.el-container
  .el-aside
    border-right solid 1px $BorderLight
    height calc(100vh - 70px)
    setScrollbar(4px)
.el-main
  padding-top 0px !important
  overflow hidden
</style>
