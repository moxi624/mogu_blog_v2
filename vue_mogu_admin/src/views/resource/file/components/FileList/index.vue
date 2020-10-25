<template>
  <div class="file-list-wrapper">
    <!-- 操作按钮 -->
    <el-header class="file-list-header">
      <OperationMenu
        :operation-file="operationFile"
        :selection-file="selectionFile"
        :filepath="filePath"
        :storage-value="storageValue"
        @showStorage="showStorage"
        @getTableDataByType="getTableDataByType"
        @setMoveFileDialogData="setMoveFileDialogData"
      />
    </el-header>
    <div class="middle-wrapper">
      <!-- 面包屑导航栏 -->
      <BreadCrumb class="breadcrumb"/>
      <!-- 图片展示模式 -->
      <div v-show="fileType === 1" class="change-image-model">
        <el-radio-group v-model="imageGroupLable" size="mini" @change="changeImageDisplayModel">
          <el-radio-button :label="0">列表</el-radio-button>
          <el-radio-button :label="1">网格</el-radio-button>
          <el-radio-button :label="2">时间线</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    <!-- 文件列表表格 -->
    <FileTable
      v-show="!imageModel || fileType !== 1"
      :file-list="fileList"
      :loading="loading"
      @setMoveFileDialogData="setMoveFileDialogData"
      @setOperationFile="setOperationFile"
      @setSelectionFile="setSelectionFile"
      @showStorage="showStorage"
      @getTableDataByType="getTableDataByType"
      @getImgReviewData="getImgReviewData"
    />

    <!-- 图片网格模式 -->
    <ImageModel
      v-if="imageModel && fileType === 1"
      :file-list="fileList"
      class="image-model"
      @getImgReviewData="getImgReviewData"
    />

    <!-- 移动文件模态框 -->
    <MoveFileDialog
      :dialog-move-file="dialogMoveFile"
      @setSelectFilePath="setSelectFilePath"
      @confirmMoveFile="confirmMoveFile"
      @setMoveFileDialogData="setMoveFileDialogData"
    />
    <!-- 查看大图 -->
    <ImgReview :img-review="imgReview" @getImgReviewData="getImgReviewData"/>
  </div>
</template>

<script>
import OperationMenu from './components/OperationMenu'
import BreadCrumb from './components/BreadCrumb'
import FileTable from './components/FileTable'
import ImageModel from './components/ImageModel'
import MoveFileDialog from './components/MoveFileDialog'
import ImgReview from './components/ImgReview'
import {
  getFileList,
  getFileTree,
  getStorage,
  moveFile,
  batchMoveFile
} from '@/api/file.js'

