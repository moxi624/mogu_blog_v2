<!-- 裁剪图片 -->
<template>
  <div class="wrapper">
    <div class="model" v-show="model" @click="model = false">
      <div class="model-show">
        <img :src="modelSrc" alt="">
      </div>
    </div>
    <div class="content">

      <div class="show-info">
        <h2>拖动截图框进行截图</h2>
        <div class="test">
          <vueCropper ref="cropper2"
                      :img="example2.img"
                      :outputSize="example2.size"
                      :outputType="example2.outputType"
                      :info="example2.info"
                      :canScale="example2.canScale"
                      :autoCrop="example2.autoCrop"
                      :autoCropWidth="example2.autoCropWidth"
                      :autoCropHeight="example2.autoCropHeight"
                      :fixed="example2.fixed"
                      :fixedNumber="example2.fixedNumber"
                      :enlarge="4"></vueCropper>
        </div>

        <div style="margin-top: 10px">
          <el-button @click="cropper" type="primary">裁剪</el-button>
          <el-button @click="cancel" type="danger">撤销</el-button>
          <el-button @click="submit" type="success">提交</el-button>
        </div>

        <div style="margin-top: 20px" v-if="picUrl">
          <div>裁剪后的图片</div>
          <img style="width: 200px" :src="picUrl"/>
        </div>

      </div>
    </div>

  </div>
</template>

<script>
  import {cropperPicture} from "@/api/test"
  import { VueCropper } from 'vue-cropper'
  import { getToken } from '@/utils/auth'
  export default {
    name: 'PictureCropper',
    components: {
      VueCropper,
    },
    props: ['modelSrc', 'fileName'],
    data() {
      return {
        model: false,
        crap: false,
        previews: {},
        form: {
          head: ''
        },
        bakPicUrl: "",
        picUrl: "",
        example2: {
          //img的路径自行修改
          img: this.modelSrc,
          info: true,
          size: 1,
          outputType: 'jpeg',
          canScale: true,
          autoCrop: true,
          // 只有自动截图开启 宽度高度才生效
          autoCropWidth: 260,
          autoCropHeight: 140,
          fixed: true,
          // 真实的输出宽高
          infoTrue: true,
          fixedNumber: [13, 7]
        },
        downImg: '#',
      }
    },
    created() {
      this.bakPicUrl = this.modelSrc;
      this.example2.img = this.modelSrc
    },
    methods: {
      //点击裁剪，这一步是可以拿到处理后的地址
      cropper() {
        this.$refs.cropper2.getCropData((data) => {
          this.modelSrc = data
          this.model = false;
          this.picUrl = data;
          //裁剪后的图片显示
          this.example2.img = this.modelSrc;
        })
      },
      // base64转blob
      toBlob(ndata) {
        //ndata为base64格式地址
        let arr = ndata.split(','),
          mime = arr[0].match(/:(.*?);/)[1],
          bstr = atob(arr[1]),
          n = bstr.length,
          u8arr = new Uint8Array(n);
        while (n--) {
          u8arr[n] = bstr.charCodeAt(n);
        }
        let blob = new Blob([u8arr], {
          type: mime
        })
        return new window.File([blob], this.fileName, {type: mime})
      },

      /**
       * 撤销截图
       */
      cancel: function() {
        this.modelSrc = this.bakPicUrl;
        this.example2.img = this.bakPicUrl;
        this.picUrl = "";
        this.$message({
          type: "success",
          message: "撤销成功"
        })
      },
      submit: function() {
        if(this.picUrl == "") {
          this.$message({
            type: "warning",
            message: "请先裁剪图片"
          })
          return;
        }
        //将图片上传服务器中
        let params = new FormData();
        params.append("file", this.toBlob(this.picUrl))
        params.append("token", getToken())
        params.append("source", "picture")
        params.append("userUid", "uid00000000000000000000000000000000")
        params.append("adminUid", "uid00000000000000000000000000000000")
        params.append("projectName", "blog")
        params.append("sortName", "admin")

        cropperPicture(params).then(response => {
          if(response.code == "success") {
            this.picUrl = response.data[0].url;
            this.$emit("cropperSuccess", response.data[0])
          } else {
            this.$message({
              type: "error",
              message: response.data
            })
          }
        });
      }
    },

  }
</script>

<style>
  * {
    margin: 0;
    padding: 0;
  }

  .content {
    margin: auto;
    max-width: 585px;
    margin-bottom: 100px;
  }

  code.language-html {
    padding: 10px 20px;
    margin: 10px 0px;
    display: block;
    background-color: #333;
    color: #fff;
    overflow-x: auto;
    font-family: Consolas, Monaco, Droid, Sans, Mono, Source, Code, Pro, Menlo, Lucida, Sans, Type, Writer, Ubuntu, Mono;
    border-radius: 5px;
    white-space: pre;
  }

  .show-info {
    margin-bottom: 50px;
  }

  .show-info h2 {
    line-height: 50px;
  }

  /*.title, .title:hover, .title-focus, .title:visited {
        color: black;
    }*/

  .title {
    display: block;
    text-decoration: none;
    text-align: center;
    line-height: 1.5;
    margin: 20px 0px;
    background-image: -webkit-linear-gradient(left, #3498db, #f47920 10%, #d71345 20%, #f7acbc 30%, #ffd400 40%, #3498db 50%, #f47920 60%, #d71345 70%, #f7acbc 80%, #ffd400 90%, #3498db);
    color: transparent;
    -webkit-background-clip: text;
    background-size: 200% 100%;
    animation: slide 5s infinite linear;
    font-size: 40px;
  }

  .test {
    height: 285px;
  }

  .model {
    position: fixed;
    z-index: 10;
    width: 100vw;
    height: 100vh;
    overflow: auto;
    top: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.8);
  }

  .model-show {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100vw;
    height: 100vh;
  }

  .model img {
    display: block;
    margin: auto;
    max-width: 80%;
    user-select: none;
    background-position: 0px 0px, 10px 10px;
    background-size: 20px 20px;
    background-image: linear-gradient(45deg, #eee 25%, transparent 25%, transparent 75%, #eee 75%, #eee 100%), linear-gradient(45deg, #eee 25%, white 25%, white 75%, #eee 75%, #eee 100%);
  }

  .c-item {
    display: block;
    padding: 10px 0;
    user-select: none;
  }

  @keyframes slide {
    0% {
      background-position: 0 0;
    }

    100% {
      background-position: -100% 0;
    }
  }

  @media screen and (max-width: 1000px) {
    .content {
      max-width: 90%;
      margin: auto;
    }

    .test {
      height: 400px;
    }
  }
</style>
