<template>
  <div class="move-dialog-wrapper">
    <!-- 移动文件-选择目录模态框 -->
    <el-dialog title="选择目录" :visible.sync="dialogMoveFile.visible">
      <div class="el-dialog-div">
        <el-tree
          :data="dialogMoveFile.fileTree"
          :props="defaultProps"
          :highlight-current="true"
          @node-click="selectNodeClick"
        ></el-tree>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="$emit('setMoveFileDialogData', null, false)">取 消</el-button>
        <el-button type="primary" @click="$emit('confirmMoveFile')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MoveFileDialog',
  props: {
    dialogMoveFile: Object
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  methods: {
    //  移动文件模态框：选择目录事件
    selectNodeClick(data) {
      let selectFilePath = data.attributes.filepath
        ? data.attributes.filepath
        : '/'
      this.$emit('setSelectFilePath', selectFilePath)
    }
  }
}
</script>

<style lang="stylus" scoped>
.move-dialog-wrapper
  >>> .el-dialog
    .el-dialog__header
      display flex
    .el-dialog__body
      padding 10px 30px
      .el-tree
        .el-tree-node__content
          height 34px
          font-size 16px
          .el-icon-caret-right
            font-size 18px
        .el-tree-node.is-current>.el-tree-node__content
          color $Primary
          .el-tree-node__expand-icon
            color inherit
</style>