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

  <FirstRecommend></FirstRecommend>
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
      <h3 class="blogtitle"><a href="javascript:void(0);" @click="goToInfo(item.uid)" target="_blank">{{item.title}}</a></h3>
      <span class="blogpic"><a href="javascript:void(0);" @click="goToInfo(item.uid)" title=""><img v-if="item.photoList" :src="item.photoList[0]" alt=""></a></span>
      <p class="blogtext">{{item.summary}}</p>
      <div class="bloginfo">
        <ul>
          <li class="author"><a href="/">{{item.author}}</a></li>
          <li class="lmname"><a href="/">{{item.blogSort.sortName}}</a></li>
          <li class="timer">{{item.createTime}}</li>
          <li class="view"><span>{{item.clickCount}}</span></li>
          <li class="like">{{item.collectCount}}</li>
        </ul>
      </div>
    </div>
  </div>
  <!--blogsbox end-->
  
  
  <div class="sidebar">

    <!-- 三级推荐 -->
    <ThirdRecommend></ThirdRecommend>

    <!--标签云-->
    <TagCloud></TagCloud>

    <!--四级推荐-->
    <FourthRecommend></FourthRecommend>

    <!--点击排行-->
    <HotBlog></HotBlog>

    <div class="links">
      <h2 class="hometitle">友情链接</h2>
      <ul>
          <li v-for="item in linkData" :key="item.uid"><a :href="item.url" target="_blank">{{item.title}}</a></li>
      </ul>
    </div>

    <!--关注我们-->
    <FollowUs></FollowUs>

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
import FirstRecommend from '../components/FirstRecommend';
import ThirdRecommend from '../components/ThirdRecommend';
import FourthRecommend from '../components/FourthRecommend';
import TagCloud from '../components/TagCloud';
import HotBlog from '../components/HotBlog';
import FollowUs from '../components/FollowUs';
import { getBlogByLevel, getNewBlog, getHotBlog,  getHotTag, getLink } from "../api/index";
export default {
  name: "index",
  components: {
    //注册组件
    BlogHead,
    BlogFooter,
    FirstRecommend,
    FourthRecommend,
    ThirdRecommend,
    TagCloud,
    HotBlog,
    FollowUs,
  },
  data() {
    return {
      firstData: [], //；一级推荐数据
      secondData: [], //；二级级推荐数据
      thirdData: [], //三级推荐
      fourthData: [], //四级推按
      newBlogData: [], //最新文章
      hotBlogData: [], //最热文章
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

    var secondParams = new URLSearchParams();
    secondParams.append("currentPage", 0);
    secondParams.append("pageSize", 2);
    secondParams.append("level", 2);    
    getBlogByLevel(secondParams).then(response => {
      console.log("二级推荐", response);
      this.secondData = response.data.records;
    });

    var newBlogParams = new URLSearchParams();
    newBlogParams.append("currentPage", 0);
    newBlogParams.append("pageSize", 30);
    getNewBlog(newBlogParams).then(response => {
      console.log("最新博客", response);
      this.newBlogData = response.data.records;
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
