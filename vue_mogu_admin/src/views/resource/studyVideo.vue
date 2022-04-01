<template>
<div id="table" class="app-container calendar-list-container">
	    <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
	      <el-input clearable class="filter-item" style="width: 200px;" v-model="keyword" placeholder="请输入分类名称"></el-input>
	      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/studyVideo/getList'">查找</el-button>
	      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/studyVideo/add'">添加</el-button>
        <el-button class="filter-item" type="danger" @click="handleDeleteBatch" icon="el-icon-delete" v-permission="'/studyVideo/deleteBatch'">删除选中</el-button>
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
	      	<img  v-if="scope.row.photoList" :src="scope.row.photoList[0]" style="width: 105px;height: 70px;"/>
	      </template>
	    </el-table-column>

      <el-table-column label="名称" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="分类名" width="160" align="center">
	      <template slot-scope="scope">
          <el-tag type="danger">{{ scope.row.resourceSort.sortName }}</el-tag>
	      </template>
	    </el-table-column>

      <el-table-column label="简介" width="160" align="center">
	      <template slot-scope="scope">
	        <span>{{ scope.row.summary }}</span>
	      </template>
	    </el-table-column>

      <el-table-column label="点击数" width="100" align="center">
	      <template slot-scope="scope">
	        <span>{{ scope.row.clickCount }}</span>
	      </template>
	    </el-table-column>

      <el-table-column label="百度云路径" width="200" align="center">
	      <template slot-scope="scope">
	        <span>{{ scope.row.baiduPath }}</span>
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
	      <template slot-scope="scope" >
	      	<el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/studyVideo/edit'">编辑</el-button>
	        <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/studyVideo/deleteBatch'">删除</el-button>
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
		<el-dialog :title="title" :visible.sync="dialogFormVisible" fullscreen="">
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

        <el-row :gutter="24">
          <el-col span="10">
            <el-form-item label="分类" :label-width="formLabelWidth" prop="resourceSortUid">
              <el-select
                v-model="form.resourceSortUid"
                size="small"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in resourceSortData"
                  :key="item.uid"
                  :label="item.sortName"
                  :value="item.uid"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col span="10">
            <el-form-item label="名称" :label-width="formLabelWidth">
              <el-input v-model="form.name" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col span="10">
            <el-form-item label="简介" :label-width="formLabelWidth">
              <el-input type="textarea" v-model="form.summary" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>

          <el-col span="10">
            <el-form-item label="百度云路径" :label-width="formLabelWidth">
              <el-input type="textarea" v-model="form.baiduPath" auto-complete="off"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="正文" :label-width="formLabelWidth">
		      <CKEditor ref="ckeditor" :content="form.content"></CKEditor>
		    </el-form-item>

		  </el-form>
		  <div slot="footer" class="dialog-footer">
		    <el-button @click="dialogFormVisible = false">取 消</el-button>
		    <el-button type="primary" @click="submitForm">确 定</el-button>
		  </div>
		</el-dialog>

		<CheckPhoto @choose_data="getChooseData" @cancelModel="cancelModel" :photoVisible="photoVisible" :photos="photoList" :files="fileIds" :limit="1"></CheckPhoto>

  </div>
</template>

<script>
import {
  getStudyVideoList,
  addStudyVideo,
  editStudyVideo,
  deleteBatchStudyVideo
} from "@/api/studyVideo";

import { getResourceSortList } from "@/api/resourceSort";
import CKEditor from "../../components/CKEditor";
import CheckPhoto from "../../components/CheckPhoto";

