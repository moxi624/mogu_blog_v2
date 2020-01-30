<template>
  <div>
    <div class="commentList">
    <span class="left p1">
      <img src="https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png" />
    </span>

      <span class="right p1">
        <div class="rightTop">
          <el-link class="userName" :underline="false">陌溪</el-link>
          <span class="timeAgo">4天前</span>
        </div>
        <div class="rightCenter">
          你好呀，老哥
        </div>
        <div class="rightBottom">
          <el-link class="b1" :underline="false">回复</el-link>
          <el-link class="b1" :underline="false">举报</el-link>
          <el-link class="b1" :underline="false">删除</el-link>
        </div>

        <div class="rightCommentList">
          <div class="commentList">
          <span class="left p1">
            <img src="https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png" />
          </span>

            <span class="right p1">
              <div class="rightTop">
                <el-link class="userName" :underline="false">陌溪</el-link>
                <span class="timeAgo">4天前</span>
              </div>
              <div class="rightCenter">
                你好呀，老哥
              </div>
              <div class="rightBottom">
                <el-link class="b1" :underline="false">回复</el-link>
                <el-link class="b1" :underline="false">举报</el-link>
                <el-link class="b1" :underline="false">删除</el-link>
              </div>

              <div class="rightCommentList">

              </div>
            </span>
          </div>
        </div>
      </span>
    </div>
  </div>

</template>

<script>
  export default {
    name: "commentList",
    data() {
      return {
        count: 255,
        value: "",
      }
    },
    methods: {
    },
  };
</script>


<style scoped>
.commentStyle {
  display: block;
  margin-left: 5px;
  border-left: 1px dashed SlateGray;
}
.commentList {
  width: 100%;
  height: 200px;
  margin: 0 auto;
}
.commentList .p1 {
  float: left;
}
.commentList .left {
  display: inline-block;
  width: 4%;
  height: 100%;
  padding-top: 20px;
}
.commentList .left img {
  margin: 0 auto;
  width: 60%;
  border-radius: 50%;
}
.commentList .right {
  display: inline-block;
  width: 95%;
  margin-left: 5px;
}
.commentList .rightTop {
  height: 30px;
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
  height: 60px;
}
.commentList .rightBottom {
  height: 20px;
  margin-left: 10px;
}
.commentList .rightBottom el-link {

}
.commentList .rightBottom .b1 {
  margin-left: 10px;
}
.commentList .rightCommentList {
  width: 100%;

}
</style>
