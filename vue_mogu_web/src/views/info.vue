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

<article>
  <h1 class="t_nav"><a href="/" class="n1">网站首页</a><a href="/" class="n2">{{blogData.blogSort.sortName}}</a></h1>
  <div class="infosbox">
    <div class="newsview">
      <h3 class="news_title" v-if="blogData.title">{{blogData.title}}</h3>
      <div class="bloginfo">
        <ul>
          <li class="author"><a href="/">{{blogData.author}}</a></li>
          <li class="lmname"><a href="javascript:void(0);" @click="goToSortList(blogData.blogSort.uid)">{{blogData.blogSort.sortName}}</a></li>
          <li class="timer">{{blogData.createTime}}</li>
          <li class="view">{{blogData.clickCount}}</li>
          <li class="like">{{blogData.collectCount}}</li>
        </ul>
      </div>
      <div class="tags">        
        <a v-if="blogData.tagList" v-for="item in blogData.tagList" :key="item.uid" href="javascript:void(0);" @click="goToList(item.uid)" target="_blank">{{item.content}}</a>         
      </div>
      <div class="news_about"><strong>简介</strong>{{blogData.summary}}</div>
      <div class="news_con" v-html="blogData.content" v-highlight> 
        {{blogData.content}} 
      </div>
    </div>
    <div class="share">
      <p class="diggit"><a href="JavaScript:makeRequest('/e/public/digg/?classid=3&amp;id=19&amp;dotop=1&amp;doajax=1&amp;ajaxarea=diggnum','EchoReturnedText','GET','');"> 很赞哦！ </a>(<b id="diggnum">13</b>)</p>
      <p class="dasbox"><a href="javascript:void(0)" onClick="dashangToggle()" class="dashang" title="打赏，支持一下">打赏本站</a></p>
      <div class="hide_box"></div>
      <div class="shang_box"> <a class="shang_close" href="javascript:void(0)" onclick="dashangToggle()" title="关闭">关闭</a>
        <div class="shang_tit">
          <p>感谢您的支持，我会继续努力的!</p>
        </div>
        <div class="shang_payimg"> <img src="../../static/images/alipayimg.jpg" alt="扫码支持" title="扫一扫"> </div>
        <div class="pay_explain">扫码打赏，你说多少就多少</div>
        <div class="shang_payselect">
          <div class="pay_item checked" data-id="alipay"> <span class="radiobox"></span> <span class="pay_logo"><img src="../../static/images/alipay.jpg" alt="支付宝"></span> </div>
          <div class="pay_item" data-id="weipay"> <span class="radiobox"></span> <span class="pay_logo"><img src="../../static/images/wechat.jpg" alt="微信"></span> </div>
        </div>
      </div>
    </div>

    <div class="otherlink" v-if="sameBlogData.length > 0">
      <h2>相关文章</h2>
      <ul>
        <li v-for="item in sameBlogData" :key="item.uid"><a href="javascript:void(0);" @click="goToInfo(item.uid)" title="item.title">{{item.title}}</a></li>
      </ul>
    </div>
    <div class="news_pl">
      <h2>文章评论</h2>
      <ul>
        <ChangYan :sid="this.blogUid"></ChangYan>
      </ul>
    </div>
  </div>
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
          <li v-for="item in linkData" :key="item.uid"><a :href="item.url" target="_blank" v-if="item.title">{{item.title}}</a></li>
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
import Head from '../components/Head';
import BlogHead from "../components/BlogHead";
import BlogFooter from "../components/BlogFooter";
import { getBlogByLevel, getNewBlog, getHotTag, getLink } from "../api/index";
import { getBlogByUid, getSameBlog } from "../api/blogContent";

import ThirdRecommend from "../components/ThirdRecommend";
import FourthRecommend from "../components/FourthRecommend";
import TagCloud from "../components/TagCloud";
import HotBlog from "../components/HotBlog";
import FollowUs from "../components/FollowUs";
import ChangYan from "../components/ChangYan";
export default {
  name: "info",
  data() {
    return {
      blogUid: null, //传递过来的博客uid
      blogData: null,
      sameBlogData: [], //相关文章
      linkData: [] //友情链接
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
    ChangYan,
    Head,
  },
  created() {
    getLink().then(response => {
      console.log("友情链接列表", response);
      this.linkData = response.data.records;
    });

    var params = new URLSearchParams();
    this.blogUid = this.$route.query.blogUid;
    params.append("uid", this.blogUid);
    getBlogByUid(params).then(response => {
      console.log("通过uid获取博客", response);
      if (response.code == "success") {
        this.blogData = response.data;
        var params1 = new URLSearchParams();
        params1.append("tagUid", response.data.tagUid);
        getSameBlog(params1).then(sameResponse => {
          console.log(sameResponse);
          if (sameResponse.code == "success") {
            this.sameBlogData = sameResponse.data.records;
          }
        });
      }
    });
  },
  methods: {
    //跳转到文章详情
    goToInfo(uid) {
      let routeData = this.$router.resolve({ path: "/info", query: { blogUid: uid } });
      window.open(routeData.href, '_blank');
    },
    //跳转到搜索详情页
    goToList(uid) {
      let routeData = this.$router.resolve({ path: "/list", query: { tagUid: uid } });
      window.open(routeData.href, '_blank');
    },
    //跳转到搜索详情页
    goToSortList(uid) {
      let routeData = this.$router.resolve({ path: "/list", query: { sortUid: uid } });
      window.open(routeData.href, '_blank');
    },
  }
};
</script>

<style>
</style>
