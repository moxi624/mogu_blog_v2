<template>
  <div>
    <div class="pagebg sh"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>好咖啡要和朋友一起品尝，好“资料”也要和同样喜欢它的人一起分享。</span>
        <a href="/" class="n1">网站首页</a>
        <a href="/" class="n2">学习教程</a>
      </h1>

      <div class="share">
        <ul>
          <li v-for="item in studyVideoData" :key="item.uid">
            <div class="shareli">
              <a href="/" target="_blank">
                <i>
                  <img class="resImg" :src="item.photoList[0]">
                </i>
                <h2>
                  <b>{{item.name}}</b>
                </h2>
                <span>{{item.resourceSort.sortName}}</span>
              </a>
            </div>
          </li>
        </ul>
      </div>

      <div class="pagelist">
        <a title="Total record">
          &nbsp;
          <b>45</b>
        </a>&nbsp;&nbsp;&nbsp;
        <b>1</b>&nbsp;
        <a href="/download/index_2.html">2</a>&nbsp;
        <a href="/download/index_2.html">下一页</a>&nbsp;
        <a href="/download/index_2.html">尾页</a>
      </div>
    </div>
  </div>
</template>

<script>
import { getStudyVideoBySort } from "../api/resource";

export default {
  name: "share",
  data() {
    return {
      studyVideoData: [],
      currentPage: 1,
      pageSize: 8,
      total: 0 //总数量
    };
  },
  components: {
    //注册组件
  },
  created() {
    getStudyVideoBySort().then(response => {
      if (response.code == this.$ECode.SUCCESS) {
        this.studyVideoData = response.data.records;
        this.total = response.data.total;
        this.pageSize = response.data.size;
        this.currentPage = response.data.current;
      }
    });

  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.resImg {
  max-height: 100%;
  max-width: 100%;
  vertical-align: middle;
}
</style>
