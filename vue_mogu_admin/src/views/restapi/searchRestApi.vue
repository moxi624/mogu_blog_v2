<template>
  <div class="app-container">
    <iframe :src="searchSwaggerUrl" width="100%" height="750px;"></iframe>
  </div>
</template>

<script>

export default {

  data() {
    return {
      searchSwaggerUrl: process.env.Search_API + "/swagger-ui.html",
    }
  },
  created() {

  },
  methods: {

  }
}
</script>

