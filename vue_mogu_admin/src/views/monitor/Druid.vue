<template>
  <div class="app-container">
    <iframe  :src="druidAdminUrl" width="100%" height="780px;"></iframe>
  </div>
</template>

<script>

  export default {

    data() {
      return {
        druidAdminUrl: process.env.DRUID_ADMIN,
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>

