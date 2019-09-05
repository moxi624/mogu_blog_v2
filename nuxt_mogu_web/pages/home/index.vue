<template>
  <div>
    <article>

      <!--banner begin-->
      <div class="picsbox">
        <div class="banner">
          <div class="carousel-wrap" id="carousel" @mouseenter="inDiv" @mouseleave="outDiv">
            <span v-show="isShow" class="left" @click="leftChange"><</span>
            <span v-show="isShow" class="right" @click="rightChange">></span>
            <transition-group tag="ul" class="slide-ul" name="list">
              <li
                v-for="(list,index) in slideList"
                :key="index"
                v-show="index===currentIndex"
                @mouseenter="stop"
                @mouseleave="go"
              >
                <a :href="BASE_BLOG_API + '/info?blogUid=' + list.uid">
                  <img
                    v-if="list.photoList"
                    :src="list.photoList[0]"
                    :alt="list.title"
                  >
                </a>
                <div class="carousel-title">
                  <span>{{list.title}}</span>
                </div>
              </li>
            </transition-group>
            <div class="carousel-items">
              <span
                v-for="(item,index) in slideList.length"
                :key="item.uid"
                :class="{'active':index===currentIndex}"
                @mouseover="change(index)"
              ></span>
            </div>
          </div>
        </div>
        <!--banner end-->

        <!-- 二级推荐 -->
        <div class="toppic">
          <li v-for="item in secondData" :key="item.uid">
            <a :href="BASE_BLOG_API + '/info?blogUid=' + item.uid">
              <i>
                <img :src="item.photoList[0]">
              </i>
              <h2>{{item.title}}</h2>
              <span>{{item.blogSort.sortName}}</span>
            </a>
          </li>
        </div>
      </div>
      <div class="blank"></div>

      <!--blogsbox begin-->
      <div class="blogsbox">
        <div
          v-for="item in newBlogData"
          :key="item.uid"
          class="blogs"
          data-scroll-reveal="enter bottom over 1s"
        >
          <h3 class="blogtitle">
            <a :href="BASE_BLOG_API + '/info?blogUid=' + item.uid">{{item.title}}</a>
          </h3>
          <span class="blogpic">
            <a :href="BASE_BLOG_API + '/info?blogUid=' + item.uid" title>
              <img v-if="item.photoList" :src="item.photoList[0]" alt>
            </a>
          </span>
          <p class="blogtext">{{item.summary}}</p>
          <div class="bloginfo">
            <ul>
              <li class="author">
                <a href="javascript:void(0);" @click="goToAuthor(item.author)">{{item.author}}</a>
              </li>
              <li class="lmname" v-if="item.blogSort">
                <a
                  href="javascript:void(0);"
                  @click="goToList(item.blogSort.uid)"
                >{{item.blogSort.sortName}}</a>
              </li>
              <li class="timer">{{item.createTime}}</li>
              <li class="view">
                <span>{{item.clickCount}}</span>
              </li>
              <li class="like">{{item.collectCount}}</li>
            </ul>
          </div>
        </div>

        <!--分页插件-->
        <div class="loadContent">
          <span>
            <div v-if="currentPage - 1 <= 0">上一页</div>
            <a v-if="currentPage - 1 > 0" :href="'/home?currentPage=' + (currentPage - 1)">上一页</a>
          </span>
          <input v-model="currentPage" @change="change">
          <span>
            <a :href="'/home?currentPage=' + currentPage">跳转</a>
          </span>
          <span>
            <div v-if="currentPage*pageSize >= total">下一页</div>
            <a v-if="currentPage*pageSize < total" :href="'/home?currentPage=' + (currentPage + 1)">下一页</a>
          </span>
        </div>
      </div>
      <!--blogsbox end-->

      <div class="sidebar">
        <!--三级推荐开始-->
        <div class="zhuanti" v-if="thirdData.length > 0">
          <h2 class="hometitle">特别推荐</h2>
          <ul>
            <li v-for="item in thirdData" :key="item.uid">
              <i>
                <img v-if="item.photoList" :src="item.photoList[0]">
              </i>
              <p>
                {{item.title}}
                <span>
                  <a :href="BASE_BLOG_API + '/info?blogUid=' + item.uid">阅读</a>
                </span>
              </p>
            </li>
          </ul>
        </div>
        <!--三级推荐结束-->

        <!--标签云开始-->
        <div class="cloud" v-if="hotTagData.length > 0">
          <h2 class="hometitle">标签云</h2>
          <ul>
            <a
              v-for="item in hotTagData"
              :key="item.uid"
              href="javascript:void(0);"
              @click="goToList(item.uid)"
            >{{item.content}}</a>
          </ul>
        </div>
        <!--标签云结束-->

        <!--四级推荐开始-->
        <div class="tuijian" v-if="fourthData.length > 0">
          <h2 class="hometitle">推荐文章</h2>
          <ul class="tjpic" v-if="fourthData[0]">
            <i>
              <a :href="BASE_BLOG_API + '/info?blogUid=' + fourthData[0].uid">
                <img
                  v-if="fourthData[0].photoList"
                  style="cursor:pointer"
                  :src="fourthData[0].photoList[0]"
                >
              </a>
            </i>
            <p>
              <a
                :href="BASE_BLOG_API + '/info?blogUid=' + fourthData[0].uid"
              >{{fourthData[0].title}}</a>
            </p>
          </ul>

          <ul class="sidenews">
            <li v-for="(item, index) in fourthData" v-if="index != 0" :key="item.uid">
              <i>
                <a :href="BASE_BLOG_API + '/info?blogUid=' + fourthData[0].uid">
                  <img style="cursor:pointer" v-if="item.photoList" :src="item.photoList[0]">
                </a>
              </i>
              <p>
                <a :href="BASE_BLOG_API + '/info?blogUid=' + item.uid">{{item.title}}</a>
              </p>
              <span>{{item.createTime}}</span>
            </li>
          </ul>
        </div>
        <!--四级推荐结束-->

        <!--热门博客开始-->
        <div class="tuijian" v-if="hotBlogData.length > 0">
          <h2 class="hometitle">点击排行</h2>
          <ul class="tjpic" v-if="hotBlogData[0]">
            <i>
              <a :href="BASE_BLOG_API + '/info?blogUid=' + hotBlogData[0].uid">
                <img
                  style="cursor:pointer"
                  v-if="hotBlogData[0].photoList"
                  :src="hotBlogData[0].photoList[0]"
                >
              </a>
            </i>
            <p>
              <a
                :href="BASE_BLOG_API + '/info?blogUid=' + hotBlogData[0].uid"
              >{{hotBlogData[0].title}}</a>
            </p>
          </ul>
          <ul class="sidenews">
            <li v-for="(item, index) in hotBlogData" v-if="index != 0" :key="item.uid">
              <i>
                <a :href="BASE_BLOG_API + '/info?blogUid=' + item.uid">
                <img
                  style="cursor:pointer"
                  v-if="item.photoList"
                  :src="item.photoList[0]"
                >
                </a>

              </i>
              <p>
                <a :href="BASE_BLOG_API + '/info?blogUid=' + item.uid">{{item.title}}</a>
              </p>
              <span>{{item.createTime}}</span>
            </li>
          </ul>
        </div>
        <!--热门博客结束-->

        <!--友情链接开始-->
        <div class="links" v-if="linkData.length > 0">
          <h2 class="hometitle">友情链接</h2>
          <ul>
            <li v-for="item in linkData" :key="item.uid">
              <a :href="item.url">{{item.title}}</a>              
            </li>
          </ul>
        </div>
        <!--友情链接结束-->

        <!--关注我们 开始-->
        <div class="guanzhu" id="follow-us" ref="follow">
          <h2 class="hometitle">关注我们 么么哒！</h2>
          <ul>
            <!-- <li class="sina"><a href="/" target="_blank"><span>新浪微博</span>蘑菇博客</a></li>         -->
            <li class="qq" v-if="contact.qqNumber">
              <a
                href="tencent://AddContact/?fromId=50&fromSubId=1&subcmd=all&uin=1595833114"
                target="_blank"
              >
                <span>QQ号</span>
                {{contact.qqNumber}}
              </a>
            </li>
            <li class="email" v-if="contact.email">
              <a :href="mailto" target="_blank">
                <span>邮箱帐号</span>
                {{contact.email}}
              </a>
            </li>
            <li class="wxgzh" v-if="contact.weChat">
              <a href="/" target="_blank">
                <span>微信号</span>
                {{contact.weChat}}
              </a>
            </li>
          </ul>
        </div>
        <!--关注我们 介绍-->

      </div>
    </article>
  </div>
