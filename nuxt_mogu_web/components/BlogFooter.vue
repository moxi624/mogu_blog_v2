<template>
	<footer>
	  <p><a href="http://localhost:9527/" target="_blank">&nbsp;&nbsp;</a> <a href="javasrcipt:void(0);" @click="goIndex()">Copyright 2019&nbsp;{{info.name}}&nbsp;</a>

     <a href="http://www.miitbeian.gov.cn">{{info.recordNum}} </a></p>
	</footer>
</template>

<script>

import { getWebConfig } from "~/api/index";

export default {
  name: 'Footer',
  data () {
  	return {
      info: {}
    };
  },
  created() {
    getWebConfig().then(response => {
      if(response.code == "success") {
        this.info = response.data;
      }
    })
  },
  methods: {
    goIndex() {
      this.$router.push({ path: '/'});
    }
  }
}
</script>

<style>

</style>
