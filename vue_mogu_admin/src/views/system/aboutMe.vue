<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <el-tabs type="border-card">
      <el-tab-pane label="关于我">
        <span slot="label"><i class="el-icon-star-on"></i> 关于我</span>
        <el-form style="margin-left: 20px;" label-position="left" :model="form" label-width="100px" ref="changeAdminForm">
          <el-form-item label="用户头像">
            <div class="imgBody" v-if="form.photoList">
                <i class="el-icon-error inputClass" v-show="icon" @click="deletePhoto()" @mouseover="icon = true"></i>
              <img @mouseover="icon = true" @mouseout="icon = false" v-bind:src="BASE_IMAGE_URL + form.photoList[0]" />	    		 
            </div>
            <div v-else class="uploadImgBody" @click="checkPhoto">
              <i class="el-icon-plus avatar-uploader-icon"></i>
            </div>
          </el-form-item>

          <el-form-item label="昵称">
            <el-input v-model="form.nickName" style="width: 400px"></el-input>
          </el-form-item>

            <el-form-item label="性别">
              <el-radio v-model="form.gender" label="1" border size="medium">男</el-radio>
              <el-radio v-model="form.gender" label="2" border size="medium">女</el-radio>
            </el-form-item>
          
          <!-- <el-form-item label="手机号">
            <el-input v-model="form.mobile" style="width: 400px"></el-input>
          </el-form-item> -->
          
          <el-form-item label="邮箱">
            <el-input v-model="form.email" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="QQ号">
            <el-input v-model="form.qqNumber" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="Github">
            <el-input v-model="form.github" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="Gitee">
            <el-input v-model="form.gitee" style="width: 400px"></el-input>
          </el-form-item>

          <!-- <el-form-item label="微信号">
            <el-input v-model="form.weChat" style="width: 400px"></el-input>
          </el-form-item> -->
          
          <el-form-item label="职业">
            <el-input v-model="form.occupation" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="简介">
            <el-input
              type="textarea"
              :autosize="{ minRows: 3, maxRows: 10}"
              placeholder="请输入内容"
              style="width: 400px"
              v-model="form.summary">
            </el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="submitForm('changeAdminForm')">保 存</el-button>
            <!-- <el-button @click="cancel('changeAdminForm')">重 置</el-button>             -->
          </el-form-item>

        </el-form>
      </el-tab-pane>

      <!-- <el-tab-pane label="显示配置" name="second">
      <span slot="label"><i class="el-icon-setting"></i> 显示配置</span>
        <el-form style="margin-left: 20px;" label-position="left" :model="form" label-width="100px">
        
        <el-tag style="font-size: 15px; margin:10px 0 10px 0;" type="warning" >这里主要是控制web前端，关于我页面中，一些个人信息的显示</el-tag>

        <el-form-item label="手机">
          <el-switch v-model="form.delivery"></el-switch>
        </el-form-item>

        <el-form-item label="微信">
          <el-switch v-model="form.delivery"></el-switch>
        </el-form-item>

        <el-form-item label="QQ号">
          <el-switch v-model="form.delivery"></el-switch>
        </el-form-item>

        <el-form-item label="职业">
          <el-switch v-model="form.delivery"></el-switch>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">保 存</el-button>
          <el-button @click="cancel">重 置</el-button>          
        </el-form-item>
        
        </el-form>
        
      </el-tab-pane> -->

      <el-tab-pane label="修改密码" name="third">
        <span slot="label"><i class="el-icon-edit"></i> 修改密码</span>
        <el-form :rules="rules" style="margin-left: 20px;" label-position="left" :model="changePwdForm"  label-width="80px" ref="changePwdForm">
          <el-form-item label="旧密码" prop="oldPwd">
            <el-input type="password" v-model="changePwdForm.oldPwd" style="width: 400px"></el-input>
          </el-form-item>
          
          <el-form-item label="新密码" prop="newPwd1">
            <el-input type="password" v-model="changePwdForm.newPwd1" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="重复输入" prop="newPwd2">
            <el-input type="password" v-model="changePwdForm.newPwd2" style="width: 400px"></el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="submitForm('changePwdForm')">保 存</el-button>
            <el-button @click="cancel('changePwdForm')">重 置</el-button>          
          </el-form-item>

        </el-form>
      </el-tab-pane>   

    </el-tabs>

  <!--
    作者：xzx19950624@qq.com
    时间：2018年9月23日16:16:09
    描述：图片选择器
  -->
	<CheckPhoto @choose_data="getChooseData" @cancelModel="cancelModel" :photoVisible="photoVisible" :photos="photoList" :files="fileIds" :limit="1"></CheckPhoto>
  </div>
</template>

<script>
import CheckPhoto from "../../components/CheckPhoto";
import CKEditor from "../../components/CKEditor";
import { getMe, editMe, changePwd } from "@/api/system";
export default {
  data() {
    return {
      BASE_IMAGE_URL: process.env.BASE_IMAGE_URL,
      form: {},
      changePwdForm: {
        oldPwd: "",
        newPwd1: "",
        newPwd2: ""
      },
      photoVisible: false, //控制图片选择器的显示
      photoList: [],
      fileIds: "",
      icon: false, //控制删除图标的显示

      //定义规则
      rules: {
        oldPwd: [
          { required: true, message: "请输入原来密码", trigger: "blur" }
        ],
        newPwd1: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          { min: 5, message: "密码长度需要大于等于 5 个字符", trigger: "blur" }
        ],
        newPwd2: [
          { required: true, message: "请再次输入新密码", trigger: "blur" },
          { min: 5, message: "密码长度需要大于等于 5 个字符", trigger: "blur" }
        ]
      }
    };
  },
  components: {
    CheckPhoto,
    CKEditor
  },
  created() {
    var tagParams = new URLSearchParams();
    getMe(tagParams).then(response => {
      console.log(response);
      if (response.code == "success") {
        this.form = response.data;
        this.fileIds = this.form.avatar;
        this.photoList = this.form.photoList;
      }
    });
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

    submitForm: function(type) {
      switch (type) {
        // 1、改变用户信息
        case "changeAdminForm":
          {
            this.form.avatar = this.fileIds;
            console.log("提交的内容", this.form);
            editMe(this.form).then(response => {
              console.log(response);
              this.$notify({
                title: "成功",
                message: "保存成功！",
                type: "success"
              });
            });
          }
          break;

        //3、改变密码
        case "changePwdForm":
          {
            this.$refs[type].validate(valid => {
              if (valid) {
                if (this.changePwdForm.newPwd1 != this.changePwdForm.newPwd2) {
                  this.$notify.error({
                    title: "警告",
                    message: "两次输入密码不一致"
                  });
                  return false;
                }
                if (this.changePwdForm.oldPwd == this.changePwdForm.newPwd2) {
                  this.$notify.error({
                    title: "警告",
                    message: "新密码不能和旧密码一致"
                  });
                  return false;
                }
                var params = new URLSearchParams();
                params.append("oldPwd", this.changePwdForm.oldPwd);
                params.append("newPwd", this.changePwdForm.newPwd1);
                changePwd(params).then(response => {
                  console.log(response);
                  if (response.code == "success") {
                    this.$notify({
                      title: "成功",
                      message: response.data,
                      type: "success"
                    });
                    this.cancel(type);
                  } else {
                    this.$notify.error({
                      title: "警告",
                      message: response.data
                    });
                  }
                });
              } else {
                console.log("error submit!!");
                return false;
              }
            });
          }
          break;
      }
    },
    cancel: function(type) {
      console.log("点击了重置", type);      
      this.$refs[type].resetFields();
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
