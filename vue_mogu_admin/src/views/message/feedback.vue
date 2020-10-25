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
        placeholder="请输入标题"
      ></el-input>

      <el-select v-model="feedbackStatusKeyword" clearable placeholder="反馈状态" style="width:140px">
        <el-option
          v-for="item in feedbackStatusDictList"
          :key="item.uid"
          :label="item.dictLabel"
          :value="item.dictValue"
        ></el-option>
      </el-select>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/feedback/getList'">查找</el-button>
      <el-button class="filter-item" type="danger" @click="handleDeleteBatch" icon="el-icon-delete" v-permission="'/feedback/deleteBatch'">删除选中</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="反馈人" width="150" align="center">
        <template slot-scope="scope">
          <el-tag style="cursor: pointer" v-if="scope.row.user" @click.native="goUserInfo(scope.row.user)" type="primary">{{ scope.row.user.nickName }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="标题" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>

      <el-table-column label="反馈状态" width="100" align="center">
        <template slot-scope="scope">
          <template>
            <el-tag v-for="item in feedbackStatusDictList" :key="item.uid" :type="item.listClass" v-if="scope.row.feedbackStatus == item.dictValue">{{item.dictLabel}}</el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="反馈内容" width="200" align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" :content="scope.row.content" placement="top">
            <span>{{subText(scope.row.content, 10)}}</span>
          </el-tooltip>
        </template>
      </el-table-column>

      <el-table-column label="回复" width="200" align="center">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" :content="scope.row.reply" placement="top">
            <span>{{subText(scope.row.reply, 10)}}</span>
          </el-tooltip>
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
          <template v-if="scope.row.status == 0">
            <span>已删除</span>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" min-width="240">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/feedback/edit'">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/feedback/deleteBatch'">删除</el-button>
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

        <el-form-item label="标题" :label-width="formLabelWidth">
          <el-input v-model="form.title" auto-complete="off" disabled></el-input>
        </el-form-item>

        <el-form-item label="内容" :label-width="formLabelWidth">
          <el-input
            disabled
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 5}"
            placeholder="请输入反馈内容"
            v-model="form.content">
          </el-input>
        </el-form-item>

        <el-form-item label="反馈状态" :label-width="formLabelWidth" required>
          <el-select v-model="form.feedbackStatus" size="small" placeholder="请选择" style="width:100px">
            <el-option
              v-for="item in feedbackStatusDictList"
              :key="item.uid"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="回复" :label-width="formLabelWidth">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入回复"
            v-model="form.reply">
          </el-input>
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
    getFeedbackList,
    editFeedback,
    deleteBatchFeedback
  } from "@/api/feedback";
  import {getListByDictTypeList} from "@/api/sysDictData"
  import { formatData } from "@/utils/webUtils";
  export default {
    data() {
      return {
        multipleSelection: [], //多选，用于批量删除
        tableData: [],
        keyword: "",
        feedbackStatusKeyword: null, //反馈状态查询
        currentPage: 1,
        pageSize: 10,
        total: 0, //总数量
        title: "增加反馈",
        dialogFormVisible: false, //控制弹出框
        feedbackStatusDictList: [], // 反馈状态字典
        formLabelWidth: "120px",
        isEditForm: false,
        form: {}
      };
    },
    created() {
      // 字典查询
      this.getDictList()

      this.feedbackList();
    },
    methods: {
      handleDeleteBatch: function(row) {
        var that = this;
        if(that.multipleSelection.length <= 0 ) {
          this.$message({
            type: "error",
            message: "请先选中需要删除的内容！"
          });
          return;
        }
        this.$confirm("此操作将把选中博客删除, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            deleteBatchFeedback(that.multipleSelection).then(response => {
              this.$commonUtil.message.success(response.message)
              that.feedbackList();
            });
          })
          .catch(() => {
            this.$commonUtil.info("已取消删除")
          });
      },
      goUserInfo: function(user) {
        this.$router.push({
          path: "/user/user",
          query: { nickName: user.nickName, source: user.source }
        });
      },
      subText(str, index) {
        if(str == null || str == undefined) {
          return "";
        }
        if(str.length < index){
          return str;
        } else {
          return str.substring(0, index) + "..."
        }
      },
      feedbackList: function() {
        var params = {};
        params.title = this.keyword;
        params.feedbackStatus = this.feedbackStatusKeyword
        params.currentPage = this.currentPage;
        params.pageSize = this.pageSize;

        getFeedbackList(params).then(response => {
          if(response.code == this.$ECode.SUCCESS) {
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
          title: null,
          content: null
        };
        return formObject;
      },
      /**
       * 字典查询
       */
      getDictList: function () {
        var dictTypeList = ['sys_feedback_status']
        getListByDictTypeList(dictTypeList).then(response => {
          if (response.code == this.$ECode.SUCCESS) {
            let dictMap = response.data;
            this.feedbackStatusDictList = dictMap.sys_feedback_status.list
          }
        });
      },
      handleFind: function() {
        this.feedbackList();
      },
      handleEdit: function(row) {
        title: "编辑反馈";
        this.dialogFormVisible = true;
        this.isEditForm = true;
        this.form = row;
      },
      handleDelete: function(row) {
        var that = this;
        this.$confirm("此操作将把反馈删除, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            let params = [];
            var feedback = {}
            feedback.uid = row.uid
            params.push(feedback);
            deleteBatchFeedback(params).then(response => {
              this.$commonUtil.message.success(response.message)
              that.feedbackList();
            });
          })
          .catch(() => {
            this.$commonUtil.message.info("已取消删除")
          });
      },
      handleCurrentChange: function(val) {
        this.currentPage = val;
        this.feedbackList();
      },
      // 改变多选
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      submitForm: function() {
        if (this.isEditForm) {
          editFeedback(this.form).then(response => {
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
    }
  };
</script>
