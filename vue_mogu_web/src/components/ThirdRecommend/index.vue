<template>
  <div class="zhuanti" v-if="thirdData.length > 0">
    <h2 class="hometitle">特别推荐</h2>
    <ul>
      <li  v-for="item in thirdData" :key="item.uid" style="cursor: pointer" @click="goToInfo(item)"> <i><img v-if="item.photoList" :src="item.photoList[0]"></i>
        <p @click="goToInfo(item)" style="cursor: pointer">{{splitStr(item.title, 30)}}<span><a href="javascript:void(0);">阅读</a></span> </p>
      </li>
    </ul>
  </div>
</template>

<script>
import { getBlogByLevel } from "../../api/index";
import {getBlogByUid} from "../../api/blogContent";
export default {
  name: 'ThirdRecommend',
    data() {
    	return {
        slideList: [],
	      thirdData: [], //；一级推荐数据
    	}
    },
    created() {
      var thirdParams = new URLSearchParams();
      thirdParams.append("currentPage", 0);
      thirdParams.append("pageSize", 3);
      thirdParams.append("level", 3);
      thirdParams.append("useSort", 1);
      getBlogByLevel(thirdParams).then(response => {
        if (response.code == this.$ECode.SUCCESS) {
          this.thirdData = response.data.records;
        }
      });
    },
    methods: {
      //跳转到文章详情【或推广链接】
      goToInfo(blog) {
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
      },
      splitStr(str, flagCount) {
        return this.$commonUtil.splitStr(str, flagCount)
      }
    },

}
</script>

<style>
</style>
