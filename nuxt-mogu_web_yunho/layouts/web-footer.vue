<template>
    <div>
          <footer style="margin-bottom:0px;">
            <p>
            <a href="javasrcipt:void(0);" @click="goIndex()">Copyright &nbsp;{{info.name}}&nbsp;</a>
            <a href="http://www.beian.gov.cn/portal/index.do">{{info.recordNum}}</a>
            </p>
        </footer>


    </div>
</template>
<script>
// vuex中有mapState方法，相当于我们能够使用它的getset方法
import {mapMutations} from 'vuex';
import {getWebConfig} from "~/assets/scripts/index";
export default {
    name:'WebFooter',
    data(){
        return{
            info:{}
        }
    },
    created(){
        this.getWebConfigInfo();
    },
    methods:{
      //拿到vuex中的写的方法
      ...mapMutations(['setWebConfigData']),
      returnTop: function () {
        window.scrollTo(0, 0);
      },
      
      /**
       * 获取网站配置
        */
      getWebConfigInfo: function() {
        let webConfigData = this.$store.state.webConfigData;
        console.log("webConfigData.createTime*****"+webConfigData.createTime);
        if(webConfigData.createTime) {
          this.contact = webConfigData;
          this.mailto = "mailto:" + this.contact.email;
          this.openComment = webConfigData.openComment
        } else {
          getWebConfig().then(response => {
            if (response.data.code == this.$ECode.SUCCESS) {
              this.info = response.data.data;
              // 存储在Vuex中
              this.setWebConfigData(response.data.data)
              this.openComment = this.info.openComment
            }
          });
        }
      },
    }
}
</script>