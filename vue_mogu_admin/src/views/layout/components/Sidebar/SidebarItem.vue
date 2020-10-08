<template>
  <div class="menu-wrapper">
    <template v-for="(item,index) in items" v-if="items.length>0">
      <el-submenu :key="index" :index="index+''" v-if="checkShowOrHidden(item)">
        <template slot="title">
          <i v-if="item.parent.icon" :class="item.parent.icon"></i>
          <span v-if="item.parent.name" slot="title">{{item.parent.name}}</span>
        </template>

        <template v-for="child in item.sonItem" v-if="hasOneShowingChildren(child)">
            <el-menu-item :index="child.url" :key="child.name" @click="goTo(child)">
              <i v-if="child.icon" :class="child.icon"></i>
              <span v-if="child.name" slot="title">{{child.name}}</span>
            </el-menu-item>
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
    goTo: function(child) {
      if(child.isJumpExternalUrl == 1) {
        window.open(child.url)
      } else {
        this.$router.push({path: child.url})
      }
    },
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
