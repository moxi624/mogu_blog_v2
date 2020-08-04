<template name="basics">
	<view>

		<view class="fixed">
			<cu-custom :isBack="true" bgColor="bg-shadeTop text-white">
				<block slot="content"> 登录 </block>
			</cu-custom>
		</view>
		
		<view class="UCenter-bg">
			<image src="/static/logo.png" class="png" mode="widthFix" @tap="goLogin"></image>
			<view class="text-xl" style="margin-top: 5px;">
				蘑菇博客
			</view>
			<image src="https://raw.githubusercontent.com/weilanwl/ColorUI/master/demo/images/wave.gif" mode="scaleToFill" class="gif-wave"></image>
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
			
		</form>

	</view>
</template>

<script>
	import {localLogin, authVerify} from "../../api/user.js";
	import { tokenUtil } from '../../utils/token.js'
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
			login() {
				console.log("提交表单", this.loginForm)
				if(this.loginForm.userName == "" || this.loginForm.password == "") {
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
				  if (response.code == "success") {

					let token = response.data;
					authVerify(token).then(res => {
					  if (res.code == "success") {
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
		background-image: url(https://image.weilanwl.com/color2.0/index.jpg);
		background-size: cover;
		height: 300rpx;
		display: flex;
		justify-content: center;
		/* padding-top: 40rpx; */
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
