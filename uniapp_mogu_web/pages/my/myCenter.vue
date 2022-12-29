<template name="basics">
	<view>

		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" title="个人资料"></nav-bar>

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
			
			<view class="cu-form-group solid-bottom">
				<view class="title">昵称</view>
				<input placeholder="请输入用户昵称" v-model="userInfo.nickName"></input>
			</view>
			
			<radio-group class="block solid-bottom" @change="genderChange">
				<view class="cu-form-group">
					<view class="title">性别</view>
					<view>
						<radio class='blue radio' :class="userInfo.gender=='1'?'checked':''" :checked="userInfo.gender=='1'?true:false" value="1"></radio> <text class="margin-left-sm">男</text>
						<radio class='red margin-left-sm' :class="userInfo.gender=='0'?'checked':''" :checked="userInfo.gender=='0'?true:false" value="0"></radio> <text class="margin-left-sm">女</text>
					</view>
				</view>
			</radio-group>
			
			<radio-group class="block solid-bottom" @change="RadioChange">
				<view class="cu-form-group">
					<view class="title">评论邮件通知</view>
					<view>
						<radio class='blue radio' :class="userInfo.startEmailNotification=='1'?'checked':''" :checked="userInfo.startEmailNotification=='1'?true:false" value="1"></radio> <text class="margin-left-sm">是</text>
						<radio class='red margin-left-sm' :class="userInfo.startEmailNotification=='0'?'checked':''" :checked="userInfo.startEmailNotification=='0'?true:false" value="0"></radio> <text class="margin-left-sm">否</text>
					</view>
				</view>
			</radio-group>
			
			<view class="cu-form-group">
				<view class="title">邮箱</view>
				<input placeholder="请输入邮箱" v-model="userInfo.email"></input>
			</view>
			
			<view class="cu-form-group ">
				<view class="title">QQ号</view>
				<input placeholder="请输入QQ" v-model="userInfo.qqNumber"></input>
			</view>
			
			<view class="cu-form-group ">
				<view class="title">职业</view>
				<input placeholder="请输入职业" v-model="userInfo.occupation"></input>
			</view>
			
			<view class="cu-form-group align-start">
				<view class="title">个人简介</view>
				<textarea maxlength="-1" v-model="userInfo.summary" placeholder="请输入个人简介"></textarea>
			</view>
			
			<button class="cu-btn block bg-green margin-sm lg" @click="updateUserInfo"> 更新资料 </button>
		</form>

	</view>
</template>

<script>
	import {editUser, authVerify} from "../../api/user.js";
	import {cropperPicture} from "../../api/pictureUpload.js"
	import {tokenUtil} from "../../utils/token.js"
	import { appConfig } from '../../config/config.js'
	export default {
		name: "login",
		data() {
			return {
				imgList: [],
				userInfo: {}
			}
		},
		created() {
			this.getUserInfo()
		},
		methods: {
			genderChange(e) {
				this.userInfo.gender = e.detail.value
				console.log(this.userInfo.gender)
			},
			RadioChange(e) {	
				this.userInfo.startEmailNotification = e.detail.value
				console.log(this.userInfo.gender)
			},
			ChooseImage() {
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album'], //从相册选择
					success: (res) => {
						if (this.imgList.length != 0) {
							// 调用图片上传接口
							this.imgList = this.imgList.concat(res.tempFilePaths)
						} else {
							this.imgList = res.tempFilePaths
						}
						console.log("选择图片成功", this.imgList)
						// 上传图片 
						this.uploadPicture(this.imgList[0])
					}
				});
			},
			ViewImage(e) {
				uni.previewImage({
					urls: this.imgList,
					current: e.currentTarget.dataset.url
				});
			},
			DelImg(e) {
				this.imgList.splice(e.currentTarget.dataset.index, 1)
			},
			getUserInfo() {
				authVerify(tokenUtil.get()).then(response => {
				  console.log("获取用户信息", response)
				  if (response.code == this.$ECode.SUCCESS) {
					let userInfo = response.data
					if(userInfo.photoUrl) {
						this.imgList = [userInfo.photoUrl]
					}
					this.userInfo = userInfo
					uni.setStorageSync("userInfo", userInfo)
				  } else {
					uni.showToast({
						icon: "none",
						title: response.data,
					})
					uni.navigateTo({
						url: '/pages/user/login',
					});
				  }
				});
			},
			updateUserInfo() {
				editUser(this.userInfo).then(response => {
				  console.log("更新用户信息", response)
				  if (response.code == this.$ECode.SUCCESS) {
					uni.showToast({
						icon: "success",
						title: response.data,
					})
					this.getUserInfo()
				  } else {
					uni.showToast({
						icon: "none",
						title: response.message,
					})
				  }
				});
			},
			uploadPicture(pictureUrl) {
				var that = this
				console.log("开始上传服务器")
				 uni.uploadFile({
				  url : appConfig.PICTURE_API + '/file/cropperPicture',
				  filePath: pictureUrl,
				  name: 'file',
				  formData: {
				   'token': tokenUtil.get(),
				   'source': "picture",
				   'platform': "web",
				   'userUid': "uid00000000000000000000000000000000",
				   'adminUid': "uid00000000000000000000000000000000",
				   'projectName': "blog",
				   'sortName': "admin",
				  },
				  success: function (uploadFileRes) {
					  if(uploadFileRes.statusCode == 200) {
						  let data = uploadFileRes.data;
						  let res = JSON.parse(data)
						  console.log("上传成功", res)
						  if(res.code == this.$ECode.SUCCESS) {
							  uni.showToast({
							  	title: "头像上传成功"
							  })
							  if(res.data.length > 0) {
								that.userInfo.avatar = res.data[0].uid
								that.userInfo.photoUrl = res.data[0].url
								console.log("上传成功的用户信息", that.userInfo)  
							  }
						  } else {
							  uni.showToast({
							  	title: res.data,
								icon: "none"
							  })
						  }
					  }
				  },
				  fail: function(err) {
					  console.log("错误信息", err)
				  }
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
