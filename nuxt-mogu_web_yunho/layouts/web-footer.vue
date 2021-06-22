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
import {mapGetters,mapMutations} from 'vuex';
import {getWebConfig} from "~/assets/scripts/index";
export default {
    name:'WebFooter',
    data(){
        return{
            info:{}
        }
    },
    head(){
        console.log("head run");
        console.log(this.info.title);
        return this.$seo(this.info.title,this.info.keyword,this.info.summary,[{}]);
    },
    created(){
        this.getWebConfigInfo();
    },
    methods:{
      //拿到vuex中的写的方法
      ...mapGetters(['getWebConfigData']),
      ...mapMutations(['setWebConfigData']),
      returnTop: function () {
        window.scrollTo(0, 0);
      },
      
      /**
       * 获取网站配置
        */
      getWebConfigInfo: function() {
        let webConfigData = this.$store.getters.getWebConfigData;
        if(webConfigData && webConfigData.data && webConfigData.data.createTime) {
          this.contact = webConfigData.data;
          this.mailto = "mailto:" + this.contact.email;
          this.openComment = webConfigData.data.openComment;
          this.info = webConfigData.data;
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