<template>
<head>
  <meta charset="utf-8">
  <title>{{info.title}}</title>
  <meta name="keywords" :content="info.keyword">
  <meta name="description" :content="info.summary">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
</template>

<script>
import { getWebConfig } from "../../api/index";

export default {
  name: "Head",
  data() {
    return {
      info: {}
    };
  },
  created() {
    getWebConfig().then(response => {
      if (response.code == this.$ECode.SUCCESS) {
        this.info = response.data;
      }
    });
  },
  methods: {}
};
</script>

<style>
</style>
