# zhouWei-navBar 适用于 uni-app 项目的头部导航组件，支持V3编译、nvue编译

导航栏组件，主要用于头部导航，组件名：zhouWei-navBar

本组件目前兼容微信小程序、H5、5+APP。其他平台没试过

本组件支持模式：
1. 普通固定顶部导航  
2. 透明导航  
3. 透明固定顶部导航 
4. 不固定普通导航 
5. 颜色渐变导航

本组件内置特殊功能：
1. fontColor字体颜色为白色的时候手机状态栏会自动显示白色，否则显示灰色
2. 页面为第一个页面时左上角自动显示返回主页的图标(具体看组件：zhouWei-navBar/index.vue =>页面script)
3. nvue页面必须在当前页面引入本组件才可以使用

### QQ交流群(学习干货多多) 607391225
![QQ交流群](http://qn.kemean.cn//upload/202004/14/15868301778472k7oubi6.png)

# [点击跳转-5年的web前端开源的uni-app快速开发模板-下载看文档](https://ext.dcloud.net.cn/plugin?id=2009)

### 本组件全局配置（位置：zhouWei-navBar/index.vue =>页面script）
1. 主页页面的页面路径
2. 首页页面路径

```
// 主页页面的页面路径
// 关联功能：打开的页面只有一个的时候右上角自动显示返回首页按钮，下面这个数组是排除显示返回首页的页面。
// 主页使用场景：小程序分享出去的页面，用户点击开是分享页面，很多情况下是没有返回首页按钮的
const mainPagePath = ["pages/navList"];
//返回首页的地址
const homePath = "/pages/navList";
```

### 在main.js引入组件，并注册全局组件
```
import zhouWeiNavBar from "@/components/zhouWei-navBar";
Vue.component("nav-bar", zhouWeiNavBar);
```

### 或者在页面script中引入组件，并注册组件（nvue页面必须是这样引入）
```
import navBar from "@/components/zhouWei-navBar";
export default {
    components: {navBar}
}
```

### 案例一
默认特性：左上角有返回箭头，nav-bar导航固定在顶部、标题居中
```
<nav-bar>我的</nav-bar>
```

### 案例二
特性：无返回箭头、字体色为白色、标题左对齐、nav-bar导航透明并不固定在顶部、右边插槽有按钮
```
<nav-bar backState="2000" fontColor="#FFF" :titleCenter="false" type="transparent" title="我的">
	<view class="icon_setUp" slot="right">设置</view>
</nav-bar>
```

### 案例三：颜色渐变导航
特性：颜色渐变导航
```
<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" fontColor="#FFF" title="颜色渐变导航"></nav-bar>
```

### 案例四：颜色渐变导航
特性：颜色渐变导航
```
<nav-bar :bgColor="bgColorList" bgColorAngle="45" fontColor="#FFF" title="颜色渐变导航"></nav-bar>
<!-- bgColor值 -->
bgColorList:[
	{color:"#f37402",scale:"0%"},
	{color:"#0f0",scale:"20%"},
	{color:"#f00",scale:"80%"},
	{color:"#00f",scale:"100%"}
]
```

### 案例五：滑动透明导航
特性：有返回箭头、nav-bar导航透明并固定在顶部、透明状态字体为白色、页面想下滑动nav-bar导航条逐渐变白色、右边插槽有按钮
```
<nav-bar ref="navBar" :scrollTop="scrollTop" transparentFixedFontColor="#FFF" type="transparentFixed" title="我的简历">
	<view class="transparent_fixed_preview" slot="transparentFixedRight" @click="onPreview">预览</view> //透明状态下的按钮
	<view class="preview" slot="right" @click="onPreview">预览</view> //不状态下的按钮
</nav-bar>
```
```
//设置滚动值方法一：
data() {
	return {
		scrollTop:0
	}
},
onPageScroll(e) {
	this.scrollTop = e.scrollTop;
}
//设置滚动值方法二：
onPageScroll (e) {
	this.$refs.navBar.pageScroll(e);
}
```

### 案例六：搜索框|地区选择
特性：无返回箭头、nav-bar导航固定在顶部、地区选择、搜索框
```
<nav-bar backState="2000">
	<view slot="left" class="address_select">深圳市</view>
	<view slot="right" class="search_box" @click="onPageJump('/pages/home/search')">
		<text class="icon_search"></text>
		<text class="tips">搜索目的地/职位等</text>
	</view>
</nav-bar>
```

### 属性
| 名称                        | 类型          | 默认值          | 描述                                               |
| ----------------------------|--------------- | ------------- | ---------------------------------------------------|
| backState                   | String         | 1000          | 返回上一页面按钮,`1000` 显示返回按钮,`2000` 不显示返回按钮,`3000`自定义返回按钮方法，点击返回箭头后会发送一个`backClick`事件|
| home                        | Boolean        | true          | 返回首页按钮（首页地址在源文件里配置）                |
| bgColor                     | String，Array  | #FFF          | 导航背景颜色，值为数组的时候显示渐变颜色，`bgColor="themeBgColor"`的时候会调用全局`class="themeBgColor"`的样式|
| bgColorAngle                | String，Number | 90            | 导航背景颜色渐变角度（`bgColor`为数组生效）              |
| fontColor                   | String         | #000          | 导航字体颜色，（当颜色为白色的时候导航状态栏和图片为白色的）|
| titleCenter                 | Boolean        | true          | 标题`title`居中                                       |
| title                       | String         | --            | 标题`title`值                                         |
| transparentFixedFontColor   | String         | #000          | 导航`type`类型为`transparentFixed`时透明状态下的字体颜色  |
| type                        | String         | fixed         | 导航类型，可选：1.`fixed`固定导航 2.`ordinary`不固定导航 3.`transparent`透明不固定导航 4.`transparentFixed`透明固定导航|
| scrollTop                   | Number         | 0             | 导航`type`类型为`transparentFixed`时页面滚动值（`具体看上面的案例五`）|
| shadow                      | Boolean        | true          | 是否显示底边阴影                                     |

### bgColor数组值为JSON的参数
| 名称                        | 类型            | 默认值         | 描述                                               |
| ----------------------------|--------------- | ------------- | ---------------------------------------------------|
| color                       | String         | --            | 渐变颜色值                                          |
| scale                       | String         | --            | 渐变比例（百分比%）                                  |

### 插槽
| 名称                  | 描述                                                               |
| ----------------------|-------------------------------------------------------------------|   
| left                  | 左插槽                                                             |
| default               | 中间标题插槽（`type`类型为`transparentFixed`时插槽只会穿透到实色背景下）   |
| right                 | 右插槽                                                             |
| transparentFixed      | 导航`type`类型为`transparentFixed`时透明状态下中间插槽                   |
| transparentFixedRight | 导航`type`类型为`transparentFixed`时透明状态下右插槽                     |
| transparentFixedRight | 导航`type`类型为`transparentFixed`时透明状态下右插槽                     |

### 事件（type类型为transparentFixed时可用）
| 名称             | 参数              | 描述                      |
| -----------------|------------------ | --------------------------|
| backClick        | 返回上一页按钮方法 | `backState=3000`时生效    |
