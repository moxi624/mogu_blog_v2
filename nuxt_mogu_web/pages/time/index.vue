<template>
  <div>
    <div class="pagebg timer"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>时光飞逝，机会就在我们眼前，何时找到了灵感，就要把握机遇，我们需要智慧和勇气去把握机会。</span>
        <a href="/home" class="n1">网站首页</a>
        <a href="javascript:void(0);" class="n2">时间轴</a>
      </h1>
      <div class="timebox">
        <ul id="list" style>
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
      <div class="isEnd">
        <span v-if="!isEnd">正在加载中~</span>
        <span v-else>我也是有底线的~</span>
      </div>
    </div>
  </div>
</template>

<script>
import { getBlogByTime, recorderVisitPage } from "~/api/index";
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

    window.addEventListener("scroll", function() {
      let scrollTop = document.documentElement.scrollTop; //当前的的位置
      let scrollHeight = document.documentElement.scrollHeight; //最高的位置
      if (scrollTop >= 0.25 * scrollHeight && !that.isEnd && !loading) {
        loading = true;
        that.currentPage = that.currentPage + 1;
        var params = new URLSearchParams();
        params.append("currentPage", that.currentPage);
        params.append("pageSize", that.pageSize);
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
      }
    });
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
    var params = new URLSearchParams();
    params.append("pageName", "TIME");
    recorderVisitPage(params).then(response => {});
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
