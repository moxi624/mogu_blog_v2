<template>
    <div class="links" v-if="linkData.length > 0">
      <h2 class="hometitle">友情链接</h2>
      <ul>
          <li v-for="item in linkData" :key="item.uid"><a  href="javascript:void(0);" @click="goLink(item)">{{item.title}}</a></li>
      </ul>
    </div>

</template>

<script>
import { getLink, addLinkCount } from "~/api/index";
export default {
  name: "TagCloud",
  data() {
    return {
      linkData: [], //友情链接
    };
  },
  created() {
    getLink().then(response => {
      console.log("友情链接列表", response);
      if(response.code == "success") {
        this.linkData = response.data.records;
      }      
    });
  },
  methods: {
    goLink: function(item) {
      var params = new URLSearchParams();
      params.append("uid", item.uid);
      addLinkCount(params).then(response => {
        if(response.code == "success") {
          window.location.href = item.url;    
        }
      })
      
    }
  }
};
</script>

<style>

</style>
