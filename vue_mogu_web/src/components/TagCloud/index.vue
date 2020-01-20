<template>
  <div class="cloud" v-if="hotTagData.length > 0">
    <h2 class="hometitle">标签云</h2>
    <ul>
      <a v-for="item in hotTagData" :key="item.uid" href="javascript:void(0);" @click="goToList(item.uid)">{{item.content}}</a>
    </ul>
  </div>
</template>

<script>
import { getHotTag } from "../../api/index";
export default {
  name: "TagCloud",
  data() {
    return {
      hotTagData: [] //标签列表
    };
  },
  created() {
    getHotTag().then(response => {
      this.hotTagData = response.data.records;
    });
  },
  methods: {
    //跳转到搜索详情页
    goToList(uid) {
      let routeData = this.$router.resolve({ path: "/list", query: { tagUid: uid } });
      window.open(routeData.href, '_blank');
    },
  }
};
</script>

<style>
</style>
