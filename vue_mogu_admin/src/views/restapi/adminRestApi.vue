<template>
  <div class="app-container">
    <iframe :src="adminSwaggerUrl" width="100%" height="750px;"></iframe>
  </div>
</template>

<script>

export default {

  data() {
    return {
      adminSwaggerUrl: process.env.ADMIN_API + "/swagger-ui.html",
    }
  },
  created() {

  },
  methods: {

  }
}
</script>

