<template>
  <div class="menu-wrapper">
    <template v-for="(item,index) in items" v-if="items.length>0">
      <el-submenu :key="index" :index="index+''" v-if="checkShowOrHidden(item)">
        <template slot="title">
          <i v-if="item.parent.icon" :class="item.parent.icon"></i>
<!--          <svg-icon v-if="item.parent.icon" :icon-class="item.parent.icon"></svg-icon>-->
          <span v-if="item.parent.name" slot="title">{{item.parent.name}}</span>
        </template>

        <template v-for="(child,index2) in item.sonItem" v-if="hasOneShowingChildren(child)">
          <router-link :to="child.url" :key="index2">
            <el-menu-item :index="child.url" :key="child.name">
              <i v-if="child.icon" :class="child.icon"></i>
<!--              <svg-icon v-if="child.icon" :icon-class="child.icon"></svg-icon>-->
              <span v-if="child.name" slot="title">{{child.name}}</span>
            </el-menu-item>
          </router-link>
        </template>
      </el-submenu>

    </template>

  </div>
</template>

<script>

export default {
  name: 'SidebarItem',
  props: {
    routes: {
      type: Array
    },
    items:{
      type:Array
    },
    isNest: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      isShow: false
    }
  },
  methods: {
    hasOneShowingChildren(children) {
      return (children.isShow == 1)
    },
    // 检测父菜单是否隐藏
    checkShowOrHidden(item) {
      return (item.parent.isShow == 1)
    }
  }
}
</script>
