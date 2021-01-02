<template>
  <div>
    <div v-for="item in comments" :key="item.uid">
      <div class="commentList">
        <span class="left p1">
          <img v-if="item.user" :src="item.user.photoUrl ? item.user.photoUrl:defaultAvatar" onerror="onerror=null;src=defaultAvatar" />
          <img v-else :src="defaultAvatar" />
        </span>

        <span class="right p1">
          <div class="rightTop" v-if="item.user">
            <el-link class="userName" :underline="false">{{item.user.nickName}}</el-link>
            <el-tag style="height: 30px; margin-left:5px;" v-for="userTag in userTagDictList" :key="userTag.uid" v-if="item.user.userTag == userTag.dictValue && item.user.userTag != 0" :type="userTag.listClass">{{userTag.dictLabel}}</el-tag>
            <span class="timeAgo" v-if="item.createTime">{{timeAgo(item.createTime)}}</span>
            <span class="timeAgo" v-else>刚刚</span>
          </div>

          <div class="rightCenter" v-html="$xss(item.content, options)"></div>
<!--          <div class="rightCenter" v-html="item.content"></div>-->

          <div class="rightBottom">
            <el-link class="b1" :underline="false" @click="replyTo(item)">回复</el-link>
            <el-link class="b1" :underline="false" @click="report(item)">举报</el-link>
            <el-link class="b1" v-if="$store.state.user.isLogin && $store.state.user.userInfo.uid == item.userUid" :underline="false" @click="delComment(item)">删除</el-link>
          </div>

          <div class="rightCommentList">
            <CommentBox class="comment" :userInfo="userInfo" :toInfo="toInfo" :id="item.uid" :commentInfo="commentInfo"
                        @submit-box="submitBox" @cancel-box="cancelBox"></CommentBox>

            <CommentList class="commentStyle" :id="'commentStyle:' + item.uid" :comments="item.replyList" :commentInfo="commentInfo"></CommentList>
          </div>
        </span>
      </div>
    </div>
  </div>

</template>

