<template>
  <div class="app-container">
    <iframe src="http://localhost:8080/solr" width="100%" height="650px;"></iframe>
  </div>
</template>

<script>

export default {

  data() {
    return {
      solrServiceUrl: process.env.SOLR_SERVICEE,
    }
  },
  created() {
    console.log(process.env.SOLR_SERVICE);
    this.solrServiceUrl = process.env.SOLR_SERVICEE;
    console.log(this.solrServiceUrl);
  },
  methods: {

  }
}
</script>
