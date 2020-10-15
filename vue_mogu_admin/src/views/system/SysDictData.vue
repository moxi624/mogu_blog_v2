<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;" v-permission="'/sysDictData/getList'">


      <el-select
        v-model="dictTypeUid"
        placeholder="请选择字典名称"
        style="width:150px"
      >
        <el-option
          v-for="item in dictTypeList"
          :key="item.uid"
          :label="item.dictName"
          :value="item.uid"
        ></el-option>
      </el-select>

      <el-input
        clearable
        @keyup.enter.native="handleFind"
        class="filter-item"
        style="width: 200px;"
        v-model="dictLabelQuery"
        placeholder="请输入字典标签"
      ></el-input>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/sysDictData/getList'" >查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/sysDictData/add'">添加</el-button>
      <el-button class="filter-item" type="danger" @click="handleDeleteBatch" icon="el-icon-delete" v-permission="'/sysDictData/deleteBatch'">删除选中</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>

      <el-table-column label="序号" width="60" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="字典标签" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.dictLabel }}</span>
        </template>
      </el-table-column>

      <el-table-column label="字典键值" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.$index%5 == 0" type="warning">{{scope.row.dictValue}}</el-tag>
          <el-tag v-if="scope.$index%5 == 1" type="success">{{scope.row.dictValue}}</el-tag>
          <el-tag v-if="scope.$index%5 == 2" type="info">{{scope.row.dictValue}}</el-tag>
          <el-tag v-if="scope.$index%5 == 3" type="danger">{{scope.row.dictValue}}</el-tag>
          <el-tag v-if="scope.$index%5 == 4" >{{scope.row.dictValue}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="字典类型" width="200" align="center">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.sysDictType.dictType}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="是否默认" width="100" align="center">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.isDefault==1">是</el-tag>
          <el-tag type="warning" v-if="scope.row.isDefault==0">否</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="回显样式" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.listClass == 'default'">{{scope.row.listClass}}</el-tag>
          <el-tag v-if="scope.row.listClass == 'success'" type="success">{{scope.row.listClass}}</el-tag>
          <el-tag v-if="scope.row.listClass == 'primary'" type="primary">{{scope.row.listClass}}</el-tag>
          <el-tag v-if="scope.row.listClass == 'info'" type="info">{{scope.row.listClass}}</el-tag>
          <el-tag v-if="scope.row.listClass == 'warning'" type="warning">{{scope.row.listClass}}</el-tag>
          <el-tag v-if="scope.row.listClass == 'danger'" type="danger">{{scope.row.listClass}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="排序" width="50" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.sort }}</span>
        </template>
      </el-table-column>

      <el-table-column label="发布状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.isPublish == '1' ">上架</el-tag>
          <el-tag type="danger" v-else>下架</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="备注" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.remark }}</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
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
          <el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/sysDictData/edit'">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/sysDictData/deleteBatch'">删除</el-button>
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

        <el-form-item label="字典标签" :label-width="formLabelWidth" prop="dictLabel">
          <el-input v-model="form.dictLabel" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="字典键值" :label-width="formLabelWidth" prop="dictValue">
          <el-input v-model="form.dictValue" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="字典类型" :label-width="formLabelWidth" prop="dictTypeUid">
          <el-select
            disabled
            v-model="form.dictTypeUid"
            placeholder="请选择字典类型"
            style="width:200px"
          >
            <el-option
              v-for="item in dictTypeList"
              :key="item.uid"
              :label="item.dictType"
              :value="item.uid"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="回显样式" :label-width="formLabelWidth">
          <el-select v-model="form.listClass" clearable placeholder="推荐等级" style="width:140px">
            <el-option
              v-for="item in listClassType"
              :key="item.key"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="扩展样式" :label-width="formLabelWidth">
          <el-input v-model="form.cssClass" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="排序" :label-width="formLabelWidth" prop="sort">
          <el-input v-model="form.sort" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="备注" :label-width="formLabelWidth">
          <el-input v-model="form.remark" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="系统默认" :label-width="formLabelWidth" prop="isDefault">
          <el-radio-group v-model="form.isDefault" size="small">
            <el-radio :label="1" border>是</el-radio>
            <el-radio :label="0" border>否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="发布状态" :label-width="formLabelWidth" prop="isPublish">
          <el-radio-group v-model="form.isPublish" size="small">
            <el-radio label="1" border>上架</el-radio>
            <el-radio label="0" border>下架</el-radio>
          </el-radio-group>
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
  import {addSysDictData, deleteBatchSysDictData, editSysDictData, getSysDictDataList} from "@/api/sysDictData"
  import {getSysDictTypeList} from "@/api/sysDictType"

  export default {
    data() {
      return {
        dictTypeUid: null, // 从SysDictType传递过来的
        dictTypeMap: {}, // DictType对象集合
        dictLabelQuery: "", // 字典标签查询
        multipleSelection: [], //多选，用于批量删除
        dictTypeList: [],
        tableData: [],
        keyword: "",
        currentPage: 1,
        pageSize: 10,
        total: 0, //总数量
        title: "增加字典数据",
        dialogFormVisible: false, //控制弹出框
        formLabelWidth: "120px",
        isEditForm: false,
        form: {},
        listClassType: [
          {key: 1, label: "default", value: 'default'},
          {key: 2, label: "primary", value: 'primary'},
          {key: 3, label: "success", value: 'success'},
          {key: 4, label: "info", value: 'info'},
          {key: 5, label: "warning", value: 'warning'},
          {key: 6, label: "danger", value: 'danger'}
        ],
        rules: {
          dictLabel: [
            {required: true, message: '字典标签不能为空', trigger: 'blur'},
            {min: 1, max: 20, message: '长度在1到20个字符'},
          ],
          dictValue: [
            {required: true, message: '字典键值不能为空', trigger: 'blur'},
            {min: 1, max: 20, message: '长度在1到20个字符'},
          ],
          dictTypeUid: [
            {required: true, message: '字典类型不能为空', trigger: 'blur'},
          ],
          isDefault: [
            {required: true, message: '系统默认不能为空', trigger: 'blur'}
          ],
          isPublish: [
            {required: true, message: '发布状态不能为空', trigger: 'blur'}
          ],
          sort: [
            {required: true, message: '排序字段不能为空', trigger: 'blur'},
            {pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数'},
          ]
        }
      };
    },
    created() {

      //传递过来的dictTypeUid
      this.dictTypeUid = this.$route.query.dictTypeUid;
      this.sysDictTypeList();
      this.sysDictDataList();
    },
    methods: {
      sysDictDataList: function () {
        var params = {};
        params.dictTypeUid = this.dictTypeUid;
        params.dictLabel = this.dictLabelQuery;
        params.currentPage = this.currentPage;
        params.pageSize = this.pageSize;
        getSysDictDataList(params).then(response => {
          if (response.code == this.$ECode.SUCCESS) {
            this.tableData = response.data.records;
            this.currentPage = response.data.current;
            this.pageSize = response.data.size;
            this.total = response.data.total;
          }
        });
      },
      sysDictTypeList: function () {
        var params = {};
        params.keyword = '';
        params.currentPage = 0;
        params.pageSize = 100;
        getSysDictTypeList(params).then(response => {
          if (response.code == this.$ECode.SUCCESS) {
            this.dictTypeList = response.data.records;
          }
        });
      },
      // 这里可以设置一些初始值
      getFormObject: function () {
        var formObject = {
          isPublish: '1',
          sort: 0,
          isDefault: 0,
          listClass: ''
        };
        return formObject;
      },
      handleFind: function () {
        this.sysDictDataList();
      },
      handleAdd: function () {
        this.title = "增加数据字典"
        this.dialogFormVisible = true;
        this.form = this.getFormObject();
        this.form.dictTypeUid = this.dictTypeUid;
        this.isEditForm = false;
      },
      handleEdit: function (row) {
        title: "编辑字典数据";
        this.dialogFormVisible = true;
        this.isEditForm = true;
        this.form = row;
        this.form.dictTypeUid = this.dictTypeUid;
      },
      handleDelete: function (row) {
        var that = this;
        this.$confirm("此操作将把字典数据删除, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            let sysDictData = {};
            sysDictData.uid = row.uid;
            let deleteList = []
            deleteList.push(sysDictData)
            deleteBatchSysDictData(deleteList).then(response => {
              if(response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                that.sysDictDataList();
              } else {
                this.$commonUtil.message.error(response.message)
              }
            });
          })
          .catch(() => {
            this.$commonUtil.message.info("已取消删除")
          });
      },
      handleDeleteBatch: function (row) {
        var that = this;
        if (that.multipleSelection.length <= 0) {
          this.$commonUtil.message.error("请先选中需要删除的内容")
          return;
        }
        this.$confirm("此操作将把选中字典数据删除, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            deleteBatchSysDictData(that.multipleSelection).then(response => {
              if(response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                that.sysDictDataList();
              } else {
                this.$commonUtil.message.error(response.message)
              }
            });
          })
          .catch(() => {
            this.$commonUtil.message.info("已取消删除")
          });
      },
      handleCurrentChange: function (val) {
        this.currentPage = val;
        this.sysDictDataList();
      },
      // 改变多选
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      submitForm: function () {
        this.$refs.form.validate((valid) => {
          if(!valid) {
            console.log("校验出错")
          } else {
            if (this.isEditForm) {
              editSysDictData(this.form).then(response => {
                console.log(response);
                if (response.code == this.$ECode.SUCCESS) {
                  this.$commonUtil.message.success(response.message)
                  this.dialogFormVisible = false;
                  this.sysDictDataList();
                } else {
                  this.$commonUtil.message.error(response.message)
                }
              });
            } else {
              addSysDictData(this.form).then(response => {
                if (response.code == this.$ECode.SUCCESS) {
                  this.$commonUtil.message.success(response.message)
                  this.dialogFormVisible = false;
                  this.sysDictDataList();
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
