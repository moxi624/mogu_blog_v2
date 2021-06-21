<template>
  <div class="errPage-container">
    <el-button @click="back" icon='arrow-left' class="pan-back-btn">返回</el-button>
    <el-row>
      <el-col :span="12">
        <h1 class="text-jumbo text-ginormous">Oops!</h1>
        <h2>网站正在维护中...</h2>
        <h4>请联系博主尽快恢复~</h4>
        <ul class="list-unstyled">
          <li>或者你可以去:</li>
          <li class="link-type">
            <router-link to="/">回首页</router-link>
          </li>
          <li class="link-type"><a href="https://gitee.com/moxi159753/mogu_blog_v2">蘑菇博客源码</a></li>
          <li class="link-type"><a href="http://moxi159753.gitee.io/mogu_blog_doc">蘑菇博客文档</a></li>
          <li class="link-type"><a href="https://gitee.com/moxi159753/LearningNotes">学习笔记</a></li>
          <li><a @click.prevent="dialogVisible=true" href="#">点我看图</a></li>
        </ul>
      </el-col>
      <el-col :span="12">
        <img :src="errGif" width="313" height="428" alt="Girl has dropped her ice cream.">
      </el-col>
    </el-row>
    <el-dialog title="随便看" :visible.sync="dialogVisible">
      <img class="pan-img" :src="ewizardClap">
    </el-dialog>
  </div>
</template>

<script>
import errGif from '@/assets/401_images/401.gif'

export default {
  name: 'page401',
  data() {
    return {
      errGif: errGif + '?' + +new Date(),
      ewizardClap: 'https://wpimg.wallstcn.com/007ef517-bafd-4066-aae4-6883632d9646',
      dialogVisible: false
    }
  },
  methods: {
    back() {
      if (this.$route.query.noGoBack) {
        this.$router.push({ path: '/index' })
      } else {
        this.$router.go(-1)
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  body{
    line-height: 2;
    background-color: #FFFFFF
  }
  .errPage-container {
    width: 800px;
    margin: 100px auto;
    .pan-back-btn {
      background: #008489;
      color: #fff;
      border: none!important;
    }
    .pan-gif {
      margin: 0 auto;
      display: block;
    }
    .pan-img {
      display: block;
      margin: 0 auto;
      width: 100%;
    }
    .text-jumbo {
      font-size: 60px;
      font-weight: 700;
      color: #484848;
    }
    .list-unstyled {
      font-size: 14px;
      li {
        padding-bottom: 5px;
      }
      a {
        color: #008489;
        text-decoration: none;
        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
</style>
