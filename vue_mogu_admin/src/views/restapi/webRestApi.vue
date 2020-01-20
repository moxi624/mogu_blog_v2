<template>
  <div class="app-container">
    <iframe :src="webSwaggerUrl" width="100%" height="750px;"></iframe>
  </div>
</template>

<script>

export default {

  data() {
    return {
      webSwaggerUrl: process.env.WEB_API + "/swagger-ui.html",
    }
  },
  created() {

  },
  methods: {

  }
}
</script>

