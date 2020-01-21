
<template>
  <div>
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
</template>

<script>
  import CommentList from "../CommentList";
  import CommentBox from "../CommentBox";
  import {addComment, getCommentList} from "../../api/comment";

  // vuex中有mapState方法，相当于我们能够使用它的getset方法
  import {mapMutations} from 'vuex';

  export default {
    name: "CommentList",
    props: ['commentInfo'],
    data() {
      return {
        source: "MESSAGE_BOARD",
        showCancel: false,
        submitting: false,
        value: '',
        comments: [],
        toInfo: {

        },
        userInfo: {

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
    },
  }
  ;
</script>
<style>
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
