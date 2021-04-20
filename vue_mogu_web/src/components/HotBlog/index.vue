<template>
    <div class="tuijian" v-if="hotBlogData.length > 0">
      <h2 class="hometitle">点击排行</h2>
      <ul class="tjpic" v-if="hotBlogData[0]">
        <i><img style="cursor:pointer" v-if="hotBlogData[0].photoList" :src="hotBlogData[0].photoList[0]" @click="goToInfo(hotBlogData[0])"></i>
        <p><a href="javascript:void(0);" @click="goToInfo(hotBlogData[0])">{{hotBlogData[0].title}}</a></p>
      </ul>
      <ul class="sidenews">
        <li v-for="(item, index) in sideNews" v-if="index != 0" :key="item.uid">
          <i><img style="cursor:pointer"  v-if="item.photoList" :src="item.photoList[0]" @click="goToInfo(item)"></i>
          <p><a href="javascript:void(0);" @click="goToInfo(item)">{{item.title}}</a></p>
          <span>{{item.createTime}}</span>
        </li>
      </ul>
    </div>
</template>

<script>
import { getHotBlog } from "../../api/index";
import {getBlogByUid} from "../../api/blogContent";
export default {
  name: "TagCloud",
  data() {
    return {
      hotBlogData: [] //热门博客列表
    };
  },
  created() {
    getHotBlog().then(response => {
      if (response.code == this.$ECode.SUCCESS) {
        this.hotBlogData = response.data.records;
      }
    });
  },
  computed: {
    //添加一个计算属性用于简单过滤掉数组中第一个数据
    sideNews() {
      return this.hotBlogData.filter(blog =>
        this.hotBlogData.indexOf(blog) != 0
      )
    }
  },
  methods: {
    //跳转到文章详情【或推广链接】
    goToInfo(blog) {
      if(blog.type == "0") {
        let routeData = this.$router.resolve({
          path: "/info",
          query: {blogOid: blog.oid}
        });
        window.open(routeData.href, '_blank');
      } else if(blog.type == "1") {
        var params = new URLSearchParams();
        params.append("uid", blog.uid);
        getBlogByUid(params).then(response => {
          // 记录一下用户点击日志
        });
        window.open(blog.outsideLink, '_blank');
      }
    },
  }
};
</script>

<style>

</style>
