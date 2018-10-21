<template>
    <div class="links" v-if="linkData.length > 0">
      <h2 class="hometitle">友情链接</h2>
      <ul>
          <li v-for="item in linkData" :key="item.uid"><a :href="item.url" target="_blank">{{item.title}}</a></li>
      </ul>
    </div>

</template>

<script>
import { getLink } from "../../api/index";
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
      this.linkData = response.data.records;
    });
  },
  methods: {
  }
};
</script>

<style>

</style>
