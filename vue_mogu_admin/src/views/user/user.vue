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
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">查找</el-button>
<!--      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit">添加用户</el-button>-->
    </div>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="头像" width="120">
        <template slot-scope="scope">
          <img
            v-if="scope.row.photoUrl"
            :src="BASE_IMAGE_URL + scope.row.photoUrl"
            style="width: 100px;height: 100px;"
          >
        </template>
      </el-table-column>

      <el-table-column label="用户名" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.userName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="性别" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.gender==1" type="success">男</el-tag>
          <el-tag v-if="scope.row.gender==2" type="danger">女</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="邮箱" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.email }}</span>
        </template>
      </el-table-column>

      <el-table-column label="来源" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.source }}</span>
        </template>
      </el-table-column>

      <el-table-column label="登录次数" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.loginCount }}</span>
        </template>
      </el-table-column>

      <el-table-column label="登录IP" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.lastLoginIp }}</span>
        </template>
      </el-table-column>

      <el-table-column label="最后登录时间" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.lastLoginTime }}</span>
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

      <el-table-column label="操作" fixed="right" min-width="230">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status == 2" @click="handleStick(scope.row)" type="success" size="small">解结</el-button>
          <el-button v-if="scope.row.status == 1" @click="handleStick(scope.row)" type="warning" size="small">冻结</el-button>
<!--          <el-button @click="handleEdit(scope.row)" type="primary" size="small">编辑</el-button>-->
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
      <el-form :model="form">
        <el-form-item v-if="isEditForm == true" label="标签UID" :label-width="formLabelWidth">
          <el-input v-model="form.uid" auto-complete="off" disabled></el-input>
        </el-form-item>

        <el-form-item
          v-if="isEditForm == false"
          label="标签UID"
          :label-width="formLabelWidth"
          style="display: none;"
        >
          <el-input v-model="form.uid" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="标签名" :label-width="formLabelWidth">
          <el-input v-model="form.content" auto-complete="off"></el-input>
        </el-form-item>

        <!-- <el-form-item label="标签点击数" :label-width="formLabelWidth">
		      <el-input v-model="form.clickCount" auto-complete="off"></el-input>
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getUserList,
    deleteUser,
    freezeUser
  } from "@/api/user";

  export default {
    data() {
      return {
        BASE_IMAGE_URL: process.env.BASE_IMAGE_URL,
        tableData: [],
        keyword: "",
        currentPage: 1,
        pageSize: 10,
        total: 0, //总数量
        title: "增加用户",
        dialogFormVisible: false, //控制弹出框
        formLabelWidth: "120px",
        isEditForm: false,
        form: {
          uid: null,
        }
      };
    },
    created() {
      this.userList();
    },
    methods: {
      userList: function() {
        var params = {};
        params.keyword = this.keyword;
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
      handleStick: function(row) {
        this.$confirm("此操作将会冻结/解冻, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            var params = {};
            params.uid = row.uid;
            freezeUser(params).then(response => {
              if (response.code == "success") {
                this.userList();
                this.$message({
                  type: "success",
                  message: response.data
                });
              } else {
                this.$message({
                  type: "error",
                  message: response.data
                });
              }
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消冻结/解冻"
            });
          });
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
      handleCurrentChange: function(val) {
        this.currentPage = val;
        this.userList();
      },
      submitForm: function() {

      }
    }
  };
</script>
