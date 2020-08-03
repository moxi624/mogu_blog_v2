<template name="basics">
	<scroll-view scroll-y class="scrollPage">
		<view class="UCenter-bg">
			<image v-if="userInfo.photoUrl" :src="userInfo.photoUrl" style="border-radius:50%" mode="widthFix"></image>
			<image v-else src="../../static/logo.png" mode="widthFix" @tap="goLogin"></image>
			<view class="text-xl" style="margin-top: 5px;" v-if="userInfo.nickName">
				{{userInfo.nickName}}
			</view>
			<image src="https://raw.githubusercontent.com/weilanwl/ColorUI/master/demo/images/wave.gif" mode="scaleToFill" class="gif-wave"></image>
		</view>
		<view class="padding flex text-center text-grey bg-white shadow-warp">

			<view class="flex flex-sub flex-direction solid-right">
				<navigator class="content" url="/pages/my/about" hover-class="none">
					<view class="margin-top-sm text-xxl">
						<text class="cuIcon-messagefill" style="color: #00B0E8;"></text>
					</view>
					<view class="text-grey">留言</view>
				</navigator>
			</view>

			<view class="flex flex-sub flex-direction solid-right">
				<navigator class="content" url="/pages/my/link" hover-class="none">
					<view class="margin-top-sm text-xxl">
						<text class="cuIcon-link" style="color: #ffaa00;"></text>
					</view>
					<view class="text-grey">友链</view>
				</navigator>
			</view>
			
			<view class="flex flex-sub flex-direction solid-right">
				<navigator class="content" url="/pages/my/about" hover-class="none">
					<view class="margin-top-sm text-xxl">
						<text class="cuIcon-tagfill" style="color: #00ff00;"></text>
					</view>
					<view class="text-grey">标签</view>
				</navigator>
			</view>
			
			<view class="flex flex-sub flex-direction solid-right">
				<navigator class="content" url="/pages/my/about" hover-class="none">
					<view class="margin-top-sm text-xxl">
						<text class="cuIcon-sort" style="color: #ff0000;"></text>
					</view>
					<view class="text-grey">分类</view>
				</navigator>
			</view>
			
		</view>
		<view class="cu-list menu card-menu margin-top-xl margin-bottom-xl shadow-lg radius">
			
			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/myComment" hover-class="none">
					<text class="cuIcon-comment text-black"></text>
					<text class="text-grey">我的评论</text>
				</navigator>
			</view>

			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/myReply" hover-class="none">
					<text class="cuIcon-notice text-green"></text>
					<text class="text-grey">我的回复</text>
				</navigator>
			</view>

			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/myPraise" hover-class="none">
					<text class="cuIcon-favor text-yellow"></text>
					<text class="text-grey">我的点赞</text>
				</navigator>
			</view>
			
			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/applyLink" hover-class="none">
					<text class="cuIcon-link text-blue"></text>
					<text class="text-grey">友链申请</text>
				</navigator>
			</view>
			
			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/about" hover-class="none">
					<text class="cuIcon-github text-grey"></text>
					<text class="text-grey">关于我们</text>
				</navigator>
			</view>
			
			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/myAppreciate" hover-class="none">
					<text class="cuIcon-appreciatefill text-red"></text>
					<text class="text-grey">赞赏支持</text>
				</navigator>
			</view>
			
			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/myFeedback" hover-class="none">
					<text class="cuIcon-writefill text-brown"></text>
					<text class="text-grey">意见反馈</text>
				</navigator>
			</view>

		</view>
		<view class="cu-tabbar-height"></view>
	</scroll-view>
</template>

<script>
	import {getWebConfig} from "../../api/about.js";
	export default {
		name: "my",
		data() {
			return {
				webConfig: {},
				userInfo: {},
			}
		},
		created() {
			this.getWebConfigData()
			this.getUserInfo()
		},
		methods: {
			goLogin() {
				console.log("跳转到登录页面")
				uni.navigateTo({
					url: '/pages/user/login',
				});
			},
			// 获取用户信息
			getUserInfo() {
				this.userInfo = uni.getStorageSync("userInfo")
			},
			getWebConfigData() {
				var that = this
				let params = {}
				getWebConfig(params).then(res =>{
					console.log("获取网站配置", res)
					if(res.code == "success") {
						that.webConfig = res.data;	
					}
				})
			},	
		}
	}
</script>

<style>
	.page {
		height: 100vh;
	}

	.UCenter-bg {
		background-image: url(https://image.weilanwl.com/color2.0/index.jpg);
		background-size: cover;
		height: 400rpx;
		display: flex;
		justify-content: center;
		padding-top: 40rpx;
		overflow: hidden;
		position: relative;
		flex-direction: column;
		align-items: center;
		color: #fff;
		font-weight: 300;
		text-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
	}

	.UCenter-bg text {
		opacity: 0.8;
	}

	.UCenter-bg image {
		width: 200rpx;
		height: 200rpx;
	}

	.UCenter-bg .gif-wave {
		position: absolute;
		width: 100%;
		bottom: 0;
		left: 0;
		z-index: 99;
		mix-blend-mode: screen;
		height: 100rpx;
	}

	map,
	.mapBox {
		left: 0;
		z-index: 99;
		mix-blend-mode: screen;
		height: 100rpx;
	}

	map,
	.mapBox {
		width: 750rpx;
		height: 300rpx;
	}
</style>
