<template>
  <div id="app">
    <router-view/>
  </div>

</template>
	
<script>
import Head from './components/Head';
import BlogHead from './components/BlogHead';
import BlogFooter from './components/BlogFooter';

export default {
  name: 'App'
}
</script>

<style>
 @import '../static/css/index.css';
</style>
