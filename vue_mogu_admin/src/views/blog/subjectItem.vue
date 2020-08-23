<template>
  <div class="app-container">
    <el-table ref="dragTable" :data="list" row-key="id" border fit highlight-current-row style="width: 100%">

      <el-table-column label="标题图" width="180px" align="center">
        <template slot-scope="{row}">
          <img
            v-if="row.blog.photoList"
            :src="row.blog.photoList[0]"
            style="width: 130px;height: 70px;"
          >
        </template>
      </el-table-column>

      <el-table-column width="180px" label="标题" align="center">
        <template slot-scope="{row}">
          <span @click="onClick(row)" style="cursor:pointer;">{{ row.blog.title }}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" label="作者" align="center">
        <template slot-scope="{row}">
          <span>{{ row.blog.author }}</span>
        </template>
      </el-table-column>

      <el-table-column width="80px" label="是否原创" align="center">
        <template slot-scope="{row}">
          <el-tag v-if="row.blog.isOriginal==1" type="success">原创</el-tag>
          <el-tag v-if="row.blog.isOriginal==0" type="info">转载</el-tag>
        </template>
      </el-table-column>

      <el-table-column width="80px" label="分类" align="center">
        <template slot-scope="{row}">
          <span>{{ row.blog.blogSort.sortName }}</span>
        </template>
      </el-table-column>

      <el-table-column width="120px" label="标签" align="center">
        <template slot-scope="{row}">
          <template>
            <el-tag
              style="margin-left: 3px"
              type="warning"
              v-if="item"
              :key="index"
              v-for="(item, index) in row.blog.tagList"
            >{{item.content}}</el-tag>
          </template>
        </template>
      </el-table-column>

      <el-table-column align="center" label="拖拽" width="80">
        <template slot-scope="{}">
          <svg-icon class="drag-handler" icon-class="drag" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { getSubjectItemList, editSubjectItem } from '@/api/subjectItem'
  import Sortable from 'sortablejs'

  export default {
    name: 'DragTable',
    filters: {
      statusFilter(status) {
        const statusMap = {
          published: 'success',
          draft: 'info',
          deleted: 'danger'
        }
        return statusMap[status]
      }
    },
    data() {
      return {
        list: [],
        total: null,
        listLoading: true,
        BLOG_WEB_URL: process.env.BLOG_WEB_URL,
        listQuery: {
          page: 1,
          limit: 10
        },
        sortable: null,
        oldList: [],
        newList: []
      }
    },
    created() {
      this.getList()
    },
    methods: {
      async getList() {
        var params = {};
        params.keyword = "";
        params.pageSize = 10;
        params.currentPage = 1;
        getSubjectItemList(params).then(response => {
          console.log("得到的列表", response)
          if(response.code == this.$ECode.SUCCESS) {
            this.list = response.data.records
            this.total = response.total
            this.$nextTick(() => {
              this.setSort()
            })
          }
        })
      },
      getSubjectList() {
        var params = {};
        params.keyword = "";
        params.pageSize = 10;
        params.currentPage = 1;
        getSubjectItemList(params).then(response => {
          console.log("得到的列表", response)
          if(response.code == this.$ECode.SUCCESS) {
            this.list = response.data.records
            this.total = response.total
          }
        })
      },
      // 跳转到该博客详情
      onClick: function(row) {
        window.open( this.BLOG_WEB_URL + "/#/info?blogUid=" + row.uid);
      },
      setSort() {
        console.log(this.$refs.dragTable)
        const el = this.$refs.dragTable.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
        this.sortable = Sortable.create(el, {
          ghostClass: 'sortable-ghost', // Class name for the drop placeholder,
          setData: function(dataTransfer) {
            dataTransfer.setData('Text', '')
          },
          onEnd: evt => {
            let list = this.list
            const targetRow = list.splice(evt.oldIndex, 1)[0]
            list.splice(evt.newIndex, 0, targetRow)

            let subjectList = []
            for(let a=list.length-1; a >= 0; a--) {
              let params = {}
              params.uid = list[a].uid
              params.blogUid = list[a].blogUid
              params.subjectUid = list[a].subjectUid
              params.sort = list.length - a
              subjectList.push(params)
            }
            editSubjectItem(subjectList).then(response => {
              console.log("修改完成后的状态", response)
              if(response.code == this.$ECode.SUCCESS) {
                // this.$commonUtil.message.success(response.data)
                this.getSubjectList()
              }
            })
          }
        })
      }
    }
  }
</script>

<style>
  .sortable-ghost{
    opacity: .8;
    color: #fff!important;
    background: #42b983!important;
  }
</style>

<style scoped>
  .icon-star{
    margin-right:2px;
  }
  .drag-handler{
    width: 20px;
    height: 20px;
    cursor: pointer;
  }
  .show-d{
    margin-top: 15px;
  }
</style>
