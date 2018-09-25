<template>
<div id = "body-loading" class="app-container calendar-list-container">
	<el-dialog
  title="请选择图片"	
  :visible.sync="dialogVisible" fullscreen :before-close="before_close">

		<div class="filter-container" style="margin: 10px 0 10px 0;">
			<el-input clearable class="filter-item" style="width: 200px;" v-model="keyword" placeholder="请输入分类名称"></el-input>
			<el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">查找</el-button>
			<el-button class="filter-item" type="primary" @click="handleRest" icon="el-icon-refresh">重置</el-button>   
	  </div>

		<el-tabs v-model="activeName" type="border-card" tab-position="left" style="height: 600px; width: 100%;"  @tab-click="clickTab">			
		  <el-tab-pane style="height: 570px; width: 100%; overflow:auto;" v-for="(pictureSort, index) in  pictureSorts" :key = "index">
		    <div class="sortItem" slot="label" style="float:left">
					<i class="el-icon-picture"></i> {{submitText(pictureSort.name)}}</div>
					<div style="clear:both;"></div>
        <div>
          <img v-if="pictureSort.pictures" v-for = "picture in pictureSort.pictures" :key = "picture.fileUid" class = "showPicture" @click="checkLogoConfirm(picture.fileUid,picture.pictureUrl)" :src = "picture.pictureUrl"/>
        </div>
        <div class="addPicture" @click="toPictureManager(pictureSort.pictureSortUid)">
        	<span>+</span>
        </div>
		  </el-tab-pane>		  
		</el-tabs>
			<span slot="footer" class="dialog-footer">
				<div class="ChooseBody" :key="index" v-for = "(picture, index) in form.photoList">
					<i  @click="deletePhoto(index)" class="el-icon-error inputClass" v-show="icon"></i>
					<img style="width: 100%;height: 100%;" :src = "picture"/>  		 
				</div>
				<el-button @click="cancel">取 消</el-button>
				<el-button type="primary" @click="commit">确 定</el-button>
			</span>
	</el-dialog>
</div>
</template>

<script>
import { getPictureSortList } from "@/api/pictureSort";
import { getPictureList } from "@/api/picture";
import Vue from "vue";
import { Loading } from "element-ui";

