<template>
  <div class="app-container">
    <textarea id="editor" rows="10" cols="80"></textarea>
  </div>
</template>

<script>
  import { getToken } from '@/utils/auth'
import CKEDITOR from 'CKEDITOR';
export default {
  props: ["content"],
  mounted() {
    var that = this;

    //使用ckeditor替换textarea，设置代码块风格为 zenburn
    CKEDITOR.replace('editor',
      {height: '275px',
        width: '100%',
        toolbar: 'toolbar_Full',
        codeSnippet_theme: 'zenburn',
        filebrowserImageUploadUrl: 'http://localhost:8602/ckeditor/imgUpload?token=' + getToken(),
        filebrowserUploadUrl: 'http://localhost:8602/ckeditor/imgUpload?token=' + getToken(),
      });

    this.editor = CKEDITOR.instances.editor;
    this.editor.setData(this.content); //初始化内容

    // 一秒钟通知子组件，ckeditor中内容改变
    that.editor.on('change', function( event ) {
      that.timeout = setTimeout(function() {
        that.fun();
      }, 1000);
    });

    // that.editor.on('fileUploadRequest', function (evt) {
    //   let xhr = evt.data.fileLoader.xhr;
    //   console.log('文件上传请求：', evt);
    //   xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    //   xhr.setRequestHeader('Cache-Control', 'no-cache');
    //   let accessToken = sessionStorage.getItem('accessToken');
    //   if (accessToken) {
    //     xhr.setRequestHeader('Authorization', `Bearer ${sessionStorage.getItem('accessToken')}`);
    //   }
    //   xhr.withCredentials = true;
    // });


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
