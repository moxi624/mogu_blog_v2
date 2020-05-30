<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">

      <el-cascader
        :options="options"
        placeholder="请选择菜单名"
        v-model="keyword"
        :props="{ checkStrictly: true }"
        clearable></el-cascader>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFind" v-permission="'/categoryMenu/getList'">查找</el-button>
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/categoryMenu/add'">添加按钮</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%">

      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-table :data="scope.row.childCategoryMenu" :show-header="showHeader" style="width: 100%">

              <el-table-column label width="60" align="center">
                <template slot-scope="scope_child">
                  <span>{{scope_child.$index + 1}}</span>
                </template>
              </el-table-column>

              <el-table-column label width="150" align="center">
                <template slot-scope="scope_child">
                  <span>{{ scope_child.row.name }}</span>
                </template>
              </el-table-column>

              <el-table-column label width="100" align="center">
                <template slot-scope="scope_child">
                  <el-tag v-for="item in menuLevelDictList" :key="item.uid" v-if="scope_child.row.menuLevel == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
                </template>
              </el-table-column>

              <el-table-column label width="100" align="center">
                <template slot-scope="scope">
                  <el-tag v-for="item in menuTypeDictList" :key="item.uid" v-if="scope.row.menuType == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
                </template>
              </el-table-column>

              <el-table-column label width="250" align="center">
                <template slot-scope="scope_child">
                  <span>{{ scope_child.row.summary }}</span>
                </template>
              </el-table-column>

              <el-table-column label width="100" align="center">
                <template slot-scope="scope_child">
                  <span>{{ scope_child.row.icon }}</span>
                  <i :class="scope_child.row.icon" />
                </template>
              </el-table-column>

              <el-table-column label width="200" align="center">
                <template slot-scope="scope_child">
                  <span>{{ scope_child.row.url }}</span>
                </template>
              </el-table-column>

              <el-table-column width="100" align="center">
                <template slot-scope="scope">
                  <el-tag v-for="item in yesNoDictList" :key="item.uid" v-if="scope.row.isShow == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
                </template>
              </el-table-column>

              <el-table-column label width="160" align="center">
                <template slot-scope="scope_child">
                  <span>{{ scope_child.row.createTime }}</span>
                </template>
              </el-table-column>

              <el-table-column label width="100" align="center">
                <template slot-scope="scope_child">
                  <template v-if="scope_child.row.status == 1">
                    <span>正常</span>
                  </template>
                  <template v-if="scope_child.row.status == 2">
                    <span>推荐</span>
                  </template>
                  <template v-if="scope_child.row.status == 0">
                    <span>已删除</span>
                  </template>
                </template>
              </el-table-column>

              <el-table-column fixed="right" min-width="230">
                <template slot-scope="scope_child">
                  <el-button @click="handleStick(scope_child.row)" type="warning" size="small" v-permission="'/categoryMenu/stick'">置顶</el-button>
                  <el-button @click="handleEdit(scope.row, scope_child.row)" type="primary" size="small" v-permission="'/categoryMenu/edit'">编辑</el-button>
                  <el-button @click="handleDelete(scope_child.row)" type="danger" size="small" v-permission="'/categoryMenu/delete'">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form>
        </template>
      </el-table-column>

      <el-table-column label="序号" width="60" align="center">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>

      <el-table-column label="菜单名称" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="菜单级别" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item in menuLevelDictList" :key="item.uid" v-if="scope.row.menuLevel == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="菜单类型" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item in menuTypeDictList" :key="item.uid" v-if="scope.row.menuType == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="菜单简介" width="250" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.summary }}</span>
        </template>
      </el-table-column>

      <el-table-column label="图标" width="100" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon"></i>
