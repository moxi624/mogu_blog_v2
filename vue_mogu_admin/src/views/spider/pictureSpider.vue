<template>
<div id="table" class="app-container calendar-list-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 10px 10px 0;">
      <el-input
        clearable
        class="filter-item"
        style="width: 220px;"
        v-model="keyword"
        placeholder="输入爬取图片的英文关键字"
        @keyup.enter.native="getPictureSpiderList"
      ></el-input>
      <el-button class="filter-item" type="primary" @click="getPictureSpiderList" icon="el-icon-edit" v-permission="'/picture/add'">开始爬取</el-button>
      <el-button class="filter-item" type="success" @click="handleUploadPicture" icon="el-icon-delete" v-permission="'/picture/delete'">上传图片管理</el-button>
      <el-button class= "button" type="primary"  @click="checkAll()" icon="el-icon-refresh">{{chooseTitle}}</el-button>
      <el-button class="filter-item" type="danger" @click="handleDeleteBatch" icon="el-icon-delete" v-permission="'/picture/delete'">删除选中</el-button>
    </div>

  <el-row>
    <el-col
      v-for="(url, index) in spiderPictureList"
      :key="index"
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
        <input style="position: absolute;z-index: 100;" type="checkbox" :id="url" :checked="pictureUrls.indexOf(url)>=0" @click="checked(url)">
        <el-image
          :src="url"
          style="cursor:pointer"
          fit="scale-down"
          @click="showPicture(url)"
        />
        <div @click="showPicture(url)">
          <span class="media-title">图片 {{index + 1}}</span>
        </div>
        <div style="margin-bottom: 14px;">
          <el-button-group>

            <el-tooltip class="item" effect="dark" content="上传至图片管理" placement="bottom-start" style="margin-right: 2px">
              <el-button
                type="success"
                size="mini"
                icon="el-icon-copy-document"
                @click="uploadPicture(url)"
              />
            </el-tooltip>

            <el-tooltip class="item" effect="dark" content="复制图片地址" placement="bottom-start" style="margin-right: 2px">
              <el-button
                size="mini"
                icon="el-icon-copy-document"
                @click="copyUrl(url)"
              />
            </el-tooltip>

            <el-tooltip class="item" effect="dark" content="复制Markdown格式图片地址" placement="bottom-start" style="margin-right: 2px">
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-document-copy"
                @click="copyMarkdownUrl(url)"
              >
              </el-button>
            </el-tooltip>

<!--            <el-tooltip class="item" effect="dark" content="裁剪图片" placement="bottom-start" style="margin-right: 2px" v-permission="'/picture/add'">-->
<!--              <el-button-->
<!--                type="warning"-->
<!--                size="mini"-->
<!--                icon="el-icon-s-open"-->
<!--                @click="handleCropper(url)"-->
<!--              />-->
<!--            </el-tooltip>-->

            <el-tooltip class="item" effect="dark" content="删除图片" placement="bottom-start" style="margin-right: 2px" v-permission="'/picture/delete'">
              <el-button
                type="danger"
                size="mini"
                icon="el-icon-delete"
                @click="handleDelete(url)"
              />
            </el-tooltip>

          </el-button-group>
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


  <el-dialog :visible.sync="dialogPictureVisible" fullscreen style="text-align: center">
    <img :src="dialogImageUrl" alt="">
  </el-dialog>

  <!-- 添加或修改对话框 -->
  <el-dialog title="上传至图片管理" :visible.sync="dialogFormVisible">
    <el-form :model="form" :rules="rules" ref="form">

      <el-form-item label="上传的图片分类" :label-width="formLabelWidth" prop="pictureSortUid">
        <el-select
          v-model="form.pictureSortUid"
          size="small"
          placeholder="请选择"
          style="width:150px"
        >
          <el-option
            v-for="item in pictureSortData"
            :key="item.uid"
            :label="item.name"
            :value="item.uid"
          ></el-option>
        </el-select>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
  </el-dialog>

  </div>
</template>

<script>

import PictureCropper from '@/components/PictureCropper'
import {pictureSpider} from "@/api/spider"
import { getPictureSortList } from "@/api/pictureSort";
import { uploadPicsByUrl, addPicture } from "@/api/picture";
import { getToken } from '@/utils/auth'

