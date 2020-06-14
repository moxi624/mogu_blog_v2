<template>
  <div
    class="img-review-wrapper"
    v-show="imgReview.visible"
    @click.self="closeImgReview"
  >
    <img class="img-large" ref="rotate" v-if="imgReview.fileurl" :src="imgReview.fileurl" alt />
    <div class="opera-btn-group">
      <a
        class="download-link"
        target="_blank"
        :href="imgReview.fileurl"
        :download="imgReview.filename"
      >
        <i class="opera-icon el-icon-download" title="保存到本地"></i>
      </a>
      <i class="opera-icon el-icon-refresh-right" title="向右旋转" @click="rotateImg"></i>
      <i class="opera-icon el-icon-aim" title="恢复" @click="restore"></i>
      <i class="opera-icon el-icon-zoom-in" title="放大" @click="zoomIn"></i>
      <i class="opera-icon el-icon-zoom-out" title="缩小" @click="zoomOut"></i>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ImgReview',
  props: {
    imgReview: Object
  },
  data() {
    return {
      rotate: 0,
      zoomX: 1,
      zoomY: 1
    }
  },
  methods: {
    //  关闭图片预览
    closeImgReview() {
      this.$emit('getImgReviewData', null, false)
      this.rotate = 0
      this.zoomX = 1
      this.zoomY = 1
      this.$refs.rotate.style.transform = `rotate(${this.rotate}deg) scale(${this.zoomX}, ${this.zoomY})`
    },
    //  旋转图片
    rotateImg() {
      this.rotate += 90
      this.$refs.rotate.style.transform = `rotate(${this.rotate}deg) scale(${this.zoomX}, ${this.zoomY})`
    },
    //  重置图片
    restore() {
      this.rotate = 0
      this.zoomX = 1
      this.zoomY = 1
      this.$refs.rotate.style.transform = `rotate(${this.rotate}deg) scale(${this.zoomX}, ${this.zoomY})`
    },
    //  放大图片
    zoomIn() {
      this.zoomX += 0.2
      this.zoomY += 0.2
      this.$refs.rotate.style.transform = `rotate(${this.rotate}deg) scale(${this.zoomX}, ${this.zoomY})`
    },
    //  缩小图片
    zoomOut() {
      if(this.zoomX > 0.3) {
        this.zoomX -= 0.2
        this.zoomY -= 0.2
        this.$refs.rotate.style.transform = `rotate(${this.rotate}deg) scale(${this.zoomX}, ${this.zoomY})`
      }
    }
  }
}
</script>

<style lang="stylus" scoped>
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
    max-height 80%
    transition:transform 0.5s;
    -webkit-transition:transform 0.5s; /* Safari */
  .opera-btn-group
    background rgba(0, 0, 0, 0.5)
    position fixed
    right 0
    bottom 20px
    left 0
    margin 0 auto
    border-radius 6px
    width 600px
    font-size 30px
    color #fff
    cursor pointer
    .download-link
      color inherit
    .opera-icon
      margin 0 10px
</style>
