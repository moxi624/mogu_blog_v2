<template>
  <div>
    <div class="commentBox">
    <span class="left">
      <img :src="getUserPhoto" onerror="onerror=null;src='https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'"/>
    </span>

      <span class="right">
      <textarea class="textArea" placeholder="既然来了，那就留下些什么吧~" v-model="value" @input="vaildCount"></textarea>
    </span>
    </div>
    <div class="bottom">
      <el-button class="submit p2" type="primary"  @click="handleSubmit">发送评论</el-button>
      <el-button class="cancel p2" type="info" @click="handleCancle">取消评论</el-button>
      <span class="allow p2">还能输入{{count}}个字符</span>
    </div>
  </div>

</template>

<script>
  import {dateFormat} from '../../utils/webUtils'
  import {mapGetters} from 'vuex';
  export default {
    name: 'CommentBox',
    props: {
      userInfo: {
        type: Object
      },
      // 回复的对象
      toInfo: {
        type: Object
      },
      // 博客信息
      commentInfo: {
        type: Object
      },
      showCancel: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        PICTURE_HOST: process.env.PICTURE_HOST,
        comments: [],
        submitting: false,
        value: '',
        user: {},
        count: 255,
      }
    },
    computed: {
      ...mapGetters(['getUserPhoto'])
    },
    methods: {
      vaildCount: function() {
        var count = 255 - this.value.length;
        if(count <= 0) {
          this.count = 0
        } else {
          this.count = count;
        }
      },
      handleSubmit() {
        let info = this.$store.state.user.userInfo
        let isLogin = this.$store.state.user.isLogin
        console.log("是否登录", isLogin);
        if(!isLogin) {

          this.$notify.error({
            title: '警告',
            message: '登录后才可以评论哦~',
            offset: 100
          });
          return;
        }

        if(this.value =="") {
          this.$notify.error({
            title: '警告',
            message: '评论内容不能为空哦~',
            offset: 100
          });
          return;
        }

        let userUid = info.uid;
        let toUserUid = "";
        let toCommentUid = "";
        let blogUid = "";

        // 评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等 代表来自某些页面的评论
        let source = "";
        let content = this.value;
        if(this.toInfo) {
          toUserUid = this.toInfo.uid;
          toCommentUid = this.toInfo.commentUid;
        }
        if(this.commentInfo) {
          blogUid = this.commentInfo.blogUid;
          source = this.commentInfo.source;
        }

        this.comments = {
          userUid: userUid,
          toCommentUid: toCommentUid,
          toUserUid: toUserUid,
          content: content,
          blogUid: blogUid,
          source: source,
          reply: [],
        }
        this.value = '';
        this.count = 255;
        this.comments.createTime = dateFormat("YYYY-mm-dd HH:MM:SS", new Date());
        console.log('返回的内容', this.comments)
        this.$emit("submit-box", this.comments)
      },
      handleCancle() {
        this.value = '';
        this.count = 255;
        this.$emit("cancel-box", this.toInfo.commentUid)
      }
    },
  };
</script>


<style scoped>
  .commentBox {
    width: 100%;
    height: 100px;
    margin: 0 auto;
  }
  .commentBox .left {
    display: inline-block;
    width: 4%;
    height: 100%;
    padding-top: 3px;
  }
  .commentBox .left img {
    cursor: pointer;
    margin: 0 auto;
    width: 90%;
    border-radius: 50%;
  }
  .commentBox .right {
    display: inline-block;
    margin: 5px 2px 0 0;
    width: 95%;
    height: 100%;
  }
  textarea::-webkit-input-placeholder {
    color: #909399;
  }
  .commentBox .right textarea {
    color: #606266;
    padding:10px 5px 5px 10px;
    resize: none;
    width: 95%;
    height: 100%;
  }
  .bottom {
    width: 98%;
    height: 60px;
    line-height: 40px;
    margin-top: 20px;
  }
  .bottom .p2 {
    float: right;
    margin-top: 5px;
    margin-right: 10px;
  }

</style>
