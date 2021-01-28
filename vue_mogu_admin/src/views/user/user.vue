<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">
      <el-input
        @keyup.enter.native="handleFind"
        class="filter-item"
        clearable
        placeholder="请输入用户名"
        style="width: 200px;"
        v-model="keyword"
      ></el-input>

      <el-select clearable placeholder="账号类型" style="width:140px" v-model="accountSourceKeyword">
        <el-option
          :key="item.uid"
          :label="item.dictLabel"
          :value="item.dictValue"
          v-for="item in accountSourceDictList"
        ></el-option>
      </el-select>

      <el-select clearable placeholder="评论状态" style="width:140px" v-model="commentStatusKeyword">
        <el-option
          :key="item.uid"
          :label="item.dictLabel"
          :value="item.dictValue"
          v-for="item in commentStatusDictList"
        ></el-option>
      </el-select>

      <el-button @click="handleFind" class="filter-item" icon="el-icon-search" type="primary"
                 v-permission="'/user/getList'">查找
      </el-button>

      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/user/add'">添加用户</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column type="selection"></el-table-column>

      <el-table-column align="center" label="序号" width="60">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="头像" width="120">
        <template slot-scope="scope">
          <img
            :src="scope.row.photoUrl ? scope.row.photoUrl:defaultAvatar" onerror="onerror=null;src=defaultAvatar"
            style="width: 100px;height: 100px;"
          >
        </template>
      </el-table-column>

      <el-table-column align="center" label="昵称" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.nickName }}</span>
        </template>
      </el-table-column>


      <el-table-column align="center" label="账号来源" width="100">
        <template slot-scope="scope">
          <template>
            <el-tag :key="item.uid" :type="item.listClass" v-for="item in accountSourceDictList"
                    v-if="scope.row.source == item.dictValue">{{item.dictLabel}}
            </el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column align="center" label="评论状态" width="100">
        <template slot-scope="scope">
          <template>
            <el-tag :key="item.uid" :type="item.listClass" v-for="item in commentStatusDictList"
                    v-if="scope.row.commentStatus == item.dictValue">{{item.dictLabel}}
            </el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column align="center" label="用户标签" width="100">
        <template slot-scope="scope">
          <template>
            <el-tag :key="item.uid" :type="item.listClass" v-for="item in userTagDictList"
                    v-if="scope.row.userTag == item.dictValue">{{item.dictLabel}}
            </el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column align="center" label="登录次数" width="50">
        <template slot-scope="scope">
          <span>{{ scope.row.loginCount }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="登录IP" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.lastLoginIp }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作系统" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.os }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="浏览器" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.browser }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="最后登录时间" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.lastLoginTime }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="邮箱" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.email }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="IP来源" width="160">
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

      <el-table-column fixed="right" label="操作" min-width="250">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" size="small" type="primary" v-permission="'/user/edit'">编辑
          </el-button>
          <el-button @click="handleUpdatePassword(scope.row)" size="small" type="primary"
                     v-permission="'/user/resetUserPassword'">重置密码
          </el-button>
          <el-button @click="handleDelete(scope.row)" size="small" type="danger" v-permission="'/user/delete'">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--分页-->
    <div class="block">
      <el-pagination
        :current-page.sync="currentPage"
        :page-size="pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        layout="total, prev, pager, next, jumper"
      ></el-pagination>
    </div>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item :label-width="formLabelWidth" label="用户头像">
          <div class="imgBody" v-if="form.photoUrl">
            <i @click="deletePhoto()" @mouseover="icon = true" class="el-icon-error inputClass" v-show="icon"></i>
            <img @mouseout="icon = false" @mouseover="icon = true" v-bind:src="form.photoUrl"/>
          </div>

          <div @click="checkPhoto" class="uploadImgBody" v-else>
            <i class="el-icon-plus avatar-uploader-icon"></i>
          </div>
        </el-form-item>

        <el-row :gutter="24">
          <el-col :span="9">
            <el-form-item :label-width="formLabelWidth" label="用户名" prop="userName">
              <el-input v-model="form.userName"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="9">
            <el-form-item :label-width="formLabelWidth" label="昵称" prop="nickName">
              <el-input v-model="form.nickName"></el-input>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row :gutter="24">
          <el-col :span="9">
            <el-form-item :label-width="formLabelWidth" label="QQ号" prop="qqNumber">
              <el-input auto-complete="off" v-model="form.qqNumber"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item :label-width="formLabelWidth" label="职业">
              <el-input auto-complete="off" v-model="form.occupation"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="9">
            <el-form-item :label-width="formLabelWidth" label="邮箱" prop="email">
              <el-input auto-complete="off" v-model="form.email"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="9">
            <el-form-item label="性别" :label-width="formLabelWidth" prop="gender">
              <el-radio :key="gender.uid" :label="gender.dictValue" border
                        size="medium" v-for="gender in genderDictList" v-model="form.gender">{{gender.dictLabel}}
              </el-radio>
            </el-form-item>
          </el-col>

        </el-row>


        <el-row :gutter="24">
          <el-col :span="9">
            <el-form-item label="评论状态" :label-width="formLabelWidth" prop="commentStatus">
              <el-select placeholder="请选择" style="width:205px" v-model="form.commentStatus">
                <el-option
                  :key="item.uid"
                  :label="item.dictLabel"
                  :value="parseInt(item.dictValue)"
                  v-for="item in commentStatusDictList"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="9">
            <el-form-item label="用户标签" :label-width="formLabelWidth" prop="userTag">
              <el-select placeholder="请选择"  style="width:205px" v-model="form.userTag">
                <el-option
                  :key="item.uid"
                  :label="item.dictLabel"
                  :value="parseInt(item.dictValue)"
                  v-for="item in userTagDictList"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item :label-width="formLabelWidth" label="简介">
          <el-input
            :autosize="{ minRows: 3, maxRows: 10}"
            placeholder="请输入内容"
            style="width: 70%"
            type="textarea"
            v-model="form.summary">
          </el-input>
        </el-form-item>

      </el-form>
      <div class="dialog-footer" slot="footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button @click="submitForm" type="primary">确 定</el-button>
      </div>
    </el-dialog>

    <avatar-cropper
      :height="300"
      :key="imagecropperKey"
      :url="url"
      :width="300"
      @close="close"
      @crop-upload-success="cropSuccess"
      lang-type="zh"
      v-show="imagecropperShow"
    />
  </div>
