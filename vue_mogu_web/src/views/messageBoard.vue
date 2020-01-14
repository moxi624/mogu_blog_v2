
<template>
  <div>
    <div class="pagebg ab"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>每个人都有自己故事，只是演绎的方式不同。</span>
        <a href="/" class="n1">网站首页</a>
        <a href="/" class="n2">留言</a>
      </h1>
      <a-anchor>
        <CommentBox :userInfo="userInfo" :commentInfo="commentInfo" @submit-box="submitBox"
                    :showCancel="showCancel"></CommentBox>
      </a-anchor>


      <div class="message_infos">
        <CommentList :comments="comments"></CommentList>
        <div class="noComment" v-if="comments.length ==0">
          还没有评论，快来抢沙发吧！
        </div>
      </div>

    </div>
  </div>
</template>

<script>
  import CommentList from "../components/CommentList";
  import CommentBox from "../components/CommentBox";
  import {addComment, getCommentList} from "../api/comment";
  import {authVerify} from "../api/user";
  import {getCookie} from "@/utils/cookieUtils";

  // vuex中有mapState方法，相当于我们能够使用它的getset方法
  import {mapMutations} from 'vuex';

  export default {
    data() {
      return {
        showCancel: false,
        submitting: false,
        value: '',
        comments: [],
        commentInfo: {
          blogUid: "51fa6be01a7296c4fc380f7780db9641",
          resource: "blogInfo"
        },
        toInfo: {
          userName: "moguBlog_GITHUB_18610136",
          passWord: "410074",
          nickName: "Streamlet",
          avatar: "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
          email: "moxi0624@163.com",
          loginCount: 3,
          source: "GITHUB",
          uuid: "18610136",
          lastLoginTime: "2020-01-12 01:39:03",
          lastLoginIp: "116.1.3.214",
          uid: "0b51c75ed5744cdcadefe0ad947be9b5",
          status: 1,
          createTime: "2020-01-01 08:42:23",
          updateTime: "2020-01-01 08:42:23",
          commentUid: "defd44fca1cd24c52aab5f696613f842"
        },
        userInfo: {
          userName: "moguBlog_GITHUB_18610136",
          passWord: "410074",
          nickName: "Streamlet",
          avatar: "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
          email: "moxi0624@163.com",
          loginCount: 3,
          source: "GITHUB",
          uuid: "18610136",
          lastLoginTime: "2020-01-12 01:39:03",
          lastLoginIp: "116.1.3.214",
          uid: "0b51c75ed5744cdcadefe0ad947be9b5",
          status: 1,
          createTime: "2020-01-01 08:42:23",
          updateTime: "2020-01-01 08:42:23",
        }
      };
    },
    watch: {},
    computed: {},
    components: {
      CommentList,
      CommentBox
    },
    created() {
      this.getCommentList();
    },
    mounted() {
      // this.setCommentList(this.comments);
    },
    methods: {
      //拿到vuex中的写的两个方法
      ...mapMutations(['setCommentList']),
      submitBox(e) {
        let params = {};
        params.userUid = e.userUid;
        params.content = e.content;
        params.blogUid = e.blogUid;
        addComment(params).then(response => {
            if (response.code == "success") {

            } else {

            }
            this.getCommentList();
          }
        );
      },
      getCommentList: function () {
        let params = {};
        params.currentPage = 0;
        params.pageSize = 10;
        getCommentList(params).then(response => {
          console.log("得到的响应", response);
          if (response.code == "success") {
            this.comments = response.data;
            this.setCommentList(this.comments);
          }
        });
      }
      ,
      getUserInfo() {
        // 从cookie中获取token
        token = getCookie("token")
        if (token != undefined) {
          authVerify(token).then(response => {
            if (response.code == "success") {
              console.log("得到的用户信息");
              this.userInfo = response.data;
            }
          });
        }

      }
    },
  }
  ;
</script>
<style>
  .contain {
    width: 600px;
    margin: 0 auto;
  }
  .message_infos {
    width: 100%;
    min-height: 500px;
    /*height: 800px;*/
    /*overflow: auto;*/
    margin-left: 10px;
  }
  .noComment {
    width: 100%;
    text-align: center;
  }
</style>
