<template>
    <div class="links" v-if="linkData.length > 0">
      <h2 class="hometitle">友情链接</h2>
      <ul>
          <li v-for="item in linkData" :key="item.uid"><a  href="javascript:void(0);" @click="goLink(item)">{{item.title}}</a></li>
      </ul>
    </div>

</template>

<script>
import { getLink, addLinkCount } from "../../api/index";
export default {
  name: "TagCloud",
  data() {
    return {
      linkData: [], //友情链接
    };
  },
  created() {
    getLink().then(response => {
      if(response.code == this.$ECode.SUCCESS) {
        this.linkData = response.data;
      }
    });
  },
  methods: {
    goLink: function(item) {
      var params = new URLSearchParams();
      params.append("uid", item.uid);
      addLinkCount(params).then(response => {
        if(response.code == this.$ECode.SUCCESS) {
          window.open(item.url, "_blank")
        }
      })

    }
  }
};
</script>

<style>

</style>
