<template>
  <div>
    <a-comment v-for="item in comments" :key="item.uid">
      <span slot="actions" @click="replyTo(item.uid)">回复</span>
      <span slot="author" v-if="item.user">
        <span>{{item.user.userName}}</span>
      </span>
      <a-avatar
        slot="avatar"
        :src="item.user? PICTURE_HOST + item.user.photoUrl:''"
        :alt="item.userName"
      />
      <p slot="content">
        {{item.content}}
      </p>

      <CommentBox class="comment" :userInfo="userInfo" :reply-info="replyInfo" :id="item.uid"
                  @submit-box="submitBox" @cancel-box="cancelBox"></CommentBox>

      <CommentList :comments="item.reply"></CommentList>

    </a-comment>
  </div>
</template>
<script>

  import {mapMutations} from 'vuex';
  import CommentBox from "../CommentBox";

  export default {
    name: "CommentList",
    props: ['comments'],
    data() {
      return {
        PICTURE_HOST: process.env.PICTURE_HOST,
        showCancle: true,
        submitting: false,
        value: '',
        userInfo: {
          uid: "uid000001",
          userName: "张三",
          avatar: "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"

        },
        replyInfo: {
          uid: "",
          blogUid: "uid000003",
          replyUserUid: 0,
          avatar: "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
        },
      };
    },
    created() {

    },
    components: {
      CommentBox
    },

    compute: {},
    methods: {
      ...mapMutations(['setCommentList', 'increment']),
      replyTo: function (uid) {
        var lists = document.getElementsByClassName("comment");
        for (var i = 0; i < lists.length; i++) {
          lists[i].style.display = 'none';
        }
        document.getElementById(uid).style.display = 'block';
        this.replyInfo.replyUid = uid
      },
      submitBox(e) {
        // 一级评论
        if (e.replyUid == 0) {
          var firstComment = this.$store.state.app.commentList;
          firstComment.push(e);
          this.$store.commit("setCommentList", firstComment);
          this.$store.commit("increment");

          return;
        }

        document.getElementById(e.replyUid).style.display = 'none'
        var comments = this.$store.state.app.commentList;
        this.updateCommentList(comments, e.replyUid, e)
        this.$store.commit("setCommentList", comments);
        this.$store.commit("increment");

      },
      cancelBox(e) {
        document.getElementById(e).style.display = 'none'
      },
      updateCommentList(commentList, uid, targetComment) {

        if (commentList == undefined || commentList.length <= 0) {
          return;
        }

        for (let item of commentList) {
          console.log("递归中", item.uid, uid)
          if (item.uid === uid) {
            var menu = item.reply;
            menu.push(targetComment);
          } else {
            this.updateCommentList(item.reply, uid, targetComment);
          }
        }
      }
    },
  };
</script>
<style>
  .comment {
    display: none;
  }
</style>
