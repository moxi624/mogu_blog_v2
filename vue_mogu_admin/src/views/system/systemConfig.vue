<template>
  <div class="app-container">
    <el-tabs type="border-card">
      <el-tab-pane v-permission="'/systemConfig/getSystemConfig'">
        <span slot="label">
          <i class="el-icon-date"></i> 七牛云配置
        </span>

        <el-form
          style="margin-left: 20px;"
          label-position="left"
          :model="form"
          label-width="120px"
          :rules="rules"
          ref="form"
        >
          <el-form-item label="本地图片域名" prop="localPictureBaseUrl">
            <el-input v-model="form.localPictureBaseUrl" auto-complete="new-password" style="width: 400px"></el-input>
          </el-form-item>

          <el-form-item label="七牛云图片域名" prop="qiNiuPictureBaseUrl">
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

          <el-form-item label="图片上传七牛云">
            <el-radio v-for="item in yesNoDictList" :key="item.uid" v-model="form.uploadQiNiu" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item label="图片上传本地">
            <el-radio v-for="item in yesNoDictList" :key="item.uid" v-model="form.uploadLocal" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>


          <el-form-item label="图片显示优先级">
            <el-radio v-for="item in picturePriorityDictList" :key="item.uid" v-model="form.picturePriority" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-permission="'/systemConfig/editSystemConfig'">保 存</el-button>
          </el-form-item>

        </el-form>
      </el-tab-pane>

      <el-tab-pane name="two" v-permission="'/systemConfig/getSystemConfig'">
        <span slot="label"><i class="el-icon-edit"></i> 邮箱配置</span>
        <el-form style="margin-left: 20px;" label-position="left"   label-width="80px" >
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

          <!--当有新的反馈，友链申请时进行通知，首先需要在系统管理处设置接收通知的邮箱 -->
          <el-form-item label="邮件通知">
            <el-radio v-for="item in openDictList" :key="item.uid" v-model="form.startEmailNotification" :label="item.dictValue" border size="medium">{{item.dictLabel}}</el-radio>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-permission="'/systemConfig/editSystemConfig'">保 存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane name="three" v-permission="'/systemConfig/cleanRedisByKey'">
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

    </el-tabs>

  </div>
</template>

<script>
import { getSystemConfig, editSystemConfig, cleanRedisByKey } from "@/api/systemConfig";
import {getListByDictTypeList} from "@/api/sysDictData"
import { Loading } from 'element-ui';
export default {
  data() {
    return {
      form: {

      },
      areaDictList: [], //存储区域字典
      yesNoDictList: [], //是否字典
      openDictList: [], // 开启关闭字典
      picturePriorityDictList: [], //图片显示优先级字典
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

  },
  created() {
    this.loadingInstance = Loading.service({ fullscreen: true, text:'正在努力加载中~' });
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

      var dictTypeList =  ['sys_yes_no', 'sys_picture_priority', 'sys_storage_region', 'sys_normal_disable']

      getListByDictTypeList(dictTypeList).then(response => {
        if (response.code == "success") {
          var dictMap = response.data;
          this.areaDictList = dictMap.sys_storage_region.list
          this.yesNoDictList = dictMap.sys_yes_no.list
          this.openDictList = dictMap.sys_normal_disable.list
          this.picturePriorityDictList = dictMap.sys_picture_priority.list
          this.loadingInstance.close();
        } else {
          this.loadingInstance.close();
        }
      });
    },
    getSystemConfigList: function() {
      getSystemConfig().then(response => {
        if (response.code == "success") {
          if (response.data) {
            this.form = response.data;
          }
        }
      });
    },
    cleanRedis: function(key) {
      console.log(key)
      let params = []
      params.push(key)
      cleanRedisByKey(params).then(response => {
        if(response.code == "success") {
          this.$message({
            type: "success",
            message: response.data
          })
        } else {
          this.$message({
            type: "error",
            message: response.data
          })
        }
      })
    },
    submitForm: function() {
      this.$refs.form.validate((valid) => {
        if(!valid) {
          console.log("校验出错");
        } else {
          editSystemConfig(this.form).then(res => {
            console.log(res);
            if (res.code = "success") {
              this.$message({
                type: "success",
                message: res.data
              });
            } else {
              this.$message({
                type: "warning",
                message: res.data
              });
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