<script>

  import {mapMutations} from 'vuex';
  import CommentBox from "../CommentBox";
  import {timeAgo} from "../../utils/webUtils"
  import {addComment, deleteComment, getCommentList, reportComment} from "../../api/comment";
  import {getListByDictTypeList} from "@/api/sysDictData"
  export default {
    name: "CommentList",
    props: ['comments', 'userInfos', 'commentInfo'],
    data() {
      return {
        // xss白名单配置
        options : {
          whiteList: {
            a: ['href', 'title', 'target'],
            span: ['class']
          }
        },
        taggleStatue: true,
        submitting: false,
        value: '',
        toInfo: {
          uid: "",
          commentUid: ""
        },
        userInfo: {},
        userTagDictList: [], // 用户标签字典
        defaultAvatar: this.$SysConf.defaultAvatar
      };
    },
    created() {
      this.getDictList()
    },
    components: {
      CommentBox
    },
    mounted() {

    },
    compute: {},
    methods: {
      ...mapMutations(['setCommentList', 'setUserTag']),
      /**
       * 字典查询
       */
      getDictList: function () {
        if(this.$store.state.app.userTagDictList.length > 0) {
          this.userTagDictList = this.$store.state.app.userTagDictList
          return;
        }
        var dictTypeList =  ['sys_user_tag']
        getListByDictTypeList(dictTypeList).then(response => {
          if (response.code == this.$ECode.SUCCESS) {
            var dictMap = response.data;
            this.userTagDictList = dictMap.sys_user_tag.list
            this.setUserTag(dictMap.sys_user_tag.list)
          }
        });
      },
      replyTo: function (item) {
        if(!this.validLogin()) {
          this.$notify.error({
            title: '错误',
            message: "登录后才能回复评论哦~",
            offset: 100
          });
          return
        }
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
        console.log("添加内容", e)
        let params = {};
        params.userUid = e.userUid;
        params.content = e.content;
        params.blogUid = e.blogUid;
        params.toUid = e.toCommentUid;
        params.toUserUid = e.toUserUid;
        params.source = e.source
        addComment(params).then(response => {
            if (response.code == this.$ECode.SUCCESS) {
              let commentData = response.data
              document.getElementById(commentData.toUid).style.display = 'none'
              let comments = this.$store.state.app.commentList;
              commentData.user = this.userInfo;
              // 设置回复为空
              commentData.replyList = [];
              commentData.user = this.$store.state.user.userInfo
              this.updateCommentList(comments, commentData.toUid, commentData)
              console.log('得到的评论', comments)
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
          }
        )
        ;
      },
      getCommentList: function () {
        let params = {};
        params.currentPage = 0;
        params.pageSize = 10;
        getCommentList(params).then(response => {
          if (response.code == this.$ECode.SUCCESS) {
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
        if(!this.validLogin()) {
          this.$notify.error({
            title: '错误',
            message: "登录后才能举报评论哦~",
            offset: 100
          });
          return
        }

        let userUid = this.$store.state.user.userInfo.uid

        if(userUid == item.userUid) {
          this.$notify.error({
            title: '错误',
            message: "不能举报自己的评论哦~",
            offset: 100
          });
          return;
        }

        let params = {};
        params.uid = item.uid;
        params.userUid = userUid
        reportComment(params).then(response => {
          if (response.code == this.$ECode.SUCCESS) {
            this.$notify({
              title: '成功',
              message: response.data,
              type: 'success',
              offset: 100
            });
          } else {
            this.$notify.error({
              title: '错误',
              message: response.data,
              type: 'success',
              offset: 100
            });
          }
        });
      },
      delComment: function (item) {
        if(!this.validLogin()) {
          this.$notify.error({
            title: '错误',
            message: "登录后才能删除评论哦~",
            offset: 100
          });
          return
        }

        this.$confirm("此操作将把本评论和子评论删除, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            let params = {};
            params.uid = item.uid;
            params.userUid = this.$store.state.user.userInfo.uid
            deleteComment(params).then(response => {
              if (response.code == this.$ECode.SUCCESS) {
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
                  offset: 100
                });
              }
              let comments = this.$store.state.app.commentList;
              this.deleteCommentList(comments, params.uid, null)
              this.$store.commit("setCommentList", comments);
              this.$emit("deleteComment", "")
            });

          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });


      },
      // 校验是否登录
      validLogin() {
        let userInfo = this.$store.state.user.userInfo
        if(userInfo.userName == undefined) {
          return false;
        } else {
          return true;
        }
      },
      /**
       * dateTimeStamp是一个时间毫秒，注意时间戳是秒的形式，在这个毫秒的基础上除以1000，就是十位数的时间戳。13位数的都是时间毫秒。
       * @param dateTimeStamp
       * @returns {string}
       */
      timeAgo(dateTimeStamp) {
        return timeAgo(dateTimeStamp)
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

<style scoped>
  .commentStyle {
    display: block;
    margin-top: 10px;
    margin-left: 10px;
    border-left: 1px dashed SlateGray;
  }
  .comment {
    display: none;
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
    width: 5%;
    height: 100%;
  }
  .commentList .left img {
    margin: 0 auto;
    width: 100%;
    border-radius: 50%;
  }
  .commentList .right {
    display: inline-block;
    width: 93%;
    margin-left: 5px;
  }
  .commentList .rightTop {
    height: 30px;
    margin-top: 2px;
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
    min-height: 50px;
    margin-top: 15px;
  }
  .commentList .rightBottom {
    margin-left: 10px;
    height: 30px;
  }
  .commentList .rightBottom el-link {

  }
  .commentList .rightBottom .b1 {
    margin-left: 10px;
  }

  @media only screen and (min-width: 300px) and (max-width: 767px) {
    .commentList .left {
      display: inline-block;
      width: 30px;
      height: 100%;
    }
    .commentList .right {
      display: inline-block;
      width: calc(100% - 35px);
      margin-left: 5px;
    }
  }
</style>
