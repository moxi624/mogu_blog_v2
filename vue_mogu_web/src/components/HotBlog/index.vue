<template>
    <div class="tuijian">
      <h2 class="hometitle">点击排行</h2>
      <ul class="tjpic" v-if="hotBlogData[0]">
        <i><img :src="hotBlogData[0].photoList[0]" @click="goToInfo(hotBlogData[0].uid)"></i>
        <p><a href="javascript:void(0);" @click="goToInfo(hotBlogData[0].uid)">{{hotBlogData[0].title}}</a></p>
      </ul>
      <ul class="sidenews">
        <li v-for="(item, index) in hotBlogData" v-if="index != 0" :key="item.uid"> 
          <i><img :src="item.photoList[0]" @click="goToInfo(item.uid)"></i>
          <p><a href="javascript:void(0);" @click="goToInfo(item.uid)">{{item.title}}</a></p>
          <span>{{item.createTime}}</span> 
        </li>
      </ul>
    </div>
</template>

<script>
import { getHotBlog } from "../../api/index";
export default {
  name: "TagCloud",
  data() {
    return {
      hotBlogData: [] //热门博客列表
    };
  },
  created() {
    getHotBlog().then(response => {
      console.log("最热博客排行", response);
      this.hotBlogData = response.data.records;
    });
  },
  methods: {
      //跳转到文章详情
	    goToInfo(uid) {
	      console.log("跳转到文章详情");
	      this.$router.push({ path: '/info', query: { blogUid: uid }});
	    }    
  }
};
</script>

<style>

</style>
