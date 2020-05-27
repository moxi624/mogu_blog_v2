<template>
  <div class="dashboard-editor-container">
    <!-- 右上角github图标 -->
    <el-tooltip content="Github源码" effect="dark" placement="bottom">
      <github-corner style="position: absolute; top: 0px; border: 0; right: 0;"></github-corner>
    </el-tooltip>

    <!-- 顶部内容 -->
    <el-row class="panel-group" :gutter="40">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-money" @click="btnClick('1')">
            <svg-icon icon-class="eye" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">今日IP数：</div>
            <count-to class="card-panel-num" :startVal="0" :endVal="visitAddTotal" :duration="3200"></count-to>
          </div>
        </div>
      </el-col>

      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-people" @click="btnClick('2')">
            <svg-icon icon-class="peoples" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">用户数:</div>
            <count-to class="card-panel-num" :startVal="0" :endVal="userTotal" :duration="2600"></count-to>
          </div>
        </div>
      </el-col>

      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-message" @click="btnClick('3')">
            <svg-icon icon-class="message" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">评论数：</div>
            <count-to class="card-panel-num" :startVal="0" :endVal="commentTotal" :duration="3000"></count-to>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-shoppingCard" @click="btnClick('4')">
            <svg-icon icon-class="form" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">文章数:</div>
            <count-to class="card-panel-num" :startVal="0" :endVal="blogTotal" :duration="3600"></count-to>
          </div>
        </div>
      </el-col>
    </el-row>

    <!--文章贡献度-->
    <el-row>
      <CalendarChart></CalendarChart>
    </el-row>

    <!-- 分类图-->
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart
            ref="blogSortPie"
            @clickPie="clickBlogSortPie"
            v-if="showPieBlogSortChart"
            :value="blogCountByBlogSort"
            :tagName="blogSortNameArray"
          ></pie-chart>
        </div>
      </el-col>

      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart
            v-if="showPieChart"
            @clickPie="clickBlogTagPie"
            :value="blogCountByTag"
            :tagName="tagNameArray"
          ></pie-chart>
        </div>
      </el-col>

      <el-col
        :xs="{span: 24}"
        :sm="{span: 12}"
        :md="{span: 12}"
        :lg="{span: 6}"
        :xl="{span: 6}"
        style="margin-bottom:30px;"
      >
        <div class="chart-wrapper" v-permission="'/todo/getList'">
          <todo-list></todo-list>
        </div>
      </el-col>
    </el-row>

    <!--访问量统计-->
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart v-if="showLineChart" :chart-data="lineChartData"></line-chart>
    </el-row>

  </div>
</template>

<script>
import { mapGetters } from "vuex";
import CountTo from "vue-count-to";
import {
  init,
  getVisitByWeek,
  getBlogCountByTag,
  getBlogCountByBlogSort
} from "@/api/index";
import GithubCorner from "@/components/GithubCorner";
import PieChart from "@/components/PieChart";
import TodoList from "@/components/TodoList";
import BarChart from "@/components/BarChart";
import LineChart from "@/components/LineChart";
import CalendarChart from "@/components/CalendarChart";

export default {
  name: "dashboard",
  computed: {
    ...mapGetters(["name", "roles"])
  },
  components: {
    GithubCorner,
    PieChart,
    TodoList,
    BarChart,
    CountTo,
    LineChart,
    CalendarChart
  },
  data() {
    return {
      visitAddTotal: 0,
      userTotal: 0,
      commentTotal: 0,
      blogTotal: 0,
      showPieChart: false,
      showPieBlogSortChart: false,
      showLineChart: false,
      blogCountByTag: [],
      blogCountByBlogSort: [],
      tagNameArray: [],
      blogSortNameArray: [],
      lineChartData: {}
    };
  },
  created() {
    init().then(response => {
      console.log(response);
      if (response.code == "success") {
        this.blogTotal = response.data.blogCount;
        this.commentTotal = response.data.commentCount;
        this.userTotal = response.data.userCount;
        this.visitAddTotal = response.data.visitCount;
      }
    });

    getVisitByWeek().then(response => {
      if (response.code == "success") {
        var visitByWeek = response.data;
        var lineChartData = {
          date: visitByWeek.date,
          expectedData: visitByWeek.pv,
          actualData: visitByWeek.uv
        };
        this.lineChartData = lineChartData;
        this.showLineChart = true;
      }
    });

    //通过标签获取博客数目
    getBlogCountByTag().then(response => {
      if (response.code == "success") {
        this.blogCountByTag = response.data;

        var tagList = this.blogCountByTag;

        for (var a = 0; a < this.blogCountByTag.length; a++) {
          this.tagNameArray.push(tagList[a].name);
        }

        this.showPieChart = true;
      }
    });

    //通过博客分类获取博客数目
    getBlogCountByBlogSort().then(response => {
      if (response.code == "success") {
        this.blogCountByBlogSort = response.data;

        var blogSortList = this.blogCountByBlogSort;

        for (var a = 0; a < this.blogCountByBlogSort.length; a++) {
          this.blogSortNameArray.push(blogSortList[a].name);
        }

        this.showPieBlogSortChart = true;
      }
    });
  },
  methods: {
    clickBlogTagPie: function(index) {
      var tag = this.blogCountByTag[index];
      this.$router.push({
        path: "/blog/blog",
        query: { tag: tag }
      });
    },
    clickBlogSortPie: function(index) {
      var blogSort = this.blogCountByBlogSort[index];

      this.$router.push({
        path: "/blog/blog",
        query: { blogSort: blogSort }
      });

    },
    btnClick: function(type) {
      switch (type) {
        case "1":
          {
            this.$router.push({ path: "/log/webVisit" });
          }
          break;
        case "2":
          {
            this.$router.push({ path: "/user/user" });
          }
          break;
        case "3":
          {
            this.$router.push({ path: "/message/comment" });
          }
          break;
        case "4":
          {
            this.$router.push({ path: "/blog/blog" });
          }
          break;
      }
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}
.btn {
  width: 80px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 14px;
  display: inline-block;
  white-space: nowrap;
  cursor: pointer;
  background: #fff;
  border: 1px solid #dcdfe6;
  color: #606266;
}

.btnClick {
  color: #409eff;
  border-color: #c6e2ff;
  background-color: #ecf5ff;
}

.btn:hover {
  color: #409eff;
  border-color: #c6e2ff;
  background-color: #ecf5ff;
}

.statistics-item {
  width: 600px;
  height: 400px;
  float: left;
  margin: 20px auto;
}

.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
    border-color: rgba(0, 0, 0, 0.05);
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-people {
        background: #40c9c6;
      }
      .icon-message {
        background: #36a3f7;
      }
      .icon-money {
        background: #f4516c;
      }
      .icon-shoppingCard {
        background: #34bfa3;
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-message {
      color: #36a3f7;
    }
    .icon-money {
      color: #f4516c;
    }
    .icon-shoppingCard {
      color: #34bfa3;
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px 70px 0 0;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}
</style>
