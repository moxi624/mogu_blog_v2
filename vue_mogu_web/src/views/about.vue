<template>
  <div>
    <div class="pagebg ab"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>你，我生命中一个重要的过客，我们之所以是过客，因为你未曾会为我停留。</span>
        <a href="/" class="n1">网站首页</a>
        <a href="/" class="n2">关于我</a>
      </h1>
      <div class="news_infos">
          <a-anchor>
            <CommentBox :userInfo="userInfo" :commentInfo="commentInfo" @submit-box="submitBox"
                        :showCancel="showCancel" ></CommentBox>
          </a-anchor>
          <div class="message_infos">
            <CommentList :comments="comments" :commentInfo="commentInfo"></CommentList>
            <div class="noComment" v-if="comments.length ==0">
              还没有评论，快来抢沙发吧！
            </div>
          </div>
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
import { recorderVisitPage } from "../api/index";
import { getMe } from "../api/about";
import CommentList from "../components/CommentList";
import CommentBox from "../components/CommentBox";
import {mapMutations} from 'vuex';
import {addComment, getCommentList} from "../api/comment";

export default {
  name: "about",
  data() {
    return {
      source: "MESSAGE_BOARD",
      showCancel: false,
      submitting: false,
      comments: [],
      commentInfo: {
        // 评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等 代表来自某些页面的评论
        source: "ABOUT"
      },
      toInfo: {},
      userInfo: {},
      PICTURE_HOST: process.env.PICTURE_HOST,
      info: {},
      sid: "test",
      isRouterAlive: false
    };
  },
  components: {
    //注册组件
    FollowUs,
    CommentList,
    CommentBox
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

    this.getCommentList();
  },
  methods: {
    //拿到vuex中的写的两个方法
    ...mapMutations(['setCommentList']),
    submitBox(e) {
      let params = {};
      params.blogUid = e.blogUid;
      params.source = e.source;
      params.userUid = e.userUid;
      params.content = e.content;
      params.blogUid = e.blogUid;
      addComment(params).then(response => {
          if (response.code == "success") {
            this.$notify({
              title: '成功',
              message: "发表成功~",
              type: 'success',
              offset: 100
            });
          } else {
            this.$notify.error({
              title: '错误',
              message: "发表失败，请稍后再试",
              offset: 100
            });
          }
          this.getCommentList();
        }
      );
    },
    getCommentList: function () {
      let params = {};
      params.source = this.commentInfo.source;
      params.currentPage = 0;
      params.pageSize = 10;
      getCommentList(params).then(response => {
        if (response.code == "success") {
          this.comments = response.data;
          this.setCommentList(this.comments);
        }
      });
    }
  }
};
</script>

<style>
  .ant-comment-actions {
    margin-top: -20px;
  }
  .ant-anchor-ink {
    position: relative;
  }
  .ant-form-item {
    margin-bottom: 1px;
  }
  .contain {
    width: 600px;
    margin: 0 auto;
  }
  .message_infos {
    width: 100%;
    min-height: 500px;
    margin-left: 10px;
  }
  .noComment {
    width: 100%;
    text-align: center;
  }
</style>
