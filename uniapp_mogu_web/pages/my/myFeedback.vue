<template name="basics">
	<view>
		
		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" title="意见反馈"></nav-bar>
		
		<view class="box">
			<view class="cu-bar">
				<view class="action border-title">
					<text class="text-xl text-bold text-blue">反馈须知</text>
					<text class="bg-gradual-blue" style="width:3rem"></text>
				</view>
			</view>
		</view>
		
		<view>
			<view class="action" style="margin-top: 10px;">
				<text class="cuIcon-emoji text-red" style="font-size: 18px; margin-left: 15px; margin-right: 5px;"></text> 如果您对本站有什么想法，可以在这里进行反馈
			</view>
			<view class="action" style="margin-top: 10px;">
				<text class="cuIcon-emoji text-red" style="font-size: 18px; margin-left: 15px; margin-right: 5px;"></text> 或者加入我们的QQ群进行交流
			</view>
		</view>
		
		<view class="box">
			<view class="cu-bar">
				<view class="action border-title">
					<text class="text-xl text-bold text-blue">反馈</text>
					<text class="bg-gradual-blue" style="width:3rem"></text>
				</view>
			</view>
		</view>
				
		<form>
			<view class="cu-form-group margin-top">
				<view class="title">标题</view>
				<input placeholder="请输入标题" v-model="feedback.title"></input>
			</view>
			
			<view class="cu-form-group align-start">
				<view class="title">内容</view>
				<textarea maxlength="-1" v-model="feedback.content" placeholder="请输入反馈内容"></textarea>
			</view>
			
			<view class="cu-bar bg-white solid-top">
				<view class="action">
					图片上传
				</view>
				<view class="action">
					{{imgList.length}}/4
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
					<view class="solids" @tap="ChooseImage" v-if="imgList.length<4">
						<text class='cuIcon-cameraadd'></text>
					</view>
				</view>
			</view>
		</form>
		
		<button class="cu-btn block bg-black margin-sm lg" @click="submit"> 提交 </button>

	</view>
</template>

<script>
	import {addFeedback} from "../../api/user.js"
	export default {
		name: "myFeedback",
		data() {
			return {
				imgList: [],
				feedback: {
					title: "",
					content: "",
				}
			}
		},
		created() {
			
		},
		methods: {
			ChooseImage() {
				uni.chooseImage({
					count: 4, //默认9
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
			DelImg(e) {
				this.imgList.splice(e.currentTarget.dataset.index, 1)
			},
			submit() {
				console.log("提交表单", this.feedback)
				if(this.feedback.title == "" || this.feedback.content == "") {
					uni.showToast({
						icon: "none",
						title: "请先完善表单~",
					})
					return;
				}

				addFeedback(this.feedback).then(res =>{
					console.log(res)
					if(res.code == this.$ECode.SUCCESS) {
						uni.showToast({
							icon: "success",
							title: res.data,
						})
					}
				})
			}
		}
	}
</script>


<style scoped>

</style>
