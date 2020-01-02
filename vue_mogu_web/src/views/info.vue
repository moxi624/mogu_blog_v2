<template>
  <article>
    <h1 class="t_nav">
      <a href="/" class="n1">网站首页</a>
      <a
        href="javascript:void(0);"
        @click="goToSortList(blogData.blogSort.uid)"
        class="n2"
      >{{blogData.blogSort ? blogData.blogSort.sortName:""}}</a>
    </h1>
    <div class="infosbox">
      <div class="newsview">
        <h3 class="news_title" v-if="blogData.title">{{blogData.title}}</h3>
        <div class="bloginfo" v-if="blogData.blogSort">
          <ul>
            <li class="author">
              <span class="iconfont">&#xe60f;</span>
              <a href="javascript:void(0);" @click="goToAuthor(blogData.author)">{{blogData.author}}</a>
            </li>
            <li class="lmname">
              <span class="iconfont">&#xe603;</span>
              <a
                href="javascript:void(0);"
                @click="goToSortList(blogData.blogSort == null ?'':blogData.blogSort.uid)"
              >{{blogData.blogSort ? blogData.blogSort.sortName:""}}</a>
            </li>
            <li class="createTime"><span class="iconfont">&#xe606;</span>{{blogData.createTime}}</li>
            <li class="view"><span class="iconfont">&#xe8c7;</span>{{blogData.clickCount}}</li>
            <li class="like"><span class="iconfont">&#xe663;</span>{{blogData.collectCount}}</li>
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
          <strong>版权</strong>
          {{blogData.copyright}}
        </div>
        <div
          class="news_con fixck"
          v-html="blogData.content"
          v-highlight
          @click="imageChange"
        >{{blogData.content}}
        </div>
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
            >{{subText(item.title, 18)}}</a>
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
</template>

<script>
  import {getLink, recorderVisitPage} from "../api/index";
  import {getBlogByUid, getSameBlogByBlogUid} from "../api/blogContent";

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
      FourthRecommend,
      ThirdRecommend,
      TagCloud,
      HotBlog,
      FollowUs,
      ChangYan,
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
          query: {blogUid: uid}
        });
        window.open(routeData.href, "_blank");
      },
      //跳转到搜索详情页
      goToList(uid) {
        let routeData = this.$router.resolve({
          path: "/list",
          query: {tagUid: uid}
        });
        window.open(routeData.href, "_blank");
      },
      //跳转到搜索详情页
      goToSortList(uid) {
        let routeData = this.$router.resolve({
          path: "/list",
          query: {sortUid: uid}
        });
        window.open(routeData.href, "_blank");
      },
      //跳转到搜索详情页
      goToAuthor(author) {
        let routeData = this.$router.resolve({
          path: "/list",
          query: {author: author}
        });
        window.open(routeData.href, "_blank");
      },

      imageChange: function (e) {
        //首先需要判断点击的是否是图片
        var type = e.target.localName;
        if (type == "img") {
          window.open(e.target.currentSrc);
        }
      },
      //切割字符串
      subText: function (str, index) {
        if (str.length < index) {
          return str;
        }
        return str.substring(0, index) + "...";
      }
    }
  };
</script>

<style>
  .fixck {
    /* font-family: Arial, Verdana, sans-serif !important;
    font-size: 12px !important;
    color: #222 !important;
    line-height: normal !important; */
  }

  .fixck p {
    margin: 12px 0 !important;
  }

  .fixck a {
    text-decoration: underline !important;
    color: #00e !important;
  }

  .fixck ul li {
    list-style: disc;
  }

  .fixck ol li {
    list-style: decimal;
  }

  .fixck ul,
  .fixck ol {
    padding-left: 40px !important;
    padding-right: 40px !important;
  }

  .fixck li {
    display: list-item !important;
  }

  .fixck h1 {
    font-weight: bold !important;
    font-size: 32px !important;
    margin: 21px 0 !important;
  }

  .fixck h2 {
    font-weight: bold !important;
    font-size: 24px !important;
    margin: 19px 0 !important;
  }

  .fixck h3 {
    font-weight: bold !important;
    font-size: 19px !important;
    margin: 18px 0 !important;
  }

  .fixck h4 {
    font-weight: bold !important;
    font-size: 16px !important;
    margin: 21px 0 !important;
  }

  .fixck h5 {
    font-weight: bold !important;
    font-size: 13px !important;
    margin: 22px 0 !important;
  }

  .fixck h6 {
    font-weight: bold !important;
    font-size: 11px !important;
    margin: 24px 0 !important;
  }

  .news_con {
    line-height: 1.8;
    font-size: 16px;
    text-align: justify;
  }

  .iconfont {
    font-size: 14px;
    margin-right: 3px;
  }
</style>