</template>
<script>
//配置文件
import querystring from "querystring";

import {
  getBlogByLevel,
  getNewBlog,
  getHotBlog,
  getHotTag,
  getLink,
  recorderVisitPage
} from "~/api/index";

import { getContact } from "~/api/about";

export default {
  components: {},
  async asyncData({ store, route }) {
    let currentPage = route.query.currentPage;
    let pageSize = route.query.pageSize;

    if (!currentPage) {
      currentPage = 1;
    }
    if (!pageSize) {
      pageSize = 13;
    }

    var params = new URLSearchParams();
    params.append("currentPage", currentPage);
    params.append("pageSize", pageSize);
    let response = await getNewBlog(params);

    var firstParams = new URLSearchParams();
    firstParams.append("level", 1);
    let firstResponse = await getBlogByLevel(firstParams);

    var secondParams = new URLSearchParams();
    secondParams.append("level", 2);
    let secondResponse = await getBlogByLevel(secondParams);

    var thirdParams = new URLSearchParams();
    thirdParams.append("currentPage", 0);
    thirdParams.append("pageSize", 3);
    thirdParams.append("level", 3);
    let thirdResponse = await getBlogByLevel(thirdParams);

    var fourthParams = new URLSearchParams();
    fourthParams.append("currentPage", 0);
    fourthParams.append("pageSize", 5);
    fourthParams.append("level", 4);
    let fourthResponse = await getBlogByLevel(fourthParams);

    var hotTagParams = new URLSearchParams();
    hotTagParams.append("currentPage", currentPage);
    hotTagParams.append("pageSize", pageSize);
    let hotTagResponse = await getHotTag(hotTagParams);

    let hotResponse = await getHotBlog();

    let linkResponse = await getLink();

    let follwoResponse = await getContact();

    if (response.code == "success") {
      return {
        BASE_BLOG_API: process.env.BASE_BLOG_API,
        currentPage: currentPage,
        slideList: firstResponse.data.records,
        secondData: secondResponse.data.records,
        thirdData: thirdResponse.data.records,
        fourthData: fourthResponse.data.records,
        hotTagData: hotTagResponse.data.records,
        hotBlogData: hotResponse.data.records,
        linkData: linkResponse.data.records,
        newBlogData: response.data.records,
        contact: follwoResponse.data,
        mailto: "mailto:" + follwoResponse.data.email,

        total: response.data.total,
        pageSize: response.data.size,
        currentPage: response.data.current
      };
    } else {
      return {
        BASE_BLOG_API: process.env.BASE_BLOG_API,
        slideList: [],
        secondData: [],
        thirdData: [],
        fourthData: [],
        hotTagData: [],
        hotBlogData: [],
        linkData: [],
        newBlogData: [],
        contact: [],
        mailto: [],

        total: 0,
        currentPage: 1,
        pageSize: 10
      };
    }
  },
  data() {
    return {
      isShow: false, //控制左右滑动按钮是否显示
      currentIndex: 0,
      timer: "",

      keyword: "",
      currentPage: 1,
      pageSize: 15,
      total: 0, //总数量
      isEnd: false, //是否到底底部了
      loading: false //是否正在加载
    };
  },
  watch: {
    $route: function() {
      //路由变化会触发
    }
  },
  methods: {
    to: function() {
      // console.log(this.currentPage);
      // this.$router.push(`/home?currentPage=` + this.currentPage);
    },
    go() {
      this.timer = setInterval(() => {
        this.autoPlay();
      }, 4000);
    },
    stop() {
      clearInterval(this.timer);
      this.timer = null;
    },
    inDiv: function() {
      this.isShow = true;
    },
    outDiv: function() {
      this.isShow = false;
    },
    leftChange: function() {
      var currentIndex = this.currentIndex - 1;
      if (currentIndex < 0) {
        this.currentIndex = this.slideList.length - 1;
      } else {
        this.currentIndex = currentIndex;
      }

      console.log(this.currentIndex);
    },
    rightChange: function() {
      var currentIndex = this.currentIndex + 1;
      if (currentIndex >= this.slideList.length) {
        this.currentIndex = 0;
      } else {
        this.currentIndex = currentIndex;
      }
    },

    change(index) {
      this.currentIndex = index;
    },
    autoPlay() {
      this.currentIndex++;
      if (this.currentIndex > this.slideList.length - 1) {
        this.currentIndex = 0;
      }
    },

    //跳转到搜索详情页
    goToList(uid) {
      let routeData = this.$router.resolve({
        path: "/list",
        query: { sortUid: uid }
      });
      window.open(routeData.href, "_blank");
    },

    //跳转到搜索详情页
    goToAuthor(author) {
      let routeData = this.$router.resolve({
        path: "/list",
        query: { author: author }
      });
      window.open(routeData.href, "_blank");
    },

    //最新博客列表
    newBlogList() {
      var params = new URLSearchParams();
      params.append("currentPage", this.currentPage);
      params.append("pageSize", this.pageSize);
      getNewBlog(params).then(response => {
        if (response.code == "success") {
          this.newBlogData = response.data.records;
          this.total = response.data.total;
          this.pageSize = response.data.size;
          this.currentPage = response.data.current;
        }
      });
    },

    loadContent: function() {
      var that = this;
      // that.loading = false;
      that.currentPage = that.currentPage + 1;
      var params = new URLSearchParams();
      params.append("currentPage", that.currentPage);
      params.append("pageSize", that.pageSize);

      let query = Object.assign({}, this.$route.query);
      query.currentPage = that.currentPage;
      query.pageSize = that.pageSize;
      let querys = querystring.stringify(query);
      this.$router.push(`/course/home?` + querys);
    }
  },
  mounted() {}
};
</script>

