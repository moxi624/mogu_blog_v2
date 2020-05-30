<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/categoryMenu/add'">添加菜单</el-button>
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

              <el-table-column label width="200" align="center">
                <template slot-scope="scope_child">
                  <span>{{ scope_child.row.summary }}</span>
                </template>
              </el-table-column>

              <el-table-column label width="100" align="center">
                <template slot-scope="scope_child">
<!--                  <span>{{ scope_child.row.icon }}</span>-->
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
                  <el-button @click="handleEdit(scope_child.row)" type="primary" size="small" v-permission="'/categoryMenu/edit'">编辑</el-button>
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

      <el-table-column label="菜单简介" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.summary }}</span>
        </template>
      </el-table-column>

      <el-table-column label="图标" width="100" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon" />
<!--          <span>{{ scope.row.icon }}</span>-->
        </template>
      </el-table-column>

      <el-table-column label="路由" width="200" align="center">
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
          <el-button @click="handleStick(scope.row)" type="warning" size="small" v-permission="'/categoryMenu/stick'">置顶</el-button>
          <el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/categoryMenu/edit'">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/categoryMenu/delete'">删除</el-button>
        </template>
      </el-table-column>
    </el-table>


    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="菜单名称" :label-width="formLabelWidth" prop="name">
          <el-input v-model="form.name" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="菜单等级" :label-width="formLabelWidth" prop="menuLevel">
          <el-select v-model="form.menuLevel" placeholder="请选择">
            <el-option
              v-for="item in menuLevelDictList"
              :key="item.uid"
              :label="item.dictLabel"
              v-if="item.dictValue != 3"
              :value="parseInt(item.dictValue)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          v-if="form.menuLevel == 2"
          label="父菜单名"
          :label-width="formLabelWidth"
          prop="parentUid"
        >
          <el-select
            v-model="form.parentUid"
            filterable
            clearable
            remote
            reserve-keyword
            placeholder="请输入父菜单名"
            :remote-method="remoteMethod"
            :loading="loading"
          >
            <el-option
              v-for="item in menuOptions"
              :key="item.uid"
              :label="item.name"
              :value="item.uid"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="菜单介绍" :label-width="formLabelWidth" prop="summary">
          <el-input v-model="form.summary" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="图标" :label-width="formLabelWidth" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入前图标名称">
            <el-button slot="append" icon="el-icon-setting" @click="openIconsDialog('prefix-icon')">
              选择
            </el-button>
          </el-input>
        </el-form-item>

        <el-form-item label="路由" :label-width="formLabelWidth" prop="url">
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

    <icons-dialog :visible.sync="iconsVisible" :current="form.icon" @select="setIcon" />
  </div>
</template>

<script>
import {
  getMenuList,
  getAllMenu,
  addMenu,
  editMenu,
  deleteMenu,
  stickMenu
} from "@/api/categoryMenu";
import {getListByDictTypeList} from "@/api/sysDictData"
import { formatData } from "@/utils/webUtils";
import IconsDialog from "../../components/IconsDialog";
export default {
  components: {
    IconsDialog
  },
  data() {
    return {
      iconsVisible: false, // 是否显示icon选择器
      activeData: '', // 激活的图标
      showHeader: false, //是否显示表头
      tableData: [],
      keyword: "",
      menuLevel: "",
      currentPage: 1,
      pageSize: 10,
      total: 0, //总数量
      title: "增加菜单",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: "120px",
      isEditForm: false,
      menuLevelDictList: [], //菜单等级字典
      yesNoDictList: [], // 是否字典
      yesNoDefault: null,
      form: {
        uid: null,
        name: "",
        summary: "",
        icon: "",
        url: "",
        sort: ""
      },
      loading: false,
      menuOptions: [], //一级菜单候选项
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
          {required: true, message: '路由不能为空', trigger: 'blur'}
        ],
        isShow: [
          {required: true, message: '显示字段不能为空', trigger: 'blur'}
        ]
      }
    };
  },
  created() {
    this.getDictList();
    this.menuList();
  },
  methods: {
    menuList: function() {
      getAllMenu().then(response => {
        console.log("getAllMenu", response);
        if (response.code == "success") {
          this.tableData = response.data;
          this.menuOptions = response.data;
        }
      });
    },
    // 选择图标
    setIcon(val) {
      this.form.icon = val
    },
    openIconsDialog(model) {
      this.iconsVisible = true
      this.currentIconModel = model
    },
    /**
     * 字典查询
     */
    getDictList: function () {
      var dictTypeList =  ['sys_menu_level', 'sys_yes_no']
      getListByDictTypeList(dictTypeList).then(response => {
        if (response.code == "success") {
          var dictMap = response.data;
          this.menuLevelDictList = dictMap.sys_menu_level.list
          this.yesNoDictList = dictMap.sys_yes_no.list
          if(dictMap.sys_yes_no.defaultValue) {
            this.yesNoDefault = parseInt(dictMap.sys_yes_no.defaultValue);
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
        menuType: 0, //菜单类型  菜单
        isShow: this.yesNoDefault
      };
      return formObject;
    },
    handleFind: function() {
      this.menuList();
    },
    handleAdd: function() {
      this.title = "增加菜单"
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    handleEdit: function(row) {
      this.dialogFormVisible = true;
      this.isEditForm = true;
      var parentUid = row.parentUid;
      this.title = "编辑菜单"
      this.form = row;
    },
    handleStick: function(row) {
      this.$confirm("此操作将会把该标签放到首位, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {}
          params.uid = row.uid
          stickMenu(params).then(response => {
            if (response.code == "success") {
              this.menuList();
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
      this.$confirm("此操作将把菜单删除, 是否继续?", "提示", {
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
            that.menuList();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    //菜单远程搜索函数
    remoteMethod: function(query) {
      if (query !== "") {
        //这里只搜索一级菜单出来
        var params = new URLSearchParams();
        params.append("keyword", query);
        params.append("menuLevel", 1);
        params.append("pageSize", 100);
        getMenuList(params).then(response => {
          console.log(response);
          if (response.code == "success") {
            this.menuOptions = response.data.data.records;
          }
        });
      } else {
        this.menuOptions = [];
      }
    },

    submitForm: function() {
      this.$refs.form.validate((valid) => {
        if(!valid) {
          console.log("校验失败")
        } else {
          if (this.isEditForm) {
            editMenu(this.form).then(response => {
              console.log(response);
              if (response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                });
                this.dialogFormVisible = false;
                this.menuList();
              } else {
                this.$message({
                  type: "success",
                  message: response.data
                });
              }
            });
          } else {
            addMenu(this.form).then(response => {
              console.log(response);
              if (response.code == "success") {
                this.$message({
                  type: "success",
                  message: response.data
                });
                this.dialogFormVisible = false;
                this.menuList();
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
