<template>
  <header :class="isVisible?'header-navigation slideDown':'header-navigation slideUp'" id="header">
    <nav>
      <div class="logo">
        <a href="javascript:void(0);" @click="goTo('/')" v-if="info.name">{{info.name}}</a>
      </div>
      <h2 id="mnavh" @click="openHead" :class="showHead?'open':''">
        <span class="navicon"></span>
      </h2>
      <ul id="starlist" :style="showHead?'display: block':''">
        <li>
          <a href="javascript:void(0);" @click="goTo('/')">网站首页</a>
        </li>
        <li>
          <a href="javascript:void(0);" @click="goTo('/about')">关于我</a>
        </li>
        <!-- <li><a href="javascript:void(0);" @click="goTo('/study')">学习教程</a></li> -->
        <li>
          <a href="javascript:void(0);" @click="goTo('/time')">时间轴</a>
        </li>
      </ul>

      <div class="searchbox">
        <div id="search_bar" :class="showSearch?'search_bar search_open':'search_bar'">
          <input
            class="input"
            placeholder="想搜点什么呢.."
            type="text"
            name="keyboard"
            v-model="keyword"
            v-on:keyup.enter="search"
          >
          <p class="search_ico" @click="clickSearchIco">
            <span></span>
          </p>
        </div>
      </div>
    </nav>
  </header>
</template>

<script>
import { BASE_BLOG_API } from "../../../config/dev.env";
import { getWebConfig } from "../../api/index";

export default {
  name: "Head",
  created() {
    var tempValue = decodeURI(this.getUrlVars()["keyword"]);
    if (
      tempValue == null ||
      tempValue == undefined ||
      tempValue == "undefined"
    ) {
    } else {
      this.keyword = tempValue;
    }

    getWebConfig().then(response => {
      if (response.code == "success") {
        this.info = response.data;
      }
    });
  },
  data() {
    return {
      BaseBlog: BASE_BLOG_API,
      keyword: "",
      info: {},
      showSearch: false, // 控制搜索框的弹出
      showHead: false, //控制导航栏的弹出
      isVisible: true //控制web端导航的隐藏和现实
    };
  },
  watch: {
    $route(to, from) {
      this.$router.go(0);
    }
  },
  mounted() {
    var that = this;
    var after = 0;
    window.addEventListener("scroll", function() {
      let scrollTop = document.documentElement.scrollTop; //当前的的位置
      if (scrollTop > after) {
        that.isVisible = false;
      } else {
        that.isVisible = true;
      }
      after = scrollTop;
    });
  },
  methods: {
    goTo: function(url) {
      switch (url) {
        case "/":
          {
            this.$router.push({ path: "/" });
          }
          break;
        case "/about":
          {
            this.$router.push({ path: "/about" });
          }
          break;
        case "/study":
          {
            this.$router.push({ path: "/share" });
          }
          break;
        case "/time":
          {
            this.$router.push({ path: "/time" });
          }
          break;
      }
    },
    search: function() {
      if (this.keyword == "") {
        alert("关键字不能为空");
        return;
      }
      this.$router.push({ path: "/list", query: { keyword: this.keyword } });
    },
    getUrlVars: function() {
      var vars = {};
      var parts = window.location.href.replace(
        /[?&]+([^=&]+)=([^&#]*)/gi,
        function(m, key, value) {
          vars[key] = value;
        }
      );
      return vars;
    },
    clickSearchIco: function() {
      this.showSearch = !this.showSearch;
    },
    openHead: function() {
      this.showHead = !this.showHead;
    }
  }
};
</script>

<style>
</style>
