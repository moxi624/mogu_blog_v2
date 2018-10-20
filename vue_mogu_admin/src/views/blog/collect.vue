<template>
  <div class="app-container">
    <el-button @click="getData">点击</el-button>
    <textarea id="editor" rows="10" cols="80"></textarea>
  </div>  
</template>

<script>
import CKEDITOR from 'CKEDITOR';
export default {
  mounted() {
    CKEDITOR.replace('editor', {height: '400px', width: '100%', toolbar: 'toolbar_Full'});
    this.editor = CKEDITOR.instances.editor;    
  },
  data() {
    return {
      editor: null, //编辑器对象
      content: null  
    }    
  },
  methods: {
      getData: function() {
        console.log(this.editor.getData());
      }
  }
}

</script>
