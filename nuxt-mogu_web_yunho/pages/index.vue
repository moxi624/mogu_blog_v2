<template>
  <article>
    <!--banner begin-->
    <div class="picsbox">
      <FirstRecommend></FirstRecommend>
    <!--banner end-->
            <!-- 二级推荐 -->
      <div class="toppic">
        <li v-for="item in secondData" :key="item.uid" @click="goToInfo(item)">
          <a href="javascript:void(0);">
            <i>
              <img v-if="item.photoList" :src="item.photoList[0]">
            </i>
            <h2>{{item.title}}</h2>
            <span>{{item.blogSort.sortName}}</span>
          </a>
        </li>
      </div>
    </div>
    <div class="blank"></div>
<!--blogsbox begin-->
    <div class="blogsbox">
      <div
        v-for="item in newBlogData"
        :key="item.uid"
        class="blogs"
        data-scroll-reveal="enter bottom over 1s"
      >
        <h3 class="blogtitle">
          <a href="javascript:void(0);" @click="goToInfo(item)">{{item.title}}</a>
        </h3>

        <span class="blogpic">
          <a href="javascript:void(0);" @click="goToInfo(item)" title>
            <img v-if="item.photoList" :src="item.photoList[0]" alt>
          </a>
        </span>

        <p class="blogtext">{{item.summary}}</p>
        <div class="bloginfo">
          <ul>

            <li class="author">
              <span class="iconfont">&#xe60f;</span>
              <a href="javascript:void(0);" @click="goToAuthor(item.author)">{{item.author}}</a>
            </li>
            <li class="lmname" v-if="item.blogSort">
              <span class="iconfont">&#xe603;</span>
              <a
                href="javascript:void(0);"
                @click="goToList(item.blogSort.uid)"
              >{{item.blogSort.sortName}}</a>
            </li>
            <li class="view">
              <span class="iconfont">&#xe8c7;</span>
              <span>{{item.clickCount}}</span>
            </li>
            <li class="like">
              <span class="iconfont">&#xe663;</span>
              {{item.collectCount}}
            </li>
            <li class="createTime">
              <span class="iconfont">&#xe606;</span>
              {{item.createTime}}
            </li>
          </ul>
        </div>
      </div>

      <div class="isEnd">
        <!-- <span v-if="!isEnd">正在加载中~</span> -->

        <div class="loadContent" @click="loadContent" v-if="!isEnd&&!loading">点击加载更多</div>

        <div class="lds-css ng-scope" v-if="!isEnd&&loading">
          <div style="width:100%;height:100%" class="lds-facebook">
            <div></div>
            <div></div>
            <div></div>
          </div>
        </div>

        <span v-if="isEnd">我也是有底线的~</span>
      </div>
    </div>
    <!--blogsbox end-->
    <div class="sidebar">
      <!--标签云-->
      <TagCloud></TagCloud>

      <!--关注我们-->
      <FollowUs></FollowUs>

      <!-- 三级推荐 -->
      <ThirdRecommend></ThirdRecommend>

      <!--四级推荐-->
      <FourthRecommend></FourthRecommend>

      <!--点击排行-->
      <HotBlog></HotBlog>

      <!-- 友情链接-->
      <Link></Link>
    </div>
  </article>
</template>

