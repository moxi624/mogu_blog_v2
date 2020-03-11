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

        <li>
          <router-link to="/messageBoard">
            <a href="javascript:void(0);" :class="[saveTitle == '/messageBoard' ? 'title' : '']">留言板</a>
          </router-link>
        </li>

      </ul>


      <div class="searchbox">
        <div id="search_bar" :class="(showSearch || keyword.length > 0)?'search_bar search_open':'search_bar'">
          <input
            ref="searchInput"
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
        <span class="el-dropdown-link" >
          <img v-if="!isLogin" src="../../static/images/defaultAvatar.png">
          <img v-if="isLogin&&userInfo.photoUrl!=undefined" :src="PICTURE_HOST + userInfo.photoUrl" onerror="onerror=null;src='https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'">
          <img v-if="isLogin&&userInfo.photoUrl==undefined"
               src="https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif">
        </span>

        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="login" v-show="!isLogin">登录</el-dropdown-item>
          <el-dropdown-item command="goUserInfo" v-show="isLogin">个人中心</el-dropdown-item>
          <el-dropdown-item command="logout" v-show="isLogin">退出登录</el-dropdown-item>
        </el-dropdown-menu>

      </el-dropdown>


    </nav>
  </header>
  <LoginBox v-if="showLogin" @closeLoginBox="closeLoginBox"></LoginBox>

  <el-drawer
    :show-close="true"
    :visible.sync="drawer"
    :with-header="false">

      <el-tabs type="border-card" tab-position="left" style="margin-top: 50px; height: 100%;">
      <el-tab-pane label="个人中心">
        <span slot="label"><i class="el-icon-star-on"></i> 关于我</span>
        <el-form style="margin-left: 20px;" label-position="left" :model="userInfo" label-width="100px" ref="changeAdminForm">
          <el-form-item label="用户头像" :label-width="labelWidth">

            <div class="imgBody" v-if="userInfo.photoUrl">
              <i class="el-icon-error inputClass" v-show="icon" @click="deletePhoto()" @mouseover="icon = true"></i>
              <img @mouseover="icon = true" @mouseout="icon = false" v-bind:src="userInfo.photoUrl" />
            </div>

            <div v-else class="uploadImgBody" @click="checkPhoto">
              <i class="el-icon-plus avatar-uploader-icon"></i>
            </div>
          </el-form-item>

          <el-form-item label="昵称" :label-width="labelWidth">
            <el-input v-model="userInfo.nickName" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item label="性别" :label-width="labelWidth">
            <el-radio v-for="gender in genderDictList" :key="gender.uid" v-model="userInfo.gender" :label="gender.dictValue" border size="medium">{{gender.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item label="生日" :label-width="labelWidth">
            <el-date-picker
              v-model="userInfo.birthday"
              type="date"
              placeholder="选择日期">
            </el-date-picker>
          </el-form-item>

          <el-form-item label="邮箱" :label-width="labelWidth">
            <el-input v-model="userInfo.email" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item label="QQ号" :label-width="labelWidth">
            <el-input v-model="userInfo.qqNumber" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item label="职业" :label-width="labelWidth">
            <el-input v-model="userInfo.occupation" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item label="简介" :label-width="labelWidth">
            <el-input
              type="textarea"
              :autosize="{ minRows: 5, maxRows: 10}"
              placeholder="请输入内容"
              style="width: 100%"
              v-model="userInfo.summary">
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm('editUser')">保 存</el-button>
          </el-form-item>

        </el-form>
      </el-tab-pane>
      <el-tab-pane label="我的评论">我的评论</el-tab-pane>
      <el-tab-pane label="我的回复">我的回复</el-tab-pane>
      <el-tab-pane label="我的点赞">我的点赞</el-tab-pane>
      <el-tab-pane label="我的收藏">我的收藏</el-tab-pane>
      <el-tab-pane label="修改密码">修改密码</el-tab-pane>
    </el-tabs>
  </el-drawer>

  <!--头像裁剪-->
  <avatar-cropper
    v-show="imagecropperShow"
    :key="imagecropperKey"
    :width="300"
    :height="300"
    :url="url"
    lang-type="zh"
    @close="close"
    @crop-upload-success="cropSuccess"
  />

  <div>
    <router-view/>
  </div>

  <footer>
    <p>
      <a href="http://localhost:9527/" target="_blank">&nbsp;&nbsp;</a>
      <a href="javasrcipt:void(0);" @click="goIndex()">Copyright 2019-2020&nbsp;{{info.name}}&nbsp;</a>
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
  import AvatarCropper from '@/components/AvatarCropper'
  import {getWebConfig} from "../api/index";
  import {delCookie, getCookie, setCookie} from "@/utils/cookieUtils";
  import {authVerify, editUser, deleteUserAccessToken} from "../api/user";
  import LoginBox from "../components/LoginBox";
  import {getListByDictType} from "@/api/sysDictData"
  // vuex中有mapState方法，相当于我们能够使用它的getset方法
  import {mapMutations} from 'vuex';

  export default {
    name: "index",
    components: {
      LoginBox,
      AvatarCropper
    },
    data() {
      return {
        genderDictList: [], //字典列表
        imagecropperShow: false,
        imagecropperKey: 0,
        url: process.env.PICTURE_API + "/file/cropperPicture",
        drawer: false,
        PICTURE_HOST: process.env.PICTURE_HOST,
        info: {},
        saveTitle: "",
        keyword: "",
        showSearch: false, // 控制搜索框的弹出
        showHead: false, //控制导航栏的弹出
        isCdTopVisible: false,
        isVisible: true, //控制web端导航的隐藏和显示
        isLogin: false,
        showLogin: false, //显示登录框
        userInfo: { // 用户信息
        },
        icon: false, //控制删除图标的显示
        labelWidth: "70px"
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
        this.getCurrentPageTitle()
      }
    },
    created() {
      // 字典查询
      this.getDictList();
      this.getToken()
      this.getKeyword()
      this.getCurrentPageTitle()
      this.getWebConfigInfo()

    },
    methods: {
      //拿到vuex中的写的方法
      ...mapMutations(['setUserInfo', 'setLoginState', 'setWebConfigData']),

      // 搜索
      search: function () {
        if (this.keyword == "" || this.keyword.trim() == "") {
          this.$notify.error({
            title: '错误',
            message: "关键字不能为空",
            type: 'success',
            offset: 100
          });
          return;
        }
        this.$router.push({path: "/list", query: {keyword: this.keyword}});
      },

      //弹出选择图片框
      checkPhoto() {
        this.imagecropperShow = true
      },

      cropSuccess(resData) {
        console.log("裁剪成功", resData)
        this.imagecropperShow = false
        this.imagecropperKey = this.imagecropperKey + 1
        this.userInfo.photoUrl = resData[0].url
        this.userInfo.avatar = resData[0].uid
      },
      deletePhoto: function() {
        this.userInfo.photoUrl = null;
        this.userInfo.avatar = "";
        this.icon = false;
      },
      close() {
        this.imagecropperShow = false
      },

      submitForm: function(type) {
        switch (type) {
          case "editUser": {
            console.log('提交用户信息', this.userInfo);
            editUser(this.userInfo).then(response => {
              console.log('返回的状态', response)
              if(response.code == "success") {
                this.$message({
                  type: "success",
                  message: "更新成功"
                })
              } else {
                this.$message({
                  type: "success",
                  message: response.data
                })
              }
            });
          }; break;
        }
      },

      /**
       * 字典查询
       */
      getDictList: function () {
        var params = {};
        params.dictType = 'sys_user_sex';
        getListByDictType(params).then(response => {
          console.log('得到的字典', response)
          if (response.code == "success") {
            this.genderDictList = response.data.list;
          }
        });
      },

      getToken: function() {
        let token = this.getUrlVars()["token"];
        // 判断url中是否含有token
        if (token != undefined) {
          // 设置token七天过期
          setCookie("token", token, 7)
        }
        // 从cookie中获取token
        token = getCookie("token")
        if (token != undefined) {
          authVerify(token).then(response => {
            if (response.code == "success") {
              console.log('得到的用户信息', response.data)
              this.isLogin = true;
              this.userInfo = response.data;
              this.setUserInfo(this.userInfo)
            } else {
              this.isLogin = false;
              delCookie("token");
            }
            this.setLoginState(this.isLogin);
          });
        } else {
          this.isLogin = false;
          this.setLoginState(this.isLogin);
        }
      },
      getKeyword: function() {
        var tempValue = decodeURI(this.getUrlVars()["keyword"]);
        if (
          tempValue == null ||
          tempValue == undefined ||
          tempValue == "undefined"
        ) {
        } else {
          this.keyword = tempValue;
        }
      },
      /**
       * 获取当前所在页面的标题
       * @returns {{}}
       */
      getCurrentPageTitle: function() {
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
      },
      /**
       * 获取网站配置
        */
      getWebConfigInfo: function() {
        let webConfigData = this.$store.state.app.webConfigData
        if(webConfigData.createTime) {
          this.contact = webConfigData;
          this.mailto = "mailto:" + this.contact.email;
        } else {
          getWebConfig().then(response => {
            if (response.code == "success") {
              this.info = response.data;
              // 存储在Vuex中
              this.setWebConfigData(response.data)
            }
          });
        }
      },
      /**
       * 截取URL中的参数
       * @returns {{}}
       */
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
        if(this.keyword != "") {
          this.search();
        }
        this.showSearch = !this.showSearch;
        //获取焦点
        this.$refs.searchInput.focus();
      },
      openHead: function () {
        this.showHead = !this.showHead;
      },
      returnTop: function () {
        window.scrollTo(0, 0);
      },
      userLogin: function () {
        this.showLogin = true;
      },
      userLogout: function () {
        deleteUserAccessToken(getCookie("token"));
        delCookie("token");
        let url = window.parent.location.href;
        let haveToken = url.indexOf("?token")
        if (haveToken != -1) {
          let list = url.split("?token");
          this.isLogin = false;
          window.location.href = list[0]
          let userInfo = {};
          this.setCommentList(userInfo)
        } else {
          window.location.reload()
        }

      },

      handleCommand(command) {
        switch (command) {
          case "logout" : {
            this.userLogout();
          };break;
          case "login" : {
            this.userLogin();
          };break;
          case "goUserInfo" : {
            this.drawer = true;
          };break;
        }
      },
      closeLoginBox: function () {
        this.showLogin = false;
      }

    }
  };
</script>

<style scoped>

  #starlist li .title {
    color: #00a7eb;
  }


  .userInfoAvatar {

    width: 35px;
    height: 35px;
    position: absolute;
    right: -77px;
    top: 15px;
  }

  .userInfoAvatar img {
    width: 35px;
    height: 35px;
    border-radius: 50%;
  }

  @media only screen and (max-width: 600px) {
    .userInfoAvatar {
      width: 35px;
      height: 35px;
      position: absolute;
      right: 0px;
      top: 12px;
    }

    .searchbox {
      position: absolute;
      right: 40px;
      top: 0
    }
  }

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    margin: 0, 0, 0, 10px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409eff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    line-height: 100px;
    text-align: center;
  }
  .imgBody {
    width: 100px;
    height: 100px;
    border: solid 2px #ffffff;
    float: left;
    position: relative;
  }
  .imgBody img {
    width: 100px;
    height: 100px;
  }
  .uploadImgBody {
    margin-left: 5px;
    width: 100px;
    height: 100px;
    border: dashed 1px #c0c0c0;
    float: left;
    position: relative;
  }
  .uploadImgBody :hover {
    border: dashed 1px #00ccff;
  }
  .inputClass {
    position: absolute;
  }

</style>
