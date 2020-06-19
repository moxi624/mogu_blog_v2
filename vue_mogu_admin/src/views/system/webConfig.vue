<template>
  <div class="app-container">

    <el-tabs type="border-card">
      <el-tab-pane v-permission="'/webConfig/getWebConfig'">
        <span slot="label">
          <i class="el-icon-date"></i> 网站信息
        </span>

        <el-form
          style="margin-left: 20px;"
          label-position="left"
          :model="form"
          label-width="80px"
          ref="from"
        >
          <el-form-item label="LOGO">
            <div class="imgBody" v-if="form.photoList">
              <i
                class="el-icon-error inputClass"
                v-show="icon"
                @click="deletePhoto()"
                @mouseover="icon = true"
              ></i>
              <img @mouseover="icon = true" @mouseout="icon = false" v-bind:src="BASE_IMAGE_URL + form.photoList[0]">
            </div>
            <div v-else class="uploadImgBody" @click="checkPhoto">
              <i class="el-icon-plus avatar-uploader-icon"></i>
            </div>
          </el-form-item>

          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="网站名称" prop="oldPwd">
                <el-input v-model="form.name" style="width: 400px"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="标题" prop="newPwd1">
                <el-input v-model="form.title" style="width: 400px"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="关键字" prop="newPwd2">
                <el-input v-model="form.keyword" style="width: 400px"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="描述" prop="newPwd1">
                <el-input v-model="form.summary" style="width: 400px"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="作者" prop="newPwd2">
                <el-input v-model="form.author" style="width: 400px"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="备案号" prop="newPwd2">
                <el-input v-model="form.recordNum" style="width: 400px"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="3">
              <el-form-item label="阿里支付">
                <el-upload
                  class="avatar-uploader"
                  name="file"
                  :action="uploadPictureHost"
                  :file-list="fileList"
                  :show-file-list="false"
                  :before-upload="beforeUpload"
                  :on-success="fileSuccess_ali"
                  :data="otherData"
                >
                  <img v-if="form.aliPayPhoto" :src="BASE_IMAGE_URL + form.aliPayPhoto" class="avatar">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label="微信支付">
                <el-upload
                  class="avatar-uploader"
                  name="file"
                  :action="uploadPictureHost"
                  :file-list="fileList"
                  :show-file-list="false"
                  :before-upload="beforeUpload"
                  :on-success="fileSuccess_weixin"
                  :data="otherData"
                >
                  <img v-if="form.weixinPayPhoto" :src="BASE_IMAGE_URL + form.weixinPayPhoto" class="avatar">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="10">
              <el-form-item label="登录方式">
                <el-checkbox-group v-model="form.loginTypeList">
                  <el-checkbox label="1" style="margin-left: 10px">账号密码</el-checkbox>
                  <el-checkbox label="2" style="margin-left: 10px">码云</el-checkbox>
                  <el-checkbox label="3" style="margin-left: 10px">Github</el-checkbox>
                  <el-checkbox label="4" style="margin-left: 10px">QQ</el-checkbox>
                  <el-checkbox label="5" style="margin-left: 10px">微信</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="6">
              <el-form-item label="网站评论">
                <el-radio v-for="item in openDictList" :key="item.uid" v-model="form.startComment" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-permission="'/webConfig/editWebConfig'">保 存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane v-permission="'/webConfig/getWebConfig'">
        <span slot="label">
          <i class="el-icon-date"></i> 关注我们
        </span>

        <el-form
          style="margin-left: 20px;"
          label-position="left"
          :model="form"
          label-width="80px"
          :rules="rules"
          ref="form"
        >
          <el-checkbox-group v-model="form.showList">

          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" style="width: 400px"></el-input>
            <el-checkbox label="1" style="margin-left: 10px">在首页显示</el-checkbox>
          </el-form-item>

          <el-form-item label="QQ号" prop="qqNumber">
            <el-input v-model="form.qqNumber" style="width: 400px"></el-input>
            <el-checkbox label="2" style="margin-left: 10px">在首页显示</el-checkbox>
          </el-form-item>

          <el-form-item label="QQ群" prop="qqGroup">
            <el-input v-model="form.qqGroup" style="width: 400px"></el-input>
            <el-checkbox label="3" style="margin-left: 10px">在首页显示</el-checkbox>
          </el-form-item>

          <el-form-item label="github" prop="github">
            <el-input v-model="form.github" style="width: 400px"></el-input>
            <el-checkbox label="4" style="margin-left: 10px">在首页显示</el-checkbox>
          </el-form-item>

          <el-form-item label="Gitee" prop="gitee">
            <el-input v-model="form.gitee" style="width: 400px"></el-input>
            <el-checkbox label="5" style="margin-left: 10px">在首页显示</el-checkbox>
          </el-form-item>

          <el-form-item label="微信" prop="weChat">
            <el-input v-model="form.weChat" style="width: 400px"></el-input>
            <el-checkbox label="6" style="margin-left: 10px">在首页显示</el-checkbox>
          </el-form-item>

          </el-checkbox-group>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-permission="'/webConfig/editWebConfig'">保 存</el-button>
          </el-form-item>

        </el-form>
      </el-tab-pane>

    </el-tabs>

    <CheckPhoto
      @choose_data="getChooseData"
      @cancelModel="cancelModel"
      :photoVisible="photoVisible"
      :photos="photoList"
      :files="fileIds"
      :limit="1"
    ></CheckPhoto>
  </div>
</template>

<script>
import CheckPhoto from "../../components/CheckPhoto";
import { getToken } from '@/utils/auth'
import { getWebConfig, editWebConfig } from "@/api/webConfig";
import { Loading } from 'element-ui';
import {getListByDictType} from "@/api/sysDictData"

