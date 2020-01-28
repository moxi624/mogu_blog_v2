<template>
  <article>
    <el-dialog :visible.sync="dialogPictureVisible" width="80%">
      <img width="100%" :src="dialogImageUrl" alt="dialogImageUrl">
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
            <li class="createTime"><span class="iconfont">&#xe606;</span>{{blogData.createTime}}</li>
            <li class="view"><span class="iconfont">&#xe8c7;</span>{{blogData.clickCount}}</li>
            <li class="like"><span class="iconfont">&#xe663;</span>{{blogData.collectCount}}</li>
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
          class="news_con fixck"
          v-html="blogData.content"
          v-highlight
          @click="imageChange"
        >{{blogData.content}}
        </div>
      </div>

      <!--付款码和点赞-->
      <PayCode :blogUid="blogUid"></PayCode>

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
          <sticky :sticky-top="60">
            <CommentBox :userInfo="userInfo" :commentInfo="commentInfo" @submit-box="submitBox"
                        :showCancel="showCancel" ></CommentBox>
          </sticky>
          <div class="message_infos">
            <CommentList :comments="comments" :commentInfo="commentInfo"></CommentList>
            <div class="noComment" v-if="comments.length ==0">
              还没有评论，快来抢沙发吧！
            </div>
          </div>

        </ul>
      </div>
    </div>
    <div class="sidebar">

      <!-- 三级推荐 -->
      <ThirdRecommend></ThirdRecommend>

      <!--标签云-->
      <TagCloud></TagCloud>

      <!--四级推荐-->
      <FourthRecommend></FourthRecommend>

      <!--点击排行-->
      <HotBlog></HotBlog>

      <div class="links">
        <h2 class="hometitle">友情链接</h2>
        <ul>
          <li v-for="item in linkData" :key="item.uid">
            <a :href="item.url" target="_blank" v-if="item.title">{{item.title}}</a>
          </li>
        </ul>
      </div>

      <!--关注我们-->
      <FollowUs></FollowUs>
    </div>
  </article>
</template>

