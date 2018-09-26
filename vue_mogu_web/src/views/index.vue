<template>
<html>
<body>
<head>
<meta charset="utf-8">
<title>蘑菇博客 - 一个Java开发人员的个人博客网站</title>
<meta name="keywords" content="个人博客,蘑菇博客" />
<meta name="description" content="蘑菇博客 - 一个Java开发人员的个人博客网站" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<!--
	作者：xzx19950624@qq.com
	时间：2018-07-15
	描述：顶部标题
-->
<BlogHead></BlogHead>

<article> 
  <!--banner begin-->
 <div class="picsbox"> 
  <div class="banner">
    <div id="banner" class="fader">
      <li v-for="item in bannerData" :key="item.uid" class="slide" ><a href="/" target="_blank"><img :src="item.photoList[0]"><span class="imginfo">{{item.title}}</span></a></li>
      <div class="fader_controls">
        <div class="page prev" data-target="prev">&lsaquo;</div>
        <div class="page next" data-target="next">&rsaquo;</div>
        <ul class="pager_list">
        </ul>
      </div>
    </div>
  </div>
  <!--banner end-->
  <div class="toppic">
    <li v-for="item in topicData" :key="item.uid"> <a href="/" target="_blank"> <i><img :src="item.photoList[0]"></i>
      <h2>{{item.title}}</h2>
      <span>{{item.blogSort.sortName}}</span> </a> 
    </li>
  </div>
  </div>
  <div class="blank"></div>
  <!--blogsbox begin-->
  <div class="blogsbox">
    	<div v-for="item in newBlogData" :key="item.uid" class="blogs" data-scroll-reveal="enter bottom over 1s" >	  
      <h3 class="blogtitle"><a href="/" target="_blank">{{item.title}}</a></h3>
      <span class="blogpic"><a href="/" title=""><img src="../../static/images/toppic01.jpg" alt=""></a></span>
      <p class="blogtext">{{item.summary}}</p>
      <div class="bloginfo">
        <ul>
          <li class="author"><a href="/">{{item.author}}</a></li>
          <li class="lmname"><a href="/">{{item.blogSort.sortName}}</a></li>
          <li class="timer">{{item.createTime}}</li>
          <li class="view"><span>{{item.clickCount}}</span>已阅读</li>
          <li class="like">{{item.collectCount}}</li>
        </ul>
      </div>
    </div>
  </div>
  <!--blogsbox end-->
  
  <div class="sidebar">
    <div class="zhuanti">
      <h2 class="hometitle">特别推荐</h2>
      <ul>
        <li> <i><img src="../../static/images/banner03.jpg"></i>
          <p>帝国cms调用方法 <span><a href="/">阅读</a></span> </p>
        </li>
        <li> <i><img src="../../static/images/b04.jpg"></i>
          <p>5.20 我想对你说 <span><a href="/">阅读</a></span></p>
        </li>
        <li> <i><img src="../../static/images/b05.jpg"></i>
          <p>个人博客，属于我的小世界！ <span><a href="/">阅读</a></span></p>
        </li>
      </ul>
    </div>
    <div class="tuijian">
      <h2 class="hometitle">推荐文章</h2>
      <ul class="tjpic">
        <i><img src="../../static/images/toppic01.jpg"></i>
        <p><a href="/">别让这些闹心的套路，毁了你的网页设计</a></p>
      </ul>
      <ul class="sidenews">
        <li> <i><img src="../../static/images/toppic01.jpg"></i>
          <p><a href="/">别让这些闹心的套路，毁了你的网页设计</a></p>
          <span>2018-05-13</span> </li>
        <li> <i><img src="../../static/images/toppic02.jpg"></i>
          <p><a href="/">给我模板PSD源文件，我给你设计HTML！</a></p>
          <span>2018-05-13</span> </li>
        <li> <i><img src="../../static/images/v1.jpg"></i>
          <p><a href="/">别让这些闹心的套路，毁了你的网页设计</a></p>
          <span>2018-05-13</span> </li>
        <li> <i><img src="../../static/images/v2.jpg"></i>
          <p><a href="/">给我模板PSD源文件，我给你设计HTML！</a></p>
          <span>2018-05-13</span> </li>
      </ul>
    </div>
    <div class="tuijian">
      <h2 class="hometitle">点击排行</h2>
      <ul class="tjpic">
        <i><img src="../../static/images/toppic01.jpg"></i>
        <p><a href="/">别让这些闹心的套路，毁了你的网页设计</a></p>
      </ul>
      <ul class="sidenews">
        <li> <i><img src="../../static/images/toppic01.jpg"></i>
          <p><a href="/">别让这些闹心的套路</a></p>
          <span>2018-05-13</span> </li>
        <li> <i><img src="../../static/images/toppic02.jpg"></i>
          <p><a href="/">给我模板PSD源文件，我给你设计HTML！</a></p>
          <span>2018-05-13</span> </li>
        <li> <i><img src="../../static/images/v1.jpg"></i>
          <p><a href="/">别让这些闹心的套路，毁了你的网页设计</a></p>
          <span>2018-05-13</span> </li>
        <li> <i><img src="../../static/images/v2.jpg"></i>
          <p><a href="/">给我模板PSD源文件，我给你设计HTML！</a></p>
          <span>2018-05-13</span> </li>
      </ul>
    </div>
    <div class="cloud">
      <h2 class="hometitle">标签云</h2>
      <ul>

        <a v-for="item in hotTagData" :key="item.uid" href="/">{{item.content}}</a>
      </ul>
    </div>
    <div class="links">
      <h2 class="hometitle">友情链接</h2>
      <ul>
          <li v-for="item in linkData" :key="item.uid"><a :href="item.url" target="_blank">{{item.title}}</a></li>
      </ul>
    </div>
    <div class="guanzhu" id="follow-us">
      <h2 class="hometitle">关注我们 么么哒！</h2>
      <ul>
        <li class="sina"><a href="/" target="_blank"><span>新浪微博</span>蘑菇博客</a></li>
        <li class="tencent"><a href="/" target="_blank"><span>腾讯微博</span>蘑菇博客</a></li>
        <li class="qq"><a href="/" target="_blank"><span>QQ号</span>1595833114</a></li>
        <li class="email"><a href="/" target="_blank"><span>邮箱帐号</span>1595833114@qq.com</a></li>
        <li class="wxgzh"><a href="/" target="_blank"><span>微信号</span>moxi624</a></li>
        <li class="wx"><img src="../../static/images/wx.jpg"></li>
      </ul>
    </div>
  </div>
