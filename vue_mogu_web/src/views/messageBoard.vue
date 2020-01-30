
<template>
  <div>
    <div class="pagebg ab"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>每个人都有自己故事，只是演绎的方式不同。</span>
        <a href="/" class="n1">网站首页</a>
        <a href="/" class="n2">留言</a>
      </h1>
      <sticky :sticky-top="60">
        <CommentBox :userInfo="userInfo" :commentInfo="commentInfo" @submit-box="submitBox"
                    :showCancel="showCancel" ></CommentBox>
      </sticky>


      <div class="message_infos">
        <CommentList :comments="comments" :commentInfo="commentInfo"></CommentList>
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
  import Sticky from '@/components/Sticky'
  import {addComment, getCommentList} from "../api/comment";

  // vuex中有mapState方法，相当于我们能够使用它的getset方法
  import {mapMutations} from 'vuex';

  export default {
    data() {
      return {
        source: "MESSAGE_BOARD",
        showCancel: false,
        submitting: false,
        value: '',
        comments: [],
        commentInfo: {
          // 评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等 代表来自某些页面的评论
          source: "MESSAGE_BOARD"
        },
        toInfo: {},
        userInfo: {
        }
      };
    },
    watch: {},
    computed: {},
    components: {
      CommentList,
      CommentBox,
      Sticky
    },
    created() {
      this.getCommentList();
    },
    mounted() {

    },
    methods: {
      //拿到vuex中的写的两个方法
      ...mapMutations(['setCommentList']),
      submitBox(e) {
        let params = {};
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
                message: response.data,
                offset: 100
              });
            }
            this.getCommentList();
          }
        );
      },
      getCommentList: function () {
        let params = {};
        params.source = "MESSAGE_BOARD";
        params.currentPage = 0;
        params.pageSize = 100;
        getCommentList(params).then(response => {
          console.log("得到的评论内容", response)
          if (response.code == "success") {
            this.comments = response.data;
            this.setCommentList(this.comments);
          }
        });
      }
    },
  }
  ;
</script>
<style scoped>
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
