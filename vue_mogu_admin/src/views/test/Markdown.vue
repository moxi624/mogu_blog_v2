<template>
  <div class="components-container">

    <div class="editor-container">
      <markdown-editor  />
    </div>

  </div>
</template>

<script>
  import MarkdownEditor from '@/components/MarkdownEditor'
  export default {
    name: 'MarkdownDemo',
    components: { MarkdownEditor },
    data() {
      return {

      }
    },
    computed: {

    },
    methods: {

    }
  }
</script>

<style scoped>

</style>
