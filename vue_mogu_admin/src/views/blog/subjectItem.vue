<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;" v-permission="'/subjectItem/sortByCreateTime'">
      <el-button
        class="filter-item"
        type="info"
        @click="handleSortByCreateTime(true)"
        icon="el-icon-document"
      >按创建时间倒排</el-button>

      <el-button
        class="filter-item"
        type="info"
        @click="handleSortByCreateTime(false)"
        icon="el-icon-document"
      >按创建时间正排</el-button>
    </div>

    <aside>
      在博客管理添加专题元素，通过拖拽实现专题内列表的排序
    </aside>

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

      <el-table-column width="250px" label="标题" align="center">
        <template slot-scope="{row}">
          <el-tooltip @click.native="onClick(row)" class="item" effect="dark" :content="row.blog.title" placement="top">
            <span style="cursor:pointer;">{{ strSubstring(row.blog.title, 20) }}</span>
          </el-tooltip>
        </template>
      </el-table-column>

      <el-table-column width="100px" label="作者" align="center">
        <template slot-scope="{row}">
          <span>{{ row.blog.author }}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" label="是否原创" align="center">
        <template slot-scope="{row}">
          <el-tag v-if="row.blog.isOriginal==1" type="success">原创</el-tag>
          <el-tag v-if="row.blog.isOriginal==0" type="info">转载</el-tag>
        </template>
      </el-table-column>

      <el-table-column width="100px" label="分类" align="center">
        <template slot-scope="{row}">
          <span>{{ row.blog.blogSort.sortName }}</span>
        </template>
      </el-table-column>

      <el-table-column width="300px" label="标签" align="center">
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

      <el-table-column label="排序" width="100" align="center">
        <template slot-scope="scope">
          <el-tag type="warning">{{ scope.row.sort }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column width="100px" label="操作" align="center">
        <template slot-scope="{row}">
          <el-button @click="handleDelete(row)" type="danger" size="small" v-permission="'/subjectItem/delete'">删除</el-button>
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
  import { getSubjectItemList, editSubjectItem, deleteBatchSubjectItem, sortByCreateTime} from '@/api/subjectItem'
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
        subjectUid: "",
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
      //传递过来的pictureSordUid
      this.subjectUid = this.$route.query.subjectUid;
      this.getList()
    },
    methods: {
      // 根据创建时间对专题进行排序
      handleSortByCreateTime: function(isDesc) {

        if(this.list.length == 0) {
          this.$commonUtil.message.error("没有专题元素，无法进行排序")
          return
        }

        this.$confirm(
          "此操作将根据博客创建时间对所有的专题元素进行升序&降序排列, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        )
          .then(() => {
            let params = new URLSearchParams()
            params.append('subjectUid', this.subjectUid)
            params.append('isDesc', isDesc)
            sortByCreateTime(params).then(response => {
              if (response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                this.getList();
              } else {
                this.$commonUtil.message.error(response.message)
              }
            });
          })
          .catch(() => {
            this.$commonUtil.message.info("已取消批量排序")
          });
      },
      getList() {
        // TODO 这里暂时没有做成分页而是全部显示，考虑到分页后不太好拖拽
        var params = {};
        params.subjectUid = this.subjectUid;
        params.pageSize = 100;
        params.currentPage = 1;
        getSubjectItemList(params).then(response => {
          if(response.code == this.$ECode.SUCCESS) {
            this.list = response.data.records
            this.total = response.total
            this.$nextTick(() => {
              this.setSort()
            })
          }
        })
      },
      strSubstring(str, count) {
        return this.$commonUtil.splitStr(str, count)
      },
      handleDelete: function(row) {
        this.$confirm("此操作将把博客移除该专辑, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            var params = {};
            params.uid = row.uid;
            let subjectItemList = [params]
            deleteBatchSubjectItem(subjectItemList).then(response => {
              if(response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
              } else {
                this.$commonUtil.message.error(response.message)
              }
              this.getList();
            });
          })
          .catch(() => {
            this.$commonUtil.message.info("已取消删除")
          });
      },
      // 跳转到该博客详情
      onClick: function(row) {
        window.open( this.BLOG_WEB_URL + "/#/info?blogUid=" + row.blog.uid);
      },
      setSort() {
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
              if(response.code == this.$ECode.SUCCESS) {
                this.$commonUtil.message.success(response.message)
                this.$router.go(0);
              }
            })
          }
        })
      }
    }
  }
</script>

<style lang="scss">
  .sortable-ghost{
    opacity: .8;
    color: #fff!important;
    background: #42b983!important;
  }
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
