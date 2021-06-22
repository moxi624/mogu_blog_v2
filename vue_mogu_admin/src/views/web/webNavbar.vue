<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/categoryMenu/add'">添加导航栏</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%">

      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-form label-position="left" inline class="demo-table-expand">

            <el-table :data="scope.row.childWebNavbar" :show-header="showHeader" style="width: 100%">
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
                  <el-tag v-for="item in menuLevelDictList" :key="item.uid" v-if="scope_child.row.navbarLevel  == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
                </template>
              </el-table-column>

              <el-table-column label width="200" align="center">
                <template slot-scope="scope_child">
                  <span>{{ scope_child.row.summary }}</span>
                </template>
              </el-table-column>

              <el-table-column label width="50" align="center">
                <template slot-scope="scope_child">
                  <i :class="scope_child.row.icon" />
                </template>
              </el-table-column>

              <el-table-column label width="250" align="center">
                <template slot-scope="scope_child">
                  <span>{{ scope_child.row.url }}</span>
                </template>
              </el-table-column>

              <el-table-column width="100" align="center">
                <template slot-scope="scope">
                  <el-tag v-for="item in yesNoDictList" :key="item.uid" v-if="scope.row.isShow == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
                </template>
              </el-table-column>

              <el-table-column width="120" align="center">
                <template slot-scope="scope">
                  <el-tag v-for="item in jumpExternalDictList" :key="item.uid" v-if="scope.row.isJumpExternalUrl == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
                </template>
              </el-table-column>

              <el-table-column width="100" align="center">
                <template slot-scope="scope">
                  <el-tag type="warning">{{ scope.row.sort }}</el-tag>
                </template>
              </el-table-column>

              <el-table-column fixed="right" min-width="230">
                <template slot-scope="scope_child">
                  <el-button @click="handleEdit(scope_child.row)" type="primary" size="small" v-permission="'/webNavbar/edit'">编辑</el-button>
                  <el-button @click="handleDelete(scope_child.row)" type="danger" size="small" v-permission="'/webNavbar/delete'">删除</el-button>
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

      <el-table-column label="导航栏名称" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="导航栏级别" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item in menuLevelDictList" :key="item.uid" v-if="scope.row.navbarLevel  == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="导航栏简介" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.summary }}</span>
        </template>
      </el-table-column>

      <el-table-column label="图标" width="50" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon" />
        </template>
      </el-table-column>

      <el-table-column label="路由" width="250" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.url }}</span>
        </template>
      </el-table-column>

      <el-table-column label="是否显示" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item in yesNoDictList" :key="item.uid" v-if="scope.row.isShow == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="是否跳转外链" width="120" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item in jumpExternalDictList" :key="item.uid" v-if="scope.row.isJumpExternalUrl == item.dictValue" :type="item.listClass">{{item.dictLabel}}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="排序" width="100" align="center">
        <template slot-scope="scope">
          <el-tag type="warning">{{ scope.row.sort }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" min-width="230">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="primary" size="small" v-permission="'/webNavbar/edit'">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small" v-permission="'/webNavbar/delete'">删除</el-button>
        </template>
      </el-table-column>
    </el-table>


    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="导航栏名称" :label-width="formLabelWidth" prop="name">
          <el-input v-model="form.name" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="导航栏等级" :label-width="formLabelWidth" prop="menuLevel">
          <el-select v-model="form.navbarLevel" placeholder="请选择">
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
          v-if="form.navbarLevel == 2"
          label="父导航栏名"
          :label-width="formLabelWidth"
          prop="parentUid"
        >
          <el-select
            v-model="form.parentUid"
            filterable
            clearable
            remote
            reserve-keyword
            placeholder="请输入父导航栏名"
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

        <el-form-item label="导航栏介绍" :label-width="formLabelWidth" prop="summary">
          <el-input v-model="form.summary" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="图标" :label-width="formLabelWidth" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入前图标名称">
            <el-button slot="append" icon="el-icon-setting" @click="openIconsDialog('prefix-icon')">
              选择
            </el-button>
          </el-input>
        </el-form-item>

        <el-form-item label="是否跳转外链" :label-width="formLabelWidth" prop="isShow">
          <el-radio-group v-model="form.isJumpExternalUrl" size="small">
            <el-radio v-for="item in jumpExternalDictList" :key="item.uid" :label="parseInt(item.dictValue)" border>{{item.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="路由" :label-width="formLabelWidth" prop="url">
          <el-input v-model="form.url" placeholder="跳转外链时，路由为外部URL" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="是否显示" :label-width="formLabelWidth" prop="isShow">
          <el-radio-group v-model="form.isShow" size="small">
            <el-radio v-for="item in yesNoDictList" :key="item.uid" :label="parseInt(item.dictValue)" border>{{item.dictLabel}}</el-radio>
          </el-radio-group>
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

    <icons-dialog :visible.sync="iconsVisible" :current="form.icon" @select="setIcon" />
  </div>
</template>

<script>
import {
  getWebNavbarList,
  getWebNavbarAllList,
  addWebNavbar,
  editWebNavbar,
  deleteWebNavbar
} from "@/api/webNavbar";
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
      title: "增加导航栏",
      dialogFormVisible: false, //控制弹出框
      formLabelWidth: "120px",
      isEditForm: false,
      menuLevelDictList: [], //导航栏等级字典
      yesNoDictList: [], // 是否字典
      jumpExternalDictList: [], // 是否字典
      yesNoDefault: null,
      jumpExternalDefault: null,
      form: {
        uid: null,
        name: "",
        summary: "",
        icon: "",
        url: "",
        sort: ""
      },
      loading: false,
      menuOptions: [], //一级导航栏候选项
      rules: {
        name: [
          {required: true, message: '导航栏名称不能为空', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在1到20个字符'},
        ],
        navbarLevel: [
          {required: true, message: '导航栏等级不能为空', trigger: 'blur'}
        ],
        parentUid: [
          {required: true, message: '父导航栏名不能为空', trigger: 'blur'}
        ],
        summary: [
          {required: true, message: '导航栏简介不能为空', trigger: 'blur'}
        ],
        icon: [
          {required: true, message: '图标不能为空', trigger: 'blur'}
        ],
        url: [
          {required: true, message: '路由不能为空', trigger: 'blur'}
        ],
        isShow: [
          {required: true, message: '显示字段不能为空', trigger: 'blur'}
        ],
        sort: [
          {required: true, message: '排序字段不能为空', trigger: 'blur'},
          {pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数'},
        ],
      }
    };
  },
  created() {
    this.getDictList();
    this.WebNavbarAllList();
  },
  methods: {
    WebNavbarAllList: function() {
      console.log("获取全部")
      getWebNavbarAllList().then(response => {
        if (response.code == this.$ECode.SUCCESS) {
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
      var dictTypeList =  ['sys_menu_level', 'sys_yes_no', 'sys_jump_external']
      getListByDictTypeList(dictTypeList).then(response => {
        if (response.code == this.$ECode.SUCCESS) {
          var dictMap = response.data;
          this.menuLevelDictList = dictMap.sys_menu_level.list
          this.yesNoDictList = dictMap.sys_yes_no.list
          this.jumpExternalDictList = dictMap.sys_jump_external.list
          if(dictMap.sys_yes_no.defaultValue) {
            this.yesNoDefault = parseInt(dictMap.sys_yes_no.defaultValue);
          }
          if(dictMap.sys_jump_external.defaultValue) {
            this.jumpExternalDefault = parseInt(dictMap.sys_jump_external.defaultValue);
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
        sort: 0,
        menuType: 0, //导航栏类型  导航栏
        isShow: this.yesNoDefault,
        isJumpExternalUrl: this.jumpExternalDefault
      };
      return formObject;
    },
    handleFind: function() {
      this.currentPage = 1
      this.WebNavbarList();
    },
    handleAdd: function() {
      this.title = "增加导航栏"
      this.dialogFormVisible = true;
      this.form = this.getFormObject();
      this.isEditForm = false;
    },
    handleEdit: function(row) {
      this.dialogFormVisible = true;
      this.isEditForm = true;
      this.title = "编辑导航栏"
      this.form = row;
    },
    handleDelete: function(row) {
      this.$confirm("此操作将把导航栏删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {}
          params.uid = row.uid
          deleteWebNavbar(params).then(response => {
            if(response.code == this.$ECode.SUCCESS) {
              this.$commonUtil.message.success(response.message)
            } else {
              this.$commonUtil.message.error(response.message)
            }
            this.WebNavbarAllList();
          });
        })
        .catch(() => {
          this.$commonUtil.message.info("已取消删除")
        });
    },
    //导航栏远程搜索函数
    remoteMethod: function(query) {
      if (query !== "") {
        //这里只搜索一级导航栏出来
        var params = new URLSearchParams();
        params.append("keyword", query);
        params.append("navbarLevel", 1);
        params.append("pageSize", 100);
        getMenuList(params).then(response => {
          console.log(response);
          if (response.code == this.$ECode.SUCCESS) {
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
            editWebNavbar(this.form).then(response => {
              if (response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                this.dialogFormVisible = false;
              } else {
                this.$commonUtil.message.error(response.message)
              }
            });
          } else {
            addWebNavbar(this.form).then(response => {
              if (response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                this.dialogFormVisible = false;
                this.WebNavbarAllList();
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
