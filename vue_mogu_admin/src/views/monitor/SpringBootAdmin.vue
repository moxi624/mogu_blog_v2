<template>
  <div class="app-container">
    <iframe  :src="springBootAddminUrl" width="100%" height="780px;"></iframe>
  </div>
</template>

<script>

  export default {

    data() {
      return {
        springBootAddminUrl: process.env.SPRING_BOOT_ADMIN,
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>