<style>
.carousel-wrap {
  position: relative;
}

.left:hover {
  color: rgba(90, 88, 88, 0.8);
}
.right:hover {
  color: rgba(90, 88, 88, 0.9);
}

.left {
  font-size: 80px;
  color: rgba(223, 219, 219, 0.8);
  position: absolute;
  z-index: 99999;
  cursor: pointer;
  top: 28%;
}

.right {
  font-size: 80px;
  color: rgba(223, 219, 219, 0.8);
  position: absolute;
  z-index: 99999;
  cursor: pointer;
  top: 28%;
  right: 0%;
}

.carousel-wrap {
  height: 453px;
  width: 100%;
  overflow: hidden;
  background-color: #fff;
}

.carousel-title span {
  color: white;
  font-size: 22px;
  display: inline-block;
}

@media only screen and (max-width: 1100px) {
  .left {
    width: 80px;
    height: 80px;
    font-size: 80px;
    top: 27%;
  }

  .right {
    width: 80px;
    height: 80px;
    font-size: 80px;
    top: 27%;
    right: 0%;
  }
  .carousel-wrap {
    height: 380px;
    width: 100%;
    overflow: hidden;
    background-color: #fff;
  }

  .carousel-title span {
    color: white;
    font-size: 20px;
    display: inline-block;
  }
}

