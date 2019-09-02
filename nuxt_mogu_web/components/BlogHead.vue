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
          <a href="/home">首页</a>
          <!-- <nuxt-link to="/course/home">首页</nuxt-link> -->
        </li>
        <li>
          <a href="/about">关于我</a>
        </li>
        <!-- <li><a href="javascript:void(0);" @click="goTo('/study')">学习教程</a></li> -->
        <li>
          <a href="/time">时间轴</a>
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
import { getWebConfig } from "~/api/index";

export default {
  name: "Head",
  head() {
    return {
      title: "蘑菇博客 - 专注于技术分享的博客平台",
      meta: [
        { charset: "utf-8" },
        {
          name: "description",
          content:
            "一个专注于技术分享的博客平台，大家以共同学习，乐于分享，拥抱开源的价值观进行学习交流"
        },
        {
          name: "keywords",
          content:
            "蘑菇博客,蘑菇社区,蘑菇技术社区,,蘑菇IT社区,IT社区,技术社区,Java技术分享,Spring教程,开发者社区"
        }
      ]
    };
  },
  created() {
    getWebConfig().then(response => {
      if (response.code == "success") {
        this.info = response.data;
      }
    });
  },
  data() {
    return {
      keyword: "",
      info: {},
      showSearch: false, // 控制搜索框的弹出
      showHead: false, //控制导航栏的弹出
      isVisible: true //控制web端导航的隐藏和现实
    };
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
            this.$router.push(`/home`);
          }
          break;
        case "/about":
          {
            this.$router.push(`/about`);
          }
          break;
        case "/study":
          {
            this.$router.push(`/share`);
          }
          break;
        case "/time":
          {
            this.$router.push(`/time`);
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

<style lang="postcss">
</style>
