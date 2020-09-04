<template>
  <el-drawer
    title="我是标题"
    :visible.sync="drawer"
    @close="beforeClose"
    direction="ltr"
    :with-header="false">

    <div class="block" style="margin:10px;">
      <div style="text-align: center;font-size: 16px;" v-if="subjectItemlist.length == 0">空空如也</div>
      <el-timeline>
        <div class="blogsbox">
          <div
            v-for="item in subjectItemlist"
            :key="item.uid"
            class="blogs"
          >
            <el-timeline-item :timestamp="item.createTime" placement="top">
              <el-card>
                <span class="blogpic" @click="goToInfo(item.blog.uid)">
                  <a href="javascript:void(0);" title>
                    <img v-if="item.blog && item.blog.photoList.length > 0" :src="item.blog.photoList[0]" alt>
                  </a>
                </span>
                <p class="blogtext" style="font-weight: bold; cursor: pointer;" @click="goToInfo(item.blog.uid)">{{item.blog.title}}</p>
                <div class="bloginfo">
                  <ul>
                    <li class="author">
                      <span class="iconfont">&#xe60f;</span>
                      <a href="javascript:void(0);" @click="goToAuthor(item.blog.author)">{{item.blog.author}}</a>
                    </li>
                    <li class="lmname" v-if="item.blog.blogSort">
                      <span class="iconfont">&#xe603;</span>
                      <a
                        href="javascript:void(0);"
                        @click="goToList(item.blog.blogSort.uid)"
                      >{{item.blog.blogSort.sortName}}</a>
                    </li>
                    <li class="view">
                      <span class="iconfont">&#xe8c7;</span>
                      <span>{{item.blog.clickCount}}</span>
                    </li>
                    <li class="like">
                      <span class="iconfont">&#xe663;</span>
                      {{item.blog.collectCount}}
                    </li>
                  </ul>
                </div>
              </el-card>
            </el-timeline-item>
          </div>
        </div>
      </el-timeline>
    </div>

  </el-drawer>
</template>

<script>
  import {getSubjectItemList} from "../../api/subject";
    export default {
      name: "SubjectItemList",
      props: ["visiable", "subjectUid"],
      watch: {
        visiable: function() {
          this.drawer = this.visiable;
        },
        subjectUid: function () {
          this.currentPage = 1
          this.subjectItemlist = []
          this.getList()
        }
      },
      data() {
        return {
          drawer: this.visiable,
          subjectItemlist: [],
          pageSize: 50,
          currentPage: 1,
          total: 0,
        };
      },
      created() {
        // this.getList()
      },
      methods: {
        getList() {
          var params = {};
          params.subjectUid = this.subjectUid;
          params.pageSize = this.pageSize;
          params.currentPage = this.currentPage;
          getSubjectItemList(params).then(response => {
            if(response.code == this.$ECode.SUCCESS) {
              let itemList = response.data.records
              let oldItemList = this.subjectItemlist
              this.currentPage = response.data.current
              this.total = response.data.total
              this.subjectItemlist = oldItemList.concat(itemList);
            }
          })
        },
        load() {
          // console.log("加载")
          // this.currentPage = this.currentPage + 1
          // this.getList()
        },
        beforeClose() {
          //取消时，关闭侧边栏
          this.$emit("close", "");
        },
        //跳转到文章详情
        goToInfo(uid) {
          console.log("传递过来的uid", uid)
          let routeData = this.$router.resolve({
            path: "/info",
            query: {blogUid: uid}
          });
          window.open(routeData.href, '_blank');
        },
        //跳转到搜索详情页
        goToList(uid) {
          let routeData = this.$router.push({
            path: "/list",
            query: {sortUid: uid}
          });

        },
        //跳转到搜索详情页
        goToAuthor(author) {
          let routeData = this.$router.push({
            path: "/list",
            query: {author: author}
          });
        },
      }
    }
</script>

<style scoped>

  .blogsbox {
    width: 100%;
    height: 900px;
    overflow-y: scroll;
  }
  .blogs {
    margin-bottom: 0px;
    padding: 20px;
  }
  .blogs .blogtext {
    margin-top: 0px;
  }
</style>