<!--          <span>{{ scope.row.icon }}</span>-->
        </template>
      </el-table-column>

      <el-table-column label="URL" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.url }}</span>
        </template>
      </el-table-column>

      <el-table-column label="是否显示" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item in yesNoDictList" :key="item.uid" v-if="scope.row.isShow == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
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

      <el-table-column label="操作" fixed="right" min-width="230">
        <template slot-scope="scope">

        </template>
      </el-table-column>
    </el-table>


    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="按钮名称" :label-width="formLabelWidth" prop="name">
          <el-input v-model="form.name" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="菜单类型" :label-width="formLabelWidth" prop="menuType">
          <el-radio-group v-model="form.menuType" size="small" disabled>
            <el-radio v-for="item in menuTypeDictList" :key="item.uid" :label="parseInt(item.dictValue)" border>{{item.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="菜单等级" :label-width="formLabelWidth" prop="menuLevel">
          <el-select v-model="form.menuLevel" placeholder="请选择" disabled>
            <el-option
              v-for="item in menuLevelDictList"
              :key="item.uid"
              :label="item.dictLabel"
              :value="parseInt(item.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>

        <!-- 用于按钮 -->
        <el-form-item
          v-if="form.menuType == 1"
          label="父菜单名"
          :label-width="formLabelWidth"
        >
          <el-cascader
            :options="options"
            placeholder="请选择父菜单"
            v-model="buttonParentUid"
            :props="{ checkStrictly: true }"
            clearable></el-cascader>
        </el-form-item>

        <el-form-item label="按钮介绍" :label-width="formLabelWidth" prop="summary">
          <el-input v-model="form.summary" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="图标" :label-width="formLabelWidth" prop="icon" v-if="form.menuType == 0">
          <el-input v-model="form.icon" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="URL" :label-width="formLabelWidth" prop="url">
          <el-input v-model="form.url" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="是否显示" :label-width="formLabelWidth" prop="isShow">
          <el-radio-group v-model="form.isShow" size="small">
            <el-radio v-for="item in yesNoDictList" :key="item.uid" :label="parseInt(item.dictValue)" border>{{item.dictLabel}}</el-radio>
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
import {
  getAllMenu,
  getButtonAll,
  addMenu,
  editMenu,
  deleteMenu,
  stickMenu
} from "@/api/categoryMenu";
import {getListByDictTypeList} from "@/api/sysDictData"
import { formatData } from "@/utils/webUtils";
export default {
  data() {
    return {
      showHeader: false, //是否显示表头
      tableData: [],
      keyword: [],
      menuLevel: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加按钮",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: "120px",
      isEditForm: false,
      menuLevelDictList: [], //菜单等级字典
      yesNoDictList: [], // 是否字典
      menuTypeDictList: [], //菜单类型字典
      yesNoDefault: null, // 是否 默认值
      menuTypeDefault: null, // 菜单类型默认值
      buttonParentUid: [], // 选中的button父UID
      form: {
        uid: null,
        name: "",
        summary: "",
        icon: "",
        url: "",
        sort: ""
      },
      loading: false,
      rules: {
        name: [
          {required: true, message: '菜单名称不能为空', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在1到20个字符'},
        ],
        menuLevel: [
          {required: true, message: '菜单等级不能为空', trigger: 'blur'}
        ],
        parentUid: [
          {required: true, message: '父菜单名不能为空', trigger: 'blur'}
        ],
        summary: [
          {required: true, message: '菜单简介不能为空', trigger: 'blur'}
        ],
        icon: [
          {required: true, message: '图标不能为空', trigger: 'blur'}
        ],
        url: [
          {required: true, message: 'URL不能为空', trigger: 'blur'}
        ],
        isShow: [
          {required: true, message: '显示字段不能为空', trigger: 'blur'}
        ]
      },
      options: []
    };
  },
  created() {
    // 得到菜单列表
    this.menuList()
    this.getDictList();
    this.buttonList();
  },
  methods: {
    menuList: function() {
      getAllMenu().then(response => {
        if (response.code == "success") {
          let tableData = response.data;
          let options = []
          for(let a=0; a<tableData.length; a++) {
            let parent = {}
            parent.label = tableData[a].name;
            parent.value = tableData[a].uid;
            let childData = tableData[a].childCategoryMenu
            let childList = []
            for(let b=0; b<childData.length; b++) {
              let child = {}
              child.label = childData[b].name;
              child.value = childData[b].uid;
              childList.push(child)
            }
            parent.children = childList
            options.push(parent)
          }
          this.options = options
        }
      });
    },
    buttonList: function() {
      var params = {};
      var keyword = this.keyword
      if(keyword.length > 0) {
        // 选取最后一个元素
        params.keyword = keyword[keyword.length - 1]
      }
      getButtonAll(params).then(response => {
        console.log("getAllMenu", response);
        if (response.code == "success") {
          this.tableData = response.data;
        }
      });
    },
    /**
     * 字典查询
     */
    getDictList: function () {
      var dictTypeList =  ['sys_menu_level', 'sys_yes_no', 'sys_menu_type']
      getListByDictTypeList(dictTypeList).then(response => {
        if (response.code == "success") {

          var dictMap = response.data;

          this.menuLevelDictList = dictMap.sys_menu_level.list

          this.yesNoDictList = dictMap.sys_yes_no.list

          this.menuTypeDictList = dictMap.sys_menu_type.list

          if(dictMap.sys_yes_no.defaultValue) {
            this.yesNoDefault = parseInt(dictMap.sys_yes_no.defaultValue);
          }

          if(dictMap.sys_menu_type.defaultValue) {
            this.menuTypeDefault = parseInt(dictMap.sys_menu_type.defaultValue);
          }

        }
      });
    },
    getFormObject: function() {
      var formObject = {
        uid: null,
        name: "",
        summary: "",
        icon: "",
        url: "",
        sort: "",
        menuLevel: 3,
        isShow: this.yesNoDefault,
        menuType: 1
      };
      return formObject;
    },
    handleFind: function() {
      this.buttonList();
    },
    handleAdd: function() {
      this.title = "增加按钮"
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    handleEdit: function(parentRow, row) {
      this.dialogFormVisible = true;
      this.isEditForm = true;
      // 设置级联的父菜单名
      var parentUid = []
      parentUid.push(parentRow.parentUid)
      parentUid.push(parentRow.uid)
      this.buttonParentUid = parentUid
      this.title = "编辑按钮"
      this.form = row;
    },
    handleStick: function(row) {
      this.$confirm("此操作将会把该按钮放到首位, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {}
          params.uid = row.uid
          stickMenu(params).then(response => {
            if (response.code == "success") {
              this.buttonList();
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
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消置顶"
          });
        });
    },
    handleDelete: function(row) {
      var that = this;
      this.$confirm("此操作将把按钮删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {}
          params.uid = row.uid
          deleteMenu(params).then(response => {
            if(response.code == "success") {
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
            that.buttonList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },

    submitForm: function() {
      this.$refs.form.validate((valid) => {
        if(!valid) {
          console.log("校验失败")
        } else {

          // 如果菜单类型是 按钮，那么设置菜单等级为 3
          let menuType = this.form.menuType
          if(menuType == 1) {
            this.form.menuLevel = 3
          }

          let buttonParentUid = this.buttonParentUid
          if(buttonParentUid.length > 0) {
            // 选取最后一个元素
            this.form.parentUid = buttonParentUid[buttonParentUid.length - 1]
          } else {
            this.$message({
              type: "error",
              message: "请选中父菜单"
            })
            return;
          }

          if (this.isEditForm) {
            editMenu(this.form).then(response => {
              console.log(response);
              if (response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                });
                this.dialogFormVisible = false;
                this.buttonList();
              } else {
                this.$message({
                  type: "success",
                  message: response.data
                });
              }
            });
          } else {
            addMenu(this.form).then(response => {
              if (response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                });
                this.dialogFormVisible = false;
                this.buttonList();
              } else {
                this.$message({
                  type: "error",
                  message: response.data
                });
              }
            });
          }
        }
      })

    }
  }
};
</script>
<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
