<template>
  <div class="app-container">
    <iframe  :src="rabbitMqAdminUrl" width="100%" height="780px;"></iframe>
  </div>
</template>

<script>

  export default {

    data() {
      return {
        rabbitMqAdminUrl: process.env.RABBIT_MQ_ADMIN,
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>

