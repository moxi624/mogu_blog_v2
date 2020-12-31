<template>
  <div class="file-table-wrapper">
    <!-- 文件表格 -->
    <el-table
      v-loading="loading"
      ref="multipleTable"
      :data="tableData"
      :default-sort="{ prop: 'isDir', order: 'descending'}"
      class="file-table"
      fit
      element-loading-text="数据加载中"
      tooltip-effect="dark"
      @select-all="selectAllFileRow"
      @select="selectFileRow"
    >
      <el-table-column type="selection" width="55"/>
      <el-table-column label prop="isDir" width="60">
        <template slot-scope="scope">
          <img :src="setFileImg(scope.row.extendName)" style="width: 30px;" >
        </template>
      </el-table-column>
      <el-table-column
        prop="filename"
        show-overflow-tooltip
      >
        <template slot="header" slot-scope="scope">
          <span>文件名</span>
          <el-input
            v-model="fileNameSearch"
            size="mini"
            style="width: 200px;display: inline-block;float:right;margin-right: calc(100% - 294px);"
            placeholder="输入文件名搜索"
          />
        </template>
        <template slot-scope="scope">
          <div style="cursor:pointer;" @click="clickFileName(scope.row)">
            <span v-if="scope.row.isDir == 1">{{ scope.row.fileName }}</span>
            <span v-else>{{ scope.row.fileOldName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        v-if="Number($route.query.filetype)"
        label="路径"
        width="200"
        prop="filepath"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span
            style="cursor: pointer;"
            title="点击跳转"
            @click="$router.push({ query: { filepath:scope.row.filePath, filetype: 0 } })"
          >{{ scope.row.filePath }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :sort-by="['isDir','extendName']"
        label="类型"
        width="80"
        prop="extendname"
        sortable
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span v-if="scope.row.extendName">{{ scope.row.extendName }}</span>
          <span v-else>文件夹</span>
        </template>
      </el-table-column>
      <el-table-column
        :sort-by="['isDir','fileSize']"
        label="大小"
        width="80"
        prop="filesize"
        sortable
        show-overflow-tooltip
        align="right"
      >
        <template slot-scope="scope">
          <div style="padding: 0 10px;">{{ calculateFileSize(scope.row.fileSize) }}</div>
        </template>
      </el-table-column>

      <el-table-column
        :sort-by="['isDir','createTime']"
        label="创建时间"
        prop="createTime"
        width="180"
        show-overflow-tooltip
        sortable
      />

      <el-table-column :width="operaColumnWidth">
        <template slot="header">
          <span>操作</span>
          <i
            class="el-icon-circle-plus"
            title="展开操作列按钮"
            @click="$store.commit('changeOperaColumnExpand', 1)"
          />
          <i
            class="el-icon-remove"
            title="收起操作列按钮"
            @click="$store.commit('changeOperaColumnExpand', 0)"
          />
        </template>

        <template slot-scope="scope">
          <div v-if="operaColumnExpand">
            <el-button type="warning" size="mini" @click.native="showEditDialog(scope.row)">重命名</el-button>
            <el-button v-permission="'/networkDisk/delete'" type="danger" size="mini" @click.native="deleteFile(scope.row)">删除</el-button>
            <el-button v-permission="'/networkDisk/move'" type="primary" size="mini" @click.native="showMoveFileDialog(scope.row)">移动</el-button>
            <el-button v-if="scope.row.isDir === 0" type="success" size="mini">
              <a
                v-download="scope.row.fileUrl"
                target="_blank"
                style="display: block;color: inherit;"
              >下载</a>
            </el-button>
            <el-button
              v-if="scope.row.extendname=='zip'"
              type="warning"
              size="mini"
              @click.native="handleUnzipFile(scope.row)"
            >解压缩</el-button>
          </div>

          <el-dropdown v-else trigger="click">
            <el-button size="mini">
              操作
              <i class="el-icon-arrow-down el-icon--right"/>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="showEditDialog(scope.row)" >重命名</el-dropdown-item>
              <el-dropdown-item v-permission="'/networkDisk/delete'" @click.native="deleteFile(scope.row)">删除</el-dropdown-item>
              <el-dropdown-item v-permission="'/networkDisk/move'" @click.native="showMoveFileDialog(scope.row)">移动</el-dropdown-item>
              <el-dropdown-item v-if="scope.row.isDir === 0">
                <a
                  v-download="scope.row.fileUrl"
                  target="_blank"
                  style="display: block;color: inherit;"
                >下载</a>
              </el-dropdown-item>
              <el-dropdown-item
                v-if="scope.row.extendname=='zip'"
                @click.native="handleUnzipFile(scope.row)"
              >解压缩</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>

      </el-table-column>

    </el-table>

    <!-- 添加或修改对话框 -->
    <el-dialog :visible.sync="dialogFormVisible" title="重命名">
      <el-form ref="form" :model="form" :rules="rules">

        <el-form-item label="文件名" label-width="120px" prop="content">
          <el-input v-model="form.fileOldName" auto-complete="off"/>
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
import { unzipFile, deleteFile, editFile } from '@/api/file.js'
import { mapGetters } from 'vuex'

export default {
  name: 'FileTable',
  props: {
    fileList: Array,
    loading: Boolean
  },
  data() {
    return {
      storageValue: 0,
      fileNameSearch: '',
      dialogFormVisible: false,
      form: {},
      rules: {
        fileOldName: [
          { required: true, message: '名称不能为空', trigger: 'blur' },
          { min: 1, max: 10, message: '长度在1到10个字符' }
        ],
        sort: [
          { required: true, message: '排序字段不能为空', trigger: 'blur' },
          { pattern: /^[0-9]\d*$/, message: '排序字段只能为自然数' }
        ]
      },
      //  移动文件模态框数据
      dialogMoveFile: {
        isBatchMove: false,
        visible: false, //  是否可见
        fileTree: [], //  目录树
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      },
      //  可以识别的文件类型
      fileImgTypeList: [
        'png',
        'jpg',
        'jpeg',
        'webp',
        'md',
        'docx',
        'doc',
        'ppt',
        'pptx',
        'xls',
        'xlsx',
        'avi',
        'mp4',
        'css',
        'csv',
        'chm',
        'rar',
        'zip',
        'dmg',
        'mp3',
        'open',
        'pdf',
        'rtf',
        'txt',
        'oa',
        'js',
        'html',
        'img',
        'sql',
        'jar',
        'svg',
        'gif',
        'json',
        'exe',
        'war'
      ],
      //  文件图片Map映射
      fileImgMap: {
        dir: require('@/assets/images/file/dir.png'),
        chm: require('@/assets/images/file/file_chm.png'),
        css: require('@/assets/images/file/file_css.png'),
        csv: require('@/assets/images/file/file_csv.png'),
        png: require('@/assets/images/file/file_pic.png'),
        jpg: require('@/assets/images/file/file_pic.png'),
        jpeg: require('@/assets/images/file/file_pic.png'),
        webp: require('@/assets/images/file/file_pic.png'),
        md: require('@/assets/images/file/file_md.png'),
        docx: require('@/assets/images/file/file_word.png'),
        doc: require('@/assets/images/file/file_word.png'),
        ppt: require('@/assets/images/file/file_ppt.png'),
        pptx: require('@/assets/images/file/file_ppt.png'),
        xls: require('@/assets/images/file/file_excel.png'),
        xlsx: require('@/assets/images/file/file_excel.png'),
        mp4: require('@/assets/images/file/file_video.png'),
        avi: require('@/assets/images/file/file_avi.png'),
        rar: require('@/assets/images/file/file_rar.png'),
        zip: require('@/assets/images/file/file_zip.png'),
        dmg: require('@/assets/images/file/file_dmg.png'),
        mp3: require('@/assets/images/file/file_music.png'),
        open: require('@/assets/images/file/file_open.png'),
        pdf: require('@/assets/images/file/file_pdf.png'),
        rtf: require('@/assets/images/file/file_rtf.png'),
        txt: require('@/assets/images/file/file_txt.png'),
        oa: require('@/assets/images/file/file_oa.png'),
        unknown: require('@/assets/images/file/file_unknown.png'),
        js: require('@/assets/images/file/file_js.png'),
        html: require('@/assets/images/file/file_html.png'),
        img: require('@/assets/images/file/file_img.png'),
        sql: require('@/assets/images/file/file_sql.png'),
        jar: require('@/assets/images/file/file_jar.png'),
        svg: require('@/assets/images/file/file_svg.png'),
        gif: require('@/assets/images/file/file_gif.png'),
        json: require('@/assets/images/file/file_json.png'),
        exe: require('@/assets/images/file/file_exe.png'),
        war: require('@/assets/images/file/file_war.png')
      }
      // //  查看图片模态框数据
      // imgReview: {
      //   visible: false,
      //   url: ''
      // }
    }
  },
  watch: {
    operaColumnExpand(newValue) {
      console.log(newValue)
    }
  },
  computed: {
    //  selectedColumnList:判断当前用户设置的左侧栏是否折叠, operaColumnExpand:判断当前用户设置的操作列是否展开
    ...mapGetters(['selectedColumnList', 'operaColumnExpand']),
    //  当前查看的文件路径
    filepath: {
      get() {
        return this.$route.query.filepath
      },
      set() {
        return ''
      }
    },
    //  过滤后的表格数据
    tableData() {
      return this.fileList.filter(
        data =>
          !this.fileNameSearch ||
          data.fileOldName
            .toLowerCase()
            .includes(this.fileNameSearch.toLowerCase())
      )
    },
    //  判断当前路径下是否有普通文件
    isIncludeNormalFile() {
      return this.fileList.map(data => data.isDir).includes(0)
    },
    //  判断当前路径下是否有压缩文件
    isIncludeZipRarFile() {
      return (
        this.fileList.map(data => data.extendName).includes('zip') ||
        this.fileList.map(data => data.extendName).includes('rar')
      )
    },
    operaColumnWidth() {
      return this.operaColumnExpand
        ? this.isIncludeNormalFile
          ? this.isIncludeZipRarFile
            ? 300
            : 300
          : 300
        : 300
    }
  },
  methods: {
    // 显示编辑框
    showEditDialog: function(row) {
      this.dialogFormVisible = true
      this.form = row
    },
    submitForm: function() {
      //  重命名
      const data = {
        uid: this.form.uid,
        fileOldName: this.form.fileOldName,
        fileName: this.form.fileName,
        oldFilePath: this.form.filePath,
        newFilePath: this.form.filePath,
        extendName: this.form.extendName
      }
      console.log('上传文件', data)
      editFile(data).then(res => {
        if (res.code == this.$ECode.SUCCESS) {
          this.$message.success('重命名成功')
          if (this.form.extendName == null) {
            console.log('对文件重命名')
            this.form.fileName = this.form.fileOldName
          }
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.message)
        }
      })
    },
    /**
     * 表格数据获取相关事件
     */
    //  根据文件扩展名设置文件图片
    setFileImg(extendname) {
      // 根据扩展名设置图片
      if (extendname === undefined) {
        //  文件夹
        return this.fileImgMap.dir
      } else if (!this.fileImgTypeList.includes(extendname)) {
        //  无法识别文件类型的文件
        return this.fileImgMap.unknown
      } else {
        //  可以识别文件类型的文件
        return this.fileImgMap[extendname]
      }
    },
    //  计算文件大小
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

    //  点击文件名
    clickFileName(row) {
      //  若是目录则进入目录
      if (row.isDir) {
        this.$router.push({
          query: {
            filepath: row.filePath + row.fileName + '/',
            filetype: 0
          }
        })
      }
      //  若是文件，则进行相应的预览
      else {
        //  若当前点击项是图片
        const PIC = ['png', 'jpg', 'jpeg', 'gif', 'svg', 'webp']
        if (PIC.includes(row.extendName)) {
          this.$emit('getImgReviewData', row, true)
        }
        //  若当前点击项是pdf
        const TEXT = ['txt', 'pdf', 'md']
        if (TEXT.includes(row.extendName)) {
          window.open(row.fileUrl, '_blank')
        }
        //  若当前点击项是html、js、css、json
        const CODE = ['html', 'js', 'css', 'json']
        if (CODE.includes(row.extendName)) {
          window.open(row.fileUrl, '_blank')
        }
        //  若当前点击项是视频mp4格式
        const VIDEO = ['mp4']
        if (VIDEO.includes(row.extendName)) {
          window.open(row.fileUrl, '_blank')
        }
        //  若当前点击项是视频mp3格式
        const AUDIO = ['mp3']
        if (AUDIO.includes(row.extendName)) {
          window.open(row.fileUrl, '_blank')
        }
      }
    },

    /**
     * 表格勾选框事件
     */
    //  表格-全选事件, selectoin 勾选的行数据
    selectAllFileRow(selection) {
      this.$emit('setSelectionFile', selection)
    },
    //  表格-选中一行事件, selectoin 勾选的行数据
    selectFileRow(selection) {
      this.$emit('setSelectionFile', selection)
    },

    /**
     * 移动按钮相关事件
     */
    //  操作列-移动按钮：打开移动文件模态框
    showMoveFileDialog(file) {
      //  第一个参数: 是否批量移动；第二个参数：打开/关闭移动文件模态框
      this.$emit('setOperationFile', file)
      this.$emit('setMoveFileDialogData', false, true)
    },

    //  操作列-解压缩按钮
    handleUnzipFile(fileInfo) {
      const loading = this.$loading({
        lock: true,
        text: '正在解压缩，请稍等片刻...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      unzipFile(fileInfo).then(res => {
        if (res.code == this.$ECode.SUCCESS) {
          this.$emit('getTableDataByType')
          this.$emit('showStorage')
          this.$message.success('解压成功')
          loading.close()
        } else {
          this.$message.error(res.message)
        }
      })
    },

    /**
     * 删除按钮相关事件
     */
    //  操作列-删除按钮
    deleteFile(fileInfo) {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.confirmDeleteFile(fileInfo)
        })
        .catch(() => {
          this.$commonUtil.message.info('已取消删除')
        })
    },
    //  删除文件模态框-确定按钮
    confirmDeleteFile(fileInfo) {
      deleteFile(fileInfo).then(res => {
        if (res.code == this.$ECode.SUCCESS) {
          this.$emit('getTableDataByType')
          this.$emit('showStorage')
          this.$message.success('删除成功')
        } else {
          this.$message.error(res.message)
        }
      })
    }
  }
}
</script>

