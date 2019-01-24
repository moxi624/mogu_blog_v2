<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>

    <sidebar class="sidebar-container" :items="items"></sidebar>

    <div class="main-container">
      <navbar/>
      <tags-view></tags-view>
      <app-main/>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { Navbar, Sidebar, AppMain, TagsView } from "./components";
import ResizeMixin from "./mixin/ResizeHandler";
import { getMenu } from "@/api/login";
export default {
  name: "Layout",
  components: {
    Navbar,
    Sidebar,
    AppMain,
    TagsView
  },
  mixins: [ResizeMixin],
  data() {
    return {
      items: []
    };
  },
  computed: {
    ...mapGetters(["menu"]),
    sidebar() {
      return this.$store.state.app.sidebar;
    },
    device() {
      return this.$store.state.app.device;
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === "mobile"
      };
    }
  },
  created() {
    var that = this;
    this.$store.dispatch("GetMenu").then(response => {
      if (response.code == "success") {
        var parentList = response.data.parentList;
        var sonList = response.data.sonList;
        var items = [];
        if (
          parentList &&
          parentList.length > 0 &&
          sonList &&
          sonList.length > 0
        ) {
          for (var index = 0; index < parentList.length; index++) {
            var newObject = { parent: parentList[index] };
            var sonItem = [];

            for (var index1 = 0; index1 < sonList.length; index1++) {
              if (sonList[index1].parentUid == parentList[index].uid) {
                sonItem.push(sonList[index1]);
              }
            }
            //对子目录进行排序
            for (var a = 0; a < sonItem.length; a++) {
              for (var b = 0; b < sonItem.length - a - 1; b++) {
                var tag = false;
                if (sonItem[b].sort < sonItem[b + 1].sort) {
                  var temp = sonItem[b];
                  sonItem[b] = sonItem[b + 1];
                  sonItem[b + 1] = temp;
                  tag = true;
                }
              }
            }
            //加入
            newObject.sonItem = sonItem;
            items.push(newObject);
          }
        }
        that.items = items;
      }
    });

    // getMenu().then(response => {
    //   if (response.code == "success") {
    //     var parentList = response.data.parentList;
    //     var sonList = response.data.sonList;
    //     var items = [];
    //     if (
    //       parentList &&
    //       parentList.length > 0 &&
    //       sonList &&
    //       sonList.length > 0
    //     ) {
    //       for (var index = 0; index < parentList.length; index++) {
    //         var newObject = { parent: parentList[index] };
    //         var sonItem = [];

    //         for (var index1 = 0; index1 < sonList.length; index1++) {
    //           if (
    //             sonList[index1].parentUid == parentList[index].uid
    //           ) {
    //             sonItem.push(sonList[index1]);
    //           }
    //         }
    //         //jiaru
    //         newObject.sonItem = sonItem;
    //         items.push(newObject);
    //       }
    //     }

    //     that.items = items;
    //   }
    // });
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch("CloseSideBar", { withoutAnimation: false });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
@import "src/styles/mixin.scss";
.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;
  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}
.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}
</style>