@media only screen and (max-width: 900px) {
  .carousel-wrap {
    height: 300px;
    width: 100%;
    overflow: hidden;
    background-color: #fff;
  }

  .carousel-title span {
    color: white;
    font-size: 18px;
    display: inline-block;
  }
}

@media only screen and (max-width: 700px) {
  .left {
    width: 60px;
    height: 60px;
    font-size: 60px;
    top: 27%;
  }

  .right {
    width: 60px;
    height: 60px;
    font-size: 60px;
    top: 27%;
    right: 0%;
  }
  .carousel-wrap {
    height: 250px;
    width: 100%;
    overflow: hidden;
    background-color: #fff;
  }

  .carousel-title span {
    color: white;
    font-size: 16px;
    display: inline-block;
  }
}

@media only screen and (max-width: 500px) {
  .left {
    width: 50px;
    height: 50px;
    font-size: 50px;
    top: 27%;
  }

  .right {
    width: 50px;
    height: 50px;
    font-size: 50px;
    top: 27%;
    right: 0%;
  }

  .carousel-wrap {
    height: 200px;
    width: 100%;
    overflow: hidden;
    background-color: #fff;
  }
}

.slide-ul {
  position: relative;
  width: 100%;
  height: 100%;
}

.slide-ul li {
  position: absolute;
  width: 100%;
  height: 100%;
}

img {
  width: 100%;
  height: auto;
}
.carousel-title {
  position: absolute;
  z-index: 10;
  bottom: 40px;
  height: 40px;
  width: 100%;
  line-height: 40px;
  text-align: center;
  background: rgba(0, 0, 0, 0.3);
}

.carousel-items {
  position: absolute;
  z-index: 10;
  bottom: 20px;
  width: 100%;
  margin: 0 auto;
  text-align: center;
  font-size: 0;
}

.carousel-items span {
  display: inline-block;
  height: 6px;
  width: 30px;
  margin: 0 3px;
  background-color: #b2b2b2;
  cursor: pointer;
}
.carousel-items .active {
  background-color: orange;
}

.list-enter-to {
  transition: all 1s ease;
  transform: translateX(0);
}

.list-leave-active {
  transition: all 1s ease;
  transform: translateX(-100%);
}

.list-enter {
  transform: translateX(100%);
}

.list-leave {
  transform: translateX(0);
}

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
  width: 400px;
  height: 30px;
  margin: 0 auto;
  margin-bottom: 10px;
  color: aliceblue;
}
.loadContent span {
  width: 60px;
  line-height: 30px;
  text-align: center;
  font-size: 16px;
  display: inline-block;
  margin-right: 10px;
  cursor: pointer;
  background: rgba(0, 0, 0, 0.8);
}

.loadContent span a{
  color:wheat;
}

.loadContent input {
  width: 60px;
  height: 22px;
  text-align: center;
  margin-right: 10px;
  line-height: 30px;
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