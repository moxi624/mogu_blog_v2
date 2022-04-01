<template>
<div id="table" class="app-container calendar-list-container">
	    <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
	      <el-input clearable class="filter-item" style="width: 200px;" v-model="keyword" placeholder="请输入分类名称"></el-input>
	      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/resourceSort/getList'">查找</el-button>
	      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/resourceSort/add'">添加</el-button>
        <el-button class="filter-item" type="danger" @click="handleDeleteBatch" icon="el-icon-delete" v-permission="'/resourceSort/deleteBatch'">删除选中</el-button>
	    </div>

    <el-table :data="tableData"  style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60" align="center">
	      <template slot-scope="scope">
	        <span >{{scope.$index + 1}}</span>
	      </template>
	    </el-table-column>

	   	<el-table-column label="标题图" width="160" align="center">
	      <template slot-scope="scope">
	      	<img  v-if="scope.row.photoList" :src="scope.row.photoList[0]" style="width: 130px;height: 70px;"/>
	      </template>
	    </el-table-column>

	    <el-table-column label="分类名" width="160" align="center">
	      <template slot-scope="scope">
	        <span>{{ scope.row.sortName }}</span>
	      </template>
	    </el-table-column>

        <el-table-column label="分类介绍" width="160" align="center">
	      <template slot-scope="scope">
	        <span>{{ scope.row.content }}</span>
	      </template>
	    </el-table-column>

      <el-table-column label="排序" width="100" align="center">
        <template slot-scope="scope">
          <el-tag type="warning">{{ scope.row.sort }}</el-tag>
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

	    <el-table-column label="操作" fixed="right" min-width="220">
	      <template slot-scope="scope" >
          <el-button @click="handleStick(scope.row)" type="warning" size="small" v-permission="'/resourceSort/stick'">置顶</el-button>
	      	<el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/resourceSort/edit'">编辑</el-button>
	        <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/resourceSort/delete'">删除</el-button>
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
		  <el-form :model="form" :rules="rules" ref="form">

				<el-form-item label="图片" :label-width="formLabelWidth">
	    		<div class="imgBody" v-if="form.photoList">
	    		  	<i class="el-icon-error inputClass" v-show="icon" @click="deletePhoto()" @mouseover="icon = true"></i>
	    			<img @mouseover="icon = true" @mouseout="icon = false" v-bind:src="form.photoList[0]" style="display:inline; width: 195px;height: 105px;"/>
	    		</div>
	    		<div v-else class="uploadImgBody" @click="checkPhoto">
 		 			<i class="el-icon-plus avatar-uploader-icon"></i>
		    	</div>
		    </el-form-item>

		    <el-form-item label="分类名" :label-width="formLabelWidth" prop="sortName">
		      <el-input v-model="form.sortName" auto-complete="off"></el-input>
		    </el-form-item>

		    <el-form-item label="分类介绍" :label-width="formLabelWidth">
		      <el-input type="textarea" v-model="form.content" auto-complete="off"></el-input>
		    </el-form-item>

        <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
          <el-input v-model="form.sort" auto-complete="off"></el-input>
        </el-form-item>

		  </el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="dialogFormVisible = false">取 消</el-button>
		    <el-button type="primary" @click="submitForm">确 定</el-button>
		  </div>
		</el-dialog>
    		<!--
        	作者：xzx19950624@qq.com
        	时间：2018年9月23日16:16:09
         描述：图片选择器
        -->
		<CheckPhoto @choose_data="getChooseData" @cancelModel="cancelModel" :photoVisible="photoVisible" :photos="photoList" :files="fileIds" :limit="1"></CheckPhoto>

  </div>
</template>

<script>
import {
  getResourceSortList,
  addResourceSort,
  editResourceSort,
  deleteBatchResourceSort,
  stickResourceSort
} from "@/api/resourceSort";
import CheckPhoto from "../../components/CheckPhoto";

