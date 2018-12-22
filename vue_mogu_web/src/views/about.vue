<template>
<html>

<Head></Head>

<body>
<!--
	作者：xzx19950624@qq.com
	时间：2018-07-15
	描述：顶部标题
-->
<BlogHead></BlogHead>

<div class="pagebg ab"> </div>
<div class="container">
  <h1 class="t_nav"><span>你，我生命中一个重要的过客，我们之所以是过客，因为你未曾会为我停留。</span><a href="/" class="n1">网站首页</a><a href="/" class="n2">留言</a></h1>
  <div class="news_infos">
    <ul>

        <keep-alive>
          <ChangYan :sid="sid"  ></ChangYan>
        </keep-alive>

    </ul>
  </div>
  <div class="sidebar">
    <div class="about">
      <p class="avatar" v-if="info.photoList"> <img :src="info.photoList[0]" alt=""> </p>
      <p class="abname">{{info.nickName}}</p>
      <p class="abposition">{{info.occupation}}</p>
      <p class="abtext"> {{info.summary}}</p>
    </div>
  <!--
    关注我们
  -->
  <follow-us></follow-us>
  </div>
</div>

<!--
	作者：xzx19950624@qq.com
	时间：2018-07-15
	描述：博客底部
-->
<BlogFooter></BlogFooter>

<a href="#" class="cd-top">Top</a>
</body>
</html>
</template>

<script>
import Head from '../components/Head';
import BlogHead from '../components/BlogHead';
import BlogFooter from '../components/BlogFooter';
import FollowUs from "../components/FollowUs";
import ChangYan from "../components/ChangYan";

import { getMe } from "../api/about";

export default {
  name: 'about',
  data () {
  	return {
      info: {},
      sid: "",
    };
  },
  components: {
  	//注册组件
  	BlogHead,
  	BlogFooter,
    FollowUs,
    ChangYan,
    Head,
  },
  activated: function() {
    this.getCase()
    
  },
  created() {
    getMe().then(response => {      
      if(response.code == "success") {
        this.info = response.data;
      }
    })
    
    this.sid = "test";
  }
}
</script>

<style>


</style>
