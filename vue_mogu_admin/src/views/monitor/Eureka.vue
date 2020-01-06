<template>
  <div class="app-container">
    <iframe  :src="adminSwaggerUrl" width="100%" height="780px;"></iframe>
  </div>
</template>

<script>

  export default {

    data() {
      return {
        adminSwaggerUrl: "http://localhost:8761",
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>

