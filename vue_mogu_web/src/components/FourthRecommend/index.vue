<template>
    <div class="tuijian" v-if="fourthData.length > 0">
      <h2 class="hometitle">推荐文章</h2>
      <ul class="tjpic" v-if="fourthData[0]">
        <i><img v-if="fourthData[0].photoList" style="cursor:pointer"  @click="goToInfo(fourthData[0].uid)" :src="PICTURE_HOST + fourthData[0].photoList[0]"></i>
        <p><a href="javascript:void(0);" @click="goToInfo(fourthData[0].uid)">{{fourthData[0].title}}</a></p>
      </ul>

      <ul class="sidenews">
        <li v-for="(item, index) in fourthData" v-if="index != 0" :key="item.uid">
          <i><img style="cursor:pointer" v-if="item.photoList" @click="goToInfo(fourthData[0].uid)" :src="PICTURE_HOST + item.photoList[0]"></i>
          <p><a href="javascript:void(0);" @click="goToInfo(item.uid)">{{item.title}}</a></p>
          <span>{{item.createTime}}</span>
        </li>
      </ul>
    </div>
</template>

<script>
import { getBlogByLevel } from "../../api/index";
export default {
  name: 'FourthRecommend',
    data() {
    	return {
        PICTURE_HOST: process.env.PICTURE_HOST,
	      fourthData: [], //；四级推荐数据
    	}
    },
    created() {
      var fourthParams = new URLSearchParams();
      fourthParams.append("currentPage", 0);
      fourthParams.append("pageSize", 5);
      fourthParams.append("level", 4);
      fourthParams.append("useSort", 1);
      getBlogByLevel(fourthParams).then(response => {
        this.fourthData = response.data.records;
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
