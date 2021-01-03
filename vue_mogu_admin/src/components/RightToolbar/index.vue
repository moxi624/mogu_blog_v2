<!-- @author Shiyn/   huangmx 20200807优化-->
<template>
  <div class="top-right-btn">
    <el-row>
      <el-tooltip class="item" effect="dark" :content="showSearch ? '隐藏搜索' : '显示搜索'" placement="top">
        <el-button circle icon="el-icon-search" @click="toggleSearch()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="刷新" placement="top">
        <el-button circle icon="el-icon-refresh" @click="refresh()" />
      </el-tooltip>
    </el-row>
  </div>
</template>
<script>
import {setCookie} from "../../utils/cookieUtils"
export default {
  name: "RightToolbar",
  data() {
    return {};
  },
  props: {
    showSearch: {
      type: Boolean,
      // default: true,
    },
  },
  methods: {
    //搜索
    toggleSearch() {
      setCookie('showSearch', !this.showSearch)
      this.$emit("update:showSearch", !this.showSearch);
    },
    //刷新
    refresh() {
      this.$emit("queryTable");
    },
  },
};
</script>
<style scoped>
.top-right-btn {
  position: relative;
  float: right;
}
</style>
