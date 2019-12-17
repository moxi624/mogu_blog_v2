<template>
  <div>
    <div class="pagebg ab"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>你，我生命中一个重要的过客，我们之所以是过客，因为你未曾会为我停留。</span>
        <a href="/" class="n1">网站首页</a>
        <a href="/" class="n2">留言</a>
      </h1>
      <div class="news_infos">
        <ul>
          <ChangYan :sid="sid"></ChangYan>
        </ul>
      </div>
      <div class="sidebar">
        <div class="about">
          <p class="avatar" v-if="info.photoList">
            <img :src="PICTURE_HOST + info.photoList[0]" alt>
          </p>
          <p class="abname">{{info.nickName}}</p>
          <p class="abposition">{{info.occupation}}</p>
          <p class="abtext">{{info.summary}}</p>
        </div>
        <!--
    关注我们
        -->
        <follow-us></follow-us>
      </div>
    </div>
  </div>
</template>

<script>
import FollowUs from "../components/FollowUs";
import ChangYan from "../components/ChangYan";
import { recorderVisitPage } from "../api/index";
import { getMe } from "../api/about";

export default {
  name: "about",
  data() {
    return {
      PICTURE_HOST: process.env.PICTURE_HOST,
      info: {},
      sid: "test",
      isRouterAlive: false
    };
  },
  components: {
    //注册组件
    FollowUs,
    ChangYan,

  },

  created() {
    var that = this;
    getMe().then(response => {
      if (response.code == "success") {
        this.info = response.data;
      }
    });

    var params = new URLSearchParams();
    params.append("pageName", "ABOUT");
    recorderVisitPage(params).then(response => {});

    var reloaded = window.localStorage.getItem("reloaded") || "0";
    if (reloaded % 2 == 0) {
      window.location.reload();
      window.localStorage.setItem("reloaded", +1);
    }
  },
  methods: {}
};
</script>

<style>
</style>
