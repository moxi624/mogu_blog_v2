<template>
  <div class="menu-wrapper">
    <template v-for="(item,index) in items" v-if="items.length>0">
      <el-submenu :key="index" :index="index+''">
        <template slot="title">
          <svg-icon v-if="item.parent.icon" :icon-class="item.parent.icon"></svg-icon>
          <span v-if="item.parent.name" slot="title">{{item.parent.name}}</span>
        </template>

        <template v-for="(child,index2) in item.sonItem">          
          <router-link  :to="child.url" :key="index2">
            <el-menu-item :index="child.url" :key="child.name">
              <svg-icon v-if="child.icon" :icon-class="child.icon"></svg-icon>
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
 
  methods: {
    hasOneShowingChildren(children) {
      const showingChildren = children.filter(item => {
        return !item.hidden
      })
      if (showingChildren.length === 1) {
        return true
      }
      return false
    }
  }
}
</script>
