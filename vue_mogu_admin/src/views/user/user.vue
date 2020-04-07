<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">
      <el-input
        clearable
        @keyup.enter.native="handleFind"
        class="filter-item"
        style="width: 200px;"
        v-model="keyword"
        placeholder="请输入用户名"
      ></el-input>

      <el-select v-model="accountSourceKeyword" clearable placeholder="账号类型" style="width:140px">
        <el-option
          v-for="item in accountSourceDictList"
          :key="item.uid"
          :label="item.dictLabel"
          :value="item.dictValue"
        ></el-option>
      </el-select>

      <el-select v-model="commentStatusKeyword" clearable placeholder="评论状态" style="width:140px">
        <el-option
          v-for="item in commentStatusDictList"
          :key="item.uid"
          :label="item.dictLabel"
          :value="item.dictValue"
        ></el-option>
      </el-select>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">查找</el-button>
<!--      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit">添加用户</el-button>-->
    </div>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="头像" width="120" align="center">
        <template slot-scope="scope">
          <img
            v-if="scope.row.photoUrl"
            :src="BASE_IMAGE_URL + scope.row.photoUrl"
            style="width: 100px;height: 100px;"
          >
        </template>
      </el-table-column>

      <el-table-column label="用户名" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.nickName }}</span>
        </template>
      </el-table-column>

