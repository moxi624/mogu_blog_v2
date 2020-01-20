<template>
  <div>
    <a-comment>

      <a-avatar
        slot="avatar"
        :src="getUserPhoto"
        alt="item.userName"
      />

      <div slot="content">
        <a-form-item>
          <a-textarea :rows="4" @change="handleChange" :value="value"></a-textarea>
        </a-form-item>
        <a-form-item>
          <a-button htmlType="submit" :loading="submitting" @click="handleSubmit" type="primary">
            添加评论
          </a-button>
          <a-button v-if="showCancel" style="margin-left:5px;"  @click="handleCancle">
            取消评论
          </a-button>
        </a-form-item>
      </div>
    </a-comment>
  </div>
</template>
<script>

  import {mapGetters} from 'vuex';

  export default {
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
      };
    },
    computed: {
      ...mapGetters(['getUserPhoto'])
    },
    mounted() {

    },
    methods: {

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
          reply: []
        }

        this.value = '';
        this.$emit("submit-box", this.comments)
      },
      handleChange(e) {
        this.value = e.target.value;
      },
      handleCancle() {
        this.value = '';
        // this.$emit("cancel-box", this.replyInfo.replyUid)
        this.$emit("cancel-box", this.toInfo.commentUid)
      }
    },
  };
</script>
