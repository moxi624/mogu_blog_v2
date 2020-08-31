<template>
  <div class="image-model-wrapper">
    <ul class="image-model" v-show="imageModel === 1">
      <li
        class="image-item"
        v-for="(item, index) in fileList"
        :key="index"
        @click="$emit('getImgReviewData', item, true)"
        style="list-style: none;"
      >
        <img class="image" :src="item.fileUrl" :alt="item.fileOldName" />
        <div class="image-name">{{splitStr(item.fileOldName, 10)}}</div>
      </li>
    </ul>
    <div v-show="imageModel === 2">
      <div class="radio">
        排序：
        <el-radio-group v-model="reverse">
          <el-radio :label="true">倒序</el-radio>
          <el-radio :label="false">正序</el-radio>
        </el-radio-group>
      </div>
      <el-timeline class="image-timeline" :reverse="reverse" v-if="imageTimelineData.length > 0">
        <el-timeline-item
          class="image-item"
          v-for="(item, index) in imageTimelineData"
          :key="index"
          :timestamp="item.uploadDate"
          placement="top"
        >
          <img
            class="image"
            v-for="image in item.imageList"
            :key="image.uid"
            :src="image.fileUrl"
            :alt="image.fileOldName"
            @click="$emit('getImgReviewData', image, true)"
          />
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ImageModel',
  props: {
    fileList: Array
  },
  data() {
    return {
      reverse: true
    }
  },
  created() {
    console.log("传递过来的列表", this.fileList)
  },
  computed: {
    imageModel() {
      return this.$store.getters.imageModel
    },
    //  按年-月-日分组排序
    imageTimelineData() {
      let res = []
      //  去重，获取返回的所有日期年-月-日
      console.log("得到的时间", this.fileList)
      let uploadtimeSet = new Set(
        this.fileList.map(item => item.createTime.split(' ')[0])
      )
      let uploadDate = [...uploadtimeSet]
      //  分组
      uploadDate.forEach(element => {
        res.push({
          uploadDate: element,
          imageList: this.fileList.filter(
            item => item.createTime.split(' ')[0] === element
          ) //  过滤
        })
      })
      return res
    }
  },
  methods: {
    splitStr(str, count) {
      return this.$commonUtil.splitStr(str, count)
    },
  }
}
</script>

<style lang="stylus" scoped>
@import '~@/assets/styles/mixins.styl'
.image-model-wrapper
  margin-top 20px
  padding 0 20px
  height calc(100vh - 200px)
  overflow-y auto
  setScrollbar(10px)
  .image-model
    display flex
    flex-wrap wrap
    .image-item
      margin 0 10px 10px 0
      width 150px
      cursor pointer
      .image
        width 150px
        height 150px
      .image-name
        padding-top 4px
        font-size 12px
        text-align center
  .image-timeline
    margin-top 10px
    .image
      margin 0 10px 10px 0
      width 150px
      height 150px
      cursor pointer
</style>