<!--      <el-table-column label="性别" width="100">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag v-if="scope.row.gender==1" type="success">男</el-tag>-->
<!--          <el-tag v-if="scope.row.gender==2" type="danger">女</el-tag>-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="账号来源" width="100" align="center">
        <template slot-scope="scope">
          <template>
            <el-tag v-for="item in accountSourceDictList" :key="item.uid" :type="item.listClass" v-if="scope.row.source == item.dictValue">{{item.dictLabel}}</el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="评论状态" width="100" align="center">
        <template slot-scope="scope">
          <template>
            <el-tag v-for="item in commentStatusDictList" :key="item.uid" :type="item.listClass" v-if="scope.row.commentStatus == item.dictValue">{{item.dictLabel}}</el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="用户标签" width="100" align="center">
        <template slot-scope="scope">
          <template>
            <el-tag v-for="item in userTagDictList" :key="item.uid" :type="item.listClass" v-if="scope.row.userTag == item.dictValue">{{item.dictLabel}}</el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="登录次数" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.loginCount }}</span>
        </template>
      </el-table-column>

      <el-table-column label="登录IP" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.lastLoginIp }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作系统" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.os }}</span>
        </template>
      </el-table-column>

      <el-table-column label="浏览器" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.browser }}</span>
        </template>
      </el-table-column>

      <el-table-column label="最后登录时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.lastLoginTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="邮箱" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.email }}</span>
        </template>
      </el-table-column>

      <el-table-column label="IP来源" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.ipSource }}</span>
        </template>
      </el-table-column>

      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <template v-if="scope.row.status == 1">
            <span>正常</span>
          </template>
          <template v-if="scope.row.status == 2">
            <span>冻结</span>
          </template>
          <template v-if="scope.row.status == 0">
            <span>已删除</span>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" min-width="250">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="primary" size="small">编辑</el-button>
          <el-button @click="handleUpdatePassword(scope.row)" type="primary" size="small">重置密码</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small">删除</el-button>
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
        <el-form-item label="用户头像" :label-width="formLabelWidth">
          <div class="imgBody" v-if="form.photoUrl">
            <i class="el-icon-error inputClass" v-show="icon" @click="deletePhoto()" @mouseover="icon = true"></i>
            <img @mouseover="icon = true" @mouseout="icon = false" v-bind:src="BASE_IMAGE_URL + form.photoUrl" />
          </div>

          <div v-else class="uploadImgBody" @click="checkPhoto">
            <i class="el-icon-plus avatar-uploader-icon"></i>
          </div>
        </el-form-item>

        <el-row :gutter="24">
          <el-col :span="9">
            <el-form-item label="用户名" :label-width="formLabelWidth" prop="nickName">
              <el-input v-model="form.nickName" ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item label="邮箱" :label-width="formLabelWidth" prop="email">
              <el-input v-model="form.email" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="9">
            <el-form-item label="QQ号" :label-width="formLabelWidth" prop="qqNumber">
              <el-input v-model="form.qqNumber" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item label="职业" :label-width="formLabelWidth">
              <el-input v-model="form.occupation" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="6">
            <el-form-item label="评论状态" label-width="120px" prop="commentStatus">
              <el-select v-model="form.commentStatus" size="small" placeholder="请选择" style="width:100px">
                <el-option
                  v-for="item in commentStatusDictList"
                  :key="item.uid"
                  :label="item.dictLabel"
                  :value="parseInt(item.dictValue)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label="用户标签" label-width="90px" prop="userTag">
              <el-select v-model="form.userTag" size="small" placeholder="请选择" style="width:100px">
                <el-option
                  v-for="item in userTagDictList"
                  :key="item.uid"
                  :label="item.dictLabel"
                  :value="parseInt(item.dictValue)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="性别" label-width="50px" prop="gender">
              <el-radio v-for="gender in genderDictList" :key="gender.uid" v-model="form.gender" :label="gender.dictValue" border size="medium">{{gender.dictLabel}}</el-radio>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="简介" :label-width="formLabelWidth">
          <el-input
            style="width: 70%"
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 10}"
            placeholder="请输入内容"
            v-model="form.summary">
          </el-input>
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
    getUserList,
    deleteUser,
    editUser,
    resetUserPassword
  } from "@/api/user";
  import AvatarCropper from '@/components/AvatarCropper'
  import {getListByDictTypeList} from "@/api/sysDictData"

  export default {
    data() {
      return {
        photoVisible: false, //控制图片选择器的显示
        photoList: [],
        fileIds: "",
        icon: false, //控制删除图标的显示
        imagecropperShow: false,
        imagecropperKey: 0,
        url: process.env.PICTURE_API + "/file/cropperPicture",
        BASE_IMAGE_URL: process.env.BASE_IMAGE_URL,
        tableData: [],
        keyword: "",
        accountSourceKeyword: "",
        commentStatusKeyword: "",
        currentPage: 1,
        pageSize: 10,
        total: 0, //总数量
        title: "增加用户",
        dialogFormVisible: false, //控制弹出框
        formLabelWidth: "120px",
        isEditForm: false,
        form: {
          uid: null,
        },
        accountSourceDictList: [], //账号来源字典
        commentStatusDictList: [], //评论状态字典
        genderDictList: [], //评论状态字典
        userTagDictList: [], // 用户标签列表
        rules: {
          nickName: [
            {required: true, message: '用户名不能为空', trigger: 'blur'},
            {min: 1, max: 20, message: '长度在1到20个字符'},
          ],
          commentStatus: [
            {required: true, message: '评论状态不能为空', trigger: 'blur'}
          ],
          userTag: [
            {required: true, message: '用户标签不能为空', trigger: 'blur'}
          ],
          gender: [
            {required: true, message: '性别不能为空', trigger: 'blur'},
          ],
          email: [
            {pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/, message: '请输入正确的邮箱'},
          ],
          qqNumber: [
            {pattern: /[1-9]([0-9]{5,11})/, message: '请输入正确的QQ号码'}
          ]
        }
      };
    },
    components: {
      AvatarCropper
    },
    created() {

      //传递过来的pictureSordUid
      let source = this.$route.query.source;
      let nickName = this.$route.query.nickName;
      if(source != undefined && nickName != undefined) {
        this.accountSourceKeyword = source;
        this.keyword = nickName;
        this.userList();
      }

      // 字典查询
      this.getDictList();
      this.userList();
    },
    methods: {
      userList: function() {
        var params = {};
        params.keyword = this.keyword;
        params.commentStatus = this.commentStatusKeyword;
        params.source = this.accountSourceKeyword;
        params.currentPage = this.currentPage;
        params.pageSize = this.pageSize;

        getUserList(params).then(response => {
          console.log("getUserList", response);
          if(response.code == "success") {
            this.tableData = response.data.records;
            this.currentPage = response.data.current;
            this.pageSize = response.data.size;
            this.total = response.data.total;
          }
        });
      },

      cropSuccess(resData) {
        console.log("裁剪成功", resData)
        this.imagecropperShow = false
        this.imagecropperKey = this.imagecropperKey + 1
        let photoList = []
        photoList.push(resData[0].url);
        this.form.photoList = photoList;
        this.form.avatar = resData[0].uid
      },
      close() {
        this.imagecropperShow = false
      },
      deletePhoto: function() {
        this.form.photoList = null;
        this.form.fileUid = "";
        this.icon = false;
      },
      //弹出选择图片框
      checkPhoto() {
        this.imagecropperShow = true
      },

      /**
       * 字典查询
       */
      getDictList: function () {

        var dictTypeList =  ['sys_account_source', 'sys_comment_status', 'sys_user_sex', 'sys_user_tag']

        getListByDictTypeList(dictTypeList).then(response => {
          if (response.code == "success") {
            var dictMap = response.data;
            this.accountSourceDictList = dictMap.sys_account_source.list
            this.commentStatusDictList = dictMap.sys_comment_status.list
            this.genderDictList = dictMap.sys_user_sex.list
            this.userTagDictList = dictMap.sys_user_tag.list
          }
        });
      },
      getFormObject: function() {
        var formObject = {
          uid: null,
        };
        return formObject;
      },
      handleFind: function() {
        this.userList();
      },
      handleAdd: function() {
        this.dialogFormVisible = true;
        this.form = this.getFormObject();
        this.isEditForm = false;
      },
      handleEdit: function(row) {
        this.title = "编辑用户";
        this.dialogFormVisible = true;
        this.isEditForm = true;
        this.form = row;
      },
      handleDelete: function(row) {
        var that = this;
        this.$confirm("此操作将把用户删除, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            var params = {};
            params.uid = row.uid;
            deleteUser(params).then(response => {
              this.$message({
                type: "success",
                message: response.data
              });
              that.userList();
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          });
      },
      handleUpdatePassword: function(row) {
        var that = this;
        this.$confirm("此操作将把用户密码重置为初始密码?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            var params = {};
            params.uid = row.uid;
            resetUserPassword(params).then(response => {
              this.$message({
                type: "success",
                message: response.data
              });
              that.userList();
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
        this.userList();
      },
      submitForm: function() {
        this.$refs.form.validate((valid) => {
          if(!valid) {
            console.log("校验出错")
          } else {
            editUser(this.form).then(response => {
              if(response.code == "success") {
                this.$notify({
                  title: "成功",
                  message: "保存成功！",
                  type: "success"
                });
                this.dialogFormVisible = false
              } else {
                this.$notify.error({
                  title: "失败",
                  message: response.data
                });
              }
            });
          }
        })
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
