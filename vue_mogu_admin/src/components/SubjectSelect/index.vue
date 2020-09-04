<template>
  <div id="body-loading" class="app-container calendar-list-container">
    <el-dialog title="请选择需要添加的专题" :visible.sync="dialogVisible" fullscreen :before-close="before_close">

    <el-row>
      <el-col
        v-for="(item, index) in tableData"
        :key="item.uid"
        style="padding: 6px"
        :xs="24"
        :sm="12"
        :md="12"
        :lg="6"
        :xl="4"
      >

        <el-card
          :body-style="{ padding: '0px', textAlign: 'center' }"
          shadow="always"
        >
          <input style="position: absolute;z-index: 100;" type="checkbox" :id="item.uid" :checked="selectUids.indexOf(item.uid)>=0" @click="checked(item)">
          <el-image
            @click="checked(item)"
            :src="item.photoList[0]"
            style="cursor:pointer;"
            fit="scale-down"
          />
          <div>
            <span class="media-title" v-if="item.subjectName">{{item.subjectName}}</span>
          </div>
        </el-card>

      </el-col>
    </el-row>

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

      <span slot="footer" class="dialog-footer">
        <el-button @click="before_close">取 消</el-button>
        <el-button type="primary" @click="commit">确 定</el-button>
      </span>

    </el-dialog>
  </div>
</template>

<script>
import {
  getSubjectList,
} from "@/api/subject";

export default {
  props: ["subjectVisible"],
  data() {
    return {
      dialogImageUrl: "", //图片显示地址
      dialogVisible: this.subjectVisible,
      tableData: [],
      uploadPictureHost: null,
      fileList: [],
      pictureSortUid: "",
      selectUids: [], //图片uid集合
      pictureUploadList: [], //图片上传列表
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
      pageSize: 18,
      total: null,
      title: "增加图片",
      dialogFormVisible: false,
      keyword: "",
      reFresh: true, //是否刷新组件
    };
  },
  watch: {
    subjectVisible: function() {
      this.dialogVisible = this.subjectVisible;
    },
  },
  created() {
    // 获取图片列表
    this.subjectList()
  },
  methods: {
    subjectList: function() {
      var params = {};
      params.keyword = this.keyword
      params.pageSize = this.pageSize
      params.currentPage = this.currentPage
      getSubjectList(params).then(response => {
        console.log("得到的专题列表", response)
        if (response.code == this.$ECode.SUCCESS) {
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
    before_close(done) {
      //取消时，开始状态
      this.$emit("cancelModel", "");
      done();
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
      // 多选
      // let idIndex = this.selectUids.indexOf(data.uid);
      // if (idIndex >= 0) {
      //   //选过了
      //   this.selectUids.splice(idIndex, 1);
      // } else {
      //   this.selectUids.push(data.uid);
      // }

      // 单选
      this.selectUids = []
      this.selectUids.push(data.uid);
    },
    //改变页码
    handleCurrentChange(val) {
      this.currentPage = val; //改变当前所指向的页数
      this.subjectList();
    },
    commit: function () {
      if (this.selectUids.length == 0) {
        this.$message({
          type: "error",
          message: "请先选中需要加入的专题!"
        });
        return;
      }
      this.$emit("selectData", this.selectUids)
    }
  }
};
</script>

<style scoped>
  .media-title {
    color: #8492a6;
    font-size: 14px;
    padding: 14px;
    display: inline-block;
    white-space: nowrap;
    width: 60%;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .el-image {
    width: 100%;
    height: 160px;
  }
</style>
