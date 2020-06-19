<template>
  <div class="operation-menu-wrapper">

    <el-button size="medium" type="primary" icon="el-icon-upload2" id="uploadFileId" @click="handleAdd" v-permission="'/networkDisk/add'">上传文件</el-button>

    <el-button size="medium" @click="addFolder()" type="success" v-if="!filetype" v-permission="'/networkDisk/create'">新建文件夹</el-button>

    <div style="display: inline-block;" v-if="selectionFile.length !== 0">
      <el-button size="medium" type="danger" icon="el-icon-delete" @click="deleteSelectedFile()" v-permission="'/networkDisk/delete'">删除选中</el-button>
      <el-button size="medium" icon="el-icon-edit" @click="moveSelectedFile()" v-if="!filetype" v-permission="'/networkDisk/move'">移动</el-button>
      <!-- <el-button size="medium" icon="el-icon-document-copy">拷贝</el-button> -->
      <el-button size="medium" icon="el-icon-download" @click="downloadSelectedFile()" v-permission="'/networkDisk/download'">下载</el-button>
    </div>

    <!-- 多选文件下载，页面隐藏 -->
    <a
      target="_blank"
      v-for="(item,index) in selectionFile"
      :key="index"
      :href="'api' + item.fileurl"
      :download="item.filename+'.' + item.extendname"
      :title="'downloadLink' + index"
      :ref="'downloadLink' + index"
    ></a>

    <div class="storeDisWrapper" style="float:right;">已使用 {{storageValue}} 容量</div>


    <!-- 添加或修改对话框 -->
    <el-dialog title="上传文件" :visible.sync="dialogFormVisible">
      <el-upload
        class="upload-demo"
        drag
        ref="upload"
        name="filedatas"
        :action="uploadPictureHost"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :data="otherData"
        :on-success = "fileSuccess"
        multiple>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">单文件不超过100MB，总文件不超过500MB</div>
      </el-upload>
    </el-dialog>

  </div>
</template>

<script>
import {
  // deleteFile,
  batchDeleteFile,
  createFile
} from '@/api/file.js'
import { getToken } from '@/utils/auth'
export default {
  name: 'OperationMenu',
  props: {
    selectionFile: Array,
    operationFile: Object,
    storageValue: {
      type: String,
      default: "0"
    }
  },
  data() {
    return {
      BASE_IMAGE_URL: process.env.BASE_IMAGE_URL,
      filePath: "/",
      dialogFormVisible: false,
      fileTree: [],
      batchDeleteFileDialog: false
    }
  },
  computed: {
    //  当前查看的文件路径
    filepath: {
      get() {
        return this.$route.query.filepath
      },
      set() {
        return ''
      }
    },
    //  文件类型索引
    filetype: {
      get() {
        return Number(this.$route.query.filetype)
      },
      set() {
        return 0
      }
    },
    //  上传文件组件参数
    uploadFileData: {
      get() {
        let res = {
          filePath: this.filepath,
          isDir: 0
        }
        return res
      },
      set() {
        return {
          filepath: '/',
          isdir: 0
        }
      }
    }
  },
  created() {
    this.handleEnterDown(),

    //图片上传地址
    this.uploadPictureHost = process.env.PICTURE_API + "/storage/uploadfile";

    // 判断上传路径是否存在
    if(this.$route.query.filepath) {
      this.filePath = this.$route.query.filepath
    }
    //其它数据
    this.otherData = {
      filePath: this.filePath,
      isdir: 0,
      source: "picture",
      userUid: "uid00000000000000000000000000000000",
      adminUid: "uid00000000000000000000000000000000",
      projectName: "blog",
      sortName: "admin",
      token: getToken()
    };

    console.log("得到的上传path", this.$route.query.filepath)
  },
  methods: {
    handleAdd: function() {
      this.dialogFormVisible = true;
      if(this.$route.query.filepath) {
        this.filePath = this.$route.query.filepath
        this.otherData.filePath = this.$route.query.filepath
      }
    },
    handlePreview: function() {

    },
    handleRemove: function() {

    },
    submitNormalUpload: function() {
      console.log();
      this.$refs.upload.submit();
    },
    //  上传按钮
    fileSuccess(result, file, fileList) {
      if (result.success) {
        this.$message.success('上传成功')
        this.$emit('getTableDataByType')
        this.$emit('showStorage')
      } else {
        this.$message.error(result.errorMessage)
      }
    },

    //  enter+down 新建文件夹，请不要删除
    handleEnterDown() {
      //  测试enter+down组合键触发事件
      let self = this
      let code1 = 0
      let code2 = 0

      document.onkeydown = function(e) {
        let evn = e || event
        let key = evn.keyCode || evn.which || evn.charCode

        // enter
        if (key === 13) {
          code1 = 13
          e.preventDefault() //禁止默认事件
        }
        // down keyup时及时的 归零
        if (key === 40) {
          code2 = 0
          e.preventDefault() //禁止默认事件
        }
      }
      document.onkeyup = function(e) {
        // enter keyup时及时的 归零
        if (e.keyCode === 13) {
          code1 = 0
        }
        //  down
        if (e.keyCode === 40) {
          code2 = 40
        }
        // enter+down
        if (code1 === 13 && code2 === 40) {
          console.log('enter + down')
          //  这里写你要触发的事件名称
          self.addFolder()
        }
      }
    },
    //  新建文件夹按钮：打开模态框
    addFolder() {
      this.$prompt('请输入文件夹名称', '创建文件夹', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
        .then(({ value }) => {
          this.createFile(value)
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消输入'
          })
        })
    },
    //  新建文件夹模态框-确定按钮
    createFile(fileName) {
      let data = {
        fileName: fileName,
        isDir: 1
      }

      // 判断上传路径是否存在
      if(this.$route.query.filepath) {
        data.filePath = this.$route.query.filepath
      } else {
        data.filePath = "/"
      }

      createFile(data).then(res => {
        if (res.success) {
          this.$message.success('添加成功')
          this.$emit('getTableDataByType')
        } else {
          this.$message.warning(res.errorMessage)
        }
      })
    },

    //  批量操作-删除按钮
    deleteSelectedFile() {
      //  批量删除接口
      batchDeleteFile(this.selectionFile).then(res => {
        if (res.success) {
          this.$message({
            message: res.data,
            type: 'success'
          })
          this.$emit('getTableDataByType')
          this.$emit('showStorage')
        } else {
          this.$message.error('失败' + res.errorMessage)
        }
      })
    },
    //  批量操作-移动按钮
    moveSelectedFile() {
      this.$emit('setMoveFileDialogData', true, true)
    },
    //  批量操作：下载按钮
    downloadSelectedFile() {
      for (let i = 0; i < this.selectionFile.length; i++) {
        let name = 'downloadLink' + i
        this.$refs[name][0].click()
      }
    }
  }
}
</script>

<style lang="stylus" scoped>
.operation-menu-wrapper
  height 60px
  line-height 60px
  .upload-demo
    display inline-block
  .el-button--medium
    margin-left 10px
</style>
