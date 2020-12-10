<template>
  <div>
    <div class="pagebg sh"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>慢生活，不是懒惰，放慢速度不是拖延时间，而是让我们在生活中寻找到平衡。</span>
        <a href="/" class="n1">网站首页</a>
        <a href="/" class="n2">搜索</a>
      </h1>

      <!--blogsbox begin-->
      <div class="blogsbox">
        <div
          v-for="item in blogData"
          :key="item.uid"
          class="blogs"
          data-scroll-reveal="enter bottom over 1s"
        >
          <h3 class="blogtitle">
            <a
              href="javascript:void(0);"
              @click="goToInfo(item)"
              v-html="item.title"
            >{{item.title}}</a>
          </h3>
          <span class="blogpic">
            <a href="javascript:void(0);" @click="goToInfo(item)" title>
              <img v-if="item.photoUrl" :src="item.photoUrl" alt="">
            </a>
          </span>
          <p class="blogtext" v-html="item.summary">{{item.summary}}</p>
          <div class="bloginfo">
            <ul>
              <li class="author">
                <span class="iconfont">&#xe60f;</span>
                <a href="javascript:void(0);" @click="goToAuthor(item.author)">{{item.author}}</a>
              </li>
              <li class="lmname" v-if="item.blogSortName">
                <span class="iconfont">&#xe603;</span>
                <a href="javascript:void(0);" @click="goToList(item.blogSortUid)">{{item.blogSortName}}</a>
              </li>
              <li class="createTime"><span class="iconfont">&#xe606;</span>{{item.createTime}}</li>
            </ul>
          </div>
        </div>

        <div class="isEnd">
          <div
            class="loadContent"
            @click="loadContent"
            v-if="!isEnd && !loading && totalPages>0"
          >点击加载更多</div>

          <div class="lds-css ng-scope" v-if="!isEnd && loading">
            <div style="width:100%;height:100%" class="lds-facebook">
              <div></div>
              <div></div>
              <div></div>
            </div>
          </div>

          <span v-if="blogData.length >= 0 && isEnd &&!loading && totalPages>0">我也是有底线的~</span>

          <span v-if="totalPages == 0 && !loading">空空如也~</span>
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
  </div>
</template>

<script>

import ThirdRecommend from "../components/ThirdRecommend";
import FourthRecommend from "../components/FourthRecommend";
import TagCloud from "../components/TagCloud";
import HotBlog from "../components/HotBlog";
import FollowUs from "../components/FollowUs";
import {
  searchBlog,
  searchBlogByTag,
  searchBlogBySort,
  searchBlogByAuthor
} from "../api/search";
import {getBlogByUid} from "../api/blogContent";

