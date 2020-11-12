<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">
      <el-button class="filter-item" type="danger" @click="handleDeleteBatch" icon="el-icon-delete" v-permission="'/admin/forceLogout'">删除选中</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="50" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="用户名" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.userName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="角色" width="120" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.roleName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="ip" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.ipaddr }}</span>
        </template>
      </el-table-column>

      <el-table-column label="登录地点" width="150" align="center">
        <template slot-scope="scope">
          <el-tag type="warning">{{ scope.row.loginLocation }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作系统" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.os }}</span>
        </template>
      </el-table-column>

      <el-table-column label="浏览器" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.browser }}</span>
        </template>
      </el-table-column>

      <el-table-column label="登录时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.loginTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="过期时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.expireTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" min-width="100">
        <template slot-scope="scope">
          <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/admin/forceLogout'">强踢</el-button>
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
  </div>
</template>

<script>
import {
  getOnlineAdminList,
  forceLogout
} from "@/api/admin";
export default {
  data() {
    return {
      multipleSelection: [], //多选，用于批量删除
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
        content: ""
      },
    };
  },
  created() {
    this.onlineAdminList();
  },
  methods: {
    onlineAdminList: function() {
      var params = {};
      params.keyword = this.keyword;
      params.currentPage = this.currentPage;
      params.pageSize = this.pageSize;
      getOnlineAdminList(params).then(response => {
        if(response.code == this.$ECode.SUCCESS) {
          this.tableData = response.data.records;
          this.currentPage = response.data.current;
          this.pageSize = response.data.size;
          this.total = response.data.total;
        }
      });
    },
    handleFind: function() {
      this.onlineAdminList();
    },
    handleDelete: function(row) {
      console.log(row)
      var that = this;
      this.$confirm("此操作将把用户踢下线, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          var params = [];
          params.push(row.tokenId);
          forceLogout(params).then(response => {
            if(response.code == this.$ECode.SUCCESS) {
              this.$commonUtil.message.success(response.message)
            } else {
              this.$commonUtil.message.error(response.message)
            }
            that.onlineAdminList();
          });
        })
        .catch(() => {
          this.$commonUtil.message.info("已取消删除")
        });
    },
    handleDeleteBatch: function() {
      let tokenUidList = []
      let multipleSelection = this.multipleSelection;
      if(multipleSelection.length <= 0 ) {
        this.$commonUtil.message.error("请先选中需要踢出的用户!")
        return;
      } else {
        for(let a=0; a< multipleSelection.length; a++) {
          tokenUidList.push(multipleSelection[a].tokenId)
        }
      }
      this.$confirm("此操作将把选中的用户踢出, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          forceLogout(tokenUidList).then(response => {
            if(response.code == this.$ECode.SUCCESS) {
              this.$commonUtil.message.success(response.message)
            } else {
              this.$commonUtil.message.error(response.message)
            }
            this.onlineAdminList();
          });
        })
        .catch(() => {
          this.$commonUtil.message.info("已取消删除")
        });
    },
    handleCurrentChange: function(val) {
      this.currentPage = val;
      this.onlineAdminList();
    },
    // 改变多选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    }
  }
};
</script>
