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

<div class="pagebg sh"></div>
<div class="container">
  <h1 class="t_nav"><span>慢生活，不是懒惰，放慢速度不是拖延时间，而是让我们在生活中寻找到平衡。</span><a href="/" class="n1">网站首页</a><a href="/" class="n2">搜索</a></h1>
  
  <!--blogsbox begin-->
  <div class="blogsbox">
    	<div v-for="item in blogData" :key="item.uid" class="blogs" data-scroll-reveal="enter bottom over 1s" >	  
      <h3 class="blogtitle"><a href="javascript:void(0);" @click="goToInfo(item.id?item.id:item.uid)" target="_blank" v-html="item.title">{{item.title}}</a></h3>
      <span class="blogpic"><a href="javascript:void(0);" @click="goToInfo(item.id?item.id:item.uid)" title=""><img v-if="item.photoList" :src="item.photoList[0]" alt=""></a></span>
      <p class="blogtext" v-html="item.summary">{{item.summary}}</p>
      <div class="bloginfo">
        <ul>
          <li class="author"><a href="/">{{item.author}}</a></li>
          <li class="lmname" v-if="item.blogSort"><a href="javascript:void(0);" @click="goToList(item.blogSortUid)">{{item.blogSort}}</a></li>
          <li class="timer">{{item.updateTime}}</li>
          <li class="view"><span>{{item.clickCount}}</span></li>
          <li class="like">{{item.collectCount}}</li>
        </ul>
      </div>
    </div>
    <div class="isEnd">
      <span v-if="blogData.length> 0">
        <span v-if="!isEnd">正在加载中~</span>  
        <span v-else>我也是有底线的~</span>    
      </span>
      <span v-else>
        <span>空空如也~</span>    
      </span>      
      
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
import BlogHead from "../components/BlogHead";
import BlogFooter from "../components/BlogFooter";

import ThirdRecommend from "../components/ThirdRecommend";
import FourthRecommend from "../components/FourthRecommend";
import TagCloud from "../components/TagCloud";
import HotBlog from "../components/HotBlog";
import FollowUs from "../components/FollowUs";

import { searchBlog, searchBlogByTag, searchBlogBySort, searchBlogByAuthor  } from "../api/search";

export default {
  name: "list",
  data() {
    return {
      blogData: [],
      isEnd: true,
      keywords: "",
      tagUid: "",
      sortUid: ""
    };
  },
  components: {
    //注册组件
    BlogHead,
    BlogFooter,
    FourthRecommend,
    ThirdRecommend,
    TagCloud,
    HotBlog,
    FollowUs,
    Head
  },
  created() {
    this.keywords = this.$route.query.keyword;
    this.tagUid = this.$route.query.tagUid;
    this.sortUid = this.$route.query.sortUid;
    this.author = this.$route.query.author;

    console.log("keywords", this.keywords);
    console.log("tagUid", this.tagUid);
    console.log("sortUid", this.sortUid);
    console.log("author", this.author);
    this.search();
  },
  watch: {
    $route(to, from) {
      console.log(to, from);
      this.keywords = this.$route.query.keyword;
      this.tagUid = this.$route.query.tagUid;
      this.sortUid = this.$route.query.sortUid;
      this.search();
    }
  },
  methods: {
    //跳转到文章详情
    goToInfo(uid) {
      let routeData = this.$router.resolve({
        path: "/info",
        query: { blogUid: uid }
      });
      window.open(routeData.href, "_blank");
    },
    //点击了分类
    goToList(uid) {
      console.log("点击了跳转", uid);
      let routeData = this.$router.resolve({ path: "/list", query: { sortUid: uid } });      
      window.open(routeData.href, '_blank');
    },
    search: function() {
      if (this.keywords != undefined) {
        var params = new URLSearchParams();
        params.append("keywords", this.keywords);
        searchBlog(params).then(response => {
          console.log(response);
          if (response.code == "success") {
            var blogData = response.data.rows;
            for (var i = 0; i < blogData.length; i++) {
              if (blogData[i].photoList) {
                var tempList = blogData[i].photoList.split(",");
                blogData[i].photoList = tempList;
              } else {
                blogData[i].photoList = [];
              }
            }
            this.blogData = blogData;
          }
          console.log("blogData", this.blogData);
        });
      } else if (this.tagUid != undefined) {

        var params = new URLSearchParams();
        params.append("tagUid", this.tagUid);
        searchBlogByTag(params).then(response => {
          console.log(response);
          if (response.code == "success") {
            console.log("根据标签查找", response);
            var blogData = response.data;
            for (var i = 0; i < blogData.length; i++) {
              // console.log(blogData[i].blogSort);
              blogData[i].blogSort = blogData[i].blogSort.sortName;
            }
            this.blogData = blogData;
          }
          console.log("blogData", this.blogData);
        });

      } else if(this.sortUid != undefined) {

        var params = new URLSearchParams();
        params.append("blogSortUid", this.sortUid);
        searchBlogBySort(params).then(response => {
          console.log(response);
          if (response.code == "success") {
            console.log("根据分类查找", response);
            var blogData = response.data;
            for (var i = 0; i < blogData.length; i++) {
              blogData[i].blogSort = blogData[i].blogSort.sortName;
            }
            this.blogData = blogData;
          }
          console.log("blogData", this.blogData);
        });

      } else if(this.author != undefined) {

        var params = new URLSearchParams();
        params.append("author", this.author);
        searchBlogByAuthor(params).then(response => {
          console.log(response);
          if (response.code == "success") {
            console.log("根据作者名查找", response);
            var blogData = response.data;
            for (var i = 0; i < blogData.length; i++) {
              blogData[i].blogSort = blogData[i].blogSort.sortName;
            }
            this.blogData = blogData;
          }
          console.log("blogData", this.blogData);
        });

      }
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
</style>

<style>
.isEnd {
  width: 100%;
  height: 40px;
  line-height: 40px;
  text-align: center;
}
</style>