export default {
  components: {
    CheckPhoto
  },
  created() {
    this.resourceSortList();
  },
  data() {
    return {
      multipleSelection: [], //多选，用于批量删除
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
      isEditForm: false,
      photoVisible: false, //控制图片选择器的显示
      photoList: [],
      fileIds: "",
      icon: false, //控制删除图标的显示
      rules: {
        sortName: [
          {required: true, message: '分类名不能为空', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在1到20个字符'},
        ],
        sort: [
          {required: true, message: '排序字段不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数'},
        ]
      }
    };
  },
  methods: {
    resourceSortList: function() {
      var params = {};
      params.keyword = this.keyword;
      params.currentPage = this.currentPage;
      params.pageSize = this.pageSize;

      getResourceSortList(params).then(response => {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;
      });
    },
    handleFind: function() {
      this.currentPage = 1
      this.resourceSortList();
    },
    getFormObject: function() {
      var formObject = {
        uid: null,
        sortName: null,
        content: null,
        fileUid: null,
        sort: 0
      };
      return formObject;
    },
    //弹出选择图片框
    checkPhoto: function() {
      this.photoList = [];
      this.fileIds = "";
      this.photoVisible = true;
    },
    getChooseData(data) {
      var that = this;
      this.photoVisible = false;
      this.photoList = data.photoList;
      this.fileIds = data.fileIds;
      var fileId = this.fileIds.replace(",", "");
      if (this.photoList.length >= 1) {
        this.form.fileUid = fileId;
        this.form.photoList = this.photoList;
      }
    },
    //关闭模态框
    cancelModel() {
      this.photoVisible = false;
    },
    deletePhoto: function() {
      this.form.photoList = null;
      this.form.fileUid = "";
    },
    //改变页码
    handleCurrentChange(val) {
      var that = this;
      this.currentPage = val; //改变当前所指向的页数
      this.resourceSortList();
    },
    //点击新增
    handleAdd: function() {
      this.title = "增加分类"
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    //点击编辑
    handleEdit: function(row) {
      this.title = "编辑分类"
      this.dialogFormVisible = true;
      this.isEditForm = true;
      console.log(row);
      this.form = row;
    },
    handleStick: function(row) {
      this.$confirm("此操作将会把该分类放到首位, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          var params = {};
          params.uid = row.uid;
          stickResourceSort(params).then(response => {
            if (response.code == this.$ECode.SUCCESS) {
              this.resourceSortList();
              this.$commonUtil.message.success(response.message)
            } else {
              this.$commonUtil.message.error(response.message)
            }
          });
        })
        .catch(() => {
          this.$commonUtil.message.info("已取消置顶")
        });
    },
    handleDelete: function(row) {
      this.$confirm("此操作将会把该分类删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          var params = [];
          params.push(row);
          deleteBatchResourceSort(params).then(response => {
            if (response.code == this.$ECode.SUCCESS) {
              this.$commonUtil.message.success(response.message)
            } else {
              this.$commonUtil.message.error(response.message)
            }
            this.resourceSortList();
          });
        })
        .catch(() => {
          this.$commonUtil.message.info("已取消删除")
        });
    },
    handleDeleteBatch: function() {
      var that = this;
      var that = this;
      if(that.multipleSelection.length <= 0 ) {
        this.$commonUtil.message.error("请先选中需要删除的内容")
        return;
      }
      this.$confirm("此操作将把选中的分类删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteBatchResourceSort(that.multipleSelection).then(response => {
            if (response.code == this.$ECode.SUCCESS) {
              this.$commonUtil.message.success(response.message)
            } else {
              this.$commonUtil.message.error(response.message)
            }
            that.resourceSortList();
          });
        })
        .catch(() => {
          this.$commonUtil.message.info("已取消删除")
        });
    },
    submitForm: function() {
      this.$refs.form.validate((valid) => {
        if(!valid) {
          console.log("校验出错")
        } else {
          if (this.isEditForm) {
            editResourceSort(this.form).then(response => {
              this.$commonUtil.message.success(response.message)
              this.dialogFormVisible = false;
              this.resourceSortList();
            });
          } else {
            addResourceSort(this.form).then(response => {
              if (response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
              } else {
                this.$commonUtil.message.error(response.message)
              }
              this.dialogFormVisible = false;
              this.resourceSortList();
            });
          }
        }
      })

    },
    // 改变多选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    }
  }
};
</script>
<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  margin: 0, 0, 0, 10px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 195px;
  height: 105px;
  line-height: 105px;
  text-align: center;
}
.imgBody {
  width: 195px;
  height: 105px;
  border: solid 2px #ffffff;
  float: left;
  position: relative;
}
.uploadImgBody {
  margin-left: 5px;
  width: 195px;
  height: 105px;
  border: dashed 1px #c0c0c0;
  float: left;
  position: relative;
}
.uploadImgBody :hover {
  border: dashed 1px #00ccff;
}
.inputClass {
  position: absolute;
}
.img {
  width: 100%;
  height: 100%;
}
</style>
