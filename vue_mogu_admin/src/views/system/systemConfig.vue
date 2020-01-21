<template>
  <div class="app-container">
    <el-tabs type="border-card">
      <el-tab-pane>
        <span slot="label">
          <i class="el-icon-date"></i> 七牛云配置
        </span>

        <el-form
          style="margin-left: 20px;"
          label-position="left"
          :model="form"
          label-width="100px"
          ref="from"
        >
          <el-form-item label="七牛云公钥">
            <el-input v-model="form.qiNiuAccessKey" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="七牛云私钥">
            <el-input type="password" v-model="form.qiNiuSecretKey" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()">保 存</el-button>
          </el-form-item>

        </el-form>
      </el-tab-pane>

      <el-tab-pane name="two">
        <span slot="label"><i class="el-icon-edit"></i> 邮箱配置</span>
        <el-form :rules="rules" style="margin-left: 20px;" label-position="left" :model="changePwdForm"  label-width="80px" ref="changePwdForm">
          <el-form-item label="邮箱" prop="oldPwd">
            <el-input  v-model="form.email" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="密码" prop="newPwd1">
            <el-input type="password" v-model="form.emailPassword" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="用户名" prop="newPwd2">
            <el-input  v-model="form.emailUserName" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="SMTP地址" prop="newPwd2">
            <el-input  v-model="form.smtpAddress" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="SMTP端口" prop="newPwd2">
            <el-input  v-model="form.smtpPort" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()">保 存</el-button>
          </el-form-item>

        </el-form>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import { getSystemConfig, editSystemConfig } from "@/api/systemConfig";

export default {
  data() {
    return {
      form: {

      },
    };
  },
  watch: {

  },
  components: {

  },
  created() {
    getSystemConfig().then(response => {
      if (response.code == "success") {
        console.log("得到的结果", response);
        if (response.data) {
          this.form = response.data;
        }
      }
    });

  },
  methods: {
    submitForm: function() {
      editSystemConfig(this.form).then(response => {
        if ((response.code = "success")) {
          this.$notify({
            title: "成功",
            message: response.data,
            type: "success"
          });
        } else {
          this.$notify.error({
            title: "警告",
            message: response.data
          });
        }
      });
    },

  }
};
</script>

<style>

</style>

