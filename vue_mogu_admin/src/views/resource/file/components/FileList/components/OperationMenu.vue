<template>
  <div class="operation-menu-wrapper">

    <el-button
      v-permission="'/networkDisk/add'"
      id="uploadFileId"
      size="medium"
      type="primary"
      icon="el-icon-upload2"
      @click="handleAdd">上传文件
    </el-button>

    <el-button v-permission="'/networkDisk/create'" v-if="!filetype" size="medium" type="success" @click="addFolder()">
      新建文件夹
    </el-button>

    <div v-if="selectionFile.length !== 0" style="display: inline-block;">
      <el-button
        v-permission="'/networkDisk/delete'"
        size="medium"
        type="danger"
        icon="el-icon-delete"
        @click="deleteSelectedFile()">删除选中
      </el-button>
      <el-button
        v-permission="'/networkDisk/move'"
        v-if="!filetype"
        size="medium"
        icon="el-icon-edit"
        @click="moveSelectedFile()">移动
      </el-button>
      <!-- <el-button size="medium" icon="el-icon-document-copy">拷贝</el-button> -->
      <el-button
        v-permission="'/networkDisk/download'"
        size="medium"
        icon="el-icon-download"
        @click="downloadSelectedFile()">下载
      </el-button>
    </div>

    <!-- 多选文件下载，页面隐藏 -->
    <a
      v-download="item.fileUrl"
      v-for="(item,index) in selectionFile"
      :key="index"
      :title="'downloadLink' + index"
      :ref="'downloadLink' + index"
      target="_blank"
    />

    <div class="storeDisWrapper" style="float:right;">已使用 {{ storageValue }} 容量</div>

    <!-- 添加或修改对话框 -->
    <el-dialog :visible.sync="dialogFormVisible" title="上传文件">
      <el-upload
        ref="upload"
        :action="uploadPictureHost"
        :data="otherData"
        :on-success="fileSuccess"
        class="upload-demo"
        drag
        name="filedatas"
        multiple>
        <i class="el-icon-upload"/>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">单文件不超过100MB，总文件不超过500MB</div>
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
      default: '0'
    }
  },
  data() {
    return {
      filePath: '/',
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
        const res = {
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

    // 图片上传地址
    this.uploadPictureHost = process.env.PICTURE_API + '/storage/uploadFile'

    // 判断上传路径是否存在
    if (this.$route.query.filepath) {
      this.filePath = this.$route.query.filepath
    }
    // 其它数据
    this.otherData = {
      filePath: this.filePath,
      isdir: 0,
      source: 'picture',
      userUid: 'uid00000000000000000000000000000000',
      adminUid: 'uid00000000000000000000000000000000',
      projectName: 'blog',
      sortName: 'admin',
      token: getToken()
    }

    console.log('得到的上传path', this.$route.query.filepath)
  },
  methods: {
    handleAdd: function() {
      this.dialogFormVisible = true
      if (this.$route.query.filepath) {
        this.filePath = this.$route.query.filepath
        this.otherData.filePath = this.$route.query.filepath
      }
    },
    submitNormalUpload: function() {
      this.$refs.upload.submit()
    },
    //  上传按钮
    fileSuccess(result, file, fileList) {
      if (result.code == this.$ECode.SUCCESS) {
        this.$message.success(result.message)
        this.$emit('getTableDataByType')
        this.$emit('showStorage')
      } else {
        this.$message.error(result.message)
      }
    },

    /**
     *  enter+down 新建文件夹
     */
    handleEnterDown() {
      //  测试enter+down组合键触发事件
      const self = this
      let code1 = 0
      let code2 = 0

      document.onkeydown = function(e) {
        const evn = e || event
        const key = evn.keyCode || evn.which || evn.charCode
        // enter
        if (key === 13) {
          code1 = 13
          e.preventDefault() // 禁止默认事件
        }
        // down keyup时及时的 归零
        if (key === 40) {
          code2 = 0
          e.preventDefault() // 禁止默认事件
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
      const data = {
        fileName: fileName,
        fileOldName: fileName,
        isDir: 1
      }

      // 判断上传路径是否存在
      if (this.$route.query.filepath) {
        data.filePath = this.$route.query.filepath
      } else {
        data.filePath = '/'
      }

      createFile(data).then(res => {
        if (res.code == this.$ECode.SUCCESS) {
          this.$message.success(res.message)
          this.$emit('getTableDataByType')
        } else {
          this.$message.warning(res.message)
        }
      })
    },

    //  批量操作-删除按钮
    deleteSelectedFile() {
      //  批量删除接口
      batchDeleteFile(this.selectionFile).then(res => {
        if (res.code == this.$ECode.SUCCESS) {
          this.$commonUtil.message.success(res.message)
          this.$emit('getTableDataByType')
          this.$emit('showStorage')
        } else {
          this.$commonUtil.message.error(res.message)
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
        console.log('选中的文件', this.selectionFile[i])
        // 如果下载的是文件夹，那么不作处理
        if (this.selectionFile[i].isDir == 1) {
          this.$commonUtil.message.error('文件夹无法下载')
        } else {
          const name = 'downloadLink' + i
          this.$refs[name][0].click()
        }
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
