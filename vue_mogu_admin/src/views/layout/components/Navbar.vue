<template>
  <div class="dashboard-editor-container">
    <el-menu class="navbar" mode="horizontal">
      <hamburger
        :toggle-click="toggleSideBar"
        :is-active="sidebar.opened"
        class="hamburger-container"
      />

      <breadcrumb/>

      <div class="right-menu">

        <el-tooltip content="门户页面" effect="dark" placement="bottom">
          <Website id="website" class="right-menu-item" />
        </el-tooltip>

        <el-tooltip content="Gitee源码" effect="dark" placement="bottom">
          <MoGuGit id="mogu-git" class="right-menu-item" />
        </el-tooltip>

        <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <MoGuDoc id="mogu-doc" class="right-menu-item" />
        </el-tooltip>

        <el-tooltip effect="dark" content="全屏" placement="bottom">
          <screenfull class="screenfull right-menu-item"></screenfull>
        </el-tooltip>

        <el-tooltip effect="dark" content="换肤" placement="bottom">
          <theme-picker class="theme-switch right-menu-item"></theme-picker>
        </el-tooltip>

        <el-dropdown class="avatar-container" trigger="click">
          <div class="avatar-wrapper">
            <img :src="avatar+'?imageView2/1/w/80/h/80'" class="user-avatar">
            <i class="el-icon-caret-bottom"/>
          </div>
          <el-dropdown-menu slot="dropdown" class="user-dropdown">
            <el-dropdown-item>
              <span style="display:block;" @click="aboutMe">关于我</span>
            </el-dropdown-item>
            <el-dropdown-item divided>
              <span style="display:block;" @click="showLog">更新日志</span>
            </el-dropdown-item>
            <el-dropdown-item divided>
              <span style="display:block;" @click="logout">退出</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-menu>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import Hamburger from "@/components/Hamburger";
import Screenfull from "@/components/Screenfull";
import ThemePicker from "@/components/ThemePicker";
import MoGuGit from '@/components/MoGu/Git'
import MoGuDoc from '@/components/MoGu/Doc'
import Website from '@/components/MoGu/Website'

export default {
  components: {
    Screenfull,
    Breadcrumb,
    Hamburger,
    ThemePicker,
    MoGuGit,
    MoGuDoc,
    Website
  },
  data() {
    return {
      title: "更新日志",
      activeName: "1"
    };
  },
  computed: {
    ...mapGetters(["sidebar", "avatar"])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch("ToggleSideBar");
    },
    logout() {
      this.$store.dispatch("LogOut").then(() => {
        // 为了重新实例化vue-router对象 避免bug
        location.reload();
      });
    },
    showLog: function() {
      console.log("点击了显示日志");
      window.open("https://gitee.com/moxi159753/mogu_blog_v2/releases", "_blank")
    },
    aboutMe: function () {
      this.$router.push({path:'/system/aboutMe'})
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .breadcrumb-container {
    float: left;
  }
  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .right-menu {
    float: right;
    height: 100%;
    &:focus {
      outline: none;
    }
    .right-menu-item {
      display: inline-block;
      margin: 0 8px;
    }
    .screenfull {
      height: 20px;
    }
    .international {
      vertical-align: top;
    }
    .theme-switch {
      vertical-align: 15px;
    }
    .avatar-container {
      height: 50px;
      margin-right: 30px;
      .avatar-wrapper {
        cursor: pointer;
        margin-top: 5px;
        position: relative;
        .user-avatar {
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>

