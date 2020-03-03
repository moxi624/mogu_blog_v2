<template>
  <div class="zhuanti" v-if="thirdData.length > 0">
    <h2 class="hometitle">特别推荐</h2>
    <ul>
      <li  v-for="item in thirdData" :key="item.uid"> <i><img v-if="item.photoList" :src="PICTURE_HOST + item.photoList[0]"></i>
        <p>{{item.title}} <span><a href="javascript:void(0);" @click="goToInfo(item.uid)">阅读</a></span> </p>
      </li>
    </ul>
  </div>
</template>

<script>
import { getBlogByLevel } from "../../api/index";
export default {
  name: 'ThirdRecommend',
    data() {
    	return {
        PICTURE_HOST: process.env.PICTURE_HOST,
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
        if (response.code == "success") {
          this.thirdData = response.data.records;
        }
      });
    },
    methods: {
      //跳转到文章详情
	    goToInfo(uid) {

        let routeData = this.$router.resolve({ path: "/info", query: { blogUid: uid } });
        window.open(routeData.href, '_blank');

	    }
    },

}
</script>

<style>
</style>
