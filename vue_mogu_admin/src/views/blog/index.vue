<template>
  <div class="app-container">
      <!-- 查询和其他操作 -->
	    <div class="filter-container" style="margin: 10px 0 10px 0;">
				<el-input clearable class="filter-item" style="width: 200px;" v-model="keyword" placeholder="请输入博客名"></el-input>
	      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind">查找</el-button>
	      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit">添加博客</el-button>	              
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
	      	<img  v-if="scope.row.photoList" :src="scope.row.photoList[0]" style="width: 100px;height: 100px;"/>
	      </template>
	    </el-table-column>
		    
	    <el-table-column label="标题" width="160">
	      <template slot-scope="scope">
	        <span>{{ scope.row.title }}</span>
	      </template>
	    </el-table-column>

			<el-table-column label="作者" width="100">
	      <template slot-scope="scope">
	        <span>{{ scope.row.author }}</span>
	      </template>
	    </el-table-column>

			<el-table-column label="是否原创" width="100">
	      <template slot-scope="scope">
					<el-tag v-if="scope.row.isOriginal==0" type="success">是</el-tag>
	        <el-tag v-if="scope.row.isOriginal==1" type="danger">否</el-tag>
	      </template>
	    </el-table-column>

      <el-table-column label="分类" width="100">
	      <template slot-scope="scope">
	        <span>{{ scope.row.blogSort.sortName }}</span>
	      </template>
	    </el-table-column>
		    
	    <!-- <el-table-column label="简介" width="250">
	      <template slot-scope="scope">
	        <span>{{ submitStr(scope.row.summary, 30) }}</span>
	      </template>
	    </el-table-column> -->
	    
	   	<el-table-column label="标签" width="100">
	      <template slot-scope="scope">
	      	<template>		
	      	  <el-tag type="warning"  v-if="item" :key="index" v-for="(item, index) in scope.row.tagList">{{item.content}}</el-tag>		
	      	</template>
	      </template>
	    </el-table-column>

      <el-table-column label="推荐等级" width="150">
	      <template slot-scope="scope">
	        <span v-if="scope.row.level==0">正常</span>
          <span v-if="scope.row.level==1">一级推荐</span>
          <span v-if="scope.row.level==2">二级推荐</span>
          <span v-if="scope.row.level==3">三级推荐</span>
          <span v-if="scope.row.level==4">四级推荐</span>					
	      </template>
	    </el-table-column>

			<el-table-column label="点击数" width="100">
	      <template slot-scope="scope">
	        <span>{{ scope.row.clickCount }}</span>
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
		  	
		    <!-- <el-form-item v-if="isEditForm == true" label="博客UID" :label-width="formLabelWidth">
		      <el-input v-model="form.uid" auto-complete="off" disabled></el-input>
		    </el-form-item>
		    
		   	<el-form-item v-if="isEditForm == false" label="博客UID" :label-width="formLabelWidth" style="display: none;">
		      <el-input v-model="form.uid" auto-complete="off"></el-input>
		    </el-form-item> -->

				<el-form-item label="标题图" :label-width="formLabelWidth">
	    		<div class="imgBody" v-if="form.photoList">
	    		  	<i class="el-icon-error inputClass" v-show="icon" @click="deletePhoto()" @mouseover="icon = true"></i>
	    			<img @mouseover="icon = true" @mouseout="icon = false" v-bind:src="form.photoList[0]" style="display:inline; width: 150px;height: 150px;"/>	    		 
	    		</div>
	    		<div v-else class="uploadImgBody" @click="checkPhoto">
 		 			<i class="el-icon-plus avatar-uploader-icon"></i>
		    	</div>				
		    </el-form-item>

		    <el-form-item label="标题" :label-width="formLabelWidth" required>
		      <el-input v-model="form.title" auto-complete="off"></el-input>
		    </el-form-item>
		    
        <el-form-item label="简介" :label-width="formLabelWidth">
		      <el-input v-model="form.summary" auto-complete="off"></el-input>
		    </el-form-item>

        <el-form-item label="分类" :label-width="formLabelWidth" required>
          <el-select v-model="form.blogSortUid" size="small" placeholder="请选择">
            <el-option
                v-for="item in blogSortData"
                :key="item.uid"
                :label="item.sortName"
                :value="item.uid">
              </el-option>
          </el-select>				
		    </el-form-item>

				<el-form-item label="标签" :label-width="formLabelWidth" required>
				<el-select v-model="tagValue" multiple  size="small" placeholder="请选择" filterable >
					<el-option
				      v-for="item in tagData"
				      :key="item.uid"
				      :label="item.content"
				      :value="item.uid">
				    </el-option>
				</el-select>
				<!--<p v-if="labelValue.length > 2" style="color: red;">最多选择两个标签</p>-->
		    </el-form-item>

        <el-form-item label="推荐等级" :label-width="formLabelWidth" required>
				<el-select v-model="form.level" size="small" placeholder="请选择">
					<el-option
				      v-for="item in blogLevelList"
				      :key="item.value"
				      :label="item.label"
				      :value="item.value">
				    </el-option>
				</el-select>
		    </el-form-item>

				<el-form-item label="点击数" :label-width="formLabelWidth">
		      <el-input v-model="form.clickCount" auto-complete="off"></el-input>
		    </el-form-item>

				<el-form-item label="作者" :label-width="formLabelWidth" required>
		      <el-input v-model="form.author" auto-complete="off"></el-input>
		    </el-form-item>

				<el-form-item label="是否原创" :label-width="formLabelWidth" required>
		      <el-radio-group v-model="form.isOriginal" size="small">
							<el-radio label="1" border>是</el-radio>
							<el-radio label="0" border>否</el-radio>
					</el-radio-group>
		    </el-form-item>

				<el-form-item label="文章出处" :label-width="formLabelWidth">
		      <el-input v-model="form.articlesPart" auto-complete="off"></el-input>
		    </el-form-item>

        <el-form-item label="内容" :label-width="formLabelWidth" required>
		      <CKEditor ref="ckeditor" :content="form.content"></CKEditor>
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
import { getBlogList, addBlog, editBlog, deleteBlog } from "@/api/blog";
import { getTagList } from "@/api/tag";
import { getBlogSortList } from "@/api/blogSort";
import { formatData, formatDataToJson, formatDataToForm } from "@/utils/webUtils";
import CheckPhoto from "../../components/CheckPhoto";
import CKEditor from "../../components/CKEditor";
var querystring = require('querystring');
export default {
  components: {
    CheckPhoto,
    CKEditor
  },
  data() {
    return {
       CKEditorData: null,
      tableData: [], //博客数据
      tagData: [], //标签数据
      tagValue: [], //保存选中标签id(编辑时)
      blogSortData: [],
      keyword: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加博客",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: "120px",
      isEditForm: false,
      photoVisible: false, //控制图片选择器的显示
      photoList: [],
      fileIds: "",
      icon: false, //控制删除图标的显示
      blogLevelList: [
        { label: "正常", value: 0 },
        { label: "一级推荐", value: 1 },
        { label: "二级推荐", value: 2 },
        { label: "三级推荐", value: 3 },
        { label: "四级推荐", value: 4 },
      ],
      form: {
        uid: null,
        title: null,
        summary: null,
        content: null,
        tagUid: null,
				fileUid: null,
				isOriginal: null, //是否原创
				author: null, //作者
				articlesPart: null, //文章出处
      }
    };
  },
  created() {
    var that = this;
    
    this.blogList(); //获取博客列表

    var tagParams = new URLSearchParams();
    getTagList(tagParams).then(response => {
      this.tagData = response.data.records;
      console.log(response);
    });

    var blogSortParams = new URLSearchParams();
    getBlogSortList(blogSortParams).then(response => {
      this.blogSortData = response.data.records;
      console.log(response);
    });
  },
  methods: {
    blogList: function() {
      var params = new URLSearchParams();
      params.append("keyword", this.keyword);
      params.append("currentPage", this.currentPage);
      params.append("pageSize", this.pageSize);
      getBlogList(params).then(response => {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;
      });
    },
    getFormObject: function() {
      var formObject = {
        uid: null,
        title: null,
        summary: null,
        content: null,
        tagUid: null,
				fileUid: null,
        isOriginal: "1", //是否原创
        clickCount: 0,
        author: null, //作者
        level: 0, //推荐等级，默认是正常
				articlesPart: "蘑菇博客", //文章出处
      };
      return formObject;
    },
    getCkEditorData: function(data) {
      this.CKEditorData = data;
      console.log("获取内容", data);
      
    },
    emptyCkEditorData: function(data) {
      console.log(data);
    },
    //弹出选择图片框
    checkPhoto: function() {
      console.log(this.photoVisible);
      console.log("点击了选择图");
      this.photoVisible = true;
      console.log(this.photoVisible);
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
    checkPhoto() {
      this.photoList = [];
      this.fileIds = "";
      this.photoVisible = true;
    },
    submitStr: function(str, index) {
      if (str.length > index) {
        return str.slice(0, index) + "...";
      }
      return str;
    },
    handleFind: function() {
      this.blogList();
    },
    handleAdd: function() {
      var that = this;
      try {
        that.$refs.ckeditor.initData(); //清空CKEditor中内容            
      } catch (error) {
        // 第一次还未加载的时候，可能会报错，不过不影响使用
        // 暂时还没有想到可能解决的方法
      }        
      console.log("点击了添加博客");
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.tagValue = [];
      this.isEditForm = false;
    },
    handleEdit: function(row) {
      this.title = "编辑博客",
      this.form = row;
      this.tagValue = [];
      if (row.tagList) {
        var json = row.tagList;
        for (var i = 0, l = json.length; i < l; i++) {
          if (json[i] != null) {
            this.tagValue.push(json[i]["uid"]);
          }
        }
      }
      console.log(row, "点击了编辑");
      this.dialogFormVisible = true;
      this.isEditForm = true;
    },
    handleDelete: function(row) {
      console.log("点击了删除");
      var that = this;
      let params = new URLSearchParams();
      params.append("uid", row.uid);
      deleteBlog(params).then(response => {
        console.log(response);
        this.$message({
          type: "success",
          message: response.data
        });
        that.blogList();
      });
    },
    handleCurrentChange: function(val) {
      console.log("点击了换页");
      this.currentPage = val;
      this.blogList();
    },
    submitForm: function() {
      this.form.content = this.$refs.ckeditor.getData(); //获取CKEditor中的内容 
      this.form.tagUid = this.tagValue.join(",");
      console.log(this.form);
      var params = formatData(this.form);
      
      if (this.isEditForm) {
        editBlog(params).then(response => {
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
          this.blogList();
        });
      } else {
        addBlog(this.form).then(response => {
          console.log(response);
          this.$message({
            type: "success",
            message: response.data
          });
          this.dialogFormVisible = false;
          this.blogList();
        });
      }
    }
  }
};
</script>
<style>
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
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}
.imgBody {
  width: 150px;
  height: 150px;
  border: solid 2px #ffffff;
  float: left;
  position: relative;
}
.uploadImgBody {
  margin-left: 5px;
  width: 150px;
  height: 150px;
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
