<template name="basics">
	<view>

		<view class="fixed">
			<cu-custom :isBack="true" bgColor="bg-shadeTop text-white">
				<block slot="content" >关于我们</block>
			</cu-custom>
		</view>

		<scroll-view scroll-y class="DrawerPage">
			
			<view class="box">
				<view class="cu-bar">
					<view class="action border-title">
						<text class="text-xl text-bold text-blue">联系方式</text>
						<text class="bg-gradual-blue" style="width:3rem"></text>
					</view>
				</view>
			</view>
			
			<view>
				<view class="action" style="margin-top: 10px;" v-if="contact.qqGroup">
					<span class="iconfont" style="color: #12aae8; font-size: 18px; margin-left: 15px; margin-right: 5px;">&#xe64f;</span> {{contact.qqGroup}}
				</view>
				<view class="action" style="margin-top: 10px;" v-if="contact.qqNumber">
					<span class="iconfont" style="color: #11aae6; font-size: 18px; margin-left: 15px; margin-right: 5px;">&#xe601;</span> {{contact.qqNumber}}
				</view>
				<view class="action" style="margin-top: 10px;" v-if="contact.email">
					<span class="iconfont" style="color: #eb6841; font-size: 18px; margin-left: 15px; margin-right: 5px;">&#xe647;</span> {{contact.email}}
				</view>
				<view class="action" style="margin-top: 10px;" v-if="contact.github">
					<span class="iconfont" style="font-size: 18px; margin-left: 15px; margin-right: 5px;">&#xe64a;</span> {{contact.github}}
				</view> 
				<view class="action" style="margin-top: 10px;" v-if="contact.gitee">
					<span class="iconfont" style="color: #d81e06;: 18px; margin-left: 15px; margin-right: 5px;">&#xe602;</span> {{contact.gitee}}
				</view>
			</view>
			
			<view class="box">
				<view class="cu-bar">
					<view class="action border-title">
						<text class="text-xl text-bold text-blue">关于我</text>
						<text class="bg-gradual-blue" style="width:3rem"></text>
					</view>
				</view>
			</view>
			
			<jyf-parser class="ck-content margin-sm " :html="userInfo.personResume"></jyf-parser>
			
			<view class="box">
				<view class="cu-bar">
					<view class="action border-title">
						<text class="text-xl text-bold text-blue">留言</text>
						<text class="bg-gradual-blue" style="width:3rem"></text>
					</view>
				</view>
			</view>
			
			<!--以下是评论内容-->
			<view class="cu-list menu-avatar comment solids-top">
				<view class="cu-item" style="margin-top: 10rpx">
					<view class="cu-avatar round" style="background-image:url(https://ossweb-img.qq.com/images/lol/img/champion/Morgana.png);"></view>
					<view class="content">
						<view class="text-grey">莫甘娜</view>
						<view class="text-gray text-content text-df" style="margin-top: 10rpx;">
							凯尔，你被自己的光芒变的盲目。
						</view>
						<view class="margin-top-sm flex justify-between">
							<view class="text-gray text-df">2018年12月4日</view>
							<view>
								<text class="cuIcon-appreciatefill text-red"></text>
								<text class="cuIcon-messagefill text-gray margin-left-sm"></text>
							</view>
						</view>
					</view>
				</view>
				
				<view class="cu-item" style="margin-top: 10rpx">
					<view class="cu-avatar round" style="background-image:url(https://ossweb-img.qq.com/images/lol/img/champion/Morgana.png);"></view>
					<view class="content">
						<view class="text-grey">莫甘娜</view>
						<view class="text-gray text-content text-df" style="margin-top: 10rpx;">
							凯尔，你被自己的光芒变的盲目。
						</view>
						<view class="margin-top-sm flex justify-between">
							<view class="text-gray text-df">2018年12月4日</view>
							<view>
								<text class="cuIcon-appreciatefill text-red"></text>
								<text class="cuIcon-messagefill text-gray margin-left-sm"></text>
							</view>
						</view>
					</view>
				</view>
			</view>
			
			<view class="cu-tabbar-height"></view>
		</scroll-view>
	</view>
</template>

<script>
	import jyfParser from "../../components/jyf-parser/jyf-parser";
	import {getMe, getWebConfig} from "../../api/about.js";
	export default {
		name: "about",
		data() {
			return {
				userInfo: {}, // 关于我
				contact: {}, // 联系方式
			}
		},
		components: {
			jyfParser
		},
		onLoad(option) {

		},
		created() {
			this.getMeInfo()
			this.getContactData()
		},
		methods: {
			getMeInfo() {
				var that = this
				let params = {}
				getMe(params).then(res =>{
					console.log(res)
					if(res.code == "success") {
						that.userInfo = res.data;	
					}
				})
			},
			getContactData() {
				var that = this
				let params = {}
				getWebConfig(params).then(res =>{
					console.log("获取联系方式", res)
					if(res.code == "success") {
						that.contact = res.data;	
					}
				})
			},
		}
	}
</script>


<style>
/* @import '../../static/css/ckeditor.css'; */

/* @import '../../static/iconfont/iconfont.css'; */

image[class*="gif-"] {
    /* border-radius: 6rpx; */
    display: block;
}

.gif-wave{
    /* position: absolute; */
    width: 100%;
    bottom: -2rpx;
    left: 0;
    z-index: 99;
    mix-blend-mode: screen;
    height: 100rpx;
}

page {
    background-image: var(--gradualShadow);
    width: 100vw;
    overflow: hidden;
}

.DrawerPage {
    position: fixed;
    width: 100vw;
    height: 100vh;
    left: 0vw;
    background-color: #f1f1f1;
    transition: all 0.4s;
}

.DrawerPage.show {
    transform: scale(0.9, 0.9);
    left: 85vw;
    box-shadow: 0 0 60rpx rgba(0, 0, 0, 0.2);
    transform-origin: 0;
}

.DrawerPage .cu-bar.tabbar .action button.icon {
    width: 64rpx;
    height: 64rpx;
    line-height: 64rpx;
    margin: 0;
    display: inline-block;
}

.DrawerPage .cu-bar.tabbar .action .cu-avatar {
    margin: 0;
}

.DrawerPage .nav {
    flex: 1;
}

.DrawerPage .nav .cu-item.cur {
    border-bottom: 0;
    position: relative;
}

.DrawerPage .nav .cu-item.cur::after {
    content: "";
    width: 10rpx;
    height: 10rpx;
    background-color: currentColor;
    position: absolute;
    bottom: 10rpx;
    border-radius: 10rpx;
    left: 0;
    right: 0;
    margin: auto;
}

.DrawerPage .cu-bar.tabbar .action {
    flex: initial;
}
</style>
