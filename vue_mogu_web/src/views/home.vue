<template>
  <html>
  <body>
  <head>
    <meta charset="utf-8">
    <title>{{info.title}}</title>
    <meta name="keywords" :content="info.keyword">
    <meta name="description" :content="info.summary">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  </head>

  <header
    :class="isVisible?'header-navigation slideDown':'header-navigation slideUp'"
    id="header"
  >
    <nav>
      <div class="logo">
        <router-link to="/">
          <a href="javascript:void(0);" v-if="info.name">{{info.name}}</a>
        </router-link>
      </div>

      <h2 id="mnavh" @click="openHead" :class="showHead?'open':''">
        <span class="navicon"></span>
      </h2>

      <ul id="starlist" :style="showHead?'display: block':''">
        <li>
          <router-link to="/">
            <a href="javascript:void(0);" :class="[saveTitle == '/' ? 'title' : '']">首页</a>
          </router-link>
        </li>

        <li>
          <router-link to="/about">
            <a href="javascript:void(0);" :class="[saveTitle == '/about' ? 'title' : '']">关于我</a>
          </router-link>
        </li>

        <li>
          <router-link to="/sort">
            <a href="javascript:void(0);" :class="[saveTitle == '/sort' ? 'title' : '']">归档</a>
          </router-link>
        </li>

        <li>
          <router-link to="/classify">
            <a href="javascript:void(0);" :class="[saveTitle == '/classify' ? 'title' : '']">分类</a>
          </router-link>
        </li>

        <!-- <li>
          <router-link to="/share">
            <a href="javascript:void(0);" :class="[saveTitle == '/share' ? 'title' : '']">学习教程</a>
          </router-link>
        </li> -->

        <li>
          <router-link to="/time">
            <a href="javascript:void(0);" :class="[saveTitle == '/time' ? 'title' : '']">时间轴</a>
          </router-link>
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

      <el-dropdown @command="handleCommand" class="userInfoAvatar">
        <span class="el-dropdown-link">
          <img  v-if="!isLogin" src="../../static/images/defaultAvatar.png">
          <img  v-if="isLogin" :src="userInfo.avatar">
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-if="!isLogin" command="goLogin">登录</el-dropdown-item>
          <el-dropdown-item v-if="isLogin" command="goUserInfo">{{userInfo.nickName}}</el-dropdown-item>
          <el-dropdown-item v-if="isLogin" command="changePwd">密码修改</el-dropdown-item>
          <el-dropdown-item v-if="isLogin" command="logout">登出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </nav>
  </header>

  <div>
    <router-view/>
  </div>

  <footer>
    <p>
      <a href="http://localhost:9527/" target="_blank">&nbsp;&nbsp;</a>
      <a href="javasrcipt:void(0);" @click="goIndex()">Copyright 2019&nbsp;{{info.name}}&nbsp;</a>
      <a href="http://www.beian.miit.gov.cn">{{info.recordNum}}</a>
    </p>
  </footer>

  <div>
    <a
      href="javascript:void(0);"
      @click="returnTop"
      :class="isCdTopVisible?'cd-top cd-is-visible':'cd-top'"
    >Top</a>
  </div>
  </body>
  </html>
</template>