export default {
  name: 'FileList',
  components: {
    OperationMenu,
    BreadCrumb,
    FileTable,
    ImageModel,
    MoveFileDialog,
    ImgReview
  },
  data() {
    return {
      storageValue: '0',
      fileNameSearch: '',
      loading: true, //  表格数据-loading
      fileList: [], //  表格数据-文件列表
      //  移动文件模态框数据
      dialogMoveFile: {
        isBatchMove: false,
        visible: false, //  是否可见
        fileTree: [] //  目录树
      },
      selectFilePath: '', //  移动文件路径
      filePath: '/', // 默认路径
      operationFile: {}, // 当前操作行
      selectionFile: [], // 勾选的文件
      fileType: 0, //  文件类型
      //  查看图片模态框数据
      imgReview: {
        visible: false,
        fileurl: '',
        filename: '',
        extendname: ''
      },
      imageGroupLable: 0
    }
  },
  computed: {
    imageModel() {
      return this.$store.getters.imageModel
    }
  },
  watch: {
    $route(to, from) {
      this.getTableDataByType()
    }
  },
  created() {
    this.getTableDataByType()
    this.showStorage()
  },
  mounted() {
    this.imageGroupLable = this.imageModel
  },
  methods: {
    /**
     * 表格数据获取相关事件
     */
    getTableDataByType() {
      // 判断fileType
      if (this.$route.query.filetype) {
        this.fileType = parseInt(this.$route.query.filetype)
      } else {
        this.fileType = 0
      }
      if (this.$route.query.filepath) {
        this.filePath = this.$route.query.filepath
      } else {
        this.filePath = '/'
      }

      this.showFileListByType()
    },
    /**
     * 根据文件类型展示文件列表
     */
    showFileListByType() {
      const data = {}
      data.filePath = this.filePath
      data.fileType = this.fileType
      this.fileList = []
      getFileList(data).then(res => {
        if (res.code == this.$ECode.SUCCESS) {
          this.fileList = res.data
          this.loading = false
        } else {
          this.$message.error(res.message)
        }
      })
    },
    /**
     * 计算文件大小
     * */
    calculateFileSize(size) {
      const B = 1024
      const KB = Math.pow(1024, 2)
      const MB = Math.pow(1024, 3)
      const GB = Math.pow(1024, 4)
      if (!size) {
        return '_'
      } else if (size < KB) {
        return (size / B).toFixed(0) + 'KB'
      } else if (size < MB) {
        return (size / KB).toFixed(1) + 'MB'
      } else if (size < GB) {
        return (size / MB).toFixed(2) + 'GB'
      } else {
        return (size / GB).toFixed(3) + 'TB'
      }
    },

    /**
     * 表格勾选框事件
     */
    setSelectionFile(selection) {
      this.selectionFile = selection
    },

    /**
     * 移动按钮相关事件
     */
    setOperationFile(operationFile) {
      this.operationFile = operationFile
    },
    //  设置移动文件模态框相关数据，isBatchMove为null时是确认移动，值由之前的值而定
    setMoveFileDialogData(isBatchMove, visible) {
      this.initFileTree()
      this.dialogMoveFile.isBatchMove = isBatchMove || this.dialogMoveFile.isBatchMove
      this.dialogMoveFile.visible = visible
    },
    //  移动文件模态框：初始化文件目录树
    initFileTree() {
      getFileTree().then(res => {
        if (res.code == this.$ECode.SUCCESS) {
          this.dialogMoveFile.fileTree = [res.data]
        } else {
          this.$message.error(res.errorMessage)
        }
      })
    },
    //  设置移动后的文件路径
    setSelectFilePath(selectFilePath) {
      this.selectFilePath = selectFilePath
    },
    //  移动文件模态框-确定按钮事件
    confirmMoveFile() {
      if (this.dialogMoveFile.isBatchMove) {
        //  批量移动
        const data = {
          newFilePath: this.selectFilePath,
          files: JSON.stringify(this.selectionFile)
        }
        batchMoveFile(data).then(res => {
          if (res.code == this.$ECode.SUCCESS) {
            this.$commonUtil.message.success(res.message)
            this.getTableDataByType()
            this.dialogMoveFile.visible = false
            this.selectionFile = []
          } else {
            this.$commonUtil.message.error(res.message)
          }
        })
      } else {
        //  单文件移动
        const data = {
          oldFilePath: this.operationFile.filePath,
          newFilePath: this.selectFilePath,
          fileName: this.operationFile.fileName,
          fileOldName: this.operationFile.fileOldName,
          extendName: this.operationFile.extendName
        }
        moveFile(data).then(res => {
          if (res.code == this.$ECode.SUCCESS) {
            this.$commonUtil.message.success(res.message)
            this.getTableDataByType()
            this.dialogMoveFile.visible = false
          } else {
            this.$commonUtil.message.error(res.message)
          }
        })
      }
    },
    //  获取已占用内存
    showStorage() {
      getStorage().then(res => {
        if (res.code == this.$ECode.SUCCESS) {
          const size = res.data ? res.data.storageSize : 0
          const B = 1024
          const KB = Math.pow(1024, 2)
          const MB = Math.pow(1024, 3)
          const GB = Math.pow(1024, 4)
          if (!size) {
            this.storageValue = 0 + 'KB'
          } else if (size < KB) {
            this.storageValue = (size / B).toFixed(0) + 'KB'
          } else if (size < MB) {
            this.storageValue = (size / KB).toFixed(2) + 'MB'
          } else if (size < GB) {
            this.storageValue = (size / MB).toFixed(3) + 'GB'
          } else {
            this.storageValue = (size / GB).toFixed(4) + 'TB'
          }
        } else {
          this.$commonUtil.message.error(res.message)
        }
      })
    },

    //  切换图片查看模式
    changeImageDisplayModel(label) {
      this.$store.commit('changeImageModel', label)
    },

    //  获取查看大图的数据
    getImgReviewData(row, visible) {
      if (row) {
        this.imgReview.fileurl = row.fileUrl
        this.imgReview.filename = row.fileName
        this.imgReview.extendname = row.extendName
      }
      this.imgReview.visible = visible
    }
  }
}
</script>

<style lang="stylus" scoped>
@import '~@/assets/styles/varibles.styl'
.file-list-wrapper
  .file-list-header
    .el-dialog-div
      height 200px
      overflow auto

  .middle-wrapper
    display flex

    .breadcrumb
      flex 1

    .change-image-model
      margin-right 20px
      height 30px
      line-height 30px
</style>
