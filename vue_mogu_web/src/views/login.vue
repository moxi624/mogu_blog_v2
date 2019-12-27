<template>
  <div>
    <el-button @click="dialog = true" type="primary" style="margin-left: 16px;">点我打开</el-button>

    <div class="box loginBox." v-if="showLogin == true">
      <div class="title">登录</div>
      <el-divider></el-divider>
      <el-form :label-position="labelPosition" :model="loginForm">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="loginForm.password"></el-input>
        </el-form-item>
        <el-row class="btn">
          <el-button class="loginBtn" type="primary" @click="startLogin">登录</el-button>
          <el-button class="registerBtn" type="info" @click="goRegister">注册</el-button>
        </el-row>

        <el-row class="elRow">
          <el-tooltip content="码云" placement="bottom">
            <el-button icon="el-icon-search" circle @click="goAuth('gitee')"></el-button>
          </el-tooltip>

          <el-tooltip content="Github" placement="bottom">
            <el-button type="primary" icon="el-icon-edit" circle @click="goAuth('github')"></el-button>
          </el-tooltip>

          <el-tooltip content="QQ" placement="bottom">
            <el-button type="success" icon="el-icon-check" circle></el-button>
          </el-tooltip>
        </el-row>
        <div class="loginTip">登录过的用户请沿用之前的登录方式</div>
      </el-form>
    </div>

    <div class="box registerBox" v-if="showLogin == false">
      <div class="title">注册</div>
      <el-divider></el-divider>
      <el-form :label-position="labelPosition" :model="registerForm">
        <el-form-item label="用户名">
          <el-input v-model="registerForm.userName"></el-input>
        </el-form-item>

        <el-form-item label="密码">
          <el-input type="password" v-model="registerForm.password"></el-input>
        </el-form-item>

        <el-form-item label="重复密码">
          <el-input type="password" v-model="registerForm.password2"></el-input>
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="registerForm.email"></el-input>
        </el-form-item>

        <el-row class="btn">
          <el-button class="loginBtn" type="primary" @click="startRegister">注册</el-button>
          <el-button class="registerBtn" type="info" @click="goLogin">返回登录</el-button>
        </el-row>

        <div class="loginTip">注册后，需要到邮箱进行邮件认证~</div>
      </el-form>
    </div>

    <div class="mask"></div>

    <el-drawer
      :before-close="handleClose"
      :visible.sync="dialog"
      :show-close="false"
      direction="ltr"
      custom-class="demo-drawer"
      ref="drawer"
    >
      <div class="contain">
        <el-tabs class="elTabs" type="border-card" v-if="isLogin">
          <el-tab-pane>
            <span slot="label">
              <i class="el-icon-date"></i> 我的
            </span>
            <div style="height:1000px;">
              <div>我的</div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="消息中心">
            <div style="height:1000px;">
              <div>消息中心</div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="个人设置">
            <div style="height:1000px;">
              <div>个人设置</div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="退出登录">
            <div style="height:1000px;">
              <div>退出登录</div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>
  </div>
</template>

<script>
  import Head from "../components/Head";
  import BlogHead from "../components/BlogHead";
  import BlogFooter from "../components/BlogFooter";
  import { login, logout, register  } from "../api/user";

  export default {
    name: "share",
    data() {
      return {
        // 显示登录页面
        showLogin: true,
        isLogin: false,
        table: false,
        dialog: false,
        loading: false,
        labelPosition: "right",
        loginForm: {
          userName: "",
          password: ""
        },
        registerForm: {
          userName: "",
          password: "",
          password2: "",
          email: ""
        }
      };
    },
    components: {
      //注册组件
      BlogHead,
      BlogFooter,
      Head
    },
    created() {},
    methods: {
      handleClose(done) {
        if (this.loading) {
          return;
        }
        this.$confirm("确定要提交表单吗？")
          .then(_ => {
            this.loading = true;
            this.timer = setTimeout(() => {
              done();
              // 动画关闭需要一定的时间
              setTimeout(() => {
                this.loading = false;
              }, 400);
            }, 500);
          })
          .catch(_ => {});
      },
      cancelForm() {
        this.loading = false;
        this.dialog = false;
        clearTimeout(this.timer);
      },
      startLogin: function() {
        var params = {};
        params.userName = this.loginForm.userName;
        params.passWord = this.loginForm.password;
        params.isRememberMe = 0;
        console.log("登录表单", params);
        login(params).then(response => {
          if (response.code == "success") {
            console.log(response.data);
          }
        });
      },
      startRegister: function() {

        var params = {};
        params.userName = this.registerForm.userName;
        params.passWord = this.registerForm.password;
        params.email = this.registerForm.email;
        console.log("登录表单", params);
        register(params).then(response => {
          if (response.code == "success") {
            console.log(response.data);
          }
        });
      },
      goLogin: function() {
        console.log("去登录页面");
        this.showLogin = true;
      },
      goRegister: function() {
        console.log("去注册页面");
        this.showLogin = false;
      },
      goAuth: function (source) {
        console.log("go", source)
        var params = new URLSearchParams();
        params.append("source", source);
        login(params).then(response => {
          if (response.code == "success") {
            console.log(response.data.url);
            var token = response.data.token;
            console.log(response);
            window.location.href = response.data.url

            //window.open(response.data);
          }
        });
        // this.$router.push({name: '/oauth/render/gitee'});
      },
    }
  };
</script>


<style>
  .elTabs {
    margin-top: 50px;
  }
  .contain {
    width: 100%;
    height: 100%;
    background: yellow;
  }
  .box {
    width: 400px;
    height: 420px;
    background: white;
    position: fixed;
    margin: auto;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    z-index: 1000; /* 要比遮罩层大 */
  }
  .registerBox {
    height: 570px;
  }
  .box .title {
    height: 48px;
    font-size: 22px;
    font-weight: bold;
    text-align: center;
    line-height: 48px;
  }
  .box .el-divider--horizontal {
    margin: 12px 0;
  }
  .box .el-form-item__label {
    margin-left: 10px;
    font-size: 16px;
  }
  .box .el-input__inner {
    margin-left: 10px;
    width: 90%;
  }
  .box .btn {
    text-align: center;
  }
  .box .loginBtn {
    width: 40%;
  }
  .box .registerBtn {
    width: 40%;
  }
  .elRow {
    margin-top: 15px;
    text-align: center;
  }
  .loginTip {
    margin-top: 10px;
    font-size: 14px;
    text-align: center;
    color: #bababa;
  }

  .remarksBox {
    position: fixed;
    left: 50%;
    margin-left: -100px;
    top: 50%;
    margin-top: -50px;
    border: 1px solid red;
    width: 200px;
    height: 100px;
    text-align: center;
    z-index: 1000; /* 要比遮罩层大 */
  }
  /* 遮罩层 */
  .mask {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
  }
</style>
