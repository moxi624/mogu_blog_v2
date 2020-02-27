<template>
  <div>
    <div class="pagebg timer"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>时光飞逝，机会就在我们眼前，何时找到了灵感，就要把握机遇，我们需要智慧和勇气去把握机会。</span>
        <a href="/" class="n1">网站首页</a>
        <a href="javascript:void(0);" class="n2">时间轴</a>
      </h1>
      <div class="timebox">
        <ul id="list" v-infinite-scroll="load">
          <li v-for="item in newBlogData" :key="item.uid">
            <span>{{formatDate(item.createTime)}}</span>
            <a
              href="javascript:void(0);"
              @click="goToInfo(item.uid)"
              :title="item.title"
            >{{item.title}}</a>
          </li>
        </ul>
        <ul id="list2"></ul>
      </div>
    </div>
  </div>
</template>

<script>
import { getBlogByTime } from "../api/index";
export default {
  data() {
    return {
      currentPage: 1,
      pageSize: 15,
      newBlogData: [],
      isEnd: false
    };
  },
  components: {
    //注册组件
  },
  mounted() {
    // 注册scroll事件并监听
    var that = this;
    var loading = false;
  },
  created() {
    var that = this;
    var params = new URLSearchParams();
    params.append("currentPage", this.currentPage);
    params.append("pageSize", this.pageSize);
    getBlogByTime(params).then(response => {
      that.newBlogData = response.data.records;
      that.total = response.data.total;
      that.pageSize = response.data.size;
      that.currentPage = response.data.current;
      this.isEnd = false;
    });
  },
  methods: {
    //跳转到文章详情
    goToInfo(uid) {
      let routeData = this.$router.resolve({
        path: "/info",
        query: { blogUid: uid }
      });
      window.open(routeData.href, "_blank");
    },
    load() {
      var params = new URLSearchParams();
      var that = this;
      var loading = true;
      params.append("currentPage", this.currentPage + 1);
      params.append("pageSize", this.pageSize);
      getBlogByTime(params).then(response => {
        if (response.code == "success" && response.data.records.length > 0) {
          that.isEnd = false;
          var newData = that.newBlogData.concat(response.data.records);
          that.newBlogData = newData;
          that.total = response.data.total;
          that.pageSize = response.data.size;
          that.currentPage = response.data.current;
        } else {
          that.isEnd = true;
        }
        loading = false;
      });
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
.isEnd {
  width: 100%;
  height: 40px;
  line-height: 40px;
  text-align: center;
}
</style>
