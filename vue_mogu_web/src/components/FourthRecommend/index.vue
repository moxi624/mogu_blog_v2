<template>
    <div class="tuijian">
      <h2 class="hometitle">推荐文章</h2>
      <ul class="tjpic" v-if="fourthData[0]">
        <i><img v-if="fourthData[0].photoList" :src="fourthData[0].photoList[0]"></i>
        <p><a href="javascript:void(0);" @click="goToInfo(fourthData[0].uid)">{{fourthData[0].title}}</a></p>
      </ul>
      
      <ul class="sidenews">
        <li v-for="(item, index) in fourthData" v-if="index != 0" :key="item.uid">
          <i><img v-if="item.photoList" :src="item.photoList[0]"></i>
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
	      fourthData: [], //；四级推荐数据
    	}       
    },
    created() {
      var fourthParams = new URLSearchParams();
      fourthParams.append("currentPage", 0);
      fourthParams.append("pageSize", 5);
      fourthParams.append("level", 4);    
      getBlogByLevel(fourthParams).then(response => {
        console.log("四级推荐", response);
        this.fourthData = response.data.records;
      });
    },
    methods: {
      //跳转到文章详情
	    goToInfo(uid) {
	      console.log("跳转到文章详情");
	      this.$router.push({ path: '/info', query: { blogUid: uid }});
	    }
    },

}
</script>

<style>
</style>
