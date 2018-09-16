<template>
  <div class="app-container">
      <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
	      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit">添加标签</el-button>	              
	    </div>

      <el-table :data="tableData"  style="width: 100%">
      
      <el-table-column type="selection"></el-table-column>
  		
      <el-table-column label="序号" width="60">
	      <template slot-scope="scope">
	        <span >{{scope.$index + 1}}</span>
	      </template>
	    </el-table-column>
	    
	    <el-table-column label="标签名" width="100">
	      <template slot-scope="scope">
	        <span>{{ scope.row.content }}</span>
	      </template>
	    </el-table-column>
	    
	    <el-table-column label="点击数" width="100">
	      <template slot-scope="scope">
	        <span>{{ scope.row.clickcount }}</span>
	      </template>
	    </el-table-column>
	    
	    <el-table-column label="创建时间" width="160">
	      <template slot-scope="scope">
	        <span >{{ scope.row.createtime }}</span>
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
          :total="total">
        </el-pagination>
    </div>

	  <!-- 添加或修改对话框 -->
		<el-dialog :title="title" :visible.sync="dialogFormVisible">
		  <el-form :model="form">
		  	
		    <el-form-item v-if="isEditForm == true" label="标签UID" :label-width="formLabelWidth">
		      <el-input v-model="form.uid" auto-complete="off" disabled></el-input>
		    </el-form-item>
		    
		   	<el-form-item v-if="isEditForm == false" label="标签UID" :label-width="formLabelWidth" style="display: none;">
		      <el-input v-model="form.uid" auto-complete="off"></el-input>
		    </el-form-item>
		    
        <el-form-item label="标签名" :label-width="formLabelWidth">
		      <el-input v-model="form.content" auto-complete="off"></el-input>
		    </el-form-item>
		    
		    <el-form-item label="标签点击数" :label-width="formLabelWidth">
		      <el-input v-model="form.clickcount" auto-complete="off"></el-input>
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
import { getTagList, addTag, editTag, deleteTag } from "@/api/tag";
import { formatData } from '@/utils/webUtils'
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
      formLabelWidth: '120px',
      isEditForm: false ,
      form: {
        uid: null,
        content: null,
        clickcount: null,
      }
    };
  },
  created() {
    var that = this;
    var params = new URLSearchParams();
    params.append("keyword", this.keyword);
    params.append("currentPage", this.currentPage);
    params.append("pageSize", this.pageSize);
    getTagList(params).then(response => {
      this.tableData = response.data.records;
      this.currentPage = response.data.current;
      this.pageSize = response.data.size;
      this.total = response.data.total;
      console.log(response);      
    });
  },
  methods: {
		tagList: function() {
			var params = new URLSearchParams();
			params.append("keyword", this.keyword);
			params.append("currentPage", this.currentPage);
			params.append("pageSize", this.pageSize);
			getTagList(params).then(response => {
				this.tableData = response.data.records;      
			});
		},
		getFormObject: function() {
      var formObject = {
				uid: null,
        content: null,
        clickcount: null,				
      };
      return formObject;
    },		
    handleAdd: function() {
			this.dialogFormVisible = true;
			this.form = this.getFormObject();
			this.isEditForm = false;
    },
    handleEdit: function(row) {
			this.dialogFormVisible = true;
			this.isEditForm = true;
			console.log(row);
			this.form = row;
    },
    handleDelete: function(row) {
			var that = this;
			let params = new URLSearchParams();
			params.append("uid", row.uid);
			deleteTag(params).then(response=> {
          console.log(response);
          this.$message({
            type: "success",
            message: response.data
          });
					that.tagList();
			})
    },
    handleCurrentChange: function(val) {
      console.log("点击了换页");
      this.currentPage = val;
      this.tagList();
    },
		submitForm: function() {
			console.log("点击了提交表单");
			var params = formatData(this.form);
			if(this.isEditForm) {
				editTag(params).then(response=> {
						console.log(response);
						this.$message({
              type: "success",
              message: response.data
            });
						this.dialogFormVisible = false;
						this.tagList();						
				})				
			} else {
				addTag(params).then(response=> {
						console.log(response);
						this.$message({
              type: "success",
              message: response.data
            });
						this.dialogFormVisible = false;
						this.tagList();						
				})
				
			}

		},
		
  }
};
</script>