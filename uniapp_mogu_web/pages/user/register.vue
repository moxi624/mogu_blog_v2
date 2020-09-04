<template name="basics">
	<view>
		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" title="注册"></nav-bar>

		<form>
			<view class="cu-bar bg-white solid-top">
				<view class="action">
					头像
				</view>
			</view>
			
			<view class="cu-form-group">
				<view class="grid col-4 grid-square flex-sub">
					<view class="bg-img" v-for="(item,index) in imgList" :key="index" @tap="ViewImage" :data-url="imgList[index]">
					 <image :src="imgList[index]" mode="aspectFill"></image>
						<view class="cu-tag bg-red" @tap.stop="DelImg" :data-index="index">
							<text class='cuIcon-close'></text>
						</view>
					</view>
					<view class="solids" @tap="ChooseImage" v-if="imgList.length< 1">
						<text class='cuIcon-cameraadd'></text>
					</view>
				</view>
			</view>
			
			<view class="cu-form-group margin-top">
				<view class="title">用户名</view>
				<input placeholder="请输入用户名或邮箱" v-model="registerForm.userName"></input>
			</view>
			
			<view class="cu-form-group margin-top">
				<view class="title">昵称</view>
				<input placeholder="请输入用户名或邮箱" v-model="registerForm.nickName"></input>
			</view>
			
			<view class="cu-form-group margin-top">
				<view class="title">密码</view>
				<input password="" placeholder="请输入密码" v-model="registerForm.password1"></input>
			</view>
			
			<view class="cu-form-group margin-top">
				<view class="title">重复密码</view>
				<input password="" placeholder="请再次输入密码" v-model="registerForm.password2"></input>
			</view>
			
			<view class="cu-form-group margin-top">
				<view class="title">邮箱</view>
				<input placeholder="请输入正确的邮箱用于验证" v-model="registerForm.email"></input>
			</view>
			
			<button class="cu-btn block bg-green margin-sm lg" @click="register"> 提交 </button>
			<button class="cu-btn block bg-grey margin-sm lg" @click="goLogin"> 返回登录 </button>
		</form>

	</view>
</template>

<script>
	import {localRegister} from "../../api/user.js";
	export default {
		name: "login",
		data() {
			return {
				imgList: [],
				registerForm: {
					userName: "",
					nickName: "",
					password1: "",
					password2: "",
					email: "",
				}
			}
		},
		created() {
			
		},
		methods: {
			ChooseImage() {
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album'], //从相册选择
					success: (res) => {
						if (this.imgList.length != 0) {
							this.imgList = this.imgList.concat(res.tempFilePaths)
						} else {
							this.imgList = res.tempFilePaths
						}
					}
				});
			},
			ViewImage(e) {
				uni.previewImage({
					urls: this.imgList,
					current: e.currentTarget.dataset.url
				});
			},
			register() {
				console.log("提交表单", this.registerForm)
				if(this.registerForm.userName == "" || this.registerForm.password1 == "" || this.registerForm.password2 == "" || this.registerForm.password == "") {
					uni.showToast({
						icon: "none",
						title: "请先完善表单~",
					})
					return;
				}

				let passWord = this.registerForm.password1;
				let passWord2 = this.registerForm.password2;
				if(passWord != passWord2) {
					uni.showToast({
						icon: "none",
						title: "两次密码不一致~",
					})
					return;
				}
				var params = {};
				params.userName = this.registerForm.userName;
				params.passWord = this.registerForm.password1;
				params.email = this.registerForm.email;
				params.nickName = this.registerForm.nickName
				localRegister(params).then(response => {
				  if (response.code == this.$ECode.SUCCESS) {
					uni.showToast({
						icon: "success",
						title: response.data,
					})
					// 打开登录页面
					this.goLogin();
				  } else {
					uni.showToast({
						icon: "none",
						title: response.data,
					})
				  }
				});
			},
			goLogin() {
				console.log("跳转到登录页面")
				uni.navigateTo({
					url: '/pages/user/login',
				});
			},
			DelImg(e) {
				this.imgList.splice(e.currentTarget.dataset.index, 1)
			},
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
