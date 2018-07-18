<template>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>蘑菇博客 - 一个Java开发人员的个人博客网站</title>
<meta name="keywords" content="个人博客,蘑菇博客" />
<meta name="description" content="蘑菇博客 - 一个Java开发人员的个人博客网站" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<!--
	作者：xzx19950624@qq.com
	时间：2018-07-15
	描述：顶部标题
-->
<BlogHead></BlogHead>

<div class="pagebg ab"> </div>
<div class="container">
  <h1 class="t_nav"><span>你，我生命中一个重要的过客，我们之所以是过客，因为你未曾会为我停留。</span><a href="/" class="n1">网站首页</a><a href="/" class="n2">留言</a></h1>
  <div class="news_infos">
    <ul>
      此处安装评论插件
    </ul>
  </div>
  <div class="sidebar">
    <div class="about">
      <p class="avatar"> <img src="../../static/images/avatar.jpg" alt=""> </p>
      <p class="abname">Moxi | 陌溪</p>
      <p class="abposition">Java开发工程师</p>
      <p class="abtext"> 一个95后！一直潜心研究和学习Java后端技术，一边工作一边积累经验</p>
    </div>
    <div class="weixin">
      <h2 class="hometitle">微信关注</h2>
      <ul>
        <img src="../../static/images/wx.jpg">
      </ul>
    </div>
  </div>
</div>

<!--
	作者：xzx19950624@qq.com
	时间：2018-07-15
	描述：博客底部
-->
<BlogFooter></BlogFooter>

<a href="#" class="cd-top">Top</a>
</body>
</html>
</template>

<script>
import BlogHead from '../components/BlogHead';
import BlogFooter from '../components/BlogFooter';
export default {
  name: 'about',
  data () {
  	return {
  		
    };
  },
  components: {
  	//注册组件
  	BlogHead,
  	BlogFooter,
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>


</style>