export default {
  name: "list",
  data() {
    return {
      blogData: [],
      keywords: "",
      currentPage: 1,
      totalPages: 0,
      pageSize: 10,
      total: 0, //总数量
      tagUid: "",
      searchBlogData: [], //搜索出来的文章
      sortUid: "",
      isEnd: false, //是否到底底部了
      loading: false //内容是否正在加载
    };
  },
  components: {
    FourthRecommend,
    ThirdRecommend,
    TagCloud,
    HotBlog,
    FollowUs,
  },
  created() {
    this.keywords = this.$route.query.keyword;
    this.tagUid = this.$route.query.tagUid;
    this.sortUid = this.$route.query.sortUid;
    this.author = this.$route.query.author;

    if (
      this.keywords == undefined &&
      this.tagUid == undefined &&
      this.sortUid == undefined &&
      this.author == undefined
    ) {
      return;
    }

    this.search();
  },
  mounted() {
    // 注册scroll事件并监听
    // var that = this;
    // window.addEventListener("scroll", function() {
    //   let scrollTop = document.documentElement.scrollTop; //当前的的位置
    //   let scrollHeight = document.documentElement.scrollHeight; //最高的位置
    //   if (scrollTop >= 0.6 * scrollHeight && !that.isEnd && !that.loading) {
    //     that.loading = true;
    //     that.currentPage = that.currentPage + 1;
    //     that.search();
    //   }
    // });
  },
  watch: {
    $route(to, from) {
      this.keywords = this.$route.query.keyword;
      this.tagUid = this.$route.query.tagUid;
      this.sortUid = this.$route.query.sortUid;
      this.searchBlogData = [] // 清空查询出来的博客
      this.search();
    }
  },
  methods: {
    //跳转到文章详情
    goToInfo(blog) {
      if(blog.type == "0") {
        let routeData = this.$router.resolve({
          path: "/info",
          query: {blogOid: blog.oid}
        });
        window.open(routeData.href, '_blank');
      } else if(blog.type == "1") {
        var params = new URLSearchParams();
        params.append("uid", blog.uid);
        getBlogByUid(params).then(response => {
          // 记录一下用户点击日志
        });
        window.open(blog.outsideLink, '_blank');
      }
    },
    //点击了分类
    goToList(uid) {
      let routeData = this.$router.resolve({
        path: "/list",
        query: { sortUid: uid }
      });
      window.open(routeData.href, '_blank');
    },
    goToAuthor(author) {
      let routeData = this.$router.resolve({
        path: "/list",
        query: {author: author}
      });
      window.open(routeData.href, '_blank');
    },
    // 加载内容
    loadContent: function() {
      var that = this;
      that.currentPage = that.currentPage + 1;
      that.search();
    },
    search: function() {
      var that = this;

      that.loading = true;

      if (this.keywords != undefined) {
        var params = new URLSearchParams();
        params.append("currentPage", that.currentPage);
        params.append("pageSize", that.pageSize);
        params.append("keywords", that.keywords);
        searchBlog(params).then(response => {
          if (response.code == this.$ECode.SUCCESS) {
            that.isEnd = false;
            //获取总页数
            that.totalPages = response.data.blogList.length;
            that.total = response.data.total;
            that.pageSize = response.data.pageSize;
            that.currentPage = response.data.currentPage;
            var blogData = response.data.blogList;

            // 判断搜索的博客是否有内容
            if(response.data.total <= 0) {
              that.isEnd = true;
              that.loading = false;
              this.blogData = []
              return;
            }

            //全部加载完毕
            if (blogData.length < that.pageSize) {
              that.isEnd = true;
            }

            blogData = that.searchBlogData.concat(blogData);
            that.searchBlogData = blogData;
            this.blogData = blogData;
          } else {
            that.isEnd = true;
          }
          that.loading = false;
        });
      } else if (this.tagUid != undefined) {
        var params = new URLSearchParams();

        params.append("tagUid", that.tagUid);
        params.append("currentPage", that.currentPage);
        params.append("pageSize", that.pageSize);

        searchBlogByTag(params).then(response => {
          if (response.code == this.$ECode.SUCCESS && response.data.records.length > 0) {
            that.isEnd = false;
            //获取总页数
            that.totalPages = response.data.total;

            var blogData = response.data.records;
            that.total = response.data.total;
            that.pageSize = response.data.size;
            that.currentPage = response.data.current;

            //全部加载完毕
            if (blogData.length < that.pageSize) {
              that.isEnd = true;
            }

            // 设置分类名
            for (var i = 0; i < blogData.length; i++) {
              blogData[i].blogSort = blogData[i].blogSort.sortName;
            }

            blogData = that.searchBlogData.concat(blogData);
            that.searchBlogData = blogData;
            this.blogData = blogData;
            that.loading = false;

          } else {

            that.isEnd = true;
            that.loading = false;
          }
        });
      } else if (this.sortUid != undefined) {
        var params = new URLSearchParams();

        params.append("blogSortUid", that.sortUid);
        params.append("currentPage", that.currentPage);
        params.append("pageSize", that.pageSize);

        searchBlogBySort(params).then(response => {
          if (response.code == this.$ECode.SUCCESS && response.data.records.length > 0) {
            that.isEnd = false;
            //获取总页数
            that.totalPages = response.data.total;

            var blogData = response.data.records;
            that.total = response.data.total;
            that.pageSize = response.data.size;
            that.currentPage = response.data.current;

            //全部加载完毕
            if (blogData.length < that.pageSize) {
              that.isEnd = true;
            }

            for (var i = 0; i < blogData.length; i++) {
              blogData[i].blogSort = blogData[i].blogSort.sortName;
            }

            blogData = that.searchBlogData.concat(blogData);
            that.searchBlogData = blogData;
            this.blogData = blogData;
            that.loading = false;
          } else {


            that.isEnd = true;
            that.loading = false;
          }
        });
      } else if (this.author != undefined) {
        var params = new URLSearchParams();
        params.append("author", that.author);
        params.append("currentPage", that.currentPage);
        params.append("pageSize", that.pageSize);
        searchBlogByAuthor(params).then(response => {
          if (response.code == this.$ECode.SUCCESS && response.data.records.length > 0) {
            that.loading = false;

            that.isEnd = false;

            //获取总页数
            that.totalPages = response.data.total;

            var blogData = response.data.records;
            that.total = response.data.total;
            that.pageSize = response.data.size;
            that.currentPage = response.data.current;

            //全部加载完毕
            if (blogData.length < that.pageSize) {
              that.isEnd = true;
            }

            for (var i = 0; i < blogData.length; i++) {
              if (blogData[i].blogSort == undefined) {
                blogData[i].blogSort = "未分类";
              } else {
                blogData[i].blogSort = blogData[i].blogSort.sortName;
              }
            }

            blogData = that.searchBlogData.concat(blogData);
            that.searchBlogData = blogData;
            this.blogData = blogData;
            that.loading = false;
          } else {

            that.isEnd = true;
            that.loading = false;
          }
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
  float: left;
  width: 100%;
  height: 80px;
  text-align: center;
}

.ng-scope {
  margin: 0 auto;
  width: 18%;
  height: 10%;
}

.loadContent {
  width: 120px;
  height: 30px;
  line-height: 30px;
  font-size: 16px;
  margin: 0 auto;
  color: aliceblue;
  cursor: pointer;
  background: rgba(0, 0, 0, 0.8);
}

@keyframes lds-facebook_1 {
  0% {
    top: 0px;
    height: 200px;
  }
  50% {
    top: 80px;
    height: 40px;
  }
  100% {
    top: 80px;
    height: 40px;
  }
}
@-webkit-keyframes lds-facebook_1 {
  0% {
    top: 0px;
    height: 200px;
  }
  50% {
    top: 80px;
    height: 40px;
  }
  100% {
    top: 80px;
    height: 40px;
  }
}
@keyframes lds-facebook_2 {
  0% {
    top: 20px;
    height: 160px;
  }
  50% {
    top: 80px;
    height: 40px;
  }
  100% {
    top: 80px;
    height: 40px;
  }
}
@-webkit-keyframes lds-facebook_2 {
  0% {
    top: 20px;
    height: 160px;
  }
  50% {
    top: 80px;
    height: 40px;
  }
  100% {
    top: 80px;
    height: 40px;
  }
}
@keyframes lds-facebook_3 {
  0% {
    top: 40px;
    height: 120px;
  }
  50% {
    top: 80px;
    height: 40px;
  }
  100% {
    top: 80px;
    height: 40px;
  }
}
@-webkit-keyframes lds-facebook_3 {
  0% {
    top: 40px;
    height: 120px;
  }
  50% {
    top: 80px;
    height: 40px;
  }
  100% {
    top: 80px;
    height: 40px;
  }
}
.lds-facebook {
  position: relative;
}
.lds-facebook div {
  position: absolute;
  width: 20px;
}
.lds-facebook div:nth-child(1) {
  left: 40px;
  background: #1d0e0b;
  -webkit-animation: lds-facebook_1 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
  animation: lds-facebook_1 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
  -webkit-animation-delay: -0.2s;
  animation-delay: -0.2s;
}
.lds-facebook div:nth-child(2) {
  left: 90px;
  background: #774023;
  -webkit-animation: lds-facebook_2 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
  animation: lds-facebook_2 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
  -webkit-animation-delay: -0.1s;
  animation-delay: -0.1s;
}
.lds-facebook div:nth-child(3) {
  left: 140px;
  background: #d88c51;
  -webkit-animation: lds-facebook_3 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
  animation: lds-facebook_3 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
}
.lds-facebook {
  width: 90px !important;
  height: 90px !important;
  -webkit-transform: translate(-45px, -45px) scale(0.45) translate(45px, 45px);
  transform: translate(-45px, -45px) scale(0.45) translate(45px, 45px);
}
</style>

