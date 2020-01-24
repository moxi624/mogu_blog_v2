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
          label-width="120px"
          ref="from"
        >
          <el-form-item label="图片域名">
            <el-input v-model="form.pictureBaseUrl" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="七牛云公钥">
            <el-input v-model="form.qiNiuAccessKey" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="七牛云私钥">
            <el-input type="password" v-model="form.qiNiuSecretKey" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="上传空间">
            <el-input  v-model="form.qiNiuBucket" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="存储区域">
            <el-select v-model="form.qiNiuArea" placeholder="请选择存储区域">
              <el-option v-for="item in options"
                         :key="item.value"
                         :label="item.label"
                         :value="item.value"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="图片上传七牛云">
            <el-switch
              v-model="form.uploadQiNiu"
              active-text="是"
              inactive-text="否">
            </el-switch>
          </el-form-item>

          <el-form-item label="图片上传本地">
            <el-switch
              v-model="form.uploadLocal"
              active-text="是"
              inactive-text="否">
            </el-switch>
          </el-form-item>

          <el-form-item label="图片显示优先级">
            <el-switch
              v-model="form.picturePriority"
              active-text="七牛云"
              inactive-text="本地"
              active-color="#13ce66"
              inactive-color="#ff4949">
            </el-switch>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()">保 存</el-button>
          </el-form-item>

        </el-form>
      </el-tab-pane>

      <el-tab-pane name="two">
        <span slot="label"><i class="el-icon-edit"></i> 邮箱配置</span>
        <el-form style="margin-left: 20px;" label-position="left"   label-width="80px" >
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
      options: [{
        value: 'z0',
        label: '华东'
      }, {
        value: 'z1',
        label: '华北'
      }, {
        value: 'z2',
        label: '华南'
      }, {
        value: 'na0',
        label: '北美'
      }, {
        value: 'as0',
        label: '东南亚'
      }],
    };
  },
  watch: {

  },
  components: {

  },
  created() {

    this.getSystemConfigList()
  },
  methods: {
    getSystemConfigList: function() {
      getSystemConfig().then(response => {
        if (response.code == "success") {
          console.log("得到的结果", response);
          if (response.data) {

            // 进行一些转换
            var form = response.data;

            this.form = this.formFormat(form, 1)

          }
        }
      });
    },
    /**
     * 格式化form，type = 1 为将 状态转换成true false
     * @param form
     * @param type
     * @returns {*}
     */
    formFormat(form, type) {

      if(type === 1) {

        if(form.uploadLocal === "1") {
          form.uploadLocal = true;
        } else {
          form.uploadLocal = false;
        }

        if(form.uploadQiNiu === "1") {
          form.uploadQiNiu = true;
        } else {
          form.uploadQiNiu = false;
        }

        if(form.picturePriority === "1") {
          form.picturePriority = true;
        } else {
          form.picturePriority = false;
        }

      } else {

        if(form.uploadLocal === true) {
          form.uploadLocal = "1";
        } else {
          form.uploadLocal = "0";
        }

        if(form.uploadQiNiu === true) {
          form.uploadQiNiu = "1";
        } else {
          form.uploadQiNiu = "0";
        }

        if(form.picturePriority === true) {
          form.picturePriority = "1";
        } else {
          form.picturePriority = "0";
        }
      }

      return form;
    },
    submitForm: function() {
      var form = this.form
      console.log("格式化前的form", form)
      form = this.formFormat(form , 0)
      console.log("格式化后的form", form)
      editSystemConfig(form).then(response => {
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
        this.getSystemConfigList();
      });
    },

  }
};
</script>

<style>

</style>