<script>
  import {getLink, recorderVisitPage} from "../api/index";
  import {getBlogByUid, getSameBlogByBlogUid} from "../api/blogContent";

  import CommentList from "../components/CommentList";
  import CommentBox from "../components/CommentBox";

  // vuex中有mapState方法，相当于我们能够使用它的getset方法
  import {mapMutations} from 'vuex';
  import ThirdRecommend from "../components/ThirdRecommend";
  import FourthRecommend from "../components/FourthRecommend";
  import TagCloud from "../components/TagCloud";
  import HotBlog from "../components/HotBlog";
  import FollowUs from "../components/FollowUs";
  import PayCode from "../components/PayCode";
  import Sticky from '@/components/Sticky'

  import {addComment, getCommentList} from "../api/comment";

  export default {
    name: "info",
    data() {
      return {
        source: "MESSAGE_BOARD",
        showCancel: false,
        submitting: false,
        comments: [],
        commentInfo: {
          // 评论来源： MESSAGE_BOARD，ABOUT，BLOG_INFO 等 代表来自某些页面的评论
          source: "BLOG_INFO",
          blogUid: this.$route.query.blogUid
        },
        toInfo: {},
        userInfo: {},
        blogUid: null, //传递过来的博客uid
        blogData: null,
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
      Sticky
    },
    created() {
      getLink().then(response => {
        this.linkData = response.data.records;
      });

      var params = new URLSearchParams();

      this.blogUid = this.$route.query.blogUid;

      this.commentInfo.blogUid = this.$route.query.blogUid;

      params.append("uid", this.blogUid);
      getBlogByUid(params).then(response => {
        if (response.code == "success") {
          console.log("得到的评论", response.data)
          this.blogData = response.data;
        }
      });

      var blogParams = new URLSearchParams();
      blogParams.append("blogUid", this.blogUid);
      getSameBlogByBlogUid(blogParams).then(response => {
        if (response.code == "success") {
          this.sameBlogData = response.data.records;
        }
      });

      var params = new URLSearchParams();
      params.append("pageName", "INFO");
      recorderVisitPage(params).then(response => {
      });

      this.getCommentList();
    },
    methods: {
      //拿到vuex中的写的两个方法
      ...mapMutations(['setCommentList']),
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
                title: '成功',
                message: "发表成功~",
                type: 'success',
                offset: 100
              });
            } else {
              this.$notify.error({
                title: '错误',
                message: response.data,
                offset: 100
              });
            }
            this.getCommentList();
          }
        );
      },
      getCommentList: function () {
        let params = {};
        params.source = this.commentInfo.source;
        params.blogUid = this.commentInfo.blogUid;
        params.currentPage = 0;
        params.pageSize = 10;
        getCommentList(params).then(response => {
          if (response.code == "success") {
            this.comments = response.data;
            this.setCommentList(this.comments);
          }
        });
      },
      //跳转到文章详情
      goToInfo(uid) {
        let routeData = this.$router.resolve({
          path: "/info",
          query: {blogUid: uid}
        });
        window.open(routeData.href, "_blank");
      },
      //跳转到搜索详情页
      goToList(uid) {
        let routeData = this.$router.resolve({
          path: "/list",
          query: {tagUid: uid}
        });
        window.open(routeData.href, "_blank");
      },
      //跳转到搜索详情页
      goToSortList(uid) {
        let routeData = this.$router.resolve({
          path: "/list",
          query: {sortUid: uid}
        });
        window.open(routeData.href, "_blank");
      },
      //跳转到搜索详情页
      goToAuthor(author) {
        let routeData = this.$router.resolve({
          path: "/list",
          query: {author: author}
        });
        window.open(routeData.href, "_blank");
      },

      imageChange: function (e) {
        //首先需要判断点击的是否是图片
        var type = e.target.localName;
        if (type == "img") {
          // window.open(e.target.currentSrc);
          this.dialogPictureVisible = true
          this.dialogImageUrl = e.target.currentSrc
        }
      },
      //切割字符串
      subText: function (str, index) {
        if (str.length < index) {
          return str;
        }
        return str.substring(0, index) + "...";
      }
    }
  };
</script>

<style>

  .fixck {
    /* font-family: Arial, Verdana, sans-serif !important;
    font-size: 12px !important;
    color: #222 !important;
    line-height: normal !important; */
  }

  .fixck p {
    margin: 12px 0 !important;
  }

  .fixck a {
    text-decoration: underline !important;
    color: #00e !important;
  }

  .fixck ul li {
    list-style: disc;
  }

  .fixck ol li {
    list-style: decimal;
  }

  .fixck ul,
  .fixck ol {
    padding-left: 40px !important;
    padding-right: 40px !important;
  }

  .fixck li {
    display: list-item !important;
  }

  .fixck h1 {
    font-weight: bold !important;
    font-size: 32px !important;
    margin: 21px 0 !important;
  }

  .fixck h2 {
    font-weight: bold !important;
    font-size: 24px !important;
    margin: 19px 0 !important;
  }

  .fixck h3 {
    font-weight: bold !important;
    font-size: 19px !important;
    margin: 18px 0 !important;
  }

  .fixck h4 {
    font-weight: bold !important;
    font-size: 16px !important;
    margin: 21px 0 !important;
  }

  .fixck h5 {
    font-weight: bold !important;
    font-size: 13px !important;
    margin: 22px 0 !important;
  }

  .fixck h6 {
    font-weight: bold !important;
    font-size: 11px !important;
    margin: 24px 0 !important;
  }

  .news_con {
    line-height: 1.8;
    font-size: 16px;
    text-align: justify;
  }

  .iconfont {
    font-size: 14px;
    margin-right: 3px;
  }
  .ant-comment-actions {
    margin-top: -20px;
  }
  .ant-anchor-ink {
    position: relative;
  }
  .ant-form-item {
    margin-bottom: 1px;
  }
  .contain {
    width: 600px;
    margin: 0 auto;
  }
  .message_infos {
    width: 96%;
    min-height: 500px;
    margin-left: 10px;
  }
  .noComment {
    width: 100%;
    text-align: center;
  }
</style>
