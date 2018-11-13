<template>
<header> 
  <!--menu begin-->
  <div class="menu">
    <nav class="nav" id="topnav">
      <h1 class="logo"><a href="javascript:void(0);" @click="goTo('/')">蘑菇博客</a></h1>
      <li><a href="javascript:void(0);" @click="goTo('/')">网站首页</a> </li>
      <li><a href="javascript:void(0);" @click="goTo('/about')">关于我</a> </li>
      <li><a href="javascript:void(0);" @click="goTo('/study')">学习教程</a></li>
      <li><a href="javascript:void(0);" @click="goTo('/list')">学无止境</a>
        <ul class="sub-nav">
          <li><a href="/list">心得笔记</a></li>
          <li><a href="/list">CSS3|Html5</a></li>
          <li><a href="/list">网站建设</a></li>
          <li><a href="/list">推荐工具</a></li>
          <li><a href="/list">JS实例索引</a></li>
        </ul>
      </li>

      <li><a href="javascript:void(0);" @click="goTo('/time')">时间轴</a> </li>

                <!-- <input />
          <img src="../../../static/images/searchbg.png"/>z -->

      <!--search begin-->
      <div id="search_bar" class="search_bar search_open">
          <input class="input" placeholder="想搜点什么呢..." type="text" v-model="keyword" >
          <span class="search_ico" @click="search()"></span>
        <div>
        </div>
      </div>
      <!--search end--> 
    </nav>
  </div>
  <!--menu end-->

  <!--mnav begin-->
  <div id="mnav">
    <h2><a href="http://www.yangqq.com" class="mlogo">蘑菇博客</a><span class="navicon"></span></h2>
    <dl class="list_dl">
      <dt class="list_dt"> <a href="index.html">网站首页</a> </dt>
      <dt class="list_dt"> <a href="about.html">关于我</a> </dt>
      <dt class="list_dt"> <a href="#">模板分享</a> </dt>
      <dd class="list_dd">
        <ul>
          <li><a href="share.html">个人博客模板</a></li>
          <li><a href="share.html">国外Html5模板</a></li>
          <li><a href="share.html">企业网站模板</a></li>
        </ul>
      </dd>
      <dt class="list_dt"> <a href="#">学无止境</a> </dt>
      <dd class="list_dd">
        <ul>
          <li><a href="list.html">心得笔记</a></li>
          <li><a href="list.html">CSS3|Html5</a></li>
          <li><a href="list.html">网站建设</a></li>
          <li><a href="list.html">推荐工具</a></li>
          <li><a href="list.html">JS实例索引</a></li>
        </ul>
      </dd>
      <dt class="list_dt"> <a href="#">慢生活</a> </dt>
      <dd class="list_dd">
        <ul>
          <li><a href="life.html">日记</a></li>
          <li><a href="life.html">欣赏</a></li>
          <li><a href="life.html">程序人生</a></li>
          <li><a href="life.html">经典语录</a></li>
        </ul>
      </dd>
      <dt class="list_dt"> <a href="time.html">时间轴</a> </dt>
    </dl>
  </div>
  <!--mnav end--> 
</header>
</template>

<script>
import { BASE_BLOG_API } from "../../../config/dev.env"

export default {
  name: 'Head',
  created() {
  	console.log(BASE_BLOG_API);
  },
  data () {
  	return {
      BaseBlog: BASE_BLOG_API,
      keyword: "",  		
    };
  },
  methods: {
    goTo: function(url) {
      switch(url) {
        case "/" : { this.$router.push({ path: '/'}); }; break;
        case "/about" : { this.$router.push({ path: '/about'}); }; break;
        case "/study" : { this.$router.push({ path: '/share'}); }; break;
        case "/list" : { this.$router.push({ path: '/list'}); }; break;
        case "/time" : { this.$router.push({ path: '/time'}); }; break;
      } 
    },
    search: function() {
      
      if(this.keyword == "") {
        alert("关键字不能为空");
        return;
      }
      this.$router.push({ path: "/list", query: { keyword: this.keyword } })
    },
  }
}
</script>

<style>
	
</style>
