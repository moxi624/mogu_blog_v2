<template>
  <div class="app-container">
    <textarea id="editor" rows="10" cols="80"></textarea>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import CKEDITOR from 'CKEDITOR';
export default {
  props: ["content", "height"],
  mounted() {
    var that = this;

    //使用ckeditor替换textarea，设置代码块风格为 zenburn
    // 上传时，携带token信息，以便于被feign拦截后传递给mogu-admin获取七牛云相关配置
    CKEDITOR.replace('editor',
      {height: this.height,
        width: '100%',
        toolbar: 'toolbar_Full',
        codeSnippet_theme: 'zenburn',
        filebrowserImageUploadUrl: process.env.PICTURE_API + '/ckeditor/imgUpload?token=' + getToken(),
        filebrowserUploadUrl: process.env.PICTURE_API + '/ckeditor/imgUpload?token=' + getToken(),
        pasteUploadFileApi: process.env.PICTURE_API + '/ckeditor/imgUploadByUrl?token=' + getToken(),
      });

    this.editor = CKEDITOR.instances.editor;
    this.editor.setData(this.content); //初始化内容

    // 一秒钟通知子组件，ckeditor中内容改变
    that.editor.on('change', function( event ) {
      that.timeout = setTimeout(function() {
        that.fun();
      }, 1000);
    });

  },
  created() {
    this.textData = this.content;

  },
  watch: {
    content: function() {
      this.textData = this.content;
    }
  },
  data() {
    return {
      editor: null, //编辑器对象
      textData: this.content, //初始化内容
    }
  },
  methods: {
    //获取data
    getData: function() {
      return this.editor.getData();
    },
    setData: function(data) {
      return this.editor.setData(data);
    },
    initData: function() {
      try {
        this.editor.setData("");
      } catch (error) {
        console.log("CKEditor还未加载");
      }
    },
    fun: function() {
      this.$emit("contentChange", "");
    }
  }
}

</script>
