<template>
  <div>
    <div class="learing-list">
      <div class="list-box">
        <ul>
          <li>关键字：</li>
          <ol>
            <li>{{keyword}}

              <nuxt-link v-if="keyword" class="title-link" :to="'/course/search?keyword=&mt='+mt+'&st=' + st+'&grade='+grade">
                &Chi;
              </nuxt-link>
            </li>
          </ol>
        </ul>
        <ul>
          <li>一级分类：</li>
          <li v-if="mt!=''"><nuxt-link  class="title-link" :to="'/course/search?keyword='+keyword+'&grade='+grade">全部</nuxt-link></li>
          <li class="all" v-else>全部</li>
          <ol>
          <li v-for="category_v in first_category">
            <nuxt-link  class="title-link all" :to="'/course/search?keyword='+keyword+'&mt=' + category_v.id" v-if="category_v.id == mt">{{category_v.name}}</nuxt-link>
            <nuxt-link  class="title-link" :to="'/course/search?keyword='+keyword+'&mt=' + category_v.id" v-else>{{category_v.name}}</nuxt-link>
          </li>
          </ol>
          <!--<ol>
            <li>数据分析</li>
            <li>机器学习工程</li>
            <li>前端开发工程</li>
          </ol>-->
        </ul>
        <ul>
          <li>二级分类：</li>
          <li v-if="st!=''"><nuxt-link  class="title-link" :to="'/course/search?keyword='+keyword+'&mt='+mt+'&grade='+grade">全部</nuxt-link></li>
          <li class="all" v-else>全部</li>
          <ol v-if="second_category.length>0">
            <li v-for="category_v in second_category">
              <nuxt-link  class="title-link all" :to="'/course/search?keyword='+keyword+'&mt='+mt+'&st=' + category_v.id" v-if="category_v.id == st">{{category_v.name}}</nuxt-link>
              <nuxt-link  class="title-link" :to="'/course/search?keyword='+keyword+'&mt='+mt+'&st=' + category_v.id" v-else>{{category_v.name}}</nuxt-link>
            </li>
           <!-- <li>大数据</li>
            <li>云计算</li>-->
          </ol>
          <!--<a href="#" class="more">更多 ∨</a>-->
        </ul>
        <ul>
          <li>难度等级：</li>
          <li v-if="grade!=''">
            <nuxt-link  class="title-link" :to="'/course/search?keyword='+keyword+'&mt=' + mt+'&st='+st+'&grade='">全部
            </nuxt-link>
          </li>
          <li class="all" v-else>全部</li>
          <ol>
            <li v-if="grade=='200001'" class="all">初级</li>
            <li v-else><nuxt-link  class="title-link" :to="'/course/search?keyword='+keyword+'&mt=' + mt+'&st='+st+'&grade=200001'">初级</nuxt-link></li>
            <li v-if="grade=='200002'" class="all">中级</li>
            <li v-else><nuxt-link  class="title-link" :to="'/course/search?keyword='+keyword+'&mt=' + mt+'&st='+st+'&grade=200002'">中级</nuxt-link></li>
            <li v-if="grade=='200003'" class="all">高级</li>
            <li v-else><nuxt-link  class="title-link" :to="'/course/search?keyword='+keyword+'&mt=' + mt+'&st='+st+'&grade=200003'">高级</nuxt-link></li>
          </ol>
        </ul>
      </div>
    </div>

    <div class="container">
      <div class="row">
        <div class="col-md-9 list-row-left">
          <div class="list-cont-left">
            <div class="tit">
              <ul class="nav nav-tabs ">
                <!--<li class="active"><a href="#">推荐</a></li>
                <li><a href="#">最新</a></li>
                <li><a href="#">热评</a></li>-->
               <!-- <div class="page navbar-right">
                  <a href="#" class="prev">
                    < </a>
                  <span class="">1/28</span>
                  <a href="#" class="next"> ></a>
                </div>-->
              </ul>
            </div>
            <div class="tab-content">
              <div class="content-list">

                <!--<div class="recom-item">
                  <a href="/course/detail?id=1010" target="_blank">
                    <p><img src="/img/widget-demo1.png" width="100%" alt=""><span class="lab">HOT</span></p>
                    <ul>
                      <li>Think PHP 5.0 博客系统实战项目演练 </li>
                      <li><span>高级</span> <em> · </em> 1125人在学习</li>
                    </ul>
                  </a>
                </div>-->
                <div class="recom-item" v-for="(course, index) in courselist">
                  <a :href="'/course/detail/'+course._source.id+'.html'" target="_blank">
                  <!--<a href="/course/detail/test.html" target="_blank">-->
                    <div v-if="course._source.pic">
                      <p><img :src="imgUrl+'/'+course._source.pic" width="100%" alt=""></p>
                    </div>
                    <div v-else>
                      <p><img src="/img/widget-demo1.png" width="100%" alt=""></p>
                    </div>
                    <ul >
                      <li class="course_title">{{course._source.name}}</li>
                      <li style="float: left"><span v-if="course._source.charge == '203001'">免费</span><span v-if="course._source.charge == '203002'">￥{{course._source.price | money}}</span>
                        <!-- <em> · </em>-->&nbsp;&nbsp;<!--<em>1125人在学习</em>--></li>
                    </ul>
                  <!--</a>-->
                  </a>
                </div>

                <li class="clearfix"></li>
              </div>
              <div class="clearfix"></div>
            </div>
          </div>

          <div style="text-align: center">
            <el-pagination
              background
              layout="prev, pager, next"
              @current-change="handleCurrentChange"
              :total="total"
              :page-size="page_size"
              prev-text="上一页"
              next-text="下一页">
            </el-pagination>
          </div>
        </div>
        <div class="col-md-3 list-row-rit">
          <div class="list-cont-right">
            <!--精品推荐-->
            <!--#include virtual="/include/BestTop.html"-->
            <!--精品推荐结束-->
            <!--猜你喜欢开始-->
            <div class="right-box">
              <div class="tit">猜你喜欢</div>
              <div class="contList">
                <div class="contList-titB">通过对ThinkPHP框架基础，带领大家由浅入深轻松掌握ThinkPHP的理论基础，更加全面的掌握ThinkPHP框架运行机制……</div>
                <div class="contList-item">
                  <p>Think PHP 5.0 博客系统实战项目演练</p>
                  <li><span>高级</span> <em> · </em> 1125人在学习</li>
                </div>
                <div class="contList-item">
                  <p>Think PHP 5.0 博客系统实战项目演练</p>
                  <li><span>高级</span> <em> · </em> 1125人在学习</li>
                </div>
                <div class="contList-item">
                  <p>Think PHP 5.0 博客系统实战项目演练</p>
                  <li><span>高级</span> <em> · </em> 1125人在学习</li>
                </div>
                <div class="contList-item">
                  <p>Think PHP 5.0 博客系统实战项目演练</p>
                  <li><span>高级</span> <em> · </em> 1125人在学习</li>
                </div>
              </div>
            </div>
            <!--猜你喜欢结束-->
          </div>
        </div>
      </div>

    </div>

  </div>
