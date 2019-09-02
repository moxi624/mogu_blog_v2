<template>
  <div>
    <a href="javascript:void(0);" @click="returnTop" :class="isVisible?'cd-top cd-is-visible':'cd-top'">Top</a>
  </div>
</template>

<script>
export default {
  name: "CdTop",
  data() {
    return {
      isVisible: false,
    };
  },
  created() {},
  methods: {
    returnTop:function() {
      window.scrollTo(0, 0);
    }
  },
  mounted() {
    var that = this;
    var offset = 300;
    window.addEventListener("scroll", function() {
      let scrollTop = document.documentElement.scrollTop; //当前的的位置
      let scrollHeight = document.documentElement.scrollHeight; //最高的位置
      if(scrollTop > offset) {
        that.isVisible = true;
      } else {
        that.isVisible = false;
      }
    });
  },
};
</script>

<style>
</style>
