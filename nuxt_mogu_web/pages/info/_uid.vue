<template>
  <div>
    <article>
      <h1 class="t_nav">
        <a href="/home" class="n1">网站首页</a>
        <a
          href="javascript:void(0);"
          @click="goToSortList(blogData.blogSort.uid)"
          class="n2"
        >{{blogData.blogSort ? blogData.blogSort.sortName:""}}</a>
      </h1>
      <div class="infosbox">
        <div class="newsview">
          <h3 class="news_title" v-if="blogData.title">{{blogData.title}}</h3>
          <div class="bloginfo" v-if="blogData.blogSort">
            <ul>
              <li class="author">
                <a href="/">{{blogData.author}}</a>
              </li>
              <li class="lmname">
                <a
                  href="javascript:void(0);"
                  @click="goToSortList(blogData.blogSort == null ?'':blogData.blogSort.uid)"
                >{{blogData.blogSort ? blogData.blogSort.sortName:""}}</a>
              </li>
              <li class="timer">{{blogData.createTime}}</li>
              <li class="view">{{blogData.clickCount}}</li>
              <li class="like">{{blogData.collectCount}}</li>
            </ul>
          </div>
          <div class="tags">
            <a
              v-if="blogData.tagList"
              v-for="item in blogData.tagList"
              :key="item.uid"
              href="javascript:void(0);"
              v-highlight
              @click="goToList(item.uid)"
              target="_blank"
            >{{item.content}}</a>

            <!-- <pre
              v-if="blogData.tagList"
              v-for="item in blogData.tagList"
              :key="item.uid"
              href="javascript:void(0);"
              v-highlightjs
              @click="goToList(item.uid)"
              target="_blank"
            >{{item.content}}</pre> -->

          </div>
          <div class="news_about">
            <strong>版权</strong>
            {{blogData.copyright}}
          </div>
          <div class="news_con fixck"  v-html="blogData.content" v-highlight></div>
        </div>

        <!--付款码和点赞-->
        <!-- <PayCode :blogUid="blogUid"></PayCode> -->

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
            <ChangYan :sid="this.blogUid"></ChangYan>
          </ul>
        </div>
      </div>
      <div class="sidebar">
        <!--三级推荐开始-->
        <div class="zhuanti" v-if="thirdData.length > 0">
          <h2 class="hometitle">特别推荐</h2>
          <ul>
            <li v-for="item in thirdData" :key="item.uid">
              <i>
                <img v-if="item.photoList" :src="item.photoList[0]">
              </i>
              <p>
                {{item.title}}
                <span>
                  <a href="javascript:void(0);" @click="goToInfo(item.uid)">阅读</a>
                </span>
              </p>
            </li>
          </ul>
        </div>
        <!--三级推荐结束-->

        <!--标签云开始-->
        <div class="cloud" v-if="hotTagData.length > 0">
          <h2 class="hometitle">标签云</h2>
          <ul>
            <a
              v-for="item in hotTagData"
              :key="item.uid"
              href="javascript:void(0);"
              @click="goToList(item.uid)"
            >{{item.content}}</a>
          </ul>
        </div>
        <!--标签云结束-->

        <!--四级推荐开始-->
        <div class="tuijian" v-if="fourthData.length > 0">
          <h2 class="hometitle">推荐文章</h2>
          <ul class="tjpic" v-if="fourthData[0]">
            <i>
              <img
                v-if="fourthData[0].photoList"
                style="cursor:pointer"
                @click="goToInfo(fourthData[0].uid)"
                :src="fourthData[0].photoList[0]"
              >
            </i>
            <p>
              <a
                href="javascript:void(0);"
                @click="goToInfo(fourthData[0].uid)"
              >{{fourthData[0].title}}</a>
            </p>
          </ul>

          <ul class="sidenews">
            <li v-for="(item, index) in fourthData" v-if="index != 0" :key="item.uid">
              <i>
                <img
                  style="cursor:pointer"
                  v-if="item.photoList"
                  @click="goToInfo(fourthData[0].uid)"
                  :src="item.photoList[0]"
                >
              </i>
              <p>
                <a href="javascript:void(0);" @click="goToInfo(item.uid)">{{item.title}}</a>
              </p>
              <span>{{item.createTime}}</span>
            </li>
          </ul>
        </div>
        <!--四级推荐结束-->

        <!--热门博客开始-->
        <div class="tuijian" v-if="hotBlogData.length > 0">
          <h2 class="hometitle">点击排行</h2>
          <ul class="tjpic" v-if="hotBlogData[0]">
            <i>
              <img
                style="cursor:pointer"
                v-if="hotBlogData[0].photoList"
                :src="hotBlogData[0].photoList[0]"
                @click="goToInfo(hotBlogData[0].uid)"
              >
            </i>
            <p>
              <a
                href="javascript:void(0);"
                @click="goToInfo(hotBlogData[0].uid)"
              >{{hotBlogData[0].title}}</a>
            </p>
          </ul>
          <ul class="sidenews">
            <li v-for="(item, index) in hotBlogData" v-if="index != 0" :key="item.uid">
              <i>
                <img
                  style="cursor:pointer"
                  v-if="item.photoList"
                  :src="item.photoList[0]"
                  @click="goToInfo(item.uid)"
                >
              </i>
              <p>
                <a href="javascript:void(0);" @click="goToInfo(item.uid)">{{item.title}}</a>
              </p>
              <span>{{item.createTime}}</span>
            </li>
          </ul>
        </div>
        <!--热门博客结束-->

        <!--热门链接开始-->
        <div class="links" v-if="linkData.length > 0">
          <h2 class="hometitle">友情链接</h2>
          <ul>
            <li v-for="item in linkData" :key="item.uid">
              <a href="javascript:void(0);" @click="goLink(item)">{{item.title}}</a>
            </li>
          </ul>
        </div>
        <!--热门链接结束-->

        <!--关注我们-->
        <!-- <FollowUs></FollowUs> -->
      </div>
    </article>
  </div>