export default {
  components: {
    PictureCropper
  },
  data() {
    return {
      spiderPictureList: [],
      pictureUrls: [],
      chooseTitle: "全选",
      keyword: "cat",
      currentPage: 1,
      pageSize: 5,
      total: 0,
      dialogPictureVisible: false, // 是否显示图片
      dialogImageUrl: "", // 显示的图片URL
      isCheckedAll: false, // 是否全选
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: "120px",
      form: {}, // 提交的表单
      pictureSortData: [], //分类数据
      rules: {
        pictureSortUid: [
          {required: true, message: '图片分类不能为空', trigger: 'blur'},
        ]
      }
    }
  },
  watch: {

  },
  created() {
    this.getPictureSpiderList()
    this.getPictureSort()
    Array.prototype.remove = function (val) {
      var index = this.indexOf(val);
      if (index > -1) {
        this.splice(index, 1);
      }
    };
  },
  methods: {
    getPictureSpiderList: function () {
      if(this.keyword == "") {
        this.$commonUtil.message.error("请输入需要爬取的关键字")
        return
      }
      let params = {};
      params.currentPage = this.currentPage;
      params.keyword = this.keyword;
      pictureSpider(params).then(response => {
        if (response.code == this.$ECode.SUCCESS) {
          let spiderPictureList = response.data
          // TODO 暂时没有获取到返回的total
          if(spiderPictureList.length > 0) {
            this.total = 100
          } else {
            this.total = 0
          }
          this.spiderPictureList = spiderPictureList
        }
      });
    },
    getPictureSort: function () {
      let that = this
      let params = {};
      // TODO 设置一下较大的页码，把分类加载出来，如果分类很多的话，不能这么做
      params.pageSize = 500
      params.currentPage = 1;
      params.isShow = 1;
      getPictureSortList(params).then(function(response) {
        if (response.code == that.$ECode.SUCCESS) {
          console.log("获取图片分类列表", response)
          that.pictureSortData = response.data.records
        } else {
          this.$message({ type: "error", message: response.data });
        }
      })
    },
    copyUrl(url) {
      this.$commonUtil.copyText(url)
      this.$commonUtil.message.success('复制链接到剪切板成功')
    },
    copyMarkdownUrl(name, url) {
      const text = '![' + name + '](' + url + ')'
      this.$commonUtil.copyText(text)
      this.$commonUtil.message.success('复制Markdown格式链接到剪切板成功')
    },
    handleUploadPicture: function () {
      this.dialogFormVisible = true
      console.log("开始上传图片管理")
    },
    uploadPicture: function (url) {
      this.pictureUrls = [url]
      this.handleUploadPicture()
    },
    checkAll: function() {
      if(this.isCheckedAll) {
        this.pictureUrls = []
        this.chooseTitle = "全选"
        this.isCheckedAll = false
      } else {
        this.pictureUrls = this.spiderPictureList
        this.chooseTitle = "取消"
        this.isCheckedAll = true
      }
    },
    handleCurrentChange: function(val) {
      let that = this
      let params = {}
      params.currentPage = val
      params.keyword = this.keyword;
      pictureSpider(params).then(function(response) {
        if (response.code == that.$ECode.SUCCESS) {
          console.log("爬取的图片", response)
          that.spiderPictureList = response.data
          that.currentPage = val
        }
      });
    },
    handleDelete: function(url) {
      this.pictureUrls = [url]
      this.handleDeleteBatch()
    },
    handleDeleteBatch: function () {
      if (this.pictureUrls.length <= 0) {
        this.$commonUtil.message.error("请先选中图片！")
        return;
      }
      let pictureUrls = this.pictureUrls
      let spiderPictureList = this.spiderPictureList

      for(let a=0; a<pictureUrls.length; a++) {
        spiderPictureList.remove(pictureUrls[a])
      }
      this.spiderPictureList = spiderPictureList
    },
    checked: function (url) {
      let idIndex = this.pictureUrls.indexOf(url);
      if (idIndex >= 0) {
        //选过了
        this.pictureUrls.splice(idIndex, 1);
      } else {
        this.pictureUrls.push(url);
      }
    },
    showPicture: function(url) {
      this.dialogPictureVisible = true
      this.dialogImageUrl = url
    },
    submitForm: function() {
      let that = this
      this.$refs.form.validate((valid) => {
        if(!valid) {
          console.log('校验失败')
          return;
        } else {
          if(this.pictureUrls.length <= 0) {
            this.$commonUtil.message.error("请先选中图片！")
            return;
          }
          let params = {}
          params.source = "admin"
          params.userUid= "uid00000000000000000000000000000000"
          params.adminUid= "uid00000000000000000000000000000000"
          params.projectName= "blog"
          params.sortName= "admin"
          params.token = getToken()
          // 要上传的图片URL
          console.log("要上传的url", that.pictureUrls)
          // params.urlList = JSON.stringify(that.pictureUrls);
          params.urlList = that.pictureUrls;
          uploadPicsByUrl(params).then(function(response) {
            if (response.code == that.$ECode.SUCCESS) {
              console.log("上传图片", response)

              // 然后再次将上传后返回的图片，插入到图片管理中
              let data = response.data
              let pictureUploadList = []
              for (let index = 0; index < data.length; index++) {
                let picture = {};
                picture.fileUid = data[index].uid;
                picture.pictureSortUid = that.form.pictureSortUid
                picture.picName = data[index].picName
                pictureUploadList.push(picture)
              }
              addPicture(pictureUploadList).then(res => {
                if (res.code == that.$ECode.SUCCESS) {
                  that.$commonUtil.message.success(res.message)
                  // 删除上传完的图片
                  let pictureUrls = that.pictureUrls
                  let spiderPictureList = that.spiderPictureList
                  for(let a=0; a<pictureUrls.length; a++) {
                    spiderPictureList.remove(pictureUrls[a])
                  }
                  that.dialogFormVisible = false
                  that.spiderPictureList = spiderPictureList
                } else {
                  that.$commonUtil.message.error(res.message)
                }
              });
            }
          });
        }
      })
    },
  }
};
</script>

<style scoped>
  .media-title {
    color: #8492a6;
    font-size: 14px;
    padding: 3px;
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
