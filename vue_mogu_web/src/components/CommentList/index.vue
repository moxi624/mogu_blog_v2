<template>
  <div>
    <a-comment v-for="item in comments" :key="item.uid">
      <span class="actions" slot="actions">
        <span class="sp1" @click="replyTo(item)">回复</span>
      </span>

      <span class="actions" slot="actions">
        <span class="sp1" @click="report(item)">举报</span>
      </span>

      <span class="actions" slot="actions">
        <span class="sp1" @click="delComment(item)">删除</span>
      </span>

      <!--      <span class="actions" slot="actions" v-if="item.replyList && item.replyList.length > 0">-->
      <!--        <span class="sp1" @click="taggleAll(item)">收起</span>-->
      <!--      </span>-->

      <span class="author" slot="author" v-if="item.user">
        <span class="s1">{{item.user.userName}}</span>
        <span class="s2">{{timeAgo(item.createTime)}}</span>

      </span>
      <a-avatar
        class="avatarPhoto"
        slot="avatar"
        :src="item.user? PICTURE_HOST + item.user.photoUrl:''"
        :alt="item.userName"
      />
      <p slot="content">
        {{item.content}}
      </p>

      <!--      <CommentBox class="comment" :userInfo="userInfo" :reply-info="replyInfo" :id="item.uid"-->
      <!--                  @submit-box="submitBox" @cancel-box="cancelBox"></CommentBox>-->

      <CommentBox class="comment" :userInfo="userInfo" :toInfo="toInfo" :id="item.uid" :commentInfo="commentInfo"
                  @submit-box="submitBox" @cancel-box="cancelBox"></CommentBox>

      <CommentList class="commentStyle" :id="'commentStyle:' + item.uid" :comments="item.replyList"></CommentList>

    </a-comment>
  </div>
