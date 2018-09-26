<template>
  <div class="app-container">
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
      content: null  
    }    
  },
}

</script>
