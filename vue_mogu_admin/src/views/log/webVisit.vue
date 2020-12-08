<template>
  <div class="app-container">
      <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
				<el-input clearable class="filter-item" style="width: 200px;" v-model="keyword" placeholder="关键字"></el-input>
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
	      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/webVisit/getList'">查找</el-button>
	    </div>

      <el-table :data="tableData"  style="width: 100%">

      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60" align="center">
	      <template slot-scope="scope">
	        <span >{{scope.$index + 1}}</span>
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

      <el-table-column label="平台" width="150" align="center">
	      <template slot-scope="scope">
	        <span>{{ scope.row.os }}</span>
	      </template>
	    </el-table-column>

      <el-table-column label="浏览器" width="200" align="center">
	      <template slot-scope="scope">
	        <span>{{ scope.row.browser }}</span>
	      </template>
	    </el-table-column>

	    <el-table-column label="用户行为" width="150" align="center">
	      <template slot-scope="scope">
	        <span>{{ scope.row.behaviorContent }}</span>
	      </template>
	    </el-table-column>

      <el-table-column label="内容" width="200" align="center">
	      <template slot-scope="scope">
          <span v-html="$xss(scope.row.content, options)"></span>
	      </template>
	    </el-table-column>

	    <el-table-column label="请求时间" width="160" align="center">
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
import { getWebVisitList } from "@/api/webVisit";
export default {
  data() {
    return {
      // xss白名单配置
      options : {
        whiteList: {
          a: ['href', 'title', 'target', 'style'],
          span: ['class', 'style']
        }
      },
      tableData: [],
      keyword: "",
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
    this.webVisitList();
  },
  methods: {
    webVisitList: function() {
      var params = {};
      params.keyword = this.keyword;
      params.startTime = "";
      if(this.value5) {
        params.startTime = this.value5[0] + "," + this.value5[1];
      }
      params.pageSize = this.pageSize;
      params.currentPage = this.currentPage;

      console.log("params", params, this.value5);
      getWebVisitList(params).then(response => {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;
      });
    },
    handleFind: function() {
      console.log(this.value5);
      this.webVisitList();
    },
    handleCurrentChange: function(val) {
      console.log("点击了换页");
      this.currentPage = val;
      this.webVisitList();
    }
  }
};
</script>
<style>
  @import "../../assets/css/emoji.css";
  .emoji-item-common {
    background: url("../../assets/img/emoji_sprite.png");
    display: inline-block;
  }
  .emoji-item-common:hover {
    cursor: pointer;
  }
  .emoji-size-small {
    zoom: 0.3;
    margin: 5px;
    vertical-align: middle;
  }
</style>
