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
          <el-form-item label="本地图片域名">
            <el-input v-model="form.localPictureBaseUrl" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="七牛云图片域名">
            <el-input v-model="form.qiNiuPictureBaseUrl" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="七牛云公钥">
            <el-input v-model="form.qiNiuAccessKey" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="七牛云私钥">
            <el-input type="password" v-model="form.qiNiuSecretKey" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="上传空间">
            <el-input  v-model="form.qiNiuBucket" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="存储区域">
            <el-select v-model="form.qiNiuArea" placeholder="请选择存储区域" clearable>
              <el-option v-for="item in areaDictList"
                         :key="item.dictValue"
                         :label="item.dictLabel"
                         :value="item.dictValue"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="图片上传七牛云">
            <el-radio v-for="item in yesNoDictList" :key="item.uid" v-model="form.uploadQiNiu" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item label="图片上传本地">
            <el-radio v-for="item in yesNoDictList" :key="item.uid" v-model="form.uploadLocal" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>


          <el-form-item label="图片显示优先级">
            <el-radio v-for="item in picturePriorityDictList" :key="item.uid" v-model="form.picturePriority" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
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
import {getListByDictTypeList} from "@/api/sysDictData"
import { Loading } from 'element-ui';
export default {
  data() {
    return {
      form: {

      },
      areaDictList: [], //存储区域字典
      yesNoDictList: [], //是否字典
      picturePriorityDictList: [], //图片显示优先级字典
      loadingInstance: null, // loading对象
    };
  },
  watch: {

  },
  components: {

  },
  created() {
    this.loadingInstance = Loading.service({ fullscreen: true, text:'正在努力加载中~' });
    // 获取字典
    this.getDictList()

    // 获取系统配置
    this.getSystemConfigList()
  },
  methods: {
    /**
     * 字典查询
     */
    getDictList: function () {

      var dictTypeList =  ['sys_yes_no', 'sys_picture_priority', 'sys_storage_region']

      getListByDictTypeList(dictTypeList).then(response => {
        if (response.code == "success") {
          var dictMap = response.data;

          this.areaDictList = dictMap.sys_storage_region.list

          this.yesNoDictList = dictMap.sys_yes_no.list

          this.picturePriorityDictList = dictMap.sys_picture_priority.list

          this.loadingInstance.close();
        } else {
          this.loadingInstance.close();
        }
      });
    },
    getSystemConfigList: function() {
      getSystemConfig().then(response => {
        if (response.code == "success") {
          if (response.data) {
            this.form = response.data;
          }
        }
      });
    },

    submitForm: function() {
      editSystemConfig(this.form).then(res => {
        console.log(res);
        if (res.code = "success") {
          this.$message({
            type: "success",
            message: res.data
          });
        } else {
          this.$message({
            type: "warning",
            message: res.data
          });
        }
      });
    },

  }
};
</script>

<style>

</style>

