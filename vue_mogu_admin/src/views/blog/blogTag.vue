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
        placeholder="请输入博客名"
      ></el-input>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit">添加标签</el-button>
      <el-button
        class="filter-item"
        type="info"
        @click="handleTagSortByClickCount"
        icon="el-icon-document"
      >点击量排序</el-button>
      <el-button
        class="filter-item"
        type="info"
        @click="handleTagSortByCite"
        icon="el-icon-document"
      >引用量排序</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="标签名" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.content }}</span>
        </template>
      </el-table-column>

      <el-table-column label="点击数" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.clickCount }}</span>
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
          <el-button @click="handleStick(scope.row)" type="warning" size="small">置顶</el-button>
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
  getTagList,
  addTag,
  editTag,
  deleteTag,
  stickTag,
  tagSortByClickCount,
  tagSortByCite
} from "@/api/tag";
import { formatData } from "@/utils/webUtils";
export default {
  data() {
    return {
      tableData: [],
      keyword: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加标签",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: "120px",
      isEditForm: false,
      form: {
        uid: null,
        content: "",
        clickCount: null
      }
    };
  },
  created() {
    this.tagList();
  },
  methods: {
    tagList: function() {

      var params = {};
      params.keyword = this.keyword;
      params.currentPage = this.currentPage;
      params.pageSize = this.pageSize;

      getTagList(params).then(response => {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;
      });
    },
    getFormObject: function() {
      var formObject = {
        uid: null,
        content: null,
        clickCount: 0
      };
      return formObject;
    },
    handleFind: function() {
      this.tagList();
    },
    handleAdd: function() {
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    // 通过点击量排序
    handleTagSortByClickCount: function() {
      this.$confirm(
        "此操作将根据点击量对所有的标签进行降序排序, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          tagSortByClickCount().then(response => {
            if (response.code == "success") {
              this.tagList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消批量排序"
          });
        });
    },
    // 通过点击量排序
    handleTagSortByCite: function() {
      this.$confirm(
        "此操作将根据博客引用量对所有的标签进行降序排序, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
					
					tagSortByCite().then(response => {
            if (response.code == "success") {
              this.tagList();
            }
					});
					
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消批量排序"
          });
        });
    },
    handleEdit: function(row) {
      this.title = "编辑标签";
      this.dialogFormVisible = true;
      this.isEditForm = true;
      this.form = row;
    },
    handleStick: function(row) {
      this.$confirm("此操作将会把该标签放到首位, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {

          var params = {};
          params.uid = row.uid;

          stickTag(params).then(response => {
            if (response.code == "success") {
              this.tagList();
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
            message: "已取消置顶"
          });
        });
    },
    handleDelete: function(row) {
      var that = this;
      this.$confirm("此操作将把标签删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {

          var params = {};
          params.uid = row.uid;
          deleteTag(params).then(response => {
            console.log(response);
            this.$message({
              type: "success",
              message: response.data
            });
            that.tagList();
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
      this.tagList();
    },
    submitForm: function() {

      if (this.isEditForm) {
        editTag(this.form).then(response => {
          console.log(response);
          if (response.code == "success") {
            this.$message({
              type: "success",
              message: response.data
            });
            this.dialogFormVisible = false;
            this.tagList();
          } else {
            this.$message({
              type: "error",
              message: response.data
            });
          }
        });
      } else {
        addTag(this.form).then(response => {
          console.log(response);
          if (response.code == "success") {
            this.$message({
              type: "success",
              message: response.data
            });
            this.dialogFormVisible = false;
            this.tagList();
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