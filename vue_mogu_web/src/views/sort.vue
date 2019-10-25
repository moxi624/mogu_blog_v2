<template>
<html>
  <Head></Head>

  <body>
    <!--
	作者：xzx19950624@qq.com
	时间：2018-07-15
	描述：顶部标题
    -->
    <BlogHead></BlogHead>

    <div class="pagebg timer"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>有些的时候，正是为了爱才悄悄躲开。躲开的是身影，躲不开的却是那份默默的情怀。</span>
        <a href="/" class="n1">网站首页</a>
        <a href="javascript:void(0);" class="n2">归档</a>
      </h1>
      <div class="sortBox">
        <div class="time">
          <div class="block">
            <div class="radio" style="margin-bottom:20px;">
              <el-switch
                v-model="reverse"
                active-text="倒序"
                inactive-text="正序"
                active-color="#000000"
                inactive-color="#13ce66"
              ></el-switch>
            </div>
            <el-timeline :reverse="reverse">
              <el-timeline-item v-for="(activity, index) in activities" hide-timestamp :key="index">
                <span @click="clickTime(activity.content)" :class="[activity.content == selectContent ? 'sortBoxSpan sortBoxSpanSelect' : 'sortBoxSpan']">{{activity.content}}</span>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>

        <div class="article">
          <div class="block">
            <el-timeline>
              <el-timeline-item
                v-for="item in itemByDate"
                :key="item.timestamp"
                :timestamp="item.createTime"
                placement="top"
              >
                <el-card>
                  <h4 @click="goToInfo(item.uid)" class="itemTitle">{{item.title}}</h4>
                  <br />
                  <el-tag v-if="item.isOriginal==0" type="danger">原创</el-tag>
                  <el-tag v-if="item.isOriginal==1" type="info">转载</el-tag>
                  <el-tag type="success">标签二</el-tag>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
      </div>
    </div>

    <!--
	作者：xzx19950624@qq.com
	时间：2018-07-15
	描述：博客底部
    -->
    <BlogFooter></BlogFooter>

    <!--返回顶部-->
    <CdTop></CdTop>
  </body>
</html>
</template>

<script>
import Head from "../components/Head";
import BlogHead from "../components/BlogHead";
import BlogFooter from "../components/BlogFooter";
import CdTop from "../components/CdTop";
import { recorderVisitPage } from "../api/index";
import { getSortByMonth } from "../api/sort";
export default {
  data() {
    return {
      selectContent: "",
      reverse: true,
      activities: [],
      itemByDate: [],
      articleByDate: {}
    };
  },
  components: {
    //注册组件
    BlogHead,
    BlogFooter,
    Head,
    CdTop
  },
  mounted() {},
  created() {
    var that = this;
    getSortByMonth().then(response => {
      if (response.code == "success") {
        var activities = response.data.month;
        this.articleByDate = response.data.content;
        console.log("返回的内容", response);
        var result = [];
        for (var a = 0; a < activities.length; a++) {
          var temp = activities[a].replace("年", "-").replace("月", "-") + "1";
          var dataForDate = { content: activities[a], timestamp: temp };
          result.push(dataForDate);
        }
        this.activities = result;
        this.selectContent = activities[activities.length-1];        
        this.itemByDate = response.data.content[activities[activities.length-1]];
        console.log("activities", this.activities);
      }
    });

    var params = new URLSearchParams();
    params.append("pageName", "SORT");
    recorderVisitPage(params).then(response => {});
  },
  methods: {
    clickTime(content) {
      console.log("点击了", this.articleByDate[content]);
      this.selectContent = content;
      this.itemByDate = this.articleByDate[content];
    },
    //跳转到文章详情
    goToInfo(uid) {
      let routeData = this.$router.resolve({
        path: "/info",
        query: { blogUid: uid }
      });
      window.open(routeData.href, "_blank");
    },
    formatDate: function(time) {
      var date = new Date(time);
      var year = date.getFullYear();
      /* 在日期格式中，月份是从0开始的，因此要加0
       * 使用三元表达式在小于10的前面加0，以达到格式统一  如 09:11:05
       * */
      var month =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      // 拼接
      return year + "-" + month + "-" + day;
    }
  }
};
</script>


<style>
.sortBox {
  color: #555;
}
.sortBox .time {
  float: left;
  width: 17%;
  height: 800px;
  overflow: scroll;
  overflow-x: hidden;
  overflow-y: auto;
}

.sortBox .article {
  margin-left: 20px;
  float: left;
  width: 78%;
  height: 800px;
  overflow: scroll;
  overflow-x: hidden;
  overflow-y: auto;
}

.sortBoxSpan {
  cursor: pointer;
}
.sortBoxSpan:hover {
  color: #409EFF;
}
.sortBoxSpanSelect {
  color: #409EFF;
}

.itemTitle {
  cursor: pointer;
}
.itemTitle:hover {
  color: #409EFF;
}

</style>
