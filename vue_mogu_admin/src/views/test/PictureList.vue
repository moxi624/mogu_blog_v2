<template>
  <el-row>
    <el-col
      v-for="media in mediaList"
      :key="media.id"
      :xs="24"
      :sm="12"
      :md="12"
      :lg="6"
      :xl="4"
      style="padding: 6px"
    >

      <el-card
        :body-style="{ padding: '0px', textAlign: 'center' }"
        shadow="always"
      >
        <el-checkbox v-model="checked" style="position: absolute;"/>
        <el-image
          :src="media.showUrl"
          style="cursor:pointer"
          fit="scale-down"
          @click="showDetailDialog(media.uid)"
        />
        <div @click="showDetailDialog(media.uid)">
          <span class="media-title">{{ media.name }}</span>
        </div>
        <div style="margin-bottom: 14px;">
          <el-button-group>
            <el-button
              size="mini"
              icon="el-icon-copy-document"
              @click="copyUrl(media.url)"
            />
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-document-copy"
              @click="copyMarkdownUrl(media.name, media.url)"
            />
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-delete"
              @click="handleDelete(media.uid)"
            />
          </el-button-group>
        </div>
      </el-card>

    </el-col>
  </el-row>

</template>

<script>

export default {
  name: 'MediaItem',
  data() {
    return {
      mediaList: [
        {
          uid: '123',
          name: '123.jpg',
          showUrl: 'http://image.moguit.cn/1562679487171.jpg',
          url: 'http://image.moguit.cn/1562679487171.jpg',
          thumbUrl: 'http://image.moguit.cn/1562679487171.jpg',
          suffix: 'http://www.baidu.com'
        },
        {
          uid: '123',
          name: '123.jpg',
          showUrl: 'http://localhost:8600/blog/admin/jpg/2020/3/24/1585014845954.jpg',
          url: 'http://image.moguit.cn/1562679487171.jpg',
          thumbUrl: 'http://image.moguit.cn/1562679487171.jpg',
          suffix: 'http://www.baidu.com'
        },
        {
          uid: '123',
          name: '123.jpg',
          showUrl: 'http://localhost:8600/blog/admin/png/2020/4/8/1586330866122.png',
          url: 'http://image.moguit.cn/1562679487171.jpg',
          thumbUrl: 'http://image.moguit.cn/1562679487171.jpg',
          suffix: 'http://www.baidu.com'
        },
        {
          uid: '123',
          name: '123.jpg',
          showUrl: 'http://image.moguit.cn/1562679487171.jpg',
          url: 'http://image.moguit.cn/1562679487171.jpg',
          thumbUrl: 'http://image.moguit.cn/1562679487171.jpg',
          suffix: 'http://www.baidu.com'
        },
        {
          uid: '123',
          name: '123.jpg',
          showUrl: 'http://image.moguit.cn/1562679487171.jpg',
          url: 'http://image.moguit.cn/1562679487171.jpg',
          thumbUrl: 'http://image.moguit.cn/1562679487171.jpg',
          suffix: 'http://www.baidu.com'
        },
        {
          uid: '123',
          name: '123.jpg',
          showUrl: 'http://image.moguit.cn/1562679487171.jpg',
          url: 'http://image.moguit.cn/1562679487171.jpg',
          thumbUrl: 'http://image.moguit.cn/1562679487171.jpg',
          suffix: 'http://www.baidu.com'
        },
        {
          uid: '123',
          name: '123.jpg',
          showUrl: 'http://image.moguit.cn/1562679487171.jpg',
          url: 'http://image.moguit.cn/1562679487171.jpg',
          thumbUrl: 'http://image.moguit.cn/1562679487171.jpg',
          suffix: 'http://www.baidu.com'
        }
      ]
    }
  },
  methods: {
    copyUrl(url) {
      this.$util.copyText(this.$util.getServerMediaUrl(url))
      this.$util.message.success('复制链接到剪切板成功')
    },
    copyMarkdownUrl(name, url) {
      const realUrl = this.$util.getServerMediaUrl(url)
      const text = '![' + name + '](' + realUrl + ')'
      this.$util.copyText(text)
      this.$util.message.success('复制Markdown格式链接到剪切板成功')
    },
    handleDelete(id) {
      this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'danger'
      })
        .then(() => {

        })
        .catch(() => {})
    },
    showDetailDialog() {
      const url = this.$util.getServerMediaUrl(this.media.url)
      window.open(url, '_blank')
    }
  }
}
</script>

<style scoped>
  .media-title {
    color: #8492a6;
    font-size: 14px;
    padding: 14px;
    display: inline-block;
    white-space: nowrap;
    width: 60%;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .el-image {
    width: 100%;
    height: 160px;
  }
</style>