</template>
<script>
  //配置文件
  let config = require('~/config/sysConfig')
  import querystring from 'querystring'
  import * as courseApi from '~/api/course'
  export default {
    head() {
      return {
        title: '传智播客-一样的教育,不一样的品质',
        meta: [
          {charset: 'utf-8'},
          {name: 'description', content: '传智播客专注IT培训,Java培训,Android培训,安卓培训,PHP培训,C++培训,网页设计培训,平面设计培训,UI设计培训,移动开发培训,网络营销培训,web前端培训,云计算大数据培训,全栈工程师培训,产品经理培训。'},
          {name: 'keywords', content: this.keywords}
        ],
        link: [
          {rel: 'stylesheet', href: '/static/plugins/normalize-css/normalize.css'},
          {rel: 'stylesheet', href: '/static/plugins/bootstrap/dist/css/bootstrap.css'},
          {rel: 'stylesheet', href: '/static/css/page-learing-list.css'}
        ]
      }
    },
    async asyncData({ store, route }) {
//       console.log(route.query)

      let page = route.query.page;
       if(!page){
           page = 1;
       }
//       console.log("page="+page)
      //搜索课程
      let course_data = await courseApi.search_course(page,12,route.query)
      console.log(course_data)
      //查询分类
      let category_data = await courseApi.sysres_category()
//      console.log(category_data)
//      console.log(course_data.hits.hits)

      if (course_data && course_data.hits &&  course_data.hits.hits && category_data) {
        //全部分类
        let category = category_data.category
        let first_category = category[0].children
        let keywords = ''
        let second_category=[]
        let mt=''
        let st=''
        let grade=''
        let keyword=''
        let total = course_data.hits.total
        if( route.query.mt){
          mt = route.query.mt
        }
        if( route.query.st){
          st = route.query.st
        }
        if( route.query.grade){
          grade = route.query.grade
        }
        if( route.query.keyword){
          keyword = route.query.keyword
        }
        //遍历一级分类
        for(var i in first_category){
          keywords+=first_category[i].name+' '
          if(mt!=''&& mt == first_category[i].id){
            //取出二级分类
            second_category = first_category[i].children;
            // console.log(second_category)
            break;
          }
        }
        //console.log(category[0].children)
        return {
          courselist: course_data.hits.hits,
          first_category: first_category,
          keywords:keywords,
          second_category: second_category,
          mt:mt,
          st:st,
          grade:grade,
          keyword:keyword,
          total:total,
          imgUrl:config.imgUrl
        }
      } else {
        return {
          courselist: {},
          first_category:{},
          second_category:{},
          mt:'',
          st:'',
          grade:'',
          keyword:'',
          total:0,
          imgUrl:config.imgUrl
        }
      }

    },
//自定义过虑器
    filters: {
      money: function(value) {
        return Math.floor(value);
      }
    },
    data() {
      return {
        total:0,//总记录数
        page:1,//页码
        page_size:12,//每页显示个数
        keywords:''//head中的关键字
      };
    },
    watch:{//路由发生变化立即搜索search表示search方法
      '$route':'search'
    },
    methods: {
      //分页触发
      handleCurrentChange(page) {
        this.page = page
        let query = Object.assign({}, this.$route.query);
        query.page = page
        let querys = querystring.stringify(query)
//        console.log(querys)
        this.$router.push(`/course/search?`+querys)
      },
      search(){//搜索方法
//        console.log("search....")
//        console.log(this.$route.query)
        courseApi.sysres_category().then((category_data)=>{// 搜索分类
          let category = category_data.category
          let first_category = category[0].children
          let second_category=[]
          let mt=''
          let st=''
          let grade=''
          let keyword = ''

          if( this.$route.query.mt){
            mt = this.$route.query.mt
          }
          if( this.$route.query.st){
            st = this.$route.query.st
          }
          if( this.$route.query.grade){
            grade = this.$route.query.grade
          }
          if(  this.$route.query.keyword){
            keyword =  this.$route.query.keyword
          }
          if(mt!=''){
            //取出二级分类
            for(var i in first_category){
              if(mt == first_category[i].id){
                second_category = first_category[i].children;
                // console.log(second_category)
                break;
              }
            }
          }
          this.first_category=first_category
          this.second_category= second_category
          this.mt=mt
          this.st=st
          this.grade =grade
          this.keyword = keyword

        })
        courseApi.search_course(this.page,this.page_size,this.$route.query).then((course_data) => {
          //console.log(course_data.hits.hits)
          this.courselist=course_data.hits.hits
          this.total = course_data.hits.total

        })


      }
    },
    mounted() {
      this.search()
    }
  }
</script>
<style>
  a {
    color: #000;
  }
  .el-icon-arrow-left:before {
    content: "<";
  }
  .el-icon-d-arrow-left:before {
    content: "...";
  }
  .el-icon-arrow-right:before {
    content: ">";
  }
  .el-icon-d-arrow-right:before {
    content: "...";
  }
  .el-icon-more:before {
    content: "...";
  }
  .el-pagination.is-background .btn-next, .el-pagination.is-background .btn-prev, .el-pagination.is-background .el-pager li {
    background-color: #fff;
  }
  .course_title{
    height: 20px;
    width: 160px;
    overflow:hidden;
  }

</style>
