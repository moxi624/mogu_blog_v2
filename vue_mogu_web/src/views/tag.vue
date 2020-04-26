<template>
  <div>
    <div class="pagebg tagPage"></div>
    <div class="container">
      <h1 class="t_nav">
        <span>内心没有阳光的人温暖不了别人。用心写博客，知识不是高谈阔论，是现实点点滴滴的积累。</span>
        <a href="/" class="n1">网站首页</a>
        <a href="javascript:void(0);" class="n2">标签</a>
      </h1>

      <div class="sortBox">
        <div class="time">
          <div class="block">
            <div class="radio" style="margin-bottom:20px;"></div>
            <el-timeline :reverse="reverse">
              <el-timeline-item v-for="(activity, index) in activities" hide-timestamp :key="index">
                <span
                  @click="getBlogTagList(activity.uid)"
                  :class="[activity.uid == selectBlogUid ? 'sortBoxSpan sortBoxSpanSelect' : 'sortBoxSpan']"
                >{{activity.tagName}}</span>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>

        <div class="article">
          <div class="block" v-infinite-scroll="load">
            <el-timeline>
              <el-timeline-item
                v-for="item in itemByDate"
                :key="item.uid"
                :timestamp="item.createTime"
                placement="top"
              >
                <el-card>
                  <h4 @click="goToList('blogContent', item.uid)" class="itemTitle">{{item.title}}</h4>
                  <br>
                  <el-tag class="elTag" v-if="item.isOriginal==1" type="danger">原创</el-tag>
                  <el-tag class="elTag" v-if="item.isOriginal==0" type="info">转载</el-tag>

                  <el-tag
                    class="elTag"
                    v-if="item.author"
                    @click="goToList('author', item.author)"
                  >{{item.author}}</el-tag>

                  <el-tag
                    class="elTag"
                    type="success"
                    v-if="item.blogSort != null"
                    @click="goToList('blogSort', item.blogSort.uid)"
                  >{{item.blogSort.sortName}}</el-tag>
                  <el-tag
                    class="elTag"
                    v-for="tagItem in item.tagList"
                    v-if="tagItem != null"
                    :key="tagItem.uid"
                    @click="goToList('tag', tagItem.uid)"
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
  import { getTagList, getArticleByTagUid } from "../api/tag";
  export default {
    data() {
      return {
        selectBlogUid: "",
        reverse: false,
        activities: [],
        itemByDate: [],
        articleByDate: {},
        count: 0,
        currentPage: 1,
        pageSize: 10
      };
    },
    components: {
      //注册组件
    },
    mounted() { },
    created() {
      var that = this;
      getTagList().then(response => {
        if (response.code == "success") {
          var activities = response.data;
          var result = [];
          for (var a = 0; a < activities.length; a++) {
            var dataForDate = {
              tagName: activities[a].content,
              uid: activities[a].uid
            };
            result.push(dataForDate);
          }
          this.activities = result;

          // 默认选择第一个
          this.getBlogTagList(activities[0].uid);
        }
      });

    },
    methods: {
      getBlogTagList(tagUid) {
        this.selectBlogUid = tagUid;
        var params = new URLSearchParams();
        params.append("tagUid", tagUid);
        getArticleByTagUid(params).then(response => {
          if (response.code == "success") {
            this.itemByDate = response.data.records;
            this.currentPage = response.data.current;
            this.pageSize = response.data.size;
          }
        });
      },
      load() {
        var params = new URLSearchParams();
        if (
          this.selectBlogUid == null ||
          this.selectBlogUid == "" ||
          this.selectBlogUid == undefined
        ) {
          return;
        }
        params.append("tagUid", this.selectBlogUid);
        params.append("currentPage", this.currentPage + 1);
        getArticleByTagUid(params).then(response => {
          if (response.code == "success") {
            this.itemByDate = this.itemByDate.concat(response.data.records);
            this.currentPage = response.data.current;
            this.pageSize = response.data.size;
          }
        });
      },
      //跳转到搜索详情页
      goToList(type, uid) {
        switch (type) {
          case "tag":
          {
            let routeData = this.$router.resolve({
              path: "/list",
              query: { tagUid: uid }
            });
            window.open(routeData.href, "_blank");
          }
            break;
          case "blogSort":
          {
            let routeData = this.$router.resolve({
              path: "/list",
              query: { sortUid: uid }
            });
            window.open(routeData.href, "_blank");
          }
            break;
          case "author":
          {
            let routeData = this.$router.resolve({
              path: "/list",
              query: { author: uid }
            });
            window.open(routeData.href, "_blank");
          }
            break;

          case "blogContent":
          {
            let routeData = this.$router.resolve({
              path: "/info",
              query: { blogUid: uid }
            });
            window.open(routeData.href, "_blank");
          }
            break;
        }
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
