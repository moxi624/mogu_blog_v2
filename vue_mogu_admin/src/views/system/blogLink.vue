<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;" v-permission="'/link/getList'">
      <el-input
        clearable
        @keyup.enter.native="handleFind"
        class="filter-item"
        style="width: 200px;"
        v-model="keyword"
        placeholder="请输入友链名"
      ></el-input>

      <el-select v-model="linkStatusKeyword" clearable placeholder="友链状态" style="width:140px">
        <el-option
          v-for="item in linkStatusDictList"
          :key="item.uid"
          :label="item.dictLabel"
          :value="item.dictValue"
        ></el-option>
      </el-select>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/link/getList'">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/link/add'">添加友链</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="网站图标" width="80" align="center">
        <template slot-scope="scope">
          <img
            v-if="scope.row.photoList"
            :src="scope.row.photoList[0]"
            style="width: 50px;height:50px;"
          >
        </template>
      </el-table-column>

      <el-table-column label="友链名" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>

      <el-table-column label="友链简介" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.summary }}</span>
        </template>
      </el-table-column>

      <el-table-column label="友链URL" width="200" align="center">
        <template slot-scope="scope">
          <span @click="onClick(scope.row)" style="cursor:pointer;">{{ scope.row.url }}</span>
        </template>
      </el-table-column>

      <el-table-column label="站长邮箱" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.email }}</span>
        </template>
      </el-table-column>

      <el-table-column label="发布状态" width="100" align="center">
        <template slot-scope="scope">
          <template>
            <el-tag v-for="item in linkStatusDictList" :key="item.uid" :type="item.listClass" v-if="scope.row.linkStatus == item.dictValue">{{item.dictLabel}}</el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="点击数" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.clickCount }}</span>
        </template>
      </el-table-column>

      <el-table-column label="排序" width="100" align="center">
        <template slot-scope="scope">
          <el-tag type="warning">{{ scope.row.sort }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

<!--      <el-table-column label="状态" width="100" align="center">-->
<!--        <template slot-scope="scope">-->
<!--          <template v-if="scope.row.status == 1">-->
<!--            <span>正常</span>-->
<!--          </template>-->
<!--          <template v-if="scope.row.status == 2">-->
<!--            <span>推荐</span>-->
<!--          </template>-->
<!--          <template v-if="scope.row.status == 0">-->
<!--            <span>已删除</span>-->
<!--          </template>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="操作" fixed="right" min-width="240">
        <template slot-scope="scope">
          <el-button @click="handleStick(scope.row)" type="warning" size="small" v-permission="'/link/stick'">置顶</el-button>
          <el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/link/edit'">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/link/delete'">删除</el-button>
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
    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">

        <el-form-item label="网站图标" :label-width="formLabelWidth">
          <div class="imgBody" v-if="form.photoList">
            <i class="el-icon-error inputClass" v-show="icon" @click="deletePhoto()" @mouseover="icon = true"></i>
            <img @mouseover="icon = true" @mouseout="icon = false" v-bind:src="form.photoList[0]" />
          </div>
          <div v-else class="uploadImgBody" @click="checkPhoto">
            <i class="el-icon-plus avatar-uploader-icon"></i>
          </div>
        </el-form-item>

        <el-form-item label="友链名" :label-width="formLabelWidth" prop="title">
          <el-input v-model="form.title" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="友链简介" :label-width="formLabelWidth" prop="summary">
          <el-input v-model="form.summary" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="友链URL" :label-width="formLabelWidth" prop="url">
          <el-input v-model="form.url" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="站长邮箱" :label-width="formLabelWidth" prop="email">
          <el-input v-model="form.email" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="友链状态" :label-width="formLabelWidth" prop="linkStatus">
          <el-select v-model="form.linkStatus" size="small" placeholder="请选择" style="width:100px">
            <el-option
              v-for="item in linkStatusDictList"
              :key="item.uid"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
          <el-input v-model="form.sort" auto-complete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <avatar-cropper
      v-show="imagecropperShow"
      :key="imagecropperKey"
      :width="300"
      :height="300"
      :url="url"
      lang-type="zh"
      @close="close"
      @crop-upload-success="cropSuccess"
    />
  </div>
</template>

<script>
import {
  getLinkList,
  addLink,
  editLink,
  deleteLink,
  stickLink
} from "@/api/link";
import {getListByDictTypeList} from "@/api/sysDictData"
import AvatarCropper from '@/components/AvatarCropper'
export default {
  data() {
    return {
      tableData: [],
      keyword: "",
      linkStatusKeyword: null, //友链状态查询
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加友链",
      dialogFormVisible: false, //控制弹出框
      linkStatusDictList: [], // 友链状态字典
      linkStatusDefault: null, // 友链状态默认值
      formLabelWidth: "120px",
      isEditForm: false,
      imagecropperShow: false, // 是否显示截图框
      imagecropperKey: 0,
      url: process.env.PICTURE_API + "/file/cropperPicture",
      photoVisible: false, //控制图片选择器的显示
      photoList: [],
      fileIds: "",
      icon: false, //控制删除图标的显示
      form: {
        uid: null,
        content: "",
        clickCount: 0
      },
      rules: {
        title: [
          {required: true, message: '友链名称不能为空', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在1到20个字符'},
        ],
        url: [
          {required: true, message: 'URL不能为空', trigger: 'blur'},
          {pattern:  /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/, message: '请输入有效的URL'},
        ],
        email: [
          {pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/, message: '请输入正确的邮箱'},
        ],
        linkStatus: [
          {required: true, message: '友链状态不能为空', trigger: 'blur'}
        ],
        sort: [
          {required: true, message: '排序字段不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数'},
        ]
      }
    };
  },
  components: {
    AvatarCropper
  },
  created() {
    // 字典查询
    this.getDictList()

    this.linkList();
  },
  methods: {
    linkList: function() {
      var params = {};
      params.keyword = this.keyword;
      params.linkStatus = this.linkStatusKeyword
      params.currentPage = this.currentPage;
      params.pageSize = this.pageSize;

      getLinkList(params).then(response => {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;
      });
    },
    getFormObject: function() {
      var formObject = {
        uid: null,
        title: null,
        summary: null,
        url: null,
        clickCount: 0,
        sort: 0,
        linkStatus: this.linkStatusDefault
      };
      return formObject;
    },
    /**
     * 字典查询
     */
    getDictList: function () {
      var dictTypeList = ['sys_link_status']
      getListByDictTypeList(dictTypeList).then(response => {
        if (response.code == this.$ECode.SUCCESS) {
          var dictMap = response.data;
          this.linkStatusDictList = dictMap.sys_link_status.list
          if(dictMap.sys_link_status.defaultValue) {
            this.linkStatusDefault = parseInt(dictMap.sys_link_status.defaultValue);
          }
        }
      });
    },
    cropSuccess(resData) {
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      let photoList = []
      photoList.push(resData[0].url);
      this.form.photoList = photoList;
      this.form.fileUid = resData[0].uid
    },
    //弹出选择图片框
    checkPhoto() {
      this.imagecropperShow = true
    },
    close() {
      this.imagecropperShow = false
    },
    deletePhoto: function() {
      this.form.photoList = null;
      this.form.fileUid = "";
      this.icon = false;
    },
    handleFind: function() {
      this.linkList();
    },
    handleAdd: function() {
      this.title = "增加友链"
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    handleEdit: function(row) {
      title: "编辑友链";
      this.dialogFormVisible = true;
      this.isEditForm = true;
      console.log(row);
      this.form = row;
    },
    handleStick: function(row) {
      this.$confirm("此操作将会把该友链放到首位, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {};
          params.uid = row.uid;
          stickLink(params).then(response => {
            if (response.code == this.$ECode.SUCCESS) {
              this.linkList();
              this.$commonUtil.message.success(response.message)
            } else {
              this.$commonUtil.message.error(response.message)
            }
          });
        })
        .catch(() => {
          this.$commonUtil.message.info("已取消置顶")
        });
    },
    handleDelete: function(row) {
      var that = this;
      this.$confirm("此操作将把友链删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {};
          params.uid = row.uid;
          deleteLink(params).then(response => {
            if(response.code == this.$ECode.SUCCESS) {
              this.$commonUtil.message.success(response.message)
              that.linkList();
            } else {
              this.$commonUtil.message.error(response.message)
            }
          });
        })
        .catch(() => {
          this.$commonUtil.message.info("已取消删除")
        });
    },
    // 跳转到友链下
    onClick: function(row) {
      window.open(row.url);
    },
    handleCurrentChange: function(val) {
      this.currentPage = val;
      this.linkList();
    },
    submitForm: function() {

      this.$refs.form.validate((valid) => {
        if(!valid) {
          console.log("校验失败")
        } else {
          if (this.isEditForm) {
            editLink(this.form).then(response => {
              console.log(response);
              if (response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                this.dialogFormVisible = false;
                this.linkList();
              } else {
                this.$commonUtil.message.error(response.message)
              }
            });
          } else {
            addLink(this.form).then(response => {
              console.log(response);
              if (response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                this.dialogFormVisible = false;
                this.linkList();
              } else {
                this.$commonUtil.message.error(response.message)
              }
            });
          }
        }
      })

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
.img {
  width: 100%;
  height: 100%;
}
img {
  width: 100px;
  height: 100px;
}
</style>
