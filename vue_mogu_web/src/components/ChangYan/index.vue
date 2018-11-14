<template>
    <div id="SOHUCS" :sid="sid" ></div>  
</template>

<script>
import changyan from '../../../static/js/changyan.js'
export default {
  props: ["sid"],
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
