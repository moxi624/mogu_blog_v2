<template>
    <div class="guanzhu" id="follow-us" ref="follow">
      <h2 class="hometitle">关注我们 么么哒！</h2>
      <ul>
        <!-- <li class="sina"><a href="/" target="_blank"><span>新浪微博</span>蘑菇博客</a></li>         -->
        <li class="qq" v-if="contact.qqNumber"><a href="tencent://AddContact/?fromId=50&fromSubId=1&subcmd=all&uin=1595833114" target="_blank"><span>QQ号</span>{{contact.qqNumber}}</a></li>
        <li class="email" v-if="contact.email"><a :href="mailto" target="_blank"><span>邮箱帐号</span>{{contact.email}}</a></li>
        <li class="wxgzh" v-if="contact.weChat"><a href="/" target="_blank"><span>微信号</span>{{contact.weChat}}</a></li>
        <!-- <li class="wx"><img src="../../../static/images/wx.jpg"></li> -->
      </ul>
    </div>
</template>

<script>
import { getContact } from "~/api/about";
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
        this.mailto = "mailto:" + this.contact.email;
      }
    });  
  },
  methods: {
  }
};
</script>

<style>
</style>
