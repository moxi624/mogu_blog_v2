<template>
<div id="table" class="app-container calendar-list-container">
	    <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
	      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit">添加</el-button>
        <el-button class="filter-item" type="primary" @click="handleReturn" icon="el-icon-refresh">返回</el-button>
        <el-button class= "button" type="primary"  @click="checkAll()" icon="el-icon-refresh">{{chooseTitle}}</el-button>
        <el-button class="filter-item" type="danger" @click="handleDelete" icon="el-icon-delete">删除选中</el-button>
        <el-button class="filter-item" type="success" @click="setCover" icon="el-icon-s-open">设为封面</el-button>
        <el-button class="filter-item" type="warning" @click="handleCropper" icon="el-icon-s-open">裁剪图片</el-button>
	    </div>

      <div class= "imgAll">
        <div v-for="picture in tableData"  v-bind:key="picture.uid" class = "imgBody" >
              <input class="inputClass" type="checkbox" :id="picture.uid" :checked="pictureUids.indexOf(picture.uid)>=0" @click="checked(picture)">
              <img class= "imgStyle" :src="BASE_IMAGE_URL + picture.pictureUrl" @click="showPicture(BASE_IMAGE_URL + picture.pictureUrl)"/>
        </div>
        <div class= "removeFloat"></div>
      </div>

		<!--分页-->
    <div class="block">
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page.sync="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
    </div>

	  <!-- 添加或修改对话框 -->
		<el-dialog :title="title" :visible.sync="dialogFormVisible">


      <!-- 相册分类 -->
<!--    <el-upload class="upload-demo"  ref="upload" name="filedatas" :action="uploadPictureHost"-->
<!--    :on-preview="handlePreview" :on-remove="handleRemove" :data="otherData"-->
<!--          :multiple="true"-->
<!--          :file-list="fileList"-->
<!--          :on-success = "fileSuccess"-->
<!--          :auto-upload="false">-->
<!--        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>-->
<!--        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitNormalUpload">上传到服务器</el-button>-->
<!--      </el-upload>-->

      <el-upload
        class="upload-demo"
        drag
        ref="upload"
        name="filedatas"
        :action="uploadPictureHost"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :data="otherData"
        :on-success = "fileSuccess"
        multiple>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传图片，且不超过5MB</div>
      </el-upload>
		</el-dialog>

    <el-dialog :visible.sync="dialogPictureVisible" fullscreen style="text-align: center">
      <div style="margin-bottom: 40px;"><span style="font-size:14px; font-weight: bold">图片地址:</span> {{dialogImageUrl}}</div>
      <img :src="dialogImageUrl" alt="">
    </el-dialog>

    <el-dialog :visible.sync="pictureCropperVisible" fullscreen>
      <PictureCropper v-if="reFresh" :modelSrc="checkedPicture.pictureUrl" @cropperSuccess="cropperSuccess"></PictureCropper>
    </el-dialog>


  </div>
</template>

<script>
import {
  getPictureList,
  addPicture,
  editPicture,
  deletePicture,
  setCover
} from "@/api/picture";
import { getToken } from '@/utils/auth'

import PictureCropper from '@/components/PictureCropper'

import { formatData } from "@/utils/webUtils";

import { Loading } from "element-ui";