</template>

<script>
import {
  getBlogByLevel,
  getNewBlog,
  getHotTag,
  getHotBlog,
  getLink,
  recorderVisitPage
} from "~/api/index";

import ChangYan from "../../components/ChangYan";

import {
  getBlogByUid,
  getSameBlogByTagUid,
  getSameBlogByBlogUid
} from "~/api/blogContent";

import hljs from "highlight.js";
export default {
  data() {
    return {
      detail: null
    };
  },
  components: {
    ChangYan
  },

  async asyncData({ store, route }) {
    var params = new URLSearchParams();
    params.append("uid", route.query.blogUid);
    let blogResponse = await getBlogByUid(params);

    var thirdParams = new URLSearchParams();
    thirdParams.append("currentPage", 0);
    thirdParams.append("pageSize", 3);
    thirdParams.append("level", 3);
    let thirdResponse = await getBlogByLevel(thirdParams);

    var fourthParams = new URLSearchParams();
    fourthParams.append("currentPage", 0);
    fourthParams.append("pageSize", 5);
    fourthParams.append("level", 4);
    let fourthResponse = await getBlogByLevel(fourthParams);

    var hotTagParams = new URLSearchParams();
    hotTagParams.append("currentPage", 0);
    hotTagParams.append("pageSize", 10);
    let hotTagResponse = await getHotTag(hotTagParams);

    let hotResponse = await getHotBlog();

    let linkResponse = await getLink();

    var sameBlogParams = new URLSearchParams();
    sameBlogParams.append("blogUid", route.params.uid);
    let sameBlogResponse = getSameBlogByBlogUid(sameBlogParams);

    if (thirdResponse.code == "success") {
      return {
        thirdData: thirdResponse.data.records,
        fourthData: fourthResponse.data.records,
        hotTagData: hotTagResponse.data.records,
        hotBlogData: hotResponse.data.records,
        linkData: linkResponse.data.records,
        sameBlogData: [],
        blogData: blogResponse.data
      };
    } else {
      return {
        thirdData: [],
        fourthData: [],
        hotTagData: [],
        hotBlogData: [],
        linkData: [],
        sameBlogData: [],
        blogData: []
      };
    }
  },
  created() {}
};
</script>

<style>
</style>