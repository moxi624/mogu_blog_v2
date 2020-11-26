<template>
  <div id="app">
    <router-view/>
  </div>
</template>
const webApi=process.env.WEB_API
console.log(`WEB_API: ${webApi}`)
<script>
export default {
  name: 'App'
}
</script>

<style>

</style>

