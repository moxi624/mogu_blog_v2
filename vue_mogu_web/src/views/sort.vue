<template>
  <div>
    <div class="pagebg sorts"></div>
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
                <span
                  @click="clickTime(activity.content)"
                  :class="[activity.content == selectContent ? 'sortBoxSpan sortBoxSpanSelect' : 'sortBoxSpan']"
                >{{activity.content}}</span>
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
                  <h4 @click="goToList('blogContent', item)" class="itemTitle">{{item.title}}</h4>
                  <br>
                  <el-tag class="elTag" v-if="item.isOriginal==1" type="danger">原创</el-tag>
                  <el-tag class="elTag" v-if="item.isOriginal==0" type="info">转载</el-tag>

                  <el-tag
                    class="elTag"
                    v-if="item.author"
                    @click="goToList('author', item)"
                  >{{item.author}}</el-tag>

                  <el-tag
                    class="elTag"
                    type="success"
                    v-if="item.blogSort != null"
                    @click="goToList('blogSort', item.blogSort)"
                  >{{item.blogSort.sortName}}</el-tag>
                  <el-tag
                    class="elTag"
                    v-for="tagItem in item.tagList"
                    v-if="tagItem != null"
                    :key="tagItem.uid"
                    @click="goToList('tag', tagItem)"
                    type="warning"
                  >{{tagItem.content}}</el-tag>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getSortList, getArticleByMonth } from "../api/sort";
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
  },
  mounted() {},
  created() {
    var that = this;
    getSortList().then(response => {
      if (response.code == this.$ECode.SUCCESS) {
        var activities = response.data;
        var result = [];
        for (var a = 0; a < activities.length; a++) {
          var temp = activities[a].replace("年", "-").replace("月", "-") + "1";
          var dataForDate = { content: activities[a], timestamp: temp };
          result.push(dataForDate);
        }
        this.activities = result;
        // 默认选择最后一个
        this.clickTime(activities[activities.length - 1]);
      }
    });

  },
  methods: {
    clickTime(content) {
      this.selectContent = content;
      var params = new URLSearchParams();
      params.append("monthDate", content);
      getArticleByMonth(params).then(response => {
        if (response.code == this.$ECode.SUCCESS) {
          this.itemByDate = response.data;
        }
      });
    },
    //跳转到搜索详情页
    goToList(type, entity) {
      switch (type) {
        case "tag":
        {
          // 标签uid
          let routeData = this.$router.resolve({
            path: "/list",
            query: { tagUid: entity.uid }
          });
          window.open(routeData.href, "_blank");
        }
          break;
        case "blogSort":
        {
          let routeData = this.$router.resolve({
            path: "/list",
            query: { sortUid: entity.blogSort.uid }
          });
          window.open(routeData.href, "_blank");
        }
          break;
        case "author":
        {
          let routeData = this.$router.resolve({
            path: "/list",
            query: { author: entity.author }
          });
          window.open(routeData.href, "_blank");
        }
          break;

        case "blogContent":
        {
          if(entity.type == "0") {
            let routeData = this.$router.resolve({
              path: "/info",
              query: { blogOid: entity.oid }
            });
            window.open(routeData.href, "_blank");
          } else if(entity.type == "1") {
            window.open(entity.outsideLink, '_blank');
          }
        }
          break;
      }
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

.sortBoxSpan {
  cursor: pointer;
}
.sortBoxSpan:hover {
  color: #409eff;
}
.sortBoxSpanSelect {
  color: #409eff;
}

.itemTitle {
  cursor: pointer;
}
.itemTitle:hover {
  color: #409eff;
}
.elTag {
  cursor: pointer;
}
</style>
