<template>
<div class="app-container">
    <iframe id="myIframe"  :src="zipkinUrl" width="100%" height="780px;"></iframe>
  </div>
</template>

<script>

  export default {

    data() {
      return {
        zipkinUrl: process.env.Zipkin_Admin,
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>

