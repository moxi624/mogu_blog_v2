<template>
  <div class="app-container">
    <iframe id="myIframe" :src="adminSwaggerUrl" width="100%" height="780px;"></iframe>
  </div>
</template>

<script>

  import {Loading} from "element-ui";

  export default {

    data() {
      return {
        adminSwaggerUrl: process.env.EUREKA_API,
      }
    },
    mounted() {
      let iframe = document.getElementById("myIframe")
      let uploadLoading = Loading.service({
        lock: true,
        text: '正在努力加载中……'
      })
      if (iframe.attachEvent) {
        iframe.attachEvent("onload", function () {
          uploadLoading.close();
        });
      } else {
        iframe.onload = function () {
          uploadLoading.close();
        };
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>

