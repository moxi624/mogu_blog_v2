<template>
  <div class="breadcrumb-wrapper">
    <div class="networkTitle">当前位置：</div>
    <el-breadcrumb v-if="filetype" separator="/">
      <el-breadcrumb-item>全部{{filetypeMap[filetype]}}</el-breadcrumb-item>
    </el-breadcrumb>
    <el-breadcrumb v-else separator="/">
      <el-breadcrumb-item
        v-for="(item, index) in breadCrumbList"
        :key="index"
        :to="{ query: { filepath: item.path, filetype: 0 } }"
      >{{item.name}}</el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script>
export default {
  name: 'BreadCrumb',
  data() {
    return {
      filetypeMap: {
        1: '图片',
        2: '文档',
        3: '视频',
        4: '音乐',
        5: '其他'
      }
    }
  },
  computed: {
    //  面包屑导航栏数组
    breadCrumbList: {
      get() {
        let filepath = this.$route.query.filepath
        let filepathList = filepath ? filepath.split('/') : []
        let res = [] //  返回结果数组
        let _path = [] //  存放祖先路径
        for (let i = 0; i < filepathList.length; i++) {
          if (filepathList[i]) {
            _path.push(filepathList[i] + '/')
            res.push({
              path: _path.join(''),
              name: filepathList[i]
            })
          } else if (i === 0) {
            //  根目录
            filepathList[i] = '/'
            _path.push(filepathList[i])
            res.push({
              path: '/',
              name: '全部文件'
            })
          }
        }
        return res
      },
      set() {
        return []
      }
    },
    //  文件类型索引
    filetype: {
      get() {
        return Number(this.$route.query.filetype)
      },
      set() {
        return 0
      }
    }
  }
}
</script>

<style lang="stylus" scoped>
.breadcrumb-wrapper
  padding 0 20px
  height 30px
  line-height 30px
  display flex
  .networkTitle, >>> .el-breadcrumb
    height 30px
    line-height 30px
</style>