export default {
  data() {
    return {
      BASE_IMAGE_URL: process.env.BASE_IMAGE_URL,
      form: {
        name: "",
        title: "",
        keyword: "",
        summary: "",
        author: "",
        logo: "",
        recordNum: "",
        startComment: "1",
        aliPay: "",
        weixinPay: "",
        aliPayPhoto: "",
        weixinPayPhoto: "",
        showList: [],
        loginTypeList: []
      },
      loadingInstance: null, // loading对象
      fileList: [],
      photoVisible: false, //控制图片选择器的显示
      photoList: [],
      fileIds: "",
      icon: false, //控制删除图标的显示
      otherData: {},
      openDictList: [], //字典
      rules: {
        qqNumber: [
          {pattern:  /[1-9]([0-9]{5,11})/, message: '请输入正确的QQ号码'},
        ],
        qqGroup: [
          {pattern:  /-?[1-9]\d*/, message: '请输入正确的QQ群'},
        ],
        gitee: [
          {pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/, message: '请输入正确的Gitee地址'},
        ],
        github: [
          {pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/, message: '请输入正确的Github地址'},
        ],
        email: [
          {pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/, message: '请输入正确的邮箱'},
        ]
      }
    };
  },
  watch: {
    "form.aliPay": {
      handler(newVal, oldVal) {
        console.log("value change", oldVal, newVal);
      },
      deep: true
    },
    "form.weixinPay": {
      handler(newVal, oldVal) {
        console.log("value change", oldVal, newVal);
      },
      deep: true
    }
  },
  components: {
    CheckPhoto
  },
  created() {

    // 获取配置
    this.getWebConfigFun();

    //图片上传地址
    this.uploadPictureHost = process.env.PICTURE_API + "/file/cropperPicture";

    // 查询字典
    this.getDictList()

    //其它数据
    this.otherData = {
      source: "picture",
      userUid: "uid00000000000000000000000000000000",
      adminUid: "uid00000000000000000000000000000000",
      projectName: "blog",
      sortName: "admin",
      token: getToken()
    };

  },
  methods: {
    /**
     * 字典查询
     */
    getDictList: function () {
      // 获取系统开关
      var params = {};
      params.dictType = 'sys_normal_disable';
      getListByDictType(params).then(response => {
        if (response.code == "success") {
          this.openDictList = response.data.list;
        }
      });
    },
    getWebConfigFun: function() {
      getWebConfig().then(response => {
        console.log("得到的配置", response)
        if (response.code == "success") {

          let data = response.data;

          if (data.showList) {
            let showList = JSON.parse(data.showList)
            let loginTypeList = JSON.parse(data.loginTypeList)
            data.showList = showList;
            data.loginTypeList = loginTypeList;
            this.form = data;
          } else {
            data.showList = []
            this.form = data;
          }
          this.fileIds = this.form.logo;
          this.photoList = this.form.photoList;
        }
      });
    },
    //弹出选择图片框
    checkPhoto: function() {
      this.photoVisible = true;
      console.log(this.photoVisible);
    },
    getChooseData(data) {
      var that = this;
      this.photoVisible = false;
      this.photoList = data.photoList;
      this.fileIds = data.fileIds;
      var fileId = this.fileIds.replace(",", "");
      if (this.photoList.length >= 1) {
        this.form.fileUid = fileId;
        this.form.photoList = this.photoList;
      }
    },
    //关闭模态框
    cancelModel() {
      this.photoVisible = false;
    },
    deletePhoto: function() {
      this.form.photoList = null;
      this.form.fileUid = "";
      this.fileIds = "";
      this.icon = false;
    },
    checkPhoto() {
      this.photoList = [];
      this.fileIds = "";
      this.photoVisible = true;
    },
    submitForm: function() {

      let form = this.form;
      form.logo = this.fileIds;
      form.showList = JSON.stringify(this.form.showList)
      form.loginTypeList = JSON.stringify(this.form.loginTypeList)
      editWebConfig(form).then(response => {
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
        this.getWebConfigFun();
      });
    },

    beforeUpload: function(file) {
      this.loadingInstance = Loading.service({ fullscreen: true, text:'正在努力上传中~' });
    },
    fileSuccess_ali: function(response, file, fileList) {
      console.log(response);
      if (response.code == "success") {
        let fileList = response.data;
        if (fileList.length > 0) {
          this.form.aliPay = fileList[0].uid;
          this.form.aliPayPhoto = fileList[0].url;
          var tempForm = this.form;
          this.form = {};
          this.form = tempForm;
        }
        this.$message({
          type: 'success',
          message: "上传成功"
        })
      }
      this.loadingInstance.close();
    },

    fileSuccess_weixin: function(response, file, fileList) {

      if (response.code == "success") {
        let fileList = response.data;
        if (fileList.length > 0) {
          this.form.weixinPay = fileList[0].uid;
          this.form.weixinPayPhoto = fileList[0].url;
          var tempForm = this.form;
          this.form = {};
          this.form = tempForm;
        }
        this.$message({
          type: 'success',
          message: "上传成功"
        })
      }
      this.loadingInstance.close();
    }
  }
};
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  margin: 0, 0, 0, 10px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.imgBody {
  width: 100px;
  height: 100px;
  border: solid 2px #ffffff;
  float: left;
  position: relative;
}
.uploadImgBody {
  margin-left: 5px;
  width: 100px;
  height: 100px;
  border: dashed 1px #c0c0c0;
  float: left;
  position: relative;
}
.uploadImgBody :hover {
  border: dashed 1px #00ccff;
}
.inputClass {
  position: absolute;
}
img {
  width: 100px;
  height: 100px;
}
</style>

