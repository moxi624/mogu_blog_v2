<template>
  <div class="app-container">
      <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
        <el-date-picker
          clearable
          v-model="value5"
          type="datetimerange"
          :picker-options="pickerOptions2"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          align="right">
        </el-date-picker>
	      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/log/getExceptionList'">查找</el-button>
	    </div>

      <el-table :data="tableData"  style="width: 100%">

      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60" align="center">
	      <template slot-scope="scope">
	        <span >{{scope.$index + 1}}</span>
	      </template>
	    </el-table-column>


      <!-- <el-table-column label="异常Json格式" width="300">
	      <template slot-scope="scope">
	        <span>{{ scope.row.exceptionJson }}</span>
	      </template>
	    </el-table-column> -->

      <el-table-column label="异常内容" width="600" align="center">
	      <template slot-scope="scope">
	        <span>{{ scope.row.exceptionMessage }}</span>
	      </template>
	    </el-table-column>

      <el-table-column label="接口名" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.operation }}</span>
        </template>
      </el-table-column>

      <el-table-column label="IP" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.ip }}</span>
        </template>
      </el-table-column>

      <el-table-column label="IP来源" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.ipSource }}</span>
        </template>
      </el-table-column>


        <el-table-column label="创建时间" width="160" align="center">
	      <template slot-scope="scope">
	        <span >{{ scope.row.createTime }}</span>
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
          <el-button @click="handleShow(scope.row)" type="primary" size="small">详情</el-button>
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
          :total="total">
        </el-pagination>
    </div>

    <el-dialog
      title="异常详情"
      :visible.sync="dialogVisible"
      :fullscreen = "true">
      <h3>请求参数</h3>
      <span>{{params}}</span>
      <h3>异常详情</h3>
      <span>{{this.exceptionJson}}}</span>
    </el-dialog>

  </div>
</template>

<script>
import { getExceptionLogList } from "@/api/log";
export default {
  data() {
    return {
      tableData: [],
      params: "",
      exceptionJson: "",
      userName: "",
      operation: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      formLabelWidth: "120px",
      dialogVisible: false,
      pickerOptions2: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            }
          }
        ]
      },
      value5: []
    };
  },
  created() {
    this.exceptionLogList();
  },
  methods: {
    exceptionLogList: function() {
      var params = {};
      params.userName = this.userName;
      params.operation = this.operation;
      if(this.value5.length >= 2) {
        params.startTime = this.value5[0] + "," + this.value5[1];
      }
      params.currentPage = this.currentPage;
      params.pageSize = this.pageSize;
      getExceptionLogList(params).then(response => {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;
      });
    },
    handleFind: function() {
      this.currentPage = 1
      this.exceptionLogList();
    },
    handleCurrentChange: function(val) {
      this.currentPage = val;
      this.exceptionLogList();
    },
    handleShow: function(row) {
      this.exceptionJson = row.exceptionJson;
      this.params = row.params;
      this.dialogVisible = true;
    }
  }
};
</script>
