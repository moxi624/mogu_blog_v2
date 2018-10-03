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
      <li v-for="item in firstData" :key="item.uid" class="slide"  @click="goToInfo(item.uid)"><a href="javascript:void(0);" target="_blank"><img :src="item.photoList[0]"><span class="imginfo">{{item.title}}</span></a></li>
      <div class="fader_controls">
        <div class="page prev" data-target="prev">&lsaquo;</div>
        <div class="page next" data-target="next">&rsaquo;</div>
        <ul class="pager_list">
        </ul>
      </div>
    </div>
  </div>
  <!--banner end-->

  <!-- 二级推荐 -->
  <div class="toppic">
    <li v-for="item in secondData" :key="item.uid" @click="goToInfo(item.uid)"> <a href="javascript:void(0);" target="_blank"> <i><img :src="item.photoList[0]"></i>
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
  
  <!-- 三级推荐 -->
  <div class="sidebar">
    <div class="zhuanti">
      <h2 class="hometitle">特别推荐</h2>
      <ul>
        <li v-for="item in thirdData" :key="item.uid"> <i><img :src="item.photoList[0]"></i>
          <p>{{item.title}} <span><a href="/">阅读</a></span> </p>
        </li>
      </ul>
    </div>
    <div class="tuijian">
      <h2 class="hometitle">推荐文章</h2>
      <ul class="tjpic" v-if="fourthData[0]">
        <i><img :src="fourthData[0].photoList[0]"></i>
        <p><a href="/">{{fourthData[0].title}}</a></p>
      </ul>

      <ul class="sidenews">

        <li v-for="(item, index) in fourthData" v-if="index != 0" :key="item.uid">
          <i><img :src="item.photoList[0]"></i>
          <p><a href="/">{{item.title}}</a></p>
          <span>{{item.createTime}}</span> 
        </li>
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
import { getBlogByLevel, getNewBlog, getHotTag, getLink } from "../api/index";
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
      firstData: [], //；一级推荐数据
      secondData: [], //；二级级推荐数据
      thirdData: [], //三级推荐
      fourthData: [], //四级推按
      newBlogData: [], //最新文章
      hotTagData: [], //最新标签
      linkData: [], //友情链接
      keyword: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
    };
  },
  created() {

    var that = this;
    var params = new URLSearchParams();
    params.append("level", 1);    
    getBlogByLevel(params).then(response => {
      console.log("一级推荐", response);
      this.firstData = response.data.records;      
    });

    var secondParams = new URLSearchParams();
    secondParams.append("currentPage", 0);
    secondParams.append("pageSize", 2);
    secondParams.append("level", 2);    
    getBlogByLevel(secondParams).then(response => {
      console.log("二级推荐", response);
      this.secondData = response.data.records;
    });

    var thirdParams = new URLSearchParams();
    thirdParams.append("currentPage", 0);
    thirdParams.append("pageSize", 3);
    thirdParams.append("level", 3);    
    getBlogByLevel(thirdParams).then(response => {
      console.log("三级推荐", response);
      this.thirdData = response.data.records;
    });

    var fourthParams = new URLSearchParams();
    fourthParams.append("currentPage", 0);
    fourthParams.append("pageSize", 5);
    fourthParams.append("level", 4);    
    getBlogByLevel(fourthParams).then(response => {
      console.log("四级推荐", response);
      this.fourthData = response.data.records;
    });

    var newBlogParams = new URLSearchParams();
    newBlogParams.append("currentPage", 0);
    newBlogParams.append("pageSize", 30);
    getNewBlog(newBlogParams).then(response => {
      console.log("最新博客", response);
      this.newBlogData = response.data.records;
    });

    getHotTag().then(response => {
      console.log("标签列表", response);
      this.hotTagData = response.data.records;
    });

    getLink().then(response => {
      console.log("友情链接列表", response);
      this.linkData = response.data.records;
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
