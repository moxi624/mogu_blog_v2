<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">
      <el-input
        clearable
        class="filter-item"
        style="width: 200px;"
        v-model="keyword"
        placeholder="请输入角色名称"
      ></el-input>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/role/getList'">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/role/add'">添加角色</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column type="selection"></el-table-column>
      <el-table-column label="序号" width="60" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="角色名称" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.roleName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="角色介绍" width="300" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.summary }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="状态" width="100" align="center">
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

      <el-table-column label="操作" fixed="right" min-width="150">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/role/edit'">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/role/delete'">删除</el-button>
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
        <el-form-item label="角色名称" :label-width="formLabelWidth" prop="roleName">
          <el-input v-model="form.roleName" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="角色介绍" :label-width="formLabelWidth">
          <el-input v-model="form.summary" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="访问菜单" :label-width="formLabelWidth">
          <el-tree
            ref="tree"
            :data="categoryMenuList"
            show-checkbox
            node-key="uid"
            :props="defaultProps"
            :default-checked-keys="form.categoryMenuUids"
          ></el-tree>
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
import { getRoleList, addRole, editRole, deleteRole } from "@/api/role";

import { getAllMenu } from "@/api/categoryMenu";

import { formatData } from "@/utils/webUtils";
export default {
  data() {
    return {
      tableData: [],
      keyword: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加角色",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: "120px",
      isEditForm: false,
      form: {
        uid: null,
        roleName: "",
        summary: "",
        categoryMenuUids: [],
      },
      //分类菜单列表
      categoryMenuList: [],
      // tree配置项
      defaultProps: {
        children: "childCategoryMenu",
        label: "name"
      },
      rules: {
        roleName: [
          {required: true, message: '角色名称不能为空', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在1到20个字符'},
        ]
      }
    };
  },
  created() {

    this.allMenuList();
    this.roleList();
  },
  watch: {
    'form': {
      handler(newVal, oldVal) {
        console.log("value changed ", newVal, oldVal);
      },
      deep: true,
    }
  },
  methods: {
    allMenuList: function () {
      getAllMenu().then(response => {
        console.log(response);
        if (response.code == "success") {
          let data = response.data;
          this.categoryMenuList = data;
          console.log("得到的全部菜单", this.categoryMenuList)
        }
      });
    },
    handleFind: function () {
      this.roleList();
    },
    roleList: function () {
      var params = {};
      params.keyword = this.keyword;
      params.currentPage = this.currentPage;
      params.pageSize = this.pageSize;

      getRoleList(params).then(response => {
        console.log(response);
        if (response.code == "success") {
          var data = response.data.records;

          //初始化菜单UID
          for (let a = 0; a < data.length; a++) {
            if (data[a].categoryMenuUids) {
              data[a].categoryMenuUids = eval("(" + data[a].categoryMenuUids + ")");
            } else {
              data[a].categoryMenuUids = [];
            }
          }
          this.tableData = data;
          this.currentPage = response.data.current;
          this.pageSize = response.data.size;
          this.total = response.data.total;
        }

      });
    },
    getFormObject: function () {
      var formObject = {
        uid: null,
        roleName: null,
        summary: null,
        categoryMenuUids: [],
      };
      return formObject;
    },

    handleAdd: function () {
      this.title = "增加角色"
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      setTimeout(() => {
        this.$refs.tree.setCheckedKeys(this.form.categoryMenuUids);
      }, 100);
      this.isEditForm = false;
    },

    handleEdit: function (row) {
      this.title = "编辑角色"
      this.dialogFormVisible = true;
      this.isEditForm = true;
      this.form = row;
      setTimeout(() => {
        this.$refs.tree.setCheckedKeys(this.form.categoryMenuUids);
      }, 100);
    },

    handleDelete: function (row) {

      var that = this;
      this.$confirm("此操作将把分类删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          var params = {};
          params.uid = row.uid;

          deleteRole(params).then(response => {
            if(response.code == "success") {
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
            that.roleList();
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
      this.roleList();
    },
    submitForm: function () {

      this.$refs.form.validate((valid) => {
        if(!valid) {
          console.log("校验出错")
        } else {
          //得到选中树的UID
          var categoryMenuUids = this.$refs.tree.getCheckedKeys();
          console.log("全选UID", categoryMenuUids)

          // // 得到的半选UID(也就是父级菜单)
          // var halfCategoryMenuUids = this.$refs.tree.getHalfCheckedKeys();
          // console.log("半选UID", halfCategoryMenuUids)
          //
          // // 合并
          // categoryMenuUids = categoryMenuUids.concat(halfCategoryMenuUids);
          // console.log("合并后的", categoryMenuUids)

          this.form.categoryMenuUids = JSON.stringify(categoryMenuUids);
          if (this.isEditForm) {
            console.log("form", this.form);
            editRole(this.form).then(response => {
              console.log(response);
              if (response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                });
                this.dialogFormVisible = false;
                this.roleList();
              } else {
                this.$message({
                  type: "success",
                  message: response.data
                });
              }
            });
          } else {
            addRole(this.form).then(response => {
              console.log(response);
              if (response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                });
                this.dialogFormVisible = false;
                this.roleList();
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
    }
  }
};
</script>
