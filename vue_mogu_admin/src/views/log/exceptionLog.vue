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
	      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">查找</el-button>         
	    </div>

      <el-table :data="tableData"  style="width: 100%">
      
      <el-table-column type="selection"></el-table-column>
      
      <el-table-column label="序号" width="60">
	      <template slot-scope="scope">
	        <span >{{scope.$index + 1}}</span>
	      </template>
	    </el-table-column>
	    

      <!-- <el-table-column label="异常Json格式" width="300">
	      <template slot-scope="scope">
	        <span>{{ scope.row.exceptionJson }}</span>
	      </template>
	    </el-table-column> -->

      <el-table-column label="异常内容" width="700">
	      <template slot-scope="scope">
	        <span>{{ scope.row.exceptionMessage }}</span>
	      </template>
	    </el-table-column>
	    
	    <el-table-column label="创建时间" width="160">
	      <template slot-scope="scope">
	        <span >{{ scope.row.createTime }}</span>
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


  </div>
</template>

<script>
import { getExceptionLogList } from "@/api/log";
export default {
  data() {
    return {
      tableData: [],
      userName: "",
      operation: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      formLabelWidth: "120px",
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
      value5: ""
    };
  },
  created() {
    this.exceptionLogList();
  },
  methods: {
    exceptionLogList: function() {
      var params = new URLSearchParams();
      params.append("userName", this.userName);
      params.append("operation", this.operation);
      params.append("startTime", this.value5);
      params.append("currentPage", this.currentPage);
      params.append("pageSize", this.pageSize);
      getExceptionLogList(params).then(response => {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;
      });
    },
    handleFind: function() {
      console.log(this.value5);
      this.exceptionLogList();
    },
    handleCurrentChange: function(val) {
      console.log("点击了换页");
      this.currentPage = val;
      this.exceptionLogList();
    }
  }
};
</script>