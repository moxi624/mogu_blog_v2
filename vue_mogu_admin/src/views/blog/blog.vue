<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;" v-permission="'/blog/getList'">
      <el-input
        clearable
        class="filter-item"
        style="width: 150px;"
        v-model="keyword"
        placeholder="请输入博客名"
        @keyup.enter.native="handleFind"
      ></el-input>

      <el-select
        v-model="sortKeyword"
        style="width: 140px"
        filterable
        clearable
        remote
        reserve-keyword
        placeholder="请输入分类名"
        :remote-method="sortRemoteMethod"
        @keyup.enter.native="handleFind"
        :loading="loading"
      >
        <el-option
          v-for="item in sortOptions"
          :key="item.uid"
          :label="item.sortName"
          :value="item.uid"
        ></el-option>
      </el-select>

      <el-select
        v-model="tagKeyword"
        filterable
        clearable
        remote
        reserve-keyword
        placeholder="请输入标签名"
        :remote-method="tagRemoteMethod"
        :loading="loading"
        @keyup.enter.native="handleFind"
        style="width:140px"
      >
        <el-option
          v-for="item in tagOptions"
          :key="item.uid"
          :label="item.content"
          :value="item.uid"
        ></el-option>
      </el-select>

      <el-select v-model="levelKeyword" clearable placeholder="推荐等级" style="width:110px">
        <el-option
          v-for="item in blogLevelDictList"
          :key="item.uid"
          :label="item.dictLabel"
          :value="item.dictValue"
        ></el-option>
      </el-select>

      <el-select v-model="publishKeyword" clearable placeholder="是否发布" style="width:110px">
        <el-option
          v-for="item in blogPublishDictList"
          :key="item.uid"
          :label="item.dictLabel"
          :value="item.dictValue"
        ></el-option>
      </el-select>

      <el-select v-model="originalKeyword" clearable placeholder="是否原创" style="width:110px">
        <el-option
          v-for="item in blogOriginalDictList"
          :key="item.uid"
          :label="item.dictLabel"
          :value="item.dictValue"
        ></el-option>
      </el-select>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/blog/getList'">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/blog/add'">添加博客</el-button>
      <el-button class="filter-item" type="warning" @click="handleUpload" icon="el-icon-star-on" v-permission="'/blog/uploadLocalBlog'">本地上传</el-button>
      <el-button class="filter-item" type="warning" @click="handleDownload" icon="el-icon-s-flag"  v-permission="'/blog/downloadBatch'">导出选中</el-button>
      <el-button class="filter-item" type="danger" @click="handleDeleteBatch" icon="el-icon-delete" v-permission="'/blog/deleteBatch'">删除选中</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="标题图" width="160" align="center">
        <template slot-scope="scope">
          <img
            v-if="scope.row.photoList"
            :src="BASE_IMAGE_URL + scope.row.photoList[0]"
            style="width: 130px;height: 70px;"
          >
        </template>
      </el-table-column>

      <el-table-column label="标题" width="160" align="center">
        <template slot-scope="scope">
          <span @click="onClick(scope.row)" style="cursor:pointer;">{{ scope.row.title }}</span>
        </template>
      </el-table-column>

      <el-table-column label="作者" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>

      <el-table-column label="是否原创" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isOriginal==1" type="success">原创</el-tag>
          <el-tag v-if="scope.row.isOriginal==0" type="info">转载</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="分类" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.blogSort.sortName }}</span>
        </template>
      </el-table-column>

      <!-- <el-table-column label="简介" width="250">
	      <template slot-scope="scope">
	        <span>{{ submitStr(scope.row.summary, 30) }}</span>
	      </template>
      </el-table-column>-->

      <el-table-column label="标签" width="200" align="center">
        <template slot-scope="scope">
          <template>
            <el-tag
              style="margin-left: 3px"
              type="warning"
              v-if="item"
              :key="index"
              v-for="(item, index) in scope.row.tagList"
            >{{item.content}}</el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="推荐等级" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item in blogLevelDictList" :key="item.uid" v-if="scope.row.level == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="点击数" width="70" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.clickCount }}</span>
        </template>
      </el-table-column>

      <el-table-column label="开启评论" width="100" align="center">
        <template slot-scope="scope">
          <template>
            <el-tag v-for="item in openDictList" :key="item.uid" :type="item.listClass" v-if="scope.row.startComment == item.dictValue">{{item.dictLabel}}</el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="发布状态" width="100" align="center">
        <template slot-scope="scope">
          <template>
            <el-tag v-for="item in blogPublishDictList" :key="item.uid" :type="item.listClass" v-if="scope.row.isPublish == item.dictValue">{{item.dictLabel}}</el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" min-width="150">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/blog/edit'">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/blog/delete'">删除</el-button>
        </template>
      </el-table-column>

    </el-table>

    <!--分页-->
    <div class="block">
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page.sync="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next, jumper"
        :total="total"
      ></el-pagination>
    </div>

    <!-- 添加或修改对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      :before-close="closeDialog"
      fullscreen
    >
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="标题图" :label-width="formLabelWidth">
          <div class="imgBody" v-if="form.photoList">
            <i
              class="el-icon-error inputClass"
              v-show="icon"
              @click="deletePhoto()"
              @mouseover="icon = true"
            ></i>
            <img
              @mouseover="icon = true"
              @mouseout="icon = false"
              v-bind:src="BASE_IMAGE_URL + form.photoList[0]"
              style="display:inline; width: 195px;height: 105px;"
            >
          </div>
          <div v-else class="uploadImgBody" @click="checkPhoto">
            <i class="el-icon-plus avatar-uploader-icon"></i>
          </div>
        </el-form-item>

        <el-form-item label="标题" :label-width="formLabelWidth" prop="title">
          <el-input v-model="form.title" auto-complete="off" @input="contentChange"></el-input>
        </el-form-item>

        <el-form-item label="简介" :label-width="formLabelWidth">
          <el-input v-model="form.summary" auto-complete="off" @input="contentChange"></el-input>
        </el-form-item>

        <el-row>
          <el-col :span="4.5">
            <el-form-item label="分类" :label-width="formLabelWidth" prop="blogSortUid">
              <el-select
                v-model="form.blogSortUid"
                size="small"
                placeholder="请选择"
                style="width:150px"
              >
                <el-option
                  v-for="item in blogSortData"
                  :key="item.uid"
                  :label="item.sortName"
                  :value="item.uid"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="4.5">
            <el-form-item label="标签" label-width="80px">
              <el-select
                v-model="tagValue"
                multiple
                size="small"
                placeholder="请选择"
                style="width:210px"
                filterable
              >
                <el-option
                  v-for="item in tagData"
                  :key="item.uid"
                  :label="item.content"
                  :value="item.uid"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="4.5">
            <el-form-item label="推荐等级" :label-width="maxLineLabelWidth" prop="level">
              <el-select v-model="form.level" size="small" placeholder="请选择" style="width:100px">
                <el-option
                  v-for="item in blogLevelDictList"
                  :key="item.uid"
                  :label="item.dictLabel"
                  :value="parseInt(item.dictValue)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="4.5">
            <el-form-item label="是否发布" :label-width="lineLabelWidth" prop="isPublish">
              <el-radio-group v-model="form.isPublish" size="small">
                <el-radio v-for="item in blogPublishDictList" :key="item.uid" :label="item.dictValue" border>{{item.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-form-item label="是否原创" :label-width="formLabelWidth" prop="isOriginal">
              <el-radio-group v-model="form.isOriginal" size="small">
                <el-radio v-for="item in blogOriginalDictList" :key="item.uid" :label="item.dictValue" border>{{item.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="4.5">
            <el-form-item label="网站评论" :label-width="formLabelWidth" prop="startComment">
              <el-radio v-for="item in openDictList" :key="item.uid" v-model="form.startComment" :label="item.dictValue" border size="small">{{item.dictLabel}}</el-radio>
            </el-form-item>
          </el-col>

        </el-row>

        <el-form-item label="作者" :label-width="formLabelWidth" v-if="form.isOriginal==0" prop="author">
          <el-input v-model="form.author" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="文章出处" :label-width="formLabelWidth" v-if="form.isOriginal==0">
          <el-input v-model="form.articlesPart" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="内容" :label-width="formLabelWidth" prop="content">
          <CKEditor ref="ckeditor" :content="form.content" @contentChange="contentChange" :height="320"></CKEditor>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="本地博客上传"
      :visible.sync="localUploadVisible"
    >
      <div class="tipBox">
        <div class="tip">导入须知</div>
        <div class="tipItem">1）如果你的Markdown文档里面的图片是本地，需要选择本地图片，然后提交到图片服务器</div>
        <div class="tipItem">2）含有本地图片一定需要提前上传图片，否者会出现图片无法替换的问题</div>
        <div class="tipItem">3）如果你的Markdown文档里面的图片不是本地，直接选择博客文件上传即可</div>
        <div class="tipItem">4）目前支持Markdown文件批量上传，步骤是先提交所有图片，在提交全部的博客文件</div>
        <div class="tipItem">5）因为网络或者服务器性能等不可抗拒的原因，因此不推荐一次上传太多</div>
      </div>

      <el-upload
        class="upload-demo2"
        ref="uploadPicture"
        name="filedatas"
        :data="otherData"
        :action="uploadPictureHost"
        :auto-upload="false"
        multiple
      >
        <el-button slot="trigger" size="small" type="primary">选取本地图片</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitPictureUpload">提交到图片服务器</el-button>
      </el-upload>


      <el-upload
      class="upload-demo"
      ref="uploadFile"
      name="filedatas"
      :headers="importHeaders"
      :action="uploadAdminHost"
      :auto-upload="false"
      multiple
    >
      <el-button slot="trigger" size="small" type="primary">选取博客文件</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">提交到服务器</el-button>
    </el-upload>

<!--      <el-upload-->
<!--        class="upload-demo2"-->
<!--        drag-->
<!--        ref="upload2"-->
<!--        name="filedatas"-->
<!--        :action="uploadPictureHost"-->
<!--        :data="otherData"-->
<!--        :on-success = "fileSuccess"-->
<!--        multiple>-->
<!--        <i class="el-icon-upload"></i>-->
<!--        <div class="el-upload__text">将图片拖到此处，或<em>点击上传</em></div>-->
<!--        <div slot="tip" class="el-upload__tip">将博客内的本地图片选中</div>-->
<!--      </el-upload>-->

    </el-dialog>

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
import { getBlogList, addBlog, uploadLocalBlog, editBlog, deleteBlog, deleteBatchBlog } from "@/api/blog";
import { getTagList } from "@/api/tag";
import { getBlogSortList } from "@/api/blogSort";
import {
  formatData,
  formatDataToJson,
  formatDataToForm
} from "@/utils/webUtils";
import { getToken } from '@/utils/auth'
import { setCookie, getCookie, delCookie } from "@/utils/cookieUtils";
import {getListByDictTypeList} from "@/api/sysDictData"
import CheckPhoto from "../../components/CheckPhoto";
import CKEditor from "../../components/CKEditor";
var querystring = require("querystring");
import { mapGetters } from "vuex";
import data2blob from "../../components/AvatarCropper/utils/data2blob";
import { Loading } from 'element-ui';
export default {
  computed: {
    ...mapGetters(["name", "roles"])
  },
  components: {
    CheckPhoto,
    CKEditor
  },
  data() {
    return {
      uploadPictureHost: process.env.PICTURE_API + "/file/pictures",
      uploadAdminHost: process.env.ADMIN_API + "/blog/uploadLocalBlog",
      importHeaders: {
        Authorization: getToken()
      },
      otherData: {
        source: "picture",
        userUid: "uid00000000000000000000000000000000",
        adminUid: "uid00000000000000000000000000000000",
        projectName: "blog",
        sortName: "admin",
        token: getToken()
      },
      pictureList: [], // 上传的图片列表
      BASE_IMAGE_URL: process.env.BASE_IMAGE_URL,
      BLOG_WEB_URL: process.env.BLOG_WEB_URL,
      multipleSelection: [], //多选，用于批量删除
      tagOptions: [], //标签候选框
      sortOptions: [], //分类候选框
      loading: false, //搜索框加载状态
      uploadLoading: null, //文件上传loading
      CKEditorData: null,
      tableData: [], //博客数据
      tagData: [], //标签数据
      tagValue: [], //保存选中标签id(编辑时)
      blogSortData: [],
      keyword: "",
      tagKeyword: "", //标签搜索
      sortKeyword: "", //分类搜索
      levelKeyword: "", //等级搜索
      publishKeyword: "", // 发布 搜索
      originalKeyword: "", // 原创 搜索
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加博客",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: "120px",
      lineLabelWidth: "120px", //一行的间隔数
      maxLineLabelWidth: "100px",
      isEditForm: false,
      photoVisible: false, //控制图片选择器的显示
      photoList: [],
      fileIds: "",
      icon: false, //控制删除图标的显示
      interval: null, //定义触发器
      isChange: false, // 表单内容是否改变
      changeCount: 0, // 改变计数器
      blogOriginalDictList: [], //存储区域字典
      blogPublishDictList: [], //是否字典
      blogLevelDictList: [], //博客推荐等级字典
      openDictList: [], // 是否启动字典
      blogOriginalDefault: null, //博客原创默认值
      blogLevelDefault: null, //博客等级默认值
      blogPublishDefault: null, //博客发布默认值
      openDefault: null, // 是否开启评论默认值
      fileList: [],
      localUploadVisible: false,
      form: {
        uid: null,
        title: null,
        summary: null,
        content: null,
        tagUid: null,
        fileUid: null,
        isOriginal: null, //是否原创
        isPublish: null,
        author: null, //作者
        clickCount: 0,
        articlesPart: null //文章出处
      },
      rules: {
        title: [
          {required: true, message: '标题不能为空', trigger: 'blur'}
        ],
        blogSortUid: [
          {required: true, message: '分类不能为空', trigger: 'blur'}
        ],
        level: [
          {required: true, message: '推荐等级不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '推荐等级只能为自然数'},
        ],
        isPublish: [
          {required: true, message: '发布字段不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '发布字段只能为自然数'},
        ],
        isOriginal: [
          {required: true, message: '原创字段不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '原创字段只能为自然数'},
        ],
        startComment: [
          {required: true, message: '网站评论不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '网站评论只能为自然数'},
        ],
        content: [
          {required: true, message: '内容不能为空', trigger: 'blur'}
        ]
      }
    };
  },
  created() {
    var that = this;
    //从dashboard传递过来的 tagUid 以及 blogSortUid
    var tempTag = this.$route.query.tag;
    var tempBlogSort = this.$route.query.blogSort;
    if(tempTag != undefined) {
      this.tagRemoteMethod(tempTag.name);
      this.tagKeyword = tempTag.tagUid;
    }
    if(tempBlogSort != undefined) {
      this.sortRemoteMethod(tempBlogSort.name);
      this.sortKeyword = tempBlogSort.blogSortUid;
    }

    // 获取字典
    this.getDictList()

    // 获取标签列表
    this.tagList()

    // 获取博客分类
    this.blogSortList()

    //获取博客列表
    this.blogList()

  },
  methods: {
    openLoading() {
      this.uploadLoading = Loading.service({
        lock: true,
        text: '正在努力上传中……'
      })
    },
    closeLoading() {
        this.uploadLoading.close()
    },
    tagList: function() {
      var tagParams = {};
      tagParams.pageSize = 100;
      tagParams.currentPage = 1;
      getTagList(tagParams).then(response => {
        this.tagData = response.data.records;
        this.tagOptions = response.data.records;
      });
    },
    blogSortList: function() {
      var blogSortParams = {};
      blogSortParams.pageSize = 100;
      blogSortParams.currentPage = 1;
      getBlogSortList(blogSortParams).then(response => {
        if(response.code == "success") {
          this.blogSortData = response.data.records;
          this.sortOptions = response.data.records;
        }
      });
    },
    blogList: function() {
      var params = {};
      params.keyword = this.keyword;
      params.blogSortUid = this.sortKeyword;
      params.tagUid = this.tagKeyword;
      params.levelKeyword = this.levelKeyword;
      params.isPublish = this.publishKeyword;
      params.isOriginal = this.originalKeyword;
      params.currentPage = this.currentPage;
      params.pageSize = this.pageSize;
      getBlogList(params).then(response => {
        if(response.code == "success") {
          this.tableData = response.data.records;
          this.currentPage = response.data.current;
          this.pageSize = response.data.size;
          this.total = response.data.total;
        }
      });
    },
    /**
     * 字典查询
     */
    getDictList: function () {

      var dictTypeList =  ['sys_recommend_level', 'sys_original_status', 'sys_publish_status', 'sys_normal_disable']

      getListByDictTypeList(dictTypeList).then(response => {
        if (response.code == "success") {

          var dictMap = response.data;

          this.blogOriginalDictList = dictMap.sys_original_status.list

          this.blogPublishDictList = dictMap.sys_publish_status.list

          this.blogLevelDictList = dictMap.sys_recommend_level.list

          this.openDictList = dictMap.sys_normal_disable.list

          if(dictMap.sys_original_status.defaultValue) {
            this.blogOriginalDefault = dictMap.sys_original_status.defaultValue;
          }

          if(dictMap.sys_publish_status.defaultValue) {
            this.blogPublishDefault = dictMap.sys_publish_status.defaultValue;
          }

          if(dictMap.sys_recommend_level.defaultValue) {
            this.blogLevelDefault = dictMap.sys_recommend_level.defaultValue;
          }

          if(dictMap.sys_normal_disable.defaultValue) {
            this.openDefault = dictMap.sys_normal_disable.defaultValue;
          }

        }
      });
    },
    getFormObject: function() {
      var formObject = {
        uid: null,
        title: null,
        summary: null,
        content: null,
        tagUid: null,
        fileUid: null,
        isOriginal: this.blogOriginalDefault, //是否原创
        isPublish: this.blogOriginalDefault, //是否发布
        author: null, //作者
        level: parseInt(this.blogLevelDefault), //推荐等级，默认是正常
        startComment: this.openDefault, // 是否启动
        articlesPart: null //文章出处，默认蘑菇博客
      };
      return formObject;
    },
    // 跳转到该博客详情
    onClick: function(row) {
      window.open( this.BLOG_WEB_URL + "/#/info?blogUid=" + row.uid);
    },
    //标签远程搜索函数
    tagRemoteMethod: function(query) {
      if (query !== "") {
        var params = {};
        params.keyword = query;
        params.pageSize = 10;
        params.currentPage = 1;
        getTagList(params).then(response => {
          this.tagOptions = response.data.records;
        });
      } else {
        this.tagOptions = [];
      }
    },
    //分类远程搜索函数
    sortRemoteMethod: function(query) {
      if (query !== "") {
        var params = {};
        params.keyword = query;
        params.pageSize = 10;
        params.currentPage = 1;
        getBlogSortList(params).then(response => {
          this.sortOptions = response.data.records;
        });
      } else {
        this.sortOptions = [];
      }
    },
    //弹出选择图片框
    checkPhoto: function() {
      this.photoVisible = true;
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
    },
    checkPhoto() {
      this.photoList = [];
      this.fileIds = "";
      this.photoVisible = true;
    },
    submitStr: function(str, index) {
      if (str.length > index) {
        return str.slice(0, index) + "...";
      }
      return str;
    },
    // 关闭窗口
    closeDialog(done) {
      if(this.isChange) {
        this.$confirm("是否关闭博客编辑窗口", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            // 清空触发器
            clearInterval(this.interval);
            this.isChange = false;
            this.changeCount = 0
            done();
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消关闭"
            });
          });
      } else {
        // 清空触发器
        clearInterval(this.interval);
        this.isChange = false;
        this.changeCount = 0
        done();
      }
    },
    handleFind: function() {
      this.blogList();
    },
    handleAdd: function() {
      this.title = "增加博客"
      var that = this;
      var tempForm = JSON.parse(getCookie("form"));
      console.log('cookie中的', tempForm)
      if (tempForm != null && tempForm.title != null && tempForm.title != "") {
        this.$confirm("还有上次未完成的博客编辑，是否继续编辑?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.dialogFormVisible = true;
            this.tagValue = [];
            this.form = JSON.parse(getCookie("form"));
            var tagValue = this.form.tagUid.split(",");
            for (var a = 0; a < tagValue.length; a++) {
              if (tagValue[a] != null && tagValue[a] != "") {
                this.tagValue.push(tagValue[a]);
              }
            }
            if(this.form.uid) {
              this.title = "编辑博客";
              this.isEditForm = true;
            } else {
              this.title = "新增博客";
              this.isEditForm = false;
            }
          })
          .catch(() => {
            try {
              that.$refs.ckeditor.initData(); //清空CKEditor中内容
            } catch (error) {
              // 第一次还未加载的时候，可能会报错，不过不影响使用
              // 暂时还没有想到可能解决的方法
            }
            this.dialogFormVisible = true;

            this.form = this.getFormObject();

            try {
              that.$refs.ckeditor.setData(this.form.content); //设置富文本内容
            } catch (error) {
              // 第一次还未加载的时候，可能会报错，不过不影响使用
              // 暂时还没有想到可能解决的方法
            }
            this.tagValue = [];
            this.isEditForm = false;
            this.title = "新增博客";
            delCookie("form");
          });
      } else {
        this.dialogFormVisible = true;
        this.form = this.getFormObject();
        try {
          that.$refs.ckeditor.setData(this.form.content); //设置富文本内容
        } catch (error) {
          // 第一次还未加载的时候，可能会报错，不过不影响使用
          // 暂时还没有想到可能解决的方法
        }
        this.tagValue = [];
        this.isEditForm = false;
        this.formBak();
      }
    },
    handleUpload: function() {
      this.localUploadVisible = true
    },
    // 文件上传
    submitUpload() {
      let {uploadFiles, action} = this.$refs.uploadFile
      let data = {}
      data.pictureList = JSON.stringify(this.pictureList)
      this.openLoading()
      this.uploadFiles({
        uploadFiles,
        data,
        action,
        success: (response) => {
          let res = JSON.parse(response)
          if(res.code == "success") {
            this.$message({
              type: "success",
              message: "博客上传成功"
            })
            //获取博客列表
            this.blogList()
          } else {
            this.$message({
              type: "error",
              message: res.data
            })
          }
          this.localUploadVisible = false
          this.closeLoading()

          // 上传成功后，将里面的内容删除
          this.$refs.uploadFile.clearFiles();
          this.$refs.uploadPicture.clearFiles();
        },
        error: (error) => {
          this.closeLoading()
          console.log('失败了', error)
        }
      })
    },
    // 图片上传
    submitPictureUpload() {
      let {uploadFiles, action, data} = this.$refs.uploadPicture
      this.openLoading()
      this.uploadFiles({
        uploadFiles,
        data,
        action,
        success: (response) => {
          let res = JSON.parse(response)
          if(res.code == "success") {
            this.$message({
              type: "success",
              message: "图片上传成功"
            })
            let pictureList = res.data
            let list = []
            for(let a=0; a<pictureList.length; a++) {
              let picture = {}
              picture.uid = pictureList[a].uid
              picture.fileOldName = pictureList[a].fileOldName
              picture.picUrl = pictureList[a].picUrl
              picture.qiNiuUrl = pictureList[a].qiNiuUrl
              list.push(picture)
            }
            this.pictureList = list
          }
          this.closeLoading()
        },
        error: (error) => console.log('失败了', error)
      })

    },
    /**
     * 自定义上传文件
     * @param fileList 文件列表
     * @param data 上传时附带的额外参数
     * @param url 上传的URL地址
     * @param success 成功回调
     * @param error 失败回调
     */
    uploadFiles({uploadFiles, headers, data, action, success, error}) {
      let form = new FormData()
      // 文件对象
      uploadFiles.map(file => form.append("filedatas", file.raw))
      // 附件参数
      for (let key in data) {
        form.append(key, data[key])
      }
      let xhr = new XMLHttpRequest()
      // 异步请求
      xhr.open("post", action, true)
      // 设置请求头
      xhr.setRequestHeader("Authorization", getToken());
      xhr.onreadystatechange = function() {
        if (xhr.readyState == 4){
          if ((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304){
            success && success(xhr.responseText)
          } else {
            error && error(xhr.status)
          }
        }
      }
      xhr.send(form)
    },
    handleDownload: function() {
      if(this.multipleSelection.length <= 0 ) {
        this.$message({
          type: "error",
          message: "请先选中需要导出的博客！"
        });
        return;
      }

      let blogList = this.multipleSelection
      for(let a=0; a<blogList.length; a++) {
        this.$commonUtil.htmlToMarkdownFile(blogList[a].title, blogList[a].content)
      }
    },
    // 内容改变，触发监听
    contentChange: function() {
      var that = this;
      if(this.changeCount > 0) {
        that.isChange = true;
        //存放到cookie中，时间10天
        that.form.content = that.$refs.ckeditor.getData(); //获取CKEditor中的内容
        that.form.tagUid = that.tagValue.join(",");
        setCookie("form", JSON.stringify(that.form), 10);
      }
      this.changeCount = this.changeCount + 1;
    },
    //备份form表单
    formBak: function() {
      var that = this;
      that.interval = setInterval(function() {
        if (that.form.title != null && that.form.title != "") {
          //存放到cookie中，时间10天
          that.form.content = that.$refs.ckeditor.getData(); //获取CKEditor中的内容
          that.form.tagUid = that.tagValue.join(",");
          setCookie("form", JSON.stringify(that.form), 10);
        }
      }, 10000);
    },
    handleEdit: function(row) {
      this.title = "编辑博客";
      this.form = row;
      var that = this;
      try {
        that.$refs.ckeditor.setData(this.form.content); //设置富文本内容
      } catch (error) {
        // 第一次还未加载的时候，可能会报错，不过不影响使用
        // 暂时还没有想到可能解决的方法
      }
      this.tagValue = [];
      if (row.tagList) {
        var json = row.tagList;
        for (var i = 0, l = json.length; i < l; i++) {
          if (json[i] != null) {
            this.tagValue.push(json[i]["uid"]);
          }
        }
      }
      this.dialogFormVisible = true;
      this.isEditForm = true;
    },
    handleDelete: function(row) {
      var that = this;
      this.$confirm("此操作将把博客删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          var params = {};
          params.uid = row.uid;
          deleteBlog(params).then(response => {
            this.$message({
              type: "success",
              message: response.data
            });
            that.blogList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    handleDeleteBatch: function(row) {
      var that = this;
      if(that.multipleSelection.length <= 0 ) {
        this.$message({
          type: "error",
          message: "请先选中需要删除的博客！"
        });
        return;
      }
      this.$confirm("此操作将把选中博客删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteBatchBlog(that.multipleSelection).then(response => {
            this.$message({
              type: "success",
              message: response.data
            });
            that.blogList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    handleCurrentChange: function(val) {
      this.currentPage = val;
      this.blogList();
    },
    submitForm: function() {

      this.$refs.form.validate((valid) => {
        if(!valid) {

        } else {
          this.form.content = this.$refs.ckeditor.getData(); //获取CKEditor中的内容
          this.form.tagUid = this.tagValue.join(",");
          var params = formatData(this.form);
          if (this.isEditForm) {
            editBlog(this.form).then(response => {
              if (response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                });
                // 清空cookie中的内容
                delCookie("form");
                this.dialogFormVisible = false;
                this.blogList();
              } else {
                this.$message({
                  type: "error",
                  message: response.data
                });
              }
            });

          } else {
            addBlog(this.form).then(response => {
              if (response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                });

                // 清空cookie中的内容
                // Cookie("form", JSON.stringify(this.getFormObject()), 1);
                delCookie("form");

                // 清空触发器
                clearInterval(this.interval);

                this.dialogFormVisible = false;
                this.blogList();
              } else {
                this.$message({
                  type: "error",
                  message: response.data
                });
              }
            });
          }
        }
      })
    },
    // 改变多选
    handleSelectionChange(val) {
      this.multipleSelection = val;
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
  width:  195px;
  height: 105px;
  line-height: 105px;
  text-align: center;
}
.imgBody {
  width:  195px;
  height: 105px;
  border: solid 2px #ffffff;
  float: left;
  position: relative;
}
.uploadImgBody {
  margin-left: 5px;
  width:  195px;
  height: 105px;
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
.el-dialog__body {
  padding-top: 10px;
  padding-bottom: 0px;
}
.el-dialog {
  min-height: 400px;
}
.el-upload__tip {
  margin-top: 10px;
  margin-left: 10px;
  color: #3e999f;
}

.upload-demo {
  margin-top: 50px;
}
.tipBox {
  margin-bottom: 30px;
}
.tip {
  font-size: 14px;
  font-weight: bold;
  color: 	#808080;
}
.tipItem {
  line-height: 22px;
  color: 	#A9A9A9;
}
</style>
