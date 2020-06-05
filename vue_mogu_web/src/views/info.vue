<template>
  <article>
    <el-dialog :visible.sync="dialogPictureVisible" fullscreen>
      <img :src="dialogImageUrl" alt="dialogImageUrl" style="margin: 0 auto;" />
    </el-dialog>
    <h1 class="t_nav">
      <a href="/" class="n1">网站首页</a>
      <a
        href="javascript:void(0);"
        v-if="blogData.blogSort.uid"
        @click="goToSortList(blogData.blogSort.uid)"
        class="n2"
      >{{blogData.blogSort ? blogData.blogSort.sortName:""}}</a>
    </h1>
    <div class="infosbox">
      <div class="newsview">
        <h3 class="news_title" v-if="blogData.title">{{blogData.title}}</h3>
        <div class="bloginfo" v-if="blogData.blogSort.uid">
          <ul>
            <li class="author">
              <span class="iconfont">&#xe60f;</span>
              <a href="javascript:void(0);" @click="goToAuthor(blogData.author)">{{blogData.author}}</a>
            </li>
            <li class="lmname">
              <span class="iconfont">&#xe603;</span>
              <a
                href="javascript:void(0);"
                @click="goToSortList(blogData.blogSort.uid)"
              >{{blogData.blogSort ? blogData.blogSort.sortName:""}}</a>
            </li>
            <li class="createTime">
              <span class="iconfont">&#xe606;</span>
              {{blogData.createTime}}
            </li>
            <li class="view">
              <span class="iconfont">&#xe8c7;</span>
              {{blogData.clickCount}}
            </li>
            <li class="like">
              <span class="iconfont">&#xe663;</span>
              {{blogData.collectCount}}
            </li>
          </ul>
        </div>
        <div class="tags">
          <a
            v-if="blogData.tagList"
            v-for="item in blogData.tagList"
            :key="item.uid"
            href="javascript:void(0);"
            @click="goToList(item.uid)"
            target="_blank"
          >{{item.content}}</a>
        </div>
        <div class="news_about">
          <strong>版权</strong>
          {{blogData.copyright}}
        </div>
        <div
          class="news_con ck-content"
          v-html="blogData.content"
          v-highlight
          @click="imageChange"
        >{{blogData.content}}</div>
      </div>

      <!--付款码和点赞-->
      <PayCode :blogUid="blogUid" :praiseCount="blogData.collectCount"></PayCode>

      <div class="otherlink" v-if="sameBlogData.length > 0">
        <h2>相关文章</h2>
        <ul>
          <li v-for="item in sameBlogData" :key="item.uid">
            <a
              href="javascript:void(0);"
              @click="goToInfo(item.uid)"
              title="item.title"
            >{{subText(item.title, 18)}}</a>
          </li>
        </ul>
      </div>
      <div class="news_pl">
        <h2>文章评论</h2>
        <ul>
            <CommentBox
              :userInfo="userInfo"
              :commentInfo="commentInfo"
              @submit-box="submitBox"
              :showCancel="showCancel"
            ></CommentBox>
          <div class="message_infos">
            <CommentList :comments="comments" :commentInfo="commentInfo"></CommentList>
            <div class="noComment" v-if="comments.length ==0">还没有评论，快来抢沙发吧！</div>
          </div>
        </ul>
      </div>
    </div>
    <div class="sidebar2">

      <side-catalog
        ref="catalog"
        class="side-catalog"
        v-bind="catalogProps"
        :catalogSum = "catalogSum"
        v-if="showSideCatalog"
      >
        <span v-if="catalogSum > 0" style="font-size: 16px;font-weight: bold;">目录</span>
      </side-catalog>

      <sticky :sticky-top="stickyTop" v-if="!showSideCatalog">
        <side-catalog
          ref="catalog"
          class="side-catalog"
          v-bind="catalogProps"
          :catalogSum = "catalogSum"
        >
          <span v-if="catalogSum > 0" style="font-size: 16px;font-weight: bold;">目录</span>
        </side-catalog>
      </sticky>
    </div>

  </article>
