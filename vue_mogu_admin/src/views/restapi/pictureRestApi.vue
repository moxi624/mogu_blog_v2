<template>
  <div class="app-container">
    <iframe :src="pictureSwaggerUrl" width="100%" height="750px;"></iframe>
  </div>
</template>

<script>

export default {

  data() {
    return {
      pictureSwaggerUrl: process.env.PICTURE_API + "/swagger-ui.html",
    }
  },
  created() {

  },
  methods: {

  }
}
</script>

