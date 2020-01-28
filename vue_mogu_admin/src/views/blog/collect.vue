<template>
  <div class="components-container">

    <div class="editor-container">
      <markdown-editor ref="markdownEditor" v-model="content" :language="language" height="500px" />
    </div>

    <div v-html="html" />
  </div>
</template>

<script>
  import MarkdownEditor from '@/components/MarkdownEditor'

  export default {
    name: 'MarkdownDemo',
    components: { MarkdownEditor },
    data() {
      return {
        content: "# 我是测试内容",
        html: '',
        languageTypeList: {
          'en': 'en_US',
          'zh': 'zh_CN',
          'es': 'es_ES'
        }
      }
    },
    computed: {
      language() {
        return this.languageTypeList['zh']
      }
    },
    methods: {
      getHtml() {
        this.html = this.$refs.markdownEditor.getHtml()
        console.log(this.html)
      }
    }
  }
</script>

<style scoped>
  .editor-container{
    margin-bottom: 30px;
  }
</style>