export default {
  props: ["photoVisible", "photos", "files", "limit"],
  created() {
    this.dialogVisible = this.photoVisible;
    this.form.photoList = this.photos;
    this.form.fileIds = this.files;

    let loadingInstance = Loading.service({
      target: "#body-loading",
      text: "加载中~"
    });

    //加载数据
    var that = this;
    let index = 0;
    //先加载分类
    if (!that.havePictureSorts) {
      console.log("开始加载数据");
      var params = new URLSearchParams();
      getPictureSortList(params).then(function(response) {
        console.log("加载数据", response);
        if (response.code == "success") {
          //成功
          var pictureSorts = response.data.records; 
          that.pictureSorts = pictureSorts;
          loadingInstance.close();
          //默认初始化第一个
          if (pictureSorts.length > 0) {
            var pictureSortUid = pictureSorts[0].uid;
            var name = pictureSorts[0].name;
            var params = new URLSearchParams();
            params.append("pictureSortUid", pictureSortUid);
            getPictureList(params).then(function(response) {
              console.log("获取列表的图片:", response);
              if (response.code == "success") {
                var newObject = {
                  pictureSortUid: pictureSortUid,
                  name: name,
                  pictures: response.data.records
                };
                Vue.set(that.pictureSorts, 0, newObject);
              } else {
                this.$message({ type: "error", message: response.data });
              }
            });
          }
        } else {
          this.$message({ type: "error", message: response.data });
        }
      });
    }
  },

  data() {
    return {
      dialogVisible: this.photoVisible,
      sortList: [],
      havePictureSorts: false, //是否加载完图片分类
      pictureSorts: [], //分类列表
      icon: true,
      activeName: "0",
      limitCount: this.limit,
      newPictureSort: [],
      keyword: "",
      form: {
        photoList: [],
        fileIds: []
      }
    };
  },
  watch: {
    photoVisible: function() {
      this.dialogVisible = this.photoVisible;
    },
    photos: function() {
      this.form.photoList = this.photos;
    },
    files: function() {
      this.form.fileIds = this.files;
    },
    limit: function() {
      if (this.limit) {
        this.limitCount = this.limit;
      }
    }
  },
  methods: {
    handleRest: function() {
      var loadingInstance = Loading.service({
        target: "#body-loading",
        text: "加载中~"
      });
      this.activeName = "0";
      var that = this;
      var params = new URLSearchParams();
      params.append("pageSize", "500");
      getPictureSortList(params).then(function(response) {
        console.log("加载数据", response);
        if (response.code == "success") {
          //成功
          var pictureSorts = response.data.records; 
          that.pictureSorts = pictureSorts;
          loadingInstance.close();
          //默认初始化第一个
          if (pictureSorts.length > 0) {
            var pictureSortUid = pictureSorts[0].uid;
            var name = pictureSorts[0].name;
            var params = new URLSearchParams();
            params.append("pictureSortUid", pictureSortUid);
            getPictureList(params).then(function(response) {
              console.log("获取列表的图片:", response);
              if (response.code == "success") {
                var newObject = {
                  pictureSortUid: pictureSortUid,
                  name: name,
                  pictures: response.data.records
                };
                Vue.set(that.pictureSorts, 0, newObject);
              } else {
                this.$message({ type: "error", message: response.data });
              }
            });
          }
        } else {
          this.$message({ type: "error", message: response.data });
        }
      });
    },
    handleFind: function() {
      var that = this;
      if (this.keyword == "") {
        that.$message({
          type: "error",
          message: "分类名称不能为空!"
        });
        return;
      }
      var params = new URLSearchParams();
      params.append("pageSize", "500");
      params.append("keyword", this.keyword);
      getPictureSortList(params).then(function(response) {
        if (response.code == "success") {
          //成功
          var pictureSorts = response.data.resultList; //这里不应该加进去
          console.log(pictureSorts);
          that.pictureSorts = pictureSorts;
          if (pictureSorts.length <= 0) {
            that.$message({
              type: "error",
              message: "没有搜索到任何信息！"
            });
          }
          for (let index = 0; index < pictureSorts.length; index++) {
            var pictureSortuid = pictureSorts[index].pictureSorUid;
            var name = pictureSorts[index].name;
            var params = new URLSearchParams();
            params.append("pictureSortUid", pictureSortUid);
            getPictureList(params).then(function(response) {
              if (response.code == "success") {
                Vue.set(that.pictureSorts[index], "pictures", response.data.records);
                // console.log(that.pictureSorts);
              } else {
                this.$message({ type: "error", message: response.data });
              }
            });
          }
        } else {
          this.$message({ type: "error", message: response.data });
        }
      });
      console.log("点击了查找：", this.keyword);
    },
    clickTab(e) {
      var that = this;
      var index = this.activeName;
      var pictureSortUid = this.pictureSorts[index].uid;
      console.log("选中的分类UID", this.pictureSorts[index]);
      var name = this.pictureSorts[index].name;
      var params = new URLSearchParams();
      params.append("pictureSortUid", pictureSortUid);
      getPictureList(params).then(function(response) {
        if (response.code == "success") {
          var newObject = {
            pictureSortUid: pictureSortUid,
            name: name,
            pictures: response.data.records
          };
          Vue.set(that.pictureSorts, index, newObject);
        } else {
          this.$message({ type: "error", message: response.data });
        }
      });
    },
    submitText: function(str) {
      var result = "";
      if (str.length > 6) {
        result = str.substring(0, 6) + "...";
      } else {
        result = str;
      }
      return result;
    },
    before_close(done) {
      console.log("关闭前的回调");
      this.$emit("cancelModel", "");
      done();
    },
    cancel() {
      this.$emit("cancelModel", "");
      console.log("点击了取消");
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    commit(photoList, fileIds) {
      let data = {
        photoList: this.form.photoList,
        fileIds: this.form.fileIds
      };
      console.log("点击提交", data);
      this.$emit("choose_data", data);
      //    	this.dialogVisible = false;
    },
    //点击选中图片
    checkLogoConfirm: function(fileId, fileUrl) {
      console.log(this.limitCount, "图片限制数");
      if (this.limitCount != undefined) {
        if (this.form.photoList.length >= this.limitCount) {
          this.$message({
            message: "最多只能选择" + this.limitCount + "张图片！",
            type: "error"
          });
          return;
        }
      }
      console.log( "fileIds的内容", this.form.fileIds);
      if(this.form.fileIds != null) {
        if (this.form.fileIds.indexOf(fileId) != -1) {
          this.$message({
            message: "该图片已存在列表中！",
            type: "warning"
          });
          return;
        }
      }
      
      this.form.photoList.push(fileUrl);
      this.$forceUpdate();
      this.form.fileIds = this.form.fileIds + "," + fileId;
      console.log("点击了添加图片", this.form.photoList);
      this.$message({
        message: "添加成功",
        type: "success"
      });
    },
    deletePhoto(index) {
      var ids = this.form.fileIds;
      ids = ids
        .split(",")
        .join(" ")
        .trim();
      var array = ids.split(" ");
      this.form.photoList.splice(index, 1);
      console.log(this.form.photoList, "图片list");
      console.log(array);
      var newStr = "";
      var tag = -1;
      this.$message({
        message: "删除成功",
        type: "success"
      });
      for (var a = 0; a < array.length - 1; a++) {
        tag++;
        if (array[a] == null || array[a] == "" || tag == index) continue;
        if (a == 0) {
          newStr = array[a];
        } else {
          newStr = newStr + "," + array[a];
        }
      }
      this.form.fileIds = newStr;
      console.log(this.form.fileIds, "修改后");
    },
    toPictureManager: function(pictureSortUid) {
      this.$confirm("是否跳转到图片管理进行上传？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          /**
           * 跳转到图片上传
           */
          this.$router.push({
            path: "/example/picture",
            query: { pictureSortUid: pictureSortUid }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消跳转"
          });
        });
      console.log("选择图片", pictureSortUid);
    }
  }
};
</script>
<style>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 150px;
  min-height: 400px;
}
.sortItem {
  height: 38px;
  font-size: 16px;
}
.showPicture {
  width: 150px;
  height: 150px;
  float: left;
  margin-left: 10px;
  margin-top: 10px;
  border: solid 1px #c7aeae;
}
.ChooseBody {
  width: 150px;
  height: 150px;
  float: left;
  margin-left: 10px;
  border: solid 1px #c7aeae;
}
.inputClass {
  position: absolute;
}
.addPicture {
  width: 150px;
  height: 150px;
  float: left;
  margin-left: 10px;
  margin-top: 10px;
  border: solid 1px #c7aeae;
  line-height: 150px;
  text-align: center;
  cursor: pointer;
}

.addPicture span {
  font-size: 30px;
  color: #97a8be;
  height: 60px;
  margin: 0 auto;
}
</style>