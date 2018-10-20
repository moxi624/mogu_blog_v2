<template>
  <div class="app-container">
      <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
				<el-input clearable class="filter-item" style="width: 200px;" v-model="keyword" placeholder="请输入评论名"></el-input>
	      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">查找</el-button>             
	    </div>

      <el-table :data="tableData"  style="width: 100%">
      
      <el-table-column type="selection"></el-table-column>
  		
      <el-table-column label="序号" width="60">
	      <template slot-scope="scope">
	        <span >{{scope.$index + 1}}</span>
	      </template>
	    </el-table-column>
	    
	    <el-table-column label="评论人" width="100">
	      <template slot-scope="scope">
	        <span>{{ scope.row.userName }}</span>
	      </template>
	    </el-table-column>

      <el-table-column label="内容" width="200">
	      <template slot-scope="scope">
	        <span>{{ scope.row.content }}</span>
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
	    
	    <el-table-column label="操作" fixed="right" min-width="150"> 
	      <template slot-scope="scope" >
          <el-button @click="handleShow(scope.row)" type="primary" size="small">详情</el-button>
          <el-button @click="handleReply(scope.row)" type="success" size="small">回复</el-button>
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
          :total="total">
        </el-pagination>
    </div>
  </div>
</template>

<script>
import { getCommentList, addComment, editComment, deleteComment } from "@/api/comment";
import { formatData } from '@/utils/webUtils'
export default {
  data() {
    return {
      tableData: [],
      keyword: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加友链",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: '120px',
      isEditForm: false 
    };
  },
  created() {
    this.commentList();
  },
  methods: {
		commentList: function() {
			var params = new URLSearchParams();
			params.append("keyword", this.keyword);
			params.append("currentPage", this.currentPage);
			params.append("pageSize", this.pageSize);
			getCommentList(params).then(response => {				
				this.tableData = response.data.records;
				this.currentPage = response.data.current;
				this.pageSize = response.data.size;
				this.total = response.data.total;      
			});
		},

		handleFind: function() {
			this.commentList();
		},			
    handleShow: function(row) {
      console.log("点击了查看详情");
    },
    handleReply: function(row) {
      console.log("点击了回复");
    },
    handleDelete: function(row) {
			var that = this;
			let params = new URLSearchParams();
			params.append("uid", row.uid);
			deleteComment(params).then(response=> {
          console.log(response);
          this.$message({
            type: "success",
            message: response.data
          });
					that.commentList();
			})
    },
    handleCurrentChange: function(val) {
      console.log("点击了换页");
      this.currentPage = val;
      this.commentList();
    }

  }
};
</script>