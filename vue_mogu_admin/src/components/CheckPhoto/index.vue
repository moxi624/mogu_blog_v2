<template>
  <div id="body-loading" class="app-container calendar-list-container">
    <el-dialog title="请选择图片" :visible.sync="dialogVisible" fullscreen :before-close="before_close">
      <div class="filter-container" style="margin: 10px 0 10px 0;">
        <el-input
          clearable
          class="filter-item"
          style="width: 200px;"
          v-model="keyword"
          placeholder="请输入分类名称"
        ></el-input>
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">查找</el-button>
        <el-button class="filter-item" type="primary" @click="handleRest" icon="el-icon-refresh">重置</el-button>
      </div>

      <el-tabs
        v-model="activeName"
        type="border-card"
        tab-position="left"
        style="height: 600px; width: 100%;"
        @tab-click="clickTab"
      >
        <el-tab-pane
          style="height: 570px; width: 100%; overflow:auto;"
          v-for="(pictureSort, index) in  pictureSorts"
          :key="index"
        >
          <div class="sortItem" slot="label" style="float:left">
            <i class="el-icon-picture"></i>
            {{submitText(pictureSort.name)}}
          </div>
          <div style="clear:both;"></div>
          <div>
            <img
              v-if="pictureSort.pictures"
              v-for="picture in pictureSort.pictures"
              :key="picture.fileUid"
              class="showPicture"
              @click="checkLogoConfirm(picture.fileUid,picture.pictureUrl)"
              :src="BASE_IMAGE_URL + picture.pictureUrl"
            >
          </div>
          <div class="addPicture" v-if="pictureSort.total - (pictureSort.pageSize*pictureSort.currentPage) < 0" @click="toPictureManager(pictureSort.pictureSortUid)">
            <span>+</span>
          </div>

          <el-pagination
            class="pagination"
            @current-change="handleCurrentChange"
            :current-page.sync="pictureSort.currentPage"
            :page-size="pictureSort.pageSize"
            small
            layout="prev, pager, next"
            :total="pictureSort.total"
          ></el-pagination>

        </el-tab-pane>
      </el-tabs>
      <span slot="footer" class="dialog-footer">
        <div class="ChooseBody" :key="index" v-for="(picture, index) in form.photoList">
          <i @click="deletePhoto(index)" class="el-icon-error inputClass" v-show="icon"></i>
          <img style="width: 100%;height: 100%;" :src="BASE_IMAGE_URL + picture">
        </div>
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="commit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getPictureSortList, getPictureSortByUid } from "@/api/pictureSort";
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
      var params = new URLSearchParams();
      // TODO 全部把分类加载出来，如果图片很多的话，不能这么做
      params.append("pageSize", "500");
      getPictureSortList(params).then(function(response) {
        if (response.code == "success") {
          var pictureSorts = response.data.records;
          that.pictureSorts = pictureSorts;
          loadingInstance.close();
          //默认初始化第一个
          if (pictureSorts.length > 0) {
            var pictureSortUid = pictureSorts[0].uid;
            that.currentPictureSortUid = pictureSorts[0].uid;
            var name = pictureSorts[0].name;
            var params = new URLSearchParams();
            params.append("pictureSortUid", pictureSortUid);
            params.append("pageSize", 20); //每一页显示40个
            getPictureList(params).then(function(response) {
              console.log("得到的分类中的图片", response);
              if (response.code == "success") {
                var newObject = {
                  pictureSortUid: pictureSortUid,
                  name: name,
                  pictures: response.data.records,
                  pageSize: response.data.size,
                  currentPage: response.data.current,
                  total: response.data.total
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
      BASE_IMAGE_URL: process.env.BASE_IMAGE_URL,
      dialogVisible: this.photoVisible,
      sortList: [],
      havePictureSorts: false, //是否加载完图片分类
      pictureSorts: [], //分类列表
      icon: true,
      activeName: "0",
      limitCount: this.limit,
      newPictureSort: [],
      keyword: "",
      currentPictureSortUid: null, //当前图片分类uid
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
    handleCurrentChange: function(val) {
      console.log("当前页", val);
      console.log("uid", this.currentPictureSortUid);
      var that = this;
      var pictureSortUid = this.currentPictureSortUid;
      var pictureSortParams = new URLSearchParams();
      pictureSortParams.append("uid", pictureSortUid);
      getPictureSortByUid(pictureSortParams).then(function(sortResponse) {
        console.log("返回的分类", sortResponse);
        if (sortResponse.code == "success") {
          var pictureSort = sortResponse.data;
          var params = new URLSearchParams();
          params.append("pictureSortUid", pictureSortUid);
          params.append("currentPage", val); //每一页显示40个
          params.append("pageSize", 20); //每一页显示40个
          getPictureList(params).then(function(response) {
            if (response.code == "success") {
              var newObject = {
                pictureSortUid: pictureSortUid,
                name: pictureSort.name,
                pictures: response.data.records,
                pageSize: response.data.size,
                currentPage: response.data.current,
                total: response.data.total
              };
              Vue.set(that.pictureSorts, that.activeName, newObject);
            } else {
              this.$message({ type: "error", message: response.data });
            }
          });
        }
      });
    },
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
        console.log("加载数据图片：", response);
        if (response.code == "success") {
          //成功
          var pictureSorts = response.data.records;
          that.pictureSorts = pictureSorts;
          loadingInstance.close();
          //默认初始化第一个
          if (pictureSorts.length > 0) {
            var pictureSortUid = pictureSorts[0].uid;
            that.currentPictureSortUid = pictureSortUid; //当前pictureSortUid
            var name = pictureSorts[0].name;
            var params = new URLSearchParams();
            params.append("pictureSortUid", pictureSortUid);
            params.append("pageSize", 20); //每一页显示40个
            getPictureList(params).then(function(response) {
              if (response.code == "success") {
                var newObject = {
                  pictureSortUid: pictureSortUid,
                  name: name,
                  pictures: response.data.records,
                  pageSize: response.data.size,
                  currentPage: response.data.current,
                  total: response.data.total
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
          var pictureSorts = response.data.records; //这里不应该加进去
          console.log("查询到的列表", response);
          that.pictureSorts = pictureSorts;
          if (pictureSorts.length <= 0) {
            that.$message({
              type: "error",
              message: "没有搜索到任何信息！"
            });
          }

          var pictureSortUid = pictureSorts[0].uid;
          var name = pictureSorts[0].name;
          var params = new URLSearchParams();
          console.log(pictureSortUid);
          params.append("pictureSortUid", pictureSortUid);
          params.append("pageSize", 20); //每一页显示40个
          getPictureList(params).then(function(response) {
            if (response.code == "success") {
              console.log("获取列表的图片:", response);
              var newObject = {
                pictureSortUid: pictureSortUid,
                name: name,
                pictures: response.data.records,
                pageSize: response.data.size,
                currentPage: response.data.current,
                total: response.data.total
              };
              Vue.set(that.pictureSorts, 0, newObject);
            } else {
              this.$message({ type: "error", message: response.data });
            }
          });
        } else {
          this.$message({ type: "error", message: response.data });
        }
      });
      console.log("点击了查找：", this.keyword);
    },
    clickTab(e) {
      var that = this;
      var index = this.activeName;
      var pictureSortUid = this.pictureSorts[index].uid == undefined ? this.pictureSorts[index].pictureSortUid : this.pictureSorts[index].uid;
      console.log("选中的分类UID", pictureSortUid);
      this.currentPictureSortUid = pictureSortUid; //当前pictureSortUid
      var name = this.pictureSorts[index].name;
      var params = new URLSearchParams();
      params.append("pictureSortUid", pictureSortUid);
      params.append("pageSize", 20);
      getPictureList(params).then(function(response) {
        if (response.code == "success") {
          if (response.data.records.length > 0) {
            var newObject = {
              pictureSortUid: pictureSortUid,
              name: name,
              pictures: response.data.records,
              pageSize: response.data.size,
              currentPage: response.data.current,
              total: response.data.total
            };
            console.log(that.pictureSorts);
            Vue.set(that.pictureSorts, index, newObject);
          }
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
      //取消时，欢迎成开始状态
      this.form.photoList = this.photos;
      this.form.fileIds = this.files;

      this.$emit("cancelModel", "");
      done();
    },
    cancel() {
      //取消时，还原成开始状态
      this.form.photoList = this.photos;
      this.form.fileIds = this.files;

      this.$emit("cancelModel", "");
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    commit(photoList, fileIds) {
      let data = {
        photoList: this.form.photoList,
        fileIds: this.form.fileIds
      };
      this.$emit("choose_data", data);
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
      console.log("fileIds的内容", this.form.fileIds);
      if (this.form.fileIds != null) {
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
            path: "/picture/picture",
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

.pagination {
  position: absolute;
  bottom: 5%;
  left: 38%;
}
</style>