<template>
    <div class="guanzhu" id="follow-us" ref="follow" @click="click">
      <h2 class="hometitle">关注我们 么么哒！</h2>
      <ul>
        <!-- <li class="sina"><a href="/" target="_blank"><span>新浪微博</span>蘑菇博客</a></li>         -->
        <li class="qqGroup"><a href="//shang.qq.com/wpa/qunwpa?idkey=88bc57d77601a3c5ae97fe6d9c0bfa25c2ae166d8f0b9f6da6f7294097be6d08" target="_blank"><span>QQ群</span>950309755</a></li>
        <li class="qq" v-if="contact.qqNumber"><a href="tencent://AddContact/?fromId=50&fromSubId=1&subcmd=all&uin=1595833114" target="_blank"><span>QQ号</span>{{contact.qqNumber}}</a></li>
        <li class="email" v-if="contact.email"><a :href="mailto" target="_blank"><span>邮箱帐号</span>{{contact.email}}</a></li>
        <li class="wxgzh" v-if="contact.weChat"><a href="/" target="_blank"><span>微信号</span>{{contact.weChat}}</a></li>
        <li class="github" v-if="contact.github"><a :href="contact.github" target="_blank"><span>Github</span>{{contact.github}}</a></li>
        <li class="gitee" v-if="contact.gitee"><a :href="contact.gitee" target="_blank"><span>Gitee</span>{{contact.gitee}}</a></li>
        <!-- <li class="wx"><img src="../../../static/images/wx.jpg"></li> -->
      </ul>
    </div>
</template>

<script>
import { getContact } from "../../api/about";
import $ from 'jquery'
export default {
  name: "FollowUs",
  data() {
    return {
      contact: {},
      mailto: ""
    };
  },
  created() {

    getContact().then(response => {
      if (response.code == "success") {
        this.contact = response.data;
        console.log("返回的github", this.contact);
        this.mailto = "mailto:" + this.contact.email;
      }
    });
  },
  methods: {
    click: function() {
      console.log("top", this.$refs.follow.getBoundingClientRect().top)
    }
  }
};
</script>

<style>
</style>
