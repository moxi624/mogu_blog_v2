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
      <h1 class="t_nav">
        <a href="/" class="n1">网站首页</a>
        <a href="/" class="n2">{{blogData.blogSort.sortName}}</a>
      </h1>
      <div class="infosbox">
        <div class="newsview">
          <h3 class="news_title" v-if="blogData.title">{{blogData.title}}</h3>
          <div class="bloginfo">
            <ul>
              <li class="author">
                <a href="/">{{blogData.author}}</a>
              </li>
              <li class="lmname">
                <a
                  href="javascript:void(0);"
                  @click="goToSortList(blogData.blogSort.uid)"
                >{{blogData.blogSort.sortName}}</a>
              </li>
              <li class="timer">{{blogData.createTime}}</li>
              <li class="view">{{blogData.clickCount}}</li>
              <li class="like">{{blogData.collectCount}}</li>
            </ul>
          </div>
          <div class="tags">
            <a
              v-if="blogData.tagList"
              v-for="item in blogData.tagList"
              :key="item.uid"
              href="javascript:void(0);"
              @click="goToList(item.uid)"
              target="_blank"
            >{{item.content}}</a>
          </div>
          <div class="news_about">
            <strong>简介</strong>
            {{blogData.summary}}
          </div>
          <div
            class="news_con"
            v-html="blogData.content"
            v-highlight
            @click="imageChange"
          >{{blogData.content}}</div>
        </div>

        <!--付款码和点赞-->
        <PayCode :blogUid="blogUid"></PayCode>

        <div class="otherlink" v-if="sameBlogData.length > 0">
          <h2>相关文章</h2>
          <ul>
            <li v-for="item in sameBlogData" :key="item.uid">
              <a
                href="javascript:void(0);"
                @click="goToInfo(item.uid)"
                title="item.title"
              >{{item.title}}</a>
            </li>
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
            <li v-for="item in linkData" :key="item.uid">
              <a :href="item.url" target="_blank" v-if="item.title">{{item.title}}</a>
            </li>
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
import Head from "../components/Head";
import BlogHead from "../components/BlogHead";
import BlogFooter from "../components/BlogFooter";
import { getBlogByLevel, getNewBlog, getHotTag, getLink, recorderVisitPage } from "../api/index";
import { getBlogByUid, getSameBlogByTagUid, getSameBlogByBlogUid } from "../api/blogContent";

import ThirdRecommend from "../components/ThirdRecommend";
import FourthRecommend from "../components/FourthRecommend";
import TagCloud from "../components/TagCloud";
import HotBlog from "../components/HotBlog";
import FollowUs from "../components/FollowUs";
import ChangYan from "../components/ChangYan";
import PayCode from "../components/PayCode";

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
    PayCode
  },
  created() {
    getLink().then(response => {
      this.linkData = response.data.records;
    });

    var params = new URLSearchParams();
    this.blogUid = this.$route.query.blogUid;
    params.append("uid", this.blogUid);
    getBlogByUid(params).then(response => {
      if (response.code == "success") {
        this.blogData = response.data;
      }
    });

    var blogParams = new URLSearchParams();
    blogParams.append("blogUid", this.blogUid);
    getSameBlogByBlogUid(blogParams).then(response => {
      if (response.code == "success") {
        this.sameBlogData = response.data.records;
      }
    });

    var params = new URLSearchParams();
    params.append("pageName", "INFO");
    recorderVisitPage(params).then(response => {
    });
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
    //跳转到搜索详情页
    goToList(uid) {
      let routeData = this.$router.resolve({
        path: "/list",
        query: { tagUid: uid }
      });
      window.open(routeData.href, "_blank");
    },
    //跳转到搜索详情页
    goToSortList(uid) {
      let routeData = this.$router.resolve({
        path: "/list",
        query: { sortUid: uid }
      });
      window.open(routeData.href, "_blank");
    },
    imageChange: function(e) {
      //首先需要判断点击的是否是图片
      var type = e.target.localName;
      if (type == "img") {
        window.open(e.target.currentSrc);
      }
    }
  }
};
</script>

<style>
</style>
