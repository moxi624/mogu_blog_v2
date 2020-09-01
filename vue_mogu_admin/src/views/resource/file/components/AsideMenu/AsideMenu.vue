<template>
  <div class="aside-menu-wrapper" :class="{'expand': !isFolder}">
    <div class="aside-title" @click="changeIsFolder">
      <i v-show="!isFolder"></i>
      <span v-show="!isFolder">蘑菇网盘</span>
      <el-tooltip class="item" effect="dark" content="收起分类栏" placement="bottom-end">
        <i
          class="el-icon-d-arrow-left"
          v-show="!isFolder"
        ></i>
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="展开分类栏" placement="bottom-start">
        <i
          class="el-icon-d-arrow-right"
          v-show="isFolder"
        ></i>
      </el-tooltip>
    </div>
    <el-menu
      class="aside-menu"
      :default-openeds="[0]"
      :default-active="activeIndex"
      @select="handleSelect"
    >
      <el-menu-item index="0" title="全部">
        <i class="el-icon-menu"></i>
        <span slot="title" v-show="!isFolder">全部</span>
      </el-menu-item>
      <el-menu-item index="1" title="图片">
        <i class="el-icon-picture"></i>
        <span slot="title" v-show="!isFolder">图片</span>
      </el-menu-item>
      <el-menu-item index="2" title="文档">
        <i class="el-icon-document"></i>
        <span slot="title" v-show="!isFolder">文档</span>
      </el-menu-item>
      <el-menu-item index="3" title="视频">
        <i class="el-icon-video-camera-solid"></i>
        <span slot="title" v-show="!isFolder">视频</span>
      </el-menu-item>
      <el-menu-item index="4" title="音乐">
        <i class="el-icon-headset"></i>
        <span slot="title" v-show="!isFolder">音乐</span>
      </el-menu-item>
      <el-menu-item index="5" title="其他">
        <i class="el-icon-truck"></i>
        <span slot="title" v-show="!isFolder">其他</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
// import { selectFileByFileType } from '@/request/file.js'

export default {
  name: 'AsideMenu',
  data() {
    return {
      fileListByFiletype: [],
      activeIndex: "0"
    }
  },
  created() {
    if(this.$route.query.filetype) {
      this.activeIndex = this.$route.query.filetype
    }
  },
  computed: {
    //  判断当前用户设置的左侧栏是否折叠
    isFolder() {
      return this.$store.getters.isFolder
    }
  },
  methods: {
    //  导航菜单项点击事件
    handleSelect(index) {
      this.$router.push({
        path: '/resource/file',
        query: { filepath: '/', filetype: index }
      })
    },
    /**
     * 收纳
     */
    changeIsFolder: function () {
      if(this.$store.getters.isFolder === 0) {
        this.$store.commit('changeIsFolder', 1)
      } else {
        this.$store.commit('changeIsFolder', 0)
      }
    }
  }
}
</script>

<style lang="stylus" scoped>
@import '~@/assets/styles/varibles.styl'
.aside-menu-wrapper
  width 69px
  transition width 0.5s
  -webkit-transition width 0.5s
  .aside-title
    cursor pointer
    background-color $Primary
    color #fff
    height 80px
    line-height 80px
    font-size 18px
    text-align center
    position relative
    .el-icon-d-arrow-left
      position absolute
      right 6px
      top 32px
      cursor pointer
    .el-icon-d-arrow-right
      cursor pointer
  >>> .el-menu
    border none
    .el-menu-item.is-active
      background $PrimaryHover
.expand
  width 220px
  >>> .el-menu
    .el-menu-item
      padding-left 40px !important
</style>