</template>

<script>
    import { getLink } from "../api/index";
    import { getBlogByUid, getSameBlogByBlogUid } from "../api/blogContent";

    import CommentList from "../components/CommentList";
    import CommentBox from "../components/CommentBox";
    // vuex中有mapState方法，相当于我们能够使用它的getset方法
    import { mapMutations } from "vuex";
    import ThirdRecommend from "../components/ThirdRecommend";
    import FourthRecommend from "../components/FourthRecommend";
    import TagCloud from "../components/TagCloud";
    import HotBlog from "../components/HotBlog";
    import FollowUs from "../components/FollowUs";
    import PayCode from "../components/PayCode";
    import Link from "../components/Link";
    import { addComment, getCommentList } from "../api/comment";
    import { Loading } from "element-ui";
    import SideCatalog from "../components/VueSideCatalog"
    import Sticky from "@/components/Sticky";

    export default {
        name: "info",
        data() {
            return {
                // 目录列表数
                catalogSum: 0,
                stickyTop: 10,
                showSideCatalog: true,
                catalogProps: {
                  // 内容容器selector(必需)
                  containerElementSelector: '.ck-content',
                  openDomWatch: true,
                },
                loadingInstance: null, // loading对象
                showCancel: false,
                submitting: false,
                comments: [],
                commentInfo: {
                    // 评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等 代表来自某些页面的评论
                    source: "BLOG_INFO",
                    blogUid: this.$route.query.blogUid
                },
                currentPage: 1,
                pageSize: 10,
                total: 0, //总数量
                toInfo: {},
                userInfo: {},
                blogUid: null, //传递过来的博客uid
                blogData: {
                  blogSort: {}
                },
                sameBlogData: [], //相关文章
                linkData: [], //友情链接
                dialogPictureVisible: false,
                dialogImageUrl: ""
            };
        },
        components: {
            //注册组件
            FourthRecommend,
            ThirdRecommend,
            TagCloud,
            HotBlog,
            FollowUs,
            PayCode,
            CommentList,
            CommentBox,
            SideCatalog,
            Link,
            Sticky
        },
        mounted () {
          var that = this;
          var params = new URLSearchParams();
          params.append("uid", this.blogUid);
          getBlogByUid(params).then(response => {
            if (response.code == "success") {
              this.blogData = response.data;
            }
            this.loadingInstance.close();
          });

          var offset = 200;
          var after = 0;
          $(window).scroll(function () {
            var docHeight = $(document).height(); // 获取整个页面的高度(不只是窗口,还包括为显示的页面)
            var winHeight = $(window).height(); // 获取当前窗体的高度(显示的高度)
            var winScrollHeight = $(window).scrollTop(); // 获取滚动条滚动的距离(移动距离)

            if (winScrollHeight < offset) {
              console.log("当前值", winScrollHeight)
              that.showSideCatalog = true
            } else {
              console.log("当前值", winScrollHeight)
              that.showSideCatalog = false
            }
            if (winScrollHeight > after) {
              // console.log("隐藏顶部栏", winScrollHeight)
              that.stickyTop = 10
            } else {
              // console.log("显示顶部栏", winScrollHeight)
              that.stickyTop = 70
            }
            after = winScrollHeight;


            //还有30像素的时候,就查询
            if(docHeight == winHeight + winScrollHeight){
              if(that.comments.length >= that.total) {
                console.log('已经到底了')
                return;
              }
              let params = {};
              params.source = that.commentInfo.source;
              params.currentPage = that.currentPage + 1
              params.pageSize = that.pageSize;
              getCommentList(params).then(response => {
                if (response.code == "success") {
                  that.comments = that.comments.concat(response.data.records);
                  that.setCommentList(this.comments);
                  that.currentPage = response.data.current;
                  that.pageSize = response.data.size;
                  that.total = response.data.total;
                }
              });
            }
          })
        },
        created() {
            this.loadingInstance = Loading.service({
                fullscreen: true,
                text: "正在努力加载中~"
            });
            getLink().then(response => {
                this.linkData = response.data.records;
            });
            // var params = new URLSearchParams();
            this.blogUid = this.$route.query.blogUid;
            this.commentInfo.blogUid = this.$route.query.blogUid;
            var blogParams = new URLSearchParams();
            blogParams.append("blogUid", this.blogUid);
            getSameBlogByBlogUid(blogParams).then(response => {
                if (response.code == "success") {
                    this.sameBlogData = response.data.records;
                }
            });
            this.getCommentDataList();
        },
        methods: {
            //拿到vuex中的写的两个方法
            ...mapMutations(["setCommentList"]),
            handleCurrentChange: function(val) {
                this.currentPage = val;
                this.getCommentDataList();
            },
            submitBox(e) {
                let params = {};
                params.blogUid = e.blogUid;
                params.source = e.source;
                params.userUid = e.userUid;
                params.content = e.content;
                params.blogUid = e.blogUid;
                addComment(params).then(response => {
                    if (response.code == "success") {
                        this.$notify({
                            title: "成功",
                            message: "发表成功~",
                            type: "success",
                            offset: 100
                        });
                    } else {
                        this.$notify.error({
                            title: "错误",
                            message: response.data,
                            offset: 100
                        });
                    }
                    this.getCommentDataList();
                });
            },
            getCommentDataList: function() {
                let params = {};
                params.source = this.commentInfo.source;
                params.blogUid = this.commentInfo.blogUid;
                params.currentPage = this.currentPage;
                params.pageSize = this.pageSize;
                getCommentList(params).then(response => {
                    if (response.code == "success") {
                        this.comments = response.data.records;
                        this.setCommentList(this.comments);
                        this.currentPage = response.data.current;
                        this.pageSize = response.data.size;
                        this.total = response.data.total
                    }
                });
            },
            //跳转到文章详情
            goToInfo(uid) {
                let routeData = this.$router.resolve({
                    path: "/info",
                    query: { blogUid: uid }
                });
                window.open(routeData.href, "_blank");
            },
            //跳转到搜索详情页
            goToList(uid) {
                let routeData = this.$router.resolve({
                    path: "/list",
                    query: { tagUid: uid }
                });
                window.open(routeData.href, "_blank");
            },
            //跳转到搜索详情页
            goToSortList(uid) {
                let routeData = this.$router.resolve({
                    path: "/list",
                    query: { sortUid: uid }
                });
                window.open(routeData.href, "_blank");
            },
            //跳转到搜索详情页
            goToAuthor(author) {
                let routeData = this.$router.resolve({
                    path: "/list",
                    query: { author: author }
                });
                window.open(routeData.href, "_blank");
            },

            imageChange: function(e) {
                //首先需要判断点击的是否是图片
                var type = e.target.localName;
                if (type == "img") {
                    // window.open(e.target.currentSrc);
                    this.dialogPictureVisible = true;
                    this.dialogImageUrl = e.target.currentSrc;
                }
            },
            //切割字符串
            subText: function(str, index) {
                if (str.length < index) {
                    return str;
                }
                return str.substring(0, index) + "...";
            }
        }
    };
</script>

<style>
  .emoji-panel-wrap {
    box-sizing: border-box;
    border: 1px solid #cccccc;
    border-radius: 5px;
    background-color: #ffffff;
    width: 470px;
    height: 190px;
    position: absolute;
    z-index: 999;
    left: 35px;
    top: 10px;
  }
  .emoji-size-small {
    zoom: 0.3;
    margin: 5px;
  }
  .emoji-size-large {
    zoom: 0.5; // emojipanel表情大小
    margin: 5px;
  }
  .iconfont {
    font-size: 14px;
    margin-right: 3px;
  }
  .message_infos {
    width: 96%;
    /*min-height: 500px;*/
    margin-left: 10px;
  }
  .noComment {
    width: 100%;
    text-align: center;
  }
</style>
