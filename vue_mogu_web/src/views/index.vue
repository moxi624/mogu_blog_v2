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
          <li class="lmname" v-if="item.blogSort"><a href="/">{{item.blogSort.sortName}}</a></li>
          <li class="timer">{{item.createTime}}</li>
          <li class="view"><span>{{item.clickCount}}</span></li>
          <li class="like">{{item.collectCount}}</li>
        </ul>
      </div>
    </div>
    <div class="isEnd">
      <span v-if="!isEnd">正在加载中~</span>  
      <span v-else>我也是有底线的~</span>  
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

    <Link></Link>

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
import FirstRecommend from "../components/FirstRecommend";
import ThirdRecommend from "../components/ThirdRecommend";
import FourthRecommend from "../components/FourthRecommend";
import TagCloud from "../components/TagCloud";
import HotBlog from "../components/HotBlog";
import FollowUs from "../components/FollowUs";
import Link from "../components/Link";
import {
  getBlogByLevel,
  getNewBlog,
  getHotBlog,
  getHotTag,
} from "../api/index";
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
    Link
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
      keyword: "",
      currentPage: 1,
      pageSize: 15,
      total: 0, //总数量
      isEnd: false, //是否到底底部了
    };
  },
  mounted() {
    // 注册scroll事件并监听
    var that = this;
    window.addEventListener("scroll", function() {
      let scrollTop = document.documentElement.scrollTop; //当前的的位置
      let scrollHeight = document.documentElement.scrollHeight; //最高的位置
      if (scrollTop >= 0.7 * scrollHeight && !that.isEnd) {
        that.currentPage = that.currentPage + 1;        
        var params = new URLSearchParams();
        params.append("currentPage", that.currentPage);
        params.append("pageSize", that.pageSize);
        getNewBlog(params).then(response => {                  
          if(response.code == "success" && response.data.records.length > 0) {
            that.isEnd = false;
            var newData = that.newBlogData.concat(response.data.records);
            that.newBlogData = newData;            
            that.total = response.data.total;
            that.pageSize = response.data.size;
            that.currentPage = response.data.current;                      
          } else {
            that.isEnd = true;
          }          
        });
      }
    });
  },
  created() {
    var secondParams = new URLSearchParams();
    secondParams.append("level", 2);
    getBlogByLevel(secondParams).then(response => {
      console.log("二级推荐", response);
      this.secondData = response.data.records;
    });

    // 获取最新博客
    this.newBlogList();

  },
  methods: {
    //跳转到文章详情
    goToInfo(uid) {
      console.log("跳转到文章详情");
      let routeData = this.$router.resolve({ path: "/info", query: { blogUid: uid } });
      window.open(routeData.href, '_blank');
    },
    //最新博客列表
    newBlogList() {
      var params = new URLSearchParams();
      params.append("currentPage", this.currentPage);
      params.append("pageSize", this.pageSize);
      getNewBlog(params).then(response => {
        console.log("最新博客", response);
        if(response.code == "success") {          
          this.newBlogData = response.data.records;
          this.total = response.data.total;
          this.pageSize = response.data.size;
          this.currentPage = response.data.current;
        }        
      });
    }
  }
};
</script>

<style>
.isEnd {
  width: 100%;
  height: 40px;
  line-height: 40px;
  text-align: center;
}
</style>
