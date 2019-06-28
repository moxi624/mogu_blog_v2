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
      <ChangYan :sid="sid"></ChangYan>
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

import {recorderVisitPage} from "../api/index";
import { getMe } from "../api/about";

export default {
  name: 'about',
  data () {
  	return {
      info: {},
      sid: "test",
      isRouterAlive: false
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

  created() {
    var that = this;
    getMe().then(response => {      
      if(response.code == "success") {
        this.info = response.data;
      }
    })    

    var params = new URLSearchParams();
    params.append("pageName", "ABOUT");
    recorderVisitPage(params).then(response => {
    });
    
    var reloaded = window.localStorage.getItem('reloaded') || '0'
    if (reloaded % 2 == 0 ) {
      window.location.reload()
      window.localStorage.setItem('reloaded',  + 1)
    }

  },
  methods: {

  }
}
</script>

<style>


</style>
