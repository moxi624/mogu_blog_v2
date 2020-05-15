
<template>
  <div class="page">
    <div class="pagebg ab"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>每个人都有自己故事，只是演绎的方式不同。</span>
        <a href="/" class="n1">网站首页</a>
        <a href="/" class="n2">留言</a>
      </h1>

        <CommentBox
          :userInfo="userInfo"
          :commentInfo="commentInfo"
          @submit-box="submitBox"
          :showCancel="showCancel"
        ></CommentBox>

      <div class="message_infos">
        <CommentList :comments="comments" :commentInfo="commentInfo"></CommentList>
        <div class="noComment" v-if="comments.length ==0">还没有评论，快来抢沙发吧！</div>
      </div>
    </div>
  </div>
</template>

<script>
    import CommentList from "../components/CommentList";
    import CommentBox from "../components/CommentBox";
    import Sticky from "@/components/Sticky";
    import { addComment, getCommentList } from "../api/comment";

    // vuex中有mapState方法，相当于我们能够使用它的getset方法
    import { mapMutations } from "vuex";
    export default {
        data() {
            return {
                pageMinHeight: 0,
                source: "MESSAGE_BOARD",
                showCancel: false,
                submitting: false,
                value: "",
                comments: [],
                commentInfo: {
                    // 评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等 代表来自某些页面的评论
                    source: "MESSAGE_BOARD"
                },
                currentPage: 1,
                pageSize: 10,
                total: 0, //总数量
                toInfo: {},
                userInfo: {}
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
            this.getCommentDataList();
        },
        mounted () {
          var that = this;
          // 屏幕的高度
          this.pageMinHeight = window.innerHeight - 62
          $(window).scroll(function () {
            var docHeight = $(document).height(); // 获取整个页面的高度(不只是窗口,还包括为显示的页面)
            var winHeight = $(window).height(); // 获取当前窗体的高度(显示的高度)
            var winScrollHeight = $(window).scrollTop(); // 获取滚动条滚动的距离(移动距离)
            //还有30像素的时候,就查询
            if(docHeight == winHeight + winScrollHeight){
              if(that.comments.length >= that.total) {
                return;
              }
              let params = {};
              params.source = "MESSAGE_BOARD";
              params.currentPage = that.currentPage + 1
              params.pageSize = that.pageSize;
              getCommentList(params).then(response => {
                if (response.code == "success") {
                  that.comments = that.comments.concat(response.data.records);
                  that.setCommentList(this.comments);
                  that.currentPage = response.data.current;
                  that.pageSize = response.data.size;
                  that.total = response.data.total;
                }
              });
            }
          })
        },
        methods: {
            //拿到vuex中的写的两个方法
            ...mapMutations(["setCommentList"]),
            handleCurrentChange: function(val) {
                this.currentPage = val;
                this.getCommentDataList();
            },
            submitBox(e) {
                let params = {};
                params.source = e.source;
                params.userUid = e.userUid;
                params.content = e.content;
                params.blogUid = e.blogUid;
                addComment(params).then(response => {
                    if (response.code == "success") {
                        this.$notify({
                            title: "成功",
                            message: "发表成功~",
                            type: "success",
                            offset: 100
                        });
                    } else {
                        this.$notify.error({
                            title: "错误",
                            message: response.data,
                            offset: 100
                        });
                    }
                    this.getCommentDataList();
                });
            },
            getCommentDataList: function() {
                let params = {};
                params.source = "MESSAGE_BOARD";
                params.currentPage = this.currentPage;
                params.pageSize = this.pageSize;
                getCommentList(params).then(response => {
                    if (response.code == "success") {
                        this.comments = response.data.records;
                        this.setCommentList(this.comments);
                        this.currentPage = response.data.current;
                        this.pageSize = response.data.size;
                        this.total = response.data.total;
                    }
                });
            },
        }
    };
</script>
<style>
  .emoji-panel-wrap {
    box-sizing: border-box;
    border: 1px solid #cccccc;
    border-radius: 5px;
    background-color: #ffffff;
    width: 650px;
    height: 135px;
    position: absolute;
    z-index: 999;
    left: 50px;
    top: 10px;
  }
  .emoji-size-small {
    zoom: 0.3;
    margin: 5px;
  }
  .emoji-size-large {
    zoom: 0.5; // emojipanel表情大小
    margin: 5px;
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
  .page {
    position: relative;
  }
  .block {
    position: relative;
    bottom: 0px;
  }
</style>