<script>
  import {getWebConfig} from "../api/index";
  import {delCookie, getCookie, setCookie} from "@/utils/cookieUtils";
  import {authVerify, deleteUserAccessToken} from "../api/user";

  export default {
    name: "index",
    components: {},
    data() {
      return {

        info: {},
        saveTitle: "",
        keyword: "",
        showSearch: false, // 控制搜索框的弹出
        showHead: false, //控制导航栏的弹出
        isCdTopVisible: false,
        isVisible: true, //控制web端导航的隐藏和显示
        isLogin: false,
        userInfo: {

        }, // 用户信息
      };
    },
    mounted() {
      var that = this;
      var offset = 300;
      var after = 0;
      window.addEventListener("scroll", function () {
        let scrollTop = document.documentElement.scrollTop; //当前的的位置
        let scrollHeight = document.documentElement.scrollHeight; //最高的位置

        if (scrollTop > offset) {
          that.isCdTopVisible = true;
        } else {
          that.isCdTopVisible = false;
        }

        if (scrollTop > after) {
          that.isVisible = false;
        } else {
          that.isVisible = true;
        }
        after = scrollTop;
      });
    },
    watch: {
      $route(to, from) {
        // console.log("路由变化", to, from);
        if (to.path == "/list" || to.path == "/about") {
          this.$router.go(0);
        }

        // 获取当前所在页面kbs
        var test = window.location.href;
        var start = 0;
        var end = test.length;
        for (var i = 0; i < test.length; i++) {
          if (test[i] == "#") {
            start = i;
          }
          if (test[i] == "?" && i > start) {
            end = i;
          }
        }
        var result = test.substring(start + 1, end);
        this.saveTitle = result;
      }
    },
    created() {

      let token = this.getUrlVars()["token"];
      // 判断url中是否含有token
      if (token != undefined) {
        setCookie("token", token, 1)
      }

      // 从cookie中获取token
      token = getCookie("token")
      console.log("token", token);
      if (token != undefined) {
        authVerify(token).then(response => {
          if (response.code == "success") {
            console.log("userInfo", response.data)
            this.isLogin = true;
            this.userInfo = response.data;
          } else {
            this.isLogin = false;
          }
        });
      } else {
        this.isLogin = false;
      }
      var tempValue = decodeURI(this.getUrlVars()["keyword"]);
      if (
        tempValue == null ||
        tempValue == undefined ||
        tempValue == "undefined"
      ) {
      } else {
        this.keyword = tempValue;
      }

      // 获取当前所在页面
      var test = window.location.href;
      var start = 0;
      var end = test.length;
      for (var i = 0; i < test.length; i++) {
        if (test[i] == "#") {
          start = i;
        }
        if (test[i] == "?" && i > start) {
          end = i;
        }
      }
      var result = test.substring(start + 1, end);
      this.saveTitle = result;

      getWebConfig().then(response => {
        if (response.code == "success") {
          this.info = response.data;
        }
      });
    },
    methods: {
      search: function () {
        if (this.keyword == "") {
          alert("关键字不能为空");
          return;
        }
        this.$router.push({path: "/list", query: {keyword: this.keyword}});
      },
      getUrlVars: function () {
        var vars = {};
        var parts = window.location.href.replace(
          /[?&]+([^=&]+)=([^&#]*)/gi,
          function (m, key, value) {
            vars[key] = value;
          }
        );
        return vars;
      },
      clickSearchIco: function () {
        this.showSearch = !this.showSearch;
      },
      openHead: function () {
        this.showHead = !this.showHead;
      },
      returnTop: function () {
        window.scrollTo(0, 0);
      },
      userlogin: function () {
        this.$router.push({path: '/login'})
      },
      userlogout: function () {
        deleteUserAccessToken(getCookie("token"));
        delCookie("token");
        let url = window.parent.location.href;
        let haveToken = url.indexOf("?token")
        console.log("我来了", haveToken);
        if(haveToken != -1) {
          let list = url.split("?token");
          console.log("我来了2", list);
          this.isLogin = false;
          window.location.href = list[0]

        } else {
          console.log("我来了1");
          window.location.reload()
        }

      },

      handleCommand(command) {
        switch (command) {
          case "goLogin" : {
            this.userlogin();
          }; break;
          case "logout" : {
            this.userlogout();
          }; break;
          case "goUserInfo" : {
            this.$message('click on item ' + command);
          }; break;
          case "changePwd" : {
            this.$message('click on item ' + command);
          }; break;
        }
      }

    }
  };
</script>

<style>
  #starlist li .title {
    color: #00a7eb;
  }

  .userInfoAvatar {
    width: 35px;
    height: 35px;
    /*float: right;*/
    position: absolute;
    right: -77px;
    top: 15px;
  }

  .userInfoAvatar img {
    border-radius:50%;
  }
</style>
