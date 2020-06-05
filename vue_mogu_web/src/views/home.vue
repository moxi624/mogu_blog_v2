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

        <li>
          <router-link to="/tag">
            <a href="javascript:void(0);" :class="[saveTitle == '/tag' ? 'title' : '']">标签</a>
          </router-link>
        </li>

<!--        <li>-->
<!--          <router-link to="/share">-->
<!--            <a href="javascript:void(0);" :class="[saveTitle == '/share' ? 'title' : '']">学习教程</a>-->
<!--          </router-link>-->
<!--        </li>-->

<!--        <li>-->
<!--          <router-link to="/time">-->
<!--            <a href="javascript:void(0);" :class="[saveTitle == '/time' ? 'title' : '']">时间轴</a>-->
<!--          </router-link>-->
<!--        </li>-->

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

      <el-tabs type="border-card" tab-position="left" v-model="activeName" style="margin-top: 50px; height: 100%;"  @tab-click="handleClick">
      <el-tab-pane label="个人中心" name="0">
        <span slot="label"><i class="el-icon-user-solid"></i> 个人中心</span>
        <el-form label-position="left" :model="userInfo" label-width="100px" :rules="rules" ref="userInfo">
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

          <el-form-item label="评论邮件通知" :label-width="labelWidth">
            <el-radio v-for="item in yesNoDictList" :key="item.uid" v-model="userInfo.startEmailNotification" :label="parseInt(item.dictValue)" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item label="邮箱" :label-width="labelWidth" prop="email">
            <el-input v-model="userInfo.email" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item label="QQ号" :label-width="labelWidth" prop="qqNumber">
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

      <el-tab-pane label="我的评论" name="1">
        <span slot="label"><i class="el-icon-message-solid"></i> 我的评论</span>
        <div style="width: 100%; height: 840px;overflow:auto;">
          <el-timeline>
            <el-timeline-item v-for="comment in commentList" :key="comment.uid" :timestamp="timeAgo(comment.createTime)" placement="top">
              <el-card>
                <div class="commentList">
                <span class="left p1">
                  <img v-if="comment.user" :src="comment.user.photoUrl ? PICTURE_HOST + comment.user.photoUrl:'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'" onerror="onerror=null;src='https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'" />
                  <img v-else src="https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif" />
                </span>

                  <span class="right p1">
                  <div class="rightTop">
                    <el-link class="userName" :underline="false">{{comment.user.nickName}}</el-link>
                    <el-tag style="cursor: pointer;"  @click.native="goSource(comment)">{{comment.sourceName}}</el-tag>
                  </div>

                  <div class="rightCenter" v-html="$xss(comment.content, options)"></div>
                </span>
                </div>
              </el-card>
            </el-timeline-item>

            <el-timeline-item v-if="commentList.length == 0" placement="top">
              <el-card>
                <span style="font-size: 16px">空空如也~</span>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-tab-pane>

      <el-tab-pane label="我的回复" name="2">
        <span slot="label"><i class="el-icon-s-promotion"></i> 我的回复</span>
        <div style="width: 100%; height: 840px;overflow:auto">
          <el-timeline>
            <el-timeline-item v-for="reply in replyList" :key="reply.uid" :timestamp="timeAgo(reply.createTime)" placement="top">
              <el-card>
                <div class="commentList">
                  <span class="left p1">
                    <img v-if="reply.user" :src="reply.user.photoUrl ? PICTURE_HOST + reply.user.photoUrl:'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'" onerror="onerror=null;src='https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'" />
                    <img v-else src="https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif" />
                  </span>

                  <span class="right p1">

                      <div class="rightTop">
                        <el-link class="userName" :underline="false">{{reply.user.nickName}}</el-link>
                        <el-tag style="cursor: pointer;"  @click.native="goSource(reply)">{{reply.sourceName}}</el-tag>
                      </div>

                      <div class="rightCenter" v-html="$xss(reply.content, options)">
                      </div>
                  </span>
                </div>
              </el-card>
            </el-timeline-item>

            <el-timeline-item v-if="replyList.length == 0" placement="top">
              <el-card>
                <span style="font-size: 16px">空空如也~</span>
              </el-card>
            </el-timeline-item>

          </el-timeline>
        </div>
      </el-tab-pane>
      <el-tab-pane label="我的点赞" name="3">
        <span slot="label"><i class="el-icon-star-on"></i> 我的点赞</span>
        <div style="width: 100%; height: 840px;overflow:auto">
          <el-timeline>
            <el-timeline-item v-for="praise in praiseList" :key="praise.uid" :timestamp="timeAgo(praise.createTime)" placement="top">
              <el-card>
                <span>点赞</span><el-tag type="warning" style="cursor: pointer" v-if="praise.blog" @click.native="goToInfo(praise.blog.uid)">{{praise.blog.title}}</el-tag>
              </el-card>
            </el-timeline-item>

            <el-timeline-item v-if="praiseList.length == 0" placement="top">
              <el-card>
                <span style="font-size: 16px">空空如也~</span>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-tab-pane>
      <el-tab-pane label="我的反馈" name="4">
        <span slot="label"><i class="el-icon-phone"></i> 我的反馈</span>

        <el-collapse v-model="activeNames">
          <el-collapse-item title="反馈须知" name="1">
            <div>如果您对本站有什么想法，可以在这里进行反馈</div>
            <div>或者加入我们的QQ群进行交流</div>
          </el-collapse-item>
        </el-collapse>

        <el-divider></el-divider>

        <div style="width: 100%; height: 450px;overflow:auto">
          <el-timeline>
            <el-timeline-item v-for="feedbackItem in feedbackList" :key="feedbackItem.uid" :timestamp="timeAgo(feedbackItem.createTime)" placement="top">
              <el-card class="feedbackCard">
                <div class="item">
                  <span class="title">
                    标题:
                  </span>
                  <span class="content">
                    {{feedbackItem.title}}
                  </span>
                </div>

                <div class="item">
                  <span class="title">
                    内容:
                  </span>
                  <span class="content">
                    {{feedbackItem.content}}
                  </span>
                </div>

                <div class="item">
                  <span class="title">
                    反馈状态:
                  </span>
                  <span class="content">
                    <el-tag v-for="item in feedbackDictList" :key="item.uid" :type="item.listClass" v-if="feedbackItem.feedbackStatus == item.dictValue">{{item.dictLabel}}</el-tag>
                  </span>
                </div>

                <div class="item">
                  <span class="title">
                    回复:
                  </span>
                  <span class="content">
                    {{feedbackItem.reply}}
                  </span>
                </div>
              </el-card>
            </el-timeline-item>

            <el-timeline-item v-if="feedbackList.length == 0" placement="top">
              <el-card>
                <span style="font-size: 16px">空空如也~</span>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>

        <el-divider></el-divider>

        <el-form label-position="left" :model="userInfo" label-width="100px">
          <el-form-item label="标题" :label-width="labelWidth">
            <el-input v-model="feedback.title" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item label="内容" :label-width="labelWidth">
            <el-input
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4}"
              placeholder="请输入反馈内容"
              v-model="feedback.content">
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm('feedback')">提 交</el-button>
          </el-form-item>
        </el-form>

      </el-tab-pane>
      <el-tab-pane label="申请友链" name="5">
        <span slot="label"><i class="el-icon-share"></i> 申请友链</span>

        <el-form label-position="left" :model="blogLink" label-width="100px" ref="blogLink" :rules="linkRules">
          <el-collapse v-model="activeNames">
            <el-collapse-item title="申请须知" name="1">
              <div>请确定贵站可以稳定运营</div>
              <div>原创博客优先，技术类博客优先</div>
              <div>申请前请先添加下方蘑菇博客友链</div>
              <div>欢迎各位小伙伴一起互换友链~</div>
            </el-collapse-item>
            <el-collapse-item title="蘑菇博客" name="2">
              <div>网站名称：蘑菇博客</div>
              <div>网站LOGO：http://image.moguit.cn/favicon.png</div>
              <div>网站简介：蘑菇博客 - 专注于技术分享的博客平台</div>
              <div>网站地址：http://www.moguit.cn</div>
            </el-collapse-item>
          </el-collapse>

          <el-divider></el-divider>

          <el-form-item label="网站名称" :label-width="labelWidth" prop="title">
            <el-input v-model="blogLink.title" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item label="网站简介" :label-width="labelWidth" prop="summary">
            <el-input v-model="blogLink.summary" style="width: 100%"></el-input>
          </el-form-item>


          <el-form-item label="网站地址" :label-width="labelWidth" prop="url">
            <el-input v-model="blogLink.url" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm('replyBlogLink')">申 请</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="修改密码" name="6">
        <span slot="label"><i class="el-icon-s-tools"></i> 修改密码</span>
        <el-collapse v-model="activeNames">
          <el-collapse-item title="修改密码须知" name="1">
            <div>此修改密码功能仅适用于账号和密码登录</div>
            <div>对于第三方登录的账号，无法进行密码修改</div>
          </el-collapse-item>
        </el-collapse>
        <el-form ref="userInfoForm" label-position="left" :model="userInfo" label-width="100px" :rules="userInfoRules">
          <el-form-item label="旧密码" :label-width="labelWidth" prop="oldPwd">
            <el-input type="password" v-model="userInfo.oldPwd" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item label="新密码" :label-width="labelWidth" prop="newPwd">
            <el-input type="password" v-model="userInfo.newPwd" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item label="重复密码" :label-width="labelWidth" prop="newPwd2">
            <el-input type="password" v-model="userInfo.newPwd2" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm('changePwd')">提 交</el-button>
          </el-form-item>
        </el-form>

      </el-tab-pane>
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
  import {authVerify, editUser, updateUserPwd, replyBlogLink, deleteUserAccessToken, getFeedbackList, addFeedback} from "../api/user";
  import {getCommentListByUser, getPraiseListByUser} from "../api/comment";
  import LoginBox from "../components/LoginBox";
  import {getListByDictTypeList} from "@/api/sysDictData"
  // vuex中有mapState方法，相当于我们能够使用它的getset方法
  import {mapMutations} from 'vuex';
  import {timeAgo} from "../utils/webUtils";

  export default {
    name: "index",
    components: {
      LoginBox,
      AvatarCropper
    },
    data() {
      return {
        // xss白名单配置
        options : {
          whiteList: {
            a: ['href', 'title', 'target'],
            span: ['class']
          }
        },
        activeNames: ['1', '2'], //激活的折叠面板
        activeName: "0", // 激活的标签
        yesNoDictList: [], // 是否 字典列表
        genderDictList: [], //性别 字典列表
        feedbackDictList: [], // 反馈 字典列表
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
        feedback: {}, // 反馈提交
        blogLink: {}, // 友链申请
        icon: false, //控制删除图标的显示
        labelWidth: "100px",
        commentList: [], //我的评论
        replyList: [], // 我的回复
        praiseList: [], // 我的点赞
        feedbackList: [], // 我的反馈
        rules: {
          qqNumber: [
            {pattern:  /[1-9]([0-9]{5,11})/, message: '请输入正确的QQ号码'},
          ],
          email: [
            {pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/, message: '请输入正确的邮箱'},
          ]
        },
        linkRules: {
          title: [
            {required: true, message: '网站名称不能为空', trigger: 'blur'},
            {min: 1, max: 10, message: '长度在1到10个字符'},
          ],
          summary: [
            {required: true, message: '简介不能为空', trigger: 'blur'},
            {min: 1, max: 50, message: '长度在1到50个字符'},
          ],
          url: [
            {required: true, message: '网站地址不能为空', trigger: 'blur'},
            {pattern:  /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/, message: '请输入有效的URL'},
          ]
        },
        userInfoRules: {
          oldPwd: [
            {required: true, message: '旧密码不能为空', trigger: 'blur'},
            {min: 5, max: 20, message: '密码长度在5到20个字符'},
          ],
          newPwd: [
            {required: true, message: '新密码不能为空', trigger: 'blur'},
            {min: 5, max: 20, message: '密码长度在5到20个字符'},
          ],
          newPwd2: [
            {required: true, message: '新密码不能为空', trigger: 'blur'},
            {min: 5, max: 20, message: '密码长度在5到20个字符'},
          ]
        }
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

      //跳转到文章详情
      goToInfo(uid) {

        let routeData = this.$router.resolve({
          path: "/info",
          query: {blogUid: uid}
        });
        window.open(routeData.href, '_blank');
      },

      // 跳转到资源详情
      goSource: function(comment) {
        let source = comment.source
        switch(source) {
          case "MESSAGE_BOARD": {
            let routeData = this.$router.resolve({
              path: "/messageBoard"
            });
            window.open(routeData.href, '_blank');
          };break;
          case "BLOG_INFO": {
            let routeData = this.$router.resolve({
              path: "/info",
              query: {blogUid: comment.blogUid}
            });
            window.open(routeData.href, '_blank');
          };break;
          case "ABOUT": {
            let routeData = this.$router.resolve({
              path: "/about"
            });
            window.open(routeData.href, '_blank');
          };break;
        }
      },
      // 获取评论列表
      getCommentList: function() {
        let params = {}
        params.pageSize = 10;
        params.currentPage = 1;
        getCommentListByUser(params).then(response => {
          if(response.code == "success") {
            this.commentList = response.data.commentList
            this.replyList = response.data.replyList
          }
        })
      },

      // 获取评论列表
      getFeedback: function() {
        let params = {}
        getFeedbackList(params).then(response => {
          if(response.code == "success") {
            this.feedbackList = response.data.records;
          }
        })
      },

      // 获取点赞列表
      getPraiseList: function() {
        let params = {}
        params.pageSize = 10;
        params.currentPage = 1;
        getPraiseListByUser(params).then(response => {
          if(response.code == "success") {
            this.praiseList = response.data.records;
          }
        })
      },
      // 标签选择
      handleClick(tab, event) {
        switch(tab.index) {
          case "0": {
            console.log("点击个人中心")
          }; break;
          case "1": {
            console.log("点击我的评论")
          }; break;
          case "2": {
            console.log("点击我的回复")
          }; break;
          case "3": {
            console.log("点击我的点赞")
          }; break;
          case "4": {
            console.log("点击我的反馈")
          }; break;
          case "5": {
            console.log("点击申请友链")
          }; break;
          case "6": {
            console.log("点击修改密码")
          }; break;
        }
      },

      //弹出选择图片框
      checkPhoto() {
        this.imagecropperShow = true
      },

      /**
       * dateTimeStamp是一个时间毫秒，注意时间戳是秒的形式，在这个毫秒的基础上除以1000，就是十位数的时间戳。13位数的都是时间毫秒。
       * @param dateTimeStamp
       * @returns {string}
       */
      timeAgo(dateTimeStamp) {
        return timeAgo(dateTimeStamp)
      },

      cropSuccess(resData) {
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
            this.$refs.userInfo.validate((valid) => {
              if(!valid) {
                console.log("校验失败")
              } else {
                editUser(this.userInfo).then(response => {
                  if(response.code == "success") {
                    this.$message({
                      type: "success",
                      message: response.data
                    })
                  } else {
                    this.$message({
                      type: "error",
                      message: response.data
                    })
                  }
                });
              }
            })
          }; break;

          case "replyBlogLink": {
            this.$refs.blogLink.validate((valid) => {
              if(!valid) {
                console.log("校验失败")
              } else {
                replyBlogLink(this.blogLink).then(response => {
                  if(response.code == "success") {
                    this.$message({
                      type: "success",
                      message: response.data
                    })
                  } else {
                    this.$message({
                      type: "error",
                      message: response.data
                    })
                  }
                });
              }
            })

          }; break;

          case "feedback": {
            var feedback = this.feedback

            if(feedback.title == undefined || feedback.title == "" || feedback.content == undefined || feedback.content == "") {
              this.$message({
                type: "error",
                message: "必填项不能为空"
              })
              return;
            }
            addFeedback(this.feedback).then(response => {
              if(response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                })
                this.feedback = {}
                this.getFeedback()
              } else {
                this.$message({
                  type: "error",
                  message: response.data
                })
              }
            });
          }; break;

          case "changePwd": {
            let newPwd = this.userInfo.newPwd
            let newPwd2 = this.userInfo.newPwd2
            let oldPwd = this.userInfo.oldPwd
            if(newPwd != newPwd2) {
              this.$message({
                type: "error",
                message: "两次密码不一致"
              })
              return
            }
            if(newPwd == oldPwd) {
              this.$message({
                type: "error",
                message: "新旧密码相同"
              })
              return
            }
            let params = new URLSearchParams()
            params.append("oldPwd", oldPwd)
            params.append("newPwd", newPwd)
            updateUserPwd(params).then(response => {
              if(response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                })
              } else {
                this.$message({
                  type: "error",
                  message: response.data
                })
              }
              // 重置表单
              this.$refs.userInfoForm.resetFields()
            })
          };break;
        }
      },

      /**
       * 字典查询
       */
      getDictList: function () {
        var dictTypeList =  ['sys_yes_no', 'sys_user_sex', 'sys_feedback_status']

        getListByDictTypeList(dictTypeList).then(response => {
          if (response.code == "success") {
            var dictMap = response.data;
            this.genderDictList = dictMap.sys_user_sex.list
            this.yesNoDictList = dictMap.sys_yes_no.list
            this.feedbackDictList = dictMap.sys_feedback_status.list
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
          this.setUserInfo(userInfo)
        } else {
          window.location.reload()
        }

      },

      // 点击头像触发的动作
      handleCommand(command) {
        switch (command) {
          case "logout" : {
            this.userLogout();
          };break;
          case "login" : {
            this.userLogin();
          };break;
          case "goUserInfo" : {
            // 打开抽屉
            this.drawer = true;
            // 获取评论列表
            this.getCommentList();

            // 获取点赞列表
            this.getPraiseList()

            // 获取反馈列表
            this.getFeedback()

          };break;
        }
      },
      closeLoginBox: function () {
        this.showLogin = false;
      }

    }
  };