</template>
<script>

  import {mapMutations} from 'vuex';
  import CommentBox from "../CommentBox";
  import {addComment, deleteComment, getCommentList, reportComment} from "../../api/comment";

  export default {
    name: "CommentList",
    props: ['comments'],
    data() {
      return {
        PICTURE_HOST: process.env.PICTURE_HOST,
        taggleStatue: true,
        submitting: false,
        value: '',
        commentInfo: {
          blogUid: "51fa6be01a7296c4fc380f7780db9641",
          resource: "blogInfo"
        },
        toInfo: {
          uid: "",
          commentUid: ""
        },
        userInfo: {

        }
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
      replyTo: function (item) {

        let userUid = item.userUid;
        let commentUid = item.uid;
        var lists = document.getElementsByClassName("comment");
        for (var i = 0; i < lists.length; i++) {
          lists[i].style.display = 'none';
        }
        document.getElementById(commentUid).style.display = 'block';
        this.toInfo.commentUid = commentUid
        this.toInfo.uid = userUid
      },
      submitBox(e) {
        let params = {};
        params.userUid = e.userUid;
        params.content = e.content;
        params.blogUid = e.blogUid;
        params.toUid = e.toCommentUid;
        params.toUserUid = e.toUserUid;
        params.resource = e.resource
        addComment(params).then(response => {
            if (response.code == "success") {

              let commentData = response.data
              document.getElementById(commentData.toUid).style.display = 'none'
              let comments = this.$store.state.app.commentList;
              commentData.user = this.userInfo;
              // 设置回复为空
              commentData.replyList = [];

              commentData.user = this.$store.state.user.userInfo
              this.updateCommentList(comments, commentData.toUid, commentData)

              this.$store.commit("setCommentList", comments);

              this.$notify({
                title: '成功',
                message: "评论成功",
                type: 'success',
                offset: 100
              });
            } else {
              this.$notify.error({
                title: '错误',
                message: "评论失败",
                type: 'success',
                offset: 100
              });
            }
            // let commentData = response.data
            // document.getElementById(commentData.toUid).style.display = 'none'
            // this.$emit("addComment", "")
          }
        )
        ;
      },
      getCommentList: function () {
        let params = {};
        params.currentPage = 0;
        params.pageSize = 10;
        getCommentList(params).then(response => {
          if (response.code == "success") {
            this.comments = response.data;
          }
        });
      }
      ,
      cancelBox(toCommentUid) {
        document.getElementById(toCommentUid).style.display = 'none'
      }
      ,
      taggleAll: function (item) {

        this.taggleStatue = !this.taggleStatue;
        var lists = document.getElementsByClassName("commentStyle");
        for (var i = 0; i < lists.length; i++) {
          lists[i].style.display = 'block';
        }
        if (this.taggleStatue) {
          document.getElementById('commentStyle:' + item.uid).style.display = 'block';
        } else {
          document.getElementById(item.uid).style.display = 'none';
        }
      }
      ,
      report: function (item) {
        let params = {};
        params.uid = item.uid;
        params.userUid = this.userInfo.uid;
        reportComment(params).then(response => {
          if (response.code == "success") {
            this.$notify({
              title: '成功',
              message: "举报成功",
              type: 'success',
              offset: 100
            });
          } else {
            this.$notify.error({
              title: '错误',
              message: "举报失败",
              type: 'success',
              offset: 100
            });
          }
        });
      }
      ,
      delComment: function (item) {
        var that = this;
        let params = {};
        params.uid = item.uid;
        params.userUid = this.userInfo.uid;

        deleteComment(params).then(response => {
          if (response.code == "success") {
            this.$notify({
              title: '成功',
              message: "删除成功",
              type: 'success',
              offset: 100
            });

          } else {
            this.$notify.error({
              title: '错误',
              message: "删除失败",
              type: 'success',
              offset: 100
            });
          }
          let comments = this.$store.state.app.commentList;
          this.deleteCommentList(comments, params.uid, null)
          this.$store.commit("setCommentList", comments);

          this.$emit("deleteComment", "")

        });
      }
      ,
      /**
       * dateTimeStamp是一个时间毫秒，注意时间戳是秒的形式，在这个毫秒的基础上除以1000，就是十位数的时间戳。13位数的都是时间毫秒。
       * @param dateTimeStamp
       * @returns {string}
       */
      timeAgo(dateTimeStamp) {
        let result = "";
        let minute = 1000 * 60;      //把分，时，天，周，半个月，一个月用毫秒表示
        let hour = minute * 60;
        let day = hour * 24;
        let week = day * 7;
        let halfamonth = day * 15;
        let month = day * 30;
        let now = new Date().getTime();   //获取当前时间毫秒

        dateTimeStamp = dateTimeStamp.substring(0, 18);
        //必须把日期'-'转为'/'
        dateTimeStamp = dateTimeStamp.replace(/-/g, '/');
        let timestamp = new Date(dateTimeStamp).getTime();

        let diffValue = now - timestamp;//时间差

        if (diffValue < 0) {
          return result;
        }
        let minC = diffValue / minute;  //计算时间差的分，时，天，周，月
        let hourC = diffValue / hour;
        let dayC = diffValue / day;
        let weekC = diffValue / week;
        let monthC = diffValue / month;
        if (monthC >= 1 && monthC <= 3) {
          result = " " + parseInt(monthC) + "月前"
        } else if (weekC >= 1 && weekC <= 3) {
          result = " " + parseInt(weekC) + "周前"
        } else if (dayC >= 1 && dayC <= 6) {
          result = " " + parseInt(dayC) + "天前"
        } else if (hourC >= 1 && hourC <= 23) {
          result = " " + parseInt(hourC) + "小时前"
        } else if (minC >= 1 && minC <= 59) {
          result = " " + parseInt(minC) + "分钟前"
        } else if (diffValue >= 0 && diffValue <= minute) {
          result = "刚刚"
        } else {
          let datetime = new Date();
          datetime.setTime(dateTimeStamp);
          let Nyear = datetime.getFullYear();
          let Nmonth = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
          let Ndate = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
          let Nhour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
          let Nminute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
          let Nsecond = datetime.getSeconds() < 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
          result = Nyear + "-" + Nmonth + "-" + Ndate
        }
        return result;
      },
      updateCommentList(commentList, uid, targetComment) {
        if (commentList == undefined || commentList.length <= 0) {
          return;
        }
        for (let item of commentList) {
          if (item.uid === uid) {
            var menu = item.replyList;
            menu.push(targetComment);
          } else {
            this.updateCommentList(item.replyList, uid, targetComment);
          }
        }
      },
      deleteCommentList(commentList, uid, parentList) {
        if (commentList == undefined || commentList.length <= 0) {
          return;
        }
        for (let item of commentList) {
          if (item.uid === uid) {
            commentList.splice(commentList.indexOf(item), 1);
          } else {
            this.deleteCommentList(item.replyList, uid, item);
          }
        }
      },
    },
  };
</script>
<style>
  .comment {
    display: none;
  }


  .commentStyle {
    display: block;
    margin-left: 5px;
    /*border-right: 1px solid #dfdfdf;*/
    border-left: 1px dashed SlateGray;
  }

  .avatarPhoto {
    margin-left: 10px;
  }
  .actions .sp1 {
    margin-left: 5px;
  }

  .author .s1 {
    cursor: pointer;
    color: #333333;
    font-weight: bold;
    font-size: 16px;
    margin-left: 5px;
  }

  .author .s1:hover {
    color: cornflowerblue;
  }

  .author .s2 {
    margin-left: 10px;
  }
</style>