</template>

<script>
  import {deleteUser, addUser, editUser, getUserList, resetUserPassword} from "@/api/user";
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
        defaultAvatar: this.$SysConf.defaultAvatar, // 默认头像
        rules: {
          userName: [
            {required: true, message: '用户名不能为空', trigger: 'blur'},
            {min: 5, max: 30, message: '长度在5到30个字符'},
          ],
          nickName: [
            {required: true, message: '昵称不能为空', trigger: 'blur'},
            {min: 1, max: 30, message: '长度在1到30个字符'},
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
      if (source != undefined || nickName != undefined) {
        this.accountSourceKeyword = source;
        this.keyword = nickName;
        this.userList();
      }

      // 字典查询
      this.getDictList();
      this.userList();
    },
    methods: {
      userList: function () {
        var params = {};
        params.keyword = this.keyword;
        params.commentStatus = this.commentStatusKeyword;
        params.source = this.accountSourceKeyword;
        params.currentPage = this.currentPage;
        params.pageSize = this.pageSize;

        getUserList(params).then(response => {
          console.log("getUserList", response);
          if (response.code == this.$ECode.SUCCESS) {
            this.tableData = response.data.records;
            this.currentPage = response.data.current;
            this.pageSize = response.data.size;
            this.total = response.data.total;
          }
        });
      },

      cropSuccess(resData) {
        this.imagecropperShow = false
        this.imagecropperKey = this.imagecropperKey + 1
        this.form.photoUrl = resData[0].url;
        this.form.avatar = resData[0].uid
      },
      close() {
        this.imagecropperShow = false
      },
      deletePhoto: function () {
        console.log("删除图片", this.form)
        this.form.photoUrl = null;
        this.form.avatar = "";
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
        var dictTypeList = ['sys_account_source', 'sys_comment_status', 'sys_user_sex', 'sys_user_tag']
        getListByDictTypeList(dictTypeList).then(response => {
          if (response.code == this.$ECode.SUCCESS) {
            var dictMap = response.data;
            this.accountSourceDictList = dictMap.sys_account_source.list
            this.commentStatusDictList = dictMap.sys_comment_status.list
            this.genderDictList = dictMap.sys_user_sex.list
            this.userTagDictList = dictMap.sys_user_tag.list
          }
        });
      },
      getFormObject: function () {
        var formObject = {
          uid: null,
        };
        return formObject;
      },
      handleFind: function () {
        this.userList();
      },
      handleAdd: function () {
        this.dialogFormVisible = true;
        this.form = this.getFormObject();
        this.isEditForm = false;
      },
      handleEdit: function (row) {
        this.title = "编辑用户";
        this.dialogFormVisible = true;
        this.isEditForm = true;
        this.form = row;
      },
      handleDelete: function (row) {
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
              if(response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                that.userList();
              } else {
                this.$commonUtil.message.error(response.message)
              }
            });
          })
          .catch(() => {
            this.$commonUtil.message.info("已取消删除")
          });
      },
      handleUpdatePassword: function (row) {
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
              if(response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                that.userList();
              } else {
                this.$commonUtil.message.success(response.message)
              }
            });
          })
          .catch(() => {
            this.$commonUtil.message.info("已取消重置")
          });
      },
      handleCurrentChange: function (val) {
        this.currentPage = val;
        this.userList();
      },
      submitForm: function () {
        this.$refs.form.validate((valid) => {
          if (!valid) {
            console.log("校验出错")
          } else {
            if(this.isEditForm) {
              editUser(this.form).then(response => {
                if (response.code == this.$ECode.SUCCESS) {
                  this.$notify({
                    title: "成功",
                    message: response.message,
                    type: "success"
                  });
                  this.dialogFormVisible = false
                  this.userList();
                } else {
                  this.$notify.error({
                    title: "失败",
                    message: response.message
                  });
                }
              });
            } else {
              addUser(this.form).then(response => {
                if (response.code == this.$ECode.SUCCESS) {
                  this.$notify({
                    title: "成功",
                    message: response.message,
                    type: "success"
                  });
                  this.dialogFormVisible = false
                  this.userList();
                } else {
                  this.$notify.error({
                    title: "失败",
                    message: response.message
                  });
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
