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
import web from '~/assets/scripts/config/webconst'
export default {
    name:'WebFooter',
    data(){
        return{
            info:{}
        }
    },

    created(){
      // if (process.client) {
        console.log("webfooter created run");
        this.getWebConfigInfo();
      // }
    },
    // head(){
    //     console.log("webfooter head run");
    //     console.log(this.info.title);
    //     // return this.$seo(this.info.title,this.info.keyword,this.info.summary,[{}]);
    //     let mm = this.info.title+"sss";
    //     return{
    //             title:mm,
    //             meta:[{
    //                 hid:"keywords-h",
    //                 name:"keywords",
    //                 content:"webfoot kewords"
    //             },{
    //                 hid:"description",
    //                 name:"description",
    //                 desc:"webfoot desc"
    //             }
    //             ]
    //         }
    // },
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
        // console.log("webConfigData ***web foot**");
        // console.log(webConfigData);
        if(webConfigData && webConfigData.createTime) {
          console.log("not run getconfig regquest");
          // this.contact = webConfigData;
          // this.mailto = "mailto:" + this.contact.email;
          // this.openComment = webConfigData.openComment;
          this.info = webConfigData;
        } else {
          getWebConfig().then(response => {
            if (response.data.code == this.$ECode.SUCCESS) {
              this.info = response.data.data;
              // 存储在Vuex中
              this.$cookies.set(web.key+"-"+web.webConfig,response.data.data);
              this.setWebConfigData(response.data.data)
              // this.openComment = this.info.openComment
            }
            return;
          });
        }
      },
    }
}
</script>