<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">
      <el-select
        v-model="adminKeyword"
        filterable
        clearable
        remote
        reserve-keyword
        placeholder="请输入管理员名"
        :remote-method="adminRemoteMethod"
        :loading="loading"
      >
        <el-option
          v-for="item in adminOptions"
          :key="item.uid"
          :label="item.userName"
          :value="item.uid"
        ></el-option>
      </el-select>

      <el-select v-model="roleKeyword" placeholder="请选择" style="width:150px" clearable>
        <el-option
          v-for="item in roleOptions"
          :key="item.uid"
          :label="item.roleName"
          :value="item.uid"
        ></el-option>
      </el-select>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit">添加权限</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column type="selection"></el-table-column>
      <el-table-column label="序号" width="60">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="用户名" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.admin.userName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="角色名" width="250">
        <template slot-scope="scope">
          <span>{{ scope.row.role.roleName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <template v-if="scope.row.status == 1">
            <span>正常</span>
          </template>
          <template v-if="scope.row.status == 2">
            <span>推荐</span>
          </template>
          <template v-if="scope.row.status == 0">
            <span>已删除</span>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" min-width="230">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="primary" size="small">编辑</el-button>
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
        <el-form-item label="管理员名" :label-width="formLabelWidth">
          <el-select
            v-model="form.adminUid"
            filterable
            clearable
            remote
            reserve-keyword
            placeholder="请输入管理员名"
            :remote-method="adminRemoteMethod"
            :loading="loading"
          >
            <el-option
              v-for="item in adminOptions"
              :key="item.uid"
              :label="item.userName"
              :value="item.uid"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="角色名" :label-width="formLabelWidth">
          <el-select v-model="form.roleUid" placeholder="请选择" style="width:150px">
            <el-option
              v-for="item in roleOptions"
              :key="item.uid"
              :label="item.roleName"
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
import {
  getAdminRoleList,
  addAdminRole,
  editAdminRole,
  deleteAdminRole
} from "@/api/adminRole";

import { getRoleList } from "@/api/role";
import { getAdminList } from "@/api/admin";

import { formatData } from "@/utils/webUtils";
export default {
  data() {
    return {
      adminOptions: [], //管理员候选框
      roleOptions: [], //角色候选框
      loading: false, //搜索框加载状态

      tableData: [],
      adminKeyword: "",
      roleKeyword: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加分类",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: "120px",
      isEditForm: false,
      form: {
        uid: null,
        content: "",
        sortName: ""
      }
    };
  },
  created() {
    this.adminRoleList();
    this.roleList();
  },
  methods: {
    adminRoleList: function () {
      var params = new URLSearchParams();
      params.append("adminUid", this.adminKeyword);
      params.append("roleUid", this.roleKeyword);
      params.append("currentPage", this.currentPage);
      params.append("pageSize", this.pageSize);
      getAdminRoleList(params).then(response => {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;

        //初始化选项列表
        var data = this.tableData;
        for (let a = 0; a < data.length; a++) {
          let tag1 = false;
          let tag2 = false;

          this.adminOptions.forEach(element => {
            if (element.uid == data[a].admin.uid) {
              tag1 = true;
            }
          });

          this.roleOptions.forEach(element => {
            if (element.uid == data[a].role.uid) {
              tag2 = true;
            }
          });

          if (!tag1) {
            this.adminOptions.push(data[a].admin);
          }
          if (!tag2) {
            this.roleOptions.push(data[a].role);
          }
        }

      });
    },
    getFormObject: function () {
      var formObject = {
        uid: null,
        content: null,
        sortName: null
      };
      return formObject;
    },
    handleFind: function () {
      this.adminRoleList();
    },
    handleAdd: function () {
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    handleEdit: function (row) {
      this.dialogFormVisible = true;
      this.isEditForm = true;
      this.form = row;
    },

    //管理员远程搜索函数
    adminRemoteMethod: function (query) {
      if (query !== "") {
        var params = new URLSearchParams();
        params.append("keyword", query);
        getAdminList(params).then(response => {
          this.adminOptions = response.data.records;
        });
      } else {
        this.adminOptions = [];
      }
    },

    //角色远程搜索函数
    roleList: function () {

      var params = {};
      params.currentPage = 1;
      params.pageSize = 10;

      getRoleList(params).then(response => {
        this.roleOptions = response.data.records;
      });

    },

    handleDelete: function (row) {
      var that = this;
      this.$confirm("此操作将把分类删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = new URLSearchParams();
          params.append("uid", row.uid);
          deleteAdminRole(params).then(response => {
            this.$message({
              type: "success",
              message: response.data
            });
            that.adminRoleList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    handleCurrentChange: function (val) {
      this.currentPage = val;
      this.adminRoleList();
    },
    submitForm: function () {
      if (this.isEditForm) {
        editAdminRole(this.form).then(response => {
          if (response.code == this.$ECode.SUCCESS) {
            this.$message({
              type: "success",
              message: response.data
            });
            this.dialogFormVisible = false;
            this.adminRoleList();
          } else {
            this.$message({
              type: "error",
              message: response.data
            });
          }
        });
      } else {
        addAdminRole(this.form).then(response => {
          if (response.code == this.$ECode.SUCCESS) {
            this.$message({
              type: "success",
              message: response.data
            });
            this.dialogFormVisible = false;
            this.adminRoleList();
          } else {
            this.$message({
              type: "error",
              message: response.data
            });
          }
        });
      }
    }
  }
};
</script>