</article>

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
import BlogHead from "../components/BlogHead";
import BlogFooter from "../components/BlogFooter";
import { getBanner, getTopic, getNewBlog, getHotTag, getLink } from "../api/index";
export default {
  name: "index",
  data() {
    return {};
  },
  components: {
    //注册组件
    BlogHead,
    BlogFooter
  },
  data() {
    return {
      bannerData: [], //；轮播图数据
      topicData: [], //顶部推荐
      newBlogData: [], //最新文章
      hotTagData: [], //最新文章
      linkData: [], //友情链接
      keyword: "",
      currentPage: 1,
      pageSize: 10,
      total: 0 //总数量
    };
  },
  created() {
    console.log("测试", process.env.WEB_API);
    var that = this;
    var params = new URLSearchParams();
    params.append("currentPage", 0);
    params.append("pageSize", 5);
    getBanner(params).then(response => {
      console.log("博客列表", response);
      this.bannerData = response.data.records;
      this.currentPage = response.data.current;
      this.pageSize = response.data.size;
      this.total = response.data.total;
    });

    var topicParams = new URLSearchParams();
    topicParams.append("currentPage", 0);
    topicParams.append("pageSize", 2);
    getTopic(topicParams).then(response => {
      console.log("博客列表", response);
      this.topicData = response.data.records;
    });

    var newBlogParams = new URLSearchParams();
    newBlogParams.append("currentPage", 0);
    newBlogParams.append("pageSize", 30);
    getTopic(newBlogParams).then(response => {
      console.log("博客列表", response);
      this.newBlogData = response.data.records;
    });

    var tagParams = new URLSearchParams();
    getHotTag(tagParams).then(response => {
      console.log("标签列表", response);
      this.hotTagData = response.data.records;
    });

    var linkParams = new URLSearchParams();
    getLink(linkParams).then(response => {
      console.log("友情链接列表", response);
      this.linkData = response.data.records;
    });

  }
};
</script>


<style>
</style>