</script>

<style>
  @import "../assets/css/emoji.css";
  .emoji-panel-btn:hover {
    cursor: pointer;
    opacity: 0.8;
  }
  .emoji-item-common {
    background: url("../assets/img/emoji_sprite.png");
    display: inline-block;
  }
  .emoji-item-common:hover {
    cursor: pointer;
  }
  .emoji-size-small {
    // 表情大小
    zoom: 0.3;
  }
</style>

<style scoped>
  .el-tag {
    height: 25px;
    line-height: 25px;
    margin-left: 6px;
  }
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

  @media only screen and (max-width: 780px) {
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

  .commentList {
    width: 100%;
    margin: 0 auto;
  }
  .commentList .p1 {
    float: left;
  }
  .commentList .left {
    display: inline-block;
    width: 10%;
    height: 100%;
  }
  .commentList .left img {
    margin: 0 auto;
    width: 100%;
    border-radius: 50%;
  }
  .commentList .right {
    display: inline-block;
    width: 85%;
    margin-left: 5px;
  }
  .commentList .rightTop {
    height: 30px;
  }
  .commentList .rightTop .userName {
    color: #303133;
    margin-left: 10px;
    font-size: 16px;
    font-weight: bold;
  }
  .commentList .rightTop .timeAgo {
    color: #909399;
    margin-left: 10px;
    font-size: 15px;
  }
  .commentList .rightCenter {
    margin-left: 20px;
    line-height: 30px;
    height: 60px;
  }

  .commentList .rightBottom el-link {

  }

  .feedbackCard .item .title {
    display: inline-block;
    width: 70px;
    margin-bottom: 10px;
    font-size: 14px;
    font-weight: bold;
  }
  .feedbackCard .item .content {
    display: inline-block;
    width: 240px;
    margin-bottom: 5px;
  }

  .emoji-panel-btn img{
    height: 35px;
    width: 35px;
  }
</style>
