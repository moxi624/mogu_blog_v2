<template>
  <div>
    <aside>
      首先在博客管理中，设置推荐等级。然后通过拖动列表，调节推荐博客的显示的优先级<br/>
      一级推荐：轮播图<br/>
      二级推荐：轮播图右侧<br/>
      三级推荐：特别推荐<br/>
      四级推荐：推荐文章
    </aside>
    <div class="components-container board">
      <Kanban :key="1" :list="list1"  class="kanban first" header-text="一级推荐" @change="change" />
      <Kanban :key="2" :list="list2"  class="kanban second" header-text="二级推荐" @change="change"/>
      <Kanban :key="3" :list="list3"  class="kanban third" header-text="三级推荐" @change="change"/>
      <Kanban :key="4" :list="list4"  class="kanban fourth" header-text="四级推荐" @change="change"/>
    </div>
  </div>
</template>
<script>
  import Kanban from '@/components/Kanban'
  import { getBlogList, editBlog } from "@/api/blog";
  import {editBatchBlog} from "../../api/blog";
  import { Loading } from 'element-ui';
  export default {
    name: 'DragKanbanDemo',
    components: {
      Kanban
    },
    data() {
      return {
        group: 'mission',
        list1: [],
        list2: [],
        list3: [],
        list4: []
      }
    },
    created() {
      this.blogList(1)
      this.blogList(2)
      this.blogList(3)
      this.blogList(4)
    },
    methods: {
      blogList: function(level) {
        var params = {};
        params.levelKeyword = level;
        params.currentPage = 1;
        params.pageSize = 10;
        params.useSort = 1;
        getBlogList(params).then(response => {
          if(response.code == this.$ECode.SUCCESS) {
            switch (level) {
              case 1: {
                this.list1 = response.data.records;
              } break;
              case 2: {
                this.list2 = response.data.records;
              } break;
              case 3: {
                this.list3 = response.data.records;
              } break;
              case 4: {
                this.list4 = response.data.records;
              } break;
            }
          }
        });
      },
      change: function(list) {
        for (var a=0; a<list.length; a++) {
          list[a].sort = list.length - a;
        }
        editBatchBlog(list).then(response => {
          if(response.code == this.$ECode.SUCCESS) {
            this.$commonUtil.message.success(response.message)
          }
        });
      },
    }
  }
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
  .board {
    width: 100%;
    margin-left: 20px;
    display: flex;
    justify-content: space-around;
    flex-direction: row;
    align-items: flex-start;
  }
  .kanban {
    &.first {
      .board-column-header {
        background: #409EFF;
      }
    }

    &.second {
      .board-column-header {
        background: #E6A23C;
      }
    }

    &.third {
      .board-column-header {
        background: #F56C6C;
      }
    }

    &.fourth {
      .board-column-header {
        background: #67C23A;
      }
    }
  }
</style>
