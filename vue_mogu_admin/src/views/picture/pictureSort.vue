<template>
<div id="table" class="app-container calendar-list-container">
	    <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
	      <el-input clearable class="filter-item" style="width: 200px;" v-model="keyword" placeholder="请输入分类名称"></el-input>
	      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">查找</el-button>
	      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit">添加</el-button>	      
	    </div>

    <el-table :data="tableData"  style="width: 100%"> 
      <el-table-column type="selection"></el-table-column>
  		
      <el-table-column label="序号" width="60">
	      <template slot-scope="scope">
	        <span >{{scope.$index + 1}}</span>
	      </template>
	    </el-table-column>
	    
	   	<el-table-column label="标题图" width="160">
	      <template slot-scope="scope">
	      	<span>{{scope.row.fileUid}}</span>
	      </template>
	    </el-table-column>
		    
	    <el-table-column label="分类名" width="160">
	      <template slot-scope="scope">
	        <span>{{ scope.row.name }}</span>
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
          <el-button @click="handleManager(scope.row)" type="success" size="small">管理图片</el-button>
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
		  	
		    <el-form-item v-if="isEditForm == true" label="图片分类UID" :label-width="formLabelWidth">
		      <el-input v-model="form.uid" auto-complete="off" disabled></el-input>
		    </el-form-item>
		    
		   	<el-form-item v-if="isEditForm == false" label="图片分类UID" :label-width="formLabelWidth" style="display: none;">
		      <el-input v-model="form.uid" auto-complete="off"></el-input>
		    </el-form-item>

				<el-form-item label="标题图" :label-width="formLabelWidth">
		      <el-input v-model="form.fileUid" auto-complete="off"></el-input>
		    </el-form-item>
		    
		    <el-form-item label="标题" :label-width="formLabelWidth" required>
		      <el-input v-model="form.name" auto-complete="off"></el-input>
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
  getPictureSortList,
  addPictureSort,
  editPictureSort,
  deletPictureSort
} from "@/api/pictureSort";

import { formatData } from '@/utils/webUtils'

import { Loading } from "element-ui";

export default {
  created() {
    var that = this;
    	var params = new URLSearchParams();
			getPictureSortList(params).then(response => {
        console.log("初始化数据", response);
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
				this.pageSize = response.data.size;
				this.total = response.data.total;         
			});
    // var loadingInstance = Loading.service({
    //   target: "#table",
    //   text: "增加中"
    // });
  },
  data() {

    return {
      tableData: [],
      form: {
        uid: null,
        name: null,
        fileUid: null
      },
      loading: true,
      dialogVisible: false, //控制增加框和修改框的显示
      currentPage: 1,
      total: null,
      pageSize: 10,
      keyword: "",
      title: "增加分类",
      formLabelWidth: "120px", //弹框的label边框
      dialogFormVisible: false,
      isEditForm: false
    };
  },
  methods: {
    pictureSortList: function() {
			var params = new URLSearchParams();
			params.append("keyword", this.keyword);
			params.append("currentPage", this.currentPage);
			params.append("pageSize", this.pageSize);
			getPictureSortList(params).then(response => {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
				this.pageSize = response.data.size;
				this.total = response.data.total;         
			});
		},
    handleFind: function() {
      console.log(this.keyword);
      this.pictureSortList();
    },
    handleManager: function(row) {
      console.log("点击了图片管理");
      var uid = row.uid;
      this.$router.push({
        path: "picture",
        query: { pictureSortUid: uid }
      });
    },
    getFormObject: function() {
      var formObject = {
        uid: null,
        name: null,
        fileUid: null
      };
      return formObject;
    },
    //改变页码
    handleCurrentChange(val) {
      var that = this;
      console.log(`当前页: ${val}`);
      this.currentPage = val; //改变当前所指向的页数
      this.startGetList();
    },
    //点击新增
    handleAdd: function() {
      console.log("点击了添加");
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    //点击编辑
    handleEdit: function(row) {
      this.dialogFormVisible = true;
      this.isEditForm = true;
      console.log(row);
      this.form = row;
      console.log("点击编辑", this.form);
    },
    submitForm: function() {
      console.log("提交表单", this.form);
      var params = formatData(this.form);
      if (this.isEditForm) {
        editPictureSort(params).then(response => {
          console.log(response);
          this.$message({
            type: "success",
            message: response.data
          });
          this.dialogFormVisible = false;
          this.pictureSortList();
        });
      } else {
        addPictureSort(params).then(response => {
          console.log(response);
          if (response.code == "success") {
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

          this.dialogFormVisible = false;
          this.pictureSortList();
        });
      }
    }
  }
};
</script>