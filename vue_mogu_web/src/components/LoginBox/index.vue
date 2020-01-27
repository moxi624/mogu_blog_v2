<template>
  <div>
    <div class="box loginBox" v-if="showLogin == true">
      <div class="title"  >
        <span class="t1">
          登录
        </span>
        <div class="t2" @click="closeLogin()">
          X
        </div>
      </div>
      <el-divider></el-divider>
      <el-form :label-position="labelPosition" :model="loginForm">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.userName" disabled></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="loginForm.password" disabled></el-input>
        </el-form-item>
        <el-row class="btn">
          <el-button class="loginBtn" type="primary" @click="startLogin" disabled>登录</el-button>
          <el-button class="registerBtn" type="info" @click="goRegister" disabled>注册</el-button>
        </el-row>

        <el-row class="elRow">
          <el-tooltip content="码云" placement="bottom">
            <el-button type="danger" circle @click="goAuth('gitee')">
              <span class="iconfont">&#xe602;</span>
            </el-button>
          </el-tooltip>

          <el-tooltip content="Github" placement="bottom">
            <el-button type="info" circle @click="goAuth('github')">
              <span class="iconfont">&#xe64a;</span>
            </el-button>
          </el-tooltip>

          <el-tooltip content="QQ" placement="bottom">
            <el-button type="primary" circle disabled>
              <span class="iconfont">&#xe601;</span>
            </el-button>
          </el-tooltip>

          <el-tooltip content="微信" placement="bottom">
            <el-button type="success" circle disabled>
              <span class="iconfont">&#xe66f;</span>
            </el-button>
          </el-tooltip>

        </el-row>
        <div class="loginTip">目前仅支持码云和Github登录</div>
      </el-form>
    </div>

    <div class="box registerBox" v-if="showLogin == false">
      <div class="title">
        <span class="t1">
          登录
        </span>
        <div class="t2" @click="closeLogin()">
          X
        </div>
      </div>
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

  </div>
</template>

<script>
  import {login, register} from "@/api/user";
  import { Loading } from 'element-ui';
  export default {
    name: "share",
    data() {
      return {
        loading: null,
        option: {
          fullscreen: true,
          lock: true
        }, //loading
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
    components: {},
    created() {

    },
    methods: {
      startLogin: function () {
        // var params = {};
        // params.userName = this.loginForm.userName;
        // params.passWord = this.loginForm.password;
        // params.isRememberMe = 0;
        // login(params).then(response => {
        //   if (response.code == "success") {
        //     console.log(response.data);
        //   }
        // });
      },
      startRegister: function () {

        // var params = {};
        // params.userName = this.registerForm.userName;
        // params.passWord = this.registerForm.password;
        // params.email = this.registerForm.email;
        // console.log("登录表单", params);
        // register(params).then(response => {
        //   if (response.code == "success") {
        //     console.log(response.data);
        //   }
        // });
      },
      goLogin: function () {
        this.showLogin = true;
      },
      goRegister: function () {
        this.showLogin = false;
      },
      goAuth: function (source) {
        this.loading = Loading.service({
          lock: true,
          text: '加载中……',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        var params = new URLSearchParams();
        params.append("source", source);
        login(params).then(response => {
          if (response.code == "success") {
            var token = response.data.token;
            console.log(response);
            window.location.href = response.data.url
          }
        });
      },
      closeLogin: function() {
        this.$emit("closeLoginBox", "");
      }
    }
  };
</script>


<style>
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
    z-index: 101; /* 要比遮罩层大 */
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
  .box .title .t2 {
    font-size: 16px;
    float: right;
    margin-right: 6px;
    margin-top: -6px;
    cursor: pointer;
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
    z-index: 101; /* 要比遮罩层大 */
  }

  /* 遮罩层 */
  .mask {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 100;
  }
</style>
