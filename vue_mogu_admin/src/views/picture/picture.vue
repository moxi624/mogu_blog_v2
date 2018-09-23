<template>
<div id="table" class="app-container calendar-list-container">
	    <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
	      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit">添加</el-button>
        <el-button class="filter-item" type="info" @click="handleReturn" icon="el-icon-refresh">返回</el-button>	      
	    </div>

      <div class= "imgAll">
        <div v-for="picture in tableData"  v-bind:key="picture.uid" class = "imgBody" @click="checked(picture)">
              <img class= "img" :src="picture.pictureUrl"/>
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
    <el-upload class="upload-demo"  ref="upload" name="filedatas" :action="uploadPictureHost"
    :on-preview="handlePreview" :on-remove="handleRemove" :data="otherData"
          :multiple="true"
          :file-list="fileList"
          :on-success = "fileSuccess"
          :auto-upload="false">
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitNormalUpload">上传到服务器</el-button>
      </el-upload>
		</el-dialog>
  </div>
</template>

<script>
import {
  getPictureList,
  addPicture,
  editPicture,
  deletPicture
} from "@/api/picture";

import { formatData } from "@/utils/webUtils";

import { Loading } from "element-ui";

export default {
  created() {
    //传递过来的pictureSordUid
    this.pictureSortUid = this.$route.query.pictureSortUid;

    var that = this;    
    var params = new URLSearchParams();
    params.append("pictureSortUid", this.pictureSortUid);
    getPictureList(params).then(response => {
      console.log("初始化数据", response);
      if (response.code == "success") {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;
      } else {
        this.$message({
          type: "error",
          message: "系统错误"
        });
      }
    });

    //图片上传地址
    this.uploadPictureHost = process.env.PICTURE_API + "/file/pictures";

    //其它数据
    that.otherData = {
      userUid: "uid00000000000000000000000000000000",
      adminUid: "uid00000000000000000000000000000000",
      projectName: "blog",
      sortName: "admin"
    };
  },
  data() {
    return {
      tableData: [],
      uploadPictureHost: null,
      fileList: [],
      pictureSortUid: "",
      form: {
        uid: null,
        fileUid: null,
        picName: null,
        pictureSortUid: null
      },
      loading: true,
      currentPage: 1,
      total: null,
      pageSize: 10,
      title: "增加图片",
      dialogFormVisible: false,
      keyword: ""
    };
  },
  methods: {
    pictureList: function() {
      var params = new URLSearchParams();
      params.append("keyword", this.keyword);
      params.append("currentPage", this.currentPage);
      params.append("pageSize", this.pageSize);
      params.append("pictureSortUid", this.pictureSortUid);      
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
    //点击单选
    checked: function(data) {
      console.log("点击了选择");
    },
    checkAll: function() {
      console.log("点击了全选");
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
      console.log(`当前页: ${val}`);
      this.currentPage = val; //改变当前所指向的页数
      this.pictureList();
    },
    handleAdd: function() {
      this.dialogFormVisible = true;
      console.log("点击了新增");
    },
    handlePreview: function() {
      console.log("1");
    },
    handleRemove: function() {
      console.log("2");
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

<style scoped>
.img {
  width: 100%;
  height: 100%;
}
.imgBody {
  width: 150px;
  height: 150px;
  border: solid 1px #8080ff;
  float: left;
  margin: 30px;
  position: relative;
}

.removeFloat {
  clear: both;
}

.imgAll {
  width: 98%;
  overflow-y: auto;
}
.imgLimit {
  height: 50px;
  margin-left: 30%;
  margin-top: 50px;
}
</style>