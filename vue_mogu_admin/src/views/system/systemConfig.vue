<template>
  <div class="app-container">
    <el-tabs type="border-card" @tab-click="handleClick" v-model="activeName">
      <el-tab-pane name="one" v-permission="'/systemConfig/getSystemConfig'">
        <span slot="label"><i class="el-icon-edit"></i> 系统配置</span>
        <el-form style="margin-left: 20px;" label-position="left"   label-width="140px" >

          <aside>
            通过开关选择博客编辑时的文本编辑器，以及文件显示方式<br/>
          </aside>

          <el-form-item label="封面图片显示优先级">
            <el-radio v-for="item in picturePriorityDictList" :key="item.uid" v-model="form.picturePriority" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item label="详情图片显示优先级">
            <el-radio v-for="item in picturePriorityDictList" :key="item.uid" v-model="form.contentPicturePriority" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item label="文本编辑器">
            <el-radio v-for="item in editorModalDictList" :key="item.uid" v-model="form.editorModel" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <!--当有新的反馈，友链申请时进行通知，首先需要在系统管理处设置接收通知的邮箱 -->
          <el-form-item label="网站消息邮件通知">
            <el-radio v-for="item in openDictList" :key="item.uid" v-model="form.startEmailNotification" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <!-- 仪表盘弹框通知，在用户登录后台的时候会出现，可以手动关闭 -->
          <el-form-item label="仪表盘弹框通知">
            <el-radio v-for="item in openDictList" :key="item.uid" v-model="form.openDashboardNotification" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <!-- 用于控制用户是否需要通过邮箱验证，完成认证-->
          <el-form-item label="注册用户邮件激活">
            <el-radio v-for="item in openDictList" :key="item.uid" v-model="form.openEmailActivate" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <!-- 搜索模式-->
          <el-form-item>
            <template slot="label">
              文章搜索模式
              <el-popover
                placement="top-start"
                width="200"
                trigger="hover"
                content="用于控制门户搜索功能使用SQL方式，还是全文检索。开启全文检索需要启动mogu-search项目">
                <i slot="reference" style="cursor: pointer;margin-left: 2px" class="el-icon-question"></i>
              </el-popover>
            </template>
            <el-radio v-for="item in searchModelDictList" :key="item.uid" v-model="form.searchModel" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-permission="'/systemConfig/editSystemConfig'">保 存</el-button>
          </el-form-item>

        </el-form>
      </el-tab-pane>

      <el-tab-pane name="three" v-permission="'/systemConfig/getSystemConfig'">
        <span slot="label">
          <i class="el-icon-date"></i> 本地文件存储
        </span>
        <el-form
          style="margin-left: 20px;"
          label-position="left"
          :model="form"
          label-width="120px"
          :rules="rules"
          ref="form"
        >

          <aside>
            使用IO流将文件存储本地磁盘中<br/>
          </aside>

          <el-form-item label="本地文件域名" prop="localPictureBaseUrl">
            <el-input v-model="form.localPictureBaseUrl" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="文件上传本地">
            <el-radio v-for="item in yesNoDictList" :key="item.uid" v-model="form.uploadLocal" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-permission="'/systemConfig/editSystemConfig'">保 存</el-button>
          </el-form-item>

        </el-form>
      </el-tab-pane>

      <el-tab-pane name="four"  v-permission="'/systemConfig/getSystemConfig'">
        <span slot="label">
          <i class="el-icon-date"></i> 七牛云对象存储
        </span>

        <el-form
          style="margin-left: 20px;"
          label-position="left"
          :model="form"
          label-width="120px"
          :rules="rules"
          ref="form"
        >
          <aside>
            使用 <a href="https://www.moguit.cn/info/202">七牛云</a> 构建对象存储服务<br/>
          </aside>

          <el-form-item label="七牛云文件域名" prop="qiNiuPictureBaseUrl">
            <el-input v-model="form.qiNiuPictureBaseUrl" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="七牛云公钥">
            <el-input v-model="form.qiNiuAccessKey" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="七牛云私钥">
            <el-input type="password" v-model="form.qiNiuSecretKey" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="上传空间">
            <el-input  v-model="form.qiNiuBucket" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="存储区域">
            <el-select v-model="form.qiNiuArea" placeholder="请选择存储区域" clearable>
              <el-option v-for="item in areaDictList"
                         :key="item.dictValue"
                         :label="item.dictLabel"
                         :value="item.dictValue"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="文件上传七牛云">
            <el-radio v-for="item in yesNoDictList" :key="item.uid" v-model="form.uploadQiNiu" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-permission="'/systemConfig/editSystemConfig'">保 存</el-button>
          </el-form-item>

        </el-form>
      </el-tab-pane>

      <el-tab-pane name="five" v-permission="'/systemConfig/getSystemConfig'">
        <span slot="label">
          <i class="el-icon-date"></i> Minio对象存储
        </span>

        <el-form
          style="margin-left: 20px;"
          label-position="left"
          :model="form"
          label-width="120px"
          :rules="rules"
          ref="form"
        >

          <aside>
            使用 <a href="https://www.moguit.cn/info/278">Minio</a> 构建本地对象存储服务<br/>
          </aside>

          <el-form-item label="Minio访问域名" prop="localPictureBaseUrl">
            <el-input v-model="form.minioPictureBaseUrl" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="Minio连接地址" prop="qiNiuPictureBaseUrl">
            <el-input v-model="form.minioEndPoint" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="Minio公钥">
            <el-input v-model="form.minioAccessKey" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="Minio私钥">
            <el-input type="password" v-model="form.minioSecretKey" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="Minio上传空间">
            <el-input  v-model="form.minioBucket" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="文件上传Minio">
            <el-radio v-for="item in yesNoDictList" :key="item.uid" v-model="form.uploadMinio" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-permission="'/systemConfig/editSystemConfig'">保 存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>


      <el-tab-pane name="six" v-permission="'/systemConfig/getSystemConfig'">
        <span slot="label"><i class="el-icon-edit"></i> 邮箱配置</span>
        <el-form style="margin-left: 20px;" label-position="left"   label-width="80px" >

          <aside>
            邮箱配置主要用于配置网站消息的接收<br/>
            例如：友链申请、网站评论、网站反馈等，可以在系统配置处选择是否开启邮件通知<br/>
          </aside>

          <el-form-item label="邮箱" prop="email">
            <el-input  v-model="form.email" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="密码" prop="newPwd1">
            <el-input type="password" v-model="form.emailPassword" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="用户名" prop="newPwd2">
            <el-input  v-model="form.emailUserName" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="SMTP地址" prop="newPwd2">
            <el-input  v-model="form.smtpAddress" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="SMTP端口" prop="newPwd2">
            <el-input  v-model="form.smtpPort" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-permission="'/systemConfig/editSystemConfig'">保 存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane name="seven" v-permission="'/systemConfig/cleanRedisByKey'">
        <span slot="label"><i class="el-icon-edit"></i> Redis管理</span>
        <el-form style="margin-left: 20px;" label-position="left"   label-width="120px" >

          <aside>
            Redis管理主要用于清空一些缓存数据<br/>
            用户首次部署时，可以使用清空全部，把Redis中的缓存一键清空<br/>
          </aside>

          <el-form-item label="全部">
            <el-row>
              <el-col :span="6">
                <el-button type="danger" @click="cleanRedis('ALL')">清空全部</el-button>
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item label="博客相关">
            <el-row>
              <el-col :span="3">
                <el-button type="primary" @click="cleanRedis('BLOG_CLICK')">清空点击量</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="success" @click="cleanRedis('BLOG_PRAISE')">清空点赞量</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="info" @click="cleanRedis('BLOG_LEVEL')">清空推荐博客</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="warning" @click="cleanRedis('HOT_BLOG')">清空热门博客</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="danger" @click="cleanRedis('NEW_BLOG')">清空最新博客</el-button>
              </el-col>
            </el-row>
          </el-form-item>

          <el-form-item label="分类和归档相关">
            <el-row>
              <el-col :span="3">
                <el-button type="primary" @click="cleanRedis('MONTH_SET')">清空分类日期</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="success" @click="cleanRedis('BLOG_SORT_BY_MONTH')">清空分类数据</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="info" @click="cleanRedis('BLOG_SORT_CLICK')">清空分类点击量</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="warning" @click="cleanRedis('TAG_CLICK')">清空标签点击量</el-button>
              </el-col>
            </el-row>
          </el-form-item>


          <el-form-item label="系统相关">
            <el-row>
              <el-col :span="3">
                <el-button type="primary" @click="cleanRedis('REDIS_DICT_TYPE')">清空字典</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="success" @click="cleanRedis('ADMIN_VISIT_MENU')">清空角色访问菜单</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="info" @click="cleanRedis('userToken')">清空用户Token</el-button>
              </el-col>

              <el-col :span="3">
                <el-button type="warning" @click="cleanRedis('REQUEST_LIMIT')">清空接口请求限制</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="仪表盘通知" name="third">
        <span slot="label"><i class="el-icon-edit"></i> 仪表盘通知</span>
        <div class="editor-container">
          <CKEditor ref="editor" v-if="form.editorModel == '0'" :content="form.dashboardNotification" :height="500"></CKEditor>
          <MarkdownEditor ref="editor" v-if="form.editorModel == '1'" :height="660" style="margin-top: 12px"></MarkdownEditor>
        </div>
        <div style="margin-top: 5px; margin-left: 10px;" >
          <el-button type="primary" @click="submitForm()" v-permission="'/system/editMe'">保 存</el-button>
        </div>
      </el-tab-pane>

    </el-tabs>

  </div>
