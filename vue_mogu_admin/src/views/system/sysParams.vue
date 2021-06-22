<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;" v-permission="'/link/getList'">
      <el-input
        clearable
        @keyup.enter.native="handleFind"
        class="filter-item"
        style="width: 200px;"
        v-model="queryParams.paramsName"
        placeholder="请输入参数名"
      ></el-input>

      <el-input
        clearable
        @keyup.enter.native="handleFind"
        class="filter-item"
        style="width: 200px;"
        v-model="queryParams.paramsKey"
        placeholder="请输入参数键名"
      ></el-input>

<!--      <el-select v-model="queryParams.paramsType" clearable placeholder="系统内置" style="width:140px">-->
<!--        <el-option-->
<!--          v-for="item in paramsTypeDictList"-->
<!--          :key="item.uid"-->
<!--          :label="item.dictLabel"-->
<!--          :value="item.dictValue"-->
<!--        ></el-option>-->
<!--      </el-select>-->

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/link/getList'">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/link/add'">添加参数</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="参数名称" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.paramsName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="参数键名" width="240" align="center">
        <template slot-scope="scope">
          <el-tag type="primary">{{ scope.row.paramsKey }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="参数键值" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.paramsValue }}</span>
        </template>
      </el-table-column>

      <el-table-column label="系统内置" width="80" align="center">
        <template slot-scope="scope">
          <template>
            <el-tag v-for="item in paramsTypeDictList" :key="item.uid" :type="item.listClass" v-if="scope.row.paramsType == item.dictValue">{{item.dictLabel}}</el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column label="备注" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.remark }}</span>
        </template>
      </el-table-column>

      <el-table-column label="排序" width="100" align="center">
        <template slot-scope="scope">
          <el-tag type="warning">{{ scope.row.sort }}</el-tag>
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

      <el-table-column label="操作" fixed="right" min-width="240">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/link/edit'">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/link/delete'">删除</el-button>
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
        :total="total"
      ></el-pagination>
    </div>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">

        <el-form-item label="参数名称" :label-width="formLabelWidth" prop="paramsName">
          <el-input v-model="form.paramsName" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="参数键名" :label-width="formLabelWidth" prop="paramsKey">
          <el-input v-model="form.paramsKey" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="参数键值" :label-width="formLabelWidth" prop="paramsValue">
          <el-input v-model="form.paramsValue" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="系统内置" :label-width="formLabelWidth" prop="paramsType">
          <el-radio v-for="item in paramsTypeDictList" :key="item.uid" v-model="form.paramsType" :label="parseInt(item.dictValue)" border size="medium">{{item.dictLabel}}</el-radio>
        </el-form-item>

        <el-form-item label="备注" :label-width="formLabelWidth" prop="remark">
          <el-input v-model="form.remark" auto-complete="off"></el-input>
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
  </div>
</template>

<script>
import {
  getSysParamsList,
  addSysParams,
  editSysParams,
  deleteBatchSysParams
} from "@/api/sysParams";
import {getListByDictTypeList} from "@/api/sysDictData"
export default {
  data() {
    return {
      queryParams: {}, //查询参数
      tableData: [],
      keyword: "",
      linkStatusKeyword: null, //友链状态查询
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加友链",
      dialogFormVisible: false, //控制弹出框
      paramsTypeDictList: [], // 友链状态字典
      paramsTypeDefault: null, // 友链状态默认值
      formLabelWidth: "120px",
      isEditForm: false,
      form: {
        uid: null,
        content: "",
        clickCount: 0
      },
      rules: {
        paramsName: [
          {required: true, message: '参数名称不能为空', trigger: 'blur'},
          {min: 1, max: 100, message: '长度在1到100个字符'},
        ],
        paramsKey: [
          {required: true, message: '参数键名不能为空', trigger: 'blur'},
          {min: 1, max: 100, message: '长度在1到100个字符'},
        ],
        paramsValue: [
          {required: true, message: '友链状态不能为空', trigger: 'blur'},
          {min: 1, max: 100, message: '长度在1到100个字符'}
        ],
        paramsType: [
          {required: true, message: '系统内置字段不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '系统内置字段只能为自然数'},
        ],
        sort: [
          {required: true, message: '排序字段不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数'},
        ]
      }
    };
  },
  created() {
    // 字典查询
    this.getDictList()

    this.sysParamsList();
  },
  methods: {
    sysParamsList: function() {
      var params = {};
      params.paramsName = this.queryParams.paramsName;
      params.paramsKey = this.queryParams.paramsKey;
      params.currentPage = this.currentPage;
      params.pageSize = this.pageSize;

      getSysParamsList(params).then(response => {
        this.tableData = response.data.records;
        this.currentPage = response.data.current;
        this.pageSize = response.data.size;
        this.total = response.data.total;
      });
    },
    getFormObject: function() {
      var formObject = {
        paramsName: null,
        paramsKey: null,
        paramsValue: null,
        remark: "",
        paramsType: this.paramsTypeDefault,
        sort: 0
      };
      return formObject;
    },
    /**
     * 字典查询
     */
    getDictList: function () {

      var dictTypeList = ['sys_params_type']

      getListByDictTypeList(dictTypeList).then(response => {
        if (response.code == this.$ECode.SUCCESS) {
          var dictMap = response.data;
          this.paramsTypeDictList = dictMap.sys_params_type.list
          if(dictMap.sys_params_type.defaultValue) {
            this.paramsTypeDefault = parseInt(dictMap.sys_params_type.defaultValue);
          }
        }
      });
    },
    handleFind: function() {
      this.currentPage = 1
      this.sysParamsList();
    },
    handleAdd: function() {
      this.title = "增加参数"
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    handleEdit: function(row) {
      title: "编辑参数";
      this.dialogFormVisible = true;
      this.isEditForm = true;
      this.form = row;
    },
    handleDelete: function(row) {
      var that = this;
      this.$confirm("此操作将把参数配置删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let list = [row]
          deleteBatchSysParams(list).then(response => {
            if(response.code == this.$ECode.SUCCESS) {
              this.$commonUtil.message.success(response.message)
            } else {
              this.$commonUtil.message.error(response.message)
            }
            that.sysParamsList();
          });
        })
        .catch(() => {
          this.$commonUtil.message.info("已取消删除")
        });
    },
    handleCurrentChange: function(val) {
      this.currentPage = val;
      this.sysParamsList();
    },
    submitForm: function() {
      this.$refs.form.validate((valid) => {
        if(!valid) {
          console.log("校验失败")
        } else {
          if (this.isEditForm) {
            editSysParams(this.form).then(response => {
              if (response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                this.dialogFormVisible = false;
                this.sysParamsList();
              } else {
                this.$commonUtil.message.error(response.message)
              }
            });
          } else {
            addSysParams(this.form).then(response => {
              if (response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                this.dialogFormVisible = false;
                this.sysParamsList();
              } else {
                this.$commonUtil.message.error(response.message)
              }
            });
          }
        }
      })
    }
  }
};
</script>
