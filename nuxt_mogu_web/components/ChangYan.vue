<template>
    <div id="SOHUCS" :sid="sid" ></div>  
</template>
<script>
export default {
  props: ["sid"],
  head: {
    script: [{ innerHTML: require('../static/js/changyan'), type: 'text/javascript', charset: 'utf-8'}],
    __dangerouslyDisableSanitizers: ['script']
  },
  mounted() {
  },
  created() {
    console.log(this.sid);
  },
  data() {
    return {
    }    
  },
}
</script>