export default {
  components: {
    CheckPhoto,
    CKEditor
  },
  created() {
    this.studyVideoList();

    var resourceSortParams = {};
    resourceSortParams.pageSize = 100;
    resourceSortParams.currentPage = 1;
    getResourceSortList(resourceSortParams).then(response => {
      this.resourceSortData = response.data.records;
    });

  },
  data() {
    return {
      multipleSelection: [], //多选，用于批量删除
      tableData: [],
      resourceSortData: [], //资源分类列表
      form: {
        uid: null,
        name: null,
        fileUid: null
      },
      sortKeyword: "",
      sortOptions: [], //分类候选项
      loading: false,
      dialogVisible: false, //控制增加框和修改框的显示
      currentPage: 1,
      total: null,
      pageSize: 10,
      keyword: "",
      title: "增加视频",
      formLabelWidth: "120px", //弹框的label边框
      dialogFormVisible: false,
      isEditForm: false,
      photoVisible: false, //控制图片选择器的显示
      photoList: [],
      fileIds: "",
      icon: false, //控制删除图标的显示
      rules: {
        resourceSortUid: [
          {required: true, message: '分类不能为空', trigger: 'blur'},
        ]
      }
    };
  },
  methods: {
    studyVideoList: function() {
      var params = {};
      params.keyword = this.keyword;
      params.currentPage = this.currentPage;
      params.pageSize = this.pageSize;
      getStudyVideoList(params).then(response => {
        console.log("获取的响应", response);
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;
      });
    },
    handleFind: function() {
      this.currentPage = 1
      this.studyVideoList();
    },
    getFormObject: function() {
      var formObject = {
        uid: null,
        resourceSortUid: null,
        name: null,
        summary: null,
        content: null,
        fileUid: null,
        clickCount: 0
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
      console.log("点击了删除图片");

      this.form.photoList = null;
      this.form.fileUid = "";
    },
    //改变页码
    handleCurrentChange(val) {
      var that = this;
      console.log(`当前页: ${val}`);
      this.currentPage = val; //改变当前所指向的页数
      this.studyVideoList();
    },
    //点击新增
    handleAdd: function() {
      this.title = "增加视频"
      var that = this;
      try {
        that.$refs.ckeditor.initData(); //清空CKEditor中内容
      } catch (error) {
        // TODO 第一次还未加载的时候，可能会报错，不过不影响使用，暂时还没有想到可能解决的方法
      }
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    //点击编辑
    handleEdit: function(row) {
      this.title = "编辑视频"
      this.dialogFormVisible = true;
      this.isEditForm = true;
      this.form = row;
      var that = this;
      try {
        that.$refs.ckeditor.setData(this.form.content); //设置富文本内容
      } catch (error) {
        // 第一次还未加载的时候，可能会报错，不过不影响使用
        // 暂时还没有想到可能解决的方法
      }
      console.log("点击编辑", this.form);
    },
    handleDelete: function(row) {
      this.$confirm("此操作将会把分类下全部视频删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          var params = [];
          params.push(row);
          deleteBatchStudyVideo(params).then(response => {
            if(response.code == this.$ECode.SUCCESS) {
              this.$commonUtil.message.success(response.message)
              this.studyVideoList();
            } else {
              this.$commonUtil.message.error(response.message)
            }
          });
        })
        .catch(() => {
          this.$commonUtil.message.info("已取消删除")
        });
    },
    handleDeleteBatch: function() {
      var that = this;
      if(that.multipleSelection.length <= 0 ) {
        this.$commonUtil.message.error("请先选中需要删除的内容")
        return;
      }
      this.$confirm("此操作将把选中的视频删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteBatchStudyVideo(that.multipleSelection).then(response => {
            if(response.code == this.$ECode.SUCCESS) {
              this.$commonUtil.message.success(response.message)
              this.studyVideoList();
            } else {
              this.$commonUtil.message.success(response.message)
            }
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
          this.form.content = this.$refs.ckeditor.getData(); //获取CKEditor中的内容
          if (this.isEditForm) {
            editStudyVideo(this.form).then(response => {
              if(response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                this.dialogFormVisible = false;
                this.studyVideoList();
              } else {
                this.$commonUtil.message.error(response.message)
              }
            });
          } else {
            addStudyVideo(this.form).then(response => {
              if (response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                this.dialogFormVisible = false;
                this.studyVideoList();
              } else {
                this.$commonUtil.message.error(response.message)
              }
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