</template>

<script>
import { getSystemConfig, editSystemConfig, cleanRedisByKey } from "@/api/systemConfig";
import {getListByDictTypeList} from "@/api/sysDictData"

import CKEditor from "@/components/CKEditor";
import MarkdownEditor from "@/components/MarkdownEditor";

export default {
  data() {
    return {
      form: {

      },
      index: "0", // 当前激活页
      activeName: "one",
      areaDictList: [], //存储区域字典
      yesNoDictList: [], //是否字典
      openDictList: [], // 开启关闭字典
      picturePriorityDictList: [], //图片显示优先级字典
      editorModalDictList: [], // 文本编辑器字典列表
      searchModelDictList: [], // 搜索模式字典列表
      loadingInstance: null, // loading对象
      rules: {
        localPictureBaseUrl: [
          {pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/, message: '请输入正确的域名'},
        ],
        qiNiuPictureBaseUrl: [
          {pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/, message: '请输入正确的域名'},
        ],
        email: [
          {pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/, message: '请输入正确的邮箱'},
        ],
      }
    };
  },
  watch: {

  },
  components: {
    CKEditor,
    MarkdownEditor
  },
  created() {
    // 获取字典
    this.getDictList()
    // 获取系统配置
    this.getSystemConfigList()
  },
  methods: {
    /**
     * 字典查询
     */
    getDictList: function () {

      var dictTypeList =  ['sys_search_model', 'sys_yes_no', 'sys_picture_priority', 'sys_storage_region', 'sys_normal_disable', 'sys_editor_modal']

      getListByDictTypeList(dictTypeList).then(response => {
        if (response.code == this.$ECode.SUCCESS) {
          let dictMap = response.data;
          this.areaDictList = dictMap.sys_storage_region.list
          this.yesNoDictList = dictMap.sys_yes_no.list
          this.openDictList = dictMap.sys_normal_disable.list
          this.picturePriorityDictList = dictMap.sys_picture_priority.list
          this.editorModalDictList = dictMap.sys_editor_modal.list
          this.searchModelDictList = dictMap.sys_search_model.list
        }
      });
    },
    handleClick(tab, event) {
      this.index = tab.index
      //设置富文本内容
      if (this.form.dashboardNotification) {
        this.$refs.editor.setData(this.form.dashboardNotification);
      }
    },
    getSystemConfigList: function() {
      getSystemConfig().then(response => {
        if (response.code == this.$ECode.SUCCESS) {
          if (response.data) {
            this.form = response.data;
          }
        }
      });
    },
    cleanRedis: function(key) {
      let params = []
      params.push(key)
      cleanRedisByKey(params).then(response => {
        if(response.code == this.$ECode.SUCCESS) {
          this.$commonUtil.message.success(response.message)
        } else {
          this.$commonUtil.message.error(response.message)
        }
      })
    },
    submitForm: function() {
      console.log("开始提交表单")
      this.$refs.form.validate((valid) => {
        if(!valid) {
          console.log("校验出错");
        } else {
          //获取文本编辑器中的内容【只有在切换到仪表盘通知的时候，才需要获取】
          if(this.index == "6") {
            this.form.dashboardNotification = this.$refs.editor.getData();
          }
          editSystemConfig(this.form).then(res => {
            if (res.code == this.$ECode.SUCCESS) {
              this.$commonUtil.message.success(res.message)
            } else {
              this.$commonUtil.message.error(res.message)
            }
          });
        }
      })
    },

  }
};
</script>

<style lang="scss">
  aside {
    background: #eef1f6;
    padding: 8px 24px;
    margin-bottom: 20px;
    border-radius: 2px;
    display: block;
    line-height: 32px;
    font-size: 16px;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Fira Sans", "Droid Sans", "Helvetica Neue", sans-serif;
    color: #2c3e50;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;

  a {
    color: #337ab7;
    cursor: pointer;

  &:hover {
     color: rgb(32, 160, 255);
   }
  }
  }
</style>

