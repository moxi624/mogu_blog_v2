<template>
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

<div class="pagebg sh"></div>
<div class="container">
  <h1 class="t_nav"><span>好咖啡要和朋友一起品尝，好“模板”也要和同样喜欢它的人一起分享。 </span><a href="/" class="n1">网站首页</a><a href="/" class="n2">模板分享</a></h1>


<div class="share">
<ul>
 <li> <div class="shareli"><a href="/" target="_blank"> <i><img src="../../static/images/toppic01.jpg"></i>
      <h2><b>别让这些闹心的套路，毁了你的网页设计!</b></h2>
      <span>喜欢 | 190</span> </a></div> </li>
 <li> <div class="shareli"><a href="/" target="_blank"> <i><img src="../../static/images/toppic02.jpg"></i>
      <h2><b>陌上花开，可缓缓归矣</b></h2>
      <span>喜欢 | 190</span> </a></div> </li>
 <li> <div class="shareli"><a href="/" target="_blank"> <i><img src="../../static/images/banner01.jpg"></i>
      <h2><b>网页中图片属性固定宽度，如何用js改变大小</b></h2>
      <span>喜欢 | 190</span> </a> </div> </li>
 <li> <div class="shareli"><a href="/" target="_blank"> <i><img src="../../static/images/text01.jpg"></i>
      <h2><b>个人博客，属于我的小世界！</b></h2>
      <span>喜欢 | 190</span> </a> </div> </li>
 <li> <div class="shareli"><a href="/" target="_blank"> <i><img src="../../static/images/text02.jpg"></i>
      <h2><b>html5个人博客模板《黑色格调》</b></h2>
      <span>喜欢 | 190</span> </a> </div> </li>
 <li> <div class="shareli"><a href="/" target="_blank"> <i><img src="../../static/images/banner02.jpg"></i>
      <h2><b>帝国cms 首页或者列表无图，不使用默认图片的方法</b></h2>
      <span>喜欢 | 190</span> </a> </div> </li>
       <li> <div class="shareli"><a href="/" target="_blank"> <i><img src="../../static/images/text02.jpg"></i>
      <h2><b>十条设计原则教你学会如何设计网页布局!</b></h2>
      <span>喜欢 | 190</span> </a> </div> </li>
 <li> <div class="shareli"><a href="/" target="_blank"> <i><img src="../../static/images/banner02.jpg"></i>
      <h2><b>html5个人博客模板主题《清雅》</b></h2>
      <span>喜欢 | 190</span> </a> </div> </li>
</ul>
</div>

<div class="pagelist"><a title="Total record">&nbsp;<b>45</b> </a>&nbsp;&nbsp;&nbsp;<b>1</b>&nbsp;<a href="/download/index_2.html">2</a>&nbsp;<a href="/download/index_2.html">下一页</a>&nbsp;<a href="/download/index_2.html">尾页</a></div>

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
  name: 'share',
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