<style lang="stylus" scoped>
@import '~@/assets/styles/varibles.styl'
@import '~@/assets/styles/mixins.styl'
.file-table-wrapper
  margin-top 2px
  .file-table
    width 100% !important
    height calc(100vh - 180px)
    >>> .el-table__header-wrapper
      .el-icon-circle-plus, .el-icon-remove
        margin-left 6px
        cursor pointer
        font-size 16px
        &:hover
          color $Primary
    >>> .el-table__body-wrapper
      height calc(100vh - 228px)
      overflow-y auto
      setScrollbar(10px)
  .img-review-wrapper
    position fixed
    top 0
    right 0
    bottom 0
    left 0
    overflow auto
    width 100%
    height 100%
    z-index 2010
    text-align center
    display flex
    align-items center
    animation imgReviewAnimation 0.3s
    -webkit-animation imgReviewAnimation 0.3s /* Safari and Chrome */
    animation-iteration-count 0.3
    -webkit-animation-iteration-count 0.3
    animation-fill-mode forwards
    -webkit-animation-fill-mode forwards /* Safari 和 Chrome */
    @keyframes imgReviewAnimation
      0%
        background transparent
      100%
        background rgba(0, 0, 0, 0.8)
    @keyframes imgReviewAnimation
      0%
        background transparent
      100%
        background rgba(0, 0, 0, 0.8)
    .img-large
      margin 0 auto
      max-width 80%
      max-height 100%
</style>