<script>
import FirstRecommend from '../components/FirstRecommend.vue'
import {getWebConfig,getBlogByLevel, getNewBlog, recorderVisitPage} from "~/assets/scripts/index";
import {getBlogByUid} from "~/assets/scripts/blogContent";
import { Loading } from 'element-ui';
import ThirdRecommend from "../components/ThirdRecommend";
import FourthRecommend from "../components/FourthRecommend";
import TagCloud from "../components/TagCloud";
import HotBlog from "../components/HotBlog";
import FollowUs from "../components/FollowUs";
import Link from "../components/Link";
import {mapGetters,mapState,mapMutations} from 'vuex';
export default {
    name: "index",
    data(){
      return {
        loadingInstance: null, // loading对象
        VUE_MOGU_WEB: process.env.VUE_MOGU_WEB,
        firstData: [], //；一级推荐数据
        secondData: [], //；二级级推荐数据
        thirdData: [], //三级推荐
        fourthData: [], //四级推荐
        newBlogData: [], //最新文章
        hotBlogData: [], //最热文章
        hotTagData: [], //最新标签
        keyword: "",
        currentPage: 1,
        pageSize: 15,
        total: 0, //总数量
        isEnd: false, //是否到底底部了
        loading: false //是否正在加载
      }
    },
    components: {
      //注册组件
      FirstRecommend,
      FourthRecommend,
      ThirdRecommend,
      TagCloud,
      HotBlog,
      FollowUs,
      Link,
    },
    created(){
      if (process.client) {
      console.log("index created run");
      var secondParams = new URLSearchParams();
      secondParams.append("level", 2);
      // 是否排序
      secondParams.append("useSort", 1);
      getBlogByLevel(secondParams).then(response => {
        if(response.data.code == this.$ECode.SUCCESS) {
          this.secondData = response.data.data.records;
        }
      });
      
      // 获取最新博客
      // this.newBlogList();
      var params = new URLSearchParams();
      params.append("pageName", "INDEX");
        recorderVisitPage(params).then(response => {
      });
      }
    },
    head(){
      console.log("index head run");
      // console.log(this.info.title);
      let webConfigData =  this.$store.getters.getWebConfigData;
      if(webConfigData && webConfigData.createTime){
        return this.$seo("index test"+webConfigData.title,webConfigData.keyword,webConfigData.summary,[{}]);
      }
    },
    async asyncData({app}){
        var params = new URLSearchParams();
        params.append("currentPage", 1);
        params.append("pageSize", 15);
        var res = await getNewBlog(params);
        if (res.data.code == app.$ECode.SUCCESS) {
            return {
              newBlogData:res.data.data.records,
              total:res.data.data.total,
              pageSize:res.data.data.size,
              currentPage: res.data.data.current
            }
        }
    },
   async fetch({$axios,$cookies,store}){
      console.log('run fetch get all getStore');
    
      console.log('run fetch  end');
    },
    methods:{    //获取跟模块的数据
      ...mapGetters(['getWebConfigData']),
      ...mapState(['webConfigData']),
      ...mapMutations(['setWebConfigData']),
      //跳转到文章详情【或推广链接】
      goToInfo(blog) {
        if (process.client) {
          if(blog.type == "0") {
            let routeData = this.$router.resolve({
              path: "/info",
              query: {blogOid: blog.oid}
            });
            window.open(routeData.href, '_blank');
          } else if(blog.type == "1") {
            var params = new URLSearchParams();
            params.append("uid", blog.uid);
            getBlogByUid(params).then(response => {
              // 记录一下用户点击日志
            });
            window.open(blog.outsideLink, '_blank');
          }
        }
      },

      //最新博客列表
      newBlogList() {
        // var that = this;
        // that.loadingInstance = Loading.service({
        //   lock: true,
        //   text: '正在努力加载中……',
        //   background: 'rgba(0, 0, 0, 0.7)'
        // })

        
      },
      loadContent: function () {
        var that = this;
        that.loading = false;
        that.currentPage = that.currentPage + 1;
        var params = new URLSearchParams();
        params.append("currentPage", that.currentPage);
        params.append("pageSize", that.pageSize);
        getNewBlog(params).then(response => {
          if (response.data.code == this.$ECode.SUCCESS && response.data.data.records.length > 0) {
            that.isEnd = false;
            var newData = that.newBlogData.concat(response.data.data.records);
            that.newBlogData = newData;
            that.total = response.data.data.total;
            that.pageSize = response.data.data.size;
            that.currentPage = response.data.data.current;

            //全部加载完毕
            if (newData.length < that.pageSize) {
              that.isEnd = true;
            }
          } else {
            that.isEnd = true;
          }
          that.loading = false;
        });
      }
    }
}
</script>

<style>
.el-loading-mask {
    z-index: 2002;
  }
  .isEnd {
    float: left;
    width: 100%;
    height: 80px;
    text-align: center;
  }

  .ng-scope {
    margin: 0 auto;
    width: 18%;
    height: 10%;
  }

  .loadContent {
    width: 120px;
    height: 30px;
    line-height: 30px;
    font-size: 16px;
    margin: 0 auto;
    color: aliceblue;
    cursor: pointer;
    background: rgba(0, 0, 0, 0.8);
  }

  @keyframes lds-facebook_1 {
    0% {
      top: 0px;
      height: 200px;
    }
    50% {
      top: 80px;
      height: 40px;
    }
    100% {
      top: 80px;
      height: 40px;
    }
  }

  @-webkit-keyframes lds-facebook_1 {
    0% {
      top: 0px;
      height: 200px;
    }
    50% {
      top: 80px;
      height: 40px;
    }
    100% {
      top: 80px;
      height: 40px;
    }
  }

  @keyframes lds-facebook_2 {
    0% {
      top: 20px;
      height: 160px;
    }
    50% {
      top: 80px;
      height: 40px;
    }
    100% {
      top: 80px;
      height: 40px;
    }
  }

  @-webkit-keyframes lds-facebook_2 {
    0% {
      top: 20px;
      height: 160px;
    }
    50% {
      top: 80px;
      height: 40px;
    }
    100% {
      top: 80px;
      height: 40px;
    }
  }

  @keyframes lds-facebook_3 {
    0% {
      top: 40px;
      height: 120px;
    }
    50% {
      top: 80px;
      height: 40px;
    }
    100% {
      top: 80px;
      height: 40px;
    }
  }

  @-webkit-keyframes lds-facebook_3 {
    0% {
      top: 40px;
      height: 120px;
    }
    50% {
      top: 80px;
      height: 40px;
    }
    100% {
      top: 80px;
      height: 40px;
    }
  }

  .lds-facebook {
    position: relative;
  }

  .lds-facebook div {
    position: absolute;
    width: 20px;
  }

  .lds-facebook div:nth-child(1) {
    left: 40px;
    background: #1d0e0b;
    -webkit-animation: lds-facebook_1 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
    animation: lds-facebook_1 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
    -webkit-animation-delay: -0.2s;
    animation-delay: -0.2s;
  }

  .lds-facebook div:nth-child(2) {
    left: 90px;
    background: #774023;
    -webkit-animation: lds-facebook_2 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
    animation: lds-facebook_2 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
    -webkit-animation-delay: -0.1s;
    animation-delay: -0.1s;
  }

  .lds-facebook div:nth-child(3) {
    left: 140px;
    background: #d88c51;
    -webkit-animation: lds-facebook_3 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
    animation: lds-facebook_3 1s cubic-bezier(0, 0.5, 0.5, 1) infinite;
  }

  .lds-facebook {
    width: 90px !important;
    height: 90px !important;
    -webkit-transform: translate(-45px, -45px) scale(0.45) translate(45px, 45px);
    transform: translate(-45px, -45px) scale(0.45) translate(45px, 45px);
  }

  .iconfont {
    font-size: 15px;
    margin-right: 2px;
  }

</style>
