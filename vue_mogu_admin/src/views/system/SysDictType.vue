<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;" v-permission="'/sysDictType/getList'">

      <el-input
        clearable
        @keyup.enter.native="handleFind"
        class="filter-item"
        style="width: 200px;"
        v-model="query.dictType"
        placeholder="请输入字典类型"
      ></el-input>

      <el-input
        clearable
        @keyup.enter.native="handleFind"
        class="filter-item"
        style="width: 200px;"
        v-model="query.dictName"
        placeholder="请输入字典名称"
      ></el-input>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/sysDictType/getList'">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/sysDictType/add'">添加</el-button>
      <el-button class="filter-item" type="danger" @click="handleDeleteBatch" icon="el-icon-delete" v-permission="'/sysDictType/delete'">删除选中</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="字典类型" width="200" align="center">
        <template slot-scope="scope">
          <el-tag type="warning" style="cursor: pointer" >{{ scope.row.dictType }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="字典名称" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.dictName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="备注" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.remark }}</span>
        </template>
      </el-table-column>

      <el-table-column label="发布状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.isPublish == '1' ">上架</el-tag>
          <el-tag type="danger" v-else>下架</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="排序" width="50" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.sort }}</span>
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

      <el-table-column label="操作" fixed="right" min-width="240">
        <template slot-scope="scope">
          <el-button @click="handleList(scope.row)" type="success" size="small">列表</el-button>
          <el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/sysDictType/edit'">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/sysDictType/delete'">删除</el-button>
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

        <el-form-item label="字典类型" :label-width="formLabelWidth" prop="dictType">
          <el-input v-model="form.dictType" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="字典名称" :label-width="formLabelWidth" prop="dictName">
          <el-input v-model="form.dictName" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="发布状态" :label-width="formLabelWidth" prop="isPublish">
          <el-radio-group v-model="form.isPublish" size="small">
            <el-radio label="1" border>上架</el-radio>
            <el-radio label="0" border>下架</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="备注" :label-width="formLabelWidth">
          <el-input v-model="form.remark" auto-complete="off"></el-input>
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
  </div>
</template>

<script>
import {getSysDictTypeList, addSysDictType, editSysDictType, deleteBatchSysDictType} from "@/api/sysDictType"
import { formatData } from "@/utils/webUtils";
export default {
  data() {
    return {
      query: {}, // 查询对象
      multipleSelection: [], //多选，用于批量删除
      tableData: [],
      keyword: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加字典类型",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: "120px",
      isEditForm: false,
      form: {},
      rules: {
        dictType: [
          {required: true, message: '字典类型不能为空', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在1到20个字符'},
        ],
        dictName: [
          {required: true, message: '字典名称不能为空', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在1到20个字符'},
        ],
        isPublish: [
          {required: true, message: '发布状态不能为空', trigger: 'blur'}
        ],
        sort: [
          {required: true, message: '排序字段不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数'},
        ]
      }
    };
  },
  created() {
    this.sysDictTypeList();
  },
  methods: {
    sysDictTypeList: function() {
      var params = {};
      params.dictName = this.query.dictName;
      params.dictType = this.query.dictType;
      params.currentPage = this.currentPage;
      params.pageSize = this.pageSize;
      console.log('开始查找', params)
      getSysDictTypeList(params).then(response => {
        console.log("得到的类型", response)
        if(response.code == "success") {
          this.tableData = response.data.records;
          this.currentPage = response.data.current;
          this.pageSize = response.data.size;
          this.total = response.data.total;
        }
      });
    },
    // 这里可以设置一些初始值
    getFormObject: function() {
      var formObject = {
        isPublish: '1',
        sort: 0
      };
      return formObject;
    },
    handleFind: function() {
      this.sysDictTypeList();
    },
    handleAdd: function() {
      this.title = "增加字典类型"
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    handleEdit: function(row) {
      title: "编辑字典类型";
      this.dialogFormVisible = true;
      this.isEditForm = true;
      console.log(row);
      this.form = row;
    },
    handleDelete: function(row) {
      var that = this;
      this.$confirm("此操作将把字典类型删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let sysDictType = {};
          sysDictType.uid = row.uid;
          let deleteList = []
          deleteList.push(sysDictType)
          deleteBatchSysDictType(deleteList).then(response => {
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
            that.sysDictTypeList();
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
          message: "请先选中需要删除的内容！"
        });
        return;
      }
      this.$confirm("此操作将把选中字典类型删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteBatchSysDictType(that.multipleSelection).then(response => {
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
            that.sysDictTypeList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    handleList: function(row) {
      var uid = row.uid;
      this.$router.push({
        path: "sysDictData",
        query: { dictTypeUid: uid }
      });
    },
    handleCurrentChange: function(val) {
      this.currentPage = val;
      this.sysDictTypeList();
    },
    // 改变多选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    submitForm: function() {

      this.$refs.form.validate((valid) => {
        if(!valid) {
          console.log("校验出错")
        } else {
          if (this.isEditForm) {
            editSysDictType(this.form).then(response => {
              console.log(response);
              if (response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                });
                this.dialogFormVisible = false;
                this.sysDictTypeList();
              } else {
                this.$message({
                  type: "success",
                  message: response.data
                });
              }
            });
          } else {
            addSysDictType(this.form).then(response => {
              if (response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                });
                this.dialogFormVisible = false;
                this.sysDictTypeList();
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
