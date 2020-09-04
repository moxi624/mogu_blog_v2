<template name="messageBoard">
	<scroll-view scroll-y class="scrollPage">	
		<view class="UCenter-bg">
			<navigator class="content" url="/pages/my/about" hover-class="none">
				<image src="../../static/logo.png" mode="widthFix"></image>
			</navigator>
			<view class="text-xl" style="margin-top: 5px;">
				{{webConfig.name}}
			</view>
			<image src="../../static/images/wave.gif" mode="scaleToFill" class="gif-wave"></image>
		</view>
		
		<view class="padding flex text-center text-grey bg-white shadow-warp">

			<view class="flex flex-sub flex-direction solid-right" v-if="openMobileComment == '1'">
				<navigator class="content" url="/pages/messageBoard/messageBoard" hover-class="none">
					<view class="margin-top-sm text-xxl">
						<text class="cuIcon-messagefill" style="color: #00B0E8;"></text>
					</view>
					<view class="text-grey">留言板</view>
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
				<navigator open-type="reLaunch" class="content" url="/pages/index/index?PageCur=blogClassify" hover-class="other-navigator-hover">
					<view class="margin-top-sm text-xxl">
						<text class="cuIcon-sort" style="color: #ff0000;"></text>
					</view>
					<view class="text-grey">分类</view>
				</navigator>
			</view>
			
			<view class="flex flex-sub flex-direction solid-right">
				<navigator open-type="reLaunch" class="content" url="/pages/index/index?PageCur=blogTag" hover-class="other-navigator-hover">
					<view class="margin-top-sm text-xxl">
						<text class="cuIcon-tagfill" style="color: #00ff00;"></text>
					</view>
					<view class="text-grey">标签</view>
				</navigator>
			</view>
			
		</view>
		<view class="cu-list menu card-menu margin-top-xl margin-bottom-xl shadow-lg radius">
			
			<view class="cu-item arrow">
				<view class="content"  @tap="goUrl('myCenter')" hover-class="none">
					<text class="cuIcon-usefull text-black"></text>
					<text class="text-grey">我的资料</text>
				</view>
			</view>
			
			<view class="cu-item arrow" v-if="openMobileComment == '1'">
				<view class="content" @tap="goUrl('myComment')" hover-class="none">
					<text class="cuIcon-comment text-black"></text>
					<text class="text-grey">我的评论</text>
				</view>
			</view>

			<view class="cu-item arrow" v-if="openMobileComment == '1'">
				<view class="content" @tap="goUrl('myReply')" hover-class="none">
					<text class="cuIcon-notice text-green"></text>
					<text class="text-grey">我的回复</text>
				</view>
			</view>

			<view class="cu-item arrow">
				<view class="content" @tap="goUrl('myPraise')" hover-class="none">
					<text class="cuIcon-favor text-yellow"></text>
					<text class="text-grey">我的点赞</text>
				</view>
			</view>
			
			<view class="cu-item arrow">
				<view class="content" @tap="goUrl('applyLink')" hover-class="none">
					<text class="cuIcon-link text-blue"></text>
					<text class="text-grey">友链申请</text>
				</view>
			</view>
			
			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/about" hover-class="none">
					<text class="cuIcon-github text-grey"></text>
					<text class="text-grey">关于我们</text>
				</navigator>
			</view>
			
			<view class="cu-item arrow" v-if="openMobileAdmiration == '1'">
				<navigator class="content" url="/pages/my/myAppreciate" hover-class="none">
					<text class="cuIcon-appreciatefill text-red"></text>
					<text class="text-grey">赞赏支持</text>
				</navigator>
			</view>
			
			<view class="cu-item arrow">
				<view class="content" @tap="goUrl('myFeedback')" hover-class="none">
					<text class="cuIcon-writefill text-brown"></text>
					<text class="text-grey">意见反馈</text>
				</view>
			</view>
			
			<view class="cu-item arrow">
				<view class="content" @click="logout" hover-class="none">
					<text class="cuIcon-warn text-red"></text>
					<text class="text-grey">退出登录</text>
				</view>
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
				openMobileComment: "0", // 是否开启移动端评论，（1：是，0：否）
				openMobileAdmiration: "0", // 是否开启移动端赞赏，（1：是，0：否）
			}
		},
		created() {
			this.getWebConfigData()
			this.getUserInfo()
		},
		methods: {
			goUrl(url) {
				console.log("判断是否登录", this.userInfo.nickName)
				if(this.userInfo.nickName) {
					console.log("已经登录", this.userInfo)
				} else {
					// 跳转到登录页面
					this.goLogin()
					return
				}
				console.log("开始跳转", url)
				switch(url) {
					case 'myCenter': {
						this.isLogin()
						uni.navigateTo({
							url: '/pages/my/myCenter',
						});
					}break;
					
					case 'myComment': {
						uni.navigateTo({
							url: '/pages/my/myComment',
						});
					}break;
					
					case 'myReply': {
						uni.navigateTo({
							url: '/pages/my/myReply',
						});
					}break;
					
					case 'myPraise': {
						uni.navigateTo({
							url: '/pages/my/myPraise',
						});
					}break;
					
					case 'applyLink': {
						uni.navigateTo({
							url: '/pages/my/applyLink',
						});
					}break;
					
					case 'myFeedback': {
						uni.navigateTo({
							url: '/pages/my/myFeedback',
						});
					}break;
					
				}
			},
			isLogin() {
				if(this.userInfo.nickName) {
					console.log("已经登录", this.userInfo)
				} else {
					// 跳转到登录页面
					this.goLogin()
				}
			},
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
					if(res.code == this.$ECode.SUCCESS) {
						that.webConfig = res.data;
						this.openMobileComment = res.data.openMobileComment
						this.openMobileAdmiration = res.data.openMobileAdmiration
					}
				})
			},
			logout() {
				uni.showModal({
					title: '警告',
					content: '确定要退出登录？',
					cancelText: '再看看',
					confirmText: '再见',
					success: res => {
						if (res.confirm) {
							uni.showToast({
								title: "退出成功"
							})
							uni.removeStorageSync("userInfo")
							this.userInfo = {}
						}
					}
				})
			}
		}
	}
</script>

<style>
	.page {
		height: 100vh;
	}

	.UCenter-bg {
		background-image: url(../../static/images/bg.jpg);
		background-size: cover;
		height: 350rpx;
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