export default {
  components: {
    PictureCropper
  },
  data() {
    return {
      BASE_IMAGE_URL: process.env.BASE_IMAGE_URL,
      dialogImageUrl: "", //图片显示地址
      checkedPicture: {}, // 单选的图片
      pictureCropperVisible: false, // 裁剪图片框是否显示
      dialogPictureVisible: false,
      tableData: [],
      uploadPictureHost: null,
      fileList: [],
      pictureSortUid: "",
      pictureUids: [], //图片uid集合
      chooseTitle: "全选",
      isCheckedAll: false, //是否全选
      fileUids: "", //上传时候的文件uid
      form: {
        uid: null,
        fileUid: null,
        picName: null,
        pictureSortUid: null
      },
      count: 0, //计数器，用于记录上传次数
      loading: true,
      currentPage: 1,
      pageSize: 14,
      total: null,
      title: "增加图片",
      dialogFormVisible: false,
      keyword: "",
      reFresh: true, //是否刷新组件
    };
  },
  watch: {
    checkedPicture(val) {
      this.reFresh= false
    }
  },
  created() {
    //传递过来的pictureSordUid
    this.pictureSortUid = this.$route.query.pictureSortUid;

    // 获取图片列表
    this.pictureList()

    //图片上传地址
    this.uploadPictureHost = process.env.PICTURE_API + "/file/pictures";

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
    pictureList: function() {
      var params = {};
      params.keyword = this.keyword
      params.pictureSortUid = this.pictureSortUid
      params.pageSize = this.pageSize
      params.currentPage = this.currentPage
      getPictureList(params).then(response => {
        if (response.code == "success") {
          this.tableData = response.data.records;
          this.currentPage = response.data.current;
          this.pageSize = response.data.size;
          this.total = response.data.total;
        } else {
          this.$message({
            type: "error",
            message: response.data
          });
        }
      });
    },
    getFormObject: function() {
      var formObject = {
        uid: null,
        fileUid: null,
        picName: null,
        pictureSortUid: null
      };
      return formObject;
    },
    showPicture: function(url) {
      this.dialogPictureVisible = true
      this.dialogImageUrl = url
    },
    //点击单选
    checked: function(data) {
      this.checkedPicture = data;
      let idIndex = this.pictureUids.indexOf(data.uid);
      if (idIndex >= 0) {
        //选过了
        this.pictureUids.splice(idIndex, 1);
      } else {
        this.pictureUids.push(data.uid);
      }
    },
    checkAll: function() {
      //如果是全选
      if (this.isCheckedAll) {
        this.pictureUids = [];
        this.isCheckedAll = false;
        this.chooseTitle = "全选";
      } else {
        this.pictureUids = [];
        this.tableData.forEach(function(picture) {
          this.pictureUids.push(picture.uid);
        }, this);
        this.isCheckedAll = true;
        this.chooseTitle = "取消全选";
      }
    },
    handleDelete: function() {
      if (this.pictureUids.length <= 0) {
        this.$message({
          type: "error",
          message: "请先选中图片！"
        });
        return;
      }

      this.$confirm("是否删除选中图片？, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {};
          params.uid = this.pictureUids.join(","); //将数组变成,组成
          deletePicture(params).then(response => {
            if (response.code == "success") {
              this.$message({
                type: "success",
                message: response.data
              });

              // 清空选中的列表
              this.pictureUids = []
              this.checkedPicture = []

              this.pictureList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    setCover: function() {

      if (this.pictureUids.length != 1) {
        this.$message({
          type: "error",
          message: "选择一张图片设为封面图！"
        });
        return;
      }

      this.$confirm("是否将该图片设为封面？, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {};
          params.uid = this.pictureUids[0]
          params.pictureSortUid = this.pictureSortUid
          setCover(params).then(response => {
            this.$message({
              type: "success",
              message: response.data
            });
            this.pictureUids = [];
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });

    },
    handleCropper: function() {
      if (this.pictureUids.length != 1) {
        this.$message({
          type: "error",
          message: "选择一张图片进行裁剪！"
        });
        return;
      }
      this.pictureCropperVisible = true;
      this.reFresh = true;
    },
    // 裁剪成功后的回调
    cropperSuccess: function(picture) {
      this.pictureCropperVisible = false;
      var checkedPicture = this.checkedPicture
      checkedPicture.fileUid =  picture.uid
      let params = {};
      params.uid = checkedPicture.uid
      params.fileUid = checkedPicture.fileUid
      params.picName = checkedPicture.picName
      params.pictureSortUid = checkedPicture.pictureSortUid
      editPicture(params).then(response => {
        if (response.code == "success") {
          this.$message({
            type: "success",
            message: response.data
          });
          this.pictureList();
        } else {
          this.$message({
            type: "error",
            message: response.data
          });
        }
      });

      // 清空选中的列表
      this.pictureUids = []
      this.checkedPicture = []

    },
    handleReturn: function() {
      this.$router.push({
        path: "pictureSort",
        query: {}
      });
    },
    //改变页码
    handleCurrentChange(val) {
      var that = this;
      this.currentPage = val; //改变当前所指向的页数
      this.pictureList();
    },
    handleAdd: function() {
      this.dialogFormVisible = true;
    },
    handlePreview: function() {

    },
    handleRemove: function() {

    },
    submitNormalUpload: function() {
      console.log();
      this.$refs.upload.submit();
    },
    fileSuccess: function(response, file, fileList) {

      var that = this;
      if (response.code == "success") {
        let file = response.data;
        for (let index = 0; index < file.length; index++) {
          this.fileUids = this.fileUids + file[index].uid + ",";
        }

        this.count = this.count + 1;
        if(this.count % fileList.length == 0) {
          var params = {};
          params.fileUids = this.fileUids
          params.pictureSortUid = this.pictureSortUid
          addPicture(params).then(res => {
            if (res.code == "success") {
              this.$message({
                type: "success",
                message: res.data
              });
              that.pictureList();
            } else {
              this.$message({
                type: "error",
                message: res.data
              });
            }
            this.$refs.upload.clearFiles();
            this.fileUids = "";

          });
        }
      } else {
        this.$message({
          type: "error",
          message: response.data
        });
      }
    }
  }
};
</script>

<style scoped>
.imgStyle {
  max-height: 100%;
  max-width: 100%;
  vertical-align: middle;
}
.imgBody {
  width: 150px;
  height: 150px;
  /* border: solid 1px #8080ff; */
  float: left;
  margin: 30px;
  position: relative;
}
.removeFloat {
  clear: both;
}
.imgAll {
  width: 98%;
  line-height: 150px;
  text-align: center;
  overflow-y: auto;
}
.imgLimit {
  height: 50px;
  margin-left: 30%;
  margin-top: 50px;
}
.inputClass {
  position: absolute;
}
</style>
