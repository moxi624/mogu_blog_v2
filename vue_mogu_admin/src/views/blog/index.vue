<template>
  <div class="app-container">
      <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
	      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit">添加博客</el-button>	              
	    </div>

      <el-table :data="tableData"  style="width: 100%">
      
      <el-table-column type="selection"></el-table-column>
  		
      <el-table-column label="ID" width="60">
	      <template slot-scope="scope">
	        <span >{{scope.$index + 1}}</span>
	      </template>
	    </el-table-column>
	    
	   	<el-table-column label="标题图" width="160">
	      <template slot-scope="scope">
	      	<span>{{scope.row.photo}}</span>
	      </template>
	    </el-table-column>
		    
	    <el-table-column label="博客标题" width="160">
	      <template slot-scope="scope">
	        <span>{{ scope.row.title }}</span>
	      </template>
	    </el-table-column>
		    
	    <el-table-column label="博客简介" width="180">
	      <template slot-scope="scope">
	        <span>{{ scope.row.summary }}</span>
	      </template>
	    </el-table-column>
	    
	    <el-table-column label="博客点击数" width="100">
	      <template slot-scope="scope">
	        <span>¥{{ scope.row.clickcount }}</span>
	      </template>
	    </el-table-column>
	    
	   	<el-table-column label="博客标签" width="100">
	      <template slot-scope="scope">
	      	<template>		
	      	  <span >{{ scope.row.taguid }}</span>		
	      	</template>
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
	    
	    <el-table-column label="操作" fixed="right" min-width="200"> 
	      <template slot-scope="scope" >
	      	<el-button @click="handleEdit(scope.row)" type="primary" size="small">编辑</el-button>
	        <el-button @click="handleDelete(scope.row)" type="danger" size="small">删除</el-button>
	      </template>
	    </el-table-column>     	    
	  </el-table>

	  <!-- 添加或修改对话框 -->
		<el-dialog :title="title" :visible.sync="dialogFormVisible">
		  <el-form :model="form">
		  	
		    <el-form-item v-if="isEditForm == true" label="博客ID" :label-width="formLabelWidth">
		      <el-input v-model="form.uid" auto-complete="off" disabled></el-input>
		    </el-form-item>
		    
		   	<el-form-item v-if="isEditForm == false" label="博客ID" :label-width="formLabelWidth" style="display: none;">
		      <el-input v-model="form.uid" auto-complete="off"></el-input>
		    </el-form-item>
		    
		    <el-form-item label="博客标题" :label-width="formLabelWidth">
		      <el-input v-model="form.title" auto-complete="off"></el-input>
		    </el-form-item>
		    
        <el-form-item label="博客简介" :label-width="formLabelWidth">
		      <el-input v-model="form.summary" auto-complete="off"></el-input>
		    </el-form-item>

        <el-form-item label="博客内容" :label-width="formLabelWidth">
		      <el-input v-model="form.content" auto-complete="off"></el-input>
		    </el-form-item>

        <el-form-item label="标签UID" :label-width="formLabelWidth">
		      <el-input v-model="form.taguid" auto-complete="off"></el-input>
		    </el-form-item>

        <el-form-item label="标题图" :label-width="formLabelWidth">
		      <el-input v-model="form.photo" auto-complete="off"></el-input>
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
import { getBlogList } from "@/api/blog";
export default {
  data() {
    return {
      tableData: [],
      keyword: "",
      currentPage: 1,
      pageSize: 5,
      title: "增加博客",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: '120px',
      isEditForm: false ,
      form: {
        uid: null,
        title: null,
        summary: null,
        content: null,
        taguid: null,
        photo: null,
      }
    };
  },
  created() {
    var that = this;
    var params = new URLSearchParams();
    params.append("keyword", this.keyword);
    params.append("currentPage", this.currentPage);
    params.append("pageSize", this.pageSize);
    getBlogList(params).then(response => {
      this.tableData = response.data;
      console.log(response);
    });
  },
  methods: {
    handleAdd: function() {
      console.log("点击了添加博客");
      this.dialogFormVisible = true;
    },
    handleEdit: function(row) {
      console.log("点击了编辑");
    },
    handleDelete: function(row) {
      console.log("点击了删除");
    }
  }
};
</script>
