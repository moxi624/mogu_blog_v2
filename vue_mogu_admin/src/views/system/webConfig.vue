<template>
  <div class="app-container">
    <el-tabs type="border-card">
      <el-tab-pane>
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
              <img @mouseover="icon = true" @mouseout="icon = false" v-bind:src="form.photoList[0]">
            </div>
            <div v-else class="uploadImgBody" @click="checkPhoto">
              <i class="el-icon-plus avatar-uploader-icon"></i>
            </div>
          </el-form-item>

          <el-form-item label="网站名称" prop="oldPwd">
            <el-input v-model="form.name" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="标题" prop="newPwd1">
            <el-input v-model="form.title" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="关键字" prop="newPwd2">
            <el-input v-model="form.keyword" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="描述" prop="newPwd1">
            <el-input v-model="form.summary" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="作者" prop="newPwd2">
            <el-input v-model="form.author" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="备案号" prop="newPwd2">
            <el-input v-model="form.recordNum" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="支付宝打赏码">
            <el-upload
              class="avatar-uploader"
              :action="uploadPictureHost"
              :show-file-list="false"
              :on-success = "fileSuccess"
              :auto-upload="false"
            >
              <img v-if="form.aliPayPhoto" :src="form.aliPayPhoto" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>

          <el-form-item label="微信打赏码">
            <el-upload
              class="avatar-uploader"
              :action="uploadPictureHost"
              :show-file-list="false"
              :on-success = "fileSuccess"
              :data="otherData"
            >
              <img v-if="form.weixinPayPhoto" :src="form.weixinPayPhoto" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>

          <el-form-item label="网站评论">
            <template>
              <el-radio v-model="form.startComment" label="0">关闭</el-radio>
              <el-radio v-model="form.startComment" label="1">开启</el-radio>
            </template>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()">保 存</el-button>
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

import { getWebConfig, editWebConfig } from "@/api/webConfig";

export default {
  data() {
    return {
      form: {
        name: "",
        title: "",
        keyword: "",
        summary: "",
        author: "",
        logo: "",
        recordNum: "",
        startComment: "1",
        aliPayPhoto: "",
        weixinPayPhoto: ""
      },
      fileList: [],
      photoVisible: false, //控制图片选择器的显示
      photoList: [],
      fileIds: "",
      icon: false, //控制删除图标的显示
      otherData: {},
    };
  },
  components: {
    CheckPhoto
  },
  created() {
    getWebConfig().then(response => {
      console.log(response);
      if (response.code == "success") {
        if (response.data) {
          this.form = response.data;
          this.fileIds = this.form.logo;
          this.photoList = this.form.photoList;
        }
      }
    });

    //图片上传地址
    this.uploadPictureHost = process.env.PICTURE_API + "/file/pictures";

    //其它数据
    this.otherData = {
      userUid: "uid00000000000000000000000000000000",
      adminUid: "uid00000000000000000000000000000000",
      projectName: "blog",
      sortName: "admin"
    };
  },
  methods: {
    //弹出选择图片框
    checkPhoto: function() {
      console.log(this.photoVisible);
      console.log("点击了选择图");
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
      console.log("点击了删除图片");
      this.form.photoList = null;
      this.form.fileUid = "";
      this.icon = false;
    },
    checkPhoto() {
      this.photoList = [];
      this.fileIds = "";
      this.photoVisible = true;
    },
    submitForm: function() {
      this.form.logo = this.fileIds;
      editWebConfig(this.form).then(response => {
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

    submitNormalUpload: function() {
      this.$refs.upload.submit();
    },

    fileSuccess: function(response, file, fileList) {
      console.log(response);
      if (response.code == "success") {
        let fileList = response.data;
        var fileUids = "";
        for (let index = 0; index < fileList.length; index++) {
          fileUids = fileUids + fileList[index].uid + ",";
        }
        console.log("开始上传图片");
        var params = new URLSearchParams();
        params.append("fileUids", fileUids);
        params.append("pictureSortUid", this.pictureSortUid);
        addPicture(params).then(response => {
          if (response.code == "success") {
            console.log("上传成功");
            this.$message({
              type: "success",
              message: response.data
            });
            this.pictureList();
          }
        });
      }
    }
  }
};
</script>

<style>
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
.img {
  width: 100%;
  height: 100%;
}
img {
  width: 100px;
  height: 100px;
}
</style>

