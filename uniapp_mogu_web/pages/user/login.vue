<template name="basics">
	<view>

		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" title="登录"></nav-bar>

		<view class="UCenter-bg">
			<image src="/static/logo.png" class="png" mode="widthFix" @tap="goLogin"></image>
			<view class="text-xl" style="margin-top: 5px;">
				蘑菇博客
			</view>
			<image src="../../static/images/wave.gif" mode="scaleToFill" class="gif-wave"></image>
		</view>

		<form>
			<view class="cu-form-group margin-top">
				<view class="title">用户名</view>
				<input placeholder="请输入用户名或邮箱" v-model="loginForm.userName"></input>
			</view>

			<view class="cu-form-group margin-top">
				<view class="title">密码</view>
				<input password="" placeholder="请输入密码" v-model="loginForm.password"></input>
			</view>

			<button class="cu-btn block bg-green margin-sm lg" @click="login"> 提交 </button>
			<button class="cu-btn block bg-grey margin-sm lg" @click="goRegister"> 注册 </button>
			<button class="cu-btn block bg-blue margin-sm lg" open-type="getUserInfo" @getuserinfo="getUserInfo"> QQ登录 <text
				 style="font-weight: bold;">【推荐】</text> </button>

		</form>
	</view>
</template>

<script>
	import {
		localLogin,
		authVerify,
		decryptData,
		code2Session
	} from "../../api/user.js";
	import {
		tokenUtil
	} from '../../utils/token.js'
	export default {
		name: "login",
		data() {
			return {
				loginForm: {
					userName: "",
					password: "",
				}
			}
		},
		onLoad() {

		},
		created() {

		},
		methods: {
			getUserInfo(res) {
				let that = this;
				uni.login({
					provider: 'qq',
					success: function(loginRes) {
						console.log("QQ登录", loginRes)
						let params = {}
						params.encryptDataB64 = res.detail.encryptedData;
						params.jsCode = loginRes.code;
						params.ivB64 = res.detail.iv;
						decryptData(params).then(res => {
							if (res.code == that.$ECode.SUCCESS) {
								let userInfo = res.data
								console.log("登录成功，获取用户信息", userInfo)
								
								// 设置token
								tokenUtil.set(userInfo.validCode)

								// 设置用户信息
								uni.setStorageSync("userInfo", userInfo)

								// 跳转个人中心页
								setTimeout(function() {
									uni.navigateTo({
										url: '/pages/index/index',
									});
								}, 500);

							} else {
								uni.showToast({
									title: res.data,
									icon: "none"
								})
							}
							console.log("解析用户数据", res)
						})
					}
				});

			},
			login() {
				console.log("提交表单", this.loginForm)
				if (this.loginForm.userName == "" || this.loginForm.password == "") {
					uni.showToast({
						icon: "none",
						title: "请先完善表单~",
					})
					return;
				}
				var params = {};
				params.userName = this.loginForm.userName;
				params.passWord = this.loginForm.password;
				params.isRememberMe = 1;
				localLogin(params).then(response => {
					if (response.code == this.$ECode.SUCCESS) {

						let token = response.data;
						authVerify(token).then(res => {
							if (res.code == this.$ECode.SUCCESS) {
								uni.showToast({
									icon: "none",
									title: "登录成功",
								})
								// 设置token
								tokenUtil.set(token)

								console.log("登录成功，获取用户信息", res)

								// 设置用户信息
								uni.setStorageSync("userInfo", res.data)

								// 跳转个人中心页
								setTimeout(function() {
									uni.navigateTo({
										url: '/pages/index/index',
									});
								}, 500);

							}
						});
					} else {
						uni.showToast({
							icon: "none",
							title: response.data,
						})
					}
				});
			},
			goRegister() {
				console.log("跳转到注册页面")
				uni.navigateTo({
					url: '/pages/user/register',
				});
			}
		}
	}
</script>


<style scoped>
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
